package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;
import org.apache.xerces.impl.dtd.XMLContentSpec;

/**
 * SimpleContentModel is a derivative of the abstract content model base
 * class that handles a small set of simple content models that are just
 * way overkill to give the DFA treatment.
 * <p>
 * This class handles the following scenarios:
 * <ul>
 * <li> a
 * <li> a?
 * <li> a*
 * <li> a+
 * <li> a,b
 * <li> a|b
 * </ul>
 * <p>
 * These all involve a unary operation with one element type, or a binary
 * operation with two elements. These are very simple and can be checked
 * in a simple way without a DFA and without the overhead of setting up a
 * DFA for such a simple check.
 * 
 * @xerces.internal
 * 
 * @version $Id: SimpleContentModel.java 572057 2007-09-02 18:03:20Z mrglavas $
 */
public class SimpleContentModel implements ContentModelValidator {

    /** CHOICE */
    public static final short CHOICE = -1;

    /** SEQUENCE */
    public static final short SEQUENCE = -1;

    /**
     * The element decl pool indices of the first (and optional second)
     * child node. The operation code tells us whether the second child
     * is used or not.
     */
    private final QName fFirstChild = new QName();

    /**
     * The element decl pool indices of the first (and optional second)
     * child node. The operation code tells us whether the second child
     * is used or not.
     */
    private final QName fSecondChild = new QName();

    /**
     * The operation that this object represents. Since this class only
     * does simple contents, there is only ever a single operation
     * involved (i.e. the children of the operation are always one or
     * two leafs.) This is one of the XMLDTDParams.CONTENTSPECNODE_XXX values.
     */
    private final int fOperator;

    /**
     * Constructs a simple content model.
     *
     * @param operator The content model operator.
     * @param firstChild qualified name of the first child
     * @param secondChild qualified name of the second child
     *
     */
    public SimpleContentModel(short operator, QName firstChild, QName secondChild) {
        fFirstChild.setValues(firstChild);
        if (secondChild != null) {
            fSecondChild.setValues(secondChild);
        } else {
            fSecondChild.clear();
        }
        fOperator = operator;
    }

    /**
     * Check that the specified content is valid according to this
     * content model. This method can also be called to do 'what if' 
     * testing of content models just to see if they would be valid.
     * <p>
     * A value of -1 in the children array indicates a PCDATA node. All other 
     * indexes will be positive and represent child elements. The count can be
     * zero, since some elements have the EMPTY content model and that must be 
     * confirmed.
     *
     * @param children The children of this element.  Each integer is an index within
     *                 the <code>StringPool</code> of the child element name.  An index
     *                 of -1 is used to indicate an occurrence of non-whitespace character
     *                 data.
     * @param offset Offset into the array where the children starts.
     * @param length The number of entries in the <code>children</code> array.
     *
     * @return The value -1 if fully valid, else the 0 based index of the child
     *         that first failed. If the value returned is equal to the number
     *         of children, then the specified children are valid but additional
     *         content is required to reach a valid ending state.
     *
     */
    public int validate(QName[] children, int offset, int length) {
        switch(fOperator) {
            case XMLContentSpec.CONTENTSPECNODE_LEAF:
                if (length == 0) return 0;
                if (children[offset].rawname != fFirstChild.rawname) {
                    return 0;
                }
                if (length > 1) return 1;
                break;
            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE:
                if (length == 1) {
                    if (children[offset].rawname != fFirstChild.rawname) {
                        return 0;
                    }
                }
                if (length > 1) return 1;
                break;
            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE:
                if (length > 0) {
                    for (int index = 0; index < length; index++) {
                        if (children[offset + index].rawname != fFirstChild.rawname) {
                            return index;
                        }
                    }
                }
                break;
            case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE:
                if (length == 0) return 0;
                for (int index = 0; index < length; index++) {
                    if (children[offset + index].rawname != fFirstChild.rawname) {
                        return index;
                    }
                }
                break;
            case XMLContentSpec.CONTENTSPECNODE_CHOICE:
                if (length == 0) return 0;
                if ((children[offset].rawname != fFirstChild.rawname) && (children[offset].rawname != fSecondChild.rawname)) {
                    return 0;
                }
                if (length > 1) return 1;
                break;
            case XMLContentSpec.CONTENTSPECNODE_SEQ:
                if (length == 2) {
                    if (children[offset].rawname != fFirstChild.rawname) {
                        return 0;
                    }
                    if (children[offset + 1].rawname != fSecondChild.rawname) {
                        return 1;
                    }
                } else {
                    if (length > 2) {
                        return 2;
                    }
                    return length;
                }
                break;
            default:
                throw new RuntimeException("ImplementationMessages.VAL_CST");
        }
        return -1;
    }
}
