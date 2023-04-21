package org.jowidgets.common.widgets.controller;

public interface ITableCellEditorObservable {

    void addTableCellEditorListener(ITableCellEditorListener listener);

    void removeTableCellEditorListener(ITableCellEditorListener listener);
}
