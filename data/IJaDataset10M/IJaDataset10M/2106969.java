<@pp.dropOutputFile />
<#include "/@inc/ob.ftl">
<#list types as t>
<@type_info t=t/>

<@pp.changeOutputFile name="OBStore"+Type+".java" />
package net.obsearch.storage;

import java.util.Iterator;
import net.obsearch.storage.CloseIterator;
import net.obsearch.exception.OBStorageException;
import net.obsearch.OperationStatus;
import java.nio.ByteBuffer;

/*
 OBSearch: a distributed similarity search engine This project is to
 similarity search what 'bit-torrent' is to downloads. 
 Copyright (C) 2008 Arnoldo Jose Muller Molina

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * OBStore${Type} abstracts a generic storage system. The purpose of
 * this class is to allow OBSearch to run on top of different storage
 * systems (distributed, local, file system based, etc). The keys can
 * be sorted, and range queries are possible. This interface provides
 * utility methods for keys of type ${type}.
 * @author Arnoldo Jose Muller Molina
 */

public interface OBStore${Type} extends OBStore<Tuple${Type}> {
    

    /**
     * Returns the associated value for the given key. If the underlying storage system can hold repeated keys, then
     * an IllegalArgumentException is thrown.
     * @param key the key that will be searched.
     * @return the associated value for the given key.
     * @throws IllegalArgumentException If the underlying storage system can hold multiple keys.
     * @throws OBStorageException
     *                 If an exception occurs at the underlying storage system.
     *                 You can query the exception to see more details regarding
     *                 the nature of the error.
     */
    byte[] getValue(${type}  key) throws IllegalArgumentException, OBStorageException;
    
    /**
     * Process the given range of items (from low to high), including low and high. The TupleProcessor's process
     * method will be called for each value found within the range.
     * @param low
     * @param high
		 * @throws OBStorageException 
		 *                 If an exception occurs at the underlying storage system.
     *                 You can query the exception to see more details regarding
     *                 the nature of the error.
     */
    CloseIterator<Tuple${Type}> processRange(${type} low, ${type} high) throws OBStorageException;


		/**
     * Process the given range of items (from high to low), including low and high. The TupleProcessor's process
     * method will be called for each value found within the range.
     * @param low
     * @param high
		 * @throws OBStorageException 
		 *                 If an exception occurs at the underlying storage system.
     *                 You can query the exception to see more details regarding
     *                 the nature of the error.
     */
    CloseIterator<Tuple${Type}> processRangeReverse(${type} low, ${type} high) throws OBStorageException;
    
		
		/**
     * Inserts the key value pair. If the key existed, it will be overwritten.
     * @param key
     *                Key to insert
     * @param value
     *                The value that the key will hold after this operation
     *                completes.
     * @throws OBStorageException
     *                 If an exception occurs at the underlying storage system.
     *                 You can query the exception to see more details regarding
     *                 the nature of the error.
     * @return {@link net.obsearch.OperationStatus.Status#OK} the record
     * was inserted/updated successfully.
     *               {@link net.obsearch.OperationStatus.Status#ERROR} if
     * the record could not be updated.
     */
    OperationStatus put(${type} key, byte[] value) throws OBStorageException;

		/**
     * Deletes the given key and its corresponding value from the database.
     * @param key
     *                The key that will be deleted.
     * @throws OBStorageException
     *                 If an exception occurs at the underlying storage system.
     *                 You can query the exception to see more details regarding
     *                 the nature of the error.
     * @return {@link net.obsearch.OperationStatus.Status#OK} if the key was found,
     *         otherwise, {@link net.obsearch.OperationStatus.Status#NOT_EXISTS}.
     */
    OperationStatus delete(${type} key)throws OBStorageException;


		/**
		 * Convert a byte entry into a ${type}.
		 */
		public ${type} bytesToValue(byte[] entry);
    
}

</#list>
