package gui;

import net.*;
import util.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * @author Ryan Sweny
 * @version $Revision: 1.1 $ $Date: 2002/05/08 23:40:01 $
 */
public class FileNode {

    public String file;

    public int size;

    private ArrayList children = new ArrayList();

    private FileNode parent = null;

    public FileNode(String file, int size) {
        this.file = file;
        this.size = size;
    }

    public Object getChild(int i) {
        if (children != null) return children.get(i); else return null;
    }

    public void addChild(FileNode node) {
        children.add(node);
    }

    public FileNode getParent() {
        return parent;
    }

    public void setParent(FileNode node) {
        parent = node;
    }

    public int getNumChildren() {
        if (children != null) return children.size(); else return 0;
    }

    public String getSearchName() {
        if (file.charAt(0) == File.separator.charAt(0)) return file.substring(1); else return file;
    }

    public String toString() {
        return FileUtils.getName(file);
    }
}
