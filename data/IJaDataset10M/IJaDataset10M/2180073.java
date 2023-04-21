package org.columba.core.gui.globalactions;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.base.UndoDocument;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

public class UndoAction extends AbstractColumbaAction implements PropertyChangeListener {

    private JComponent focusOwner = null;

    public UndoAction(IFrameMediator controller) {
        super(controller, GlobalResourceLoader.getString(null, null, "menu_edit_undo"));
        putValue(SHORT_DESCRIPTION, GlobalResourceLoader.getString(null, null, "menu_edit_undo_tooltip").replaceAll("&", ""));
        putValue(SMALL_ICON, ImageLoader.getSmallIcon(IconKeys.EDIT_UNDO));
        putValue(LARGE_ICON, ImageLoader.getIcon(IconKeys.EDIT_UNDO));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        setShowToolBarText(false);
        setEnabled(true);
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addPropertyChangeListener("permanentFocusOwner", this);
    }

    public void propertyChange(PropertyChangeEvent e) {
        Object o = e.getNewValue();
        if (o instanceof JComponent) focusOwner = (JComponent) o; else focusOwner = null;
    }

    public void actionPerformed(ActionEvent evt) {
        if (focusOwner == null) return;
        if (focusOwner instanceof JTextComponent) {
            Document doc = ((JTextComponent) focusOwner).getDocument();
            if (doc instanceof UndoDocument) {
                ((UndoDocument) doc).undo();
            }
        }
    }
}
