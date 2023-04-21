package org.tolven.app;

/**
 * Data Transfer Object used to carry MenuData version information.
 * 
 * @author John Churin
 *
 */
public class MDVersionDTO {

    private String element;

    private String path;

    private String role;

    private long version;

    /**
	 * This is the element corresponding to the UI.
	 * @return
	 */
    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    /**
	 * This is the path to the actual list in the database (this and element may be identical).
	 */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * The version number of the list corresponding to Path.
	 * @return
	 */
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
