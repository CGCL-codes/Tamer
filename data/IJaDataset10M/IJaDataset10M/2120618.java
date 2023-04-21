package org.mobicents.diameter.stack.functional.ro;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Mode;
import org.jdiameter.api.ro.ClientRoSession;
import org.jdiameter.api.ro.ClientRoSessionListener;
import org.jdiameter.api.ro.ServerRoSession;
import org.jdiameter.api.ro.events.RoCreditControlRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.common.api.app.ro.ClientRoSessionState;
import org.jdiameter.common.api.app.ro.IClientRoSessionContext;
import org.jdiameter.common.impl.app.ro.RoCreditControlRequestImpl;
import org.jdiameter.common.impl.app.ro.RoSessionFactoryImpl;
import org.mobicents.diameter.stack.functional.StateChange;
import org.mobicents.diameter.stack.functional.TBase;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractClient extends TBase implements ClientRoSessionListener, IClientRoSessionContext {

    protected static final int CC_REQUEST_TYPE_INITIAL = 1;

    protected static final int CC_REQUEST_TYPE_INTERIM = 2;

    protected static final int CC_REQUEST_TYPE_TERMINATE = 3;

    protected static final int CC_REQUEST_TYPE_EVENT = 4;

    protected ClientRoSession clientRoSession;

    protected int ccRequestNumber = 0;

    protected List<StateChange<ClientRoSessionState>> stateChanges = new ArrayList<StateChange<ClientRoSessionState>>();

    public void init(InputStream configStream, String clientID) throws Exception {
        try {
            super.init(configStream, clientID, ApplicationId.createByAuthAppId(0, 4));
            RoSessionFactoryImpl creditControlSessionFactory = new RoSessionFactoryImpl(this.sessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ServerRoSession.class, creditControlSessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ClientRoSession.class, creditControlSessionFactory);
            creditControlSessionFactory.setStateListener(this);
            creditControlSessionFactory.setClientSessionListener(this);
            creditControlSessionFactory.setClientContextListener(this);
            this.clientRoSession = ((ISessionFactory) this.sessionFactory).getNewAppSession(this.sessionFactory.getSessionId("xxTESTxx"), getApplicationId(), ClientRoSession.class, (Object) null);
        } finally {
            try {
                configStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() throws IllegalDiameterStateException, InternalException {
        stack.start();
    }

    public void start(Mode mode, long timeOut, TimeUnit timeUnit) throws IllegalDiameterStateException, InternalException {
        stack.start(mode, timeOut, timeUnit);
    }

    public void stop(long timeOut, TimeUnit timeUnit, int disconnectCause) throws IllegalDiameterStateException, InternalException {
        stack.stop(timeOut, timeUnit, disconnectCause);
    }

    public void stop(int disconnectCause) {
        stack.stop(disconnectCause);
    }

    public long getDefaultTxTimerValue() {
        return 10;
    }

    public int getDefaultDDFHValue() {
        return 1;
    }

    public int getDefaultCCFHValue() {
        return 1;
    }

    public void txTimerExpired(ClientRoSession session) {
    }

    public void grantAccessOnDeliverFailure(ClientRoSession clientCCASessionImpl, Message request) {
    }

    public void denyAccessOnDeliverFailure(ClientRoSession clientCCASessionImpl, Message request) {
    }

    public void grantAccessOnTxExpire(ClientRoSession clientCCASessionImpl) {
    }

    public void denyAccessOnTxExpire(ClientRoSession clientCCASessionImpl) {
    }

    public void grantAccessOnFailureMessage(ClientRoSession clientCCASessionImpl) {
    }

    public void denyAccessOnFailureMessage(ClientRoSession clientCCASessionImpl) {
    }

    public void indicateServiceError(ClientRoSession clientCCASessionImpl) {
    }

    protected RoCreditControlRequest createCCR(int ccRequestType, int requestNumber, ClientRoSession ccaSession) throws Exception {
        RoCreditControlRequest ccr = new RoCreditControlRequestImpl(ccaSession.getSessions().get(0).createRequest(RoCreditControlRequest.code, getApplicationId(), getServerRealmName()));
        AvpSet ccrAvps = ccr.getMessage().getAvps();
        ccrAvps.removeAvp(Avp.ORIGIN_HOST);
        ccrAvps.addAvp(Avp.ORIGIN_HOST, getClientURI(), true);
        ccrAvps.addAvp(Avp.AUTH_APPLICATION_ID, 4);
        String serviceContextId = getServiceContextId();
        if (serviceContextId == null) {
            serviceContextId = UUID.randomUUID().toString().replaceAll("-", "") + "@mss.mobicents.org";
        }
        ccrAvps.addAvp(Avp.SERVICE_CONTEXT_ID, serviceContextId, false);
        ccrAvps.addAvp(Avp.CC_REQUEST_TYPE, ccRequestType);
        ccrAvps.addAvp(Avp.CC_REQUEST_NUMBER, requestNumber);
        ccrAvps.removeAvp(Avp.DESTINATION_HOST);
        AvpSet subscriptionId = ccrAvps.addGroupedAvp(Avp.SUBSCRIPTION_ID);
        subscriptionId.addAvp(Avp.SUBSCRIPTION_ID_TYPE, 2);
        subscriptionId.addAvp(Avp.SUBSCRIPTION_ID_DATA, "sip:alexandre@mobicents.org", false);
        AvpSet rsuAvp = ccrAvps.addGroupedAvp(Avp.REQUESTED_SERVICE_UNIT);
        rsuAvp.addAvp(Avp.CC_TIME, getChargingUnitsTime());
        return ccr;
    }

    public String getSessionId() {
        return this.clientRoSession.getSessionId();
    }

    public void fetchSession(String sessionId) throws InternalException {
        this.clientRoSession = stack.getSession(sessionId, ClientRoSession.class);
    }

    public ClientRoSession getSession() {
        return this.clientRoSession;
    }

    public List<StateChange<ClientRoSessionState>> getStateChanges() {
        return stateChanges;
    }

    protected abstract int getChargingUnitsTime();

    protected abstract String getServiceContextId();
}
