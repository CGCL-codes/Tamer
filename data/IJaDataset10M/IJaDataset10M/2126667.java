package org.itsnat.comp.table;

import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.itsnat.comp.table.ItsNatHTMLTableHeader;
import org.itsnat.comp.table.ItsNatTableHeaderUI;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * Is the base interface of the User Interface of an HTML table header component.
 *
 * <p>This interface basically provides casts of the base methods.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLTableHeaderUI extends ItsNatHTMLElementComponentUI, ItsNatTableHeaderUI {

    /**
     * Returns the associated component object.
     *
     * @return the component object.
     */
    public ItsNatHTMLTableHeader getItsNatHTMLTableHeader();

    /**
     * Returns the &lt;thead&gt; element.
     *
     * @return the &lt;thead&gt; element.
     */
    public HTMLTableSectionElement getHTMLTableSectionElement();

    /**
     * Returns the &lt;row&gt; element.
     *
     * @return the &lt;row&gt; element.
     */
    public HTMLTableRowElement getHTMLTableRowElement();

    /**
     * Returns the &lt;td&gt; or &lt;th&gt; element at the specified column.
     *
     * @param column the column index of the cell element to search.
     * @return the element in this position or null if some index is out of range.
     * @see ItsNatTableHeaderUI#getElementAt(int)
     */
    public HTMLTableCellElement getHTMLTableCellElementAt(int column);
}
