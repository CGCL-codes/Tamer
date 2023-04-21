package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import core.Globals;
import cache.ImageCache;

public class MenuRedoAction extends Action {

    public MenuRedoAction() {
        setText("&Redo\tMOD1+Y");
        setToolTipText("Redo");
        setImageDescriptor(ImageCache.getImageDescriptor("redo_edit.gif"));
        setDisabledImageDescriptor(ImageCache.getDisabledImageDescriptor("redo_edit.gif"));
        setAccelerator(SWT.MOD1 + 'Y');
    }

    public void run() {
        if (Globals.ACTIVE_SHEET == null) return;
        if (Globals.ACTIVE_SHEET.getUndoManager().redoable()) Globals.ACTIVE_SHEET.getUndoManager().redo();
    }
}
