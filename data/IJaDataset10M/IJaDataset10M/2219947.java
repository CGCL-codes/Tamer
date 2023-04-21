package gov.lanl.util.sru;

import java.io.InputStream;
import java.util.Vector;

public interface SRURecord {

    public abstract Vector<String> getKeys();

    public abstract Vector<SRUValuePair> getValuePairs();

    public abstract void addKey(String key, String value);

    public abstract String getNamespace();

    /**
     * Serializes Object to XML
     */
    public abstract String toXML() throws SRUException;

    /**
     * Parses DC element to contstruct DC object
     */
    public abstract SRURecord read(InputStream stream) throws SRUException;
}
