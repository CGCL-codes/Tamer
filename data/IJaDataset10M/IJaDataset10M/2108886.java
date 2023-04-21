package com.sshtools.common.ui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class XTextField extends JTextField implements ClipboardOwner {

    private JPopupMenu popup;

    private Action cutAction;

    private Action copyAction;

    private Action pasteAction;

    private Action deleteAction;

    private Action selectAllAction;

    /**
* Creates a new XTextField object.
*/
    public XTextField() {
        this(null, null, 0);
    }

    /**
* Creates a new XTextField object.
*
* @param text
*/
    public XTextField(String text) {
        this(null, text, 0);
    }

    /**
* Creates a new XTextField object.
*
* @param columns
*/
    public XTextField(int columns) {
        this(null, null, columns);
    }

    /**
* Creates a new XTextField object.
*
* @param text
* @param columns
*/
    public XTextField(String text, int columns) {
        this(null, text, columns);
    }

    /**
* Creates a new XTextField object.
*
* @param doc
* @param text
* @param columns
*/
    public XTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        initXtensions();
    }

    /**
*
*
* @param clipboard
* @param contents
*/
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    private void showPopup(int x, int y) {
        requestFocus();
        if (popup == null) {
            popup = new JPopupMenu("Clipboard");
            popup.add(cutAction = new CutAction());
            popup.add(copyAction = new CopyAction());
            popup.add(pasteAction = new PasteAction());
            popup.add(deleteAction = new DeleteAction());
            popup.addSeparator();
            popup.add(selectAllAction = new SelectAllAction());
        }
        cutAction.setEnabled(isEnabled() && (getSelectedText() != null));
        copyAction.setEnabled(isEnabled() && (getSelectedText() != null));
        deleteAction.setEnabled(isEnabled() && (getSelectedText() != null));
        pasteAction.setEnabled(isEnabled() && Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this).isDataFlavorSupported(DataFlavor.stringFlavor));
        selectAllAction.setEnabled(isEnabled());
        popup.show(this, x, y);
    }

    private void initXtensions() {
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    showPopup(evt.getX(), evt.getY());
                }
            }
        });
        addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent evt) {
                XTextField.this.selectAll();
            }

            public void focusLost(FocusEvent evt) {
            }
        });
    }

    class CopyAction extends AbstractAction {

        public CopyAction() {
            putValue(Action.NAME, "Copy");
            putValue(Action.SMALL_ICON, new ResourceIcon(XTextField.class, "copy.png"));
            putValue(Action.SHORT_DESCRIPTION, "Copy");
            putValue(Action.LONG_DESCRIPTION, "Copy the selection from the text and place it in the clipboard");
            putValue(Action.MNEMONIC_KEY, new Integer('c'));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent evt) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(getText()), XTextField.this);
        }
    }

    class CutAction extends AbstractAction {

        public CutAction() {
            putValue(Action.NAME, "Cut");
            putValue(Action.SMALL_ICON, new ResourceIcon(XTextField.class, "cut.png"));
            putValue(Action.SHORT_DESCRIPTION, "Cut selection");
            putValue(Action.LONG_DESCRIPTION, "Cut the selection from the text and place it in the clipboard");
            putValue(Action.MNEMONIC_KEY, new Integer('u'));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent evt) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(getText()), XTextField.this);
            setText("");
        }
    }

    class PasteAction extends AbstractAction {

        public PasteAction() {
            putValue(Action.NAME, "Paste");
            putValue(Action.SMALL_ICON, new ResourceIcon(XTextField.class, "paste.png"));
            putValue(Action.SHORT_DESCRIPTION, "Paste clipboard content");
            putValue(Action.LONG_DESCRIPTION, "Paste the clipboard contents to the current care position or replace the selection");
            putValue(Action.MNEMONIC_KEY, new Integer('p'));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent evt) {
            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    setText(t.getTransferData(DataFlavor.stringFlavor).toString());
                } catch (Exception e) {
                }
            }
        }
    }

    class DeleteAction extends AbstractAction {

        public DeleteAction() {
            putValue(Action.NAME, "Delete");
            putValue(Action.SMALL_ICON, new ResourceIcon(XTextField.class, "delete.png"));
            putValue(Action.SHORT_DESCRIPTION, "Delete selection");
            putValue(Action.LONG_DESCRIPTION, "Delete the selection from the text");
            putValue(Action.MNEMONIC_KEY, new Integer('d'));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent evt) {
            setText("");
        }
    }

    class SelectAllAction extends AbstractAction {

        SelectAllAction() {
            putValue(Action.SMALL_ICON, new EmptyIcon(16, 16));
            putValue(Action.NAME, "Select All");
            putValue(Action.SHORT_DESCRIPTION, "Select All");
            putValue(Action.LONG_DESCRIPTION, "Select all items in the context");
            putValue(Action.MNEMONIC_KEY, new Integer('a'));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent evt) {
            selectAll();
        }
    }
}
