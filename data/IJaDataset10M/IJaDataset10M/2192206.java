package org.openejb.alt.assembler.classic.xml;

import org.openejb.OpenEJBException;
import org.openejb.alt.assembler.classic.ConnectionManagerInfo;
import org.w3c.dom.Node;

/**
 * A subclass of ConnectionManagerInfo filled with data from an XML file.
 * 
 * Populates the member variables of ConnectionManagerInfo in this classes initializeFromDOM method.
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:Richard@Monson-Haefel.com">Richard Monson-Haefel</a>
 * @see org.openejb.alt.assembler.classic.ConnectionManagerInfo
 * @see #ConnectionManager.initializeFromDOM
 */
public class ConnectionManager extends ConnectionManagerInfo implements DomObject {

    /**
     * Represents the <tt>class-name</tt> element in the XML config file.
     */
    public static final String CLASS_NAME = "class-name";

    /**
     * Represents the <tt>"connection-manager-id</tt> element in the XML config file.
     */
    public static final String CONNECTION_MANAGER_ID = "connection-manager-id";

    /**
        * Represents the <tt>codebase</tt> element in the XML config file.
     */
    public static final String CODEBASE = "codebase";

    /** 
     * Parses out the values needed by this DomObject from the DOM Node passed in.
     * @see org.w3c.dom.Node
     */
    public void initializeFromDOM(Node node) throws OpenEJBException {
        className = DomTools.getChildElementPCData(node, CLASS_NAME);
        connectionManagerId = DomTools.getChildElementPCData(node, CONNECTION_MANAGER_ID);
        codebase = DomTools.getChildElementPCData(node, CODEBASE);
        properties = DomTools.readProperties(node);
    }

    public void serializeToDOM(Node node) throws OpenEJBException {
    }
}
