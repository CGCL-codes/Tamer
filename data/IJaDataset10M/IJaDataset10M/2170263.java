package com.pps.webos.model;

/**
 * Class represents the attributes listed in the appinfo.json
 * 
 * @author justinm
 *
 */
public class AppInfo {

    private String title = null;

    private String type = null;

    private String main = null;

    private String id = null;

    private String version = null;

    private String noWindow = null;

    private String icon = null;

    private String minicon = null;

    private String category = null;

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the main
	 */
    public String getMain() {
        return main;
    }

    /**
	 * @param main the main to set
	 */
    public void setMain(String main) {
        this.main = main;
    }

    /**
	 * @return the icon
	 */
    public String getIcon() {
        return icon;
    }

    /**
	 * @param icon the icon to set
	 */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the version
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * @param version the version to set
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
	 * @return the noWindow
	 */
    public String getNoWindow() {
        return noWindow;
    }

    /**
	 * @param noWindow the noWindow to set
	 */
    public void setNoWindow(String noWindow) {
        this.noWindow = noWindow;
    }

    /**
	 * @return the minicon
	 */
    public String getMinicon() {
        return minicon;
    }

    /**
	 * @param minicon the minicon to set
	 */
    public void setMinicon(String minicon) {
        this.minicon = minicon;
    }

    /**
	 * @return the category
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * @param category the category to set
	 */
    public void setCategory(String category) {
        this.category = category;
    }
}
