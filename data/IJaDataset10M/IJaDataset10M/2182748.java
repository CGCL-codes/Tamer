package com.patientis.framework.controls;

/**
 * IControlButton is the interface for the reference item to create a menu or menu item
 *
 * Design Patterns: <a href="/functionality/rm/1000072.html">Control Panel</a>
 * <br/>
 */
public interface IControlButton extends Comparable {

    /**
	 * Unique identifier for button 
	 * 
	 * @return
	 */
    public Long getId();

    /**
	 * Get the button display
	 * 
	 * @return
	 */
    public String getLabel();

    /**
	 * Get the action executed
	 * 
	 * @return
	 */
    public int getDefaultAction();

    /**
	 * Get the keystroke to activate the button
	 * 
	 * @return
	 */
    public String getKeyStroke();

    /**
	 * Get the image path for this button
	 * 
	 * @return
	 */
    public String getImagePath();

    /**
	 * Get the description for the button
	 * 
	 * @return
	 */
    public String getControlDescription();

    /**
	 * Determine if has a button menu
	 * 
	 * @return
	 */
    public boolean isMenu();
}
