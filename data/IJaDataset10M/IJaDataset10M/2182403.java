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
public class Authenticate {

    class clientThread extends Thread {

        Socket mysocket;

        String keyEx;

        String Message;

        String ReadMessage;

        public networkStats stats;

        long rtt;

        boolean done;

        public long getrtt() {
            return rtt;
        }

        public String getReply() {
            return ReadMessage;
        }

        public boolean getDone() {
            return done;
        }

        public clientThread(Socket s, String Ex, String Mess) throws Exception {
            mysocket = s;
            keyEx = Ex;
            rtt = 0;
            Message = Mess;
            done = false;
        }

        public InterKeyExClient makeKexCli() throws Exception {
            if (keyEx.equals("DH")) return new DHKeyExClient(256, "BlowfishKey");
            return null;
        }

        public void run() {
            CipherStreamClient csc = null;
            try {
                csc = new CipherStreamClient(mysocket.getInputStream(), mysocket.getOutputStream(), makeKexCli(), new EncryptOFB(64), new DecryptOFB(64));
                DataInputStream in = new DataInputStream(csc.getInputStream());
                DataOutputStream out = new DataOutputStream(csc.getOutputStream());
                long start = System.currentTimeMillis();
                out.writeUTF(Message);
                ReadMessage = in.readUTF();
                long end = System.currentTimeMillis();
                networkAnalyzerClient sona = new networkAnalyzerClient(in, out, 30);
                stats = sona.getStats();
                rtt = end - start;
                success = true;
            } catch (Exception e) {
                System.out.println(" while analysing... Exception occured-" + e);
                e.printStackTrace();
            }
            done = true;
            return;
        }
    }

    public SuperMessage theSuperMessage;

    public networkStats stats;

    private String MyMessage;

    private String RemoteKey;

    private boolean done;

    public boolean success;

    private long RTT;

    public networkStats getStats() {
        return stats;
    }

    public boolean isSuccess() {
        return success;
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
        if (!Ki.verifyType("AUTH_REP")) throw (new Exception("Invalid Message type -Expected AUTH_REQ -got" + Ki.getMESSAGE_NAME()));
        CipherKey k = (CipherKey) Crypto.fromString(Ki.getMessage());
        RemoteKey = k.toString();
    }

    public Authenticate(Socket s, logger theLog, String myPublicKey) {
        Crypto.initRandom();
        clientThread client = null;
        success = false;
        RTT = 0;
        done = false;
        MyMessage = null;
        RemoteKey = null;
        try {
            theLog.writelog("Authenticating" + s);
            theSuperMessage = new SuperMessage("AUTH_REQ", myPublicKey);
            client = new clientThread(s, "DH", theSuperMessage.toString());
            client.start();
            client.join();
            stats = client.stats;
            verifySuperMessage(client.getReply());
            RTT = client.getrtt();
            MyMessage = client.getReply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        done = true;
    }

    public static void main(String[] args) throws Exception {
        Crypto.initRandom();
        KeyPair myRSAKey = RSAKey.createKeys(256);
        logger g = new logger("test.log");
        CipherKey b = (CipherKey) myRSAKey.getPublic();
        System.out.println("My PUblic Key = " + b);
        Socket k = new Socket(args[0], 9050);
        Authenticate A = new Authenticate(k, g, b.toString() + "asdasdsa");
        while (!A.isDone()) ;
        System.out.println("RTT=" + A.getRTT() + "\n The Message=" + A.getMyMessage());
    }
}
