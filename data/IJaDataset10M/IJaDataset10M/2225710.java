package pl.edu.icm.pnpca;

import de.fzj.pkikits.Auth;
import de.fzj.pkikits.conf.Config;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.PropertyConfigurator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;
import pl.edu.icm.pnpca.setup.StaticSetup;
import pl.edu.icm.pnpca.webapp.IdConstants;

/**
 *
 * @author Aleksander Nowinski <axnow@icm.edu.pl>
 */
public class CAServer {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CAServer.class);

    public static void main(String[] args) {
        System.out.println("Starting PNP CA");
        System.out.println("Configuring log...");
        PropertyConfigurator.configure("./conf/log4j.properties");
        log.info("Configured logging.");
        Security.addProvider(new BouncyCastleProvider());
        Config config;
        log.info("Added secutiry provider Bouncy Castle");
        try {
            log.info("Initializing auth context.");
            Auth.getAuth().getLocalKeyStore();
            log.info("Initilizing config.");
            config = Config.getConfig();
            StaticSetup.getInstance().ensureConfigured();
            log.info("Configuration finished, starting.");
            String jetty_home = ".";
            Server server = new Server();
            List<Connector> connectors = new ArrayList<Connector>();
            if (config.getPropBool(Config.SERVER_HTTP_ENABLE)) {
                int httpPort = config.getPropInt(Config.SERVER_HTTP_PORT);
                SelectChannelConnector connector = new SelectChannelConnector();
                connector.setPort(httpPort);
                connectors.add(connector);
            }
            if (config.getPropBool(Config.SERVER_HTTPS_ENABLE)) {
                int httpsPort = config.getPropInt(Config.SERVER_HTTPS_PORT);
                SslSocketConnector sslConnector = new SslSocketConnector();
                sslConnector.setPort(httpsPort);
                sslConnector.setKeystore(config.getPropString(Config.AUTH_KEYSTORE));
                sslConnector.setKeystoreType("PKCS12");
                sslConnector.setKeyPassword(config.getPropString(Config.AUTH_PASSWORD));
                sslConnector.setPassword(config.getPropString(Config.AUTH_PASSWORD));
                connectors.add(sslConnector);
            }
            if (connectors.isEmpty()) {
                log.fatal("No http nor https port properely defined, unable to start.");
                System.exit(0);
            }
            server.setConnectors((Connector[]) connectors.toArray(new Connector[0]));
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setWar(jetty_home + "/webapp/");
            server.setHandler(webapp);
            HashUserRealm realm;
            realm = new HashUserRealm(IdConstants.RA_REALM, Config.getProperty(Config.RA_PASSWD_FILE));
            server.setUserRealms(new UserRealm[] { realm });
            server.start();
            log.info("**** SERVER STARTED *****");
        } catch (Exception e) {
            System.out.println("Exception at startup: " + e.getMessage());
            e.printStackTrace();
            log.fatal("Fatal exception at initialization.", e);
            System.exit(0);
        }
    }
}
