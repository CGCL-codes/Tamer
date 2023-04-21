package com.stakface.ocmd.gui.plugins;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import com.stakface.ocmd.*;
import com.stakface.ocmd.gui.*;
import com.stakface.ocmd.util.*;

public class DirectoryTree extends FilePane implements TreeSelectionListener {

    private JTree _tree;

    public DirectoryTree(OCmd ocmd) {
        super(ocmd);
        setLayout(new GridBagLayout());
        refresh();
    }

    public String getName() {
        DirectoryNode rootNode = getRootNode();
        if (rootNode == null) {
            return "<No listing>";
        }
        return "Listing: " + getCurrentDirectory().getName() + File.separatorChar;
    }

    public File getCurrentDirectory() {
        DirectoryNode rootNode = getRootNode();
        if (rootNode != null) {
            return rootNode.getFile();
        }
        return null;
    }

    private DirectoryNode getRootNode() {
        return (_tree == null ? null : (DirectoryNode) _tree.getModel().getRoot());
    }

    public File getActiveFile() {
        if (_tree == null) {
            return null;
        }
        return getFileFromTreePath(_tree.getSelectionPath());
    }

    public File[] getSelectedFiles() {
        if (_tree == null) {
            return null;
        }
        TreePath[] selected = _tree.getSelectionPaths();
        File[] selectedFiles = new File[selected.length];
        for (int i = 0; i < selectedFiles.length; i++) {
            selectedFiles[i] = getFileFromTreePath(selected[i]);
        }
        return selectedFiles;
    }

    private File getFileFromTreePath(TreePath path) {
        return (path == null ? null : ((DirectoryNode) path.getLastPathComponent()).getFile());
    }

    public void refresh() {
        if (_tree != null) {
            _tree.removeTreeSelectionListener(this);
        }
        removeAll();
        _tree = new JTree(new DirectoryNode());
        add(_tree, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        _tree.addTreeSelectionListener(this);
        validate();
    }

    public void valueChanged(TreeSelectionEvent evt) {
        TreePath[] selectionChanged = evt.getPaths();
        if (selectionChanged.length == 0) {
            return;
        } else if (selectionChanged.length == 1) {
            _ocmd.fireOCmdEvent(this, evt.isAddedPath() ? OCmdEvent.EventType.FILE_SELECTED : OCmdEvent.EventType.FILE_DESELECTED, null, getFileFromTreePath(evt.getPath()));
        } else {
            _ocmd.fireOCmdEvent(this, OCmdEvent.EventType.SELECTED_FILES, null, getSelectedFiles());
        }
        _ocmd.fireOCmdEvent(this, OCmdEvent.EventType.ACTIVE_FILE, getFileFromTreePath(evt.getOldLeadSelectionPath()), getFileFromTreePath(evt.getNewLeadSelectionPath()));
    }
}

class DirectoryNode implements TreeNode, Enumeration {

    private static final Comparator<File> FILE_SORTER = FileComparatorFactory.getFileSorter(FileComparatorFactory.PROPERTY_NAME, true);

    private File _file;

    private DirectoryNode _parent;

    private DirectoryNode[] _children;

    private int _enumeratorIndex;

    public DirectoryNode() {
        this(File.listRoots()[0], null);
    }

    public DirectoryNode(File file, DirectoryNode parent) {
        _file = file;
        _parent = parent;
    }

    private void initialize() {
        File[] files = _file.listFiles();
        if (_file.isDirectory() && files != null) {
            Arrays.sort(files, FILE_SORTER);
            _children = new DirectoryNode[files.length];
            for (int i = 0; i < _children.length; i++) {
                _children[i] = new DirectoryNode(files[i], this);
            }
        } else {
            _children = new DirectoryNode[0];
        }
    }

    public File getFile() {
        return _file;
    }

    public Enumeration children() {
        return this;
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int index) {
        if (_children == null) {
            initialize();
        }
        if (index < 0 || index >= _children.length) {
            return null;
        }
        return _children[index];
    }

    public int getChildCount() {
        if (_children == null) {
            initialize();
        }
        return _children.length;
    }

    public int getIndex(TreeNode node) {
        if (_children == null) {
            initialize();
        }
        for (int i = 0; i < _children.length; i++) {
            if (_children[i] == node) {
                return i;
            }
        }
        return -1;
    }

    public TreeNode getParent() {
        return _parent;
    }

    public boolean isLeaf() {
        return (!_file.isDirectory());
    }

    public boolean hasMoreElements() {
        if (_children == null) {
            initialize();
        }
        return (_enumeratorIndex < _children.length);
    }

    public Object nextElement() {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }
        return _children[_enumeratorIndex++];
    }

    public String toString() {
        return _file.getName() + (_parent == null ? File.separator : "");
    }
}
