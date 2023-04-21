package org.escapek.gui.ui.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.escapek.core.tools.StringUtil;
import org.escapek.domain.registry.RegistryNodeData;
import org.escapek.domain.security.Ticket;
import org.escapek.gui.ClientPlugin;
import org.escapek.gui.internal.nls.ServiceTesterMessages;
import org.escapek.gui.services.configurator.ConfigConstants;
import org.escapek.gui.services.configurator.exceptions.ConfigurationFailedException;
import org.escapek.gui.services.configurator.manager.IConfigurationManager;
import org.escapek.server.core.services.IRepositoryService;

/**
 * ServiceTester provides a user component for displaying service connection test outputs.
 * It can be used to run connection test and display the result into a Text widget. 
 * @author nicolasjouanin
 *
 */
public class ServiceTester {

    private Text outputText;

    private IConfigurationManager configManager;

    private Map<String, String> serviceConfig;

    private String testLogin;

    private String testPassword;

    public ServiceTester(Composite parent) {
        outputText = new Text(parent, SWT.BORDER | SWT.MULTI);
        outputText.setEditable(false);
    }

    public ServiceTester(Composite parent, Map<String, String> aConfig) {
        this(parent);
        serviceConfig = aConfig;
    }

    /**
	 * Run the tests !
	 * Depending on the config given, the run process will be dispatched among runXXX methods
	 */
    public boolean run() {
        boolean ret = true;
        SimpleDateFormat dF = new SimpleDateFormat("HH:mm:ss.SSS");
        String now;
        outputText.setText("");
        now = dF.format(new Date());
        outputText.append(NLS.bind(ServiceTesterMessages.testBegin, now));
        configManager = (IConfigurationManager) ClientPlugin.getService(IConfigurationManager.ID);
        outputText.append(NLS.bind(ServiceTesterMessages.createService, ServiceTesterMessages.OK));
        ret = runTest();
        now = dF.format(new Date());
        outputText.append(NLS.bind(ServiceTesterMessages.testEnd, now));
        return ret;
    }

    /**
	 * Run test for a service configuration
	 *
	 */
    private boolean runTest() {
        boolean ret = true;
        Ticket testTicket = null;
        String[] args = new String[] { serviceConfig.get(ConfigConstants.CONFIG_NAME), serviceConfig.get(ConfigConstants.CONTRIBUTOR_NAME) };
        outputText.append(NLS.bind(ServiceTesterMessages.header, args));
        try {
            configManager.applyConfiguration(serviceConfig);
            outputText.append(NLS.bind(ServiceTesterMessages.testConnect, ServiceTesterMessages.OK));
        } catch (Exception e) {
            outputText.append(NLS.bind(ServiceTesterMessages.testConnect, ServiceTesterMessages.FAILED));
            ret = false;
        }
        IRepositoryService registry = null;
        try {
            registry = (IRepositoryService) ClientPlugin.getService(IRepositoryService.ID);
            RegistryNodeData productName = registry.getRegistryNodeData("/info/productName");
            outputText.append(NLS.bind(ServiceTesterMessages.getProductName, (String) productName.getValue()));
        } catch (Exception e) {
            outputText.append(NLS.bind(ServiceTesterMessages.getProductName, ServiceTesterMessages.FAILED));
            outputText.append(StringUtil.getThrowableStack(e).toString());
            ret = false;
        }
        try {
            RegistryNodeData version = registry.getRegistryNodeData("/info/version", "stringValue");
            outputText.append(NLS.bind(ServiceTesterMessages.getVersion, (String) version.getValue()));
        } catch (Exception e) {
            outputText.append(NLS.bind(ServiceTesterMessages.getVersion, ServiceTesterMessages.FAILED));
            outputText.append(StringUtil.getThrowableStack(e).toString());
            ret = false;
        }
        try {
            configManager.resetConfiguration();
        } catch (ConfigurationFailedException e) {
        }
        return ret;
    }

    public Text getOutputText() {
        return outputText;
    }

    public void setOutputText(Text outputText) {
        this.outputText = outputText;
    }

    public Map<String, String> getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(Map<String, String> serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public String getTestLogin() {
        return testLogin;
    }

    public void setTestLogin(String testLogin) {
        this.testLogin = testLogin;
    }

    public String getTestPassword() {
        return testPassword;
    }

    public void setTestPassword(String testPassword) {
        this.testPassword = testPassword;
    }
}
