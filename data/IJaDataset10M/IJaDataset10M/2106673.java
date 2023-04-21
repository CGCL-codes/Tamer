package net.techwatch.fsindex.event;

import java.util.EventObject;

/**
 * @author wiv
 *
 */
public class FileSystemEvent extends EventObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2327382696643512868L;

    private boolean isFile;

    private long lastUpdate;

    private long id;

    private String name;

    private String parentPath;

    private long parentId;

    public FileSystemEvent(Object source) {
        super(source);
    }

    public FileSystemEvent(Object source, boolean isFile, long lastUpdate, long id, String name, String parentPath, long parentId) {
        super(source);
        this.isFile = isFile;
        this.lastUpdate = lastUpdate;
        this.id = id;
        this.name = name;
        this.parentPath = parentPath;
        this.parentId = parentId;
    }

    /**
	 * @return the isFile
	 */
    public boolean isFile() {
        return isFile;
    }

    /**
	 * @param isFile the isFile to set
	 */
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    /**
	 * @return the lastUpdate
	 */
    public long getLastUpdate() {
        return lastUpdate;
    }

    /**
	 * @param lastUpdate the lastUpdate to set
	 */
    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the parentPath
	 */
    public String getParentPath() {
        return parentPath;
    }

    /**
	 * @param parentPath the parentPath to set
	 */
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /**
	 * @return the parentId
	 */
    public long getParentId() {
        return parentId;
    }

    /**
	 * @param parentId the parentId to set
	 */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
