package org.formaria.swt;

import org.formaria.aria.Appender;
import org.formaria.aria.TextHolder;
import org.formaria.aria.events.Actionable;
import org.eclipse.swt.SWT;

/**
 * A wrapper for menus
 * <p>
 * Copyright (c) Formaria Ltd., <br>
 * 
 * @version 1.0
 */
public class Menu extends org.eclipse.swt.widgets.Menu implements TextHolder, Appender, Actionable {

    protected org.eclipse.swt.widgets.MenuItem cascadeMenu;

    /**
   * Create a new menu
   * 
   * @param parent
   *          parent MenuBar
   */
    public Menu(MenuBar parent) {
        super(parent.getNewCascadeMenu());
        cascadeMenu = parent.getLastCascademenu();
        cascadeMenu.setMenu(this);
    }

    /**
   * Suppress the subclassing exception
   */
    protected void checkSubclass() {
    }

    /**
   * Get the cascade menu
   * 
   * @return the cascade menu
   */
    public org.eclipse.swt.widgets.MenuItem getCascadeMenu() {
        return cascadeMenu;
    }

    /**
   * Set an action object
   * 
   * @param instance
   *          the action object
   */
    public void setAction(Object instance) {
    }

    /**
   * Do any final setup needed
   */
    public void setup() {
    }

    /**
   * Appends the object o to this item
   * 
   * @param o
   *          the appended item
   * @param name
   *          the name of the menu
   */
    public void append(Object o, String name) {
    }

    /**
   * Adds a separator to this item.
   */
    public void addSeparator() {
        new org.eclipse.swt.widgets.MenuItem(this, SWT.SEPARATOR);
    }

    /**
   * Get a child object by name
   * 
   * @param name
   *          the item name
   * @return the child item
   */
    public Object getObject(String name) {
        return null;
    }

    /**
   * Set the text/label of a component
   * 
   * @param text
   *          the new text
   */
    public void setText(String text) {
        cascadeMenu.setText(text);
    }

    /**
   * Get the text/label of a component
   * 
   * @return the component's text
   */
    public String getText() {
        return cascadeMenu.getText();
    }

    /**
   * Set the name of this object
   * 
   * @param name
   *          the item name
   */
    public void setName(String name) {
    }
}
