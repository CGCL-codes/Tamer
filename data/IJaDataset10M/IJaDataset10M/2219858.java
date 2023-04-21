package network;

import org.logi.crypto.*;
import org.logi.crypto.keys.*;
import org.logi.crypto.modes.*;
import org.logi.crypto.protocols.*;
import org.logi.crypto.io.*;
import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.*;
import utils.misc.*;
import utils.crypto.*;
import messages.tools.*;
import messages.*;
import network.*;

/**
 * @author 
 * @version 
 * Okay So this is a Very Insecure Reader!!! Means nothing but that the Messages are being send after DH tech (160bit)
 */
public class Authenticator {

    class serverThread extends Thread {

        Socket s;

        String keyEx;

        String OutMessage;

        String IncommingMessage;

        long rtt;

        public networkStats stats;

        private boolean done;

        public String getIncommingMessage() {
            return IncommingMessage;
        }

        public long getrtt() {
            return rtt;
        }

        public serverThread(Socket s, String Key, String Mes) {
            this.s = s;
            keyEx = Key;
            IncommingMessage = null;
            rtt = 0;
            OutMessage = Mes;
            done = false;
        }

        public boolean getDone() {
            return done;
        }

        public InterKeyExServer makeKexSer() {
            if (keyEx.equals("DH")) return new DHKeyExServer(256, "BlowfishKey");
            return null;
        }

        public void run() {
            CipherStreamServer css = null;
            done = false;
            try {
                css = new CipherStreamServer(s.getInputStream(), s.getOutputStream(), makeKexSer(), new EncryptOFB(64), new DecryptOFB(64));
                DataInputStream in = new DataInputStream(css.getInputStream());
                DataOutputStream out = new DataOutputStream(css.getOutputStream());
                long start = System.currentTimeMillis();
                IncommingMessage = in.readUTF();
                out.writeUTF(OutMessage);
                long end = System.currentTimeMillis();
                rtt = end - start;
                networkAnalyzerServer sona = new networkAnalyzerServer(in, out, 30);
                stats = sona.getStats();
                success = true;
            } catch (Exception e) {
                if (!(e instanceof java.io.EOFException)) {
                    System.err.println("S\tDied with an exception" + e);
                    e.printStackTrace();
                }
            }
            done = true;
        }
    }

    public SuperMessage theSuperMessage;

    public networkStats stats;

    private String MyMessage;

    private String RemoteKey;

    private boolean done;

    private boolean success;

    private long RTT;

    public boolean isSuccess() {
        return success;
    }

    public networkStats getStats() {
        return stats;
    }

    public boolean isDone() {
        return done;
    }

    public String getMyMessage() {
        return MyMessage;
    }

    public long getRTT() {
        return RTT;
    }

    public String getRemoteKey() {
        return RemoteKey;
    }

    private void verifySuperMessage(String Message) throws Exception {
        SuperMessage Ki = new SuperMessage(Message);
        if (!Ki.verifyType("AUTH_REQ")) throw (new Exception("Invalid Message type -Expected AUTH_REQ -got" + Ki.getMESSAGE_NAME()));
        CipherKey k = (CipherKey) Crypto.fromString(Ki.getMessage());
        RemoteKey = k.toString();
    }

    public Authenticator(Socket s, logger theLog, String myPublicKey) {
        Crypto.initRandom();
        serverThread server = null;
        RTT = 0;
        done = false;
        MyMessage = null;
        RemoteKey = null;
        success = false;
        try {
            theSuperMessage = new SuperMessage("AUTH_REP", myPublicKey);
            theLog.logit("Starting Authentication " + s);
            server = new serverThread(s, "DH", theSuperMessage.toString());
            server.start();
            server.join();
            stats = server.stats;
            verifySuperMessage(server.getIncommingMessage());
            MyMessage = server.getIncommingMessage();
            RTT = server.getrtt();
            done = true;
        } catch (Exception e) {
            e.printStackTrace();
            done = true;
        }
    }

    public static void main(String[] args) throws Exception {
        Crypto.initRandom();
        logger theLog = new logger("test.log");
        KeyPair myRSAKey = RSAKey.createKeys(256);
        CipherKey b = (CipherKey) myRSAKey.getPublic();
        System.out.println("My PUblic Key = " + b);
        ServerSocket s = new ServerSocket(9050);
        Socket k = s.accept();
        Authenticator A = new Authenticator(k, theLog, "asdasda" + b.toString());
        while (!A.isDone()) ;
        System.out.println("RTT=" + A.getRTT() + "\n The Message=" + A.getMyMessage());
    }
}
