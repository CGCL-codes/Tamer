package org.jdom;

import java.io.*;

/**
 * Superclass for JDOM objects which can be legal child content
 * of {@link org.jdom.Parent} nodes.
 *
 * @see org.jdom.Comment
 * @see org.jdom.DocType
 * @see org.jdom.Element
 * @see org.jdom.EntityRef
 * @see org.jdom.Parent
 * @see org.jdom.ProcessingInstruction
 * @see org.jdom.Text
 *
 * @author Bradley S. Huffman
 * @author Jason Hunter
 * @version $Revision: 1.6 $, $Date: 2007/11/10 05:28:58 $
 */
public abstract class Content implements Cloneable, Serializable {

    protected Parent parent = null;

    protected Content() {
    }

    /**
     * Detaches this child from its parent or does nothing if the child
     * has no parent.
     *
     * @return this child detached
     */
    public Content detach() {
        if (parent != null) {
            parent.removeContent(this);
        }
        return this;
    }

    /**
     * Return this child's parent, or null if this child is currently
     * not attached. The parent can be either an {@link Element}
     * or a {@link Document}.
     *
     * @return this child's parent or null if none
     */
    public Parent getParent() {
        return parent;
    }

    /**
     * A convenience method that returns any parent element for this element,
     * or null if the element is unattached or is a root element.  This was the
     * original behavior of getParent() in JDOM Beta 9 which began returning
     * Parent in Beta 10.  This method provides a convenient upgrade path for
     * JDOM Beta 10 and 1.0 users.
     *
     * @return the containing Element or null if unattached or a root element
     */
    public Element getParentElement() {
        Parent parent = getParent();
        return (Element) ((parent instanceof Element) ? parent : null);
    }

    /**
     * Sets the parent of this Content. The caller is responsible for removing
     * any pre-existing parentage.
     *
     * @param  parent              new parent element
     * @return                     the target element
     */
    protected Content setParent(Parent parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Return this child's owning document or null if the branch containing
     * this child is currently not attached to a document.
     *
     * @return this child's owning document or null if none
     */
    public Document getDocument() {
        if (parent == null) return null;
        return parent.getDocument();
    }

    /**
     * Returns the XPath 1.0 string value of this child.
     *
     * @return xpath string value of this child.
     */
    public abstract String getValue();

    /**
     * Returns a deep, unattached copy of this child and its descendants
     * detached from any parent or document.
     *
     * @return a detached deep copy of this child and descendants
     */
    public Object clone() {
        try {
            Content c = (Content) super.clone();
            c.parent = null;
            return c;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * This tests for equality of this Content object to the supplied object.
     * Content items are considered equal only if they are referentially equal
     * (i&#46;e&#46; the same object).  User code may choose to compare objects
     * based on their properties instead.
     *
     * @param ob <code>Object</code> to compare to.
     * @return <code>boolean</code> - whether the <code>Content</code> is
     *         equal to the supplied <code>Object</code>.
     */
    public final boolean equals(Object ob) {
        return (ob == this);
    }

    /**
     * This returns the hash code for this <code>Content</code> item.
     *
     * @return <code>int</code> - hash code.
     */
    public final int hashCode() {
        return super.hashCode();
    }
}
