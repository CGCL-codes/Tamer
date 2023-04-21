package com.mysql.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Holds functionality that falls under export-control regulations.
 * 
 * @author Mark Matthews
 * 
 * @version $Id: ExportControlled.java,v 1.1.2.1 2005/05/13 18:58:38 mmatthews
 *          Exp $
 */
public class ExportControlled {

    protected static boolean enabled() {
        return true;
    }

    /**
	 * Converts the socket being used in the given MysqlIO to an SSLSocket by
	 * performing the SSL/TLS handshake.
	 * 
	 * @param mysqlIO
	 *            the MysqlIO instance containing the socket to convert to an
	 *            SSLSocket.
	 * 
	 * @throws CommunicationsException
	 *             if the handshake fails, or if this distribution of
	 *             Connector/J doesn't contain the SSL crytpo hooks needed to
	 *             perform the handshake.
	 */
    protected static void transformSocketToSSLSocket(MysqlIO mysqlIO) throws CommunicationsException {
        javax.net.ssl.SSLSocketFactory sslFact = (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
        try {
            mysqlIO.mysqlConnection = sslFact.createSocket(mysqlIO.mysqlConnection, mysqlIO.host, mysqlIO.port, true);
            ((javax.net.ssl.SSLSocket) mysqlIO.mysqlConnection).setEnabledProtocols(new String[] { "TLSv1" });
            ((javax.net.ssl.SSLSocket) mysqlIO.mysqlConnection).startHandshake();
            if (mysqlIO.connection.getUseUnbufferedInput()) {
                mysqlIO.mysqlInput = mysqlIO.mysqlConnection.getInputStream();
            } else {
                mysqlIO.mysqlInput = new BufferedInputStream(mysqlIO.mysqlConnection.getInputStream(), 16384);
            }
            mysqlIO.mysqlOutput = new BufferedOutputStream(mysqlIO.mysqlConnection.getOutputStream(), 16384);
            mysqlIO.mysqlOutput.flush();
        } catch (IOException ioEx) {
            throw new CommunicationsException(mysqlIO.connection, mysqlIO.lastPacketSentTimeMs, ioEx);
        }
    }

    private ExportControlled() {
    }
}
