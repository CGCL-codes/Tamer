package it.battlehorse.rcp.tools.actors;

import it.battlehorse.rcp.tools.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import static it.battlehorse.rcp.tools.actors.IActorConstants.*;

/**
 * Provides a lightweight descriptor for actors definitions.
 * 
 * @author battlehorse
 * @since May 13, 2006
 */
public class ActorDescriptor {

    /**
	 * The configuration element which describes the actor
	 */
    private IConfigurationElement element;

    /**
	 * The image descriptor which represents the actor icon
	 */
    private ImageDescriptor icon;

    /**
	 * Creates a new instance of the class
	 * 
	 * @param element the configuration element which defines the actor
	 * @param namespace The namespace of the extension point which defines the actor
	 */
    public ActorDescriptor(IConfigurationElement element, String namespace) {
        this.element = element;
        if (element.getAttribute(ID_ACTOR_ATTR_ICON) != null) {
            Bundle b = Platform.getBundle(namespace);
            if (b != null && b.getResource(element.getAttribute(ID_ACTOR_ATTR_ICON)) != null) icon = ImageDescriptor.createFromURL(b.getResource(element.getAttribute(ID_ACTOR_ATTR_ICON)));
            if (icon == null) {
                icon = Activator.getImageDescriptor("resources/actor/default_actor.png");
            }
        } else {
            icon = Activator.getImageDescriptor("resources/actor/default_actor.png");
        }
    }

    /**
	 * Returns the actor id
	 * 
	 * @return the actor id
	 */
    public String getId() {
        return element.getAttribute(ID_ACTOR_ATTR_ID);
    }

    /**
	 * Returns the actor name
	 * 
	 * @return the actor name
	 */
    public String getName() {
        return element.getAttribute(ID_ACTOR_ATTR_NAME);
    }

    /**
	 * Returns an instance of the actor class
	 * 
	 * @return an instance of the actor class
	 * @throws CoreException if object instantiation fails
	 */
    public IActor getActorInstance() throws CoreException {
        return (IActor) element.createExecutableExtension(ID_ACTOR_ATTR_CLASS);
    }

    /**
	 * Returns the id of the view which is linked to this actor. The linked view is the one
	 * which provides the input parameters for the actor.
	 *  
	 * @return the id of the linked view.
	 */
    public String getViewId() {
        return element.getAttribute(ID_ACTOR_ATTR_VIEWID);
    }

    /**
	 * Defines if the actor will be visible in the actor list or not
	 * 
	 * @return whether the actor will be visible in the actor list or not
	 */
    public boolean isVisible() {
        return "true".equals(element.getAttribute(ID_ACTOR_ATTR_VISIBLE)) || element.getAttribute(ID_ACTOR_ATTR_VISIBLE) == null;
    }

    /**
	 * Returns the descriptor which defines the actor icon. This icon may have been specified
	 * directly in the actor extension point, or it may be a default one if the extension point does
	 * not specify it.
	 *   
	 * @return the descriptor of the actor icon
	 */
    public ImageDescriptor getIcon() {
        return icon;
    }

    /**
	 * Returns the id of the category this actor belongs to, or {@code null} if this actor does not
	 * belong to any category
	 * 
	 * @return the id of the category this actor belongs to
	 */
    public String getCategoryId() {
        return element.getAttribute(ID_ACTOR_ATTR_CATEGORYID);
    }
}
