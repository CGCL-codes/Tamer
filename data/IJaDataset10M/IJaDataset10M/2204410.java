package org.itsnat.comp.table;

import org.itsnat.comp.*;
import javax.swing.CellEditor;
import org.w3c.dom.Element;

/**
 * Used to specify how a table cell value is edited in place.
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createDefaultItsNatTableCellEditor(ItsNatComponent)
 * @see ItsNatTable#getItsNatTableCellEditor()
 */
public interface ItsNatTableCellEditor extends CellEditor {

    /**
     * Returns the component used to edit in place the table cell value.
     *
     * <p>Default implementation uses a {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) to edit
     * the table cell value.</p>
     *
     * <p>Default implementation ignores <code>isSelected</code> parameter.</p>
     *
     * @param table the table component, may be used to provide contextual information. Default implementation ignores it.
     * @param row the cell row.
     * @param column the cell column.
     * @param value the value to edit (initial value).
     * @param isSelected true if the cell is selected.
     * @param cellContentElem the table cell content element to render the value into. Is a hint, if provided should be obtained by calling <code>table.getItsNatTableUI().getCellContentElement(row,column)</code>.
     * @return the component used to edit in place the table cell value. Current implementation of tables does nothing with this component and may be null (is not mandatory to use a single component as an editor).
     * @see ItsNatTable#setItsNatTableCellEditor(ItsNatTableCellEditor)
     */
    public ItsNatComponent getTableCellEditorComponent(ItsNatTable table, int row, int column, Object value, boolean isSelected, Element cellContentElem);
}
