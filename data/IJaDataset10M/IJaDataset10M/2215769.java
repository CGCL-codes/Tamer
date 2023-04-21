package com.daffodilwoods.daffodildb.server.datasystem.indexsystem;

import com.daffodilwoods.daffodildb.server.datasystem.interfaces._DataSystem;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._Database;
import com.daffodilwoods.database.resource.DException;
import java.util.Properties;
import java.util.HashMap;

public class ReadOnlyTempIndexSystem implements _DataSystem {

    HashMap readOnlyTempIndexDatabaseMap;

    public ReadOnlyTempIndexSystem() {
        readOnlyTempIndexDatabaseMap = new HashMap();
    }

    public synchronized _Database getDatabase(String databaseName) throws DException {
        _Database database = (ReadOnlyTempIndexDatabase) readOnlyTempIndexDatabaseMap.get(databaseName);
        if (database != null) return database;
        database = new ReadOnlyTempIndexDatabase();
        readOnlyTempIndexDatabaseMap.put(databaseName, database);
        return database;
    }

    public void createDatabase(String string, Properties properties) throws DException {
    }

    public synchronized void dropDatabase(String databaseName) throws DException {
        removeDatabase(databaseName);
    }

    public synchronized void removeDatabase(String databaseName) throws DException {
        readOnlyTempIndexDatabaseMap.remove(databaseName);
    }
}
