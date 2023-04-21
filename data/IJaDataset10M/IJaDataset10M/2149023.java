package org.openi.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class Folder {

    private static Logger logger = Logger.getLogger(Folder.class);

    private String path = "";

    private String displayName = "";

    private Collection children = null;

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Collection getChildren() {
        return children;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void accept(FolderVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor can not be null");
        }
        traverse(this, visitor);
    }

    private void traverse(Folder folder, FolderVisitor visitor) {
        if (folder.getChildren() != null) {
            for (Iterator iter = folder.getChildren().iterator(); iter.hasNext(); ) {
                Object item = iter.next();
                if (item instanceof Folder) {
                    visitor.visit((Folder) item);
                    traverse((Folder) item, visitor);
                } else {
                    visitor.visit((FileItem) item);
                }
            }
        }
    }

    public void setChildren(Collection children) {
        this.children = children;
    }

    public static Folder findChildFolder(String path, Folder folder) {
        Folder current = folder;
        path = path.replace('\\', '/');
        logger.info("checking:" + folder.getPath());
        if (path.equals(current.getPath().replace('\\', '/'))) {
            logger.info("found path:" + path + " at folder:" + current.getPath());
            return current;
        }
        if ((current.getChildren() == null) || (current.getChildren().size() == 0)) {
            return null;
        }
        Iterator iter = current.getChildren().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (item instanceof Folder) {
                Folder fol = findChildFolder(path, (Folder) item);
                if (fol != null) {
                    return fol;
                }
            }
        }
        return null;
    }

    public static void buildChildList(Folder folder, List folders, List files) {
        if (folder == null) {
            return;
        }
        if ((folder.getChildren() == null) || (folder.getChildren().size() == 0)) {
            return;
        }
        Iterator iter = folder.getChildren().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if ((files != null) && item instanceof FileItem) {
                files.add(((FileItem) item).getPath());
            } else if (item instanceof Folder) {
                Folder current = (Folder) item;
                if (folders != null) {
                    folders.add(current.getPath());
                }
                buildChildList(current, folders, files);
            }
        }
    }
}
