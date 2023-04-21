package org.drftpd.transfer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.drftpd.ActiveConnection;
import org.drftpd.GlobalContext;
import org.drftpd.dynamicdata.Key;
import org.drftpd.exceptions.TransferFailedException;
import org.drftpd.util.FtpRequest;
import org.drftpd.vfs.FileHandle;

/**
 * @author zubov
 * @version $Id: TransferState.java 1832 2007-11-15 02:05:23Z zubov $
 */
public class TransferState {

    public static final Key TRANSFERSTATE = new Key(TransferState.class, "transferstate", TransferState.class);

    private static final Logger logger = Logger.getLogger(TransferState.class);

    /**
     * The file to be transfered
     */
    private FileHandle _transferFile = null;

    /**
     * The handle to the transfer on the slave
     */
    private Transfer _transfer;

    /**
     * Binary or Ascii transfers
     */
    private char _type = 'A';

    /**
     * Where do we connect in Active mode?
     */
    private InetSocketAddress _portAddress;

    /**
     * Where in the file should we start sending from?
     * Used for resume of transfers
     */
    private long _resumePosition = 0;

    /**
     * This flag defines if we are the client or server in the SSL handshake
     * This only means something when _encryptedDataChannel below is set
     */
    private boolean _SSLHandshakeClientMode = false;

    /**
     * Should we send files encrypted?
     */
    private boolean _encryptedDataChannel;

    /**
     * Defines if the transfer is of type Passive
     */
    private boolean _isPasv = false;

    /**
     * This class to be used as a state holder for DataConnectionHandler
     */
    public TransferState() {
    }

    public synchronized char getDirection() {
        Transfer transfer = getTransfer();
        if (transfer == null) {
            return Transfer.TRANSFER_UNKNOWN;
        }
        synchronized (transfer) {
            return transfer.getTransferDirection();
        }
    }

    public void reset() {
        if (_transfer != null) {
            _transfer.abort("reset");
        }
        _transfer = null;
        _transferFile = null;
        _isPasv = false;
        _portAddress = null;
        _resumePosition = 0;
    }

    public boolean isPort() {
        return _portAddress != null;
    }

    public boolean isPasv() {
        return _isPasv;
    }

    public void setSSLHandshakeClientMode(boolean b) {
        _SSLHandshakeClientMode = b;
    }

    public Socket getDataSocketForLIST() throws IOException {
        Socket dataSocket;
        if (isPort()) {
            try {
                ActiveConnection ac = new ActiveConnection(_encryptedDataChannel ? GlobalContext.getGlobalContext().getSSLContext() : null, _portAddress, getSSLHandshakeClientMode());
                dataSocket = ac.connect(GlobalContext.getConfig().getCipherSuites(), 0);
            } catch (IOException ex) {
                logger.warn("Error opening data socket", ex);
                dataSocket = null;
                throw ex;
            }
        } else if (isPasv()) {
            Connection pasvCon = null;
            try {
                pasvCon = getTransfer().getTransferConnection();
                dataSocket = pasvCon.connect(GlobalContext.getConfig().getCipherSuites(), 0);
            } finally {
                if (pasvCon != null) {
                    pasvCon.abort();
                    pasvCon = null;
                }
            }
        } else {
            throw new IllegalStateException("Neither PASV nor PORT");
        }
        return dataSocket;
    }

    public boolean getSSLHandshakeClientMode() {
        return _SSLHandshakeClientMode;
    }

    public boolean getSendFilesEncrypted() {
        return _encryptedDataChannel;
    }

    public void setSendFilesEncrypted(boolean encrypted) {
        _encryptedDataChannel = encrypted;
    }

    public void setTransferFile(FileHandle file) {
        _transferFile = file;
    }

    public void setTransfer(Transfer transfer) {
        _transfer = transfer;
    }

    public boolean isTransfering() {
        return getDirection() != Transfer.TRANSFER_UNKNOWN;
    }

    private Transfer getTransfer() {
        return _transfer;
    }

    public FileHandle getTransferFile() {
        return _transferFile;
    }

    public InetSocketAddress getPortAddress() {
        return _portAddress;
    }

    public void setPortAddress(InetSocketAddress addr) {
        _portAddress = addr;
    }

    public void setPasv(boolean value) {
        _isPasv = value;
    }

    public void setResumePosition(long resumePosition) {
        _resumePosition = resumePosition;
    }

    /**
     * Set the data type. Supported types are A (ascii) and I (binary).
     *
     * @return true if success
     */
    public boolean setType(char type) {
        type = Character.toUpperCase(type);
        if ((type != 'A') && (type != 'I')) {
            return false;
        }
        _type = type;
        return true;
    }

    public char getType() {
        return _type;
    }

    public long getResumePosition() {
        return _resumePosition;
    }

    /**
     * Returns true if the transfer was aborted
     * 
     * @param reason
     * @return
     */
    public synchronized boolean abort(String reason) {
        Transfer rt = getTransfer();
        if (rt != null) {
            rt.abort(reason);
            return true;
        }
        return false;
    }

    public static char getDirectionFromRequest(FtpRequest request) {
        String cmd = request.getCommand();
        if ("RETR".equals(cmd)) {
            return Transfer.TRANSFER_SENDING_DOWNLOAD;
        }
        if ("STOR".equals(cmd) || "APPE".equals(cmd)) {
            return Transfer.TRANSFER_RECEIVING_UPLOAD;
        }
        throw new IllegalStateException("Not transfering");
    }

    public synchronized void sendFile(String path, char type, long resumePosition, String address) throws IOException {
        getTransfer().sendFile(path, type, resumePosition, address);
    }

    public synchronized void receiveFile(String path, char type, long resumePosition, String address) throws IOException {
        getTransfer().receiveFile(path, type, resumePosition, address);
    }

    public synchronized TransferStatus getTransferStatus() throws TransferFailedException {
        return getTransfer().getTransferStatus();
    }

    public synchronized long getElapsed() {
        return getTransfer().getElapsed();
    }

    public synchronized long getXferSpeed() {
        return getTransfer().getXferSpeed();
    }

    public synchronized long getTransfered() {
        return getTransfer().getTransfered();
    }
}
