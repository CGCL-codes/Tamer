package org.pixory.pxftp;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * We can get the ftp data connection using this class. It uses either PORT or
 * PASV command.
 * 
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya </a>
 */
class PXFtpDataConnection extends Object {

    private static final Log LOG = LogFactory.getLog(PXFtpDataConnection.class);

    private Socket mDataSoc = null;

    private ServerSocket mServSoc = null;

    private InetAddress mAddress = null;

    private int miPort = 0;

    private boolean mbPort = false;

    private boolean mbPasv = false;

    private InetAddress _selfAddress;

    public PXFtpDataConnection() {
        LOG.info("<init>");
    }

    /**
	 * Close data socket.
	 */
    public void closeDataSocket() {
        LOG.info("closing");
        if (mDataSoc != null) {
            try {
                mDataSoc.close();
            } catch (Exception ex) {
                LOG.warn(ex);
            }
            mDataSoc = null;
        }
        if (mServSoc != null) {
            try {
                mServSoc.close();
            } catch (Exception ex) {
                LOG.warn(ex);
            }
            mServSoc = null;
        }
    }

    /**
	 * Port command.
	 */
    public void setPortCommand(InetAddress addr, int port) {
        LOG.info("addr: " + addr + " port: " + port);
        closeDataSocket();
        mbPort = true;
        mbPasv = false;
        mAddress = addr;
        miPort = port;
    }

    /**
	 * Passive command. It returns the success flag.
	 */
    public boolean setPasvCommand() {
        LOG.info("setting");
        boolean bRet = false;
        closeDataSocket();
        try {
            mServSoc = new ServerSocket(getPassivePort(), 1, this.getSelfAddress());
            mAddress = this.getSelfAddress();
            miPort = mServSoc.getLocalPort();
            mbPort = false;
            mbPasv = true;
            bRet = true;
        } catch (Exception ex) {
            mServSoc = null;
            LOG.warn(ex);
        }
        return bRet;
    }

    /**
	 * Get client address.
	 */
    public InetAddress getInetAddress() {
        LOG.info("getInetAddress");
        return mAddress;
    }

    /**
	 * Get port number.
	 */
    public int getPort() {
        LOG.info("getPort");
        return miPort;
    }

    /**
	 * Get the data socket. In case of error returns null.
	 */
    public Socket getDataSocket() {
        LOG.info("getDataSocket");
        try {
            if (mbPort) {
                mDataSoc = new Socket(mAddress, miPort);
            } else if (mbPasv) {
                mDataSoc = mServSoc.accept();
            }
        } catch (Exception ex) {
            LOG.warn(ex);
            mDataSoc = null;
        }
        return mDataSoc;
    }

    /**
	 * Get the passive port. Later we shall use only predefined port range for
	 * passive connections.
	 */
    private int getPassivePort() {
        LOG.info("getPassivePort");
        return 0;
    }

    /**
	 * Dispose data connection
	 */
    public void dispose() {
        LOG.info("dispose");
        closeDataSocket();
    }

    private InetAddress getSelfAddress() {
        if (_selfAddress == null) {
            try {
                _selfAddress = InetAddress.getLocalHost();
            } catch (Exception anException) {
                LOG.warn(null, anException);
            }
        }
        return _selfAddress;
    }
}
