package dk.pervasive.jcaf;

/**
 * Implementations of this interface receives notification about changes to the
 * context of an Entity. This is the local version of a listener interface.
 * 
 * @see RemoteEntityListener
 * @see Entity
 * @see ContextService
 * 
 * @author <a href="mailto:bardram@daimi.au.dk">Jakob Bardram </a>
 *  
 */
public interface EntityListener {

    /**
     * Notification about changes in an entity's context information.
     * 
     * @param event
     */
    public void contextChanged(ContextEvent event);
}
