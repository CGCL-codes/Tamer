package net.zero.smarttrace.ui.wizards.jdiconnection;

import net.zero.smarttrace.core.ConnectorConfiguration;
import net.zero.smarttrace.ui.wizards.ModelWizard;

public class JDIConnectionWizard extends ModelWizard {

    public static final String MODEL_JDI_CONNECTION = "MODEL_JDI_CONNECTION";

    private ConnectorConfiguration config = new ConnectorConfiguration();

    public JDIConnectionWizard() {
        setWindowTitle("JDI Connection");
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addModelPages() {
        addModelPage(new ConnectionTypeWizardPage());
        addModelPage(new ConnectionArgumentsWizardPage());
        addModelPage(new ConnectionSaveWizardPage());
    }

    public Object getModel(String name) {
        if (name.equals(MODEL_JDI_CONNECTION)) return config;
        return null;
    }

    @Override
    public boolean performModelFinish() {
        return true;
    }
}
