package welo.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Bean that is set to every node of tree as user object.
 *  
 * @author Matej Knopp
 */
public class TreeNodeBean {

    private String uuid;

    private String name;

    private String classname;

    private boolean isPublished;

    public TreeNodeBean(String name) {
        this.name = name;
    }

    public TreeNodeBean(String name, String classname) {
        this.name = name;
        this.classname = classname;
    }

    public TreeNodeBean(String uuid, String name, String classname) {
        this.uuid = uuid;
        this.name = name;
        this.classname = classname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).append("published", this.isPublished()).append("classname", this.classname).append("uuid", this.uuid).toString();
    }
}
