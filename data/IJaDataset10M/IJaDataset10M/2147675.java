package org.itsnat.core.domutil;

import org.w3c.dom.Element;

/**
 * Used by {@link ElementList} objects to obtain the content parent element of
 * a child element of the list.
 *
 * @see ElementList#setElementListStructure(ElementListStructure)
 * @see ElementGroupManager#createDefaultElementListStructure()
 * @author Jose Maria Arranz Santamaria
 */
public interface ElementListStructure {

    /**
     * Returns the content element of a child element.
     *
     * <p>The content element is the parent element containing the markup of the associated
     * value usually a text node. This element is passed to the renderer method
     * {@link ElementListRenderer#renderList(ElementList,int,Object,Element,boolean)}.</p>
     *
     * <p>Default implementation returns the first &lt;td&gt; child if <code>itemElem</code>
     * is a &lt;tr&gt; element otherwise returns <code>itemElem</code>.</p>
     *
     * @param list the target list, may be used to provide contextual information. Default implementation ignores it.
     * @param index the child element position.
     * @param itemElem the direct child element in this position.
     *      This element is a hint, if provided, should be the same as returned by <code>list.getElementAt(index)</code>.
     * @return the content parent element. Default implementation returns the first cell if itemElem is a table row, otherwise returns the itemElem parameter.
     */
    public Element getContentElement(ElementList list, int index, Element itemElem);
}
