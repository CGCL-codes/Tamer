package net.sourceforge.vietpad;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import net.sourceforge.vietpad.utilities.VietUtilities;

/**
 *  Implementation of sort function.
 *
 *@author     Quan Nguyen
 *@author     Gero Herrmann
 *@created    February 11, 2002
 *@version    1.8, 5 May 2007
 */
public class VietPadWithSort extends VietPadWithTools {

    private SortDialog sortDlg;

    public VietPadWithSort() {
        addToToolsMenu();
    }

    /**
     * Adds Sort Lines item to Tools menu.
     */
    private void addToToolsMenu() {
        ResourceBundle bundle = ResourceBundle.getBundle("net/sourceforge/vietpad/VietPad");
        JMenu mTools = menuBar.getMenu(menuBar.getMenuCount() - 2);
        Action sortAction = new AbstractAction(myResources.getString("Sort_Lines") + "...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortDlg == null) {
                    sortDlg = new SortDialog(VietPadWithSort.this, false);
                    sortDlg.setReverse(prefs.getBoolean("sortReverse", false));
                    sortDlg.setLocation(prefs.getInt("sortX", sortDlg.getX()), prefs.getInt("sortY", sortDlg.getY()));
                }
                if (m_editor.getSelectedText() == null) {
                    m_editor.selectAll();
                }
                sortDlg.setVisible(true);
            }
        };
        JMenuItem item = mTools.insert(sortAction, 0);
        item.setMnemonic(bundle.getString("jMenuItemSort.Mnemonic").charAt(0));
    }

    /**
     *  Sorts lines.
     *
     *@param  reverse    True for reverse sorting
     *@param  delimiter  Delimiter for Left-to-Right sorting; empty if not needed
     */
    protected void sort(final boolean reverse, final String delimiter) {
        if (m_editor.getSelectedText() == null) {
            m_editor.selectAll();
            return;
        }
        int start = m_editor.getSelectionStart();
        if (start != 0 && m_editor.getText().charAt(start - 1) != '\n') {
            try {
                int lineStart = m_editor.getLineStartOffset(m_editor.getLineOfOffset(start));
                start = lineStart;
                m_editor.setSelectionStart(start);
            } catch (BadLocationException e) {
                System.err.println(e);
            }
        }
        int end = m_editor.getSelectionEnd();
        if (end != m_editor.getDocument().getLength() && m_editor.getText().charAt(end) != '\n') {
            try {
                int lineEnd = m_editor.getLineEndOffset((m_editor.getLineOfOffset(end)));
                if (m_editor.getDocument().getLength() == lineEnd) {
                    end = lineEnd;
                } else {
                    end = lineEnd - 1;
                }
                m_editor.setSelectionEnd(end);
            } catch (BadLocationException e) {
                System.err.println(e);
            }
        }
        String[] words = m_editor.getSelectedText().split("\n");
        VietUtilities.sort(words, reverse, delimiter);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(words[i]).append("\n");
        }
        result.setLength(result.length() - 1);
        getUndoSupport().beginUpdate();
        m_editor.replaceSelection(result.toString());
        setSelection(start, start + result.length());
        getUndoSupport().endUpdate();
    }

    /**
     *  Updates UI component if changes in LAF.
     *
     *@param  laf  The look and feel class name
     */
    @Override
    protected void updateLaF(String laf) {
        super.updateLaF(laf);
        if (sortDlg != null) {
            SwingUtilities.updateComponentTreeUI(sortDlg);
            sortDlg.pack();
        }
    }

    /**
     *  Remembers settings and dialog locations, then quits.
     */
    @Override
    protected void quit() {
        if (sortDlg != null) {
            prefs.putBoolean("sortReverse", sortDlg.isReverse());
            prefs.putInt("sortX", sortDlg.getX());
            prefs.putInt("sortY", sortDlg.getY());
        }
        super.quit();
    }

    /**
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new VietPadWithSort().setVisible(true);
            }
        });
    }
}
