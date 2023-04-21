package se.marianna.simpleDB.persisted;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import se.marianna.simpleDB.DBInitException;
import se.marianna.simpleDB.DuplicateColumnName;
import se.marianna.simpleDB.Logger;
import se.marianna.simpleDB.MissingColumnName;
import se.marianna.simpleDB.PrimaryKeyException;
import se.marianna.simpleDB.SimpleDBException;
import se.marianna.simpleDB.SimpleDBValue;
import se.marianna.simpleDB.SortingAlgorithm;
import se.marianna.simpleDB.StorageException;
import se.marianna.simpleDB.Table;
import se.marianna.simpleDB.Relation;
import se.marianna.simpleDB.TupelComparator;
import se.marianna.simpleDB.UnknownValueType;

public abstract class RecordStoreTable extends Table {

    String tableName = "";

    Logger logger = new Logger(RecordStoreTable.class);

    public RecordStoreTable(String tableName, String[] columns) throws DuplicateColumnName, UnknownValueType {
        super(tableName, columns);
        logger.debug("new table with tablename = " + tableName + "");
        this.tableName = tableName;
    }

    public RecordStoreTable(String tableName, Object[] columnNamesAndTypes) throws DBInitException, DuplicateColumnName {
        super(tableName, columnNamesAndTypes);
        logger.debug("new table with tablename = " + tableName + "");
        this.tableName = tableName;
    }

    public RecordStoreTable(String tableName, String[] primaryKey, Object[] columnNamesAndTypes) throws DBInitException, DuplicateColumnName {
        super(tableName, primaryKey, columnNamesAndTypes);
        logger.debug("new table with tablename = " + tableName + "");
        this.tableName = tableName;
    }

    Hashtable tupel2index = new Hashtable();

    boolean loading = false;

    int currentIndex = 0;

    public synchronized void loadFromStorage() throws SimpleDBException {
        logger.debug(tableName + ".loadFromStorage()");
        tupel2index.clear();
        values.removeAllElements();
        {
            loading = true;
            Object[] allTheTupels = getTupels();
            for (int i = 0; i < allTheTupels.length; i++) {
                currentIndex = i;
                if (allTheTupels[i] != null) {
                    addTupel((SimpleDBValue[]) allTheTupels[i]);
                }
            }
            loading = false;
        }
    }

    protected abstract byte[][] getTupelsData() throws StorageException;

    protected abstract int addRecord(byte[] tupelData) throws StorageException;

    protected abstract void removeRecord(int recordToRemove) throws StorageException;

    private Object[] getTupels() throws SimpleDBException {
        byte[][] tupelsData = getTupelsData();
        Object[] tupels = new Object[tupelsData.length];
        for (int i = 0; i < tupelsData.length; i++) {
            if (tupelsData[i] != null) {
                tupels[i] = getTupel(tupelsData[i]);
            }
        }
        return tupels;
    }

    private SimpleDBValue[] getTupel(byte[] tupelData) throws SimpleDBException {
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(tupelData);
            DataInputStream dataIn = new DataInputStream(byteIn);
            SimpleDBValue[] columnClasses = getColumnClasses();
            SimpleDBValue[] tupel = new SimpleDBValue[columnClasses.length];
            for (int i = 0; i < tupel.length; i++) {
                tupel[i] = columnClasses[i].fromDataInputStream(dataIn);
            }
            return tupel;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new SimpleDBException(ex.getMessage());
        }
    }

    private int addRecord(SimpleDBValue[] tupel) {
        if (loading) {
            return currentIndex;
        }
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            for (int i = 0; i < tupel.length; i++) {
                SimpleDBValue value = tupel[i];
                value.toDataOutputStream(dataOut);
            }
            dataOut.flush();
            byte[] data = byteOut.toByteArray();
            dataOut.close();
            byteOut.close();
            return addRecord(data);
        } catch (IOException ex) {
            throw new StorageException("{" + ex.getMessage() + "," + ex.getClass().getName() + "}");
        }
    }

    protected void addTupel(SimpleDBValue[] tupel) throws PrimaryKeyException, MissingColumnName {
        super.addTupel(tupel);
        int tupelStoredIndex = addRecord(tupel);
        tupel2index.put(tupel, new Integer(tupelStoredIndex));
    }

    protected void removeTupel(SimpleDBValue[] tupel) throws MissingColumnName {
        super.removeTupel(tupel);
        int tupelStoredIndex = ((Integer) tupel2index.remove(tupel)).intValue();
        removeRecord(tupelStoredIndex);
    }

    public Relation sort(TupelComparator tupelComparator, SortingAlgorithm sortingAlgorithm) {
        throw new Error("Sorting not supported by this table type");
    }
}
