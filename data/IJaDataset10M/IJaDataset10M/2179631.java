package org.ikasan.common.configuration;

import java.io.InputStream;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Java representation of the Ikasan XML configuration file.
 * 
 * @author Ikasan Development Team
 */
public class Ikasan extends AbstractIkasan {

    /**
     * Default constructor
     */
    public Ikasan() {
    }

    /**
     * Constructor: creates new <code>Ikasan</code> instance
     * 
     * @param noNamespaceSchemaLocation
     */
    public Ikasan(String noNamespaceSchemaLocation) {
        this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ikasan [");
        StringBuilder sbEntries = new StringBuilder();
        for (Entry entry : this.getEntries()) sbEntries.append(entry.toString());
        sb.append(sbEntries);
        sb.append("].");
        return sb.toString();
    }

    /**
     * Equality test
     * 
     * @param ikasan
     * @return boolean
     */
    public boolean equals(final Ikasan ikasan) {
        if (this.getEntries() == null && ikasan.getEntries() == null) {
            for (int x = 0; x < ikasan.getEntries().size(); x++) {
                if (!(this.entries.get(x).equals(ikasan.getEntries().get(x)))) return false;
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
     * @return the Ikasan instance
     */
    public static Ikasan fromXML(final String xml) {
        XStream xstream = new XStream(new DomDriver());
        setXstreamProps(xstream);
        return (Ikasan) xstream.fromXML(xml);
    }

    /**
     * Converts an incoming XML string to an object
     * 
     * @param xml XML input stream
     * @return the Ikasan instance
     */
    public static Ikasan fromXML(final InputStream xml) {
        XStream xstream = new XStream(new DomDriver());
        setXstreamProps(xstream);
        return (Ikasan) xstream.fromXML(xml);
    }

    /**
     * Sets the common properties for the toXML/fromXML XStream object
     * 
     * @param xstream
     */
    private static void setXstreamProps(XStream xstream) {
        xstream.registerConverter(new IkasanConverter());
        xstream.alias(Ikasan.class.getSimpleName(), Ikasan.class);
        xstream.registerConverter(new EntryConverter());
        xstream.alias(Entry.class.getSimpleName(), Entry.class);
    }
}
