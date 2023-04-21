package org.microskills.ZIPAnywhere;

import java.io.File;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

class FileNode {

    public FileNode(File file) {
        zipJarFileFilter = new ZipJarFileFilter();
        m_file = file;
    }

    public int compareTo(FileNode filenode) {
        return m_file.getName().compareToIgnoreCase(filenode.m_file.getName());
    }

    public boolean expand(DefaultMutableTreeNode defaultmutabletreenode) {
        DefaultMutableTreeNode defaultmutabletreenode1 = (DefaultMutableTreeNode) defaultmutabletreenode.getFirstChild();
        if (defaultmutabletreenode1 == null) {
            return false;
        }
        Object obj = defaultmutabletreenode1.getUserObject();
        if (!(obj instanceof Boolean)) {
            return false;
        }
        defaultmutabletreenode.removeAllChildren();
        File afile[] = listFiles();
        if (afile == null) {
            return false;
        }
        Vector vector = new Vector();
        for (int i = 0; i < afile.length; i++) {
            File file = afile[i];
            FileNode filenode = new FileNode(file);
            boolean flag = false;
            for (int l = 0; l < vector.size(); l++) {
                FileNode filenode3 = (FileNode) vector.elementAt(l);
                if (filenode.compareTo(filenode3) >= 0) {
                    continue;
                }
                vector.insertElementAt(filenode, l);
                flag = true;
                break;
            }
            if (!flag) {
                vector.addElement(filenode);
            }
        }
        for (int j = 0; j < vector.size(); j++) {
            FileNode filenode1 = (FileNode) vector.elementAt(j);
            if (!filenode1.getFile().isFile()) {
                IconData icondata = new IconData(FileTreePanel.ICON_FOLDER, FileTreePanel.ICON_EXPANDEDFOLDER, filenode1);
                DefaultMutableTreeNode defaultmutabletreenode2 = new DefaultMutableTreeNode(icondata);
                defaultmutabletreenode.add(defaultmutabletreenode2);
                File afile1[] = filenode1.getFile().listFiles(zipJarFileFilter);
                if (afile1 != null && afile1.length != 0) {
                    defaultmutabletreenode2.add(new DefaultMutableTreeNode(new Boolean(true)));
                }
            }
        }
        for (int k = 0; k < vector.size(); k++) {
            FileNode filenode2 = (FileNode) vector.elementAt(k);
            if (filenode2.getFile().isFile()) {
                IconData icondata1 = new IconData(FileTreePanel.ICON_ZIPFILE, FileTreePanel.ICON_EXPANDEDFOLDER, filenode2);
                DefaultMutableTreeNode defaultmutabletreenode3 = new DefaultMutableTreeNode(icondata1);
                defaultmutabletreenode.add(defaultmutabletreenode3);
            }
        }
        return true;
    }

    public File getFile() {
        return m_file;
    }

    protected File[] listFiles() {
        if (!m_file.isDirectory()) {
            return null;
        }
        try {
            return m_file.listFiles(new ZipJarFileFilter());
        } catch (Exception _ex) {
            JOptionPane.showMessageDialog(null, "Error reading directory " + m_file.getAbsolutePath(), "Warning", 2);
        }
        return null;
    }

    public String toString() {
        return m_file.getName().length() <= 0 ? m_file.getPath() : m_file.getName();
    }

    protected File m_file;

    ZipJarFileFilter zipJarFileFilter;
}
