package OfficeServer.office.network;

import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import OfficeServer.log_error.Log;
import OfficeServer.main.MODULE;
import OfficeServer.office.Office;
import OfficeServer.office.request.AdminRequest;
import OfficeServer.office.request.AdminRequest.TYPE;
import OfficeServer.office.request.OfficeRequest;

/**
 * @author Chris Bayruns
 * 
 */
public class NetworkConnection implements Runnable {

    private BlockingQueue<OfficeRequest> queueOut;

    private Socket mySocket;

    /** the protocol for the client */
    private ObjectOutputStream myOut = null;

    private ObjectInputStream myIn = null;

    private BufferedInputStream myBufIn = null;

    private Office myOffice = null;

    public NetworkConnection(Socket theClient) throws SocketException {
        queueOut = new LinkedBlockingQueue<OfficeRequest>();
        mySocket = theClient;
        mySocket.setSoTimeout(1000);
    }

    public void run() {
        OfficeRequest tempReqIn = null;
        OfficeRequest tempReqOut = null;
        boolean disconnected = false;
        DataInputStream din = null;
        int num;
        byte temp[];
        this.myOffice = new Office();
        try {
            myBufIn = new BufferedInputStream(mySocket.getInputStream());
            myIn = new ObjectInputStream(myBufIn);
            myOut = new ObjectOutputStream(mySocket.getOutputStream());
        } catch (IOException e) {
            Log.writeToLog(MODULE.NETWORK, Level.SEVERE, "IOException");
        }
        while (!Thread.interrupted() && !mySocket.isClosed() && !disconnected) {
            if (!queueOut.isEmpty()) {
                tempReqOut = queueOut.remove();
                try {
                    myOut.writeObject(tempReqOut);
                    myOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.writeToLog(MODULE.NETWORK, Level.SEVERE, "IOException");
                }
            } else {
                try {
                    int aval = myBufIn.available();
                    if (aval == 1) {
                    } else if (aval > 0) {
                        try {
                            tempReqIn = (OfficeRequest) myIn.readObject();
                            if (tempReqIn instanceof AdminRequest) {
                                if (((AdminRequest) tempReqIn).getType() == TYPE.CON_DISCONNECT) {
                                    disconnected = true;
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            Log.writeToLog(MODULE.NETWORK, Level.SEVERE, "ClassNotFoundException");
                        }
                        RequestQueue.enqueue(new RequestQueueWrapper(tempReqIn, myOffice, this));
                    }
                } catch (IOException e) {
                    Log.writeToLog(MODULE.NETWORK, Level.SEVERE, "IOException");
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }
        this.myOffice.userLogout();
        this.myOffice = null;
        try {
            myIn.close();
            myOut.close();
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enqueOut(OfficeRequest out) {
        try {
            queueOut.put(out);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private OfficeRequest bytesToObject(byte bytes[]) throws IOException, ClassNotFoundException {
        OfficeRequest result = null;
        result = (OfficeRequest) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        return result;
    }

    private static byte[] toBytes(Object object) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        return baos.toByteArray();
    }
}
