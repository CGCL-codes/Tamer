package vh.web.utils.menu;

/**
 * Represents a menu item.
 * 
 * @version $Id: VHMenuItem.java 42 2006-10-22 16:52:52Z janjanke $
 * @author jjanke
 */
public class VHMenuItem {

    private String d_strLabel;

    private String d_strLink;

    /**
   * Returns the resource key that points to the menu item's label.
   */
    public String getLabel() {
        return d_strLabel;
    }

    /**
   * Sets the resource key that points to a localized version of this menu item's label.
   * 
   * @param label the label resource key to be set
   */
    public void setLabel(String label) {
        d_strLabel = label;
    }

    /**
   * Returns the link this menu item points to. The link is always relative to the context
   * root.
   */
    public String getLink() {
        return d_strLink;
    }

    /**
   * Sets the context relative link of the page this menu item points to.
   * 
   * @param link the link to be set
   */
    public void setLink(String link) {
        d_strLink = link;
    }
}
