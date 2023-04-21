package com.szxys.mhub.subsystem.virtual;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import com.szxys.mhub.base.manager.MHubDBHelper;

/**
 * 监护参数DB
 * 
 * @author 张丹
 * 
 */
public class MTPSDB {

    private final MHubDBHelper dbHelper;

    public MTPSDB() {
        dbHelper = new MHubDBHelper();
    }

    /**
	 * 保存监护参数方法
	 * 
	 * @param parameters
	 */
    public void save(MTPSEntity parameters) {
        String tempSql = "insert into mb_uonitoring_parameters(userID,appID,IsChange,DownTime)values(" + parameters.userID + "," + parameters.appID + "," + parameters.isChange + "," + "'" + parameters.downTime + "')";
        this.dbHelper.open(true);
        this.dbHelper.execSQL(tempSql);
    }

    /**
	 * 查询是否有更新的监护参数数据(当值为1的时候代表web端有更新监护参数、个业务子系统可以进行下载并保持到数据库)
	 * 
	 * @return
	 */
    public List<MTPSEntity> cachMTPSEntity(int IsChange) {
        List<MTPSEntity> mtpsList = new ArrayList<MTPSEntity>();
        String tempSql = "select * FROM mb_uonitoring_parameters where IsChange=" + IsChange;
        dbHelper.open(false);
        Cursor cursor = dbHelper.query(tempSql);
        if (cursor == null) {
            return mtpsList;
        }
        while (!cursor.isAfterLast()) {
            MTPSEntity parameters = new MTPSEntity();
            parameters.userID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("userID")));
            parameters.appID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("appID")));
            int iDownTime = cursor.getColumnIndex("DownTime");
            parameters.downTime = cursor.getString(iDownTime);
            parameters.isChange = Integer.parseInt(cursor.getString(cursor.getColumnIndex("IsChange")));
            mtpsList.add(parameters);
            cursor.moveToNext();
        }
        cursor.close();
        return mtpsList;
    }

    /**
	 * 根据业务ID返回数据有数据更新
	 * @param appID
	 * @return
	 */
    public boolean cachMTPSByAppID(int appID) {
        String tempSql = "SELECT * FROM mb_uonitoring_parameters where appID=" + appID;
        dbHelper.open(false);
        Cursor cursor = dbHelper.query(tempSql);
        if (cursor == null) return false; else {
            return true;
        }
    }

    /**
	 * 下载数据完成后由业务系统调用此方法将下载时间进行更新、 以及将IsChange更新成0,以供下次去web端获取数据
	 * 
	 * @param IsChange
	 * @param appID
	 * @param downTime
	 */
    public void UpdateDownTimeByAppID(int IsChange, int appID, String downTime) {
        String tempSql = "UPDATE mb_uonitoring_parameters SET IsChange=" + IsChange + "," + "downTime=" + "'" + downTime + "'" + "WHERE appID=" + appID;
        this.dbHelper.open(true);
        this.dbHelper.execSQL(tempSql);
    }

    /**
	 * 如果有业务ID返回、则表示有数据、更新他的状态
	 * 
	 * @param appID
	 */
    public void UpdateIsChangeByAppID(int appID) {
        String tempSql = "UPDATE mb_uonitoring_parameters SET IsChange=1 WHERE appID=" + appID;
        this.dbHelper.open(true);
        this.dbHelper.execSQL(tempSql);
    }
}
