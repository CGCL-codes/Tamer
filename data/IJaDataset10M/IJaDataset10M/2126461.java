package org.mobicents.diameter.stack.functional.acc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Mode;
import org.jdiameter.api.Request;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ClientAccSessionListener;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.acc.events.AccountRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.common.api.app.acc.ClientAccSessionState;
import org.jdiameter.common.api.app.acc.IClientAccActionContext;
import org.jdiameter.common.impl.app.acc.AccSessionFactoryImpl;
import org.jdiameter.common.impl.app.acc.AccountRequestImpl;
import org.mobicents.diameter.stack.functional.StateChange;
import org.mobicents.diameter.stack.functional.TBase;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractClient extends TBase implements ClientAccSessionListener, IClientAccActionContext {

    protected static final int ACC_REQUEST_TYPE_INITIAL = 2;

    protected static final int ACC_REQUEST_TYPE_INTERIM = 3;

    protected static final int ACC_REQUEST_TYPE_TERMINATE = 4;

    protected static final int ACC_REQUEST_TYPE_EVENT = 1;

    protected ClientAccSession clientAccSession;

    protected int ccRequestNumber = 0;

    protected List<StateChange<ClientAccSessionState>> stateChanges = new ArrayList<StateChange<ClientAccSessionState>>();

    public void init(InputStream configStream, String clientID) throws Exception {
        try {
            super.init(configStream, clientID, ApplicationId.createByAccAppId(0, 300));
            AccSessionFactoryImpl creditControlSessionFactory = new AccSessionFactoryImpl(this.sessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ServerAccSession.class, creditControlSessionFactory);
            ((ISessionFactory) sessionFactory).registerAppFacory(ClientAccSession.class, creditControlSessionFactory);
            creditControlSessionFactory.setStateListener(this);
            creditControlSessionFactory.setClientSessionListener(this);
            creditControlSessionFactory.setClientContextListener(this);
            this.clientAccSession = ((ISessionFactory) this.sessionFactory).getNewAppSession(this.sessionFactory.getSessionId("xxTESTxx"), getApplicationId(), ClientAccSession.class, (Object) null);
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

    @Override
    public void receivedSuccessMessage(Request request, Answer answer) {
        fail("Received \"SuccessMessage\" event, request[" + request + "], answer[" + answer + "]", null);
    }

    @Override
    public void timeoutExpired(Request request) {
        fail("Received \"Timoeout\" event, request[" + request + "]", null);
    }

    @Override
    public Answer processRequest(Request request) {
        fail("Received \"Request\" event, request[" + request + "]", null);
        return null;
    }

    public void interimIntervalElapses(ClientAccSession appSession, Request interimRequest) throws InternalException {
    }

    public boolean failedSendRecord(ClientAccSession appSession, Request accRequest) throws InternalException {
        return false;
    }

    public void disconnectUserOrDev(ClientAccSession appSession, Request sessionTermRequest) throws InternalException {
    }

    protected AccountRequest createAcc(int ccRequestType, int requestNumber, ClientAccSession ccaSession) throws Exception {
        AccountRequest ccr = new AccountRequestImpl(ccaSession.getSessions().get(0).createRequest(AccountRequest.code, getApplicationId(), getServerRealmName()));
        AvpSet ccrAvps = ccr.getMessage().getAvps();
        ccrAvps.removeAvp(Avp.ORIGIN_HOST);
        ccrAvps.addAvp(Avp.ORIGIN_HOST, getClientURI(), true);
        ccr.setAccountingRecordType(ccRequestType);
        ccr.setAccountingRecordNumber(requestNumber);
        if (ccrAvps.getAvp(Avp.ACCT_APPLICATION_ID) == null) {
            ccrAvps.addAvp(Avp.ACCT_APPLICATION_ID, getApplicationId().getAcctAppId());
        }
        ccrAvps.addAvp(Avp.USER_NAME, "ala@kota.ma.bez.siersci", false);
        return ccr;
    }

    public String getSessionId() {
        return this.clientAccSession.getSessionId();
    }

    public void fetchSession(String sessionId) throws InternalException {
        this.clientAccSession = stack.getSession(sessionId, ClientAccSession.class);
    }

    public ClientAccSession getSession() {
        return this.clientAccSession;
    }

    public List<StateChange<ClientAccSessionState>> getStateChanges() {
        return stateChanges;
    }

    protected abstract int getChargingUnitsTime();

    protected abstract String getServiceContextId();
}
