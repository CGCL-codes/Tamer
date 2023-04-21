package lichen.internal.services;

import lichen.services.Menu;

/**
 * 默认的menu类.
 * @author jcai
 * @version $Revision: 195 $
 * @since 0.0.2
 */
public class MenuImpl implements Menu {

    private Object[] linkContext;

    private String pagePath;

    private String pageText;

    private boolean _disabled;

    /**
	 * @return Returns the linkContext.
	 */
    public Object[] getPageContext() {
        return linkContext;
    }

    /**
	 * @param linkContext The linkContext to set.
	 */
    public void setPageContext(Object... linkContext) {
        this.linkContext = linkContext;
    }

    /**
	 * @return Returns the pageName.
	 */
    public String getPagePath() {
        return pagePath;
    }

    /**
	 * @param pageName The pageName to set.
	 */
    public void setPagePath(String pageName) {
        this.pagePath = pageName;
    }

    /**
	 * @return Returns the pagePath.
	 */
    public String getPageText() {
        return pageText;
    }

    /**
	 * @param pageText The pagePath to set.
	 */
    public void setPageText(String pageText) {
        this.pageText = pageText;
    }

    public boolean isDisabled() {
        return _disabled;
    }

    public void setDisabled(boolean disabled) {
        this._disabled = disabled;
    }

    public static Menu newInstance(String string) {
        MenuImpl menu = new MenuImpl();
        menu.setDisabled(true);
        menu.setPageText(string);
        return menu;
    }
}
