package org.mcisb.ui.util.edit;

import java.awt.*;

/**
 *
 * @author Neil Swainston
 */
public interface Editable {

    /**
	 * 
	 * @return Component
	 */
    public Component getEditorComponent();

    /**
	 * 
	 *
	 */
    public void edit();

    /**
	 * 
	 * @return boolean
	 */
    public boolean terminateEditOnFocusLost();

    /**
	 * 
	 * @return Component
	 */
    public Component getRoot();

    /**
	 * 
	 * @return boolean
	 */
    public boolean stopEditing();

    /**
	 *
	 */
    public void cancelEditing();

    /**
	 * 
	 */
    public void removeEditor();
}
