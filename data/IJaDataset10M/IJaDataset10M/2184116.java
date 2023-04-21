package org.hackystat.socnet.server.resource;

import org.hackystat.socnet.server.Server;
import org.hackystat.socnet.server.resource.users.UserManager;
import org.hackystat.socnet.server.resource.users.jaxb.XMLUser;
import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * An abstract superclass for all SensorBase resources that supplies common initialization and
 * validation processing.
 * <p>
 * Initialization processing includes:
 * <ul>
 * <li>Extracting the authenticated user identifier (when authentication available)
 * <li>Extracting the user email from the URI (when available)
 * <li>Declares that the TEXT/XML representational variant is supported.
 * <li>Providing instance variables bound to the ProjectManager, SdtManager, UserManager, and
 * SensorDataManager.
 * </ul>
 * <p>
 * Validation processing involves a set of "validated" methods. These check the values of various
 * parameters in the request, potentially initializing instance variables as a result. If the
 * validation process fails, these methods set the Restlet Status value appropriately and return
 * false.
 * 
 * @author Philip Johnson
 * 
 */
public abstract class SocNetResource extends Resource {

    /** The server. */
    protected Server server;

    /** Everyone generally wants to create one of these, so declare it here. */
    protected String responseMsg;

    protected UserManager userManager;

    /** The authenticated user, retrieved from the ChallengeResponse, or null. */
    protected String authUser = null;

    /** To be retrieved from the URL as the 'user' template parameter, or null. */
    protected String uriUser = null;

    /**
   * The user instance corresponding to the user indicated in the URI string, or null.
   */
    protected XMLUser user = null;

    /**
   * Provides the following representational variants: TEXT_XML.
   * 
   * @param context The context.
   * @param request The request object.
   * @param response The response object.
   */
    public SocNetResource(Context context, Request request, Response response) {
        super(context, request, response);
        if (request.getChallengeResponse() != null) {
            this.authUser = request.getChallengeResponse().getIdentifier();
        }
        userManager = (UserManager) getContext().getAttributes().get("UserManager");
        uriUser = (String) request.getAttributes().get("user");
        server = (Server) getContext().getAttributes().get("SocNetServer");
        getVariants().clear();
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }

    /**
   * The Restlet getRepresentation method which must be overridden by all concrete Resources.
   * 
   * @param variant The variant requested.
   * @return The Representation.
   */
    @Override
    public abstract Representation represent(Variant variant);

    /**
   * Creates and returns a new Restlet StringRepresentation built from xmlData. The xmlData will be
   * prefixed with a processing instruction indicating UTF-8 and version 1.0.
   * 
   * @param xmlData The xml data as a string.
   * @return A StringRepresentation of that xmldata.
   */
    public static StringRepresentation getStringRepresentation(String xmlData) {
        StringBuilder builder = new StringBuilder(500);
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append(xmlData);
        return new StringRepresentation(builder, MediaType.TEXT_XML, Language.ALL, CharacterSet.UTF_8);
    }

    /**
   * Helper function that removes any newline characters from the supplied string and replaces them
   * with a blank line.
   * 
   * @param msg The msg whose newlines are to be removed.
   * @return The string without newlines.
   */
    private String removeNewLines(String msg) {
        return msg.replace(System.getProperty("line.separator"), " ");
    }

    /**
   * Called when an exception is caught while processing a request. Just sets the response code.
   * 
   * @param timestamp The timestamp that could not be parsed.
   */
    protected void setStatusBadTimestamp(String timestamp) {
        this.responseMsg = ResponseMessage.badTimestamp(this, timestamp);
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
    }

    /**
   * Called when an exception is caught while processing a request. Just sets the response code.
   * 
   * @param e The exception that was caught.
   */
    protected void setStatusInternalError(Exception e) {
        this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
    }

    /**
   * Called when a miscellaneous "one off" error is caught during processing.
   * 
   * @param msg A description of the error.
   */
    protected void setStatusMiscError(String msg) {
        this.responseMsg = ResponseMessage.miscError(this, msg);
        getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
    }

    /**
   * Returns true if the authorized user is the administrator. Otherwise sets the Response status
   * and returns false.
   * 
   * @return True if the authorized user is the admin.
   */
    protected boolean validateAuthUserIsAdmin() {
        try {
            if (userManager.isAdmin(this.authUser)) {
                return true;
            } else {
                this.responseMsg = ResponseMessage.adminOnly(this);
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED, removeNewLines(this.responseMsg));
                return false;
            }
        } catch (RuntimeException e) {
            this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
        }
        return false;
    }

    /**
   * Returns true if the user in the URI string is defined in the UserManager. Otherwise sets the
   * Response status and returns false. If it returns true, then this.user has the corresponding
   * User instance.
   * 
   * @return True if the URI user is a real user.
   */
    protected boolean validateUriUserIsUser() {
        try {
            this.user = this.userManager.getUser(this.uriUser);
            if (this.user == null) {
                this.responseMsg = ResponseMessage.undefinedUser(this, this.uriUser);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
        }
        return false;
    }

    protected boolean validateAuthUserIsUser() {
        try {
            this.user = this.userManager.getUser(this.authUser);
            if (this.user == null) {
                this.responseMsg = ResponseMessage.undefinedUser(this, this.authUser);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
        }
        return false;
    }

    protected boolean validateAuthUserIsAdminOrUser() {
        try {
            if (userManager.isAdmin(this.authUser) || this.authUser.equals(this.authUser)) {
                return true;
            } else {
                this.responseMsg = ResponseMessage.adminOrAuthUserOnly(this, this.authUser, this.uriUser);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
                return false;
            }
        } catch (RuntimeException e) {
            this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
        }
        return false;
    }

    /**
   * Returns true if the authorized user is either the admin or user in the URI string. Otherwise
   * sets the Response status and returns false.
   * 
   * @return True if the authorized user is the admin or the URI user.
   */
    protected boolean validateAuthUserIsAdminOrUriUser() {
        try {
            if (userManager.isAdmin(this.authUser) || this.uriUser.equals(this.authUser)) {
                return true;
            } else {
                this.responseMsg = ResponseMessage.adminOrAuthUserOnly(this, this.authUser, this.uriUser);
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, removeNewLines(this.responseMsg));
                return false;
            }
        } catch (RuntimeException e) {
            this.responseMsg = ResponseMessage.internalError(this, this.getLogger(), e);
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, removeNewLines(this.responseMsg));
        }
        return false;
    }
}
