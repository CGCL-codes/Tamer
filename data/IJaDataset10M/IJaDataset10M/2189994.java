package Core;

import Datatypes.*;
import javax.microedition.rms.*;

/**
 * A class that manages, creates and deletes RecordStores for use within
 * Toothfairy, especially for changing the settings.
 * It also carries information about the managed RecordStore, like the
 * RecordStore name or the number of Records in the actual RecordStore.
 * 
 * One RecordIO object can only manage one RecordStore. All methods assume
 * that the data to be stored or read from a record are String.
 */
public class RecordIO {

    private final int RECORD_OFFSET = 0;

    private RecordStore r;

    private String record_store_name;

    private int number_of_records;

    private boolean exists;

    /**
     * Gets the RecordStore name and opens or creates (if necessary) a
     * RecordStore with the given name. It also sets the number of records to
     * -1 if the RecordStore could not be created or opened or to the actual
     * number of records if the RecordStore could be created or opened.
     * @param rsn The Name of the RecordStore
     * @param create If true, the RecordStore will be created in case it does
     * not exist
     */
    public RecordIO(String rsn, boolean create) {
        record_store_name = rsn;
        exists = create;
        try {
            r = RecordStore.openRecordStore(record_store_name, create);
            number_of_records = r.getNumRecords();
        } catch (RecordStoreException rse) {
            number_of_records = -1;
        }
    }

    /**
     * Closes a RecordStore so no further operations can be made.
     * @return ErrCode Succes if the operation was made successfully
     */
    public ErrCode closeRecordStore() {
        if (exists) {
            try {
                r.closeRecordStore();
                return new ErrCode(Constants.ERR_SUCCESS);
            } catch (RecordStoreException rse) {
                return new ErrCode(Constants.ERR_FAILURE);
            }
        } else {
            return new ErrCode(Constants.ERR_SUCCESS);
        }
    }

    /**
     * Deletes a RecordStore completely.
     * @return ErrCode Succes if the operation was made successfully
     */
    public ErrCode deleteRecordStore() {
        try {
            RecordStore.deleteRecordStore(record_store_name);
            return new ErrCode(Constants.ERR_SUCCESS);
        } catch (RecordStoreException rse) {
            return new ErrCode(Constants.ERR_FAILURE);
        }
    }

    /**
     * Adds a single Record to the RecordStore managed by the actual RecordIO.
     * Of course it also increases the number_of_records when a Record was added
     * successfully.
     * @param s String to be added to the RecordStore
     * @return ErrCode Succes if the operation was made successfully
     */
    public ErrCode addSingleRecord(String s) {
        byte[] to_add = s.getBytes();
        try {
            r.addRecord(to_add, RECORD_OFFSET, to_add.length);
            number_of_records++;
            return new ErrCode(Constants.ERR_SUCCESS);
        } catch (RecordStoreException rse) {
            return new ErrCode(Constants.ERR_FAILURE);
        }
    }

    /**
     * Adds an array of Records to the RecordStore managed by the actual
     * RecordIO. It also increases the number_of_records for every successful
     * addition.
     * @param s String-Array to be added to the RecordStore
     * @return ErrCode Succes if the operation was made successfully
     */
    public ErrCode addRecordArray(String[] s) {
        byte[] to_add;
        try {
            for (int i = 0; i < s.length; i++) {
                to_add = s[i].getBytes();
                r.addRecord(to_add, RECORD_OFFSET, to_add.length);
                number_of_records++;
            }
            return new ErrCode(Constants.ERR_SUCCESS);
        } catch (RecordStoreException rse) {
            return new ErrCode(Constants.ERR_FAILURE);
        }
    }

    /**
     * Saves a String into the given ID (number) of a Record.
     * @param s String to be saved as an array of bytes
     * @param rid ID of the record in which the String should be saved
     * @return ErrCode Succes if the operation was made successfully
     */
    public ErrCode saveToRecord(String s, int rid) {
        byte[] to_save = s.getBytes();
        try {
            r.setRecord(rid, to_save, RECORD_OFFSET, to_save.length);
            return new ErrCode(Constants.ERR_SUCCESS);
        } catch (RecordStoreException rse) {
            return new ErrCode(Constants.ERR_FAILURE);
        }
    }

    /**
     * Opens a Record and returns the content as a string.
     * @param rid ID of the record to read from
     * @return String-representation of the stored data.
     */
    public String loadFromRecord(int rid) {
        String data;
        byte[] load_from;
        try {
            load_from = r.getRecord(rid);
            data = new String(load_from);
            return data;
        } catch (RecordStoreException rse) {
            return null;
        }
    }

    /**
     * Returns the Number of Records, the managed RecordStore has.
     * @return Number of Records in the actual RecordStore
     */
    public int getNumberOfRecords() {
        return number_of_records;
    }
}
