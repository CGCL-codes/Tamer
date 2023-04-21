package poolint.integration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import dbmanager.DBManager;
import dbmanager.DBOperation;
import dbmanager.GetIdName;

/**
 * @author Divyesh
 *
 */
public class QueryProcessor {

    private String tableName;

    private String recentTableObject = "";

    String process = "";

    String tableObj = "";

    String fieldTable = "table";

    String fieldTableName = "tablename";

    String fieldTablist = "tablelist";

    String fieldList = "fieldlist";

    String database = "database";

    String field = "field";

    String column = "column";

    String property_details = "property_details";

    String connObjId;

    DBOperation integratedDB;

    Connection conn;

    GetIdName gin;

    Hashtable<Object, Object> fieldHash = new Hashtable<Object, Object>();

    public QueryProcessor() {
        gin = new GetIdName();
    }

    public void setTableObj(String tableObj) {
        this.tableObj = tableObj;
    }

    public void setRecentTableObject(String recentTableObject) {
        this.recentTableObject = recentTableObject;
    }

    public void generateQuery() {
        String tableNameId = gin.getId(fieldTableName);
        String query = "SELECT pv FROM " + property_details + " WHERE mid = " + tableObj + " AND pid = " + tableNameId;
        System.out.println(" QUERY : " + query);
        try {
            ResultSet rsTableName = DBManager.getSelect(query);
            rsTableName.next();
            tableName = gin.getItem(rsTableName.getString("pv"));
            rsTableName.close();
        } catch (SQLException e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching TableName ");
            e.printStackTrace();
        }
        String fieldId = gin.getId(field);
        String columnId = gin.getId(column);
        String fieldListId = gin.getId(fieldList);
        query = "SELECT DISTINCT d6.id,d6.mid, d6.pid, d6.pv, d6.vt FROM " + property_details + " d3 JOIN (" + property_details + " d4 JOIN (" + property_details + " d5 JOIN " + property_details;
        query = query + " d6 ON (d6.mid = d5.pv)) ON (d5.mid = d4.pv)) ON (d3.mid = d4.mid) ";
        query = query + "WHERE d3.mid = " + tableObj + " AND ";
        query = query + "d4.pid = " + fieldListId + " AND d5.pid = " + fieldId + " AND (d6.pid = " + fieldId + " OR d6.pid = " + columnId + ")  order by d6.mid, d6.pid";
        System.out.println(" Query : " + query);
        Hashtable<Object, Object> poolHash = new Hashtable<Object, Object>();
        try {
            ResultSet rsField = DBManager.getSelect(query);
            while (rsField.next()) {
                try {
                    String mid1 = rsField.getString(2);
                    String tableDefinition = gin.getItem(rsField.getString(4)).toLowerCase();
                    rsField.next();
                    String mid2 = rsField.getString(2);
                    if (!mid1.equals(mid2)) continue;
                    String poolDefinition = gin.getItem(rsField.getString(4)).toLowerCase();
                    if (rsField.getString("pid").equals(fieldId)) fieldHash.put(poolDefinition, tableDefinition); else fieldHash.put(tableDefinition, poolDefinition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rsField.close();
        } catch (Exception e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching Fields and Columns ");
            e.printStackTrace();
        }
        try {
            ResultSet rsPoolValue = DBManager.getSelect("SELECT mid, pid, pv, vt FROM " + property_details + " WHERE mid = " + recentTableObject);
            while (rsPoolValue.next()) {
                String pid = gin.getItem(rsPoolValue.getString("pid")).toLowerCase();
                String strPvVt = rsPoolValue.getString("pv");
                if (strPvVt.equalsIgnoreCase("0")) {
                    strPvVt = rsPoolValue.getString("vt");
                } else {
                    strPvVt = gin.getItem(strPvVt);
                }
                poolHash.put(pid, strPvVt);
            }
            rsPoolValue.close();
        } catch (Exception e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching Fields Values from POOL ");
            e.printStackTrace();
        }
        Enumeration<Object> poolIte = fieldHash.keys();
        while (poolIte.hasMoreElements()) {
            String key = null;
            try {
                key = poolIte.nextElement().toString();
                String Fieldvalue = fieldHash.get(key).toString();
                String value = poolHash.get(Fieldvalue).toString();
                fieldHash.put(key, value);
            } catch (Exception e) {
                fieldHash.remove(key);
            }
        }
    }

    public Hashtable<Object, Object> getQueryHash() {
        return fieldHash;
    }

    public void insertQuery() {
        String fieldTableId = gin.getId(fieldTable);
        String fieldTablistId = gin.getId(fieldTablist);
        String databaseId = gin.getId(database);
        String query = "SELECT d2.pv FROM " + property_details + " d JOIN (" + property_details + " d1 JOIN " + property_details + " d2 ON ";
        query = query + "(d1.mid = d2.mid)) ON (d1.pv = d.mid) ";
        query = query + "WHERE d.pv = " + tableObj + " AND d.pid = " + fieldTableId + " AND d1.pid = " + fieldTablistId + " AND d2.pid = " + databaseId;
        System.out.println(" QUERY : " + query);
        try {
            ResultSet rsConObj = DBManager.getSelect(query);
            rsConObj.next();
            connObjId = rsConObj.getString("pv");
            rsConObj.close();
        } catch (Exception e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching connection Object.");
            e.printStackTrace();
        }
        Poolconnection pullCon = new Poolconnection();
        pullCon.setPoolconnection(connObjId);
        integratedDB = pullCon.getConnect();
        if (integratedDB.getInsert(tableName, fieldHash) == 1) {
            System.out.println(" Congratulation Inserted Succesfully with integration ");
        } else {
            System.out.println(" Not Inserted");
        }
        integratedDB.closeConnection();
        integratedDB = null;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        QueryProcessor qrypro = new QueryProcessor();
        qrypro.setRecentTableObject("8460");
        qrypro.setTableObj("8405");
        qrypro.generateQuery();
        qrypro.getQueryHash();
    }
}
