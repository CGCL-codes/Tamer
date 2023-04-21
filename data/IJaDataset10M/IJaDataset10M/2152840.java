package org.eyewitness.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Logger;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.TCPPacket;
import org.eyewitness.nids.main.SnortRule;
import org.eyewitness.nids.main.SnortSignatureConverter;

public class FalsePositivesGenerator {

    SnortSignatureConverter rulesConverter = new SnortSignatureConverter();

    public FalsePositivesGenerator() {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        JpcapSender sender = null;
        try {
            sender = JpcapSender.openDevice(devices[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            for (SnortRule r : rulesConverter.getRules()) {
                if (Thread.currentThread().isInterrupted()) break;
                if (!r.protocol.equals("tcp")) continue;
                int portA, portB = 0;
                String addrSource, addrDest = "";
                try {
                    if (r.sourcePort.equals("any")) {
                        portA = 111;
                    } else {
                        try {
                            portA = Integer.parseInt(r.sourcePort);
                        } catch (NumberFormatException e) {
                            portA = 111;
                        }
                    }
                    if (r.destPort.equals("any")) {
                        portB = 111;
                    } else {
                        try {
                            portB = Integer.parseInt(r.destPort);
                        } catch (NumberFormatException e) {
                            portB = 111;
                        }
                    }
                    if (r.sourceIP.equals("any")) {
                        addrSource = "127.0.0.1";
                    } else addrSource = r.sourceIP;
                    if (r.destIP.equals("any")) {
                        addrDest = "127.0.0.1";
                    } else addrDest = r.destIP;
                    TCPPacket p = new TCPPacket(portA, portB, 56, 78, false, false, false, false, true, true, true, true, 10, 10);
                    p.setIPv4Parameter(0, false, false, false, 0, false, false, false, 0, 1010101, 100, IPPacket.IPPROTO_TCP, InetAddress.getLocalHost(), InetAddress.getLocalHost());
                    int fragLength = r.content.length() / 2;
                    byte[] f1 = new byte[fragLength];
                    byte[] f2 = new byte[fragLength];
                    for (int j = 0; j < fragLength; j++) {
                        f1[j] = (r.content).getBytes()[j];
                    }
                    for (int j = fragLength; j < fragLength * 2; j++) {
                        f2[j - fragLength] = (r.content).getBytes()[j];
                    }
                    p.data = f1;
                    p.ident = new Random().nextInt(5000);
                    p.more_frag = true;
                    p.syn = true;
                    p.fin = true;
                    p.ack = false;
                    p.offset = 0;
                    p.protocol = 6;
                    EthernetPacket ether = new EthernetPacket();
                    ether.frametype = EthernetPacket.ETHERTYPE_IP;
                    ether.src_mac = new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5 };
                    ether.dst_mac = new byte[] { (byte) 0, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10 };
                    p.datalink = ether;
                    int id = p.ident;
                    sender.sendPacket(p);
                    p = new TCPPacket(portA, portB, 56, 78, false, false, false, false, true, true, true, true, 10, 10);
                    p.setIPv4Parameter(0, false, false, false, 0, false, false, false, 0, 1010101, 100, IPPacket.IPPROTO_TCP, InetAddress.getLocalHost(), InetAddress.getLocalHost());
                    p.data = f2;
                    p.more_frag = false;
                    p.syn = true;
                    p.fin = true;
                    p.ack = false;
                    p.ident = id;
                    p.protocol = 6;
                    p.offset = (short) fragLength;
                    ether = new EthernetPacket();
                    ether.frametype = EthernetPacket.ETHERTYPE_IP;
                    ether.src_mac = new byte[] { (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5 };
                    ether.dst_mac = new byte[] { (byte) 0, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10 };
                    p.datalink = ether;
                    sender.sendPacket(p);
                    Logger.getLogger("org.eyewitness.nids").info("sent: " + ((IPPacket) p).toString());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
