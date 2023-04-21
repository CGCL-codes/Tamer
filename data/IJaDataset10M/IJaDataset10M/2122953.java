package org.ws4d.java.client;

/**
 * A search path is an abstract way to describe a technology (e.g. DPWS,
 * Bluetooth, ZigBee, etc.) and a physical or virtual interface of the local
 * machine within this technology (such as an IP address, a network adapter
 * name, a serial port number, etc.). Such interfaces are referred to as
 * {@link #getDomainIdentifier() domain identifiers} whereas different
 * technologies are represented by specific {@link #getTechnologyIdentifier()
 * technology identifiers}.
 */
public class SearchPath {

    private String technologyId;

    private String domainId;

    /**
	 * Creates a new search path with the given technology ID and domain ID.
	 * 
	 * @param technologyId the identifier of the technology over which to
	 *            search, e.g. DPWS, BT (Bluetooth), ZB (ZigBee), etc.
	 * @param domainId the ID of a technology-specific interface, such as IP
	 *            address or network interface name, etc.
	 */
    public SearchPath(String technologyId, String domainId) {
        this.technologyId = technologyId;
        this.domainId = domainId;
    }

    public String toString() {
        return technologyId + " / " + domainId;
    }

    /**
	 * Returns the technology ID, e.g. DPWS.
	 * 
	 * @return the technology identifier of this search path
	 */
    public String getTechnologyIdentifier() {
        return technologyId;
    }

    /**
	 * Returns e.g. for DPWS the interface name or IP address/DNS name.
	 * 
	 * @return the domain identifier referring to a physical or logical
	 *         interface of the local machine within the specified
	 *         {@link #getTechnologyIdentifier() technology}
	 */
    public String getDomainIdentifier() {
        return domainId;
    }
}
