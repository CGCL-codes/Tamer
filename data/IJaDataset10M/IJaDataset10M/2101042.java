package org.ikasan.common.configuration;

import java.io.InputStream;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Java representation of the Ikasan Security XML configuration file.
 * 
 * @author Ikasan Development Team
 */
public class IkasanSecurity extends AbstractIkasan {

    /**
     * Default constructor
     */
    public IkasanSecurity() {
    }

    /**
     * Constructor: creates new <code>IkasanSecurity</code> instance
     * 
     * @param noNamespaceSchemaLocation
     */
    public IkasanSecurity(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ikasan Security [");
        StringBuilder sbEntries = new StringBuilder();
        for (Entry entry : this.getEntries()) sbEntries.append(entry.toString());
        sb.append(sbEntries);
        sb.append("].");
        return sb.toString();
    }

    /**
     * Equality test
     * @param ikasanSecurity 
     * @return boolean
     */
    public boolean equals(final IkasanSecurity ikasanSecurity) {
        if (this.getEntries() == null && ikasanSecurity.getEntries() == null) {
            for (int x = 0; x < ikasanSecurity.getEntries().size(); x++) {
                if (!(this.entries.get(x).equals(ikasanSecurity.getEntries().get(x)))) return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Converts the object to an XML string
     * 
     * @return resulting XML string
     */
    public String toXML() {
        XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("$", "_")));
        setXstreamProps(xstream);
        return xstream.toXML(this);
    }

    /**
     * Converts an incoming XML string to an object
     * 
     * @param xml XML string
     * @return the IkasanSecurity instance
     */
    public static IkasanSecurity fromXML(final String xml) {
        XStream xstream = new XStream(new DomDriver());
        setXstreamProps(xstream);
        return (IkasanSecurity) xstream.fromXML(xml);
    }

    /**
     * Converts an incoming XML string to an object
     * 
     * @param xml XML input stream
     * @return the IkasanSecurity instance
     */
    public static IkasanSecurity fromXML(final InputStream xml) {
        XStream xstream = new XStream(new DomDriver());
        setXstreamProps(xstream);
        return (IkasanSecurity) xstream.fromXML(xml);
    }

    /**
     * Sets the common properties for the toXML/fromXML XStream object
     * 
     * @param xstream
     */
    private static void setXstreamProps(XStream xstream) {
        xstream.registerConverter(new IkasanSecurityConverter());
        xstream.alias(IkasanSecurity.class.getSimpleName(), IkasanSecurity.class);
        xstream.registerConverter(new EntryConverter());
        xstream.alias(Entry.class.getSimpleName(), Entry.class);
    }
}
