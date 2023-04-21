package net.sourceforge.gedapi.view;

public class BaseView {

    public transient long lastModified;

    public String tabIndex;

    public String gedcomurl;

    public BaseView(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getTabIndex() {
        return tabIndex;
    }

    public String getGedcomurl() {
        return gedcomurl;
    }

    public void setGedcomurl(String gedcomurl) {
        this.gedcomurl = gedcomurl;
    }
}
