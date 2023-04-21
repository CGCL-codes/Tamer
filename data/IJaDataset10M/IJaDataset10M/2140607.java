package net.zero.smarttrace.wizards.connectConfiguration;

import net.zero.smarttrace.ConnectorConfiguration;
import net.zero.smarttrace.TraceFilter;
import net.zero.smarttrace.wizards.ModelWizard;
import net.zero.smarttrace.wizards.traceFilter.FilterClassesToTraceWizardPage;
import net.zero.smarttrace.wizards.traceFilter.FilterGeneralOptionsWizardPage;

public class ConnectionConfigurationWizard extends ModelWizard {

    public static final String MODEL_CONFIG = "config";

    public static final String MODEL_FILTER = "filter";

    private ConnectorConfiguration config = new ConnectorConfiguration();

    private TraceFilter filter = new TraceFilter();

    public ConnectionConfigurationWizard() {
        setWindowTitle("Connection Configuration");
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addModelPages() {
        addModelPage(new ConnectConnectorType());
        addModelPage(new ConnectConnectorArguments());
        addModelPage(new FilterGeneralOptionsWizardPage());
        addModelPage(new FilterClassesToTraceWizardPage());
        addModelPage(new ConnectSaveConfiguration());
    }

    public Object getModel(String name) {
        if (name.equals(MODEL_CONFIG)) return config;
        if (name.equals(MODEL_FILTER)) return filter;
        return null;
    }

    @Override
    public boolean performFinish() {
        filter.setTraceFilterName(config.getName());
        filter.saveConfiguration();
        return true;
    }
}
