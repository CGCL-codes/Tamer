package com.daffodilwoods.daffodildb.server.datasystem.interfaces;

import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.utils._Reference;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._DatabaseUser;
import com.daffodilwoods.daffodildb.server.datasystem.utility.*;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;

public interface _Navigator {

    /**
     * sets the current pointer to first record in the table.
     * @return true if the first record exists in the table
     * @throws DException
     */
    boolean first() throws DException;

    /**
     * sets the current pointer of the iterator to the last record in the table
     * @return true if the record is found in  the table.
     * @throws DException
     */
    boolean last() throws DException;

    /**
     * sets the current pointer of the iterator to the next record in te table
     * @return true if nextrecord exists in the table.
     * @throws DException
     */
    boolean next() throws DException;

    /**
     * sets the current pointer of the iterator to the previous record in the table.
     * @return true if the previous record exists in the table
     * @throws DException
     */
    boolean previous() throws DException;

    /**
     * used to retreive the current position of the iterator
     * @return the current position of the iterator.
     * @throws DException
     */
    Object getKey() throws DException;

    /**
     * Used to set the pointer of the iterator to the given position.
     * @param key the key to which the pointer is to be moved.
     * @throws DException
     */
    void move(Object key) throws DException;

    /**
     * Used to get the values of the record at the current position of the iterator.
     * @param columns is an array of columns whose value is to be retreived
     * @return an array of values of that record.
     * @throws DException
     */
    _Record getRecord() throws DException;

    Object getColumnValues() throws DException;

    Object getColumnValues(int[] columns) throws DException;

    public byte[] getByteKey() throws DException;

    public void moveByteKey(byte[] key) throws DException;
}
