package org.dbe.composer.wfengine.bpel.def.io.ext;

import javax.xml.namespace.QName;

/**
 * Generic interface for all BPEL extension elements.
 * <br />
 * It is expected that readers and writers will cast
 * instances to specific subclasses for serialization/
 * deserialization.
 */
public interface ISdlExtensionElement {

    /**
     * Accessor for extension element QName.
     * @return QName
     */
    public QName getElementQName();

    /**
     * Mutator for extension element QName;
     * @param aQName
     */
    public void setElementQName(QName aQName);

    /**
     * Preserve any comments.
     * @param aComments
     */
    public void setComments(String aComments);
}
