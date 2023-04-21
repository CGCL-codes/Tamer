package com.solido.objectkitchen.data;

import com.solido.objectkitchen.log.*;
import com.solido.objectkitchen.config.*;
import com.solido.objectkitchen.*;
import java.io.*;
import java.util.*;
import java.security.*;

public class DataHandler implements ServiceProvider, DataProvider {

    private boolean isrunning;

    private String datalocation;

    private Hashtable database_hash;

    private List database_list;

    private LogProvider log;

    public void setLogProvider(LogProvider plog) {
        log = plog;
    }

    public StorageEngine getDatabase(String name) {
        if (database_hash.containsKey(name)) {
            return (StorageEngine) database_hash.get(name);
        }
        return null;
    }

    private boolean validateDataLocation() {
        File ftest = new File(datalocation);
        if (!ftest.exists()) {
            System.err.println(" ! Datalocation does not exist");
            return false;
        }
        if (!ftest.canRead()) {
            System.err.println(" ! Datalocation cannot be read");
            return false;
        }
        if (!ftest.canWrite()) {
            System.err.println(" ! Datalocation cannot be modyfied");
            return false;
        }
        return true;
    }

    private StorageEngine getStorageEngine(String data) {
        StorageEngine stor = null;
        if (!data.endsWith(File.separator)) {
            data = data + File.separator;
        }
        File ftest = new File(data);
        System.out.print("   * Database \"" + ftest.getName() + "\" ");
        try {
            StringBuffer buf = new StringBuffer();
            BufferedReader bufin = new BufferedReader(new FileReader(data + "database.conf"));
            String str = bufin.readLine();
            while (str != null) {
                buf.append(str);
                buf.append("\n");
                str = bufin.readLine();
            }
            bufin.close();
            ConfigurationParser parser = new ConfigurationParser();
            ConfigurationSection config = parser.parseString(buf.toString());
            if (config != null) {
                if (config.containsKey("CLASS")) {
                    try {
                        Class cl = Class.forName(config.getValue("CLASS"));
                        stor = (StorageEngine) cl.newInstance();
                        stor.setLocation(data);
                        if (stor.verifyDataIntegrity()) {
                            if (stor.initializeData()) {
                                System.out.print("T:<" + stor.getNumberOfTables(new TransactionId(0)) + "> ");
                                System.out.print("S:<" + stor.getDataSize() + "> OK");
                            } else {
                                System.out.print("...Could not start database!");
                                stor = null;
                            }
                        } else {
                            System.out.print("...Could not recover database!");
                            stor = null;
                        }
                    } catch (Exception e) {
                        System.out.print("...Could not instantiate class");
                        stor = null;
                    }
                } else {
                    System.out.print("...Error");
                }
            } else {
                System.out.print("...Error");
            }
        } catch (Exception e) {
            System.out.print("...Error");
        }
        System.out.println();
        return stor;
    }

    private boolean verifyDataIntegrity() {
        database_hash = new Hashtable();
        database_list = new ArrayList();
        try {
            System.out.println(" + Mounting databases in " + datalocation);
            File dloc = new File(datalocation);
            File[] dblist = dloc.listFiles();
            for (int i = 0; i < dblist.length; i++) {
                File ftest = dblist[i];
                if (!ftest.isDirectory()) {
                    System.err.println(" ! Ordinary file found in data location");
                    continue;
                }
                if (!ftest.canRead()) {
                    System.err.println(" ! Non readable folder found in data location");
                    continue;
                }
                if (!ftest.canWrite()) {
                    System.err.println(" ! Non writeable folder found in data location");
                    continue;
                }
                StorageEngine stor = getStorageEngine(ftest.getPath());
                if (stor != null) {
                    database_hash.put(ftest.getName().toLowerCase(), stor);
                    database_list.add(stor);
                }
            }
        } catch (Exception e) {
            System.err.println(" ! An unkown error occured while looking through the data folder");
            return false;
        }
        if (!database_hash.containsKey("system")) {
            try {
                File ftest = new File(datalocation + "system");
                if (!ftest.exists()) {
                    if (createSystemDatabase(datalocation)) {
                        StorageEngine stor = getStorageEngine(datalocation + "system/");
                        if (stor != null) {
                            database_hash.put(ftest.getName().toLowerCase(), stor);
                            database_list.add(stor);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        if (database_hash.containsKey("system")) {
            return true;
        } else {
            System.err.println(" ! The system database did not exist, or could not be started");
            return false;
        }
    }

    public boolean createSystemDatabase(String baselocation) {
        try {
            File ftest = new File(baselocation + "system/");
            if (ftest.mkdir()) {
                PrintWriter bufout = new PrintWriter(new FileWriter(baselocation + "system/database.conf"));
                bufout.println("CLASS   =   com.solido.objectkitchen.data.SimpleTextEngine");
                bufout.flush();
                bufout.close();
                StorageEngine stor = getStorageEngine(baselocation + "system/");
                if (stor != null) {
                    TransactionId tid = new TransactionId(0);
                    if (stor.createTable("tbl_user", tid)) {
                        stor.addColumn("id", DataElement.GUID, "tbl_user", tid);
                        stor.addColumn("username", DataElement.TEXT, "tbl_user", tid);
                        stor.addColumn("password", DataElement.TEXT, "tbl_user", tid);
                        stor.addColumn("tcreated", DataElement.TIMESTAMP, "tbl_user", tid);
                    }
                    if (stor.createTable("tbl_access", tid)) {
                        stor.addColumn("id", DataElement.GUID, "tbl_access", tid);
                        stor.addColumn("username", DataElement.TEXT, "tbl_access", tid);
                        stor.addColumn("database", DataElement.TEXT, "tbl_access", tid);
                        stor.addColumn("table", DataElement.TEXT, "tbl_access", tid);
                        stor.addColumn("read", DataElement.BOOLEAN, "tbl_access", tid);
                        stor.addColumn("write", DataElement.BOOLEAN, "tbl_access", tid);
                        stor.addColumn("tcreated", DataElement.TIMESTAMP, "tbl_access", tid);
                    }
                    if (stor.createTable("tbl_ipacl", tid)) {
                        stor.addColumn("id", DataElement.GUID, "tbl_ipacl", tid);
                        stor.addColumn("username", DataElement.TEXT, "tbl_ipacl", tid);
                        stor.addColumn("database", DataElement.TEXT, "tbl_ipacl", tid);
                        stor.addColumn("adr", DataElement.TEXT, "tbl_ipacl", tid);
                        stor.addColumn("mask", DataElement.TEXT, "tbl_ipacl", tid);
                        stor.addColumn("allow", DataElement.BOOLEAN, "tbl_ipacl", tid);
                        stor.addColumn("sort_order", DataElement.INTEGER, "tbl_ipacl", tid);
                        stor.addColumn("tcreated", DataElement.TIMESTAMP, "tbl_ipacl", tid);
                    }
                    if (stor.createTable("tbl_stat", tid)) {
                        stor.addColumn("database", DataElement.TEXT, "tbl_stat", tid);
                        stor.addColumn("year", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("month", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("day", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("hour", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("milliseconds", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("cycles", DataElement.BIGINT, "tbl_stat", tid);
                        stor.addColumn("queries", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("reads", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("writes", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("tablelocks", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("rowlocks", DataElement.INTEGER, "tbl_stat", tid);
                        stor.addColumn("updates", DataElement.INTEGER, "tbl_stat", tid);
                    }
                    DataRow row = stor.createRow("tbl_user");
                    DataElement root_user_id = DataElement.createGUID();
                    row.setData("id", root_user_id);
                    row.setData("username", new DataElement(DataElement.TEXT, "root"));
                    row.setData("password", DataElement.createMD5Sum(DataElement.TEXT, "password"));
                    row.setData("tcreated", new DataElement(DataElement.TIMESTAMP, System.currentTimeMillis()));
                    stor.addRow(row, tid);
                    row = stor.createRow("tbl_access");
                    row.setData("id", DataElement.createGUID());
                    row.setData("username", new DataElement(DataElement.TEXT, "root"));
                    row.setData("database", new DataElement(DataElement.TEXT, "*"));
                    row.setData("table", new DataElement(DataElement.TEXT, "*"));
                    row.setData("read", new DataElement(DataElement.BOOLEAN, true));
                    row.setData("write", new DataElement(DataElement.BOOLEAN, true));
                    row.setData("tcreated", new DataElement(DataElement.TIMESTAMP, System.currentTimeMillis()));
                    stor.addRow(row, tid);
                    row = stor.createRow("tbl_ipacl");
                    row.setData("id", DataElement.createGUID());
                    row.setData("username", new DataElement(DataElement.TEXT, "root"));
                    row.setData("database", new DataElement(DataElement.TEXT, "*"));
                    row.setData("adr", new DataElement(DataElement.TEXT, "127.0.0.1"));
                    row.setData("mask", new DataElement(DataElement.TEXT, "255.255.255.255"));
                    row.setData("allow", new DataElement(DataElement.BOOLEAN, true));
                    row.setData("sort_order", new DataElement(DataElement.INTEGER, 0));
                    row.setData("tcreated", new DataElement(DataElement.TIMESTAMP, System.currentTimeMillis()));
                    stor.addRow(row, tid);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean acceptConfiguration(ConfigurationSection config) {
        isrunning = false;
        if (config.containsKey("DATALOCATION")) {
            datalocation = config.getValue("DATALOCATION");
        } else {
            System.err.println(" ! Datahandler missing data location");
            return false;
        }
        if (!datalocation.endsWith(File.separator)) {
            datalocation = datalocation + File.separator;
        }
        if (!validateDataLocation()) return false;
        if (!verifyDataIntegrity()) return false;
        return true;
    }

    public void start() {
        isrunning = true;
    }

    public void stop() {
        isrunning = false;
    }

    public boolean isRunning() {
        return isrunning;
    }
}
