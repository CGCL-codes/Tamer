package com.bitmovers.maui.components.foundation;

import java.awt.*;
import java.util.*;
import com.bitmovers.utilities.*;
import com.bitmovers.maui.*;
import com.bitmovers.maui.engine.*;
import com.bitmovers.maui.engine.logmanager.*;
import com.bitmovers.maui.engine.render.*;
import com.bitmovers.maui.engine.resourcemanager.*;
import com.bitmovers.maui.events.*;
import com.bitmovers.maui.components.*;
import com.bitmovers.maui.components.foundation.*;
import com.bitmovers.maui.layouts.*;

/** This class is represents a menu item, which is the basic clickable element 
  * in the menu system. To handle menu item clicks, you should catch events 
  * published by <code>MMenuItem</code>s.
  * 
  */
public class MMenuItem extends MContainer implements MActionListener, HasLabel {

    protected MMenuButton button;

    protected MMenuBar menubar;

    private static final String base = "MMenuItem";

    private static int nameCounter = 0;

    private final int myCounterValue;

    /** Constructs a new menu item with the given label.
	  * 
	  * @param label A string text for the menu item label.
	  */
    public MMenuItem(String label) {
        this.myCounterValue = nameCounter++;
        this.name = base + myCounterValue;
        this.button = new MMenuButton(this, label, MMenuButton.LEVEL2);
        this.button.addActionListener(this);
    }

    /** Collapses menu system and republishes <code>MMenuButton</code> click event to all 
	  * <code>MMenuItem</code> listeners.
	  * 
	  * @param event  
	  * @invisible
	  * 
	  */
    public void actionPerformed(MActionEvent event) {
        if (this.menubar != null) {
            this.menubar.closeChildren();
        }
        super.dispatchActionEvent(new MActionEvent(this, event.getActionCommand()));
    }

    /** Returns the label of this menu item.
		*
	  * @return The string text part of the menu item. 
	  */
    public String getLabel() {
        return this.button.getLabel();
    }

    /** Sets the label of this menu item.
	  * 
	  * @param label The string text representing the label for the menu item.
	  */
    public void setLabel(String label) {
        this.button.setLabel(label);
        this.invalidate();
    }

    /** Returns the color of this menu item as a <code>java.awt.Color</code> 
	  * object.
	  * 
	  * @return <code>Color</code> object representing the color of the menu item.
	  */
    public Color getColor() {
        return this.button.getColor();
    }

    /** Sets the color of this menu item using the given 
	  * <code>java.awt.Color</code> object.
	  * 
	  * @param color The given <code>Color</code> object.
	  */
    public void setColor(Color color) {
        this.button.setColor(color);
        this.invalidate();
    }

    /** Checks if the menu item is enabled or not. A disabled MMenuItem will be 
	  * visible, but will not repond to user action.
		* 
		* @return  <code>true</code> if this menu item is enabled, or 
	  * <code>false</code> if disabled. 
	  *
	  */
    public boolean isEnabled() {
        return this.button.isEnabled();
    }

    /** Sets menu item to enabled or disabled by passing <code>true</code>
	  * or <code>false</code>, respectively. A disabled <code>MMenuItem</code> will be 
	  * visible, but will not repond to user action.
	  *
	  * @param isEnabled <code>true</code> to enable the menu item, <code>false</code> to disable
	  * 									the menu item.
	  */
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.button.setEnabled(isEnabled);
    }

    /** Returns the <code>MMenuButton</code> used by this menu item.
	  *
	  * @invisible
	  *
	  */
    public MMenuButton getButton() {
        return this.button;
    }

    /** Sets the menu bar to which this menu item belongs.
	  *
	  * @param menubar
	  */
    protected void setMenuBar(MMenuBar menubar) {
        this.menubar = menubar;
    }
}
