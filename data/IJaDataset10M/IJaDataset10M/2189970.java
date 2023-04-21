package org.itsnat.impl.core.domwrap;

import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatNodeDefaultImpl extends ItsNatNodeImpl {

    /**
     * Creates a new instance of ItsNatNodeDefaultImpl
     */
    public ItsNatNodeDefaultImpl(Node node, ItsNatDocumentImpl itsNatDoc) {
        super(node, itsNatDoc);
    }

    public static ItsNatNodeDefaultImpl newItsNatNodeDefault(Node node, ItsNatDocumentImpl itsNatDoc) {
        return new ItsNatNodeDefaultImpl(node, itsNatDoc);
    }
}
