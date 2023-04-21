package org.xith3d.ui.hud.listeners;

import org.xith3d.ui.hud.widgets.TextField;

/**
 * This Listener listenes for specific TextField events.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface TextFieldListener {

    /**
     * This event is fired when the user typed a char in a TextField.
     * 
     * @param textField
     * @param ch
     */
    public void onCharTyped(TextField textField, char ch);

    /**
     * This event is fired when a character is deleted in a TextField.
     * 
     * @param textField
     */
    public void onCharDeleted(TextField textField);

    /**
     * This event is fired, if the ESCAPE key is hit on a TextField.
     * 
     * @param textField
     */
    public void onEscapeHit(TextField textField);

    /**
     * This event is fired, if the TAB key is hit on a TextField.
     * 
     * @param textField
     */
    public void onTabHit(TextField textField);

    /**
     * This event is fired, if the ENTER key is hit on a TextField.
     * 
     * @param textField
     */
    public void onEnterHit(TextField textField);
}
