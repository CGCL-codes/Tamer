package de.enough.polish.io.file;

import java.util.Enumeration;
import de.enough.polish.android.io.Connector;
import de.enough.polish.android.io.file.FileConnection;
import de.enough.polish.android.io.file.FileSystemRegistry;
import de.enough.polish.ui.TreeModel;
import de.enough.polish.util.ArrayList;

/**
 * Provides the file system of a device as a TreeModel.
 * For working efficiently with a TreeItem, use UiFileSystemTreeModel.
 * 
 * @author Robert Virkus
 * @see de.enough.polish.ui.TreeModel
 * @see de.enough.polish.ui.TreeItem
 * @see UiFileSystemTreeModel
 */
public class FileSystemTreeModel implements TreeModel {

    private FileConnection fileConnection;

    public void addChildren(Object parent, ArrayList list) {
        if (parent == this) {
            Enumeration enumeration = FileSystemRegistry.listRoots();
            while (enumeration.hasMoreElements()) {
                String path = (String) enumeration.nextElement();
                list.add(new FileSystemNode(path, null));
            }
        } else if (!(parent instanceof FileSystemNode)) {
        } else {
            FileSystemNode node = (FileSystemNode) parent;
            String nodePath = node.getPath();
            boolean establishNewFileConnection = false;
            if (this.fileConnection == null) {
                establishNewFileConnection = true;
            } else {
                String currentPath = this.fileConnection.getPath();
                int pos = nodePath.indexOf(currentPath);
                String newPathName;
                if (pos != -1) {
                    newPathName = nodePath.substring(pos + currentPath.length());
                } else {
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < currentPath.length(); i++) {
                        char c = currentPath.charAt(i);
                        if (c == '/') {
                            buffer.append("../");
                        }
                    }
                    buffer.append(nodePath);
                    newPathName = buffer.toString();
                }
                try {
                    this.fileConnection.setFileConnection(newPathName);
                } catch (Exception e) {
                    de.enough.polish.util.Debug.debug("error", "de.enough.polish.io.file.FileSystemTreeModel", 93, "Unable to reset fileconnection to \"" + newPathName + "\"", e);
                    establishNewFileConnection = true;
                }
            }
            if (establishNewFileConnection) {
                if (this.fileConnection != null) {
                    try {
                        this.fileConnection.close();
                    } catch (Exception e) {
                    }
                }
                try {
                    this.fileConnection = (FileConnection) Connector.open("file:///" + node.getAbsolutePath(), Connector.READ);
                } catch (Exception e) {
                    de.enough.polish.util.Debug.debug("error", "de.enough.polish.io.file.FileSystemTreeModel", 112, "Unable to open file connection to \"file:///" + node.getAbsolutePath() + "\"", e);
                    return;
                }
            }
            try {
                Enumeration enumeration = this.fileConnection.list("*", true);
                while (enumeration.hasMoreElements()) {
                    String path = (String) enumeration.nextElement();
                    list.add(new FileSystemNode(path, node));
                }
                if (list.size() == 0) {
                    enumeration = this.fileConnection.list();
                    while (enumeration.hasMoreElements()) {
                        String path = (String) enumeration.nextElement();
                        list.add(new FileSystemNode(path, node));
                    }
                }
            } catch (Exception e) {
                de.enough.polish.util.Debug.debug("error", "de.enough.polish.io.file.FileSystemTreeModel", 133, "Unable to list contents of \"" + this.fileConnection.getPath() + "\"", e);
            }
        }
    }

    public Object getRoot() {
        return this;
    }

    public boolean isLeaf(Object node) {
        if (node instanceof FileSystemNode) {
            FileSystemNode fsNode = (FileSystemNode) node;
            return !fsNode.isDirectory();
        }
        return true;
    }
}
