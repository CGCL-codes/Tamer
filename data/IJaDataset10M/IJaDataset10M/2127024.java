package org.dbe.composer.wfengine.bpel.server.engine.storage;

import java.util.Iterator;

/**
 * Defines interface for a set of objects identified by location id and version
 * number.
 */
public interface ISdlLocationVersionSet {

    /**
     * Adds the specified location id and version number to the set.
     *
     * @param aLocationId
     * @param aVersionNumber
     */
    public void add(long aLocationId, int aVersionNumber);

    /**
     * Returns <code>true</code> if and only if the set contains the specified
     * location id and version number.
     *
     * @param aLocationId
     * @param aVersionNumber
     */
    public boolean contains(long aLocationId, int aVersionNumber);

    /**
     * Returns <code>true</code> if and only if the set contains the location id
     * and version number in the specified entry.
     *
     * @param aEntry
     */
    public boolean contains(IEntry aEntry);

    /**
     * Returns an <code>Iterator</code> over the entries in the set, where
     * entries are instances of <code>IAeLocationVersionEntry</code>.
     */
    public Iterator iterator();

    /**
     * Defines the interface for an entry in an <code>ISdlLocationVersionSet</code>.
     */
    public interface IEntry {

        /**
         * Returns the location id.
         */
        public long getLocationId();

        /**
         * Returns the version number.
         */
        public int getVersionNumber();
    }
}
