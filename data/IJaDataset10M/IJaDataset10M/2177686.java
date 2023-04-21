package jtcpfwd.listener.knockrule;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import jtcpfwd.util.BinaryExpression;
import jtcpfwd.util.MaskedBinaryExpression;
import jtcpfwd.util.NumberExpression;

public class UDPKnockRule extends PatternBasedKnockRule implements Runnable {

    public static final String SYNTAX = "UDP#<port>[#<bytes>[#<response>]]";

    public static final Class[] getRequiredClasses() {
        return new Class[] { PatternBasedKnockRule.class, BinaryExpression.class, MaskedBinaryExpression.class, NumberExpression.class, NumberExpression.NumberRange.class };
    }

    private final int port;

    private DatagramSocket ds;

    public UDPKnockRule(String[] args) throws Exception {
        super(args, 1);
        port = Integer.parseInt(args[0]);
    }

    public void listen() throws IOException {
        ds = new DatagramSocket(port);
        new Thread(this).start();
    }

    public void tryDispose() throws IOException {
        if (ds != null) ds.close();
    }

    public void run() {
        try {
            while (true) {
                DatagramPacket dp = new DatagramPacket(new byte[4096], 4096);
                ds.receive(dp);
                byte[] packet = new byte[dp.getLength()];
                System.arraycopy(dp.getData(), 0, packet, 0, packet.length);
                if (matchExpression.matches(packet)) {
                    addAddress(dp.getAddress());
                    if (response != null) {
                        ds.send(new DatagramPacket(response, response.length, dp.getAddress(), dp.getPort()));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void trigger(InetAddress peer) throws IOException {
        byte[] packet = matchExpression.getSampleValue();
        DatagramPacket dp = new DatagramPacket(packet, packet.length, peer, port);
        DatagramSocket ds = new DatagramSocket();
        ds.send(dp);
        while (response != null) {
            dp = new DatagramPacket(new byte[response.length], response.length);
            ds.receive(dp);
            if (dp.getAddress().equals(peer) && dp.getLength() == response.length && Arrays.equals(dp.getData(), response)) break;
        }
        ds.close();
    }
}
