package com.dbxml.db.core;

import com.dbxml.db.core.data.Key;
import com.dbxml.db.core.data.Value;
import com.dbxml.xml.dtsm.DocumentTable;

/**
 * Container is a generic container for objects that are stored in a
 * collection. A container can either contain a DocumentTable or a
 * Value, depending on the collection type. Container associates the
 * internal document or binary representation with its original
 * collection and key, so that the contents of the container can be
 * stored without having to maintain three separate references.
 */
public interface Container {

    static final int TYPE_DOCUMENT = 0;

    static final int TYPE_VALUE = 1;

    /**
    * getContainerType returns the type of Object being contained by this
    * Container.  This value can either TYPE_DOCUMENT or TYPE_VALUE.
    *
    * @return The Object type
    */
    int getContainerType();

    /**
    * getCollection returns the Collection that the Object contained
    * belongs to.
    *
    * @return The owner Collection
    */
    Collection getCollection();

    /**
    * getKey returns the Object Key.
    *
    * @return The Object Key
    */
    Key getKey();

    /**
    * getCanonicalName returns the canonical name for the contained
    * Object.
    * <br>
    * ex: /local/test/ocs/ytd
    *
    * @return The canonical name
    * @throws DBException If a Database Exception occurs
    */
    String getCanonicalName() throws DBException;

    /**
    * getDocument returns the contained Document.
    *
    * @return The Document
    * @throws DBException if a DocumentTable is not contained
    */
    DocumentTable getDocument() throws DBException;

    /**
    * getValue returns the contained Value (if any).
    *
    * @return The Value
    * @throws DBException if a Value is not contained
    */
    Value getValue() throws DBException;

    /**
    * reset reloads the Object from the Collection.
    *
    * @throws DBException If a Database Exception occurs
    */
    void reset() throws DBException;

    /**
    * remove removes the Object from the Collection.
    *
    * @throws DBException If a Database Exception occurs
    */
    void remove() throws DBException;

    /**
    * setDocument replaces the Document in the Collection with the
    * specified Document.
    *
    * @param document The Document
    * @throws DBException if Collection doesn't store Documents
    */
    void setDocument(DocumentTable document) throws DBException;

    /**
    * setValue replaces the Value in the Collection with the
    * specified Value.
    *
    * @param value The Value
    * @throws DBException if Collection doesn't store Values
    */
    void setValue(Value value) throws DBException;
}
