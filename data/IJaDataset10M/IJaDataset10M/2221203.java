package com.prolix.editor.listener;

/**
 * Listener for the Changes in the Model
 * @author prenner
 *
 */
public interface MenueModelLinkListener {

    /**
	 * new color for the element
	 */
    public void fireColorChange();

    /**
	 * new name for the element
	 */
    public void fireNameChange();

    /**
	 * element is deleted
	 */
    public void fireDeleteChange();
}
