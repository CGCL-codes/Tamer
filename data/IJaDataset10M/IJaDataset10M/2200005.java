package com.daffodilwoods.daffodildb.server.datasystem.indexsystem;

import java.util.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.PersistentDatabase;
import com.daffodilwoods.daffodildb.server.serversystem.ServerSystem;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.*;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.versioninfo.VersionHandlerFactory;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.versioninfo.VersionHandler;

public class TempIndexSystem implements _DataSystem {

    /**
   * Maintains lists of all database which are Currently in use having
   * mapping with Database name
   */
    private HashMap databaseMap;

    private String daffodilHome;

    public TempIndexSystem(String daffodilHome0) {
        databaseMap = new HashMap();
        daffodilHome = daffodilHome0;
    }

    public synchronized _Database getDatabase(String databaseName) throws DException {
        _IndexDatabase indexDatabase = (_IndexDatabase) databaseMap.get(databaseName);
        if (indexDatabase != null) return indexDatabase;
        String path = daffodilHome + File.separator + databaseName + File.separator + DatabaseConstants.TEMPDATABASE + ".ddb";
        long size = 2 * 1024 * 1024;
        int clusterSize = Integer.parseInt(DatabaseConstants.TEMPDATABASECLUSTERSIZE.trim());
        File raf_file = new File(path);
        RandomAccessFile file = null;
        File ff = null;
        try {
            ff = new File(path);
            if (ff.exists()) {
                boolean isDeleted = ff.delete();
                if (!isDeleted) throw new DException("DSE5522", new Object[] {});
            }
            file = new RandomAccessFile(raf_file, "rw");
            file.setLength(size);
            file.seek(0);
            file.write(new byte[clusterSize]);
        } catch (IOException ex) {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException iex) {
                }
                file = null;
            }
            throw new DException("DSE0", new Object[] { ex.getMessage() });
        }
        path = daffodilHome + File.separator + databaseName + File.separator + databaseName + ".ddb";
        RandomAccessFile file1 = null;
        try {
            file1 = new RandomAccessFile(new File(path), "r");
        } catch (IOException ex1) {
            throw new DException("DSE0", new Object[] { ex1.getMessage() });
        }
        VersionHandler versionHandler = VersionHandlerFactory.getVersionHandler(file1);
        try {
            file1.close();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        versionHandler.setClusterSize(clusterSize);
        DRandomAccessFileUpto3_2 dRandomAccessFile = versionHandler.getDRandomAccessFile(file, size, path, 10, false, null, clusterSize);
        indexDatabase = new TempIndexDatabase(new PersistentDatabase(dRandomAccessFile, DatabaseConstants.TEMPDATABASE, false, versionHandler, daffodilHome));
        databaseMap.put(databaseName, indexDatabase);
        return indexDatabase;
    }

    public void createDatabase(String databaseName, Properties prop) throws DException {
    }

    public void dropDatabase(String databaseURL) throws DException {
        DeleteFiles(databaseURL);
        databaseMap.remove(databaseURL);
    }

    public void removeDatabase(String databaseName) throws DException {
        DeleteFiles(databaseName);
        databaseMap.remove(databaseName);
    }

    private void DeleteFiles(String databaseName) {
        try {
            _IndexDatabase database = (_IndexDatabase) databaseMap.get(databaseName);
            if (database != null) ((PersistentDatabase) ((IndexDatabase) database).getUnderLyingDatabase()).closeFile();
            String path = daffodilHome + File.separator + databaseName + File.separator + DatabaseConstants.TEMPDATABASE + ".ddb";
            File f = new File(path);
            f.delete();
        } catch (DException ex) {
        }
    }

    protected void finalize() {
        try {
            Iterator iterator = databaseMap.keySet().iterator();
            String databaseName;
            while (iterator.hasNext()) {
                databaseName = (String) iterator.next();
                dropDatabase(databaseName);
            }
        } catch (DException ex) {
        }
    }

    public synchronized void closeAllDatabases() throws DException {
        Iterator iterator = databaseMap.keySet().iterator();
        ArrayList list = new ArrayList();
        String databaseName;
        while (iterator.hasNext()) {
            databaseName = (String) iterator.next();
            list.add(databaseName);
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                removeDatabase((String) list.get(i));
            } catch (DException ex) {
            }
        }
    }

    public void changeHome(String path) {
        daffodilHome = path;
    }
}
