package uy.com.ideasoft.pronaos.browse;

/**
 * @author martin
 *
 */
public class Child {

    private String creator;

    private long size;

    private String modified;

    private String name;

    private String path;

    private String contentPath;

    private String childId;

    private boolean container;

    public void setChildId(String id) {
        this.childId = id;
    }

    public String getChildId() {
        return childId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isContainer() {
        return container;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String toString() {
        return name + (container ? " folder" : " file") + " at " + path;
    }
}
