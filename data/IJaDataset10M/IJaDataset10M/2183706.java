package net.sourceforge.buildprocess.autodeploy.webservices;

import java.io.IOException;
import java.net.ServerSocket;
import net.sourceforge.buildprocess.autodeploy.AutoDeployException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.transport.http.SimpleAxisServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The <code>AutoDeploy</code> agent WebServices embedded server
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class EmbeddedServer {

    private static final Log log = LogFactory.getLog(EmbeddedServer.class);

    private static final int MAX_POOL_SIZE = 500;

    private static final int MAX_SESSIONS_NUMBER = 500;

    private SimpleAxisServer simpleAxisServer;

    /**
    * Constructor to create a <code>EmbeddedServer</code> with the port and
    * the webservice descriptor
    * 
    * @param port
    *           the port number used by the <code>EmbeddedServer</code>
    * @param wsddFile
    *           the WebServices descriptor file to use
    */
    public EmbeddedServer(int port, String wsddFile) throws AutoDeployException {
        simpleAxisServer = new SimpleAxisServer(MAX_POOL_SIZE, MAX_SESSIONS_NUMBER);
        log.debug("Create the WebServices embedded server");
        try {
            simpleAxisServer.setServerSocket(new ServerSocket(port));
            log.debug("WebServices embedded server listen on port " + port);
        } catch (IOException ioException) {
            log.error("WebServices embedded server can't bind the network port " + port + " : " + ioException.getMessage());
            throw new AutoDeployException("WebServices embedded server can't bind the network port " + port + " : " + ioException.getMessage());
        }
        simpleAxisServer.setMyConfig(this.getEngineConfiguration(wsddFile));
    }

    /**
    * Start the embedded server
    */
    public void start() throws AutoDeployException {
        try {
            simpleAxisServer.start(true);
            log.debug("Start the WebServices embedded server in daemon mode");
        } catch (Exception e) {
            log.error("WebServices embedded server can't start : " + e.getMessage());
            throw new AutoDeployException("WebServices embedded server can't start : " + e.getMessage());
        }
    }

    /**
    * Define the embedded server configuration
    * 
    * @param wsddFile
    *           the WebService descriptor file to use
    * @return the engine configuration for the embedded server
    */
    private EngineConfiguration getEngineConfiguration(String wsddFile) throws AutoDeployException {
        return new FileProvider(getClass().getResourceAsStream(wsddFile));
    }
}
