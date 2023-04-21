package com.headissue.jtapi.test;

import javax.telephony.*;
import com.headissue.jtapi.test.EventMonitor;

public class MakeCall {

    Terminal terminal;

    Provider provider;

    public static final void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.err.println("java ...MakeCall <Provider; Provider Args> <Terminal> <Addr> <Dest>");
            System.err.println("");
            System.err.println("<JTapi Implementation>");
            return;
        }
        JtapiPeer _peer = JtapiPeerFactory.getJtapiPeer(null);
        Provider _provider = _peer.getProvider(args[0]);
        EventMonitor em = new EventMonitor(_provider);
        em.listenOnEverything();
        Terminal _terminal = _provider.getTerminal(args[1]);
        Address _addr = _terminal.getAddresses()[0];
        Call _call = _provider.createCall();
        _call.connect(_terminal, _addr, args[3]);
    }
}
