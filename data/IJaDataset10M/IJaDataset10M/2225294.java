package com.the_eventhorizon.actionQueue.testApp.mainView;

import javax.swing.Action;

/**
 * Action which is displayed in globaly available UI components like main menu, toolbar, etc.
 * 
 * @author pkrupets
 */
public interface IUIAction extends Action {

    /**
     * @return cannot be <code>null</code>.
     */
    public eActionType getType();

    /**
     * Invoked when some data which might be related to action's enabled state was changed.
     */
    public void updateEnabledState();
}
