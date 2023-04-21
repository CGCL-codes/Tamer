package jbreport.xrl.dom;

import jbreport.xrl.XRLClassRef;

/**
 * 
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
public class XRLClassRefImpl extends XRLElementImpl implements XRLClassRef {

    public XRLClassRefImpl(XRLDocumentImpl ownerDocument, String value) {
        super(ownerDocument, value);
    }

    public XRLClassRefImpl(XRLDocumentImpl ownerDocument, String namespaceURI, String qualifiedName) {
        super(ownerDocument, namespaceURI, qualifiedName);
    }
}
