package org.neurox.esearch;

import org.eclipse.swt.graphics.Image;

public interface IQueryHandlerCategory {

    /**
	 * The name of the category
	 * 
	 * @return
	 */
    public String getName();

    /**
	 * The ID of the category
	 * 
	 * @return
	 */
    public String getID();

    /**
	 * The image visually representing the category may be null
	 * 
	 * @return
	 */
    public Image getImage();

    /**
	 * The manager that manages this category
	 * 
	 * @return
	 */
    public IQueryHandlerManager getManager();
}
