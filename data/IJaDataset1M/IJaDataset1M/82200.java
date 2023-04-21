package org.openejb.admin.web.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import javax.ejb.Handle;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import org.exolab.castor.xml.ValidationException;
import org.openejb.OpenEJBException;
import org.openejb.admin.web.HttpRequest;
import org.openejb.admin.web.HttpResponse;
import org.openejb.admin.web.WebAdminBean;
import org.openejb.alt.config.Bean;
import org.openejb.alt.config.ConfigUtils;
import org.openejb.alt.config.Service;
import org.openejb.alt.config.sys.ConnectionManager;
import org.openejb.alt.config.sys.Connector;
import org.openejb.alt.config.sys.Container;
import org.openejb.alt.config.sys.Deployments;
import org.openejb.alt.config.sys.JndiProvider;
import org.openejb.alt.config.sys.Openejb;
import org.openejb.alt.config.sys.ProxyFactory;
import org.openejb.alt.config.sys.Resource;
import org.openejb.alt.config.sys.SecurityService;
import org.openejb.alt.config.sys.TransactionService;
import org.openejb.core.EnvProps;
import org.openejb.util.FileUtils;
import org.openejb.util.StringUtilities;

/** This bean allows the user to graphicly edit the OpenEJB configuration file usually located at
 *  $OPENEJB_HOME/config/openejb.conf. 
 * 
 * @see org.openejb.alt.config.sys.ConnectionManager
 * @see org.openejb.alt.config.sys.Connector
 * @see org.openejb.alt.config.sys.Container
 * @see org.openejb.alt.config.sys.Deployments
 * @see org.openejb.alt.config.sys.JndiProvider
 * @see org.openejb.alt.config.sys.Openejb
 * @see org.openejb.alt.config.sys.ProxyFactory
 * @see org.openejb.alt.config.sys.Resource
 * @see org.openejb.alt.config.sys.SecurityService
 * @see org.openejb.alt.config.sys.TransactionService
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:tim_urberg@yahoo.com">Tim Urberg</a>
 */
public class ConfigBean extends WebAdminBean {

    /** the handle file name */
    private static final String HANDLE_FILE = System.getProperty("file.separator") + "configurationHandle.obj";

    /** Called when the container creates a new instance of this bean */
    public void ejbCreate() {
        section = "Configuration";
    }

    /** Called before content is written to the browser
	 * @param request the HTTP request object
	 * @param response the HTTP response object
	 * @throws IOException if an exception is thrown
	 */
    public void preProcess(HttpRequest request, HttpResponse response) throws IOException {
    }

    /** called after content is written to the browser
	 * @param request the HTTP request object
	 * @param response the HTTP response object
	 * @throws IOException if an exception is thrown
	 */
    public void postProcess(HttpRequest request, HttpResponse response) throws IOException {
    }

    /** Write the TITLE of the HTML document.  This is the part
	 * that goes into the <HEAD><TITLE></TITLE></HEAD> tags
	 * @param body the body to write the title to
	 * @exception IOException if an exception is thrown
	 */
    public void writeHtmlTitle(PrintWriter body) throws IOException {
        body.print(HTML_TITLE);
    }

    /** Write the title of the page.  This is displayed right
	 * above the main block of content.
	 * @param body the body to write the page title to
	 * @exception IOException if an exception is thrown
	 */
    public void writePageTitle(PrintWriter body) throws IOException {
        body.print("Edit your OpenEJB Configuration");
    }

    /** Writes sub menu items for this menu item
	 * @param body the output to write to
	 * @throws IOException if an exception is thrown
	 */
    public void writeSubMenuItems(PrintWriter body) throws IOException {
    }

    /** Write the main content to the browser
	 * @param body the output to write the content to
	 * @exception IOException if an exception is thrown
	 */
    public void writeBody(PrintWriter body) throws IOException {
        Openejb openejb;
        ConfigurationDataObject configurationData;
        String configLocation = System.getProperty("openejb.configuration");
        String type = request.getQueryParameter(ConfigHTMLWriter.QUERY_PARAMETER_TYPE);
        String method = request.getQueryParameter(ConfigHTMLWriter.QUERY_PARAMETER_METHOD);
        String handleFile = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_HANDLE_FILE);
        String submitOpenejb = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_SUBMIT_OPENEJB);
        String submitService = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_SUBMIT_SERVICE);
        String containerType = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_CONTAINER_TYPE);
        if (handleFile == null) {
            configurationData = getConfigurationObject();
            handleFile = createHandle(configurationData);
            try {
                openejb = ConfigUtils.readConfig(configLocation);
            } catch (OpenEJBException e) {
                throw new IOException(e.getMessage());
            }
        } else {
            configurationData = getHandle(handleFile);
            openejb = configurationData.getOpenejb();
        }
        if (submitOpenejb != null) {
            body.println(submitOpenejb(configLocation, openejb));
            return;
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_CONNECTOR.equals(submitService)) {
            submitConnector(body, openejb, handleFile, configLocation);
        } else if (containerType != null) {
            submitContainer(body, openejb, handleFile, configLocation);
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_DEPLOYMENTS.equals(submitService)) {
            submitDeployments(body, openejb, handleFile, configLocation);
        } else if (submitService != null) {
            submitService(body, openejb, handleFile, configLocation, submitService);
        } else if (ConfigHTMLWriter.TYPE_CONNECTOR.equals(type)) {
            beginConnector(method, handleFile, body, openejb, configLocation);
        } else if (ConfigHTMLWriter.TYPE_CONTAINER.equals(type)) {
            beginContainer(method, handleFile, body, openejb, configLocation);
        } else if (ConfigHTMLWriter.TYPE_DEPLOYMENTS.equals(type)) {
            beginDeployments(method, handleFile, body, openejb, configLocation);
        } else if (type != null) {
            beginService(method, handleFile, body, openejb, configLocation, type);
        } else {
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        }
        configurationData.setOpenejb(openejb);
    }

    /**
	 * Finds the connector from the array based on the id from the form and then
	 * calls a method to create, edit or delete it
	 * 
	 * @param method create, edit or delete for the action of this connector
	 * @param handleFile the handle for the ConfigurationDataBean
	 * @param body the PrintWriter to the browser
	 * @param openejb the main configuration object
	 * @param configLocation the location of the configuration object
	 * @throws IOException when an invalid method is passed in
	 */
    private void beginConnector(String method, String handleFile, PrintWriter body, Openejb openejb, String configLocation) throws IOException {
        String connectorId = request.getFormParameter(ConfigHTMLWriter.TYPE_CONNECTOR);
        Connector[] connectors = openejb.getConnector();
        int connectorIndex = -1;
        connectorId = (connectorId == null) ? "" : connectorId;
        for (int i = 0; i < connectors.length; i++) {
            if (connectorId.equals(connectors[i].getId())) {
                connectorIndex = i;
                break;
            }
        }
        if (ConfigHTMLWriter.CREATE.equals(method)) {
            ConfigHTMLWriter.writeConnector(body, null, handleFile, -1);
        } else if (ConfigHTMLWriter.EDIT.equals(method)) {
            ConfigHTMLWriter.writeConnector(body, (connectorIndex == -1) ? null : connectors[connectorIndex], handleFile, connectorIndex);
        } else if (ConfigHTMLWriter.DELETE.equals(method)) {
            if (connectorIndex > -1) openejb.removeConnector(connectorIndex);
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        } else {
            throw new IOException("Invalid method");
        }
    }

    /**
	 * Finds the container from the array based on the id from the form and then
	 * calls a method to create, edit or delete it.  In addition, it uses a ContainerData
	 * object to store the information for the container.
	 * 
	 * @param method create, edit or delete for the action of this container
	 * @param handleFile the handle for the ConfigurationDataBean
	 * @param body the PrintWriter to the browser
	 * @param openejb the main configuration object
	 * @param configLocation the location of the configuration object
	 * @see org.openejb.admin.web.config.ContainerData
	 * @throws IOException when an invalid method is passed in
	 */
    private void beginContainer(String method, String handleFile, PrintWriter body, Openejb openejb, String configLocation) throws IOException {
        String containerId = request.getFormParameter(ConfigHTMLWriter.TYPE_CONTAINER);
        Container[] containers = openejb.getContainer();
        int containerIndex = -1;
        Properties properties = new Properties();
        containerId = (containerId == null) ? "" : containerId;
        for (int i = 0; i < containers.length; i++) {
            if (containerId.equals(containers[i].getId())) {
                containerIndex = i;
                break;
            }
        }
        if (ConfigHTMLWriter.CREATE.equals(method)) {
            ConfigHTMLWriter.writeContainer(body, new ContainerData(), handleFile);
        } else if (ConfigHTMLWriter.EDIT.equals(method)) {
            ContainerData data = new ContainerData();
            if (containerIndex > -1) {
                data.setId(StringUtilities.nullToBlankString(containers[containerIndex].getId()));
                data.setJar(StringUtilities.nullToBlankString(containers[containerIndex].getJar()));
                data.setProvider(StringUtilities.nullToBlankString(containers[containerIndex].getProvider()));
                data.setContainerType(StringUtilities.nullToBlankString(containers[containerIndex].getCtype()));
                ByteArrayInputStream in = new ByteArrayInputStream(StringUtilities.nullToBlankString(containers[containerIndex].getContent()).getBytes());
                properties.load(in);
                data.setBulkPassivate(properties.getProperty(EnvProps.IM_PASSIVATE_SIZE, ""));
                data.setGlobalTxDatabase(properties.getProperty(EnvProps.GLOBAL_TX_DATABASE, ""));
                data.setIndex(containerIndex);
                data.setLocalTxDatabase(properties.getProperty(EnvProps.LOCAL_TX_DATABASE, ""));
                data.setPassivator(properties.getProperty(EnvProps.IM_PASSIVATOR, ""));
                data.setPoolSize(properties.getProperty(EnvProps.IM_POOL_SIZE, ""));
                data.setStrictPooling(properties.getProperty(EnvProps.IM_STRICT_POOLING, "true"));
                data.setTimeOut(properties.getProperty(EnvProps.IM_TIME_OUT, ""));
                data.setEdit(true);
            }
            ConfigHTMLWriter.writeContainer(body, data, handleFile);
        } else if (ConfigHTMLWriter.DELETE.equals(method)) {
            if (containerIndex > -1) openejb.removeContainer(containerIndex);
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        } else {
            throw new IOException("Invalid method");
        }
    }

    /**
	 * Finds the current deployment from the array based on the jar or the directory 
	 * from the form and then calls a method to create, edit or delete it.  
	 * 
	 * @param method create, edit or delete for the action of this deployment
	 * @param handleFile the handle for the ConfigurationDataBean
	 * @param body the PrintWriter to the browser
	 * @param openejb the main configuration object
	 * @param configLocation the location of the configuration object
	 * @throws IOException when an invalid method is passed in
	 */
    private void beginDeployments(String method, String handleFile, PrintWriter body, Openejb openejb, String configLocation) throws IOException {
        String deploymentId = request.getFormParameter(ConfigHTMLWriter.TYPE_DEPLOYMENTS);
        Deployments[] deployments = openejb.getDeployments();
        int deploymentIndex = -1;
        deploymentId = StringUtilities.nullToBlankString(deploymentId);
        for (int i = 0; i < deployments.length; i++) {
            if (deploymentId.equals(deployments[i].getDir()) || deploymentId.equals(deployments[i].getJar())) {
                deploymentIndex = i;
                break;
            }
        }
        if (ConfigHTMLWriter.CREATE.equals(method)) {
            ConfigHTMLWriter.writeDeployments(body, null, handleFile, -1);
        } else if (ConfigHTMLWriter.EDIT.equals(method)) {
            ConfigHTMLWriter.writeDeployments(body, deployments[deploymentIndex], handleFile, deploymentIndex);
        } else if (ConfigHTMLWriter.DELETE.equals(method)) {
            if (deploymentIndex > -1) openejb.removeDeployments(deploymentIndex);
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        } else {
            throw new IOException("Invalid method");
        }
    }

    /**
	 * This is a generic begin method for services where all we care about is
	 * id, jar, provider and content.  It currently handles any services that
	 * don't have much documentation.  In future implementations this method will
	 * be refactored since all services should have a specalized, specific UI 
	 * 
	 * @param method create, edit or delete for the action of this deployment
	 * @param handleFile the handle for the ConfigurationDataBean
	 * @param body the PrintWriter to the browser
	 * @param openejb the main configuration object
	 * @param configLocation the location of the configuration object
	 * @param type the type of service being passed in (see the "type" variables
	 *               in ConfigHTMLWriter)
	 * @see org.openejb.admin.web.config.ConfigHTMLWriter
	 * @throws IOException - when an invalid method is passed in
	 */
    private void beginService(String method, String handleFile, PrintWriter body, Openejb openejb, String configLocation, String type) throws IOException {
        String serviceId = StringUtilities.nullToBlankString(request.getFormParameter(type));
        Service service = null;
        Service[] services = new Service[0];
        String submit = "";
        if (ConfigHTMLWriter.TYPE_CONNECTION_MANAGER.equals(type)) {
            service = openejb.getConnectionManager();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_CONNECTION_MANAGER;
        } else if (ConfigHTMLWriter.TYPE_JNDI_PROVIDER.equals(type)) {
            services = openejb.getJndiProvider();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_JNDI_PROVIDER;
        } else if (ConfigHTMLWriter.TYPE_PROXY_FACTORY.equals(type)) {
            service = openejb.getProxyFactory();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_PROXY_FACTORY;
        } else if (ConfigHTMLWriter.TYPE_SECURITY_SERVICE.equals(type)) {
            service = openejb.getSecurityService();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_SECURITY_SERVICE;
        } else if (ConfigHTMLWriter.TYPE_TRANSACTION_SERVICE.equals(type)) {
            service = openejb.getTransactionService();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_TRANSACTION_SERVICE;
        } else if (ConfigHTMLWriter.TYPE_RESOURCE.equals(type)) {
            services = openejb.getResource();
            submit = ConfigHTMLWriter.FORM_VALUE_SUBMIT_RESOURCE;
        }
        int serviceIndex = -1;
        for (int i = 0; i < services.length; i++) {
            if (serviceId.equals(services[i].getId())) {
                serviceIndex = i;
                service = services[i];
                break;
            }
        }
        if (ConfigHTMLWriter.CREATE.equals(method)) {
            ConfigHTMLWriter.writeService(body, null, handleFile, submit, -1);
        } else if (ConfigHTMLWriter.EDIT.equals(method)) {
            ConfigHTMLWriter.writeService(body, service, handleFile, submit, serviceIndex);
        } else if (ConfigHTMLWriter.DELETE.equals(method)) {
            if (ConfigHTMLWriter.TYPE_CONNECTION_MANAGER.equals(type)) {
                openejb.setConnectionManager(null);
            } else if (ConfigHTMLWriter.TYPE_JNDI_PROVIDER.equals(type) && serviceIndex > -1) {
                openejb.removeJndiProvider(serviceIndex);
            } else if (ConfigHTMLWriter.TYPE_PROXY_FACTORY.equals(type)) {
                openejb.setProxyFactory(null);
            } else if (ConfigHTMLWriter.TYPE_SECURITY_SERVICE.equals(type)) {
                openejb.setSecurityService(null);
            } else if (ConfigHTMLWriter.TYPE_TRANSACTION_SERVICE.equals(type)) {
                openejb.setTransactionService(null);
            } else if (ConfigHTMLWriter.TYPE_RESOURCE.equals(type) && serviceIndex > -1) {
                openejb.removeResource(serviceIndex);
            }
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        } else {
            throw new IOException("Invalid method");
        }
    }

    /**
	 * This method takes care of submitting a connector.  It grabs the form parameters
	 * and constructs the connector object
	 * 
	 * @param body the output to the browser
	 * @param openejb the openejb object
	 * @param handleFile the file of the handle for the ConfigurationData object
	 * @param configLocation the location of the configuration file
	 * @throws IOException when an exception occurs
	 */
    private void submitConnector(PrintWriter body, Openejb openejb, String handleFile, String configLocation) throws IOException {
        Connector connector;
        String id = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_ID).trim();
        String jar = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_JAR).trim();
        String provider = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_PROVIDER).trim();
        String jdbcDriver = request.getFormParameter(EnvProps.JDBC_DRIVER).trim();
        String jdbcUrl = request.getFormParameter(EnvProps.JDBC_URL).trim();
        String userName = request.getFormParameter(EnvProps.USER_NAME).trim();
        String password = request.getFormParameter(EnvProps.PASSWORD).trim();
        int index = Integer.parseInt(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_INDEX).trim());
        StringBuffer contentBuffer = new StringBuffer(125);
        StringBuffer validationError = new StringBuffer(50);
        if (index > -1) {
            connector = openejb.getConnector(index);
        } else {
            connector = new Connector();
        }
        if (!"".equals(jdbcDriver)) contentBuffer.append(EnvProps.JDBC_DRIVER).append(" ").append(jdbcDriver).append('\n');
        if (!"".equals(jdbcUrl)) contentBuffer.append(EnvProps.JDBC_URL).append(" ").append(jdbcUrl).append('\n');
        if (!"".equals(userName)) contentBuffer.append(EnvProps.USER_NAME).append(" ").append(userName).append('\n');
        if (!"".equals(password)) contentBuffer.append(EnvProps.PASSWORD).append(" ").append(password).append('\n');
        if (!"".equals(id.trim())) connector.setId(id.trim());
        if (!"".equals(jar.trim())) connector.setJar(jar);
        if (!"".equals(provider.trim())) connector.setProvider(provider);
        connector.setContent((contentBuffer.length() > 0) ? contentBuffer.toString() : null);
        try {
            connector.validate();
        } catch (ValidationException e) {
            body.print("<font color=\"red\">You must fix the following errors before proceeding:<br>\n<b>");
            body.print(e.getMessage());
            body.print("</b></font>\n<br><br>");
            ConfigHTMLWriter.writeConnector(body, connector, handleFile, index);
            return;
        }
        if (index == -1) {
            openejb.addConnector(connector);
        }
        ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
    }

    /**
	 * This method takes care of submitting a container.  It constructs a ContainerData
	 * object, puts all the info into it checks to see if we've submitted the form
	 * or just switched the container type
	 * 
	 * @see org.openejb.admin.web.config.ContainerData
	 * @param body the output to the browser
	 * @param openejb the openejb object
	 * @param handleFile the file of the handle for the ConfigurationData object
	 * @param configLocation the location of the configuration file
	 * @throws IOException when an exception occurs
	 */
    private void submitContainer(PrintWriter body, Openejb openejb, String handleFile, String configLocation) throws IOException {
        ContainerData data = new ContainerData();
        int index = Integer.parseInt(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_INDEX));
        String submit = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_SUBMIT_SERVICE);
        Container container;
        StringBuffer contentBuffer = new StringBuffer(125);
        StringBuffer errorBuffer = new StringBuffer(100);
        data.setBulkPassivate(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.IM_PASSIVATE_SIZE)).trim());
        data.setContainerType(StringUtilities.nullToBlankString(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_CONTAINER_TYPE)).trim());
        data.setGlobalTxDatabase(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.GLOBAL_TX_DATABASE)).trim());
        data.setId(StringUtilities.nullToBlankString(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_ID)).trim());
        data.setIndex(index);
        data.setJar(StringUtilities.nullToBlankString(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_JAR)).trim());
        data.setLocalTxDatabase(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.LOCAL_TX_DATABASE)).trim());
        data.setPassivator(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.IM_PASSIVATOR)).trim());
        data.setPoolSize(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.IM_POOL_SIZE)).trim());
        data.setProvider(StringUtilities.nullToBlankString(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_PROVIDER)).trim());
        data.setStrictPooling(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.IM_STRICT_POOLING)).trim());
        data.setTimeOut(StringUtilities.nullToBlankString(request.getFormParameter(EnvProps.IM_TIME_OUT)).trim());
        if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_CONTAINER.equals(submit)) {
            if (index > -1) {
                container = openejb.getContainer(index);
            } else {
                container = new Container();
            }
            container.setCtype(data.getContainerType().trim());
            if (!"".equals(data.getId().trim())) container.setId(data.getId().trim());
            if (!"".equals(data.getJar().trim())) container.setJar(data.getJar().trim());
            if ("".equals(data.getProvider().trim())) container.setProvider(data.getProvider().trim());
            if (Bean.CMP_ENTITY.equals(data.getContainerType())) {
                if (!"".equals(data.getPoolSize())) contentBuffer.append(EnvProps.IM_POOL_SIZE).append(" ").append(data.getPoolSize()).append('\n');
                if (!"".equals(data.getGlobalTxDatabase())) contentBuffer.append(EnvProps.GLOBAL_TX_DATABASE).append(" ").append(data.getGlobalTxDatabase()).append('\n'); else errorBuffer.append("Global Database File is a required field.<br>\n");
                if (!"".equals(data.getLocalTxDatabase())) contentBuffer.append(EnvProps.LOCAL_TX_DATABASE).append(" ").append(data.getLocalTxDatabase()).append('\n'); else errorBuffer.append("Local Database File is a required field.<br>\n");
            } else if (Bean.STATEFUL.equals(data.getContainerType())) {
                if (!"".equals(data.getPassivator())) contentBuffer.append(EnvProps.IM_PASSIVATOR).append(" ").append(data.getPassivator()).append('\n');
                if (!"".equals(data.getTimeOut())) contentBuffer.append(EnvProps.IM_TIME_OUT).append(" ").append(data.getTimeOut()).append('\n');
                if (!"".equals(data.getPoolSize())) contentBuffer.append(EnvProps.IM_POOL_SIZE).append(" ").append(data.getPoolSize()).append('\n');
                if (!"".equals(data.getBulkPassivate())) contentBuffer.append(EnvProps.IM_PASSIVATE_SIZE).append(" ").append(data.getBulkPassivate()).append('\n');
            } else if (Bean.STATELESS.equals(data.getContainerType())) {
                if (!"".equals(data.getTimeOut())) contentBuffer.append(EnvProps.IM_TIME_OUT).append(" ").append(data.getTimeOut()).append('\n');
                if (!"".equals(data.getPoolSize())) contentBuffer.append(EnvProps.IM_POOL_SIZE).append(" ").append(data.getPoolSize()).append('\n');
                if (!"".equals(data.getStrictPooling())) contentBuffer.append(EnvProps.IM_STRICT_POOLING).append(" ").append(data.getStrictPooling()).append('\n');
                container.setContent((contentBuffer.length() > 0) ? contentBuffer.toString() : null);
            }
            try {
                container.validate();
            } catch (ValidationException e) {
                errorBuffer.insert(0, e.getMessage() + "<br>");
            }
            if (errorBuffer.length() > 0) {
                errorBuffer.insert(0, "<font color=\"red\">You must fix the following errors: <br><b>").append("</b></font><br>");
                body.println(errorBuffer.toString());
                ConfigHTMLWriter.writeContainer(body, data, handleFile);
                return;
            }
            ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
        } else {
            ConfigHTMLWriter.writeContainer(body, data, handleFile);
        }
    }

    /**
	 * This method takes care of submitting deployments.  It simply sets the jar
	 * or directory based on which one is chosen.
	 * 
	 * @param body the output to the browser
	 * @param openejb the openejb object
	 * @param handleFile the file of the handle for the ConfigurationData object
	 * @param configLocation the location of the configuration file
	 */
    private void submitDeployments(PrintWriter body, Openejb openejb, String handleFile, String configLocation) {
        String deploymentType = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_DEPLOYMENT_TYPE);
        String deploymentText = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_DEPLOYMENT_TEXT);
        int index = Integer.parseInt(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_INDEX));
        Deployments deployments;
        if (index > -1) {
            deployments = openejb.getDeployments(index);
        } else {
            deployments = new Deployments();
            openejb.addDeployments(deployments);
        }
        if (ConfigHTMLWriter.DEPLOYMENT_TYPE_DIR.equals(deploymentType.trim())) {
            deployments.setDir(deploymentText.trim());
        } else {
            deployments.setJar(deploymentText.trim());
        }
        ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
    }

    /**
	 * This is a general, "catch all" method for submitting service.  It goes through
	 * and checks to see which type of service is being submitted and then goes from
	 * there
	 * 
	 * @param body the output to the browser
	 * @param openejb the openejb object
	 * @param handleFile the file of the handle for the ConfigurationData object
	 * @param configLocation the location of the configuration file
	 * @param submit the string to be shown on the submit button
	 * @throws IOException when an exception occurs
	 */
    private void submitService(PrintWriter body, Openejb openejb, String handleFile, String configLocation, String submit) throws IOException {
        String id = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_ID);
        String jar = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_JAR);
        String provider = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_PROVIDER);
        String content = request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_CONTENT);
        int index = Integer.parseInt(request.getFormParameter(ConfigHTMLWriter.FORM_FIELD_INDEX));
        Service service = null;
        if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_CONNECTION_MANAGER.equals(submit)) {
            if (openejb.getConnectionManager() == null) {
                service = new ConnectionManager();
                openejb.setConnectionManager((ConnectionManager) service);
            } else {
                service = openejb.getConnectionManager();
            }
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_PROXY_FACTORY.equals(submit)) {
            if (openejb.getProxyFactory() == null) {
                service = new ProxyFactory();
                openejb.setProxyFactory((ProxyFactory) service);
            } else {
                service = openejb.getProxyFactory();
            }
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_SECURITY_SERVICE.equals(submit)) {
            if (openejb.getSecurityService() == null) {
                service = new SecurityService();
                openejb.setSecurityService((SecurityService) service);
            } else {
                service = openejb.getSecurityService();
            }
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_TRANSACTION_SERVICE.equals(submit)) {
            if (openejb.getTransactionService() == null) {
                service = new TransactionService();
                openejb.setTransactionService((TransactionService) service);
            } else {
                service = openejb.getTransactionService();
            }
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_JNDI_PROVIDER.equals(submit)) {
            if (index > -1) {
                service = openejb.getJndiProvider(index);
            } else {
                service = new JndiProvider();
            }
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_RESOURCE.equals(submit)) {
            if (index > -1) {
                service = openejb.getResource(index);
            } else {
                service = new Resource();
            }
        } else {
            throw new IOException("Invalid Service type");
        }
        if (!"".equals(content.trim())) {
            service.setContent(content.trim());
        }
        if (!"".equals(id.trim())) {
            service.setId(id.trim());
        }
        if (!"".equals(jar.trim())) {
            service.setJar(jar.trim());
        }
        if (!"".equals(provider.trim())) {
            service.setProvider(provider.trim());
        }
        try {
            service.validate();
        } catch (ValidationException e) {
            body.print("<font color=\"red\">You must fix the following errors before continuing.<br>\n<b>");
            body.print(e.getMessage());
            body.println("</b></font><br><br>");
            ConfigHTMLWriter.writeService(body, service, handleFile, submit, index);
            return;
        }
        if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_JNDI_PROVIDER.equals(submit)) {
            if (index == -1) openejb.addJndiProvider((JndiProvider) service);
        } else if (ConfigHTMLWriter.FORM_VALUE_SUBMIT_RESOURCE.equals(submit)) {
            if (index == -1) openejb.addResource((Resource) service);
        }
        ConfigHTMLWriter.writeOpenejb(body, openejb, handleFile, configLocation);
    }

    /**
	 * This method submits the main Openejb object and writes it to the file
	 * 
	 * @param openejb the openejb object
	 * @param configLocation the location of the configuration file
	 * @throws IOException if the changes could not be written
	 * @return the message of where the changes were written to
	 */
    private String submitOpenejb(String configLocation, Openejb openejb) throws IOException {
        FileWriter writer = new FileWriter(configLocation);
        try {
            openejb.marshal(writer);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return "Your changes were written to: " + configLocation;
    }

    /** 
	 * gets an object reference and handle 
	 * 
	 * @param configurationData the object to create a handle from
	 * @return an absolute path of the handle file
	 * @throws IOException if the file cannot be created
	 */
    private String createHandle(ConfigurationDataObject configurationData) throws IOException {
        File myHandleFile = new File(FileUtils.createTempDirectory().getAbsolutePath() + HANDLE_FILE);
        if (!myHandleFile.exists()) {
            myHandleFile.createNewFile();
        }
        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(myHandleFile));
        objectOut.writeObject(configurationData.getHandle());
        objectOut.flush();
        objectOut.close();
        return myHandleFile.getAbsolutePath();
    }

    /** 
	 * creates a new ConfigurationDataObject 
	 * 
	 * @return a new configuration data object
	 * @throws IOException if the object cannot be created
	 */
    private ConfigurationDataObject getConfigurationObject() throws IOException {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.openejb.core.ivm.naming.InitContextFactory");
        try {
            InitialContext ctx = new InitialContext(p);
            Object obj = ctx.lookup("config/webadmin/ConfigurationData");
            ConfigurationDataHome home = (ConfigurationDataHome) PortableRemoteObject.narrow(obj, ConfigurationDataHome.class);
            return home.create();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /** 
	 * this method gets the deployer handle 
	 * 
	 * @param handleFile the handle to the object
	 * @return the configuration data object
	 * @throws IOException if the file is not found
	 */
    private ConfigurationDataObject getHandle(String handleFile) throws IOException {
        File myHandleFile = new File(handleFile);
        ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(myHandleFile));
        Handle configurationHandle;
        try {
            configurationHandle = (Handle) objectIn.readObject();
            return (ConfigurationDataObject) configurationHandle.getEJBObject();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
