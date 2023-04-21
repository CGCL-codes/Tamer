package issrg.globus;

import issrg.globus.PermisAuthzServicePortType;
import issrg.globus.service.PermisAuthzServiceLocator;
import org.globus.ogsa.security.authorization.SAMLRequestType;
import org.globus.ogsa.security.authorization.SAMLResponseType;
import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;
import org.globus.axis.gsi.GSIConstants;
import org.globus.ogsa.impl.security.Constants;
import javax.xml.rpc.Stub;
import org.globus.ogsa.impl.security.authorization.SelfAuthorization;
import org.opensaml.SAMLRequest;
import org.opensaml.SAMLResponse;
import org.opensaml.SAMLException;

/**
* This class is a PermisAuthz implementation for the test client. It is a
* grid service client.
*/
public class GridSamlSender implements issrg.globus.PermisAuthz {

    protected PermisAuthzServicePortType permis;

    /**
	* This constructor builds a Grid Service client, and communicates to
	* it without authentication.
	*
	* <p>This is equivalent to GridSamlSender(host, false).
	*/
    public GridSamlSender(String host) throws Exception {
        this(host, false);
    }

    /**
	* This constructor builds a Grid Service client, and makes the 
	* conversation secure if told so.
	*
	* @param host is the host URL
	* @param secure is a boolean telling whether communication should be
	*	secured - communication is strongly authenticated, if this is 
	*	set to true
	*/
    public GridSamlSender(String host, boolean secure) throws Exception {
        PermisAuthzServiceLocator pasl = new PermisAuthzServiceLocator();
        permis = pasl.getPermisAuthzServicePort(new java.net.URL(host));
        if (secure) {
            System.out.println("setting up a secure conversation");
            ((Stub) permis)._setProperty(Constants.GSI_SEC_CONV, Constants.SIGNATURE);
            ((Stub) permis)._setProperty(GSIConstants.GSI_MODE, GSIConstants.GSI_MODE_NO_DELEG);
            ((Stub) permis)._setProperty(Constants.AUTHORIZATION, SelfAuthorization.getInstance());
        }
    }

    /**
	* This method sends the SAML Request and returns the response from
	* a PermisAuthz grid service.
	*
	* @param saml - the SAMLRequest to be sent to the grid service
	*
	* @return SAMLResponse returned by the service
	*/
    public SAMLResponse processSAMLRequest(SAMLRequest saml) throws java.rmi.RemoteException {
        SAMLRequestType req = new SAMLRequestType();
        req.set_any(new MessageElement[] { new MessageElement((Element) saml.toDOM()) });
        SAMLResponseType resp = permis.SAMLRequest(req);
        try {
            return new SAMLResponse(resp.get_any()[0].getAsDOM());
        } catch (Exception sex) {
            throw new java.rmi.UnexpectedException("SAML Response cannot be parsed", sex);
        }
    }
}
