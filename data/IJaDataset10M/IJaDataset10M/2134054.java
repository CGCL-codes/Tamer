package org.webdocwf.util.loader.graphicaleditor.gui.graph;

import org.jgraph.graph.DefaultEdge;
import org.webdocwf.util.loader.graphicaleditor.model.XmlElement;

/**
 * @author Adnan Veseli
 *
 */
public abstract class AbstractEdge extends DefaultEdge implements DefaultCell {

    private XmlElement xmlElement = null;

    private XmlElement parentXmlElement = null;

    private PortCell mySource = null;

    private PortCell myTarget = null;

    public AbstractEdge(final XmlElement element, final PortCell source, final PortCell target) {
        super();
        setXmlElement(element);
        setSource(source);
        setTarget(target);
    }

    /**
     * @param element
     *            The element to set.
     */
    public void setXmlElement(XmlElement element) {
        this.xmlElement = element;
    }

    /**
     * @return Returns the xmlElement.
     */
    public XmlElement getXmlElement() {
        return xmlElement;
    }

    public XmlElement getParentXmlElement() {
        return this.parentXmlElement;
    }

    protected void setParentXmlElement(XmlElement parent) {
        this.parentXmlElement = parent;
    }

    /**
     * @return Returns the mySource.
     */
    @Override
    public PortCell getSource() {
        return mySource;
    }

    /**
     * @return Returns the myTarget.
     */
    @Override
    public PortCell getTarget() {
        return myTarget;
    }

    /**
     * @param source
     *            The source to set.
     */
    protected void setSource(PortCell source) {
        this.mySource = source;
    }

    /**
     * @param target
     *            The target to set.
     */
    protected void setTarget(PortCell target) {
        this.myTarget = target;
    }
}
