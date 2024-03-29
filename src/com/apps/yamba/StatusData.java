package com.apps.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatusData {
	private static final String TAG = StatusData.class.getSimpleName();
	private static final String GET_ALL_ORDER_BY = DbHelper.C_CREATED_AT + " DESC";
	private static final String[] MAX_CREATED_AT_COLUMNS = { "max("
		+ DbHelper.C_CREATED_AT + ")" };
	private static final String[] DB_TEXT_COLUMNS = { DbHelper.C_TEXT };
	private final DbHelper dbHelper;

	public StatusData(Context context) {
		dbHelper = new DbHelper(context);
		Log.i(TAG, "initialized data");
	}

	public void close() {
		this.dbHelper.close();
	}

	public void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(DbHelper.TABLE, null, null);
	}

	public int deleteStatuses(String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase(); // 
		try {
			return db.delete(DbHelper.TABLE, selection, selectionArgs); // 
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			db.close(); // 
		}
	}

	public long getLatestStatusCreatedAtTime() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(DbHelper.TABLE, MAX_CREATED_AT_COLUMNS, null, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}

	public String getStatusTextById(long id) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			Cursor cursor = db.query(DbHelper.TABLE, DB_TEXT_COLUMNS, DbHelper.C_ID + "=" + id, null, null, null, null);
			try {
				return cursor.moveToNext() ? cursor.getString(0) : null;
			} finally {
				cursor.close();
			}
		} finally {
			db.close();
		}
	}

	public Cursor getStatusUpdates() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(DbHelper.TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
	}

	public long insertOrIgnore(ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			return db.insertWithOnConflict(DbHelper.TABLE, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}

	public int updateStatuses(ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			return db.update(DbHelper.TABLE, values, selection, selectionArgs);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			db.close();
		}
	}

	public Cursor queryStatuses(String[] projection, String selection,
			String[] selectionArgs, String groupBy, String having, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase(); // 
		try {
			return db.query(DbHelper.TABLE, projection, selection, selectionArgs, groupBy,
					having, sortOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}

}
