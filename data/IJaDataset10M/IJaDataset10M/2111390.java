package net;

import java.io.IOException;
import java.sql.*;
import org.eclipse.jface.preference.PreferenceStore;

public class ReqProMSAccess {

    Object[][] tiedot = null;

    Statement rStmt;

    private int priority = 15;

    private int status = 21;

    public boolean Init(String urli) {
        boolean failure = false;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            final PreferenceStore preferenceStore = merlin.MerlinMain.preferenceStore;
            try {
                preferenceStore.load();
            } catch (IOException e) {
            }
            String reqaddr = preferenceStore.getString("RequisitePro");
            String filename = "\\\\" + reqaddr + "\\" + urli + "\\" + urli + ".mdb";
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            database += filename.trim() + ";SYSTEMDB=\\\\" + reqaddr + "\\" + urli + "\\System.mdw" + ";DriverID=22;READONLY=true}";
            Connection con = java.sql.DriverManager.getConnection(database, "", "");
            rStmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            failure = true;
            e.printStackTrace();
        }
        return failure;
    }

    public int[] getInts(String table, String column, String column2, int index) {
        int[] items = new int[getSize(table, column2, index)];
        int i = 0;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT * FROM " + table + " WHERE " + column2 + "=" + index + " ORDER BY Tag,ID");
            while (rs.next()) {
                items[i] = rs.getInt(column);
                i++;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return items;
    }

    public int[] getRqIds(String table, String column, String column2, int index, int[] featIds) {
        int[] items = new int[getSize(table, column2, index)];
        int i = 0;
        ResultSet rs;
        try {
            for (int j = 0; j < featIds.length; j++) {
                rs = rStmt.executeQuery("SELECT * FROM RqRequirements WHERE ID IN(" + "SELECT ToID FROM RqToRelationships WHERE RequirementID = " + featIds[j] + ")");
                while (rs.next()) {
                    items[i] = rs.getInt(column);
                    i++;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return items;
    }

    public String[][] getAllStrings(String table, int id) {
        int i = 0, j = 0, k = 0;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT * FROM " + table + " WHERE ID=" + id);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            int tableSize = 1;
            String[][] items = new String[tableSize + 2][colCount];
            items[0][0] = Integer.toString(tableSize);
            items[0][1] = Integer.toString(colCount);
            i++;
            for (k = 0; k < colCount; k++) {
                items[1][k] = rsmd.getColumnName(k + 1);
            }
            i++;
            while (rs.next()) {
                for (j = 0; j < colCount; j++) {
                    if (rs.getString(j + 1) == null) items[i][j] = rs.getString(j + 1); else {
                        if (rs.getString(j + 1) == null) items[i][j] = rs.getString(j + 1); else items[i][j] = "'" + rs.getString(j + 1).replaceAll("\'", "\''") + "'";
                    }
                }
                i++;
            }
            return items;
        } catch (Exception x) {
            x.printStackTrace();
            return null;
        }
    }

    public int maxInt(String table, String column, String column2, int value) {
        int max = 0;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT * FROM " + table + " WHERE " + column + " = " + value);
            while (rs.next()) {
                if (rs.getInt(column2) > max) max = rs.getInt(column2);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return max;
    }

    public int getSize(String table, String column, int index) {
        int size = 0;
        try {
            ResultSet rcount = rStmt.executeQuery("SELECT COUNT(*) AS A FROM " + table + " WHERE " + column + " = " + index);
            rcount.next();
            size = rcount.getInt("A");
        } catch (Exception x) {
            x.printStackTrace();
        }
        return size;
    }

    public ResultSet customQuery(String query) {
        ResultSet results;
        try {
            results = rStmt.executeQuery(query);
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object[][] getRequirementsTable() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, RequirementText, LongTagNumber, ID, VersionNumber " + "FROM RqRequirements Where RequirementPrefix LIKE 'SR%' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][5];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1);
                    tiedot[index][1] = rs.getString(2);
                    tiedot[index][2] = rs.getString(3);
                    tiedot[index][3] = rs.getString(5);
                    tiedot[index][4] = rs.getString(6);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public String getRequirementPriority(String reqID) {
        String[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ItemText FROM RqUserDefinedListItems WHERE ItemID = (SELECT ListItemID " + "FROM RqUserDefinedListValues WHERE FieldID = " + priority + " AND RequirementID LIKE '" + reqID + "')");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            if (koko != 0) {
                tiedot = new String[koko];
                rs.beforeFirst();
                int index = 0;
                while (rs.next()) {
                    if (index < koko) {
                        tiedot[index] = rs.getString(1);
                        index++;
                    } else break;
                }
            } else {
                tiedot = new String[1];
                tiedot[0] = "status";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String priority = tiedot[0] + "";
        return priority;
    }

    public String getRequirementStatus(String reqID) {
        String[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ItemText FROM RqUserDefinedListItems WHERE ItemID = (SELECT ListItemID " + "FROM RqUserDefinedListValues WHERE FieldID = " + status + " AND RequirementID LIKE '" + reqID + "')");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            if (koko != 0) {
                tiedot = new String[koko];
                rs.beforeFirst();
                int index = 0;
                while (rs.next()) {
                    if (index < koko) {
                        tiedot[index] = rs.getString(1);
                        index++;
                    } else break;
                }
            } else {
                tiedot = new String[1];
                tiedot[0] = "status";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String status = tiedot[0] + "";
        return status;
    }

    public String[] getSRRequirements() {
        String[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName FROM RqRequirements " + "WHERE RequirementPrefix LIKE 'SR%' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getString(1) + ": " + rs.getString(2);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getStrqRequirements() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, RequirementTypeID " + "FROM RqRequirements " + "WHERE RequirementTypeID LIKE '4' AND LongTagNumber NOT LIKE '000000000%0%' " + "ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][3];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(1);
                    tiedot[index][2] = rs.getString(3);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getFeatRequirements() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, ID " + "FROM RqRequirements WHERE RequirementTypeID = 1 " + "AND LongTagNumber NOT LIKE '000000000%0%'");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][3];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(1);
                    tiedot[index][2] = rs.getString(3);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getTracedFeatRequirements(String link) {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, ID " + "FROM RqRequirements WHERE RequirementTypeID = 1 " + "AND LongTagNumber NOT LIKE '000000000%0%' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][2];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(3);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet rs2 = rStmt.executeQuery("Select RequirementID from RqToRelationships WHERE ToID LIKE '" + link + "'");
            int koko2;
            if (rs2.last()) koko2 = rs2.getRow(); else koko2 = 0;
            tiedot = new Object[koko2][2];
            rs2.beforeFirst();
            int index2 = 0;
            while (rs2.next()) {
                if (index2 < koko2) {
                    tiedot[index2][1] = rs2.getString(1);
                    index2++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getSrRequirements() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, ID " + "FROM RqRequirements WHERE RequirementTypeID = 3 AND LongTagNumber NOT LIKE '000000000%0%' " + "ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][2];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getSrReqChild(String i) {
        Object[][] tiedot = null;
        String iiro = i;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, ID FROM RqRequirements " + "WHERE RequirementPrefix LIKE '" + iiro + ".%' AND RequirementPrefix NOT LIKE '" + iiro + ".%.%' " + "ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][2];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public String[] getRequirementNames(String prefix) {
        String[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix,RequirementName FROM RqRequirements " + "WHERE RequirementPrefix LIKE '" + prefix + "' " + "ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getString(1) + " " + rs.getString(2);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public int[] getRequirementToID(String prefix) {
        int[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ToID FROM RqToRelationships " + "WHERE RequirementID = (SELECT ID FROM RqRequirements WHERE RequirementPrefix LIKE '" + prefix + "') " + "ORDER BY ToID");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new int[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getInt(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public int[] getRequirementToID2(int iidee) {
        int[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT DISTINCT ToID FROM RqToRelationships " + "WHERE RequirementID = " + iidee + " ");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new int[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getInt(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public String[] getRequirementName(int iidee, String prefix) {
        String[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT DISTINCT RequirementPrefix, RequirementName FROM RqRequirements " + "WHERE ID = " + iidee + " " + "AND RequirementPrefix LIKE '" + prefix + "%' ");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            if (koko != 0) {
                tiedot = new String[koko];
                rs.beforeFirst();
                int index = 0;
                while (rs.next()) {
                    if (index < koko) {
                        tiedot[index] = rs.getString(1) + " " + rs.getString(2);
                        index++;
                    } else break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getTracedReqs(String iidee, String type) {
        int[] tiedot = null;
        Object[][] tiedot2 = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ToID FROM RqToRelationships " + "WHERE RequirementID LIKE '" + iidee + "' " + "AND Type LIKE '" + type + "' ORDER BY ToID");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new int[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getInt(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tiedot2 = new Object[tiedot.length][3];
        for (int j = 0; j < tiedot.length; j++) {
            Object[][] voivoi = getReqs(tiedot[j]);
            for (int e = 0; e < voivoi.length; e++) {
                tiedot2[j][0] = voivoi[e][0];
                tiedot2[j][1] = voivoi[e][1];
                tiedot2[j][2] = voivoi[e][2];
            }
        }
        return tiedot2;
    }

    public Object[][] getReqs(int iidee) {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementPrefix, RequirementName, ID, RequirementTypeID, VersionNumber " + "FROM RqRequirements WHERE ID = " + iidee + " ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][5];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1) + " " + rs.getString(2);
                    tiedot[index][1] = rs.getString(1);
                    tiedot[index][2] = rs.getString(3);
                    tiedot[index][3] = rs.getString(4);
                    tiedot[index][4] = rs.getString(5);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getRootDiscussions() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID, Subject FROM RqDiscussions WHERE ParentID = 0");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][2];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1);
                    tiedot[index][1] = rs.getString(2);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public Object[][] getChild(Integer i) {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID,Subject FROM RqDiscussions WHERE RootID = " + i + "");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][2];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1);
                    tiedot[index][1] = rs.getString(2);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public String getDiscussionText(String iiro) {
        String tiedot[] = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT Message FROM RqDiscussions WHERE Subject LIKE '" + iiro + "'");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[0] = rs.getString(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String message = ("" + tiedot[0] + "");
        return message;
    }

    public String getReqText(String prefix) {
        String tiedot[] = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT RequirementText FROM RqRequirements " + "WHERE RequirementPrefix = '" + prefix + "'");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[0] = rs.getString(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String message = ("" + tiedot[0] + "");
        return message;
    }

    public String[] getTracedFileNames(String iiro) {
        String tiedot[] = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT DISTINCT FileName FROM TraceabilityLinks WHERE ReqPrefix = '" + iiro + "' ORDER BY FileName");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getString(1) + ".java";
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    public String[] getFileNames() {
        String tiedot[] = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT FieldValue FROM RqUserDefinedFieldValues");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new String[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getString(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    /**
	 * Returns certain requirement ID
	 * @param reqPrefix
	 */
    public int getReqID(String reqPrefix) {
        int[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID FROM RqRequirements WHERE RequirementPrefix LIKE '" + reqPrefix + "' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new int[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getInt(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int tieto = tiedot[0];
        return tieto;
    }

    /**
	 * Returns SR requirement
	 */
    public Object[][] getRequirementData(int id) {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID, VersionNumber, VersionReason, RequirementText, " + "RequirementPrefix, RequirementName " + "FROM RqRequirements WHERE ID=" + id + "");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][7];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1);
                    tiedot[index][1] = rs.getString(2);
                    if (tiedot[index][1].equals("1.0000")) tiedot[index][2] = "Requirement created."; else tiedot[index][2] = rs.getString(3);
                    tiedot[index][3] = rs.getString(4);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    /**
	 * Returns a set of SR requirements' attributes as a String[][] table
	 */
    public Object[][] getReqsData() {
        Object[][] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID, VersionNumber, VersionReason, RequirementText, " + "RequirementPrefix, RequirementName " + "FROM RqRequirements WHERE RequirementPrefix LIKE 'SR%' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new Object[koko][6];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index][0] = rs.getString(1);
                    tiedot[index][1] = rs.getString(2);
                    if (tiedot[index][1].equals("1.0000")) tiedot[index][2] = "Requirement created."; else tiedot[index][2] = rs.getString(3);
                    tiedot[index][3] = rs.getString(4);
                    tiedot[index][4] = rs.getString(5);
                    tiedot[index][5] = rs.getString(6);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }

    /**
	 * Return appropriate req ID's in an int [] table
	 * @param String reqPrefix
	 */
    public int[] getAllSRReqIDs() {
        int[] tiedot = null;
        try {
            ResultSet rs = rStmt.executeQuery("SELECT ID FROM RqRequirements WHERE RequirementPrefix LIKE 'SR*' ORDER BY LongTagNumber");
            int koko;
            if (rs.last()) koko = rs.getRow(); else koko = 0;
            tiedot = new int[koko];
            rs.beforeFirst();
            int index = 0;
            while (rs.next()) {
                if (index < koko) {
                    tiedot[index] = rs.getInt(1);
                    index++;
                } else break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tiedot;
    }
}
