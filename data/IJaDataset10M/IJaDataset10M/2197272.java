package ca.ubc.jquery.api;

import java.util.Collection;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;

public interface JQueryResourceStrategy {

    /**
	 * Returns true if the adaptable passed in is of a type applicable to this strategy.
	 * This method is called by JQuery when it needs to know which strategy to use for a
	 * given file.
	 */
    public abstract boolean rightType(IAdaptable adaptable);

    /**
	 * Checks the IAdaptable, and adds any applicable resources to the collection.
	 * This method is called by JQuery when it is looking through a working set and
	 * deciding which elements it has to create buckets for.
	 */
    public abstract void addApplicableElementToCollection(IAdaptable element, Collection c);

    /**
	 * Returns the IAdaptable changed by the resource delta.
	 * This method is called by JQuery when it determines that a change has occured to 
	 * a resource.  Return an IAdaptable if the resource delta is applicable
	 * to the file type that this strategy is for.
	 */
    public abstract IAdaptable resourceDelta(IResourceDelta delta);

    /**
	 * Gets the image descriptor for the given image string.  This can be either a file
	 * path or a reference to some specific image defined by the strategy.
	 * 
	 * @param image Name of the image
	 * @return the appropriate image descriptor or null if none can be found.
	 */
    public abstract ImageDescriptor getImageDescriptor(String image);

    /**
	 * Creates a parser for the given resource.  The parser can then generate facts by
	 * calling parse.
	 * @param adaptable This is the object we wish to parse
	 * @param manager The manager for the whole database which allows us to check and analyse
	 * dependencies
	 * @return A parser
	 */
    public abstract JQueryResourceParser makeParser(IAdaptable adaptable, JQueryResourceManager manager);
}
