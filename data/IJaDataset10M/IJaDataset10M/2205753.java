package org.pixory.pxftp;

import java.net.Socket;

/**
 * implementors must be thread-safe
 */
public interface PXFtpConnectionDelegate {

    public boolean acceptConnection(Socket controlSocket);

    /**
	 * return null if userName is not valid on this socket
	 */
    public PXFtpUser getUser(String userName, Socket controlSocket);
}
