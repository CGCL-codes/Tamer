package com.daffodilwoods.daffodildb.server.datasystem.indexsystem;

import com.daffodilwoods.daffodildb.server.datasystem.interfaces._Database;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._Table;
import com.daffodilwoods.database.general.QualifiedIdentifier;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._DatabaseUser;
import java.util.ArrayList;
import java.util.HashMap;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._IndexDatabase;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._AlterRecord;

public class ReadOnlyTempIndexDatabase implements _IndexDatabase {

    private HashMap readOnlyTempIndexTables;

    public ReadOnlyTempIndexDatabase() {
        readOnlyTempIndexTables = new HashMap();
    }

    public synchronized _Table getTable(QualifiedIdentifier qualifiedIdentifier) throws DException {
        _Table readOnlyTempIndexTable = (_Table) readOnlyTempIndexTables.get(qualifiedIdentifier.getIdentifier());
        if (readOnlyTempIndexTable != null) return readOnlyTempIndexTable;
        readOnlyTempIndexTable = new ReadOnlyTempIndexTable();
        readOnlyTempIndexTables.put(qualifiedIdentifier.getIdentifier(), readOnlyTempIndexTable);
        return readOnlyTempIndexTable;
    }

    public void createTable(QualifiedIdentifier qualifiedIdentifier, Object object) throws DException {
    }

    public synchronized void dropTable(QualifiedIdentifier qualifiedIdentifier) throws DException {
        removeTable(qualifiedIdentifier);
    }

    public _DatabaseUser getDatabaseUser() throws DException {
        return null;
    }

    public synchronized void removeTable(QualifiedIdentifier qualifiedIdentifier) throws DException {
        readOnlyTempIndexTables.remove(qualifiedIdentifier.getIdentifier());
    }

    public _DatabaseUser getTempDatabaseUser() throws DException {
        return null;
    }

    public _DatabaseUser getDatabaseUser(ArrayList arrayList) throws DException {
        return null;
    }

    public void addFreeCluster(_DatabaseUser _DatabaseUser, int int1) throws DException {
    }

    public void createTemporaryIndex(QualifiedIdentifier qualifiedIdentifier, String string, _IndexInformation _IndexInformation) throws DException {
    }

    public void createPermanantIndex(QualifiedIdentifier qualifiedIdentifier, String string, _IndexInformation _IndexInformation, _DatabaseUser _DatabaseUser) throws DException {
    }

    public void dropTemporaryIndex(QualifiedIdentifier qualifiedIdentifier, String string) throws DException {
    }

    public void dropPeramanantIndex(QualifiedIdentifier qualifiedIdentifier, String string, _DatabaseUser _DatabaseUser) throws DException {
    }

    public void alterTable(QualifiedIdentifier qualifiedIdentifier, Object object, _AlterRecord _AlterRecord, Object object3, _DatabaseUser _DatabaseUser) throws DException {
    }

    public void createFullTextIndex(QualifiedIdentifier qualifiedIdentifier, String string, String[] stringArray) throws DException {
    }

    public void dropFullTextIndex(QualifiedIdentifier qualifiedIdentifier, String string) throws DException {
    }

    public _Table getTable(QualifiedIdentifier tableName, _IndexInformation[] iinf) throws DException {
        return getTable(tableName);
    }
}
