package com.foursoft.foureveredit.controller.command;

import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.mvc.controller.Command;

/**
 * Command for setting the selected instance in the controller.
 * 
 * @author bergner
 */
public interface SetFocusedInstanceCommand extends Command {

    /**
	 * Get the value of the <em>instance</em> command property.
	 * 
	 * @return the value of the <em>instance</em> command property
	 */
    public Instance getInstance();

    /**
	 * Set the value of the <em>instance</em> command property.
	 * 
	 * @param instance
	 *            the 4Ever instance to be set
	 */
    public void setInstance(Instance instance);
}
