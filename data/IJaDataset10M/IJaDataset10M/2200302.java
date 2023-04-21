package org.eclipse.update.core;

import org.eclipse.update.core.model.CategoryModel;

/**
 * Convenience implementation of feature category definition.
 * <p>
 * This class may be instantiated or subclassed by clients.
 * </p> 
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @see org.eclipse.update.core.ICategory
 * @see org.eclipse.update.core.model.CategoryModel
 * @since 2.0
 */
public class Category extends CategoryModel implements ICategory {

    /**
	 * Default Constructor
	 */
    public Category() {
    }

    /**
	 * Constructor
	 */
    public Category(String name, String label) {
        setName(name);
        setLabel(label);
    }

    /**
	 * Retrieve the detailed category description
	 * @see ICategory#getDescription()
	 */
    public IURLEntry getDescription() {
        return (IURLEntry) getDescriptionModel();
    }
}
