package org.jowidgets.tools.controller;

import org.jowidgets.common.types.IVetoable;
import org.jowidgets.common.widgets.controller.ITableCellEditEvent;
import org.jowidgets.common.widgets.controller.ITableCellEditorListener;
import org.jowidgets.common.widgets.controller.ITableCellEvent;

public class TableCellEditorAdapter implements ITableCellEditorListener {

    @Override
    public void onEdit(final IVetoable veto, final ITableCellEditEvent event) {
    }

    @Override
    public void editFinished(final ITableCellEditEvent event) {
    }

    @Override
    public void editCanceled(final ITableCellEvent event) {
    }
}
