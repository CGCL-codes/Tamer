package com.pcmsolutions.device.EMU.E4.gui.table;

import com.pcmsolutions.system.ZDisposable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ColumnData implements ZDisposable {

    public String title;

    public int width;

    public int alignment;

    public int sectionIndex;

    public Class columnClass;

    public TableCellRenderer renderer;

    public TableCellEditor editor;

    public void zDispose() {
        if (renderer instanceof ZDisposable) ((ZDisposable) renderer).zDispose();
        if (editor instanceof ZDisposable) ((ZDisposable) editor).zDispose();
    }

    public ColumnData(String title, int width, int alignment, int sectionIndex, Class columnClass, TableCellRenderer renderer, TableCellEditor editor) {
        this(title, width, alignment, sectionIndex, columnClass);
        this.renderer = renderer;
        this.editor = editor;
    }

    public ColumnData(String title, int width, int alignment, int sectionIndex, Class columnClass, TableCellRenderer renderer) {
        this(title, width, alignment, sectionIndex, columnClass);
        this.renderer = renderer;
    }

    public ColumnData(String title, int width, int alignment, int sectionIndex, Class columnClass) {
        this.title = title;
        this.width = width;
        this.alignment = alignment;
        this.sectionIndex = sectionIndex;
        this.columnClass = columnClass;
    }
}
