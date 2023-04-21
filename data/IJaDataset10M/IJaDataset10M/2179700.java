package org.akrogen.tkui.ui.swt.controls.tables;

import org.akrogen.tkui.core.ui.elements.IUIElementInfo;
import org.akrogen.tkui.core.ui.elements.tables.IUITableColumn;
import org.akrogen.tkui.ui.swt.AbstractSWTWidgetImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

public class SWTTableColumnImpl extends AbstractSWTWidgetImpl implements IUITableColumn {

    private static Log logger = LogFactory.getLog(SWTTableColumnImpl.class);

    protected Widget buildWidget(Widget parent, IUIElementInfo uiElementInfo) {
        TableColumn tableColumn = createTableColumn((Table) parent, SWT.NONE);
        return tableColumn;
    }

    protected TableColumn createTableColumn(Table parent, int style) {
        TableColumn tableColumn = new TableColumn(parent, style);
        if (logger.isDebugEnabled()) logger.debug("Create SWT TableColumn.");
        return tableColumn;
    }

    public void setWidth(int width) {
        getTableColumn().setWidth(width);
    }

    protected TableColumn getTableColumn() {
        return (TableColumn) getWidget();
    }
}
