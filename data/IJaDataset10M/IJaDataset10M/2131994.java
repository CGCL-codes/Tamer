package jsave.tree;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jsave.filter.JSaveFileFilterDestination;
import jsave.util.Console;
import org.apache.log4j.Logger;

public class FileNode implements Serializable {

    static final long serialVersionUID = 6896676213762520936L;

    protected static transient FileFilter filter = null;

    protected static transient Logger log = null;

    static {
        filter = new JSaveFileFilterDestination();
        log = Logger.getLogger(FileNode.class);
    }

    private long lastUpdateDatetime;

    private List<FileNode> childrenNodes = null;

    private boolean directory = true;

    private FileNode parentNode = null;

    protected String name;

    public FileNode(File file, FileNode parentNode) {
        this(file);
        this.parentNode = parentNode;
    }

    public FileNode(File file) {
        if (file.isFile()) {
            this.lastUpdateDatetime = file.lastModified();
            directory = false;
        }
        name = file.getName();
        childrenNodes = new ArrayList<FileNode>();
        if (file != null && file.exists() && file.isDirectory()) {
            Console.logParcours(file.getPath());
            File[] childs = file.listFiles(filter);
            for (int i = 0; i < childs.length; i++) {
                try {
                    childrenNodes.add(new FileNode(childs[i], this));
                } catch (NullPointerException e) {
                    if (childs[i].getPath().length() > 255) {
                        log.warn("Le fichier a un chemin de plus de 255 caracteres : " + file.getPath());
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    public long lastModified() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(long lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public List<FileNode> getChildrenNodes() {
        return childrenNodes;
    }

    public void setChildrenNodes(List<FileNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void afficherHierarchy() {
        afficherHierarchy(0);
    }

    public void afficherHierarchy(int level) {
        String espace = "";
        for (int i = 0; i < level; i++) {
            espace += " ";
        }
        if (directory) {
            log.info(espace + "[" + name + "]");
            for (int i = 0; i < childrenNodes.size(); i++) {
                childrenNodes.get(i).afficherHierarchy(level + 1);
            }
        } else {
            log.info(espace + name);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public FileNode getChildNodeNamed(String nom) {
        for (int i = childrenNodes.size() - 1; i >= 0; i--) {
            if (childrenNodes.get(i).name.equals(nom)) {
                return childrenNodes.get(i);
            }
        }
        return null;
    }

    public FileNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(FileNode parenteNode) {
        this.parentNode = parenteNode;
    }

    public String getRelativePath() {
        if (parentNode != null) {
            return parentNode.getRelativePath() + File.separator + name;
        }
        return "";
    }

    public void addChild(FileNode childNode) {
        childrenNodes.add(childNode);
    }
}
