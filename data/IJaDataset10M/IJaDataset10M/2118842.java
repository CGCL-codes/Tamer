package com.abiquo.framework.domain;

/**
 * This class is used in the DataStartXMLEvent class to specify the LocalID and
 * the Encoding. It is important to say that this class is just a wrapper of the above attributes, then it has no data content.
 * 
 * @see com.abiquo.framework.xml.events.DataStartXMLEvent
 * @see com.abiquo.framework.xml.events.DataEndXMLEvent
 * @author abiquo
 */
public class DataStart {

    /** The local id. */
    protected String localId;

    /** The encoding. */
    protected String encoding;

    /**
	 * Instantiates a new data start.
	 * 
	 * @param localId
	 *            the local ID
	 * @param encoding
	 *            the encoding
	 */
    public DataStart(String localId, String encoding) {
        this.localId = localId;
        this.encoding = encoding;
    }

    /**
	 * Gets the encoding.
	 * 
	 * @return the encoding
	 */
    public String getEncoding() {
        return encoding;
    }

    /**
	 * Gets the local id.
	 * 
	 * @return the local id
	 */
    public String getLocalId() {
        return localId;
    }

    /**
	 * Sets the encoding.
	 * 
	 * @param encoding
	 *            the new encoding
	 */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
	 * Sets the local id.
	 * 
	 * @param localId
	 *            the new local id
	 */
    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
