package gov.noaa.edsd.remotexmldb.urlsToXmldb;

import org.xmldb.api.base.Configurable;
import org.xmldb.api.base.XMLDBException;

/**
 * The base configurable class.
 * @author tns
 * @version $Id: UrlConfigurable.java,v 1.1 2004/10/29 22:51:03 mrxtravis Exp $
 */
public class UrlConfigurable implements Configurable {

    /** Creates a new instance of UrlConfigurable */
    public UrlConfigurable() {
    }

    /**
     * Does nothing, no properties availabe
     * @param prop Ignored
     * @throws org.xmldb.api.base.XMLDBException Never
     * @return null, always.
     */
    public String getProperty(String prop) throws XMLDBException {
        return null;
    }

    /**
     * Does nothing, no properties availabe
     * @param prop Ignored.
     * @param val Ignored.
     * @throws org.xmldb.api.base.XMLDBException Never thrown.
     */
    public void setProperty(String prop, String val) throws XMLDBException {
    }
}
