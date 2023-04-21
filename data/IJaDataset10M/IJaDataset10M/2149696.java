package cn.ac.ntarl.umt.api.vo;

public class AppInfoExt {

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    private String name;

    private String description;

    private String url;

    private String type;

    private String serviceURL;

    private boolean showInMenu;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public boolean isShowInMenu() {
        return showInMenu;
    }

    public void setShowInMenu(boolean showInMenu) {
        this.showInMenu = showInMenu;
    }
}
