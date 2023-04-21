package com.bluemarsh.jswat.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;

/**
 * Class EditPopup defines a subclass of popup menu that works for any text
 * component to provide copy, paste, and clear functions. Like any popup
 * menu, you must add this popup as a child to the text component in
 * question. It also must be added as a mouse listener to the text
 * component.
 *
 * @author  Nathan Fiedler
 */
public class EditPopup extends SmartPopupMenu implements ActionListener {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** Text component to operate on. */
    private JTextComponent textComponent;

    /** The Clear menu item. */
    private JMenuItem clearMenuItem;

    /** The Copy menu item. */
    private JMenuItem copyMenuItem;

    /** The Paste menu item. */
    private JMenuItem pasteMenuItem;

    /** The Select All menu item. */
    private JMenuItem selectAllMenuItem;

    /**
     * Constructs an EditPopup that interacts with the given text
     * component. The popup can offer pasting as well as clearing
     * of the text component. By default, the popup will allow
     * copying the selected text to the clipboard.
     *
     * @param  text   text component to manage.
     * @param  paste  true to allow pasting.
     * @param  clear  true to allow clearing.
     */
    public EditPopup(JTextComponent text, boolean paste, boolean clear) {
        super(Bundle.getString("Edit.label"));
        textComponent = text;
        if (clear) {
            clearMenuItem = createMenuItem("Edit.clear");
        }
        copyMenuItem = createMenuItem("Edit.copy");
        if (paste) {
            pasteMenuItem = createMenuItem("Edit.paste");
        }
        selectAllMenuItem = createMenuItem("Edit.selectAll");
    }

    /**
     * Invoked when a menu item has been selected.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        JMenuItem source = (JMenuItem) event.getSource();
        if (source == copyMenuItem) {
            textComponent.copy();
        } else if (source == clearMenuItem) {
            textComponent.setText("");
        } else if (source == selectAllMenuItem) {
            textComponent.selectAll();
            textComponent.getCaret().setSelectionVisible(true);
        } else {
            textComponent.paste();
        }
    }

    /**
     * Create the menu item for the given action command.
     *
     * @param  key  key for looking up resources.
     * @return  new menu item.
     */
    protected JMenuItem createMenuItem(String key) {
        JMenuItem mi = new JMenuItem(Bundle.getString(key + "Label"));
        mi.addActionListener(this);
        mi.setToolTipText("<html><small>" + Bundle.getString(key + "Tooltip") + "</small></html>");
        add(mi);
        return mi;
    }

    /**
     * Decide whether or not to show the popup menu.
     *
     * @param  e  Mouse event.
     */
    protected void showPopup(MouseEvent e) {
        if (clearMenuItem != null) {
            if (textComponent.isEnabled()) {
                clearMenuItem.setEnabled(true);
            } else {
                clearMenuItem.setEnabled(false);
            }
        }
        if (textComponent.getSelectionStart() == textComponent.getSelectionEnd()) {
            copyMenuItem.setEnabled(false);
        } else {
            copyMenuItem.setEnabled(true);
        }
        if (pasteMenuItem != null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Clipboard cb = tk.getSystemClipboard();
            Transferable content = cb.getContents(this);
            if ((content != null) && (textComponent.isEnabled())) {
                pasteMenuItem.setEnabled(true);
            } else {
                pasteMenuItem.setEnabled(false);
            }
        }
        if (selectAllMenuItem != null) {
            if (textComponent.isEnabled()) {
                selectAllMenuItem.setEnabled(true);
            } else {
                selectAllMenuItem.setEnabled(false);
            }
        }
        show(e.getComponent(), e.getX(), e.getY());
    }
}
