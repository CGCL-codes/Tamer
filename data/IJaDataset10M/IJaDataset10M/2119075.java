package com.codename1.ui.events;

/**
 * Event callback interface invoked when a component action occurs
 * 
 * @author Chen Fishbein
 */
public interface ActionListener {

    /**
     * Invoked when an action occurred on a component
     * 
     * @param evt event object describing the source of the action as well as
     * its trigger
     */
    public void actionPerformed(ActionEvent evt);
}
