package cn.ekuma.epos.datalogic.define.dao.shard;

import cn.ekuma.epos.db.table.shard.I_StateObjectTable;
import com.openbravo.bean.AppUser;
import com.openbravo.bean.shard.AbstarctStateObject;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;

public class StateObjectDAOHelper {

    public static TableDefinition getTable(I_Session s, String tableName) {
        return new TableDefinition(s, tableName, new Field[] { new Field(I_StateObjectTable.ID, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.CALLTIME, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_StateObjectTable.SUBJECT, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.USERID, Datas.STRING, Formats.STRING, AppUser.class), new Field(I_StateObjectTable.PRIORITY, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.STATE, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.NEXTTIME, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_StateObjectTable.RELATIONID, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.RELATIONMAN, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.ENDTIME, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_StateObjectTable.LASTMODIFIED, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_StateObjectTable.USERMAN, Datas.STRING, Formats.STRING), new Field(I_StateObjectTable.SUBJECTDESC, Datas.STRING, Formats.STRING) }, new int[] { 0 });
    }

    public static void writeInsertValues(DataWrite dp, AbstarctStateObject obj, int start) throws BasicException {
        dp.setString(start, obj.getId());
        dp.setTimestamp(start + 1, obj.getCallTime());
        dp.setString(start + 2, obj.getSubject());
        dp.setString(start + 3, obj.getUserId());
        dp.setInt(start + 4, obj.getPriority());
        dp.setString(start + 5, obj.getState());
        dp.setTimestamp(start + 6, obj.getNextTime());
        dp.setString(start + 7, obj.getRelationID());
        dp.setString(start + 8, obj.getRelationMan());
        dp.setTimestamp(start + 9, obj.getEndTime());
        dp.setTimestamp(start + 10, obj.getLastModified());
        dp.setString(start + 11, obj.getUserMan());
        dp.setString(start + 12, obj.getSubjectDesc());
    }

    public static void readValues(DataRead dr, AbstarctStateObject obj, int start) throws BasicException {
        obj.setKey(dr.getString(start));
        obj.setCallTime(dr.getTimestamp(start + 1));
        obj.setSubject(dr.getString(start + 2));
        obj.setUserId(dr.getString(start + 3));
        obj.setPriority(dr.getInt(start + 4));
        obj.setState(dr.getString(start + 5));
        obj.setNextTime(dr.getTimestamp(start + 6));
        obj.setRelationID(dr.getString(start + 7));
        obj.setRelationMan(dr.getString(start + 8));
        obj.setEndTime(dr.getTimestamp(start + 9));
        obj.setLastModified(dr.getTimestamp(start + 10));
        obj.setUserMan(dr.getString(start + 11));
        obj.setSubjectDesc(dr.getString(start + 12));
    }
}
