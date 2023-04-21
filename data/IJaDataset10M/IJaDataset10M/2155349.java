package net.zycomm.source;

import java.sql.DataTruncation;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Vector;
import net.zycomm.connection.GetConnection;
import net.zycomm.UI.DM;
import net.zycomm.UI.DataMigration;

/**Description : This is used to read the data from database and save table details in vector. 
 * @Version 1.0 
 */
public class GetData {

    /**oldCon is an object for java.sql.Connection.*/
    private java.sql.Connection oldCon;

    /**newCon is an object for java.sql.Connection.*/
    private java.sql.Connection newCon;

    private java.util.Vector colDataVec, tempVec;

    /**pst is an object for java.sql.PreparedStatement.*/
    private java.sql.PreparedStatement pst;

    /**rs is an object for java.sql.ResultSet.*/
    private java.sql.ResultSet rs;

    /**Enumeration interface generates a series of elements, one at a time. Successive calls to the nextElement
	 method return successive elements of the series.*/
    private java.util.Enumeration enm;

    /**rsmd is an object for java.sql.ResultSetMetaData.It maintains the metadata about resultset 
	 * returned from execute query call.*/
    private ResultSetMetaData rsmd;

    /**Varible declaration for maintaining inserted the colname values in a vector.*/
    Vector vec;

    /**Enumeration interface generates a series of elements, one at a time. Successive calls to the nextElement
	 method return successive elements of the series.*/
    Enumeration enm1;

    /**This method is used for getting the old and new connection objects.*/
    public void getConnection() {
        oldCon = DM.getOldCon();
        newCon = DM.getNewCon();
    }

    /** This function is used to get how many no.of rows in a table from the old database.*/
    public int[] getRowNumber(String tablename, String srcseldriver, String desselcdriver) {
        int rowCnt[] = new int[2];
        String query = "";
        int i = 0;
        try {
            if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("com.mysql.jdbc.Driver")) query = "select max(rownum) as mx,min(rownum) as mn from " + tablename; else if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver")) query = "select max(rownum) as mx, min(rownum) as mn from " + tablename; else if (srcseldriver.equalsIgnoreCase("com.mysql.jdbc.Driver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) query = "select count(*) as mx from " + tablename; else if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) query = "select max(rownum) as mx,min(rownum) as mn from " + tablename; else if (srcseldriver.equalsIgnoreCase("com.mysql.jdbc.Driver") && desselcdriver.equalsIgnoreCase("com.mysql.jdbc.Driver")) query = "select count(*) as mx from " + tablename; else if (srcseldriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver") && desselcdriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver")) query = "select max(id) as mx,min(id) as mn from " + tablename; else if (srcseldriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) query = "select max(id) as mx,min(id) as mn from " + tablename;
            pst = oldCon.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) try {
                i = Integer.parseInt(rs.getString("mn"));
            } catch (NumberFormatException nfe) {
                i = 0;
            } catch (java.sql.SQLException slqe) {
                i = 0;
            }
            rowCnt[0] = i;
            try {
                i = Integer.parseInt(rs.getString("mx"));
            } catch (NumberFormatException nfe) {
                i = 0;
                nfe.printStackTrace();
            } catch (java.sql.SQLException slqe) {
                i = 0;
            }
            rowCnt[1] = i;
        } catch (java.sql.SQLException slqe) {
            slqe.printStackTrace();
            rowCnt[0] = 0;
            rowCnt[1] = 0;
        }
        return rowCnt;
    }

    /** This function is used to get the selected table information from the old database.
	 * @param tablename associated with the getTableData method.
	 * @param mn - Minimum Number.
	 * @param mx - Maximum Number.
	 */
    public java.util.Vector getTableData(String tablename, Vector colNames, Vector coldtVec, int mn, int mx, String srcseldriver, String desselcdriver) {
        colDataVec = new Vector();
        String query = "";
        try {
            query = "select ";
            getDataInThread(query, colNames, coldtVec, tablename, mn, mx, srcseldriver, desselcdriver);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return colDataVec;
    }

    /** This function is used for when the data is not migrated correctly in that case the new datbase will 
	 * be shows like previous only.*/
    public boolean rollBack(String newtablename) {
        String query = "";
        int count = 0;
        try {
            query = "select count(*) as no from " + newtablename;
            pst = newCon.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt("no");
            if (count > 0) {
                query = "delete from " + newtablename;
                pst = newCon.prepareStatement(query);
                count = pst.executeUpdate();
                if (count >= 1) return true;
            } else return false;
        } catch (java.sql.SQLException sqle) {
            return false;
        }
        return true;
    }

    /** This function is used to get the selected table information from the old database.*/
    private void getDataInThread(String query, Vector colNames, Vector coldtVec, String tablename, int mn, int mx, String srcseldriver, String desselcdriver) {
        try {
            for (int i = 0; i < colNames.size(); i++) {
                if (i == (colNames.size() - 1)) {
                    query += colNames.get(i).toString();
                } else {
                    query += colNames.get(i).toString() + ",";
                }
            }
            if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("com.mysql.jdbc.Driver")) {
                query += " from " + tablename;
            } else if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver")) query += " from " + tablename + " where rownum >= " + mn + " and rownum <=" + mx; else if (srcseldriver.equalsIgnoreCase("com.mysql.jdbc.Driver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
                query += " from " + tablename + " LIMIT " + mn + "," + mx;
            } else if (srcseldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) query += " from " + tablename + " where rownum >= " + mn + " and rownum <=" + mx; else if (srcseldriver.equalsIgnoreCase("com.mysql.jdbc.Driver") && desselcdriver.equalsIgnoreCase("com.mysql.jdbc.Driver")) query += " from " + tablename + " LIMIT " + mn + "," + mx; else if (srcseldriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver") && desselcdriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver")) query += " from " + tablename + " where id >= " + mn + " and id <=" + mx; else if (srcseldriver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver") && desselcdriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) query += " from " + tablename + " where id >= " + mn + " and id <=" + mx;
            try {
            } catch (Exception e) {
                oldCon = DM.getOldCon();
                e.printStackTrace();
            }
            pst = oldCon.prepareStatement(query);
            System.out.println("query===" + query);
            rs = pst.executeQuery();
            while (rs.next()) {
                tempVec = new Vector();
                for (int i = 0; i < colNames.size(); i++) {
                    if (coldtVec.get(i).toString().equalsIgnoreCase("Timestamp") || coldtVec.get(i).toString().equalsIgnoreCase("Date")) {
                        tempVec.addElement(rs.getDate(colNames.get(i).toString()));
                    } else tempVec.addElement(rs.getString(colNames.get(i).toString()));
                }
                colDataVec.addElement(tempVec);
            }
        } catch (java.sql.DataTruncation dte) {
            dte.printStackTrace();
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (Exception sqle) {
            }
        }
    }

    /**This is for closing the old connection. */
    public void closeOldConn() {
        try {
            oldCon.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**This is for closing the new connection. */
    public void closeNewConn() {
        try {
            oldCon.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.util.HashMap getTableStructure(String tablename) {
        String query = "", str = "";
        java.util.HashMap hm = new java.util.HashMap();
        java.util.Vector tabname, colname, coltype, colConstr;
        try {
            query = "select * from " + tablename;
            Statement st = oldCon.createStatement();
            rs = st.executeQuery(query);
            rsmd = rs.getMetaData();
            colname = new Vector();
            coltype = new Vector();
            colConstr = new Vector();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                str = rsmd.getColumnName(i);
                colname.addElement(str);
                str = rsmd.getColumnTypeName(i);
                if (str.endsWith("identity")) str = str.replaceFirst("identity", "");
                coltype.addElement(str);
                if (rsmd.isNullable(i) == 1) str = "NULL";
                if (rsmd.isNullable(i) == 0) str = "NOT NULL";
                colConstr.addElement(str);
            }
            tabname = new Vector();
            tabname.addElement(tablename);
            hm.put("Old Table Name", tabname);
            hm.put("Old Column Name", colname);
            hm.put("Old Column Type", coltype);
            hm.put("Old Constraints", colConstr);
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return hm;
    }

    /**This function is used to insert the data from old database to new database.*/
    public void insertColData(String tablename, Vector colname, java.util.Vector data) {
        String query = "";
        try {
            String colNames = "";
            enm = colname.elements();
            for (int i = 0; i < colname.size(); i++) {
                if (i == colname.size() - 1) colNames += enm.nextElement().toString(); else colNames += enm.nextElement().toString() + ",";
            }
            enm = data.elements();
            vec = new Vector();
            String values = "";
            System.out.println("query--->" + query);
            while (enm.hasMoreElements()) {
                vec = (Vector) enm.nextElement();
                query = "insert into " + tablename + " (" + colNames + ") values(";
                values = "";
                for (int i = 0; i < vec.size(); i++) {
                    if (i == vec.size() - 1) {
                        values += vec.get(i).toString();
                    } else values += vec.get(i).toString() + ",";
                }
                query += values + ")";
                System.out.println("query--->" + query);
                pst = newCon.prepareStatement(query);
                int i = pst.executeUpdate();
                if (i >= 0) {
                    System.out.println("Excecuted ");
                } else {
                    System.out.println("unable to");
                }
                pst.close();
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception e) {
            }
        }
    }

    public void updateColData(String tablename, String colname, java.util.Vector data) {
        try {
            enm = data.elements();
            while (enm.hasMoreElements()) {
                pst = newCon.prepareStatement("update " + tablename + "set " + colname + " =" + enm.nextElement().toString());
                int p1 = pst.executeUpdate();
            }
        } catch (java.sql.SQLException sqle) {
        }
    }
}
