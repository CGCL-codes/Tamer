package uipp.support;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import uipp.ejb.ServersStatus;
import uipp.exceptions.SignalConnectException;
import uips.support.Consts;

/**
 * Class processing client signal requests and sending responses.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Jindrich Basek (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public class SignalClient {

    /**
     * Signal server listening for new client connections
     */
    private final ServersStatus serversStatus;

    /**
     * Client communication socket
     */
    private Socket socket;

    /**
     * Output socket stream
     */
    private BufferedOutputStream output;

    /**
     * Input socket stream
     */
    private SocketFactory sf = null;

    /**
     * Addres of UIPServer signaler
     */
    private String serverAddress;

    /**
     * Port of UIPServer signaler
     */
    private int serverPort;

    /**
     * Class constructor, set ups and test communication with server
     * 
     * @param serversStatus server status
     * @param serverAddress server address
     * @param serverPort server port
     * @throws SignalConnectException connection exception
     */
    public SignalClient(ServersStatus serversStatus, String serverAddress, int serverPort) throws SignalConnectException {
        this.serversStatus = serversStatus;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream("signalercert"), "signalerstore".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, "signalerkey".toCharArray());
            KeyStore trustStrore = KeyStore.getInstance("JKS");
            trustStrore.load(new FileInputStream("uipssigtruststore"), "signalerstore".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStrore);
            SSLContext sslc = SSLContext.getInstance("SSLv3");
            sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            sf = sslc.getSocketFactory();
            socket = sf.createSocket(this.serverAddress, this.serverPort);
            socket.close();
            Logger.getLogger(SignalClient.class.getName()).log(Level.INFO, String.format("Signal client connected to %s:%d", this.serverAddress, this.serverPort));
        } catch (UnknownHostException ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Unknows host name %s", this.serverAddress));
            try {
                socket.close();
            } catch (Exception exp) {
            }
            throw new SignalConnectException(ex.toString());
        } catch (Exception ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Exception connecting to server %s:%d", this.serverAddress, this.serverPort));
            output = null;
            try {
                socket.close();
            } catch (Exception exp) {
            }
            throw new SignalConnectException(ex.toString());
        }
    }

    /**
     * Sends text (char sequence to server)
     *
     * @param text text to send
     * @return send bytes count
     */
    public int sendTextResponse(String text) throws SignalConnectException {
        try {
            int sendBytesCount = 0;
            socket = sf.createSocket(this.serverAddress, this.serverPort);
            output = new BufferedOutputStream(socket.getOutputStream());
            if (output != null) {
                synchronized (output) {
                    try {
                        int sentBytes = 0;
                        byte sendBuffer[] = text.trim().getBytes("UTF-8");
                        sendBytesCount = sendBuffer.length;
                        while (sentBytes < sendBytesCount) {
                            int len = Math.min(Consts.SignalClientBufferSize - 1, sendBuffer.length - sentBytes);
                            output.write(sendBuffer, sentBytes, len);
                            sentBytes += len;
                            if (sentBytes >= sendBytesCount) {
                                output.write(Consts.NullByte);
                                len++;
                            }
                            output.flush();
                        }
                        Logger.getLogger(SignalClient.class.getName()).log(Level.INFO, String.format("Signal Server sent %d bytes to %s", sendBytesCount, this.serverAddress));
                    } catch (Exception ex) {
                        Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, "Send signal error", ex);
                    }
                }
            }
            socket.close();
            return sendBytesCount;
        } catch (UnknownHostException ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Unknows host name %s", this.serverAddress));
            try {
                socket.close();
            } catch (Exception exp) {
            }
            throw new SignalConnectException(ex.toString());
        } catch (Exception ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Exception connecting to server %s:%d", this.serverAddress, this.serverPort));
            output = null;
            try {
                socket.close();
            } catch (Exception exp) {
            }
            throw new SignalConnectException(ex.toString());
        }
    }

    /**
     * Try connection to singaler server
     * 
     * @throws SignalConnectException Connection was not successfull
     */
    public void tryConnection() throws SignalConnectException {
        try {
            socket = sf.createSocket(this.serverAddress, this.serverPort);
            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Unknows host name %s", this.serverAddress));
            throw new SignalConnectException(ex.toString());
        } catch (Exception ex) {
            Logger.getLogger(SignalClient.class.getName()).log(Level.SEVERE, String.format("Exception connecting to server %s:%d", this.serverAddress, this.serverPort));
            output = null;
            throw new SignalConnectException(ex.toString());
        }
    }
}
