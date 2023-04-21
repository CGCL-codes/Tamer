package org.eclipse.update.internal.ui.properties;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.*;
import org.eclipse.update.configurator.*;
import org.eclipse.update.internal.ui.UpdateUIMessages;

/**
 * @see PropertyPage
 */
public class InstallConfigurationPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

    /**
	 *
	 */
    public InstallConfigurationPropertyPage() {
        noDefaultAndApplyButton();
    }

    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        addProperty(composite, UpdateUIMessages.ConfiguredSitePropertyPage_path, ConfiguratorUtils.getInstallURL().toString());
        return composite;
    }

    private void addProperty(Composite parent, String key, String value) {
        Label label = new Label(parent, SWT.NULL);
        label.setText(key);
        label = new Label(parent, SWT.NULL);
        label.setText(value);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(gd);
    }
}
