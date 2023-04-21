package org.eaasyst.eaa.syst.data.persistent;

import org.apache.commons.beanutils.BeanUtils;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.PersistentDataBean;
import org.eaasyst.eaa.syst.data.PersistentDataBeanBase;

/**
 * <p>An entity class containing all of the attributes of a single menu
 * item in an on-line system's navigation, including any submenu items
 * if this menu item destination is a lower level menu.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public class MenuItem extends PersistentDataBeanBase {

    private static final long serialVersionUID = 1;

    private int parentId = 0;

    private int sequence = 0;

    private String menuItemKey = null;

    private String displayTextKey = null;

    private String displayStyle = null;

    private String description = null;

    private String helpTextKey = null;

    private String destinationType = null;

    private String destinationString = null;

    private MenuItem[] options = null;

    /**
	 * Constructs a new MenuItem.
	 * 
	 * @since Eaasy Street 2.0
	 */
    public MenuItem() {
        this(null, null, null, null, null, null, null, null);
    }

    /**
	 * Constructs a new MenuItem based on the parameters provided.
	 * 
	 * @param menuItemKey the key for this event class
	 * @param displayTextKey the actual text that will appear on the menu
	 * @param displayStyle the css "class" attribute for this menu item
	 * @param description the description of this menu item
	 * @param helpTextKey the text that may appear at the bottom of the screen
	 * on certain formats
	 * @param destinationType the type of link related to this menu item
	 * @param destinationString the URL or application name of the destination
	 * @param options an array of <code>MenuItem</code> objects that represent
	 * the submenu options for this menu branch.
	 * @since Eaasy Street 2.0
	 */
    public MenuItem(String menuItemKey, String displayTextKey, String displayStyle, String description, String helpTextKey, String destinationType, String destinationString, MenuItem[] options) {
        this.menuItemKey = menuItemKey;
        this.displayTextKey = displayTextKey;
        this.displayStyle = displayStyle;
        this.description = description;
        this.helpTextKey = helpTextKey;
        this.destinationType = destinationType;
        this.destinationString = destinationString;
        this.options = options;
        if (this.menuItemKey == null) {
            this.menuItemKey = Constants.EMPTY_STRING;
        }
        if (this.displayTextKey == null) {
            this.displayTextKey = Constants.EMPTY_STRING;
        }
        if (this.displayStyle == null) {
            this.displayStyle = Constants.EMPTY_STRING;
        }
        if (this.description == null) {
            this.description = Constants.EMPTY_STRING;
        }
        if (this.helpTextKey == null) {
            this.helpTextKey = Constants.EMPTY_STRING;
        }
        if (this.destinationType == null) {
            this.destinationType = Constants.EMPTY_STRING;
        }
        if (this.destinationString == null) {
            this.destinationString = Constants.EMPTY_STRING;
        }
    }

    /**
	 * <p>Updates this bean with the data from an equivalent bean.</p>
	 * 
	 * @param a previously populated instance of this class of entity bean
	 * @since Eaasy Street 2.1.2
	 */
    protected void updateBeanFields(PersistentDataBean bean) {
        MenuItem item = (MenuItem) bean;
        parentId = item.parentId;
        sequence = item.sequence;
        menuItemKey = item.menuItemKey;
        displayTextKey = item.displayTextKey;
        displayStyle = item.displayStyle;
        description = item.description;
        helpTextKey = item.helpTextKey;
        destinationType = item.destinationType;
        destinationString = item.destinationString;
        if (item.getOptions() == null) {
            options = new MenuItem[0];
        } else if (options == null) {
            options = item.getOptions();
        } else {
            MenuItem[] oldOptions = options;
            options = item.getOptions();
            for (int i = 0; i < options.length; i++) {
                MenuItem thisItem = options[i];
                MenuItem matchingItem = null;
                for (int j = 0; j < oldOptions.length; j++) {
                    MenuItem existingItem = oldOptions[j];
                    if (existingItem.getId() == thisItem.getId()) {
                        matchingItem = existingItem;
                    }
                }
                if (matchingItem != null) {
                    matchingItem.update(thisItem);
                    options[i] = matchingItem;
                }
                options[i].setSequence(i);
            }
        }
    }

    /**
	 * <p>Returns a String containing the comma-separated values of
	 * the contents of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated values of
	 * this entity bean
	 * @since Eaasy Street 2.1.2
	 */
    protected String getDataAsCsv() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(menuItemKey);
        buffer.append("\",\"");
        buffer.append(displayTextKey);
        buffer.append("\",\"");
        buffer.append(displayStyle);
        buffer.append("\",\"");
        buffer.append(description);
        buffer.append("\",\"");
        buffer.append(helpTextKey);
        buffer.append("\",\"");
        buffer.append(destinationType);
        buffer.append("\",\"");
        buffer.append(destinationString);
        buffer.append("\"");
        return buffer.toString();
    }

    /**
	 * <p>Returns a String containing the comma-separated column 
	 * headings for the csv values of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated column 
	 * headings for the csv values of this entity bean
	 * @since Eaasy Street 2.1.2
	 */
    protected String getHeadingsAsCsv() {
        return "\"menuItemKey\",\"displayTextKey\",\"displayStyle\",\"description\",\"helpTextKey\",\"destinationType\",\"destinationString\",\"Description\",\"Application\",\"Users\"";
    }

    /**
	 * Returns a copy of this menu item.
	 * 
	 * @return MenuItem
	 * @since Eaasy Street 2.0.2
	 */
    public MenuItem getMenuItem() {
        MenuItem menuitem = new MenuItem();
        try {
            BeanUtils.copyProperties(menuitem, this);
        } catch (Exception ite) {
            EaasyStreet.logError("Error Copying Menu Item: " + ite.toString(), ite);
        }
        return menuitem;
    }

    /**
	 * Returns the option identified by the passed index.
	 * 
	 * @return MenuItem
	 * @since Eaasy Street 2.0
	 */
    public MenuItem getOption(int index) {
        MenuItem returnValue = null;
        if (options != null) {
            if (index > -1) {
                if (index < options.length) {
                    returnValue = options[index];
                }
            }
        }
        return returnValue;
    }

    /**
	 * <p>Adds the passed menu item to the list of options.</a>
	 * 
	 * @param item the MenuItem to add
	 * @return the MenuItem added
	 * @since Eaasy Street 2.1.2
	 */
    public MenuItem addOption(MenuItem item) {
        MenuItem[] oldOptions = options;
        if (oldOptions == null) {
            options = new MenuItem[1];
        } else {
            options = new MenuItem[oldOptions.length + 1];
            for (int i = 0; i < oldOptions.length; i++) {
                options[i] = oldOptions[i];
            }
        }
        item.setSequence(options.length - 1);
        options[options.length - 1] = item;
        return item;
    }

    /**
	 * <p>Removes the passed menu item from the list of options.</a>
	 * 
	 * @param item the MenuItem to add
	 * @return the MenuItem added
	 * @since Eaasy Street 2.1.2
	 */
    public MenuItem removeOption(MenuItem item) {
        MenuItem[] oldOptions = options;
        if (oldOptions != null && oldOptions.length > 0) {
            int newIndex = 0;
            options = new MenuItem[oldOptions.length - 1];
            for (int i = 0; i < oldOptions.length; i++) {
                if (oldOptions[i].getId() == item.getId()) {
                    item.setParentId(-1);
                    item.setSequence(0);
                } else {
                    if (newIndex < options.length) {
                        options[newIndex] = oldOptions[i];
                        options[newIndex].setSequence(newIndex);
                        newIndex++;
                    } else {
                        options = oldOptions;
                    }
                }
            }
        }
        return item;
    }

    /**
	 * Returns the description.
	 * @return String
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Returns the destinationString.
	 * @return String
	 */
    public String getDestinationString() {
        return destinationString;
    }

    /**
	 * Returns the destinationType.
	 * @return String
	 */
    public String getDestinationType() {
        return destinationType;
    }

    /**
	 * Returns the displayStyle.
	 * @return String
	 */
    public String getDisplayStyle() {
        return displayStyle;
    }

    /**
	 * Returns the displayTextKey.
	 * @return String
	 */
    public String getDisplayTextKey() {
        return displayTextKey;
    }

    /**
	 * Returns the helpTextKey.
	 * @return String
	 */
    public String getHelpTextKey() {
        return helpTextKey;
    }

    /**
	 * Returns the menuItemKey.
	 * @return String
	 */
    public String getMenuItemKey() {
        return menuItemKey;
    }

    /**
	 * Returns the options.
	 * @return MenuItem[]
	 */
    public MenuItem[] getOptions() {
        return options;
    }

    /**
	 * Sets the description.
	 * @param description The description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Sets the destinationString.
	 * @param destinationString The destinationString to set
	 */
    public void setDestinationString(String destinationString) {
        this.destinationString = destinationString;
    }

    /**
	 * Sets the destinationType.
	 * @param destinationType The destinationType to set
	 */
    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    /**
	 * Sets the displayStyle.
	 * @param displayStyle The displayStyle to set
	 */
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    /**
	 * Sets the displayTextKey.
	 * @param displayTextKey The displayTextKey to set
	 */
    public void setDisplayTextKey(String displayTextKey) {
        this.displayTextKey = displayTextKey;
    }

    /**
	 * Sets the helpTextKey.
	 * @param helpTextKey The helpTextKey to set
	 */
    public void setHelpTextKey(String helpTextKey) {
        this.helpTextKey = helpTextKey;
    }

    /**
	 * Sets the menuItemKey.
	 * @param menuItemKey The menuItemKey to set
	 */
    public void setMenuItemKey(String menuItemKey) {
        this.menuItemKey = menuItemKey;
    }

    /**
	 * Sets the options.
	 * @param options The options to set
	 */
    public void setOptions(MenuItem[] options) {
        this.options = options;
    }

    /**
	 * @return
	 */
    public int getParentId() {
        return parentId;
    }

    /**
	 * @return
	 */
    public int getSequence() {
        return sequence;
    }

    /**
	 * @param i
	 */
    public void setParentId(int i) {
        parentId = i;
    }

    /**
	 * @param i
	 */
    public void setSequence(int i) {
        sequence = i;
    }
}
