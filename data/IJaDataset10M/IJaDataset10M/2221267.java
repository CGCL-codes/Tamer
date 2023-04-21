package jdb.event;

import jdb.DataTable;
import jdb.DataBase;
import jdb.exception.DuplicateDataException;

public abstract class DataBaseEvent {

    private DataBase db;

    private DataTable source;

    public DataBaseEvent(DataTable newSource, DataBase newDB) {
        source = newSource;
        db = newDB;
    }

    public abstract DataBaseEvent undo() throws DuplicateDataException;

    public abstract void inform(DataBaseListener dsl);

    public DataBase getDB() {
        return db;
    }

    public DataTable getContainer() {
        return source;
    }
}
