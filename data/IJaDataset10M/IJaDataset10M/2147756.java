package org.devtools.webtrans;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The AuthorManager abstracts the identity of the current page requestor so
 * that the author can be set on a page when it is edited.
 *
 * @author: Avery Regier
 */
public interface AuthorManager extends InitializableService {

    /**
	 * Apply the current author (from options) to the given page.
	 * 
	 * @param options
	 * @param page
	 * @throws PersistenceException
	 */
    public void applyAuthorTo(Properties options, WebContent page) throws PersistenceException;

    /**
	 * Associates a user id (ip address, or the results of 
	 * HttpServletRequest.getRemoteUser()) to a given wiki name.
	 * 
	 * @param userIdentifier
	 * @param wikiName
	 */
    public void changeAssociatedWikiName(String userIdentifier, String wikiName);

    /**
	 * Gets the wiki name associated with the given user id (ip address, or the results of 
	 * HttpServletRequest.getRemoteUser()).
	 * 
	 * @param userIdentifier
	 * @return String
	 */
    public String getAssociatedWikiName(String userIdentifier);

    /**
	 * Gets a user id from the given request.  Most often it will be from a
	 * cookie or from req.getRemoteUser().
	 * 
	 * @param req
	 * @return String
	 */
    public String getCurrentUserIdentifier(HttpServletRequest req);

    /**
	 * This is the wiki that the WikiName should be associated with by default.
	 * If there is more than one wiki mentioned in OnsiteLinkTypes, use the
	 * first one.  If there is not an OnsiteLinkTypes property, use the current
	 * wiki type.
	 * 
	 * @return String
	 */
    public String getDefaultAuthorType();

    /**
	 * Sets the author user identifier of the current user into the query.
	 * Optionallly it can also be used to set a new wiki name for the current
	 * user.
	 * 
	 * @param req
	 * @param res
	 * @param query
	 */
    public void setAuthor(HttpServletRequest req, HttpServletResponse res, Properties query);

    /**
	 * Transfer the author related properties from one properties object to
	 * another. Used when a page is saved and the author needs to be set on the
	 * properties for that page.
	 * 
	 * @param from
	 * @param to
	 */
    public void transferAuthor(Properties from, Properties to);
}
