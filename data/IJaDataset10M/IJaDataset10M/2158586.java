package org.xulbooster.eclipse.ui.utils.form.controls.tableViewer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class ColumnDefinition {

    private String label;

    private int width;

    public enum ColumnType {

        TEXT, INT
    }

    ;

    private ColumnType type;

    private String initValue;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ColumnDefinition(String aLabel, int aWidth, ColumnType aType, String aInitValue) {
        this.label = aLabel;
        this.width = aWidth;
        this.type = aType;
        this.initValue = aInitValue;
    }

    public String getLabel() {
        return label;
    }

    public ColumnType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public CellEditor getCellEditor(Table table) {
        CellEditor editor = null;
        switch(this.type) {
            case TEXT:
                TextCellEditor textEditor = new TextCellEditor(table);
                ((Text) textEditor.getControl()).setTextLimit(60);
                editor = textEditor;
                break;
            case INT:
                TextCellEditor intEditor = new TextCellEditor(table);
                ((Text) intEditor.getControl()).addVerifyListener(new VerifyListener() {

                    public void verifyText(VerifyEvent e) {
                        e.doit = "0123456789".indexOf(e.text) >= 0;
                    }
                });
                editor = intEditor;
                break;
        }
        return editor;
    }

    public String getInitValue() {
        return initValue;
    }
}
