package net.sergetk.bitmapfont.editor.actions;

import net.sergetk.bitmapfont.editor.BitmapFontEditor;
import net.sergetk.bitmapfont.editor.font.BitmapFont;
import net.sergetk.common.swt.actions.AbstractAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Load Font Action loads font from the file
 */
public class LoadFontAction extends AbstractAction {

    public void execute() {
        Shell parentShell = BitmapFontEditor.instance.mainWindow.getShell();
        FileDialog fileDialog = new FileDialog(parentShell, SWT.OPEN);
        fileDialog.setFilterExtensions(new String[] { "*.fnt", "*.*" });
        fileDialog.setFilterNames(new String[] { "J2ME Bitmap Font", "All files" });
        String filename = fileDialog.open();
        if (filename != null) {
            BitmapFont bitmapFont = new BitmapFont(filename);
            BitmapFontEditor.instance.setFont(bitmapFont);
        }
    }
}
