package at.arcsmed.mpower.communicator.soap.call.server;

import java.util.ResourceBundle;
import javax.xml.ws.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publisher which provides the call soap server webmethods on usually localhost:8088/services
 * @author msi
 */
public class CallServerSoapPublisher {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String host = null;

    private String port = null;

    private CallServerSoap server = null;

    public CallServerSoapPublisher() throws Exception {
        startServer();
    }

    public CallServerSoapPublisher(CallServerSoap server) throws Exception {
        setCallClient(server);
        startServer();
    }

    public CallServerSoap getCallClient() {
        return server;
    }

    public void setCallClient(CallServerSoap server) {
        this.server = server;
    }

    /**
	 * @return the host
	 */
    public String getHost() {
        return host;
    }

    /**
	 * @param host the host to set
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * @return the port
	 */
    public String getPort() {
        return port;
    }

    /**
	 * @param port the port to set
	 */
    public void setPort(String port) {
        this.port = port;
    }

    public void startServer() throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("at.arcsmed.mpower.communicator.soap.call.server.server");
        String host = bundle.getString("host");
        String port = bundle.getString("port");
        if (host == null || host.equals("") || port == null || port.equals("")) {
            throw new Exception("missing host or port. Please check the server.properties file!");
        }
        log.debug("starting... CallClient soap service at host: " + host + ":" + port);
        if (server == null) throw new Exception("missing server instance. Please use setCallClient(...) method to set it up!");
        Endpoint endpoint = Endpoint.publish("http://" + host + ":" + port + "/MPOWER-SIPCallService/SIPCallService", server);
        log.info("Soap call service started at: http://localhost:" + port + "/MPOWER-SIPCallService/SIPCallService");
        log.info("CallService WSDL is available at: http://localhost:" + port + "/MPOWER-SIPCallService/SIPCallService?wsdl");
    }
}
