package ispyb.client.menu;

import java.util.ArrayList;

/**
 * Data Object for a item in the menu
 * 
 * @author Ricardo Leal
 */
public class MenuItem {

    int id;

    int parentId;

    String name;

    String url;

    ArrayList subMenus;

    /**
     * 
     */
    public MenuItem() {
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the parentId.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            The parentId to set.
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the subMenus.
     */
    public ArrayList getSubMenus() {
        return subMenus;
    }

    /**
     * @param subMenus
     *            The subMenus to set.
     */
    public void setSubMenus(ArrayList subMenus) {
        this.subMenus = subMenus;
    }

    public String toString() {
        return "Menu Item: " + id + "; " + parentId + "; " + name + "; " + url;
    }
}
