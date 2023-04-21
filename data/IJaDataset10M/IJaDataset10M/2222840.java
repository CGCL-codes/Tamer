package org.tridentproject.repository.api;

import org.tridentproject.repository.fedora.mgmt.*;
import org.apache.commons.codec.binary.Base64;
import java.io.IOException;
import java.sql.SQLException;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Guard;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Base resource class that supports common behaviours or attributes shared by
 * all resources.
 *
 */
public abstract class BaseResource extends Resource {

    static Configuration config = null;

    protected RepositoryClient rc;

    protected UserDbClient udc;

    protected RepositoryFactory rf = new RepositoryFactory();

    protected String strRepoURI;

    protected String strAuthlistQueueURI;

    protected String strFedoraMount = null;

    protected String RepositoryUser = null;

    protected String RepositoryPassword = null;

    protected String TridentUser = null;

    protected boolean boolAuthorized = false;

    protected Item requestItem = null;

    protected User requestUser = null;

    private static Logger log = Logger.getLogger(BaseResource.class);

    public BaseResource(Context context, Request request, Response response) {
        super(context, request, response);
        init();
    }

    protected void init() {
        log.debug("BaseResource: init()");
        try {
            Map<String, Object> requestAttributes = getRequest().getAttributes();
            Set<String> strAttributes = requestAttributes.keySet();
            for (String strAttribute : strAttributes) log.debug("base-resource attribute defined: " + strAttribute);
            ConfigurationFactory factory = new ConfigurationFactory();
            factory.setConfigurationURL(getClass().getResource("/properties.xml"));
            config = factory.getConfiguration();
            config = config.subset("trident");
            String strFedoraHost = config.getString("repository.fedora.host");
            String strFedoraPort = config.getString("repository.fedora.port");
            String strFedoraURL = "http://" + strFedoraHost + ":" + strFedoraPort + "/fedora";
            strFedoraMount = config.getString("repository.mount");
            setUserName();
            setAuthorization();
            log.debug("establishing new RepositoryClient");
            rc = new RepositoryClient(strFedoraHost, strFedoraPort, strFedoraURL, RepositoryUser, RepositoryPassword);
            udc = (UserDbClient) getRequest().getAttributes().get("udc");
            log.debug("udc = " + udc.getId());
            strRepoURI = "http://" + strFedoraHost + ":" + strFedoraPort + "/repository";
            strAuthlistQueueURI = config.getString("vocab.queue.location");
        } catch (FedoraAPIException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Representation SetRepositoryMessage(Status StatusCode, String HTTPLocation, String RepoMessageCode, String RepoMessageText, Document RepoMessageBody) {
        DomRepresentation representation = null;
        log.debug("SetRepositoryMessage(" + RepoMessageCode + "," + RepoMessageText + ")");
        try {
            getResponse().setStatus(StatusCode);
            if (HTTPLocation != null) getResponse().setLocationRef(HTTPLocation);
            representation = new DomRepresentation(MediaType.TEXT_XML);
            Document responseDoc = DocumentHelper.createDocument();
            Element responseElem = responseDoc.addElement("response");
            if (RepoMessageCode != null) responseElem.addElement("code").addText(RepoMessageCode);
            if (RepoMessageText != null) responseElem.addElement("message").addText(RepoMessageText);
            if (RepoMessageBody != null) responseElem.addElement("body").add(RepoMessageBody.getRootElement());
            DOMWriter writer = new DOMWriter();
            representation.setDocument(writer.write(responseDoc));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return representation;
        }
    }

    /**
     * Returns the TridentUser.  Returns null if TridentUser has not been set.
     *
     * @return the user name (TridentUser), or null if not set
     */
    protected String getUserName() {
        return TridentUser;
    }

    /**
     * Attempts to obtain a username from the HTTP X-Trident-User-Id header
     * of the request.  If the header does not exist, attempts to use config.
     *
     * Also sets the password
     *
     * Sets the username and password to RepositoryUser and RepositoryPassword
     * variables that are later used to connect to repository API
     *
     */
    protected void setUserName() {
        Request request = getRequest();
        Form requestHeaders = (Form) request.getAttributes().get("org.restlet.http.headers");
        TridentUser = requestHeaders.getValues("X-Trident-User-Id");
        log.debug("X-Trident-User-Id = " + TridentUser);
        if (TridentUser == null) {
            RepositoryUser = config.getString("repository.fedora.user");
            RepositoryPassword = config.getString("repository.fedora.password");
        } else {
            RepositoryUser = TridentUser;
            RepositoryPassword = "Cr@ckTh!5";
        }
    }

    /**
     * Attempts to obtain a user/pass from the HTTP Authorization header
     * of the request.  If no Authorization header, boolAuthorized set to false.
     * Assumes that HTTP_BASIC is the scheme for Authorization header
     *
     * If user/pass match existing user/pass, boolAuthorized set to true.
     *
     * If user/pass do not match existing user/pass, boolAuthorized set to false.
     *
     */
    protected void setAuthorization() {
        boolAuthorized = false;
        Request request = getRequest();
        Form requestHeaders = (Form) request.getAttributes().get("org.restlet.http.headers");
        String strAuthzHeader = requestHeaders.getValues("authorization");
        log.debug("Authorization = " + strAuthzHeader);
        if (strAuthzHeader != null) {
            String strDecodedAuthz = new String(Base64.decodeBase64(strAuthzHeader.getBytes()));
            String strUser = strDecodedAuthz.split(":")[0];
            log.debug("Username = " + strUser);
            String strPassword = strDecodedAuthz.split(":")[1];
            boolAuthorized = ((ResourceApplication) getApplication()).verifyUser(strUser, strPassword);
        }
        log.debug("Authorized = " + boolAuthorized);
    }

    /**
     * Challenges the client for a username/password combo
     *
     **/
    protected void challenge() {
        Guard guard = new Guard(getContext(), ChallengeScheme.HTTP_BASIC, "Trident Client-Server");
        Representation rep = SetRepositoryMessage(Status.CLIENT_ERROR_UNAUTHORIZED, null, "ClientNotAuthorized", "Client did not supply proper authorization to perform function", null);
        getResponse().setEntity(rep);
        guard.challenge(getResponse(), false);
    }

    protected boolean userHasPermissionForItem(String strItemId, String strPermissionId, Representation rep) throws RepositoryException {
        log.debug("userHasPermissionForItem()");
        String strUserName = getUserName();
        if (strUserName == null) {
            String strMsg = "User not authorized to perform request";
            log.debug(strMsg);
            rep = SetRepositoryMessage(Status.CLIENT_ERROR_UNAUTHORIZED, null, "UserNotAllowed", strMsg, null);
            throw new RepositoryException(strMsg);
        }
        log.debug("getting user");
        requestUser = new User(udc);
        boolean userfound = false;
        try {
            userfound = requestUser.getUser(strUserName);
        } catch (SQLException e) {
            String strMsg = "Error attempting to find user, " + strUserName + ": " + e.getMessage();
            log.error(strMsg);
            rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", strMsg, null);
            throw new RepositoryException(strMsg, e);
        }
        if (!userfound) {
            String strMsg = "Unable to find user, " + strUserName;
            log.debug(strMsg);
            rep = SetRepositoryMessage(Status.CLIENT_ERROR_NOT_FOUND, null, "UserNotFound", strMsg, null);
            throw new RepositoryException(strMsg);
        }
        try {
            requestItem = rf.getItem(rc, strItemId);
        } catch (FedoraAPIException e) {
            String strMsg = "Unable to find item, " + strItemId + ": " + e.getMessage();
            log.debug(strMsg);
            rep = SetRepositoryMessage(Status.CLIENT_ERROR_NOT_FOUND, null, "ItemNotFound", strMsg, null);
            throw new RepositoryException(strMsg, e);
        }
        boolean hasPermission = false;
        try {
            List<String> itemGroups = null;
            itemGroups = requestItem.getEditGroups();
            if (itemGroups.isEmpty()) {
                hasPermission = requestUser.verifyPermission(strPermissionId);
            } else {
                for (String strGroupId : itemGroups) {
                    hasPermission = requestUser.verifyPermissionByGroup(strPermissionId, strGroupId);
                    if (hasPermission) break;
                }
            }
        } catch (FedoraAPIException e) {
            throw new RepositoryException("Error while retrieving group assignments from item, " + requestItem.getPid() + ": " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RepositoryException("Error attempting to verify that user, " + requestUser.getUserId() + ", has permission, " + strPermissionId + ", for item, " + requestItem.getPid() + ": " + e.getMessage(), e);
        }
        return hasPermission;
    }

    protected boolean userHasPermission(String strPermissionId, Representation rep) throws RepositoryException {
        String strUserName = getUserName();
        if (strUserName == null) {
            String strMsg = "User not authorized to perform request";
            log.debug(strMsg);
            rep = SetRepositoryMessage(Status.CLIENT_ERROR_UNAUTHORIZED, null, "UserNotAuthorized", strMsg, null);
            throw new RepositoryException(strMsg);
        }
        requestUser = new User(udc);
        boolean userfound = false;
        try {
            userfound = requestUser.getUser(strUserName);
        } catch (SQLException e) {
            String strMsg = "Error attempting to find user, " + strUserName + ": " + e.getMessage();
            log.error(strMsg);
            rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", strMsg, null);
            throw new RepositoryException(strMsg, e);
        }
        if (!userfound) {
            String strMsg = "Unable to find user, " + strUserName;
            log.debug(strMsg);
            rep = SetRepositoryMessage(Status.CLIENT_ERROR_NOT_FOUND, null, "UserNotFound", strMsg, null);
            throw new RepositoryException(strMsg);
        }
        boolean hasPermission = false;
        try {
            hasPermission = requestUser.verifyPermission(strPermissionId);
        } catch (SQLException e) {
            throw new RepositoryException("Error attempting to verify that user, " + requestUser.getUserId() + ", has permission, " + strPermissionId + ": " + e.getMessage(), e);
        }
        return hasPermission;
    }
}
