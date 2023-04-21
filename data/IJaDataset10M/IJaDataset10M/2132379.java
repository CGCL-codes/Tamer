package org.apache.harmony.rmi.transport.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UID;
import java.security.AccessController;
import org.apache.harmony.rmi.common.GetLongPropAction;
import org.apache.harmony.rmi.common.RMILog;
import org.apache.harmony.rmi.common.RMIProperties;
import org.apache.harmony.rmi.internal.nls.Messages;
import org.apache.harmony.rmi.server.ServerConnection;
import org.apache.harmony.rmi.server.ServerConnectionManager;

/**
 * Tcp extension of ServerConnection.
 *
 * @author  Mikhail A. Markov
 */
public class TcpServerConnection extends ServerConnection {

    /** Log for logging tcp connections activity. */
    protected static final RMILog tcpTransportLog = RMILog.getTcpTransportLog();

    private static int readTimeout = ((Long) AccessController.doPrivileged(new GetLongPropAction(RMIProperties.READTIMEOUT_PROP, 2 * 3600 * 1000))).intValue();

    /**
     * Constructs TcpServerConnection working through socket specified.
     *
     * @param s Socket connected to the client
     * @param mgr ConnectionManager managing this connection
     *
     * @throws IOException if an I/O error occurred during getting
     *         input/output streams from specified socket
     */
    public TcpServerConnection(Socket s, ServerConnectionManager mgr) throws IOException {
        super(s, mgr);
        s.setSoTimeout(readTimeout);
    }

    /**
     * @see ServerConnection.clientProtocolAck()
     */
    protected int clientProtocolAck() throws IOException {
        byte data;
        DataInputStream din = new DataInputStream(in);
        try {
            int header = din.readInt();
            if (header != RMI_HEADER) {
                throw new UnmarshalException(Messages.getString("rmi.82", header));
            }
            short ver = din.readShort();
            if (ver != PROTOCOL_VER) {
                throw new UnmarshalException(Messages.getString("rmi.83", ver));
            }
        } catch (IOException ioe) {
            throw new UnmarshalException(Messages.getString("rmi.84"), ioe);
        }
        if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
            tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.85", PROTOCOL_VER));
        }
        DataOutputStream dout = new DataOutputStream(out);
        if (din.readByte() == STREAM_PROTOCOL) {
            if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
                tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.90"));
            }
        } else {
            dout.writeByte(PROTOCOL_NOT_SUPPORTED);
            dout.flush();
            return -1;
        }
        dout.writeByte(PROTOCOL_ACK);
        String host = s.getInetAddress().getHostAddress();
        int port = s.getPort();
        dout.writeUTF(host);
        dout.writeInt(port);
        dout.flush();
        if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
            tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.log.136", host, port));
        }
        din.readUTF();
        din.readInt();
        return STREAM_PROTOCOL;
    }

    /**
     * @see ServerConnection.waitCallMsg()
     */
    protected int waitCallMsg() throws IOException {
        int data;
        while (true) {
            try {
                data = in.read();
            } catch (IOException ioe) {
                data = -1;
            }
            if (data == -1) {
                if (tcpTransportLog.isLoggable(RMILog.BRIEF)) {
                    tcpTransportLog.log(RMILog.BRIEF, Messages.getString("rmi.log.123", toString()));
                }
                return -1;
            }
            DataOutputStream dout = new DataOutputStream(out);
            if (data == PING_MSG) {
                if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
                    tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.log.124"));
                }
                dout.writeByte(PING_ACK);
                dout.flush();
            } else if (data == DGCACK_MSG) {
                if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
                    tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.log.125"));
                }
                dgcUnregisterUID(UID.read(new DataInputStream(in)));
            } else if (data == CALL_MSG) {
                if (tcpTransportLog.isLoggable(RMILog.VERBOSE)) {
                    tcpTransportLog.log(RMILog.VERBOSE, Messages.getString("rmi.log.126"));
                }
                return data;
            } else {
                if (tcpTransportLog.isLoggable(RMILog.BRIEF)) {
                    tcpTransportLog.log(RMILog.BRIEF, Messages.getString("rmi.log.127", data));
                }
                throw new RemoteException(Messages.getString("rmi.91", data));
            }
        }
    }

    /**
     * Returns string representation of this connection.
     *
     * @return string representation of this connection
     */
    public String toString() {
        return "TcpServerConnection: remote endpoint:" + ep;
    }
}
