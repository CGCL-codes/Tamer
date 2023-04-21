package component_interfaces.semanticmm4u.realization.media_elements_connector.provided;

import component_interfaces.semanticmm4u.realization.IEventListener;
import component_interfaces.semanticmm4u.realization.IQueryObject;
import component_interfaces.semanticmm4u.realization.compositor.provided.IMediaList;
import component_interfaces.semanticmm4u.realization.compositor.provided.IMedium;
import component_interfaces.semanticmm4u.realization.user_profile_connector.provided.IUserProfile;
import de.offis.semanticmm4u.failures.media_elements_connectors.MM4UCannotCloseMediaElementsConnectionException;
import de.offis.semanticmm4u.failures.media_elements_connectors.MM4UCannotOpenMediaElementsConnectionException;
import de.offis.semanticmm4u.failures.media_elements_connectors.MM4UMediumElementNotFoundException;

/**
 * Provides services such as <code>openConnection()</code> and
 * <code>closeConnection()</code> for opening and closing a connection to the
 * media storage. It supports active client-pull and passive server-push
 * retrieval of media elements.
 * 
 */
public interface IMediaElementsAccessor {

    /**
	 * Use this method to open a connection.
	 * 
	 * @throws MM4UCannotOpenMediaElementsConnectionException
	 */
    public void openConnection() throws MM4UCannotOpenMediaElementsConnectionException;

    /**
	 * Use this method to close a connection
	 * 
	 * @throws MM4UCannotCloseMediaElementsConnectionException
	 */
    public void closeConnection() throws MM4UCannotCloseMediaElementsConnectionException;

    /**
	 * Use this method to retrieve a single media element by a mediumID.
	 * 
	 * @param mediumID
	 * @return
	 * @throws MM4UMediumElementNotFoundException
	 */
    public abstract IMedium getMediumElement(String mediumID) throws MM4UMediumElementNotFoundException;

    /**
	 * Use this method to query all media element by a given user profile and
	 * application-specific query object.
	 * 
	 * @param queryObject
	 * @param userProfile
	 * @return IMediaList the media element list
	 * @throws MM4UMediumElementNotFoundException
	 */
    public IMediaList getMediaElements(IQueryObject queryObject, IUserProfile userProfile) throws MM4UMediumElementNotFoundException;

    /**
	 * Use this method to add additional observer. This is required only if the
	 * connector provides for push-services.
	 */
    public boolean addMediaElementsObserver(IEventListener observer);

    /**
	 * Use this method to remove observer. This is required only if the
	 * connector provides for push-services.
	 */
    public boolean removeMediaElementsObserver(IEventListener observer);

    /**
	 * Specific service provided for personalized multimedia applications to
	 * determine the media type and create a corresponding media element
	 * manually.
	 */
    public IMediumElementCreator determineMediumElementCreator(String mediumID);

    /**
	 * Gets the system dependent separator for hierarchically ordered media
	 * assets. This will be for a file system connector a slash ("/") or
	 * backslash ("\") depending on the operating system used.
	 * 
	 * @return the system dependent separator
	 */
    public String getMediaElementsIdentifierSeperator();
}
