package integration.pushdata;

import integration.Poolconnection;
import integration.join.Joinoperation;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import scope.Public;
import dbmanager.DBManager;
import dbmanager.GetIdName;

/**
 * @author kubuntu
 *
 */
public abstract class Pushoperation extends Joinoperation implements Operation {

    String tableName;

    String UNIQUE = "unique";

    DBManager integratedDB;

    String connObjId;

    String database = "database";

    public Pushoperation() {
    }

    public DBManager getConnection() {
        String fieldTableId = gin.getId(fieldTable);
        String fieldTablistId = gin.getId(fieldTablist);
        String databaseId = gin.getId(database);
        String query = "SELECT d2.pv FROM " + property_details + " d JOIN (" + property_details + " d1 JOIN " + property_details + " d2 ON ";
        query = query + "(d1.mid = d2.mid)) ON (d1.pv = d.mid) ";
        query = query + "WHERE d.pv = " + tableObj + " AND d.pid = " + fieldTableId + " AND d1.pid = " + fieldTablistId + " AND d2.pid = " + databaseId;
        System.out.println(" QUERY : " + query);
        try {
            ResultSet rsConObj = dbsql.getSelect(query);
            rsConObj.next();
            connObjId = rsConObj.getString("pv");
            rsConObj.close();
        } catch (Exception e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching connection Object.");
            e.printStackTrace();
        }
        Poolconnection pullCon = new Poolconnection();
        pullCon.setDBManager(dbsql);
        pullCon.setPoolconnection(connObjId);
        integratedDB = pullCon.getConnect();
        return integratedDB;
    }

    public void generateProperty() {
        Enumeration<Object> poolIte = poolHash.keys();
        while (poolIte.hasMoreElements()) {
            String key = null;
            try {
                key = poolIte.nextElement().toString();
                String Fieldvalue = ModuleHash.get(key).toString();
                String value = poolHash.get(key).toString();
                fieldHash.put(Fieldvalue, value);
            } catch (Exception e) {
            }
        }
    }

    public void getUniqueKey(String tableName) {
        UNIQUE = gin.getId(UNIQUE);
        String query = "SELECT pv uniquekey FROM " + property_details + " WHERE mid = " + tableObj + " AND pid = " + UNIQUE;
        ResultSet rs = dbsql.getSelect(query);
        try {
            rs.next();
            String unique = rs.getString("uniquekey");
            rs.close();
            query = "SELECT pv uniquekeyvalue, vt FROM " + property_details + " WHERE mid = " + recentTableObject + " AND pid = " + unique;
            rs = dbsql.getSelect(query);
            rs.next();
            String uniquekeyvalue = rs.getString("uniquekeyvalue");
            if (uniquekeyvalue.equals("0")) uniquekeyvalue = rs.getString("vt"); else uniquekeyvalue = gin.getItem(uniquekeyvalue);
            poolHash.put(gin.getItem(unique), uniquekeyvalue);
        } catch (SQLException e) {
            System.out.println(" integration.pushdata:: getUniqueKey:: UNIQUE KEY Not Found...");
            e.printStackTrace();
        }
    }

    public String getTableName() {
        String tableNameId = gin.getId(fieldTableName);
        String query = "SELECT pv FROM " + property_details + " WHERE mid = " + tableObj + " AND pid = " + tableNameId;
        System.out.println(" QUERY : " + query);
        try {
            ResultSet rsTableName = dbsql.getSelect(query);
            rsTableName.next();
            tableName = gin.getItem(rsTableName.getString("pv"));
            rsTableName.close();
        } catch (SQLException e) {
            System.out.println("Exception::Integration:: PoolConnection.java, while fetching TableName ");
            e.printStackTrace();
        }
        return tableName;
    }
}
