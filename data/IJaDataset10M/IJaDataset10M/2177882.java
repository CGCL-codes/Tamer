package com.tensegrity.palowebviewer.modules.widgets.client.actions;

/**
 * Listener for action state.
 *
 */
public interface IPropertyListener {

    /**
	 * Invoked when an action becames enabled.
	 */
    public void onEnabled();

    /**
	 * Invoked when an action becames disabled.
	 */
    public void onDisabled();
}
