package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * @xerces.internal
 * @version $Revision: 447255 $ $Date: 2006-09-18 01:36:42 -0400 (Mon, 18 Sep 2006) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLTableSectionElement
 * @see org.apache.xerces.dom.ElementImpl
 */
public class HTMLTableSectionElementImpl extends HTMLElementImpl implements HTMLTableSectionElement {

    private static final long serialVersionUID = 1016412997716618027L;

    public String getAlign() {
        return capitalize(getAttribute("align"));
    }

    public void setAlign(String align) {
        setAttribute("align", align);
    }

    public String getCh() {
        String ch;
        ch = getAttribute("char");
        if (ch != null && ch.length() > 1) ch = ch.substring(0, 1);
        return ch;
    }

    public void setCh(String ch) {
        if (ch != null && ch.length() > 1) ch = ch.substring(0, 1);
        setAttribute("char", ch);
    }

    public String getChOff() {
        return getAttribute("charoff");
    }

    public void setChOff(String chOff) {
        setAttribute("charoff", chOff);
    }

    public String getVAlign() {
        return capitalize(getAttribute("valign"));
    }

    public void setVAlign(String vAlign) {
        setAttribute("valign", vAlign);
    }

    public HTMLCollection getRows() {
        if (_rows == null) _rows = new HTMLCollectionImpl(this, HTMLCollectionImpl.ROW);
        return _rows;
    }

    public HTMLElement insertRow(int index) {
        HTMLTableRowElementImpl newRow;
        newRow = new HTMLTableRowElementImpl((HTMLDocumentImpl) getOwnerDocument(), "TR");
        newRow.insertCell(0);
        if (insertRowX(index, newRow) >= 0) appendChild(newRow);
        return newRow;
    }

    int insertRowX(int index, HTMLTableRowElementImpl newRow) {
        Node child;
        child = getFirstChild();
        while (child != null) {
            if (child instanceof HTMLTableRowElement) {
                if (index == 0) {
                    insertBefore(newRow, child);
                    return -1;
                }
                --index;
            }
            child = child.getNextSibling();
        }
        return index;
    }

    public void deleteRow(int index) {
        deleteRowX(index);
    }

    int deleteRowX(int index) {
        Node child;
        child = getFirstChild();
        while (child != null) {
            if (child instanceof HTMLTableRowElement) {
                if (index == 0) {
                    removeChild(child);
                    return -1;
                }
                --index;
            }
            child = child.getNextSibling();
        }
        return index;
    }

    /**
     * Explicit implementation of cloneNode() to ensure that cache used
     * for getRows() gets cleared.
     */
    public Node cloneNode(boolean deep) {
        HTMLTableSectionElementImpl clonedNode = (HTMLTableSectionElementImpl) super.cloneNode(deep);
        clonedNode._rows = null;
        return clonedNode;
    }

    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLTableSectionElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }

    private HTMLCollectionImpl _rows;
}
