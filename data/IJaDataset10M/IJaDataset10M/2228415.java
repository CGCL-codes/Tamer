package com.gwtext.client.widgets.menu;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Ext;
import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtext.client.widgets.menu.event.MenuListener;

/**
 * A menu object. This is the container to which you add all other menu items.
 *
 * @author Sanjiv Jivan
 */
public class Menu extends Widget {

    protected String id;

    protected JavaScriptObject config;

    protected JavaScriptObject jsObj;

    private boolean isElementSet = false;

    public Menu() {
        id = Ext.generateId();
        config = JavaScriptObjectHelper.createObject();
        JavaScriptObjectHelper.setAttribute(config, "id", id);
    }

    public void setId(String id) {
        JavaScriptObjectHelper.setAttribute(config, "id", id);
        this.id = id;
    }

    public Menu(JavaScriptObject jsObj) {
        id = JavaScriptObjectHelper.getAttribute(jsObj, "id");
        setElement(getElement(jsObj));
    }

    protected void setElement(Element elem) {
        super.setElement(elem);
        isElementSet = true;
    }

    protected native Element getElement(JavaScriptObject jsObj);

    public Element getElement() {
        if (!isElementSet) {
            if (jsObj == null) {
                jsObj = create(config);
            }
            setElement(getElement(jsObj));
        }
        return super.getElement();
    }

    public JavaScriptObject getOrCreateJsObj() {
        if (jsObj != null) {
            return jsObj;
        } else {
            jsObj = create(config);
            return jsObj;
        }
    }

    protected static native JavaScriptObject getComponent(String id);

    private static Menu menuInstance(JavaScriptObject jsObj) {
        return new Menu(jsObj);
    }

    protected JavaScriptObject create(String id, JavaScriptObject config) {
        JavaScriptObjectHelper.setAttribute(config, "id", id);
        return create(config);
    }

    protected native JavaScriptObject create(JavaScriptObject menuConfig);

    /**
	 * Adds an Element object to the menu.
	 *
	 * @param element the element to add
	 */
    public native void addElement(Element element);

    /**
	 * Adds an Ext.Element object to the menu.
	 *
	 * @param elemID the element ID
	 */
    public native void addElement(String elemID);

    /**
	 * Adds an {@link Item} to the menu.
	 *
	 * @param item the item to add
	 */
    public native void addItem(BaseItem item);

    /**
	 * Adds a separator bar to the menu.
	 */
    public native void addSeparator();

    /**
	 * Creates a new {@link TextItem} with the supplied text and adds it to the menu.
	 *
	 * @param text the text to add
	 */
    public native void addText(String text);

    /**
	 * Gets an Item.
	 *
	 * @param itemId the item Id
	 * @return the item
	 */
    public native BaseItem getItem(String itemId);

    /**
     * Gets all of this menu's items
     * 
     * @return the items
     */
    public BaseItem[] getItems() {
        JavaScriptObject nativeArray = getItems(getOrCreateJsObj());
        return convertFromNativeBaseItemsArray(nativeArray);
    }

    ;

    private static BaseItem[] convertFromNativeBaseItemsArray(JavaScriptObject nativeArray) {
        JavaScriptObject[] itemsj = JavaScriptObjectHelper.toArray(nativeArray);
        BaseItem[] items = new BaseItem[itemsj.length];
        for (int i = 0; i < itemsj.length; i++) {
            JavaScriptObject item = itemsj[i];
            items[i] = new BaseItem(item);
        }
        return items;
    }

    private native JavaScriptObject getItems(JavaScriptObject menu);

    /**
	 * Hides this menu.
	 */
    public native void hide();

    /**
	 * Hides this menu and optionally all parent menus.
	 *
	 * @param deep true to hide all parent menus recursively, if any (defaults to false)
	 */
    public native void hide(boolean deep);

    /**
	 * Inserts an e{@link Item} to the menu at a specified index.
	 *
	 * @param index the index to insert
	 * @param item  the item to insert
	 */
    public native void insert(int index, BaseItem item);

    /**
	 * Returns true if the menu is currently displayed, else false.
	 *
	 * @return true if visible
	 */
    public native boolean isVisible();

    /**
	 * Removes an {@link Item} from the menu and destroys the object.
	 *
	 * @param item the item to remove
	 */
    public native void remove(BaseItem item);

    /**
	 * Removes and destroys all items in the menu
	 */
    public native void removeAll();

    /**
	 * Displays this menu relative to another element.
	 *
	 * @param id the element ID to align to
	 */
    public native void show(String id);

    /**
	 * Displays this menu at a specific xy position.
	 *
	 * @param x the X position
	 * @param y the Y position
	 */
    public void showAt(int x, int y) {
        int[] xyPosition = new int[] { x, y };
        showAt(xyPosition);
    }

    /**
	 * Displays this menu at a specific xy position.
	 *
	 * @param xy the xy position
	 */
    public void showAt(int[] xy) {
        showAt(getOrCreateJsObj(), JavaScriptObjectHelper.convertToJavaScriptArray(xy), null);
    }

    /**
	 * Displays this menu at a specific xy position.
	 *
	 * @param x		  the X position
	 * @param y		  the Y position
	 * @param parentMenu the menu's parent menu, if applicable
	 */
    public void showAt(int x, int y, Menu parentMenu) {
        int[] xyPosition = new int[] { x, y };
        showAt(getOrCreateJsObj(), JavaScriptObjectHelper.convertToJavaScriptArray(xyPosition), parentMenu.getOrCreateJsObj());
    }

    private static native void showAt(JavaScriptObject menu, JavaScriptObject xyPosition, JavaScriptObject parentMenu);

    /**
	 * Add a menu listener.
	 *
	 * @param listener the listener
	 */
    public native void addListener(MenuListener listener);

    /**
	 * True to allow multiple menus to be displayed at the same time (defaults to false).
	 *
	 * @param allowOtherMenus true to allow multiple menus
	 */
    public void setAllowOtherMenus(boolean allowOtherMenus) {
        JavaScriptObjectHelper.setAttribute(config, "allowOtherMenus", allowOtherMenus);
    }

    /**
	 * The default {@link com.gwtext.client.core.ExtElement#alignTo} anchor position value for this menu relative to its
     * element of origin (defaults to "tl-bl").
	 *
	 * @param anchorPosition defaults to t-b?
	 */
    public void setDefaultAlign(String anchorPosition) {
        JavaScriptObjectHelper.setAttribute(config, "defaultAlign", anchorPosition);
    }

    /**
	 * The minimum width of the menu in pixels (defaults to 120).
	 *
	 * @param minWidth the min width
	 */
    public void setMinWidth(int minWidth) {
        JavaScriptObjectHelper.setAttribute(config, "minWidth", minWidth);
    }

    /**
	 * True for the default effect.
	 *
	 * @param shadow true for shadow
	 */
    public void setShadow(boolean shadow) {
        JavaScriptObjectHelper.setAttribute(config, "shadow", shadow);
    }

    /**
	 * "sides" for the default effect, "frame" for 4-way shadow, and "drop" for bottom-right shadow (defaults to "sides")
	 *
	 * @param shadow the shadow settings
	 */
    public void setShadow(String shadow) {
        JavaScriptObjectHelper.setAttribute(config, "shadow", shadow);
    }

    /**
	 * The Ext.Element.alignTo anchor position value to use for submenus of this menu (defaults to "tl-tr?").
	 *
	 * @param anchorPosition the anchor position
	 */
    public void setSubMenuAlign(String anchorPosition) {
        JavaScriptObjectHelper.setAttribute(config, "subMenuAlign", anchorPosition);
    }
}
