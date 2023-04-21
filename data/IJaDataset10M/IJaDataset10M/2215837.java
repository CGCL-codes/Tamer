package org.vesuf.model.presentation;

import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.model.uml.foundation.vepl.*;
import java.util.*;

/**
 *  Represents an abstract part of a user interface.
 *  Component factories can be used to instantiate parts,
 *  creating ui elements like delegates.
 *  Parts have properties, that can contain render options,
 *  for the component factories (widgettype), 
 *  or properties to be used by the ui element instance.
 */
public interface IPart extends IModelElement {

    /** Part is not associated to object model. */
    public static final String TYPE_PART = "Part";

    /** Part is a delegate (atomar instance element). */
    public static final String TYPE_DELEGATE = "Delegate";

    /** Part is a label for a element. */
    public static final String TYPE_LABEL = "Label";

    /** Part is a Description. */
    public static final String TYPE_DESCRIPTION = "Description";

    /** Part is a container for other parts. */
    public static final String TYPE_CONTAINER = "Container";

    /** Part is a dialog (combines container and delegate). */
    public static final String TYPE_DIALOG = "Dialog";

    /**
	 *  Get the class of this part.
	 *  @return The name of the partclass.
	 */
    public String getPartclass();

    /**
	 *  Get the primary path of this part.
	 *  The primary path is always named 'path'.
	 *  @return The path.
	 */
    public IPathElement getPath();

    /**
	 *  Get an additional path of this part by name.
	 *  @param name	The name of the path.
	 *  @return The path.
	 */
    public IPathElement getPath(String name);

    /**
	 *  Get the fully qualified name of this part.
	 *  @return The full name.
	 */
    public String getPartname();

    /**
	 *  Get the parent of this part (if any).
	 */
    public IPart getParent();

    /**
	 *  Get the subparts of this part.
	 *  @return All subparts.
	 */
    public IPart[] getSubparts();

    /**
	 *  Get a subpart of this part.
	 *  @param name	The name of the subpart.
	 *  @return The named subpart.
	 */
    public IPart getSubpart(String name);

    /**
	 *  Get property value of this part, or of it's parent.
	 *  @return The property value.
	 */
    public String getDeepProperty(String name);
}
