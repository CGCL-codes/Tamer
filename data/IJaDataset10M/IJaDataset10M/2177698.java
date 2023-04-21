package org.fao.waicent.kids.giews.communication.providermodule.TransferInfoModule;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import org.fao.waicent.kids.giews.communication.providermodule.ProviderClass;
import org.fao.waicent.kids.giews.communication.utility.Group;
import org.fao.waicent.kids.giews.communication.utility.MyDebug;
import org.fao.waicent.kids.giews.communication.utility.Profile;

/**
 * <p>Title: TransferGWsList </p>
 *
 * <p>Description:  Thread used by a SupeNode to manage Workstation list trasfering </p>
 * 
 *
 * @author A. Tamburo
 * @version 1, last modified by A. Tamburo, 9/06/06
 */
public class TransferGWsList {

    private static int MESSAGE_LENGTH = Profile.ID_LENGTH + 10 + 2;

    private static int BANDWIDTH = 10 * 128;

    /**
       * transfer
       *
       */
    public static void transfer(ProviderClass node, int id, MyDebug debug, DataOutputStream outToSender) throws IOException {
        Profile tmpGW;
        int tmpLen;
        byte[] tmpMsg;
        byte[] areaID;
        String desc, url;
        byte[] descBytes = null;
        byte[] urlBytes = null;
        byte[] idNode;
        byte[] addr, p;
        int port;
        int index = 0;
        long start = System.currentTimeMillis();
        long end = 0;
        int total_length = 4;
        Enumeration gwsList = node.getSPModule().getGWsList();
        try {
            outToSender.writeInt(node.getSPModule().getDimGWsList() + 1);
            outToSender.flush();
        } catch (IOException e) {
            throw e;
        }
        index = 0;
        tmpLen = MESSAGE_LENGTH;
        tmpGW = node.getProfile();
        desc = tmpGW.getDesc();
        try {
            descBytes = desc.getBytes("ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return;
        }
        tmpLen += descBytes.length;
        url = tmpGW.getURLPath();
        try {
            urlBytes = url.getBytes("ASCII");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return;
        }
        tmpLen += urlBytes.length;
        tmpMsg = new byte[tmpLen];
        idNode = tmpGW.getIDNode();
        for (int i = 0; i < Profile.ID_LENGTH; i++) {
            tmpMsg[index] = idNode[i];
            index++;
        }
        addr = tmpGW.getAddr().getAddress();
        for (int i = 0; i < addr.length; i++) {
            tmpMsg[index] = addr[i];
            index++;
        }
        port = tmpGW.getUDPPort();
        p = Group.PortIntToBytes(port);
        tmpMsg[index] = p[1];
        index++;
        tmpMsg[index] = p[0];
        index++;
        port = tmpGW.getTCPPort();
        p = Group.PortIntToBytes(port);
        tmpMsg[index] = p[1];
        index++;
        tmpMsg[index] = p[0];
        index++;
        port = tmpGW.getSSLPort();
        p = Group.PortIntToBytes(port);
        tmpMsg[index] = p[1];
        index++;
        tmpMsg[index] = p[0];
        index++;
        tmpMsg[index] = (byte) descBytes.length;
        index++;
        for (int j = 0; j < descBytes.length; j++) {
            tmpMsg[index] = descBytes[j];
            index++;
        }
        tmpMsg[index] = (byte) urlBytes.length;
        index++;
        for (int j = 0; j < urlBytes.length; j++) {
            tmpMsg[index] = urlBytes[j];
            index++;
        }
        try {
            total_length += (tmpLen + 4);
            if (total_length > BANDWIDTH) {
                int offset = (BANDWIDTH) - (total_length - tmpLen);
                outToSender.writeInt(tmpLen);
                if (offset > 0) {
                    outToSender.write(tmpMsg, 0, offset);
                } else {
                    offset = 0;
                }
                if (end - start < 1000) {
                    try {
                        Thread.sleep(1000 - (end - start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                outToSender.write(tmpMsg, offset, tmpLen - offset);
                outToSender.flush();
                start = System.currentTimeMillis();
                total_length = tmpLen - offset;
            } else {
                outToSender.writeInt(tmpLen);
                outToSender.write(tmpMsg);
                outToSender.flush();
            }
            end = System.currentTimeMillis();
        } catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            throw e;
        }
        try {
            while (gwsList.hasMoreElements()) {
                index = 0;
                tmpLen = MESSAGE_LENGTH;
                tmpGW = (Profile) gwsList.nextElement();
                desc = tmpGW.getDesc();
                descBytes = desc.getBytes("ASCII");
                tmpLen += descBytes.length;
                url = tmpGW.getURLPath();
                urlBytes = url.getBytes("ASCII");
                tmpLen += urlBytes.length;
                tmpMsg = new byte[tmpLen];
                idNode = tmpGW.getIDNode();
                for (int i = 0; i < Profile.ID_LENGTH; i++) {
                    tmpMsg[index] = idNode[i];
                    index++;
                }
                addr = tmpGW.getAddr().getAddress();
                for (int i = 0; i < addr.length; i++) {
                    tmpMsg[index] = addr[i];
                    index++;
                }
                port = tmpGW.getUDPPort();
                p = Group.PortIntToBytes(port);
                tmpMsg[index] = p[1];
                index++;
                tmpMsg[index] = p[0];
                index++;
                port = tmpGW.getTCPPort();
                p = Group.PortIntToBytes(port);
                tmpMsg[index] = p[1];
                index++;
                tmpMsg[index] = p[0];
                index++;
                port = tmpGW.getSSLPort();
                p = Group.PortIntToBytes(port);
                tmpMsg[index] = p[1];
                index++;
                tmpMsg[index] = p[0];
                index++;
                tmpMsg[index] = (byte) descBytes.length;
                index++;
                for (int j = 0; j < descBytes.length; j++) {
                    tmpMsg[index] = descBytes[j];
                    index++;
                }
                tmpMsg[index] = (byte) urlBytes.length;
                index++;
                for (int j = 0; j < urlBytes.length; j++) {
                    tmpMsg[index] = urlBytes[j];
                    index++;
                }
                total_length += (tmpLen + 4);
                if (total_length > BANDWIDTH) {
                    int offset = (BANDWIDTH) - (total_length - tmpLen);
                    outToSender.writeInt(tmpLen);
                    if (offset > 0) {
                        outToSender.write(tmpMsg, 0, offset);
                    } else {
                        offset = 0;
                    }
                    if (end - start < 1000) {
                        try {
                            Thread.sleep(1000 - (end - start));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    outToSender.write(tmpMsg, offset, tmpLen - offset);
                    outToSender.flush();
                    start = System.currentTimeMillis();
                    total_length = tmpLen - offset;
                } else {
                    outToSender.writeInt(tmpLen);
                    outToSender.write(tmpMsg);
                    outToSender.flush();
                }
                end = System.currentTimeMillis();
            }
        } catch (IOException ex) {
            throw ex;
        }
    }
}
