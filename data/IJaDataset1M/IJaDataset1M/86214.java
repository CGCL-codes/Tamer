package net.sourceforge.vietpad;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.Date;
import javax.swing.undo.*;

public class VietPadWithEdit extends VietPadWithPrinting {

    private Action actionCut, actionCopy, actionPaste, actionDelete;

    private Action m_redoAction, m_undoAction;

    private FindDialog m_findDialog;

    private final JPopupMenu popup = new JPopupMenu();

    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private boolean bMatchDiacritics, bMatchWholeWord, bMatchCase, bRegex;

    /**
     *  Creates a new instance of VietPadWithEdit
     */
    public VietPadWithEdit() {
        super();
        bMatchDiacritics = prefs.getBoolean("MatchDiacritics", false);
        bMatchWholeWord = prefs.getBoolean("MatchWholeWord", false);
        bMatchCase = prefs.getBoolean("MatchCase", false);
        bRegex = prefs.getBoolean("RegEx", false);
        menuBar.add(createEditMenu(), menuBar.getMenuCount() - 1);
    }

    /**
     *  Creates the Edit menu
     *
     *@return    the Menu Object
     */
    private JMenu createEditMenu() {
        JMenu menu = new JMenu(myResources.getString("Edit"));
        menu.setMnemonic('e');
        m_undoAction = new AbstractAction(myResources.getString("Undo")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    m_undo.undo();
                    updateSave(true);
                } catch (CannotUndoException ex) {
                    System.err.println("Unable to undo: " + ex);
                }
                updateUndoRedo();
            }
        };
        JMenuItem item = menu.add(m_undoAction);
        item.setMnemonic('u');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, MENU_MASK));
        popup.add(m_undoAction);
        m_redoAction = new AbstractAction(myResources.getString("Redo")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    m_undo.redo();
                    updateSave(true);
                } catch (CannotRedoException ex) {
                    System.err.println("Unable to redo: " + ex);
                }
                updateUndoRedo();
            }
        };
        item = menu.add(m_redoAction);
        item.setMnemonic('r');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, MENU_MASK));
        popup.add(m_redoAction);
        popup.addSeparator();
        menu.addSeparator();
        actionCut = new AbstractAction(myResources.getString("Cut")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_editor.cut();
                updatePaste();
            }
        };
        JMenuItem itemCut = menu.add(actionCut);
        itemCut.setMnemonic('t');
        itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, MENU_MASK));
        popup.add(actionCut);
        actionCopy = new AbstractAction(myResources.getString("Copy")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_editor.copy();
                updatePaste();
            }
        };
        JMenuItem itemCopy = menu.add(actionCopy);
        itemCopy.setMnemonic('c');
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, MENU_MASK));
        popup.add(actionCopy);
        actionPaste = new AbstractAction(myResources.getString("Paste")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                undoSupport.beginUpdate();
                m_editor.paste();
                undoSupport.endUpdate();
            }
        };
        JMenuItem itemPaste = menu.add(actionPaste);
        itemPaste.setMnemonic('p');
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, MENU_MASK));
        popup.add(actionPaste);
        actionDelete = new AbstractAction(myResources.getString("Delete")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_editor.replaceSelection(null);
            }
        };
        JMenuItem itemDelete = menu.add(actionDelete);
        itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        popup.add(actionDelete);
        popup.addSeparator();
        menu.addSeparator();
        m_toolBar.add(new SmallButton(actionCut, myResources.getString("Cut"), createIcon("Cut")));
        m_toolBar.add(new SmallButton(actionCopy, myResources.getString("Copy"), createIcon("Copy")));
        m_toolBar.add(new SmallButton(actionPaste, myResources.getString("Paste"), createIcon("Paste")));
        m_toolBar.addSeparator();
        m_toolBar.add(new SmallButton(m_undoAction, myResources.getString("Undo"), createIcon("Undo")));
        m_toolBar.add(new SmallButton(m_redoAction, myResources.getString("Redo"), createIcon("Redo")));
        m_toolBar.addSeparator();
        Action findAction = new AbstractAction(myResources.getString("Find") + "...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_findDialog == null) {
                    m_findDialog = new FindDialog(VietPadWithEdit.this, 0);
                    m_findDialog.setMatchDiacritics(bMatchDiacritics);
                    m_findDialog.setMatchWholeWord(bMatchWholeWord);
                    m_findDialog.setMatchCase(bMatchCase);
                    m_findDialog.setRegEx(bRegex);
                } else {
                    m_findDialog.setSelectedIndex(0);
                }
                m_findDialog.setVisible(true);
            }
        };
        item = menu.add(findAction);
        item.setMnemonic('f');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, MENU_MASK));
        m_toolBar.add(new SmallButton(findAction, myResources.getString("Find"), createIcon("Find")));
        m_toolBar.addSeparator();
        Action replaceAction = new AbstractAction(myResources.getString("Replace") + "...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_findDialog == null) {
                    m_findDialog = new FindDialog(VietPadWithEdit.this, 1);
                    m_findDialog.setMatchDiacritics(bMatchDiacritics);
                    m_findDialog.setMatchWholeWord(bMatchWholeWord);
                    m_findDialog.setMatchCase(bMatchCase);
                    m_findDialog.setRegEx(bRegex);
                } else {
                    m_findDialog.setSelectedIndex(1);
                }
                m_findDialog.setVisible(true);
            }
        };
        item = menu.add(replaceAction);
        item.setMnemonic('l');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, MENU_MASK));
        menu.addSeparator();
        Action actionSelectAll = new AbstractAction(myResources.getString("Select_All"), null) {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_editor.selectAll();
            }
        };
        item = menu.add(actionSelectAll);
        item.setMnemonic('a');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, MENU_MASK));
        popup.add(actionSelectAll);
        m_editor.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed(e);
            }
        });
        menu.add(new AbstractAction(myResources.getString("Date_&_Time"), null) {

            DateFormat df;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (df == null) {
                    df = new SimpleDateFormat("EEEE, 'ngày' d 'tháng' M 'năm' yyyy, HH:mm:ss Z", VIETNAM);
                }
                undoSupport.beginUpdate();
                m_editor.replaceSelection(df.format(new Date()));
                undoSupport.endUpdate();
            }
        });
        return menu;
    }

    /**
     *  Updates the Undo and Redo actions
     */
    @Override
    void updateUndoRedo() {
        m_undoAction.setEnabled(m_undo.canUndo());
        m_redoAction.setEnabled(m_undo.canRedo());
    }

    /**
     *  Updates the Cut, Copy, and Delete actions
     *
     *@param  isTextSelected  whether any text currently selected
     */
    @Override
    void updateCutCopyDelete(boolean isTextSelected) {
        actionCut.setEnabled(isTextSelected);
        actionCopy.setEnabled(isTextSelected);
        actionDelete.setEnabled(isTextSelected);
    }

    /**
     *  Updates the Paste action
     */
    @Override
    void updatePaste() {
        try {
            Transferable clipData = clipboard.getContents(clipboard);
            if (clipData != null) {
                actionPaste.setEnabled(clipData.isDataFlavorSupported(DataFlavor.stringFlavor));
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, APP_NAME + myResources.getString("_has_run_out_of_memory.\nPlease_restart_") + APP_NAME + myResources.getString("_and_try_again."), myResources.getString("Out_of_Memory"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Updates UI component if changes in LAF
     *
     *@param  laf  the look and feel class name
     */
    @Override
    void updateLaF(String laf) {
        super.updateLaF(laf);
        SwingUtilities.updateComponentTreeUI(popup);
        if (m_findDialog != null) {
            SwingUtilities.updateComponentTreeUI(m_findDialog);
        }
    }

    /**
     *  Quits the application
     */
    @Override
    protected void quit() {
        if (m_findDialog != null) {
            prefs.putBoolean("MatchDiacritics", m_findDialog.isMatchDiacritics());
            prefs.putBoolean("MatchWholeWord", m_findDialog.isMatchWholeWord());
            prefs.putBoolean("RegEx", m_findDialog.isRegEx());
            prefs.putBoolean("MatchCase", m_findDialog.isMatchCase());
        }
        super.quit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new VietPadWithEdit();
    }
}
