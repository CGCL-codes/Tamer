package com.alexmcchesney.poster.plugins;

/**
 * Object describing a service supported by a plugin.
 * @author amcchesney
 *
 */
public class ServiceDescriptor {

    /** Unique id of the service */
    private String m_sID = null;

    /** Name of the service. Should be localized for the current locale */
    private String m_sName = null;

    /** URL to the service.  eg http://www.blogger.com */
    private String m_sURL = null;

    /**
	 * Constructor
	 * @param sID		Unique id of the service
	 * @param sName		Localized friendly name of the service
	 * @param sURL		URL to the service
	 */
    public ServiceDescriptor(String sID, String sName, String sURL) {
        m_sID = sID;
        m_sName = sName;
        m_sURL = sURL;
    }

    public String getID() {
        return m_sID;
    }

    public String getName() {
        return m_sName;
    }

    public String getURL() {
        return m_sURL;
    }
}
