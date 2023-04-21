package ddbserver.connections;

import ddbserver.common.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Roar
 */
public class TableManager {

    private HashMap<String, Table> tables;

    Properties properties;

    private static TableManager instance = null;

    public static synchronized TableManager getInstance() {
        if (instance == null) {
            instance = new TableManager();
        }
        return instance;
    }

    private TableManager() {
        tables = new LinkedHashMap<String, Table>();
        try {
            properties = new Properties();
            if (!new File("tablelist.properties").exists()) {
                new File("tablelist.properties").createNewFile();
                properties.load(new FileInputStream("tablelist.properties"));
                properties.setProperty("tablenum", "0");
                properties.store(new FileOutputStream("tablelist.properties"), null);
            }
            properties.load(new FileInputStream("tablelist.properties"));
            int tablenum = Integer.parseInt(properties.getProperty("tablenum", "0"));
            for (int i = 0; i < tablenum; i++) {
                String tablename = properties.getProperty("table" + i + "tablename");
                Table table = new Table(tablename);
                int colnum = Integer.parseInt(properties.getProperty("table" + i + "colnum"));
                for (int j = 0; j < colnum; j++) {
                    String colname = properties.getProperty("table" + i + "colname" + j);
                    String coltype = properties.getProperty("table" + i + "coltype" + j);
                    table.insertCols(colname, coltype);
                }
                tables.put(tablename, table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProperties() {
        try {
            properties.load(new FileInputStream("tablelist.properties"));
            properties.setProperty("tablenum", "" + getSize());
            Set<String> keys = tables.keySet();
            int i = 0;
            for (String key : keys) {
                Table table = tables.get(key);
                properties.setProperty("table" + i + "tablename", key);
                int colnum = table.getSize();
                properties.setProperty("table" + i + "colnum", "" + colnum);
                Set<String> colnames = table.getCols();
                int j = 0;
                for (String colname : colnames) {
                    String coltype = table.getType(colname);
                    properties.setProperty("table" + i + "colname" + j, colname);
                    properties.setProperty("table" + i + "coltype" + j, coltype);
                    j++;
                }
                if (table.getKey() != null) {
                    properties.setProperty("table" + i + "key", key);
                }
                i++;
            }
            properties.store(new FileOutputStream("tablelist.properties"), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTable(String tableName, Table table) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, table);
            saveProperties();
        }
    }

    public void removeTable(String tableName) {
        if (tables.containsKey(tableName)) {
            tables.remove(tableName);
            saveProperties();
        }
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public Set<String> getAllTableName() {
        return tables.keySet();
    }

    public int getSize() {
        return tables.size();
    }
}
