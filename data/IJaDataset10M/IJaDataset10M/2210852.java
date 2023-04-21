package org.gridbus.broker.gui.action;

import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;
import org.w3c.dom.Node;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> XMLBeanJsdlEditor </code>
 */
public class XMLBeanJsdlEditor implements DocumentEditor {

    public Node newDocument() {
        return JobDefinitionDocument.Factory.newInstance().addNewJobDefinition().getDomNode();
    }
}
