package org.itsnat.impl.comp.table;

import org.itsnat.impl.comp.*;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatTableCellRendererDefaultImpl extends ItsNatCellRendererDefaultImpl implements ItsNatTableCellRenderer {

    /**
     * Creates a new instance of ItsNatTableCellRendererDefaultImpl
     */
    public ItsNatTableCellRendererDefaultImpl(ItsNatComponentManagerImpl componentMgr) {
        super(componentMgr);
    }

    public void renderTableCell(ItsNatTable table, int row, int column, Object value, boolean isSelected, boolean hasFocus, Element cellContentElem, boolean isNew) {
        if (cellContentElem == null) cellContentElem = table.getItsNatTableUI().getCellContentElementAt(row, column);
        renderCell(table, value, cellContentElem, isNew);
    }

    public void unrenderTableCell(ItsNatTable table, int row, int column, Element cellContentElem) {
    }
}
