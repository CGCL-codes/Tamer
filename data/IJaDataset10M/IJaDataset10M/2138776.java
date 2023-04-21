package com.gite.jxta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.rendezvous.RendezvousEvent;
import net.jxta.rendezvous.RendezvousListener;

public class MyChatPeer implements RendezvousListener, DiscoveryListener, MySocketInterface, MyMulticastSocketInterface {

    private static String jxtaHome = ".jxta/MyChatPeer";

    private Object rdvlock = new Object();

    private Object exitlock = new Object();

    private boolean discovery_found = false;

    private boolean connected = false;

    private MyNetwork myNetwork;

    private PeerGroupAdvertisement found_adv;

    private PipeAdvertisement socketAdv;

    private PipeAdvertisement propagatedPipeServerAdv;

    private HashSet<String> peers;

    private HashMap<String, HashSet<Date>> waitingList;

    public MyChatPeer() {
        System.setProperty("JXTA_HOME", jxtaHome);
        peers = new HashSet<String>();
        waitingList = new HashMap<String, HashSet<Date>>();
    }

    public void rendezvousEvent(RendezvousEvent event) {
        String eventDescription;
        int eventType = event.getType();
        switch(eventType) {
            case RendezvousEvent.RDVCONNECT:
                eventDescription = "RDVCONNECT";
                connected = true;
                break;
            case RendezvousEvent.RDVRECONNECT:
                eventDescription = "RDVRECONNECT";
                connected = true;
                break;
            case RendezvousEvent.RDVDISCONNECT:
                eventDescription = "RDVDISCONNECT";
                break;
            case RendezvousEvent.RDVFAILED:
                eventDescription = "RDVFAILED";
                break;
            case RendezvousEvent.CLIENTCONNECT:
                eventDescription = "CLIENTCONNECT";
                break;
            case RendezvousEvent.CLIENTRECONNECT:
                eventDescription = "CLIENTRECONNECT";
                break;
            case RendezvousEvent.CLIENTDISCONNECT:
                eventDescription = "CLIENTDISCONNECT";
                break;
            case RendezvousEvent.CLIENTFAILED:
                eventDescription = "CLIENTFAILED";
                break;
            case RendezvousEvent.BECAMERDV:
                eventDescription = "BECAMERDV";
                connected = true;
                break;
            case RendezvousEvent.BECAMEEDGE:
                eventDescription = "BECAMEEDGE";
                break;
            default:
                eventDescription = "UNKNOWN RENDEZVOUS EVENT";
        }
        System.out.println(new Date().toString() + "  Rdv: event=" + eventDescription + " from peer = " + event.getPeer());
        synchronized (rdvlock) {
            if (connected) {
                rdvlock.notify();
            }
        }
    }

    public void waitForRdv() {
        synchronized (rdvlock) {
            while (myNetwork.getAppPeerGroup().getRendezVousService().isConnectedToRendezVous()) {
                System.out.println("Awaiting rendezvous conx...");
                try {
                    if (!myNetwork.getAppPeerGroup().getRendezVousService().isConnectedToRendezVous()) {
                        rdvlock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Connected!");
    }

    private void waitForQuit() {
        synchronized (exitlock) {
            try {
                System.out.println("Waiting for exit");
                exitlock.wait();
                System.out.println("Goodbye");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private DatagramPacket createPacket() {
        DatagramPacket dp = null;
        try {
            MySocketMessageHello helloMsg = new MySocketMessageHello();
            helloMsg.setRequestReply(false);
            helloMsg.setPeerID(this.myNetwork.getUserPeerID());
            helloMsg.setPeerName(this.myNetwork.getAppPeerGroup().getPeerName());
            helloMsg.setTime();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(helloMsg);
            out.close();
            byte buf[] = bos.toByteArray();
            dp = new DatagramPacket(buf, buf.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dp;
    }

    private void doSomething() {
        new Thread("My Socket Server") {

            public void run() {
                MySocketServer ss = new MySocketServer(MyChatPeer.this, MyChatPeer.this.myNetwork.getAppPeerGroup(), MyChatPeer.this.myNetwork.getUserPeerID());
                MyChatPeer.this.socketAdv = ss.getSocketAdvertisement();
                while (true) {
                    synchronized (this) {
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ss.waitMessage();
                }
            }
        }.start();
        new Thread("My Multicast Socket Server") {

            public void run() {
                MyMulticastSocketServer ms = new MyMulticastSocketServer(MyChatPeer.this, MyChatPeer.this.myNetwork.getAppPeerGroup(), "GITe Multicast Socket");
                MyChatPeer.this.propagatedPipeServerAdv = ms.getPipeAdvertisement();
                while (true) {
                    synchronized (this) {
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ms.waitPacket();
                }
            }
        }.start();
        while (socketAdv == null || propagatedPipeServerAdv == null) {
        }
        new Thread("My Multicast Socket Client") {

            public void run() {
                MyMulticastSocketClient mc = new MyMulticastSocketClient(MyChatPeer.this.myNetwork.getAppPeerGroup(), propagatedPipeServerAdv);
                int sleepy = 80000;
                while (true) {
                    DatagramPacket packet = createPacket();
                    mc.sendToPeers(packet);
                    try {
                        sleep(sleepy);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        new Thread("My Socket Client") {

            public void run() {
                int sleepy = 40000;
                while (true) {
                    if (!peers.isEmpty()) {
                        MySocketMessageTestMessage tstMsg = new MySocketMessageTestMessage();
                        tstMsg.setPeerName(MyChatPeer.this.myNetwork.getAppPeerGroup().getPeerName());
                        tstMsg.setPeerID(MyChatPeer.this.myNetwork.getUserPeerID());
                        System.out.println("LIST OF PEER IDS:");
                        Iterator<String> it = peers.iterator();
                        while (it.hasNext()) {
                            String peerid = it.next();
                            System.out.println(peerid);
                            PipeAdvertisement pipeAdv = createAdvertisement(peerid);
                            MySocketClient sc = new MySocketClient(MyChatPeer.this.myNetwork.getAppPeerGroup(), pipeAdv);
                            System.out.println("		[THIS = " + tstMsg.getPeerName() + "] Sending message TEST to :");
                            System.out.println("		Peer ID = " + peerid);
                            System.out.println("		ReplyRequest = true");
                            tstMsg.setRequestReply(true);
                            tstMsg.setPeerID(MyChatPeer.this.myNetwork.getUserPeerID());
                            tstMsg.setPeerName(MyChatPeer.this.myNetwork.getAppPeerGroup().getPeerName());
                            Date now = new Date();
                            tstMsg.setTime(now);
                            HashSet<Date> hash = waitingList.get(tstMsg.getPeerID());
                            if (hash == null) hash = new HashSet<Date>();
                            hash.add(now);
                            waitingList.put(peerid, hash);
                            System.out.println("		Time sent = " + tstMsg.getTime());
                            System.out.println();
                            sc.sendSocketMessage(tstMsg);
                        }
                    }
                    try {
                        sleep(sleepy);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private PipeAdvertisement createAdvertisement(String digestString) {
        PipeAdvertisement pipeAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        try {
            byte[] bid = MessageDigest.getInstance("MD5").digest(digestString.getBytes("ISO-8859-1"));
            PipeID pipeID = IDFactory.newPipeID(this.myNetwork.getAppPeerGroup().getPeerGroupID(), bid);
            pipeAdv.setPipeID(pipeID);
            pipeAdv.setType(PipeService.UnicastType);
            pipeAdv.setName("Socket pipe");
            pipeAdv.setDescription("verbose description");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return pipeAdv;
    }

    public void receiveSocketMessage(MySocketMessageInterface msg) {
        if (!msg.getPeerID().equals(MyChatPeer.this.myNetwork.getUserPeerID())) {
            System.out.println("[RECEIVED UNICAST] Socket message: " + msg.getType());
            if (msg.getType() == "TEST") {
                MySocketMessageTestMessage tstMsg = (MySocketMessageTestMessage) msg;
                System.out.println("Type = TEST");
                System.out.println("From Peer = " + tstMsg.getPeerName());
                System.out.println("Message = " + tstMsg.getMessage());
                System.out.println("Time sent = " + tstMsg.getTime());
                System.out.println("Time received = " + new Date());
            }
            if (msg.getType() == "HELLO") {
                MySocketMessageHello msgHello = (MySocketMessageHello) msg;
                System.out.println("Type = HELLO");
                System.out.println("From Peer = " + msgHello.getPeerName());
                System.out.println("Time sent = " + msgHello.getTime());
                System.out.println("Time received = " + new Date());
                if (waitingList.containsKey(msgHello.getPeerID())) {
                    HashSet<Date> dateSet = waitingList.get(msgHello.getPeerID());
                    if (!dateSet.isEmpty()) {
                        if (dateSet.contains(msgHello.getTime())) {
                            System.out.println("This was for our reply request...");
                            System.out.println("Deleting request from the waiting list. Done!");
                            dateSet.remove(msgHello.getTime());
                            if (dateSet.isEmpty()) waitingList.remove(msgHello.getPeerID());
                        }
                    }
                }
            }
            System.out.println();
            if (msg.isRequestReply()) {
                System.out.print("Reply was requested! Sending.... ");
                SendReplyMessageToMessage(createAdvertisement(msg.getPeerID()), msg);
                System.out.println("Sent!");
            }
        }
    }

    public void pipeEvent(DatagramPacket packet) {
        MySocketMessageHello helloMsg = null;
        try {
            byte[] buffer = packet.getData();
            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInput in = new ObjectInputStream(bis);
            helloMsg = (MySocketMessageHello) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!helloMsg.getPeerID().equals(MyChatPeer.this.myNetwork.getUserPeerID())) {
            System.out.println("[RECEIVED MULTICAST] Message HELLO received:");
            System.out.println("From Peer = " + helloMsg.getPeerName());
            System.out.println("Time sent = " + helloMsg.getTime());
            System.out.println("Time received = " + new Date());
            System.out.println();
            if (helloMsg != null) {
                if (!helloMsg.getPeerID().equals(this.myNetwork.getUserPeerID())) {
                    peers.add(helloMsg.getPeerID());
                    System.out.println("Sending reply...");
                    SendReplyMessageToMessage(createAdvertisement(helloMsg.getPeerID()), null);
                    System.out.println("Reply sent!");
                }
            }
        }
    }

    protected void SendReplyMessageToMessage(PipeAdvertisement socketAdv, MySocketMessageInterface msg) {
        MySocketMessageHello helloMsg = new MySocketMessageHello();
        helloMsg.setRequestReply(false);
        helloMsg.setPeerID(this.myNetwork.getUserPeerID());
        helloMsg.setPeerName(MyChatPeer.this.myNetwork.getAppPeerGroup().getPeerName());
        if (msg != null) {
            helloMsg.setTime(msg.getTime());
        } else helloMsg.setTime(new Date());
        MySocketClient sc = new MySocketClient(MyChatPeer.this.myNetwork.getAppPeerGroup(), socketAdv);
        sc.sendSocketMessage(helloMsg);
    }

    public static void main(String[] args) throws Throwable {
        MyChatPeer peer = new MyChatPeer();
        peer.myNetwork = new MyNetwork();
        peer.myNetwork.configureJXTA(jxtaHome, null);
        try {
            peer.myNetwork.startJXTA(jxtaHome);
            peer.myNetwork.getNetPeerGroup().getRendezVousService().addListener(peer);
            peer.myNetwork.getNetPeerGroup().getDiscoveryService().addDiscoveryListener(peer);
            System.out.println("Finding application peer group...");
            while (!peer.discovery_found) {
                peer.myNetwork.getNetPeerGroup().getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.GROUP, "Name", "GITe APPLICATION PEER GROUP", 5);
                synchronized (peer) {
                    peer.wait(1000);
                }
            }
            PeerGroup pg = peer.myNetwork.getNetPeerGroup().newGroup(peer.found_adv);
            peer.myNetwork.joinGroup(pg);
            peer.myNetwork.setAppPeerGroup(pg);
            peer.waitForRdv();
            peer.doSomething();
            peer.waitForQuit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Initialization Error. Exiting.");
            System.exit(1);
        }
    }

    public void discoveryEvent(DiscoveryEvent ev) {
        if (!discovery_found) {
            DiscoveryResponseMsg res = ev.getResponse();
            String name = "unknown";
            PeerAdvertisement peerAdv = res.getPeerAdvertisement();
            if (peerAdv != null) {
                name = peerAdv.getName();
            }
            System.out.println("Got a discovery response [" + res.getResponseCount() + " elements] from peer :" + name);
            PeerGroupAdvertisement adv = null;
            Enumeration en = res.getAdvertisements();
            if (en != null) {
                while (en.hasMoreElements()) {
                    adv = (PeerGroupAdvertisement) en.nextElement();
                    System.out.println("Peer Group = " + adv.getName());
                }
                found_adv = adv;
                discovery_found = true;
            }
        }
    }
}
