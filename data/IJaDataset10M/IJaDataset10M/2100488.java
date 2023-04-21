package com.abiquo.framework.xml.exceptions;

import com.abiquo.framework.xml.events.messages.XMLMessageException;

/**
 * The Class ResourceManagerException appears when there are too many resource instances running or wrong resource
 * instance.
 */
public class ResourceManagerException extends XMLMessageException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new resource manager exception.
	 * 
	 * @param description
	 *            the description
	 * @param node
	 *            the node
	 */
    public ResourceManagerException(String description, String node) {
        super(description, "ResourceManager", node, null);
    }
}
