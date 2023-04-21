package de.mpiwg.vspace.diagram.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import de.mpiwg.vspace.util.swt.MyDialogCellEditor;

public class FileChooserCellEditor extends MyDialogCellEditor {

    private Composite parent;

    public FileChooserCellEditor(Composite parent) {
        super(parent);
        this.parent = parent;
    }

    protected Object openDialogBox(Control cellEditorWindow) {
        FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
        String imagePath = dialog.open();
        return imagePath;
    }
}
