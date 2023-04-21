package issrg.gt4;

import org.globus.wsrf.utils.ContextUtils;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import java.security.cert.X509Certificate;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import issrg.pba.PBAAPI;
import issrg.pba.PbaException;
import issrg.globus.SamlADF;
import protocol._0._1.SAML.tc.names.oasis.Request;
import protocol._0._1.SAML.tc.names.oasis.Response;
import javax.security.auth.Subject;
import org.globus.wsrf.security.SecurityManager;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.jaas.JaasGssUtil;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.impl.security.SecurityMessageElement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.rmi.RemoteException;

/**
 * This is the implementation of the Permis Authz Service which accepts
 * SAMLRequests and returns SAMLResponses.
 */
public class PermisAuthorizationService extends PermisPDP implements PermisAuthzPortType {

    /**
   * This method specifies the behaviour of the Authorization Service. The given
   * Request is decoded and passes to PermisRBAC and the decision is encoded
   * as a Response and sent back.
   *
   * @param req - the Request sent by the client
   *
   * @return Response with the authorisation decision in it
   */
    public Response SAMLRequest(Request req) throws java.rmi.RemoteException {
        try {
            SamlADF saml = new SamlADF(getADF(org.apache.axis.MessageContext.getCurrentContext()));
            org.opensaml.SAMLRequest sreq = new org.opensaml.SAMLRequest(((MessageElement) req.get_any()[0].getParentElement()).getAsDOM());
            org.opensaml.SAMLResponse sresp = saml.process(sreq);
            if (sreq.isSigned()) {
                Subject systemSubject = null;
                try {
                    MessageContext ctx = MessageContext.getCurrentContext();
                    SecurityManager manager = SecurityManager.getManager(ctx);
                    systemSubject = manager.getServiceSubject();
                } catch (org.globus.wsrf.security.SecurityException exp) {
                    String err = "Unable to obtain service credentials";
                    throw new RemoteException(err, exp);
                }
                GlobusGSSCredentialImpl credential = (GlobusGSSCredentialImpl) JaasGssUtil.getCredential(systemSubject);
                Vector certs = getCertificates(credential);
                try {
                    sresp.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA, credential.getPrivateKey(), certs, false);
                } catch (org.opensaml.SAMLException exp) {
                    String err = "Error signing SAMLResponse";
                    throw new RemoteException(err, exp);
                }
            }
            Element responseElement = (Element) sresp.toDOM();
            NodeList responseChildren = responseElement.getChildNodes();
            List responseElements = new ArrayList();
            for (int i = 0; i < responseChildren.getLength(); i++) {
                Node child = responseChildren.item(i);
                if (child instanceof Element) {
                    responseElements.add(new SecurityMessageElement((Element) child));
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String issueInstant = formatter.format(sresp.getIssueInstant());
            Response response = new Response((MessageElement[]) responseElements.toArray(new MessageElement[responseElements.size()]), sreq.getRequestId(), issueInstant, 1, 0, sresp.getRecipient(), sresp.getResponseId());
            return response;
        } catch (PbaException pe) {
            throw new java.rmi.RemoteException("Failed to process request: " + pe.getMessage(), pe);
        } catch (Exception e) {
            throw new java.rmi.RemoteException("Failed to process request: " + e.getMessage(), e);
        }
    }

    /**
   * This method gets the configuration parameters from the MessageContext and
   * constructs a PBAAPI out of it.
   *
   * @param ctx - the MessageContext with the configuration settings in it
   *
   * @return instance of PBAAPI
   */
    protected PBAAPI getADF(MessageContext ctx) throws InitializeException {
        String soa = (String) ContextUtils.getServiceProperty(ctx, SamlADF.SOA_STRING);
        String oid = (String) ContextUtils.getServiceProperty(ctx, SamlADF.OID_STRING);
        String ldapURL = (String) ContextUtils.getServiceProperty(ctx, SamlADF.LDAP_URL_STRING);
        String ldapAC_attribute = (String) ContextUtils.getServiceProperty(ctx, SamlADF.LDAP_AC_ATTRIBUTE_STRING);
        String ldapPKC_attribute = (String) ContextUtils.getServiceProperty(ctx, SamlADF.LDAP_PKC_ATTRIBUTE_STRING);
        String uRL = (String) ContextUtils.getServiceProperty(ctx, SamlADF.URL_STRING);
        String rootCA = (String) ContextUtils.getServiceProperty(ctx, SamlADF.ROOT_CA_STRING);
        return getADF(soa, oid, ldapURL, uRL, ldapAC_attribute, ldapPKC_attribute, rootCA);
    }

    /**
   * The default method implementation for the GT4 service.
   */
    public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy destroyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType, org.oasis.wsrf.lifetime.ResourceUnknownFaultType {
        return new org.oasis.wsrf.lifetime.DestroyResponse();
    }

    /**
   * This method throws a RemoteException all the time, since no Resources are
   * supported by PermisAuthorization.
   */
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        throw new java.rmi.RemoteException("Operation not supported");
    }

    /**
   * This method throws a org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType
   * all the time, since no Resources are
   * supported by PermisAuthorization.
   */
    public org.oasis.wsrf.properties.SetResourcePropertiesResponse setResourceProperties(org.oasis.wsrf.properties.SetResourceProperties_Element setResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.SetResourcePropertyRequestFailedFaultType, org.oasis.wsrf.properties.InvalidSetResourcePropertiesRequestContentFaultType {
        throw new org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType();
    }

    /**
   * This method throws a RemoteException all the time, since no Resources are
   * supported by PermisAuthorization.
   */
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType {
        throw new java.rmi.RemoteException("Operation not supported");
    }

    /**
   * This method is used to extract the chain of X.509 PKCs in the request.
   *
   * @param credential - the GSS credential with the chain of server X.509 PKCs
   *   and their private keys
   *
   * @return Vector of the PKCs and private keys; they will be used to sign
   *   the response
   */
    private Vector getCertificates(GlobusGSSCredentialImpl credential) {
        X509Certificate certArray[] = credential.getCertificateChain();
        Vector certs = null;
        if (certArray.length > 0) {
            certs = new Vector(certArray.length);
            for (int i = 0; i < certArray.length; i++) {
                certs.add(certArray[i]);
            }
        }
        return certs;
    }
}
