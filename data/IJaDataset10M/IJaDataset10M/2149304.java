package org.jfm.po;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import org.jfm.main.Options;
import org.jfm.event.*;
import org.jfm.filesystems.JFMFile;

/**
 * Title:        Java File Manager
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Home
 * @author Giurgiu Sergiu
 * @version 1.0
 */
public class DeleteAction implements Action {

    public DeleteAction() {
    }

    private boolean enabled = true;

    public Object getValue(String key) {
        return null;
    }

    public void putValue(String key, Object value) {
    }

    public void setEnabled(boolean b) {
        enabled = b;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    public void actionPerformed(ActionEvent e) {
        JFMFile[] files = Options.getActivePanel().getSelectedFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        if (files.length == 1) {
            int result = JOptionPane.showConfirmDialog(Options.getMainFrame(), "Do you want to delete " + files[0].toString() + "?", "Delete", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) return;
        } else {
            int result = JOptionPane.showConfirmDialog(Options.getMainFrame(), "Do you want to delete " + files.length + " files?", "Delete", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) return;
        }
        for (int i = 0; i < files.length; i++) {
            deleteFile(files[i]);
        }
        ChangeDirectoryEvent ev = new ChangeDirectoryEvent();
        ev.setSource(this);
        ev.setDirectory(Options.getActivePanel().getCurrentWorkingDirectory());
        Broadcaster.notifyChangeDirectoryListeners(ev);
    }

    private void deleteFile(JFMFile fi) {
        if (fi.isDirectory()) {
            JFMFile[] list = fi.listFiles();
            for (int i = 0; i < list.length; i++) {
                deleteFile(list[i]);
            }
            fi.delete();
        } else {
            fi.delete();
        }
    }
}
