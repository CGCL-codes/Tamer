package com.tikal.eclipse.delivery.ui.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class TikalDirectoryFieldEditor extends DirectoryFieldEditor {

    public void setButtonVisible(boolean visible, Composite parent) {
        this.getChangeControl(parent).setVisible(visible);
    }

    public TikalDirectoryFieldEditor() {
        super();
    }

    public TikalDirectoryFieldEditor(String arg0, String arg1, Composite arg2) {
        super(arg0, arg1, arg2);
    }
}
