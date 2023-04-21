package org.vesuf.runtime.presentation;

import org.vesuf.model.presentation.*;
import org.vesuf.runtime.uml.foundation.core.*;
import org.vesuf.runtime.uml.foundation.vepl.*;
import org.vesuf.util.*;

/**
 *  Represents an instantiated part of a user interface.
 */
public interface IPartInstance extends IInstanceElement {

    /**
	 *  Get the parent of this part instance (if any).
	 *  @return The parent.
	 */
    public IPartInstance getParent();

    /**
	 *  Get the path instance of this part instance.
	 *  @return The path instance.
	 */
    public IPathElementInstance getPathInstance();

    /**
	 *  Get the part, that this part instance is associated to.
	 *  @return The part.
	 */
    public IPart getPart();

    /**
	 *  Dispose this part. Remove all listeners.
	 */
    public void dispose();
}
