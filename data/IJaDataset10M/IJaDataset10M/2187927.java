package org.fao.waicent.kids.giews.communication.requestmodule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.fao.waicent.kids.giews.communication.utility.Group;
import org.fao.waicent.kids.giews.communication.utility.MyDebug;
import org.fao.waicent.kids.giews.communication.utility.Profile;
import org.fao.waicent.kids.giews.communication.utility.Resources;
import org.fao.waicent.kids.giews.communication.utility.message.Message;
import org.fao.waicent.kids.giews.communication.utility.message.MessageException;

/**
 * <p>Title: ReadResourceInfo</p>
 *
 * @author A. Tamburo
 * @version 1, last modified by A. Tamburo, 20/12/05
*/
public class ReadResourceInfo {

    private int TIMEOUT = 60 * 1000;

    private Socket socket = null;

    private byte[] buffer;

    private MyDebug debug;

    private Group myGr;

    private static int BANDWIDTH = 10 * 128;

    private static int MAX_LENGTH = 500;

    /**
        * Costruttore
        *
        * @version 1, last modified by A. Tamburo, 20/12/05
       */
    public ReadResourceInfo(Group myGr, MyDebug debug) {
        this.debug = debug;
        this.myGr = myGr;
        buffer = new byte[MAX_LENGTH];
    }

    /**
        * Costruttore
        *
        * @version 1, last modified by A. Tamburo, 3/3/06
       */
    public ReadResourceInfo(MyDebug debug) {
        this.debug = debug;
        buffer = new byte[MAX_LENGTH];
    }

    /**
       * read
       *
       * @return Hashtable
       * @version 1, last modified by A. Tamburo, 12/12/05
       */
    public Resources read(Resources res) {
        DataInputStream inFromServer;
        DataOutputStream outToServer;
        Profile gw = res.getGW();
        try {
            this.socket = new Socket(gw.getAddr(), gw.getTCPPort());
            this.socket.setSoTimeout(TIMEOUT);
            inFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outToServer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            return null;
        }
        try {
            Message msg = new Message(Message.GETRESOURCEINFO, Message.newID((byte) 10));
            msg.setResourceType(res.getType());
            msg.setIDResource(res.getID());
            msg.setArea(this.myGr);
            byte[] data = msg.getBytes();
            outToServer.writeInt(data.length);
            outToServer.write(data);
            outToServer.flush();
        } catch (MessageException e) {
            e.printStackTrace();
            this.closeAllStream(socket, inFromServer, outToServer);
            return null;
        } catch (IOException e) {
            this.closeAllStream(socket, inFromServer, outToServer);
            e.printStackTrace();
            return null;
        }
        return read(inFromServer, outToServer);
    }

    private Resources read(DataInputStream inFromServer, DataOutputStream outToServer) {
        Resources tmpRes = null;
        try {
            boolean res = true;
            int index = 0;
            int j;
            int total_length = inFromServer.readInt();
            if (total_length > MAX_LENGTH) return tmpRes;
            int offset = 0;
            long end, start;
            boolean read = true;
            while (read) {
                if (total_length > BANDWIDTH) {
                    start = System.currentTimeMillis();
                    inFromServer.read(buffer, offset, BANDWIDTH);
                    offset += BANDWIDTH;
                    total_length -= BANDWIDTH;
                    end = System.currentTimeMillis();
                    if (end - start < 1000) {
                        try {
                            Thread.sleep(1000 - (end - start));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    inFromServer.read(buffer, offset, total_length);
                    read = false;
                }
            }
            if (buffer[index] == Message.TRUE) {
                tmpRes = new Resources();
                index = 1;
                byte[] id = new byte[Resources.ID_LENGTH];
                for (j = 0; j < Resources.ID_LENGTH; j++) {
                    id[j] = buffer[index];
                    index++;
                }
                tmpRes.setID(id);
                byte type = buffer[index];
                index++;
                tmpRes.setType(type);
                byte[] dim = new byte[4];
                dim[3] = buffer[index];
                index++;
                dim[2] = buffer[index];
                index++;
                dim[1] = buffer[index];
                index++;
                dim[0] = buffer[index];
                index++;
                int d = Resources.sizeBytesToInt(dim);
                tmpRes.setSize(d);
                byte[] digest = new byte[Resources.DIGEST_LENGTH];
                for (j = 0; j < Resources.DIGEST_LENGTH; j++) {
                    digest[j] = buffer[index];
                    index++;
                }
                tmpRes.setDigest(digest);
                int len = buffer[index];
                index++;
                String nameProject = "";
                if (len > 0) {
                    ByteArrayOutputStream descStream = new ByteArrayOutputStream();
                    descStream.write(buffer, index, len);
                    nameProject = descStream.toString("ASCII");
                    index += len;
                }
                tmpRes.setNameProject(nameProject);
                if (type == Resources.DATASET) {
                    byte[] id_layer = new byte[4];
                    id_layer[3] = buffer[index];
                    index++;
                    id_layer[2] = buffer[index];
                    index++;
                    id_layer[1] = buffer[index];
                    index++;
                    id_layer[0] = buffer[index];
                    index++;
                    tmpRes.setIDLayer(Resources.sizeBytesToInt(id_layer));
                }
                len = buffer[index];
                index++;
                String nameLayer = "";
                if (len > 0) {
                    ByteArrayOutputStream descStream = new ByteArrayOutputStream();
                    descStream.write(buffer, index, len);
                    nameLayer = descStream.toString("ASCII");
                    index += len;
                }
                tmpRes.setNameLayer(nameLayer);
                len = buffer[index];
                index++;
                String name = "";
                if (len > 0) {
                    ByteArrayOutputStream descStream = new ByteArrayOutputStream();
                    descStream.write(buffer, index, len);
                    name = descStream.toString("ASCII");
                }
                tmpRes.setName(name);
            }
        } catch (IOException e) {
            this.closeAllStream(this.socket, inFromServer, outToServer);
        }
        return tmpRes;
    }

    private void closeAllStream(Socket socket, DataInputStream in, DataOutputStream out) {
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
