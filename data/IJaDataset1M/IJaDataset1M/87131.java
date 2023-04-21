package smile.stored;

import java.util.*;
import java.sql.*;

/**
 *  s_insertMap is an implements the ProcedureInterface and is a sub-class of
 *  StoredProcedure. This class inserts mappingcode to article relationships in
 *  the database. Copyright 2002 Smile Les motoristes Internet
 *  http://www.smile.fr/ Contact cofax@smile.fr for further
 *  information
 *
 *@author     Smile Les motoristes Internet
 *@created    March 22, 2002
 */
public class s_insertMap extends StoredProcedure implements ProcedureInterface {

    String itemID;

    String mappingCode;

    String rank;

    String timeStart = "01/01/1980";

    String timeEnd = "01/01/1980";

    String pubName;

    String section;

    String pubDate;

    String doesExists;

    /**
     *  Constructor for the s_insertMap object
     */
    public s_insertMap() {
    }

    /**
     *  Description of the Method
     *
     *@param  initData  Description of the Parameter
     *@param  con       Description of the Parameter
     */
    public void init(HashMap initData, Connection con) {
        super.init(initData, con);
        itemID = utils.getString(data, "ITEMID", "");
        mappingCode = utils.getString(data, "MAPPINGCODE", "");
        rank = utils.getString(data, "RANK", "");
    }

    /**
     *  Description of the Method
     *
     *@exception  SQLException  Description of the Exception
     */
    public void checkParams() throws SQLException {
        if (itemID.equals("")) throw new SQLException("ERROR: required field PUBDATE was not sent.");
        if (mappingCode.equals("")) throw new SQLException("ERROR: required field MAPPINGCODE was not sent.");
        if (rank.equals("")) throw new SQLException("ERROR: required field RANK was not sent.");
        if (timeStart.equals("1/1/1980")) {
            timeStart = sdf.format(new java.util.Date());
        }
        if (timeEnd.equals("1/1/1980")) {
            timeEnd = sdf.format(new java.util.Date());
        }
        StringBuffer v_sb = new StringBuffer();
        v_sb.append("SELECT count(*) ").append("FROM tblArticleOrder ").append("WHERE mappingCode = '" + mappingCode + "' ").append("AND itemID = '" + itemID + "'");
        doesExists = execStatementToValue(v_sb.toString());
    }

    /**
     *  Description of the Method
     *
     *@exception  SQLException  Description of the Exception
     */
    public void checkAction() throws SQLException {
    }

    /**
     *  Description of the Method
     *
     *@exception  SQLException  Description of the Exception
     */
    public void executeAction() throws SQLException {
        if (Integer.parseInt(doesExists) > 0) {
            StringBuffer v_sbDelete = new StringBuffer();
            v_sbDelete.append("DELETE FROM tblArticleOrder ").append(" WHERE mappingCode = '" + mappingCode + "' ").append("AND itemID = '" + itemID + "'");
            execStatement(v_sbDelete.toString());
        }
        StringBuffer v_sb1 = new StringBuffer();
        v_sb1.append("SELECT pubDate from tblArticles where itemID=" + itemID);
        pubDate = execStatementToValue(v_sb1.toString());
        StringBuffer v_sb2 = new StringBuffer();
        v_sb2.append("SELECT pubName from tblSections where mappingCode=" + mappingCode);
        pubName = execStatementToValue(v_sb2.toString());
        StringBuffer v_sb3 = new StringBuffer();
        v_sb3.append("SELECT section from tblSections where mappingCode=" + mappingCode);
        section = execStatementToValue(v_sb3.toString());
        StringBuffer v_sbInsert = new StringBuffer();
        v_sbInsert.append("INSERT INTO tblArticleOrder ").append("(itemID, rank, mappingCode, timeStart, timeEnd, ").append("mappingUpdateDate, pubName, section, pubDate) ").append(" VALUES ('" + itemID + "', '" + rank + "', '" + mappingCode + "', '" + timeStart + "', '" + timeEnd + "', '" + sdf.format(new java.util.Date()) + "','" + pubName + "', '" + section + "', '" + pubDate + "')");
        execStatement(v_sbInsert.toString());
        StringBuffer v_sbUpdate = new StringBuffer();
        v_sbUpdate.append("UPDATE tblArticles ").append("SET updateDate = now() ").append("WHERE itemID = '" + itemID + "'");
        execStatement(v_sbUpdate.toString());
        s_updateArticlePreload sp_uap = new s_updateArticlePreload();
        sp_uap.init(data, psConnection);
        sp_uap.setItemID(itemID);
        sp_uap.setTargetDataStore(targetDataStore);
        sp_uap.executeAction();
        execStatementToRS(queryResultOk);
    }
}
