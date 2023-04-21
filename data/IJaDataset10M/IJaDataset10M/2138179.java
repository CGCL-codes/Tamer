package org.dbe.p2p.dss;

import org.dbe.p2p.dss.exceptions.DSSErrorException;
import org.dbe.p2p.dss.exceptions.MissingLocationException;
import org.dbe.p2p.dss.exceptions.MissingBlockException;
import org.dbe.p2p.dss.exceptions.MissingTTLException;

/**
 * This interface is provided to support DSS index operations on the DHT.
 *
 * @author <a href="mailto:John.M.Kennedy@intel.com">John Kennedy</a>
 */
public interface DSS {

    /**
     * Stores a DSS index entry assigning a location to a block with the
     * given time-to-live.
     *
     * @param p_block the block that is stored
     * @param p_location the location at which this block is located
     * @param p_ttl the time-to-live for this index entry, in seconds
     * @throws MissingBlockException if a null block is supplied
     * @throws MissingLocationException if a null location is supplied
     * @throws DSSErrorException if a DSS Overlay error occurs
     */
    public void put_location(String p_block, String p_location, int p_ttl) throws MissingBlockException, MissingLocationException, DSSErrorException;

    /**
     * Retrieves the locations at which a block is stored.
     *
     * @param p_block the block whose locations are required
     * @return a String array of locations for the block.
     * @throws MissingBlockException if a null block is supplied
     * @throws DSSErrorException if a DSS Overlay error occurs
     */
    public String[] get_location(String p_block) throws MissingBlockException, DSSErrorException;

    /**
     * Updates the time-to-live for a block location on the DSS index.
     *
     * @param p_block the block whose time-to-live is to be updated
     * @param p_ttl the new time-to-live of the block, in seconds
     * @throws MissingBlockException if a null block is supplied
     * @throws MissingTTLException if a null time-to-live is supplied
     * @throws DSSErrorException if a DSS Overlay error occurs
     */
    public void updateTTL(String p_block, int p_ttl) throws MissingBlockException, MissingTTLException, DSSErrorException;
}
