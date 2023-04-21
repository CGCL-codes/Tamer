package org.sgodden.echo.ext20;

import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.Component;
import nextapp.echo.app.ImageReference;

/**
 * An ext button.
 * 
 * @author goddens
 * 
 */
public class Button extends AbstractButton {

    private static final long serialVersionUID = 20080102L;

    /**
     * When set to true, tells the client side rendering engine that this button
     * should go into the container's button bar, rather than being added
     * directly to its layout.
     */
    public static final String PROPERTY_ADD_TO_BUTTON_BAR = "addToButtonBar";

    /**
     * The group that this toggle button is a member of
     */
    public static final String PROPERTY_TOGGLE_GROUP = "toggleGroup";

    /**
     * When set to true, tells the client to show the menu assoicated with the
     * button.
     */
    public static final String PROPERTY_HOVER_MENU = "hoverMenu";

    /**
     * set the button's template
     */
    public static final String PROPERTY_TEMPLATE = "template";

    public static final String PROPERTY_FOCUSABLE = "focusable";

    public static final String PROPERTY_FIELDS_TO_LISTEN_TO = "fieldsToListenTo";

    public static final String PROPERTY_FIELD_CHANGED_CSS = "fieldChangedCSSClass";

    private ArrayList<Component> fieldsToListenToList = new ArrayList<Component>();

    private Menu menu;

    {
        setAddToButtonBar(false);
        setFocusable(true);
    }

    public Button() {
        super();
    }

    public Button(String text, ImageReference icon) {
        super(text, icon);
    }

    public Button(String text) {
        super(text);
    }

    /**
     * See {@link #PROPERTY_ADD_TO_BUTTON_BAR}.
     * 
     * @param addToButtonBar
     *            whether to add this button to the button bar rather than the
     *            main panel body.
     */
    void setAddToButtonBar(boolean addToButtonBar) {
        set(PROPERTY_ADD_TO_BUTTON_BAR, addToButtonBar);
    }

    /**
     * See {@link #PROPERTY_ADD_TO_BUTTON_BAR}.
     * 
     * @return whether this button is added to the button bar rather than the
     *         main panel body.
     */
    boolean isAddToButtonBar() {
        return (Boolean) get(PROPERTY_ADD_TO_BUTTON_BAR);
    }

    /**
     * Sets a menu to be opened when this button is clicked.
     * 
     * @param menu
     *            the menu to open.
     */
    public void setMenu(Menu menu) {
        if (this.menu != null) {
            remove(this.menu);
        }
        add(menu);
        this.menu = menu;
    }

    /**
     * Set the toggle group which this button belongs to. This also sets the
     * extjs property enableToggle to true for this button.
     */
    public void setToggleGroup(String toggleGroup) {
        set(PROPERTY_TOGGLE_GROUP, toggleGroup);
    }

    public void setTemplate(String template) {
        set(PROPERTY_TEMPLATE, template);
    }

    public static String getToggleGroup() {
        return (String) PROPERTY_TOGGLE_GROUP;
    }

    /**
     * Set this button to display the menu.
     * 
     * @param true to show the menu on mouse over.
     */
    public void isHoverMenu(boolean hoverMenu) {
        set(PROPERTY_HOVER_MENU, hoverMenu);
    }

    /**
     * Sets whether this button may receive the focus
     * (defaults to <code>true</code>).
     * <p>
     * This may be useful in situations where you
     * are dynamically adding rows to a table.  You will
     * want to know which component was focused when
     * the 'insert a row' button was pressed.  To achieve
     * that, make that button non-focusable.  The price
     * you pay is that the user cannot tab to that
     * button using the keyboard.
     * </p>
     */
    public void setFocusable(boolean focusable) {
        set(PROPERTY_FOCUSABLE, focusable);
    }

    /**
     * Gets list of fields that require action listeners adding to them 
     * @return
     */
    public List<Component> getFieldsToListenToList() {
        return fieldsToListenToList;
    }

    /**
	 * Sets list of fields that should be added to the button's property change listener
	 * @param fieldsToListenToList
	 */
    public void setFieldsToListenToList(ArrayList<Component> fieldsToListenToList) {
        this.fieldsToListenToList = fieldsToListenToList;
    }

    /**
	 * @param fieldChangedCSSClass the fieldChangedCSS to set
	 */
    public void setFieldChangedCSSClass(String fieldChangedCSSClass) {
        set(PROPERTY_FIELD_CHANGED_CSS, fieldChangedCSSClass);
    }

    /**
	 * @return the highlightCSSClass
	 */
    public String getFieldChangedCSSClass() {
        return (String) get(PROPERTY_FIELD_CHANGED_CSS);
    }
}
