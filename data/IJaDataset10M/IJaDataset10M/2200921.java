package net.sf.copernicus.server.m2.test;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import net.sf.copernicus.server.m2.Service;
import net.sf.copernicus.server.m2.transport.ClientConnectionListener;
import net.sf.copernicus.server.m2.transport.Endpoint;
import net.sf.copernicus.server.m2.transport.SecureClient;

public class TestSecureClientConnection extends TestClientConnection {

    public TestSecureClientConnection() {
        Logger logger = Logger.getLogger("net.sf.copernicus.server.m2");
        logger.setLevel(Level.ALL);
        logger.addAppender(new ConsoleAppender(new PatternLayout("%d{dd.MM.yyyy, HH:MM:SS} [%p] %C{1} : %m%n")));
        mService = new Service(8881, false);
    }

    protected Endpoint createClient(ClientConnectionListener listener) {
        SecureClient ret = new SecureClient("localhost", 8881, listener);
        ret.connect();
        return ret;
    }
}
