package com.foursoft.foureveredit.controller.command;

import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.mvc.controller.Command;

/**
 * @author kivlehan_adm Removes an instance from the ObjectModel
 */
public interface DeleteInstanceCommand extends Command {

    /**
	 * Sets the instance that will be removed when this command is executed.
	 * 
	 * @param i
	 *            the instance to be deleted upon execution.
	 */
    public void setInstanceToDelete(Instance i);
}
