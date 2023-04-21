package gov.lanl.registryclient;

import gov.lanl.registryclient.parser.Metadata;
import org.apache.log4j.Logger;

/**
 * maintain information about a specific tape
 */
public class RegistryRecord<T extends Metadata> {

    String identifier = null;

    String datestamp = null;

    T metadata;

    static Logger log = Logger.getLogger(RegistryRecord.class.getName());

    public RegistryRecord(String identifier, String datestamp, T data) {
        this.identifier = identifier;
        this.datestamp = datestamp;
        this.metadata = data;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public T getMetaData() {
        return metadata;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }
}
