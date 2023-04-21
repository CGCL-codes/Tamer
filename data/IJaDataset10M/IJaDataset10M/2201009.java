package org.xBaseJ.rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import org.xBaseJ.xBaseJException;

public class DBF extends java.rmi.server.UnicastRemoteObject implements DBFInterface {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    org.xBaseJ.DBF dbf = null;

    Field fields[];

    /** Constructor */
    public DBF(String inDBFName) throws RemoteException {
        super();
        try {
            dbf = new org.xBaseJ.DBF(inDBFName);
            int fc = dbf.getFieldCount();
            fields = new Field[fc];
            for (int i = 1; i <= fc; i++) {
                fields[i - 1] = new Field(dbf.getField(i));
            }
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * returns the number of fields in a database
  	 */
    public int getFieldCount() throws RemoteException {
        return dbf.getFieldCount();
    }

    /**
  	 * returns the number of records in a database
  	 */
    public int getRecordCount() throws RemoteException {
        return dbf.getRecordCount();
    }

    /**
  	 * returns the current record number
  	 */
    public int getCurrentRecordNumber() throws RemoteException {
        return dbf.getCurrentRecordNumber();
    }

    /**
  	 * opens an Index file associated with the database.  This index becomes the primary
  	 * index used in subsequent find methods.
  	 * @param filename      an existing ndx file(can be full or partial pathname) or mdx tag
  	 * @throws RemoteException
  	 *                                    org.xBaseJ Fields defined in index do not match fields in database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void useIndex(String filename) throws RemoteException {
        try {
            dbf.useIndex(filename);
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * associates all Index operations with an existing tag
  	 * @param tagname      an existing tag name in the production MDX file
  	 * @throws RemoteException
  	 *                                    no MDX file
  	 *                                    tagname not found
  	 */
    public void useTag(String tagname) throws RemoteException {
        try {
            dbf.useTag(tagname);
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        }
    }

    private void setInterfaceFieldsFromDBF() throws RemoteException {
        try {
            for (int i = 0; i < dbf.getFieldCount(); i++) fields[i].put(dbf.getField(i + 1).get());
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        }
    }

    private void putInterfaceFieldsToDBF() throws RemoteException {
        try {
            for (int i = 0; i < dbf.getFieldCount(); i++) dbf.getField(i + 1).put(fields[i].get());
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        }
    }

    /**
  	 * used to find a record with an equal or greater string value
  	 * when done the record pointer and field contents will be changed
  	 * @param keyString  a search string
  	 * @return boolean indicating if the record found contains the exact key
  	 * @throws RemoteException
  	 *                                    org.xBaseJ no Indexs opened with database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public boolean find(String keyString) throws RemoteException {
        try {
            boolean br = dbf.find(keyString);
            setInterfaceFieldsFromDBF();
            return br;
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to get the next  record in the index list
  	 * when done the record pointer and field contents will be changed
  	 * @throws RemoteException
  	 *                                    org.xBaseJ Index not opened or not part of the database
  	 *                                    eof - end of file
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void findNext() throws RemoteException {
        try {
            dbf.findNext();
            setInterfaceFieldsFromDBF();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to get the previous record in the index list
  	 * when done the record pointer and field contents will be changed
  	 * @throws RemoteException
  	 *                                    org.xBaseJ Index not opened or not part of the database
  	 *                                    tof - top of file
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void findPrev() throws RemoteException {
        try {
            dbf.findPrev();
            setInterfaceFieldsFromDBF();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to read the next record, after the current record pointer, in the database
  	 * when done the record pointer and field contents will be changed
  	 * @throws RemoteException
  	 *                                    usually the end of file condition
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void read() throws RemoteException {
        try {
            dbf.read();
            setInterfaceFieldsFromDBF();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to read the previous record, before the current record pointer, in the database
  	 * when done the record pointer and field contents will be changed
  	 * @throws RemoteException
  	 *                                    usually the top of file condition
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void readPrev() throws RemoteException {
        try {
            dbf.readPrev();
            setInterfaceFieldsFromDBF();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to read a record at a particular place in the database
  	 * when done the record pointer and field contents will be changed
  	 * @param recno the relative position of the record to read
  	 * @throws RemoteException
  	 *                                    passed an negative number, 0 or value greater than the number of records in database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void gotoRecord(int recno) throws RemoteException {
        try {
            dbf.gotoRecord(recno);
            setInterfaceFieldsFromDBF();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to position record pointer at the first record or index in the database
  	 * when done the record pointer will be changed.  NO RECORD IS READ.
  	 * Your program should follow this with either a read (for non-index reads) or findNext (for index processing)
  	 * @return String starting index
  	 * @throws RemoteException
  	 *                                    most likely no records in database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void startTop() throws RemoteException {
        try {
            dbf.startTop();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to position record pointer at the last record or index in the database
  	 * when done the record pointer will be changed. NO RECORD IS READ.
  	 * Your program should follow this with either a read (for non-index reads) or findPrev (for index processing)
  	 * @return String terminal index
  	 * @throws RemoteException
  	 *                                    most likely no records in database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void startBottom() throws RemoteException {
        try {
            dbf.startBottom();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * used to write a new record in the database
  	 * when done the record pointer is at the end of the database
  	 * @throws RemoteException
  	 *                                    any one of several errors
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void write() throws RemoteException {
        try {
            putInterfaceFieldsToDBF();
            dbf.write();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * updates the record at the current position
  	 * @throws RemoteException
  	 *                                    any one of several errors
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void update() throws RemoteException {
        try {
            putInterfaceFieldsToDBF();
            dbf.update();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * marks the current records as deleted
  	 * @throws RemoteException
  	 *                                    usually occurs when no record has been read
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void delete() throws RemoteException {
        try {
            dbf.delete();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * marks the current records as not deleted
  	 * @throws RemoteException
  	 *                                    usually occurs when no record has been read.
  	 */
    public void undelete() throws RemoteException {
        try {
            dbf.undelete();
        } catch (xBaseJException xe) {
            throw new RemotexBaseJException(xe.getMessage());
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * returns true if record is marked for deletion
  	 */
    public boolean deleted() throws RemoteException {
        return dbf.deleted();
    }

    /**
  	 * closes the database
  	 * @return IOException
  	 *                                    Java error caused by called methods
  	 */
    public void close() throws RemoteException {
        try {
            dbf.close();
        } catch (IOException ioe) {
            throw new RemoteException("IOException " + ioe.getMessage());
        }
    }

    /**
  	 * returns a Field type by its relative position
  	 * @param i Field number
  	 * @return char
  	 * @throws RemoteException
  	 *                                    Field name is not correct
  	 */
    public FieldInterface getField(int i) throws RemoteException {
        try {
            return fields[i - 1];
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    /**
  	 * returns a Field type by its name in the database
  	 * @param name Field name
  	 * @throws RemoteException
  	 *                                    Field name is not correct
  	 */
    public FieldInterface getField(String name) throws RemoteException {
        String lcn = name.toLowerCase();
        for (int i = 0; i < dbf.getFieldCount(); i++) if (fields[i].getName().toLowerCase().compareTo(lcn) == 0) return fields[i];
        throw new RemotexBaseJException("Field name not found");
    }
}
