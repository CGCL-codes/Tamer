package org.ftp4che;

import org.junit.Test;

public class ImplicitTLSFtpConnectionTest extends AbstractFtpConnectionTest {

    @Test
    public void connectPassiveWithoutProxy() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(true));
        connect();
        disconnect();
    }

    @Test
    public void connectActiveWithoutProxy() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(false));
        connect();
        disconnect();
    }

    @Test
    public void connectPassiveSOCKS4() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(true));
        useSocks4();
        connect();
        disconnect();
    }

    @Test
    public void connectActiveSOCKS4() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(false));
        useSocks4();
        connect();
        disconnect();
    }

    @Test
    public void connectPassiveSOCKS5() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(true));
        useSocks5();
        connect();
        disconnect();
    }

    @Test
    public void connectActiveSOCKS5() {
        connectionProperties.setProperty("connection.type", "IMPLICIT_TLS_FTP_CONNECTION");
        connectionProperties.put("connection.passive", new Boolean(false));
        useSocks5();
        connect();
        disconnect();
    }
}
