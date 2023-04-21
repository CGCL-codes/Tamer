package org.kompiro.readviewer.ui.wizards;

import java.util.Properties;
import org.eclipse.jface.wizard.IWizardPage;
import org.kompiro.readviewer.service.INotificationService;

public interface INotificationServiceWizardPage extends IWizardPage {

    public abstract Properties getProperties();

    public abstract String getType();

    public abstract INotificationService getService();
}
