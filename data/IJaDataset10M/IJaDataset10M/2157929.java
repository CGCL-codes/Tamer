package org.hardtokenmgmt.server.uds;

import java.io.Serializable;

/**
 * 
 * Printer parameter data used to contain custom data fetched from a user data source.
 * Used to fill in special fields in printed forms such as address etc.
 * 
 * Contains a key that is mapped to a variable definition in the print template.
 * 
 * @author Philip Vendil 25 jul 2009
 *
 * @version $Id$
 */
public class PrintDataParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private String value;

    public PrintDataParameter() {
    }

    /**
	 * Default constructor
	 * 
	 */
    public PrintDataParameter(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
	 * 
	 * @return the key associated with the value.
	 */
    public String getKey() {
        return key;
    }

    /**
	 * 
	 * @return the value associated with the key.
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param key  associated with the value.
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @param value associated with the key.
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
