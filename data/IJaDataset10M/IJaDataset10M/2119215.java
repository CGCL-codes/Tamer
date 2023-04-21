package org.tidelaget.gui.panel;

import org.tidelaget.*;
import org.tidelaget.core.*;
import org.tidelaget.gui.*;
import org.tidelaget.gui.tree.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class FilePanel extends TIDEPanel {

    protected static Caret m_caret = new DefaultCaret();

    protected JTextArea m_textArea;

    protected File m_file;

    protected long m_lastModified;

    public FilePanel(String name, Object id, File f, String text) {
        super(name, id);
        m_file = f;
        m_caret.setBlinkRate(600);
        m_lastModified = m_file.lastModified();
        setLayout(new BorderLayout());
        m_textArea = new JTextArea();
        JScrollPane scrlPane = new JScrollPane(m_textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrlPane, BorderLayout.CENTER);
        m_textArea.setText(text);
        m_textArea.setCaretPosition(0);
        registerAlterTextListener(m_textArea);
        registerPopupListener(m_textArea);
        m_textArea.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent event) {
                IDEController.get().getStatusPanel().updateFileInfo(m_file);
                IDEController.get().getStatusPanel().updateKeyMode(null);
            }
        });
        m_textArea.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent event) {
                checkFileStatus();
            }
        });
        m_textArea.setEditable(isWritable());
        IDEController.get().getStatusPanel().updateFileInfo(m_file);
    }

    public String getEditText() {
        return m_textArea.getText();
    }

    public JTextArea getEditTextArea() {
        return m_textArea;
    }

    /**
	 * Returns true if this file is editable
	 */
    public boolean isWritable() {
        return (m_file.canWrite() || !m_file.exists());
    }

    /**
	 * Saves this file
	 */
    public boolean save() {
        checkFileStatus();
        if (isAltered()) if (IDEController.get().saveFile(m_file, getEditText())) {
            m_lastModified = m_file.lastModified();
            setAltered(false);
            IDEController.get().getStatusPanel().updateFileInfo(m_file);
            return true;
        } else return false; else return true;
    }

    /**
	 * Saves this file under another filename
	 */
    public boolean saveAs(File anotherFile) {
        checkFileStatus();
        try {
            if (anotherFile.exists()) {
                int answer = JOptionPane.showConfirmDialog(IDEController.get().getMainFrame(), "There already exists a file with that name. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) anotherFile.createNewFile(); else return false;
            }
            FileWriter fw = new FileWriter(anotherFile);
            fw.write(getEditText(), 0, getEditText().length());
            fw.close();
            m_lastModified = anotherFile.lastModified();
            renameTab(anotherFile.getName());
            setName(anotherFile.getName());
            m_id = anotherFile.getAbsolutePath();
            m_file = anotherFile;
            setAltered(false);
            IDEController.get().getStatusPanel().updateFileInfo(m_file);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(IDEController.get().getMainFrame(), "Could not save file " + anotherFile.getName() + ".", "File error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
	 * Checks if the file has removed any writeprotection, or if it
	 * has been altered by another application.
	 */
    protected void checkFileStatus() {
        if (isWritable() && !m_textArea.isEditable()) {
            m_textArea.setEditable(isWritable());
            m_textArea.setCaret(m_caret);
        } else m_textArea.setEditable(isWritable());
        if (m_file.lastModified() > m_lastModified) {
            m_lastModified = m_file.lastModified();
            int answer = JOptionPane.showConfirmDialog(IDEController.get().getMainFrame(), "File " + m_file.getName() + " has been altered by " + "another application. Reload file?", "File changed", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                if (isAltered()) {
                    answer = JOptionPane.showConfirmDialog(IDEController.get().getMainFrame(), "All changes will be lost if the file is reloaded. Continue?", "Warning!", JOptionPane.YES_NO_OPTION);
                }
                if (answer == JOptionPane.YES_OPTION) {
                    String contents = IDEController.get().readFile(m_file);
                    if (contents != null) {
                        m_textArea.setText(contents);
                        setAltered(false);
                    }
                }
            }
        }
    }
}
