package org.mobicents.tests.diameter.base.acr;

import static org.jdiameter.common.api.app.acc.ServerAccSessionState.IDLE;
import java.io.InputStream;
import org.apache.log4j.Level;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Network;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.acc.ServerAccSessionListener;
import org.jdiameter.api.acc.events.AccountRequest;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.common.impl.app.acc.AccSessionFactoryImpl;
import org.jdiameter.common.impl.app.acc.AccountAnswerImpl;
import org.jdiameter.server.impl.app.acc.ServerAccSessionImpl;
import org.mobicents.tests.diameter.AbstractStackRunner;

/**
 * @author baranowb
 * 
 */
public class ACR extends AbstractStackRunner implements ServerAccSessionListener {

    private ApplicationId acrAppId = ApplicationId.createByAccAppId(193, 19302);

    private AccSessionFactoryImpl accSessionFactory;

    public ACR() {
        super();
    }

    @Override
    public void configure(InputStream f) throws Exception {
        super.configure(f);
        Network network = stack.unwrap(Network.class);
        network.addNetworkReqListener(this, acrAppId);
        accSessionFactory = new AccSessionFactoryImpl(super.factory);
        accSessionFactory.setServerSessionListener(this);
        ((ISessionFactory) super.factory).registerAppFacory(ServerAccSession.class, accSessionFactory);
    }

    public Answer processRequest(Request request) {
        if (request.getCommandCode() != 271) {
            if (super.log.isEnabledFor(Level.ERROR)) {
                super.log.error("Received non ACR message, discarding.");
                dumpMessage(request, false);
            }
            return null;
        }
        ApplicationId appId = request.getApplicationIdAvps().isEmpty() ? null : request.getApplicationIdAvps().iterator().next();
        try {
            ServerAccSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(request.getSessionId(), appId, ServerAccSession.class, request);
            session.addStateChangeNotification(new LocalStateChangeListener(session));
            ((ServerAccSessionImpl) session).processRequest(request);
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IllegalDiameterStateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void receivedSuccessMessage(Request arg0, Answer arg1) {
        if (super.log.isEnabledFor(Level.ERROR)) {
            super.log.error("Received answer");
            dumpMessage(arg1, false);
        }
    }

    public void timeoutExpired(Request arg0) {
        if (super.log.isInfoEnabled()) {
            super.log.info("Timeout expired");
            dumpMessage(arg0, true);
        }
    }

    public void doAccRequestEvent(ServerAccSession session, AccountRequest arg1) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
        Message answer = super.createAnswer((Request) arg1.getMessage(), 2001, arg1.getMessage().getApplicationIdAvps().iterator().next());
        AvpSet set = answer.getAvps();
        try {
            set.addAvp(480, arg1.getAccountingRecordType());
        } catch (AvpDataException e1) {
            e1.printStackTrace();
        }
        try {
            set.addAvp(485, arg1.getAccountingRecordNumber(), true);
        } catch (AvpDataException e1) {
            e1.printStackTrace();
        }
        try {
            AvpSet vendorSpecificApplicationId = set.getAvp(260).getGrouped();
            vendorSpecificApplicationId.addAvp(258, 0l, true);
        } catch (AvpDataException e1) {
            e1.printStackTrace();
        }
        if (log.isInfoEnabled()) {
            log.info("Received Request: " + ((Request) arg1.getMessage()).getCommandCode() + "\nE2E:" + ((Request) arg1.getMessage()).getEndToEndIdentifier() + "\nHBH:" + ((Request) arg1.getMessage()).getHopByHopIdentifier() + "\nAppID:" + ((Request) arg1.getMessage()).getApplicationId());
            log.info("Request AVPS: \n");
            try {
                printAvps(((Request) arg1.getMessage()).getAvps());
            } catch (AvpDataException e) {
                e.printStackTrace();
            }
            log.info("Created answer: " + answer.getCommandCode() + "\nE2E:" + answer.getEndToEndIdentifier() + "\nHBH:" + answer.getHopByHopIdentifier() + "\nAppID:" + answer.getApplicationId());
            log.info("Answer AVPS: \n");
            try {
                printAvps(answer.getAvps());
            } catch (AvpDataException e) {
                e.printStackTrace();
            }
        }
        AccountAnswerImpl ans = new AccountAnswerImpl((Answer) answer);
        session.sendAccountAnswer(ans);
    }

    public void doOtherEvent(AppSession arg0, AppRequestEvent arg1, AppAnswerEvent arg2) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    }

    private class LocalStateChangeListener implements StateChangeListener<AppSession> {

        private ServerAccSession session;

        public LocalStateChangeListener(ServerAccSession session) {
            super();
            this.session = session;
        }

        public void stateChanged(Enum oldState, Enum newState) {
            if (session.isStateless() && newState == IDLE) {
                session.release();
            }
            if (log.isInfoEnabled()) {
                log.info("Application changed state from[" + oldState + "] to[" + newState + "]");
            }
        }

        public void stateChanged(AppSession source, Enum oldState, Enum newState) {
            this.stateChanged(oldState, newState);
        }
    }
}
