package org.webdocwf.util.loader.graphicaleditor.gui.graph;

import org.jgraph.graph.GraphCell;
import org.webdocwf.util.loader.graphicaleditor.model.XmlElement;

/**
 * @author Adnan Veseli
 *
 */
public interface DefaultCell extends GraphCell {

    public XmlElement getXmlElement();

    public XmlElement getParentXmlElement();

    public void setXmlElement(XmlElement xmlElement);
}
