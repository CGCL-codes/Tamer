package org.xaware.server.engine.channel.sf;

import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.IChannelKey;
import org.xaware.server.engine.IChannelSpecification;
import org.xaware.server.engine.IGenericChannelTemplate;
import org.xaware.server.engine.channel.LocalChannelKey;
import org.xaware.server.engine.exceptions.XAwareConfigMissingException;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;
import org.xaware.server.engine.channel.AbstractConnectionChannel;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.SessionHeader;
import com.sforce.soap.partner.SforceServiceLocator;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.fault.ExceptionCode;
import com.sforce.soap.partner.fault.LoginFault;

/**
 * This is the implementation of the SalesForce channel specification. It takes a
 * xa:sf Element from the bizdriver, and sticks child element values in it's Properties object.
 * 
 * @author openweaver
 */
public class SalesForceChannelSpecification extends AbstractConnectionChannel implements IChannelSpecification {

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(SalesForceChannelSpecification.class.getName());

    private Element connectionElement;

    private String className = SalesForceChannelSpecification.class.getName();

    public Object getChannelObject() throws XAwareException {
        throw new XAwareException(className + ".getChannelObject() is not implemented instead use getChannelTemplate()");
    }

    /**
	 * Transfer Element and attribute values from the JDOM
	 * 
     * @see org.xaware.server.engine.channel.AbstractChannelSpecification#transformSpecInfo(org.xaware.server.engine.IBizViewContext)
     */
    @Override
    public void transformSpecInfo(final IBizViewContext bizViewContext) throws XAwareConfigMissingException, XAwareSubstitutionException, XAwareException {
        final Namespace xaNamespace = XAwareConstants.xaNamespace;
        connectionElement = m_bizDriverRootElement.getChild(XAwareConstants.BIZDRIVER_CONNECTION, xaNamespace);
        parseConnectionDefinition(connectionElement, bizViewContext, getBizDriverIdentifier() + ":SalesforceChannelSpec", lf, false, false);
        addProperty(XAwareConstants.BIZDRIVER_TIMEOUT, getChildElementValue(connectionElement, XAwareConstants.BIZDRIVER_TIMEOUT, bizViewContext, false));
    }

    /**
	 * This is what is used by caching and transactions to determine if there
	 * needs to be a new BizDriver and ChannelSpecification created or if it can
	 * reuse this one. The key should uniquely identify this BizDriver so it can
	 * be re-used or not re-used.
	 * 
	 * @return String
	 */
    public IChannelKey produceKey() {
        StringBuffer buff = new StringBuffer(getProperty(PN_BIZDRIVER_IDENTIFIER));
        buff.append(getProperty(XAwareConstants.BIZDRIVER_URL));
        buff.append(getProperty(XAwareConstants.BIZDRIVER_USER));
        buff.append(getProperty(XAwareConstants.BIZDRIVER_PWD));
        return new LocalChannelKey(buff.toString());
    }

    public IGenericChannelTemplate getChannelTemplate() throws XAwareException {
        String url = getProperty(XAwareConstants.BIZDRIVER_URL);
        URL sfURL = null;
        SoapBindingStub binding = null;
        if (url != null && url.length() > 0) {
            try {
                sfURL = new URL(url);
            } catch (java.net.MalformedURLException e) {
                throw new XAwareException("Malformed " + XAwareConstants.xaNamespace.getPrefix() + ":" + XAwareConstants.BIZDRIVER_URL + e.getLocalizedMessage());
            }
        }
        try {
            if (sfURL != null) {
                binding = (SoapBindingStub) new SforceServiceLocator().getSoap(sfURL);
            } else {
                binding = (SoapBindingStub) new SforceServiceLocator().getSoap();
            }
        } catch (ServiceException e) {
            throw new XAwareException("Service exception" + e.getLocalizedMessage());
        } catch (Exception e) {
            throw new XAwareException("Service exception" + e.getLocalizedMessage());
        }
        String timeout = getProperty(XAwareConstants.BIZDRIVER_TIMEOUT);
        int sfTimeout = 60000;
        if (timeout != null) {
            try {
                sfTimeout = Integer.parseInt(timeout);
            } catch (NumberFormatException e) {
                throw new XAwareException("time out value must be a number: " + e.getLocalizedMessage());
            }
        }
        binding.setTimeout(sfTimeout);
        String uid = getProperty(XAwareConstants.BIZDRIVER_USER);
        if (uid == null) {
            uid = "";
        }
        String pwd = getProperty(XAwareConstants.BIZDRIVER_PWD);
        if (pwd == null) {
            pwd = "";
        }
        LoginResult loginResult;
        try {
            loginResult = binding.login(uid, pwd);
        } catch (LoginFault ex) {
            ExceptionCode exCode = ex.getExceptionCode();
            String message = null;
            if (exCode == ExceptionCode.FUNCTIONALITY_NOT_ENABLED) {
                message = "functionality not enabled.";
            } else if (exCode == ExceptionCode.INVALID_CLIENT) {
                message = "invalid client.";
            } else if (exCode == ExceptionCode.INVALID_LOGIN) {
                message = "invalid login.";
            } else if (exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_DOMAIN) {
                message = "login during restricted domain.";
            } else if (exCode == ExceptionCode.LOGIN_DURING_RESTRICTED_TIME) {
                message = "login during restricted time.";
            } else if (exCode == ExceptionCode.ORG_LOCKED) {
                message = "org locked.";
            } else if (exCode == ExceptionCode.PASSWORD_LOCKOUT) {
                message = "password lockout.";
            } else if (exCode == ExceptionCode.SERVER_UNAVAILABLE) {
                message = "server unavailable.";
            } else if (exCode == ExceptionCode.TRIAL_EXPIRED) {
                message = "trial expired.";
            } else if (exCode == ExceptionCode.UNSUPPORTED_CLIENT) {
                message = "unsupported client.";
            } else {
                message = "an unexpected error.";
            }
            throw new XAwareException("Sales force returned " + message + ": " + ex.getExceptionMessage());
        } catch (Exception ex) {
            throw new XAwareException("An unexpected error has occurred: " + ex.getLocalizedMessage());
        }
        if (loginResult.isPasswordExpired()) {
            throw new XAwareException("An error has occurred. Your password has expired.");
        }
        binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
        SessionHeader sh = new SessionHeader();
        sh.setSessionId(loginResult.getSessionId());
        binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);
        return (IGenericChannelTemplate) new SalesForceTemplate(binding);
    }
}
