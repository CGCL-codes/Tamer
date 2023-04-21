package edu.ucla.cs.rpc.multicast.test;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import edu.ucla.cs.rpc.multicast.Dest;
import edu.ucla.cs.rpc.multicast.Sender;
import edu.ucla.cs.rpc.multicast.handlers.ConsoleMessageHandler;
import edu.ucla.cs.rpc.multicast.network.MulticastManager;
import edu.ucla.cs.rpc.multicast.network.message.RPCMessage;
import edu.ucla.cs.rpc.multicast.sequencer.SequencerDest;
import edu.ucla.cs.rpc.multicast.sequencer.SequencerSender;
import edu.ucla.cs.rpc.multicast.sequencer.fixed.FixedSequencer;

/**
 * Testing file for {@link FixedSequencer}.
 * 
 * @author Philip Russell Chase Covello
 * 
 */
public class FixedTester {

    public static void main(String[] args) throws IOException, InterruptedException {
        MulticastManager manager = new MulticastManager();
        SocketAddress managerAddr = manager.getSocketAddress();
        FixedSequencer seq = new FixedSequencer(managerAddr);
        Thread.sleep(200);
        Set<Sender> senders = new HashSet<Sender>();
        for (int i = 0; i < 2; i++) senders.add(new SequencerSender(seq.getSocketAddress()));
        Thread.sleep(200);
        Set<Dest> dests = new HashSet<Dest>();
        for (int i = 0; i < 5; i++) {
            Dest dest = new SequencerDest(managerAddr, new ConsoleMessageHandler());
            dests.add(dest);
        }
        Thread.sleep(200);
        for (Sender s : senders) s.totalOrderBroadcast(new RPCMessage(String.class, "valueOf", new Serializable[] { 10 }, new InetSocketAddress(0)));
        seq.shutdown();
        for (Dest dest : dests) dest.shutdown();
        manager.shutdown();
    }
}
