package org.op.service;

public class WwFolderInfo implements WwInfoObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2889153790678812827L;

    private String filepath;

    private Boolean expanded;

    private String name;

    public WwFolderInfo(String label, String path) {
        expanded = false;
        filepath = path;
        name = label;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
