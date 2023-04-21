package com.volantis.shared.net.http;

import java.util.Iterator;
import java.io.Serializable;

/**
 * A container for HTTPMessageEntity objects.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface HTTPMessageEntities extends Serializable {

    /**
     * Put a HTTPMessageEntity into this HTTPMessageEntities. If a
     * HTTPMessageEntity with the same identity already exists in this
     * HTTPMessageEntities then it will be replaced by be the given
     * HTTPMessageEntity and the replaced HTTPMessageEntity(s) will be
     * returned. If multiple instances of the specified entity already exist
     * then all of these existing instances will be replaced by the single
     * entity provided.
     * @param entity The HTTPMessageEntity.
     * @return The replaced HTTPMessageEntity objects or null if none were
     * replaced.
     * @see #put(com.volantis.shared.net.http.HTTPMessageEntity [] entities)
     */
    public HTTPMessageEntity[] put(HTTPMessageEntity entity);

    /**
     * Provide an Iterator over the HTTPMessageEntity objects in this
     * HTTPMessageEntities.
     * @return An HTTPMessageEntity Iterator
     */
    public Iterator iterator();

    /**
     * Provide an Iterator over the names of the HTTPMessageEntity objects
     * contained in this HTTPMessageEntities.
     * @return A names Iterator where each name is a String.
     */
    public Iterator namesIterator();

    /**
     * Remove a HTTPMessageEntity from this HTTPMessageEntities. If
     * multiple instances of the named HTTPMessageEntity exist then all
     * will be removed.
     * @param identity The identity of the entity to remove.
     * @return The removed HTTPMessageEntity objects or null if none were
     * removed.
     */
    public HTTPMessageEntity[] remove(HTTPMessageEntityIdentity identity);

    /**
     * Clear all HTTPMessageEntity objects from the HTTPMessageEntities.
     */
    public void clear();

    /**
     * Retrieve all the instances of a named HTTPMessageEntity in this
     * HTTPMessageEntities.
     * @param identity The identity of the entity to retrieve.
     * @return All the instances of the named HTTPMessageEntity or null
     * if none exist.
     */
    public HTTPMessageEntity[] retrieve(HTTPMessageEntityIdentity identity);

    /**
     * Determine whether a named HTTPMessageEntity is contained within this
     * HTTPMessageEntities.
     * @param identity The identity of the entity to test.
     * @return true if one or more instances of the name HTTPMessageEntity
     * exits; false otherwise.
     */
    public boolean containsIdentity(HTTPMessageEntityIdentity identity);

    /**
     * Add a TransmissionProperty to the HTTPMessageEntities.
     * HTTPMessageEntity objects with duplicate identities as the specifed
     * HTTPMessageEntity are ignored i.e. this method allows the addition
     * of HTTPMessageEntity objects with duplicate identities. However,
     * duplicate objects (those with all properties equal) will not be added.
     * @param entity The HTTPMessageEntity to add to this
     * HTTPMessageEntities.
     */
    public void add(HTTPMessageEntity entity);

    /**
     * Put an array of HTTPMessageEntity objects to the
     * HTTPMessageEntities. If a HTTPMessageEntity with the same identity
     * as one of the given HTTPMessageEntity objects already exists in this
     * HTTPMessageEntities then it will be replaced by be the given
     * HTTPMessageEntity and the replaced HTTPMessageEntity(s) will be
     * returned. If multiple instances of one of the given HTTPMessageEntity
     * object already exist then all of these existing instances will be
     * replaced by the single entity provided. All the HTTPMessageEntity
     * objects in the given array will be individually added not put i.e. if
     * the array contains entries that have duplicate identities these duplicate
     * entries will be added to this HTTPMessageEntities.
     * @param entities An array of HTTPMessageEntity objects to put into
     * this HTTPMessageEntities.
     * @return All of the HTTPMessageEntity objects that were replaced or
     * null if none were replaced.
     */
    public HTTPMessageEntity[] put(HTTPMessageEntity[] entities);

    /**
     * Provide an Iterator over the values for named HTTPMessageEntity.
     * @param identity The identity of the entity whose values to iterate.
     * @return An values Iterator where each value is a String.
     */
    public Iterator valuesIterator(HTTPMessageEntityIdentity identity);

    /**
     * Return the number of HTTPMessageEntity objects is this
     * HTTPMessageEntities.
     * @return The number of HTTPMessageEntities in this
     * HTTPMessageEntities.
     */
    public int size();
}
