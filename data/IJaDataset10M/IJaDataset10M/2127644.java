package org.mobicents.diameter.stack.functional.cxdx.base;

import java.io.InputStream;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.NetworkReqListener;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.cxdx.ClientCxDxSession;
import org.jdiameter.api.cxdx.ServerCxDxSession;
import org.jdiameter.api.cxdx.events.JRegistrationTerminationAnswer;
import org.jdiameter.api.cxdx.events.JRegistrationTerminationRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.common.impl.app.cxdx.CxDxSessionFactoryImpl;
import org.jdiameter.common.impl.app.cxdx.JRegistrationTerminationAnswerImpl;
import org.mobicents.diameter.stack.functional.Utils;
import org.mobicents.diameter.stack.functional.cxdx.AbstractClient;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ClientRTR extends AbstractClient {

    protected boolean receivedRegistrationTermination;

    protected boolean sentRegistrationTermination;

    protected JRegistrationTerminationRequest request;

    /**
	 * 
	 */
    public ClientRTR() {
    }

    public void init(InputStream configStream, String clientID) throws Exception {
        try {
            super.init(configStream, clientID, ApplicationId.createByAuthAppId(10415, 16777216));
            CxDxSessionFactoryImpl cxdxSessionFactory = new CxDxSessionFactoryImpl(this.sessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ServerCxDxSession.class, cxdxSessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ClientCxDxSession.class, cxdxSessionFactory);
            cxdxSessionFactory.setClientSessionListener(this);
        } finally {
            try {
                configStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRegistrationTermination() throws Exception {
        if (!receivedRegistrationTermination || request == null) {
            fail("Did not receive RTR or answer already sent.", null);
            throw new Exception("Did not receive RTR or answer already sent. Request: " + this.request);
        }
        JRegistrationTerminationAnswer answer = new JRegistrationTerminationAnswerImpl((Request) this.request.getMessage(), 2001);
        AvpSet reqSet = request.getMessage().getAvps();
        AvpSet set = answer.getMessage().getAvps();
        set.removeAvp(Avp.DESTINATION_HOST);
        set.removeAvp(Avp.DESTINATION_REALM);
        set.addAvp(reqSet.getAvp(Avp.CC_REQUEST_TYPE), reqSet.getAvp(Avp.CC_REQUEST_NUMBER), reqSet.getAvp(Avp.AUTH_APPLICATION_ID));
        if (set.getAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID) == null) {
            AvpSet vendorSpecificApplicationId = set.addGroupedAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID, 0, false, false);
            vendorSpecificApplicationId.addAvp(Avp.VENDOR_ID, getApplicationId().getVendorId(), true);
            vendorSpecificApplicationId.addAvp(Avp.AUTH_APPLICATION_ID, getApplicationId().getAuthAppId(), true);
        }
        if (set.getAvp(Avp.AUTH_SESSION_STATE) == null) {
            set.addAvp(Avp.AUTH_SESSION_STATE, 1);
        }
        this.clientCxDxSession.sendRegistrationTerminationAnswer(answer);
        Utils.printMessage(log, super.stack.getDictionary(), answer.getMessage(), true);
        this.request = null;
        this.sentRegistrationTermination = true;
    }

    @Override
    public void doRegistrationTerminationRequest(ClientCxDxSession session, JRegistrationTerminationRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
        if (this.receivedRegistrationTermination) {
            fail("Received RTR more than once", null);
            return;
        }
        this.receivedRegistrationTermination = true;
        this.request = request;
    }

    @Override
    public Answer processRequest(Request request) {
        int code = request.getCommandCode();
        if (code != JRegistrationTerminationAnswer.code) {
            fail("Received Request with code not used by CxDx!. Code[" + request.getCommandCode() + "]", null);
            return null;
        }
        if (super.clientCxDxSession != null) {
            fail("Received Request in base listener, not in app specific!" + code, null);
        } else {
            try {
                super.clientCxDxSession = ((ISessionFactory) this.sessionFactory).getNewAppSession(request.getSessionId(), getApplicationId(), ClientCxDxSession.class, (Object) null);
                ((NetworkReqListener) this.clientCxDxSession).processRequest(request);
            } catch (Exception e) {
                e.printStackTrace();
                fail(null, e);
            }
        }
        return null;
    }

    public boolean isReceivedRegistrationTermination() {
        return receivedRegistrationTermination;
    }

    public boolean isSentRegistrationTermination() {
        return sentRegistrationTermination;
    }
}
