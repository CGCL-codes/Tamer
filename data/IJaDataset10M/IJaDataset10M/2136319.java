package it.babel.funambol.CalDAV.engine;

import java.io.InputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.Properties;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.osaf.caldav4j.CalDAV4JException;
import org.osaf.caldav4j.methods.DeleteMethod;
import org.osaf.caldav4j.methods.HttpClient;
import org.osaf.caldav4j.methods.MkCalendarMethod;
import org.osaf.caldav4j.methods.PutMethod;
import com.funambol.framework.logging.FunambolLogger;
import com.funambol.framework.logging.FunambolLoggerFactory;

/**
 * Base class for CalDAV4j connection,
 * .TODO use SSL?
 * FIXME it shouldn't be possibile to change serverhost/port:
 *  	these should be changed at the same time re-creating the hostConfig
 */
public class BaseCaldavClient extends HttpClient {

    private String serverHost = "localhost";

    private int serverPort = 8080;

    private String serverProtocol = "http";

    private String serverWebDavRoot = "/ucaldav/user/";

    private String serverUserName = "guest";

    private String serverPassword = "guest";

    protected FunambolLogger logger = null;

    /**
	 * connect with credentials
	 * @param serverHost
	 * @param serverPort
	 * @param serverProtocol
	 * @param serverWebDavRoot
	 * @param serverUserName
	 * @param serverPassword
	 */
    public BaseCaldavClient(String serverHost, String serverPort, String serverProtocol, String serverWebDavRoot, String serverUserName, String serverPassword) {
        super();
        logger = FunambolLoggerFactory.getLogger("caldav.engine.client");
        logger.info("BaseCaldavClient: server -> " + serverHost + " port -> " + serverPort + " protocol -> " + serverProtocol + " webdavroot -> " + serverWebDavRoot + " username -> " + serverUserName + " password.length -> " + ((serverPassword != null) ? serverPassword.length() : 0));
        this.serverHost = serverHost;
        this.serverPort = Integer.parseInt(serverPort);
        this.serverProtocol = serverProtocol;
        this.serverWebDavRoot = serverWebDavRoot;
        this.serverUserName = serverUserName;
        this.serverPassword = serverPassword;
        try {
            setCredentials(serverUserName, serverPassword);
        } catch (NullPointerException e1) {
            logger.error("Impossible to set credential", e1);
        }
        try {
            getHostConfiguration().setHost(this.serverHost, this.serverPort, this.serverProtocol);
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("can't create BaseCaldavClient", e);
        }
    }

    /**
	 * connect without credentials using  properties set 
	 * @param props
	 */
    public BaseCaldavClient(Properties props) {
        this(props.get("CALDAV_SERVER_HOST").toString(), props.get("CALDAV_SERVER_PORT").toString(), props.get("CALDAV_SERVER_PROTOCOL").toString(), props.get("CALDAV_SERVER_WEBDAV_ROOT").toString());
    }

    /**
	 * connect without credentials
	 * @param host
	 * @param port
	 * @param protocol
	 * @param webDavRoot
	 */
    public BaseCaldavClient(String host, String port, String protocol, String webDavRoot) {
        this(host, port, protocol, webDavRoot, null, null);
    }

    /** 
	 * @return a sample client with default values
	 */
    public BaseCaldavClient() {
        this("localhost", "8080", "http", "/ucaldav/user/", "guest", "guest");
    }

    public String getCalDavServerHost() {
        return serverHost;
    }

    public void setCalDavServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getCalDavServerPort() {
        return serverPort;
    }

    public void setCalDavServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getCalDavSeverProtocol() {
        return serverProtocol;
    }

    public void setCalDavSeverProtocol(String serverProtocol) {
        this.serverProtocol = serverProtocol;
    }

    public String getCalDavSeverWebDAVRoot() {
        return serverWebDavRoot;
    }

    public void setCalDavSeverWebDAVRoot(String serverWebDavRoot) {
        this.serverWebDavRoot = serverWebDavRoot;
    }

    public String getCalDavSeverUsername() {
        return serverUserName;
    }

    public void setCalDavSeverUsername(String serverUserName) {
        this.serverUserName = serverUserName;
    }

    public String getCalDavSeverPassword() {
        return serverPassword;
    }

    public void setCalDavSeverPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    /**
	 * set credentials
	 * @return
	 */
    protected void setCredentials(String user, String pass) {
        Credentials credentials = new UsernamePasswordCredentials(user, pass);
        getParams().setAuthenticationPreemptive(true);
        getState().setCredentials(AuthScope.ANY, credentials);
    }

    private Calendar getCalendarResource(String resourceName) {
        Calendar cal;
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        CalendarBuilder cb = new CalendarBuilder();
        try {
            cal = cb.build(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cal;
    }

    private void put(String resourceFileName, String path) {
        PutMethod put = new PutMethod();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
        put.setRequestEntity(new InputStreamRequestEntity(stream));
        put.setPath(path);
        try {
            executeMethod(getHostConfiguration(), put);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void del(String path) {
        DeleteMethod delete = new DeleteMethod();
        delete.setPath(path);
        try {
            executeMethod(getHostConfiguration(), delete);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mkdir(String path) {
        MkCalendarMethod mk = new MkCalendarMethod();
        mk.setPath(path);
        try {
            executeMethod(getHostConfiguration(), mk);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws CalDAV4JException, SocketException, URISyntaxException {
        BaseCaldavClient cli = new BaseCaldavClient();
        System.out.println(cli.toString());
    }
}
