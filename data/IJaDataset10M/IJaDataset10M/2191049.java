package com.west.ConnectionDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataBase {

    public DatabaseMetaData datbaseMetaData(Connection connection) {
        DatabaseMetaData dmd = null;
        try {
            dmd = connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dmd;
    }

    public int sizeTable(Connection connection, DatabaseMetaData dmd) {
        int compt = 0;
        ResultSet tables;
        try {
            tables = dmd.getTables(connection.getCatalog(), null, "%", null);
            while (tables.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compt;
    }

    public Object[] getTableDB(Connection connection, int size, DatabaseMetaData dmd) {
        ResultSet tables;
        int i = 0;
        Object valeurTable[] = new Object[size];
        try {
            tables = dmd.getTables(connection.getCatalog(), null, "%", null);
            while (tables.next()) {
                for (int j = 0; j < tables.getMetaData().getColumnCount(); j++) {
                    String nomColonne = tables.getMetaData().getColumnName(j + 1);
                    if (nomColonne == "TABLE_NAME") {
                        valeurTable[i] = tables.getObject(j + 1);
                        i = i + 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valeurTable;
    }

    public Object[] columnNameForeignKey(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        ResultSet clefs;
        ResultSetMetaData rsmd;
        int i = 0;
        int compt = 0;
        try {
            clefs = dmd.getImportedKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Object[] fkColumnName = new Object[compt];
        try {
            clefs = dmd.getImportedKeys(connection.getCatalog(), null, nomDeLaTable);
            rsmd = clefs.getMetaData();
            while (clefs.next()) {
                for (int k = 0; k < rsmd.getColumnCount(); k++) {
                    String col = rsmd.getColumnName(k + 1);
                    if (col == "FKCOLUMN_NAME") {
                        fkColumnName[i] = clefs.getObject(k + 1).toString();
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fkColumnName;
    }

    public Boolean isForeignKeyObject(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        ResultSet clefs;
        int compt = 0;
        Boolean val = false;
        try {
            clefs = dmd.getImportedKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (compt != 0) {
            val = true;
        }
        return val;
    }

    public Boolean isForeignKeySet(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        ResultSet clefs;
        int compt = 0;
        Boolean val = false;
        try {
            clefs = dmd.getExportedKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (compt != 0) {
            val = true;
        }
        return val;
    }

    public Boolean isPrameryKey(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        ResultSet clefs;
        int compt = 0;
        Boolean val = false;
        try {
            clefs = dmd.getPrimaryKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (compt != 0) {
            val = true;
        }
        return val;
    }

    public Integer nbrPrameryKey(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        ResultSet clefs;
        int compt = 0;
        try {
            clefs = dmd.getPrimaryKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                compt++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compt;
    }

    public String prameryKey(Connection connection, DatabaseMetaData dmd, String nomDeLaTable) {
        String prameryKey = null;
        ResultSet clefs;
        try {
            clefs = dmd.getPrimaryKeys(connection.getCatalog(), null, nomDeLaTable);
            while (clefs.next()) {
                prameryKey = clefs.getString("COLUMN_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prameryKey;
    }
}
