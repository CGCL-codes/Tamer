package jmax.editors.patcher.menus;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import jmax.editors.patcher.*;
import jmax.editors.patcher.actions.*;

/** Implement the patcher editor File Menu */
public class FileMenu extends PatcherMenu {

    class FileMenuListener implements MenuListener {

        public void menuSelected(MenuEvent e) {
            updateMenu();
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }

    private ErmesSketchWindow editor;

    JMenuItem newItem;

    JMenuItem openItem;

    JMenuItem saveItem;

    JMenuItem saveAsItem;

    JMenuItem saveToItem;

    JMenuItem exportAsTextItem;

    JMenuItem closeItem;

    JMenuItem printItem;

    JMenuItem quitItem;

    public FileMenu(ErmesSketchWindow editor) {
        super("File");
        this.editor = editor;
        setHorizontalTextPosition(AbstractButton.LEFT);
        newItem = add(new NewAction(editor), "New", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_N);
        openItem = add(new OpenAction(editor), "Open", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_O);
        addSeparator();
        saveItem = add(new SaveAction(editor), "Save", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_S);
        saveAsItem = add(new SaveAsAction(editor), "Save As");
        saveToItem = add(new SaveToAction(editor), "Save Copy");
        exportAsTextItem = add(new ExportAsTextAction(editor), "Export As Text");
        closeItem = add(new CloseAction(editor), "Close", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_W);
        addSeparator();
        printItem = add(new PrintAction(editor), "Print", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_P);
        quitItem = add(new QuitAction(editor), "Quit", InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_Q);
        addMenuListener(new FileMenuListener());
    }

    private void updateMenu() {
        newItem.setEnabled(true);
        openItem.setEnabled(true);
        saveItem.setEnabled(editor.canSave());
        saveAsItem.setEnabled(editor.canSaveAs());
        saveToItem.setEnabled(true);
        exportAsTextItem.setEnabled(editor.canExportAsText());
        closeItem.setEnabled(true);
        printItem.setEnabled(false);
        quitItem.setEnabled(true);
    }
}
