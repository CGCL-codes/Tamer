package de.offis.semanticmm4u.media_elements_connector;

import java.io.Serializable;
import component_interfaces.semanticmm4u.realization.IEventListener;
import component_interfaces.semanticmm4u.realization.IEventNotifier;
import component_interfaces.semanticmm4u.realization.media_elements_connector.provided.IMediaElementsAccessor;
import component_interfaces.semanticmm4u.realization.media_elements_connector.provided.IMediumElementCreator;
import component_interfaces.semanticmm4u.realization.media_elements_connector.realization.IMediaElementsConnectorLocator;
import component_interfaces.semanticmm4u.realization.media_elements_connector.realization.IMediumCreatorManager;
import de.offis.semanticmm4u.failures.media_elements_connectors.MM4UCannotOpenMediaElementsConnectionException;
import de.offis.semanticmm4u.global.Debug;
import de.offis.semanticmm4u.media_elements_connector.media_elements_creators.MediumCreatorManager;
import de.offis.semanticmm4u.media_elements_connector.uri.URIMediaElementsConnectorFactory;
import de.offis.semanticmm4u.media_elements_connector.uri.URIMediaElementsConnectorLocator;
import de.offis.semanticmm4u.media_elements_connector.uri_and_mda.MDA_URIMediaElementsConnectorFactory;
import de.offis.semanticmm4u.media_elements_connector.uri_and_mda.MDA_URIMediaElementsConnectorLocator;
import de.offis.semanticmm4u.user_profiles_connector.UserProfileAccessorToolkit;

/**
 * Note: Diese Klasse kapselt auch den Zugriff auf die Medien weg, wenn mehr als ein
 * Server von der personalisierten Multimedia-Anwendung verwendet wird (-> Wegkapseln des Datenbank-
 * zugriffs; Stichwort: verteilte Datenbanken)
 **/
public abstract class MediaElementsAccessorToolkit implements IMediaElementsAccessor, Serializable, Cloneable {

    private IMediumCreatorManager mediumCreatorManager = new MediumCreatorManager();

    protected IEventNotifier eventNotifier = null;

    public static final IMediaElementsAccessor getFactory(IMediaElementsConnectorLocator mediaConnectorLocator) {
        Debug.println(mediaConnectorLocator.toString());
        MediaElementsAccessorToolkit tempMediaConnectorToolkit = null;
        UserProfileAccessorToolkit tempProfileConnectorToolkit = null;
        if (mediaConnectorLocator instanceof URIMediaElementsConnectorLocator) {
            tempMediaConnectorToolkit = new URIMediaElementsConnectorFactory((URIMediaElementsConnectorLocator) mediaConnectorLocator);
        } else if (mediaConnectorLocator instanceof MDA_URIMediaElementsConnectorLocator) {
            tempMediaConnectorToolkit = new MDA_URIMediaElementsConnectorFactory((MDA_URIMediaElementsConnectorLocator) mediaConnectorLocator);
        } else {
            throw new RuntimeException("Error in method getFactory() of class MediaElementsAccessorToolkit: " + " Could not find a connector factory for locator object = " + mediaConnectorLocator);
        }
        try {
            tempMediaConnectorToolkit.openConnection();
        } catch (MM4UCannotOpenMediaElementsConnectionException exception) {
            throw new RuntimeException("Error in method getFactory() of class MediaAccessorToolkit: " + "opening media connection failed; " + exception.getMessage());
        }
        return tempMediaConnectorToolkit;
    }

    /**
     * To provide other components to get a concrete media creator for instantiating 
     * MM4U media objects. 
     */
    public IMediumElementCreator determineMediumElementCreator(String mediumID) {
        return this.mediumCreatorManager.selectMediumCreator(mediumID);
    }

    protected void recursiveClone(MediaElementsAccessorToolkit object) {
        IMediumCreatorManager copiedMediumCreatorManager = this.mediumCreatorManager.recursiveClone();
        object.setMediumCreatorManager(copiedMediumCreatorManager);
    }

    /**
     * To provide access to the medium creator manager for the 
     * <code>recursiveClone( MediaAccessorToolkit object )</code> above.
     * @param tempMediumCreatorManager
     */
    private void setMediumCreatorManager(IMediumCreatorManager tempMediumCreatorManager) {
        this.mediumCreatorManager = tempMediumCreatorManager;
    }

    /**
     * To provide access to the medium creator manager for the subclasses.
     * 
     * @return the current medium creator manager
     */
    protected IMediumCreatorManager getMediumCreatorManager() {
        return this.mediumCreatorManager;
    }

    /**
     * Use this method to add additional observer.
     **/
    public boolean addMediaElementsObserver(IEventListener observer) {
        return this.eventNotifier.addObserver(observer);
    }

    /**
     * Use this method to remove observer.
     **/
    public boolean removeMediaElementsObserver(IEventListener observer) {
        return this.eventNotifier.removeObserver(observer);
    }
}
