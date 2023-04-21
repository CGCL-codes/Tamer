package net.sourceforge.socketrocket.model.network.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.socketrocket.model.network.TcpServerConnectionThread;

public class TcpOutputHandler extends Thread implements IOHandlerInterface {

    Logger logger = Logger.getLogger(getClass().getName());

    private TcpServerConnectionThread tcpConnection;

    private OutputStream outputStream;

    private boolean isConnectionRunning;

    List<byte[]> requests;

    public TcpOutputHandler(TcpServerConnectionThread tcpConnection) {
        isConnectionRunning = true;
        this.tcpConnection = tcpConnection;
        requests = new ArrayList<byte[]>();
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addRequest(byte[] requestPayload) {
        if (null == requestPayload) {
            logger.log(Level.WARNING, "Request payload is null!");
            return;
        }
        try {
            logger.log(Level.FINE, new String(requestPayload, "UTF-8"));
            System.out.println("Length: " + requestPayload.length + "; " + new String(requestPayload, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "Could not encode.", e);
        }
        requests.add(requestPayload);
        synchronized (this) {
            notify();
        }
    }

    public void run() {
        while (isConnectionRunning) {
            if (null != requests) {
                synchronized (requests) {
                    for (int i = 0; i < requests.size(); i++) {
                        byte[] request = requests.get(i);
                        if (null == request) {
                            continue;
                        }
                        try {
                            outputStream.write(request);
                            requests.remove(i);
                        } catch (IOException ioe) {
                            if (isConnectionRunning) {
                                logger.log(Level.SEVERE, "IOException writing to outputStream.", ioe);
                                tcpConnection.errorOccuredCallback();
                            }
                        } catch (NullPointerException npe) {
                            if (isConnectionRunning) {
                                logger.log(Level.SEVERE, "NPException from outputStream.", npe);
                                tcpConnection.errorOccuredCallback();
                            }
                        }
                    }
                }
            }
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                    logger.log(Level.SEVERE, "Could not wait...", ie);
                }
            }
        }
    }

    @Override
    public void stopConnection() {
        isConnectionRunning = false;
        try {
            outputStream.close();
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Could not close outputStream.", ioe);
        } catch (NullPointerException npe) {
            logger.log(Level.SEVERE, "OutputStream was null.", npe);
        } finally {
            outputStream = null;
        }
    }
}
