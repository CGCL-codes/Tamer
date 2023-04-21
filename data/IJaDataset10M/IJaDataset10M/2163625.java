package cx.ath.contribs.internal.xerces.impl.dtd.models;

import cx.ath.contribs.internal.xerces.impl.dtd.XMLContentSpec;
import cx.ath.contribs.internal.xerces.xni.QName;

/**
 * Content model leaf node.
 * 
 * @xerces.internal
 *
 * @version $Id: CMLeaf.java,v 1.1 2007/06/02 09:59:00 paul Exp $
 */
public class CMLeaf extends CMNode {

    /** This is the element that this leaf represents. */
    private QName fElement = new QName();

    /**
     * Part of the algorithm to convert a regex directly to a DFA
     * numbers each leaf sequentially. If its -1, that means its an
     * epsilon node. Zero and greater are non-epsilon positions.
     */
    private int fPosition = -1;

    /** Constructs a content model leaf. */
    public CMLeaf(QName element, int position) {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);
        fElement.setValues(element);
        fPosition = position;
    }

    /** Constructs a content model leaf. */
    public CMLeaf(QName element) {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);
        fElement.setValues(element);
    }

    final QName getElement() {
        return fElement;
    }

    final int getPosition() {
        return fPosition;
    }

    final void setPosition(int newPosition) {
        fPosition = newPosition;
    }

    public boolean isNullable() {
        return (fPosition == -1);
    }

    public String toString() {
        StringBuffer strRet = new StringBuffer(fElement.toString());
        strRet.append(" (");
        strRet.append(fElement.uri);
        strRet.append(',');
        strRet.append(fElement.localpart);
        strRet.append(')');
        if (fPosition >= 0) {
            strRet.append(" (Pos:").append(Integer.toString(fPosition)).append(')');
        }
        return strRet.toString();
    }

    protected void calcFirstPos(CMStateSet toSet) {
        if (fPosition == -1) toSet.zeroBits(); else toSet.setBit(fPosition);
    }

    protected void calcLastPos(CMStateSet toSet) {
        if (fPosition == -1) toSet.zeroBits(); else toSet.setBit(fPosition);
    }
}
