package fi.hip.gb.gateway.servlet.saml20;

import fi.hip.gb.gateway.config.Constants;
import fi.hip.gb.gateway.config.Metadata;
import fi.hip.gb.gateway.config.DatabaseFactory;
import fi.hip.gb.gateway.config.Configuration;
import fi.hip.gb.gateway.userbase.User;
import fi.hip.gb.gateway.userbase.FederationStore;
import fi.hip.gb.gateway.userbase.FederationInfo;
import fi.hip.gb.gateway.util.Base64;
import fi.hip.gb.gateway.util.CompressUtil;
import fi.hip.gb.gateway.util.Util;
import fi.hip.gb.gateway.util.xml.SAML20Util;
import org.opensaml.saml2.core.ManageNameIDRequest;
import org.opensaml.saml2.core.ManageNameIDResponse;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NewID;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.ManageNameIDResponseBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet Class
 *
 * @web.servlet              name="ManageNameIDService"
 *                           display-name="Manage NameID Service"
 *                           description="Description for ManageNameIDService"
 * @web.servlet-mapping      url-pattern="/ManageNameIDService"
 * @web.servlet-init-param   name="A parameter"
 *                           value="A value"
 */
public class ManageNameIDService extends HttpServlet {

    private Logger logger = Logger.getLogger(ManageNameIDService.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        if (user == null) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            logger.debug(request.getQueryString());
            String queryStr = request.getQueryString();
            String samlRequestQuery = Util.getQueryParameter(queryStr, "SAMLRequest");
            String relayStateQuery = Util.getQueryParameter(queryStr, "RelayState");
            String sigAlgUri = Util.getQueryParameter(queryStr, "SigAlg");
            logger.debug(samlRequestQuery);
            String samlRequestStr = CompressUtil.deCompressBytesToStr(Base64.decode(samlRequestQuery));
            logger.debug(samlRequestStr);
            ManageNameIDRequest nameIdRequest = null;
            try {
                nameIdRequest = SAML20Util.getManageNameIDRequestFromString(samlRequestStr);
            } catch (Exception e) {
                logger.error("Error during the SAMLRequest String->XML conversion!");
                e.printStackTrace();
            }
            ManageNameIDResponse nameIdResponse = processNameIdRequest(nameIdRequest, relayStateQuery, sigAlgUri, user);
        }
    }

    private ManageNameIDResponse processNameIdRequest(ManageNameIDRequest nameIdRequest, String relayState, String sigAlg, User user) {
        String providerId = nameIdRequest.getIssuer().getValue();
        ManageNameIDResponse response = makeResponseSkeleton(nameIdRequest.getID(), providerId);
        NameID nameId = nameIdRequest.getNameID();
        NewID newId = nameIdRequest.getNewID();
        if (nameId.getFormat().equals(NameID.PERSISTENT)) {
            logger.debug("NameID format is persistent.");
            FederationStore fedStore = DatabaseFactory.getFederationStore();
            FederationInfo fedInfo = fedStore.getFederationInfoBySp(providerId, nameId.getValue());
            if (fedInfo.getLocalId().equals(user.getIdentifier())) {
                logger.debug("Old NameID matched with the user.");
                fedStore.updateSpId(providerId, nameId.getValue(), newId.getNewID());
                logger.info("NameID updated for " + user.getIdentifier());
                return finalizeResponse(response, relayState, StatusCode.SUCCESS_URI);
            } else {
                logger.error("NameID did not match with the authenticated user.");
                return finalizeResponse(response, relayState, StatusCode.INVALID_NAMEID_POLICY_URI);
            }
        } else {
            logger.error("NameID format is not persistent and thus not supported!");
            return finalizeResponse(response, relayState, StatusCode.INVALID_NAMEID_POLICY_URI);
        }
    }

    private ManageNameIDResponse makeResponseSkeleton(String originalId, String recipient) {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        ManageNameIDResponseBuilder builder = (ManageNameIDResponseBuilder) builderFactory.getBuilder(ManageNameIDResponse.DEFAULT_ELEMENT_NAME);
        ManageNameIDResponse response = builder.buildObject();
        response.setInResponseTo(originalId);
        response.setID(Util.generateIdentifier());
        response.setDestination(recipient);
        return response;
    }

    private ManageNameIDResponse finalizeResponse(ManageNameIDResponse response, String relayState, String statusCode) {
        Status status = SAML20Util.makeStatus(statusCode);
        response.setStatus(status);
        return response;
    }
}
