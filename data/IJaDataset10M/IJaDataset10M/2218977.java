package org.fao.waicent.kids.giews.communication.bootmodule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.fao.waicent.kids.giews.communication.utility.Group;
import org.fao.waicent.kids.giews.communication.utility.MyDebug;
import org.fao.waicent.kids.giews.communication.utility.Profile;
import org.fao.waicent.kids.giews.communication.utility.message.Message;
import org.fao.waicent.kids.giews.communication.utility.message.MessageException;
import org.fao.waicent.kids.giews.communication.utility.message.MyParser;

/**
 * <p>Title: ExecuteSSLRequestBoot</p>
 *
 * <p>Description
 * </p>
 *
 * @author A. Tamburo
 * @version 1
 * @since 1
*/
public class ExecuteSSLRequestBoot extends Thread {

    private static int SOCKET_TIMEOUT = 2 * 60 * 1000;

    private List pool;

    private int id;

    private MyDebug debug;

    private BootNodeClass node;

    private SSLSocket socket;

    private MyParser parser;

    private boolean running = true;

    private int MAX_LENGTH = 200;

    /**
       * ExecuteTCPRequest
       *
       * @param  Vector pool
       * @param  int clientID
       * @param  MyDebug debug
       * @param  BootNodeClass node
       */
    public ExecuteSSLRequestBoot(List pool, BootNodeClass node, int clientID) {
        this.pool = pool;
        this.id = clientID;
        this.node = node;
        this.debug = this.node.getDebug();
        this.parser = new MyParser();
    }

    /**
       * Implements the Thread execution
       *
       */
    public void run() {
        int length;
        byte[] buffer = new byte[MAX_LENGTH];
        while (this.running) {
            synchronized (pool) {
                while (pool.isEmpty()) {
                    try {
                        if (this.running == false) {
                            break;
                        }
                        pool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (pool.size() <= 0) {
                    continue;
                }
                this.socket = (SSLSocket) pool.remove(0);
            }
            DataInputStream inFromClient = null;
            DataOutputStream outToClient = null;
            try {
                this.socket.setSoTimeout(SOCKET_TIMEOUT);
                inFromClient = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                outToClient = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                length = inFromClient.readInt();
                if (length > MAX_LENGTH) {
                    this.closeAllStream(this.socket, inFromClient, outToClient);
                    continue;
                }
                inFromClient.read(buffer, 0, length);
                Message msgRec = this.parser.parse(buffer, length);
                if (msgRec.getType() == Message.BOOTMESSAGE) {
                    SSLSession session = socket.getSession();
                    X509Certificate cert = (X509Certificate) session.getPeerCertificates()[0];
                    java.math.BigInteger id = cert.getSerialNumber();
                    byte idBytes[] = id.toByteArray();
                    String str = cert.getSubjectDN().toString();
                    int i = str.indexOf("CN=");
                    int f = str.indexOf(",");
                    String desc = str.substring(3, f - i);
                    bootMessage(outToClient, msgRec, idBytes, desc);
                }
            } catch (java.net.SocketTimeoutException e) {
                this.closeAllStream(this.socket, inFromClient, outToClient);
                e.printStackTrace();
                continue;
            } catch (Exception e) {
                this.closeAllStream(this.socket, inFromClient, outToClient);
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
       * clearThread
       *
       * @version 1, last modified by A. Tamburo, 13/09/06
       */
    public void clearThread() {
        this.running = false;
    }

    private void bootMessage(DataOutputStream out, Message msgRec, byte[] idGroup, String desc) throws Exception {
        Message msgResp = null;
        try {
            msgResp = new Message(Message.BOOTRESPONSE, msgRec.getIDMessage());
            Group gr = this.node.getGroup(Group.idByteToString(idGroup));
            if (gr != null) {
                msgResp.setResponseBoot(Message.BOOT_OK);
                msgResp.setArea(gr);
            } else if (msgRec.getCanBeSP() == Message.TRUE) {
                Profile spProfile = new Profile(msgRec.getIDNode());
                spProfile.setAddress(socket.getInetAddress());
                spProfile.setUDPPort(msgRec.getUDPPort());
                spProfile.setTCPPort(msgRec.getTCPPort());
                spProfile.setSSLPort(msgRec.getSSLPort());
                gr = new Group();
                gr.setID(idGroup);
                gr.setDesc(desc);
                gr.setSP(spProfile);
                if (this.node.registerNewGroup(gr, spProfile)) {
                    msgResp.setResponseBoot(Message.BOOT_SELECTEDASSP);
                    msgResp.setIDNode(this.node.getIDNode());
                } else {
                    msgResp.setIDNode(this.node.getIDNode());
                    msgResp.setResponseBoot(Message.BOOT_FAILED);
                }
            } else {
                msgResp.setIDNode(this.node.getIDNode());
                msgResp.setResponseBoot(Message.BOOT_FAILED);
            }
            out.writeInt(msgResp.getBytes().length);
            out.write(msgResp.getBytes());
            out.flush();
        } catch (MessageException ex) {
            throw new Exception(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }

    private void closeAllStream(SSLSocket socket, DataInputStream in, DataOutputStream out) {
        if (in != null) {
            try {
                in.close();
            } catch (java.io.IOException e) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (java.io.IOException e) {
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (java.io.IOException e) {
            }
        }
    }
}
