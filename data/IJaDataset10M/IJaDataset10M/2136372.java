package com.headissue.jtapi.examples;

import javax.telephony.*;
import javax.telephony.callcontrol.CallControlCall;
import javax.telephony.events.*;
import net.sourceforge.gjtapi.jcc.TerminalConnectionListenerAdapter;

/**
 * Usecase for this example: An application want to signal incoming calls to a user.
 * A user usally has one or more terminals under his control. 
 * 
 * @author jw
 */
public class IncomingTerminalCall {

    Terminal terminal;

    Provider provider;

    public static final void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("java com.headissue.asterisktpi.samples.IncomingTerminalCall <Provider; Provider Args> <Terminal>");
            System.err.println("");
            System.err.println("<JTapi Implementation>");
            return;
        }
        JtapiPeer _peer = JtapiPeerFactory.getJtapiPeer(null);
        String[] sa = _peer.getServices();
        for (int i = 0; i < sa.length; i++) {
            System.out.println(sa[i]);
        }
        Provider _provider = _peer.getProvider(args[0]);
        Terminal terminal = _provider.getTerminal(args[1]);
        terminal.addCallListener(new MyListener());
        System.in.read();
    }

    static class MyListener implements TerminalConnectionListener {

        public void connectionAlerting(ConnectionEvent event) {
        }

        public void connectionConnected(ConnectionEvent event) {
        }

        public void connectionCreated(ConnectionEvent event) {
        }

        public void connectionDisconnected(ConnectionEvent event) {
        }

        public void connectionFailed(ConnectionEvent event) {
        }

        public void connectionInProgress(ConnectionEvent event) {
        }

        public void connectionUnknown(ConnectionEvent event) {
        }

        public void callActive(CallEvent event) {
        }

        public void callInvalid(CallEvent event) {
        }

        public void callEventTransmissionEnded(CallEvent event) {
        }

        public void singleCallMetaProgressStarted(MetaEvent event) {
        }

        public void singleCallMetaProgressEnded(MetaEvent event) {
        }

        public void singleCallMetaSnapshotStarted(MetaEvent event) {
        }

        public void singleCallMetaSnapshotEnded(MetaEvent event) {
        }

        public void multiCallMetaMergeStarted(MetaEvent event) {
        }

        public void multiCallMetaMergeEnded(MetaEvent event) {
        }

        public void multiCallMetaTransferStarted(MetaEvent event) {
        }

        public void multiCallMetaTransferEnded(MetaEvent event) {
        }

        public void terminalConnectionActive(TerminalConnectionEvent event) {
            System.out.println("talking");
        }

        public void terminalConnectionCreated(TerminalConnectionEvent event) {
        }

        public void terminalConnectionDropped(TerminalConnectionEvent event) {
            System.out.println("dropped");
        }

        public void terminalConnectionPassive(TerminalConnectionEvent event) {
        }

        public void terminalConnectionRinging(TerminalConnectionEvent event) {
            Call c = event.getCall();
            if (c instanceof CallControlCall) {
                CallControlCall ccc = (CallControlCall) c;
                System.out.println("Incoming call from " + ccc.getCallingAddress().getName() + " is ringing");
            }
        }

        public void terminalConnectionUnknown(TerminalConnectionEvent event) {
        }
    }
}
