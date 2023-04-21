package demo;

import gnu.java.zrtp.jmf.transform.TransformConnector;
import gnu.java.zrtp.jmf.transform.TransformManager;
import gnu.java.zrtp.jmf.transform.srtp.SRTPPolicy;
import java.net.*;
import javax.media.rtp.*;

public class TransmitterSRTP {

    public SimpleDataSource dataOutput = null;

    RTPManager rtpManager = null;

    SessionAddress sa = null;

    SessionAddress target = null;

    TransformConnector transConnector = null;

    public TransmitterSRTP() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getByName("localhost");
        } catch (java.net.UnknownHostException ex) {
            System.err.println("Unknown local host: " + ex.getMessage());
        }
        System.err.println("Internet address: " + ia);
        sa = new SessionAddress(ia, 5004);
        target = new SessionAddress(ia, 5002);
        dataOutput = createDataSource();
    }

    public void run() {
        rtpManager = RTPManager.newInstance();
        SRTPPolicy srtpPolicy = new SRTPPolicy(SRTPPolicy.AESCM_ENCRYPTION, 16, SRTPPolicy.HMACSHA1_AUTHENTICATION, 20, 10, 14);
        byte[] masterKey = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        byte[] masterSalt = { 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d };
        try {
            transConnector = TransformManager.createSRTPConnector(sa, masterKey, masterSalt, srtpPolicy, srtpPolicy);
            rtpManager.initialize(transConnector);
            transConnector.addTarget(target);
            SendStream sendStream = rtpManager.createSendStream(dataOutput, 0);
            sendStream.start();
        } catch (java.io.IOException ex) {
            System.err.println("Cannot start sendStream: " + ex.getMessage());
        } catch (javax.media.rtp.InvalidSessionAddressException ex) {
            System.err.println("Invalid session address: " + ex.getMessage());
        } catch (javax.media.format.UnsupportedFormatException ex) {
            System.err.println("Unsupported format: " + ex.getMessage());
        }
    }

    public void stopIt() {
        transConnector.removeTarget(target);
        rtpManager.dispose();
    }

    SimpleDataSource createDataSource() {
        SimpleDataSource sps = new SimpleDataSource();
        return sps;
    }

    public static void main(String[] args) {
        System.out.println("args len: " + args.length);
        int loopCnt = 10;
        if (args.length > 0) {
            loopCnt = 70000;
        }
        TransmitterSRTP trans = new TransmitterSRTP();
        trans.run();
        System.err.println("starting send loop");
        for (int i = 0; i < loopCnt; i++) {
            trans.dataOutput.pushData();
            if (args.length == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
        trans.stopIt();
        System.exit(0);
    }
}
