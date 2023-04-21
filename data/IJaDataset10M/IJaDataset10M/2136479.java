package net.java.sip.communicator.impl.tcpinterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.java.sip.communicator.impl.gui.GuiActivator;
import net.java.sip.communicator.impl.gui.main.call.CallDialog;
import net.java.sip.communicator.impl.gui.main.call.CallManager;
import net.java.sip.communicator.impl.gui.main.call.GuiCallParticipantRecord;
import net.java.sip.communicator.plugin.sipaccregwizz.SIPAccountRegistrationWizard;
import net.java.sip.communicator.service.configuration.ConfigurationService;
import net.java.sip.communicator.service.gui.AccountRegistrationWizard;
import net.java.sip.communicator.service.protocol.AccountID;
import net.java.sip.communicator.service.protocol.Call;
import net.java.sip.communicator.service.protocol.CallParticipant;
import net.java.sip.communicator.service.protocol.CallState;
import net.java.sip.communicator.service.protocol.OperationFailedException;
import net.java.sip.communicator.service.protocol.OperationSetBasicTelephony;
import net.java.sip.communicator.service.protocol.ProtocolProviderFactory;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;
import org.osgi.framework.ServiceReference;

public class TcpInterfaceServiceImpl implements Runnable {

    private static final int TCP_CLIENT_PORT = 7777;

    private static final String REGISTER = "REGISTER";

    private static final String REGISTERING = "REGISTERING";

    private static final String INVITE = "INVITE";

    private static final String CANCEL = "CANCEL";

    private static final String BYE = "BYE";

    private static final String DE_REGISTER = "DE-REGISTER";

    private static final String DE_REGISTER_ALL = "DE-REGISTER-ALL";

    private static final String ACCEPT = "ACCEPT";

    private static final String REJECT = "REJECT";

    private static final String QUIT = "QUIT";

    private ExecutorService clientExecutors = Executors.newFixedThreadPool(1);

    private ProtocolProviderService pps = null;

    private Call outgoingcall = null;

    private Call incomingcall = null;

    private CallDialog inComingCallDialog = null;

    private volatile boolean isDeRegisterAll = false;

    private OperationSetBasicTelephony callService = null;

    private Socket clientSocket = null;

    private DataOutputStream dataOut = null;

    private DataInputStream dataIn = null;

    private String message = null;

    private volatile boolean done = false;

    private RegisterEventHandler registerEventHandler = null;

    private CallEventHandler callEventHandler = null;

    private IncomingCallEventHandler incomingCallEventHandler = null;

    private OutgoingCallEventHandler outgoingCallEventHandler = null;

    public TcpInterfaceServiceImpl() {
        incomingCallEventHandler = new IncomingCallEventHandler(this);
        outgoingCallEventHandler = new OutgoingCallEventHandler(this);
        callEventHandler = new CallEventHandler(this);
        registerEventHandler = new RegisterEventHandler(this);
    }

    public void tcpStartService() {
        clientExecutors.execute(this);
    }

    /**
	 * register the sip communicator
	 * 
	 * @param userid
	 *            is an username to be registered at the server
	 * @param password
	 *            is a password
	 * @param keepAliveinterval
	 *            is an interval set for retransmitting the REGISTER request
	 * @param serverPort
	 *            is a server port
	 * @param clientPort
	 *            is a proxy port
	 * @throws OperationFailedException
	 */
    private void register(String userid, String password, String keepAliveinterval, String serverPort, String proxyPort) throws OperationFailedException {
        AccountRegistrationWizard accRegWizzard = (AccountRegistrationWizard) TcpInterfaceActivator.getAccountRegistrationWizardService();
        SIPAccountRegistrationWizard sipaccRegWizzard = (SIPAccountRegistrationWizard) accRegWizzard;
        sipaccRegWizzard.getRegistration().setKeepAliveInterval(keepAliveinterval);
        sipaccRegWizzard.getRegistration().setServerPort(serverPort);
        sipaccRegWizzard.getRegistration().setProxyPort(proxyPort);
        pps = accRegWizzard.signin(userid, password);
        pps.addRegistrationStateChangeListener(registerEventHandler);
    }

    /**
	 * set online
	 * 
	 * @throws OperationFailedException
	 */
    private void setOnline(String userUri) throws OperationFailedException {
        Set<Entry<Object, ProtocolProviderFactory>> set = TcpInterfaceActivator.getProtocolProviderFactories().entrySet();
        for (Entry<Object, ProtocolProviderFactory> entry : set) {
            ProtocolProviderFactory providerFactory = (ProtocolProviderFactory) entry.getValue();
            ArrayList<AccountID> accountsList = providerFactory.getRegisteredAccounts();
            AccountID accountID;
            ServiceReference serRef;
            for (int i = 0; i < accountsList.size(); i++) {
                accountID = (AccountID) accountsList.get(i);
                boolean isHidden = (accountID.getAccountProperties().get(ProtocolProviderFactory.IS_PROTOCOL_HIDDEN) != null);
                if (isHidden) continue;
                ConfigurationService configService = TcpInterfaceActivator.getConfigurationService();
                serRef = providerFactory.getProviderForAccount(accountID);
                pps = (ProtocolProviderService) TcpInterfaceActivator.bundleContext.getService(serRef);
                pps.addRegistrationStateChangeListener(registerEventHandler);
                String prefix = "net.java.sip.communicator.impl.gui.accounts";
                List<String> accounts = configService.getPropertyNamesByPrefix(prefix, true);
                Iterator<String> accountsIter = accounts.iterator();
                while (accountsIter.hasNext()) {
                    String accountRootPropName = (String) accountsIter.next();
                    String accountUID = configService.getString(accountRootPropName);
                    if ((accountUID.equals(pps.getAccountID().getAccountUniqueID())) && (userUri.equals(accountUID.substring(4, accountUID.length())))) {
                        pps.register(GuiActivator.getUIService().getDefaultSecurityAuthority(pps));
                        break;
                    }
                }
            }
        }
    }

    /**
	 * unregister the accounts
	 * 
	 */
    private void unregister() {
        try {
            ProtocolProviderFactory providerFactory = TcpInterfaceActivator.getProtocolProviderFactory(pps);
            ConfigurationService confService = TcpInterfaceActivator.getConfigurationService();
            String prefix = "net.java.sip.communicator.impl.gui.accounts";
            List<String> accounts = confService.getPropertyNamesByPrefix(prefix, true);
            for (String accountRootPropName : accounts) {
                String accountUID = confService.getString(accountRootPropName);
                if (accountUID.equals(pps.getAccountID().getAccountUniqueID())) {
                    confService.setProperty(accountRootPropName, null);
                    break;
                }
            }
            providerFactory.uninstallAccount(pps.getAccountID());
        } catch (Exception ex) {
        } finally {
            if (inComingCallDialog != null) CallManager.disposeCallDialogWait(inComingCallDialog);
            if (callService != null) {
                callService.removeCallListener(callEventHandler);
            }
            if (incomingcall != null) {
                incomingcall.removeCallChangeListener(incomingCallEventHandler);
            }
            if (outgoingcall != null) {
                Iterator<CallParticipant> participants = outgoingcall.getCallParticipants();
                while (participants.hasNext()) {
                    CallParticipant participant = (CallParticipant) participants.next();
                    participant.removeCallParticipantListener(outgoingCallEventHandler);
                }
            }
            cleanAllVaiables();
        }
    }

    /**
	 * remove the account
	 * 
	 * @throws ParseException
	 * @throws OperationFailedException
	 * 
	 */
    public void removeAccount() {
        ConfigurationService configurationService = (ConfigurationService) TcpInterfaceActivator.getConfigurationService();
        List<String> storedAccounts = configurationService.getPropertyNamesByPrefix("net.java.sip.communicator.impl.protocol.sip", true);
        for (String accountRootPropertyName : storedAccounts) {
            List<String> accountPropertyNames = configurationService.getPropertyNamesByPrefix(accountRootPropertyName, false);
            for (String propName : accountPropertyNames) {
                configurationService.setProperty(propName, null);
            }
            configurationService.setProperty(accountRootPropertyName, null);
        }
    }

    /**
	 * send INVITE
	 * 
	 * @param contact
	 *            whom to invite to be sent
	 * @throws OperationFailedException
	 * @throws ParseException
	 */
    private void invite(String contact) throws OperationFailedException, ParseException {
        OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps.getOperationSet(OperationSetBasicTelephony.class);
        outgoingcall = telephony.createCall(contact);
        outgoingcall.getCallParticipants();
        Iterator<CallParticipant> participants = outgoingcall.getCallParticipants();
        while (participants.hasNext()) {
            CallParticipant participant = (CallParticipant) participants.next();
            participant.addCallParticipantListener(outgoingCallEventHandler);
        }
    }

    /**
	 * hang-up the call
	 * 
	 * @throws OperationFailedException
	 * 
	 */
    private void hangupCall() throws OperationFailedException {
        Iterator<CallParticipant> callParticipants = null;
        if (outgoingcall != null) {
            callParticipants = outgoingcall.getCallParticipants();
        } else if (incomingcall != null) {
            callParticipants = incomingcall.getCallParticipants();
        } else return;
        while (callParticipants.hasNext()) {
            CallParticipant participant = (CallParticipant) callParticipants.next();
            OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps.getOperationSet(OperationSetBasicTelephony.class);
            telephony.hangupCallParticipant(participant);
        }
        if (inComingCallDialog != null) CallManager.disposeCallDialogWait(inComingCallDialog);
        cleanCallVariables();
    }

    /**
	 * set OffLine
	 * 
	 * @throws OperationFailedException
	 * 
	 */
    private void setOffline() {
        try {
            pps.unregister();
            if (inComingCallDialog != null) CallManager.disposeCallDialogWait(inComingCallDialog);
        } catch (OperationFailedException ex) {
            sendMessage("setOffline failed");
        } catch (Exception ex) {
            sendMessage("setOffline failed");
        } finally {
            if (inComingCallDialog != null) CallManager.disposeCallDialogWait(inComingCallDialog);
            if (callService != null) {
                callService.removeCallListener(callEventHandler);
            }
            if (incomingcall != null) {
                incomingcall.removeCallChangeListener(incomingCallEventHandler);
            }
            if (outgoingcall != null) {
                Iterator<CallParticipant> participants = outgoingcall.getCallParticipants();
                while (participants.hasNext()) {
                    CallParticipant participant = (CallParticipant) participants.next();
                    participant.removeCallParticipantListener(outgoingCallEventHandler);
                }
            }
            cleanAllVaiables();
        }
    }

    /**
	 * accept the incoming call
	 * 
	 * @throws OperationFailedException
	 * 
	 */
    private void acceptIncomingCall() throws OperationFailedException {
        setDelay(100);
        inComingCallDialog = CallManager.openCallDialog(incomingcall, GuiCallParticipantRecord.INCOMING_CALL);
        new AnswerCall(incomingcall).start();
    }

    /**
	 * reject the incoming call
	 * 
	 * @throws OperationFailedException
	 * 
	 */
    private void rejectIncomingCall() throws OperationFailedException {
        setDelay(100);
        inComingCallDialog = CallManager.openCallDialog(incomingcall, GuiCallParticipantRecord.INCOMING_CALL);
        new RejectCall(incomingcall).start();
    }

    public void run() {
        try {
            clientSocket = new Socket("localhost", TCP_CLIENT_PORT);
            System.out.println("Connected to localhost in port 7777");
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
            dataOut.flush();
            dataIn = new DataInputStream(clientSocket.getInputStream());
            while (!done) {
                System.out.println("waiting ..................................... ");
                message = (String) dataIn.readUTF();
                System.out.println("from miTester <===" + message);
                if (message.startsWith(REGISTER)) {
                    String args[] = message.split(",");
                    if (args.length == 2) {
                        setOnline(args[1]);
                    } else {
                        register(args[1], args[2], args[3], args[4], args[5]);
                    }
                } else if (message.startsWith(INVITE)) {
                    String args[] = message.split(",");
                    invite(args[1]);
                } else if (message.startsWith(ACCEPT)) {
                    acceptIncomingCall();
                } else if (message.startsWith(REJECT)) {
                    rejectIncomingCall();
                } else if (message.startsWith(CANCEL) && (outgoingcall != null)) {
                    if (outgoingcall.getCallState() != CallState.CALL_ENDED) hangupCall();
                } else if (message.startsWith(BYE)) {
                    hangupCall();
                } else if (message.startsWith(DE_REGISTER_ALL)) {
                    isDeRegisterAll = true;
                    unregister();
                } else if (message.startsWith(DE_REGISTER)) {
                    setOffline();
                } else if (message.startsWith(QUIT)) {
                    closeAll();
                    GuiActivator.getUIService().beginShutdown();
                }
            }
        } catch (OperationFailedException ex) {
            System.out.println("Operation Failed " + ex);
            sendMessage("Operation Failed");
            done = true;
        } catch (SocketException ex) {
            System.out.println("Error in TCP socket " + ex);
            done = true;
        } catch (IOException ex) {
            System.out.println("Error in TCP Communication " + ex);
            done = true;
        } catch (Exception ex) {
            System.out.println("Error while communicating through TCP channel " + ex);
            done = true;
        } finally {
            closeAll();
        }
    }

    /**
	 * Sending message to miTester
	 * 
	 * @param msg
	 *            a String message expected by miTester
	 */
    public void sendMessage(String msg) {
        try {
            if (msg.equals(REGISTERING)) {
                callService = (OperationSetBasicTelephony) TcpInterfaceActivator.getCreateCallService();
                callService.addCallListener(callEventHandler);
            } else if (msg.equals(INVITE)) {
                incomingcall.addCallChangeListener(incomingCallEventHandler);
            }
            if (dataOut != null) {
                dataOut.writeUTF(msg);
                dataOut.flush();
                System.out.println(" to miTester ===>" + msg);
            }
        } catch (IOException ex) {
        }
    }

    /**
	 * set the 'done' flag
	 * 
	 * @param done
	 */
    public void setExitFlag(boolean done) {
        this.done = done;
    }

    /**
	 * set the 'isDeRegisterAll' flag
	 * 
	 * @param isDeRegisterAll
	 */
    public void setIsRegisterAll(boolean isDeRegisterAll) {
        this.isDeRegisterAll = isDeRegisterAll;
    }

    /**
	 * get the 'isDeRegisterAll'
	 * 
	 * @return isDeRegisterAll
	 */
    public boolean getIsRegisterAll() {
        return isDeRegisterAll;
    }

    /**
	 * set the incoming call
	 * 
	 * @param incomingcall
	 */
    public void setInComingCall(Call incomingcall) {
        this.incomingcall = incomingcall;
    }

    /**
	 * set the out going call
	 * 
	 * @param outgoingcall
	 */
    public void setOutGoingCall(Call outgoingcall) {
        this.outgoingcall = outgoingcall;
    }

    /**
	 * set the incoming call dialog
	 * 
	 * @param in_callDialog
	 */
    public void setInComingCallDialog(CallDialog in_callDialog) {
        this.inComingCallDialog = in_callDialog;
    }

    /**
	 * get the inComingCallDialog
	 * 
	 * @return inComingCallDialog
	 */
    public CallDialog getInComingCallDialog() {
        return inComingCallDialog;
    }

    /**
	 * get the ProtocolProviderService return pps
	 */
    public ProtocolProviderService getProtocolProviderService() {
        return pps;
    }

    /**
	 * Answers all call participants in the given call.
	 */
    private class AnswerCall extends Thread {

        private final Call call;

        public AnswerCall(Call call) {
            this.call = call;
        }

        public void run() {
            ProtocolProviderService pps = call.getProtocolProvider();
            Iterator<CallParticipant> participants = call.getCallParticipants();
            while (participants.hasNext()) {
                CallParticipant participant = participants.next();
                OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps.getOperationSet(OperationSetBasicTelephony.class);
                try {
                    telephony.answerCallParticipant(participant);
                } catch (OperationFailedException e) {
                    System.out.println("Could not answer to : " + participant + " caused by the following exception: " + e);
                }
            }
        }
    }

    /**
	 * Hang ups all call participants in the given call.
	 */
    private class RejectCall extends Thread {

        private final Call call;

        public RejectCall(Call call) {
            this.call = call;
        }

        public void run() {
            ProtocolProviderService pps = call.getProtocolProvider();
            Iterator<CallParticipant> participants = call.getCallParticipants();
            while (participants.hasNext()) {
                CallParticipant participant = participants.next();
                OperationSetBasicTelephony telephony = (OperationSetBasicTelephony) pps.getOperationSet(OperationSetBasicTelephony.class);
                try {
                    telephony.hangupCallParticipant(participant);
                } catch (OperationFailedException e) {
                    System.out.println("Could not answer to : " + participant + " caused by the following exception: " + e);
                }
            }
        }
    }

    /**
	 * set the time delay
	 * 
	 * @param milliseconds
	 */
    private void setDelay(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
	 * clear the all call variables
	 */
    public void cleanAllVaiables() {
        inComingCallDialog = null;
        outgoingcall = null;
        incomingcall = null;
        callService = null;
        pps = null;
    }

    /**
	 * clean the incoming call variables
	 */
    public void cleanCallVariables() {
        incomingcall = null;
        inComingCallDialog = null;
        outgoingcall = null;
    }

    /**
	 * close all streams
	 */
    public void closeAll() {
        try {
            if (dataOut != null) {
                dataOut.close();
            }
            if (dataIn != null) {
                dataIn.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            dataOut = null;
            dataIn = null;
            clientSocket = null;
        } catch (Exception ex) {
        }
    }
}
