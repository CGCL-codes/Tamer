package org.gjt.sp.jedit.browser;

import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;
import org.gjt.sp.jedit.gui.EnhancedDialog;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.jedit.*;

/**
 * Wraps the VFS browser in a modal dialog.
 * @author Slava Pestov
 * @version $Id: VFSFileChooserDialog.java,v 1.10 2000/08/29 07:47:12 sp Exp $
 */
public class VFSFileChooserDialog extends EnhancedDialog {

    public VFSFileChooserDialog(View view, String path, int mode, boolean multipleSelection) {
        super(view, jEdit.getProperty("vfs.browser.title"), true);
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);
        String name;
        if (path == null || path.endsWith(File.separator) || path.endsWith("/")) name = null; else {
            name = MiscUtilities.getFileName(path);
            path = MiscUtilities.getParentOfPath(path);
        }
        browser = new VFSBrowser(view, path, mode, multipleSelection);
        browser.addBrowserListener(new BrowserHandler());
        content.add(BorderLayout.CENTER, browser);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));
        if (mode == VFSBrowser.SAVE_DIALOG) {
            panel.add(new JLabel(jEdit.getProperty("vfs.browser.dialog.filename")));
            panel.add(Box.createHorizontalStrut(12));
            filenameField = new JTextField(20);
            filenameField.setText(name);
            Dimension dim = filenameField.getPreferredSize();
            dim.width = Integer.MAX_VALUE;
            filenameField.setMaximumSize(dim);
            Box box = new Box(BoxLayout.Y_AXIS);
            box.add(Box.createGlue());
            box.add(filenameField);
            box.add(Box.createGlue());
            panel.add(box);
            GUIUtilities.requestFocus(this, filenameField);
            panel.add(Box.createHorizontalStrut(12));
        } else {
            GUIUtilities.requestFocus(this, browser.getBrowserView().getDefaultFocusComponent());
            panel.add(Box.createGlue());
        }
        ok = new JButton(jEdit.getProperty("vfs.browser.dialog." + (mode == VFSBrowser.OPEN_DIALOG ? "open" : "save")));
        ok.addActionListener(new ActionHandler());
        getRootPane().setDefaultButton(ok);
        panel.add(ok);
        panel.add(Box.createHorizontalStrut(6));
        cancel = new JButton(jEdit.getProperty("common.cancel"));
        cancel.addActionListener(new ActionHandler());
        panel.add(cancel);
        if (mode != VFSBrowser.SAVE_DIALOG) panel.add(Box.createGlue());
        content.add(BorderLayout.SOUTH, panel);
        pack();
        GUIUtilities.loadGeometry(this, "vfs.browser.dialog." + (mode == VFSBrowser.OPEN_DIALOG ? "open" : "save"));
        show();
    }

    public void dispose() {
        GUIUtilities.saveGeometry(this, "vfs.browser.dialog." + (browser.getMode() == VFSBrowser.OPEN_DIALOG ? "open" : "save"));
        super.dispose();
    }

    public void ok() {
        if (filenameField != null) {
            String filename = filenameField.getText();
            if (filename.length() == 0) {
                getToolkit().beep();
                return;
            } else {
                if (browser.getMode() == VFSBrowser.SAVE_DIALOG) {
                    if (doFileExistsWarning(filename)) return;
                }
            }
        } else {
            VFS.DirectoryEntry[] files = browser.getSelectedFiles();
            if (files.length == 0) return;
            for (int i = 0; i < files.length; i++) {
                VFS.DirectoryEntry file = files[i];
                if (file.type == VFS.DirectoryEntry.FILESYSTEM || file.type == VFS.DirectoryEntry.DIRECTORY) {
                    browser.setDirectory(file.path);
                    return;
                }
            }
        }
        isOK = true;
        dispose();
    }

    public void cancel() {
        dispose();
    }

    public String[] getSelectedFiles() {
        if (!isOK) return null;
        if (filenameField != null) {
            String directory = browser.getDirectory();
            VFS vfs = VFSManager.getVFSForPath(directory);
            String[] retVal = { vfs.constructPath(directory, filenameField.getText()) };
            return retVal;
        } else {
            Vector vector = new Vector();
            VFS.DirectoryEntry[] selectedFiles = browser.getSelectedFiles();
            for (int i = 0; i < selectedFiles.length; i++) {
                VFS.DirectoryEntry file = selectedFiles[i];
                if (file.type == VFS.DirectoryEntry.FILE) vector.addElement(file.path);
            }
            String[] retVal = new String[vector.size()];
            vector.copyInto(retVal);
            return retVal;
        }
    }

    private VFSBrowser browser;

    private JTextField filenameField;

    private JButton ok;

    private JButton cancel;

    private boolean isOK;

    private boolean doFileExistsWarning(String filename) {
        String directory = browser.getDirectory();
        VFS vfs = VFSManager.getVFSForPath(directory);
        filename = vfs.constructPath(directory, filename);
        if (new File(filename).exists()) {
            String[] args = { MiscUtilities.getFileName(filename) };
            int result = JOptionPane.showConfirmDialog(browser, jEdit.getProperty("fileexists.message", args), jEdit.getProperty("fileexists.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.YES_OPTION) return true;
        }
        return false;
    }

    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == ok) ok(); else if (evt.getSource() == cancel) cancel();
        }
    }

    class BrowserHandler implements BrowserListener {

        public void filesSelected(VFSBrowser browser, VFS.DirectoryEntry[] files) {
            if (files.length == 0) return;
            if (filenameField != null) {
                VFS.DirectoryEntry file = files[0];
                if (file.type == VFS.DirectoryEntry.FILE) {
                    String path = file.path;
                    if (path.startsWith(browser.getDirectory())) path = file.name;
                    filenameField.setText(path);
                }
            }
        }

        public void filesActivated(VFSBrowser browser, VFS.DirectoryEntry[] files) {
            for (int i = 0; i < files.length; i++) {
                VFS.DirectoryEntry file = files[i];
                if (file.type == VFS.DirectoryEntry.FILESYSTEM || file.type == VFS.DirectoryEntry.DIRECTORY) {
                    return;
                }
            }
            ok();
        }
    }
}
