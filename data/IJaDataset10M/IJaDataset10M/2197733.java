package org.akrogen.tkui.gui.swt.widgets.tables;

import java.util.ArrayList;
import java.util.List;
import org.akrogen.tkui.core.dom.ITkuiWidgetElement;
import org.akrogen.tkui.core.gui.widgets.AbstractGuiWidget;
import org.akrogen.tkui.core.gui.widgets.IGuiOptions;
import org.akrogen.tkui.core.gui.widgets.facets.colors.IBackgroundColorHolder;
import org.akrogen.tkui.core.gui.widgets.graphics.RGBValue;
import org.akrogen.tkui.core.gui.widgets.tables.IGuiTable;
import org.akrogen.tkui.core.gui.widgets.tables.IGuiTableCell;
import org.akrogen.tkui.core.gui.widgets.tables.IGuiTableRow;

/**
 * GUI TableRow implemented into SWT.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class SwtGuiTableRowImpl extends AbstractGuiWidget implements IGuiTableRow, IBackgroundColorHolder {

    private List guiTableCells = new ArrayList();

    private ITkuiWidgetElement tkuiWidgetElement;

    private RGBValue backgroundColor;

    public Object buildWidget(Object parent, IGuiOptions options) {
        ((IGuiTable) getParentGuiWidget()).addRow(this);
        return null;
    }

    public void addListener(ITkuiWidgetElement element, int guiEventId, String eventAttrName) {
    }

    public Object getWidget() {
        return null;
    }

    public boolean hasBeenTreat() {
        return true;
    }

    public RGBValue getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(RGBValue backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addCell(IGuiTableCell cell) {
        guiTableCells.add(cell);
    }

    public IGuiTableCell getCell(int index) {
        if (guiTableCells.size() > index) return (IGuiTableCell) guiTableCells.get(index);
        return null;
    }
}
