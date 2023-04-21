package com.project.exam.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.project.exam.bean.performanceBean;

public class InsertService {

    private DBHelper dbOpenHelper;

    public InsertService(Context context) {
        dbOpenHelper = new DBHelper(context);
    }

    /**
	 * ��ȡ���м�¼
	 */
    public List<performanceBean> getAllInfo() {
        List<performanceBean> list = new ArrayList<performanceBean>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        int i = 0;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from performance where code = '1' order by result asc ", null);
            while (cursor.moveToNext() && i < 3) {
                performanceBean item = new performanceBean();
                item.code = cursor.getInt(cursor.getColumnIndex("code"));
                item.name = cursor.getString(cursor.getColumnIndex("name"));
                item.result = cursor.getInt(cursor.getColumnIndex("result"));
                list.add(item);
                i++;
            }
            i = 0;
            cursor = db.rawQuery("select * from performance where code = '2' order by result asc ", null);
            while (cursor.moveToNext() && i < 3) {
                performanceBean item = new performanceBean();
                item.code = cursor.getInt(cursor.getColumnIndex("code"));
                item.name = cursor.getString(cursor.getColumnIndex("name"));
                item.result = cursor.getInt(cursor.getColumnIndex("result"));
                list.add(item);
                i++;
            }
            i = 0;
            cursor = db.rawQuery("select * from performance where code = '3' order by result asc ", null);
            while (cursor.moveToNext() && i < 3) {
                performanceBean item = new performanceBean();
                item.code = cursor.getInt(cursor.getColumnIndex("code"));
                item.name = cursor.getString(cursor.getColumnIndex("name"));
                item.result = cursor.getInt(cursor.getColumnIndex("result"));
                list.add(item);
                i++;
            }
            cursor.close();
            db.close();
        }
        return list;
    }

    /**
	 * ��ӹ��α��¼�¼
	 */
    public <integer> boolean insertSubjectTable(integer code, String sub_name) {
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            if (db.isOpen()) {
                db.execSQL("insert into subject(code,sub_name) values(?,?)", new Object[] { code, sub_name });
                db.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * ��ӳɼ����¼�¼
	 */
    public <integer> boolean insertPerformanceTable(performanceBean personPerformance) {
        try {
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            if (db.isOpen()) {
                db.execSQL("insert into performance(result, name, code) values(?,?,?)", new Object[] { personPerformance.result, personPerformance.name, personPerformance.code });
                db.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
