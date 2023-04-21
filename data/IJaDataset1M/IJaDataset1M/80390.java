package org.npsnet.v.properties.controller.network;

/**
 * A property interface for modules that bear DIS-style entity
 * IDs.
 *
 * @author Andrzej Kapolka
 */
public interface EntityIDBearer extends NetworkController {

    /**
     * Sets this module's entity ID.
     *
     * @param pSiteID the new site ID
     * @param pApplicationID the new application ID
     * @param pEntityID the new entity ID
     */
    public void setEntityID(int pSiteID, int pApplicationID, int pEntityID);

    /**
     * Returns this module's site ID.
     *
     * @return this module's site ID
     */
    public int getSiteID();

    /**
     * Returns this module's application ID.
     *
     * @return this module's application ID
     */
    public int getApplicationID();

    /**
     * Returns this module's entity ID.
     *
     * @return this module's entity ID
     */
    public int getEntityID();
}
