package net.sourceforge.buildprocess.autodeploy.webservices.client;

import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ApplicationServer WebService client to launch/check configuration
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class ApplicationServerWebServiceClient extends AbstractClient {

    private static final Log log = LogFactory.getLog(ApplicationServerWebServiceClient.class);

    /**
    * Default constructor
    * 
    * @param host
    *           the hostname or IP address of the AutoDeploy Agent WebServices
    *           server
    * @param port
    *           the port number used by the AutoDeploy Agent WebServices server
    */
    public ApplicationServerWebServiceClient(String host, int port) throws ClientException {
        super("http://" + host + ":" + port + "/axis/services/AutoDeployApplicationServerService");
    }

    /**
    * Wrapper method to application server stop
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    */
    public void stop(String environmentName, String applicationServerName) throws ClientException {
        try {
            call.invoke("stop", new Object[] { environmentName, applicationServerName });
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
    }

    /**
    * Wrapper method to application server start
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    */
    public void start(String environmentName, String applicationServerName) throws ClientException {
        try {
            call.invoke("start", new Object[] { environmentName, applicationServerName });
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
    }

    /**
    * Wrapper method to application server status
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    */
    public String getStatus(String environmentName, String applicationServerName) throws ClientException {
        String status = null;
        try {
            status = (String) call.invoke("getStatus", new Object[] { environmentName, applicationServerName });
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return status;
    }

    /**
    * Wrapper method to application server connection pool check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param connectionPoolName
    *           the <code>ConnectionPoolName</code> name
    * @return true if the connection pool is ok, false else
    */
    public boolean checkConnectionPool(String environmentName, String applicationServerName, String connectionPoolName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkConnectionPool", new Object[] { environmentName, applicationServerName, connectionPoolName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }

    /**
    * Wrapper method to application server datasource check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param datasourceName
    *           the <code>datasourceName</code> name
    * @return true if the datasource is ok, false else
    */
    public boolean checkDatasource(String environmentName, String applicationServerName, String datasourceName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkDatasource", new Object[] { environmentName, applicationServerName, datasourceName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }

    /**
    * Wrapper method to application server JMS connection factory check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param jmsConnectionFactoryName
    *           the <code>JMSConnectionFactory</code> name
    * @return true if the JMS connection factory is ok, false else
    */
    public boolean checkJMSConnectionFactory(String environmentName, String applicationServerName, String jmsConnectionFactoryName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkJMSConnectionFactory", new Object[] { environmentName, applicationServerName, jmsConnectionFactoryName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }

    /**
    * Wrapper method to application server JMS server check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param jmsServerName
    *           the <code>JMSServer</code> name
    * @return true if the JMS connection factory is ok, false else
    */
    public boolean checkJMSServer(String environmentName, String applicationServerName, String jmsServerName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkJMSServer", new Object[] { environmentName, applicationServerName, jmsServerName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }

    /**
    * Wrapper method to application server name space binding check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param nameSpaceBindingName
    *           the <code>NameSpaceBinding</code> name
    * @return true if the JMS connection factory is ok, false else
    */
    public boolean checkNameSpaceBinding(String environmentName, String applicationServerName, String nameSpaceBindingName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkNameSpaceBinding", new Object[] { environmentName, applicationServerName, nameSpaceBindingName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }

    /**
    * Wrapper method to application server shared library check
    * 
    * @param environmentName
    *           the <code>Environment</code> name
    * @param applicationServerName
    *           the <code>ApplicationServer</code> name
    * @param sharedLibraryName
    *           the <code>SharedLibrary</code> name
    * @return true if the JMS connection factory is ok, false else
    */
    public boolean checkSharedLibrary(String environmentName, String applicationServerName, String sharedLibraryName) throws ClientException {
        boolean ok = true;
        try {
            ok = ((Boolean) call.invoke("checkSharedLibrary", new Object[] { environmentName, applicationServerName, sharedLibraryName })).booleanValue();
        } catch (AxisFault axisFault) {
            log.error("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
            throw new ClientException("WebServices AxisFault occurs during AutoDeploy agent environment update call : " + axisFault.getMessage());
        }
        return ok;
    }
}
