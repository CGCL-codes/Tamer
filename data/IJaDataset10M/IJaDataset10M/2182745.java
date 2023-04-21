package jade.imtp.leap.JICP;

import jade.util.leap.Properties;
import java.net.InetAddress;
import jade.security.JADESecurityException;

public interface PDPContextManager {

    public static final String MSISDN = "msisdn";

    public static final String USERNAME = "pdp-context-username";

    public static final String PASSWORD = "pdp-context-password";

    public static final String LOCATION = "location";

    public static interface Listener {

        void handlePDPContextClosed(String msisdn);
    }

    void init(Properties p) throws Exception;

    void registerListener(Listener l);

    Properties getPDPContextInfo(InetAddress addr, String owner) throws JADESecurityException;
}
