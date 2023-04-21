package org.openejb.alt.assembler.classic.xml;

import org.openejb.OpenEJBException;
import org.openejb.alt.assembler.classic.IntraVmServerInfo;
import org.w3c.dom.Node;

/**
 * A subclass of IntraVmServerInfo filled with data from an XML file.
 * 
 * Populates the member variables of IntraVmServerInfo in this classes initializeFromDOM method.
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:Richard@Monson-Haefel.com">Richard Monson-Haefel</a>
 * @see org.openejb.alt.assembler.classic.IntraVmServerInfo
 * @see #IntraVmServer.initializeFromDOM
 */
public class IntraVmServer extends IntraVmServerInfo implements DomObject {

    /**
     * Represents the <tt>factory-class</tt> element in the XML config file.
     */
    public static final String PROXY_FACTORY = "proxy-factory";

    /**
     * Represents the <tt>codebase</tt> element in the XML config file.
     */
    public static final String CODEBASE = "codebase";

    /** 
     * Parses out the values needed by this DomObject from the DOM Node passed in.
     * @see org.w3c.dom.Node
     */
    public void initializeFromDOM(Node node) throws OpenEJBException {
        proxyFactoryClassName = DomTools.getChildElementPCData(node, PROXY_FACTORY);
        codebase = DomTools.getChildElementPCData(node, CODEBASE);
        proxyFactoryClass = DomTools.toolkit.forName(proxyFactoryClassName);
        properties = DomTools.readProperties(node);
    }

    public void serializeToDOM(Node node) throws OpenEJBException {
    }
}
