package org.nightlabs.jfire.base.admin.ui.configgroup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.nightlabs.base.ui.composite.Formular;
import org.nightlabs.base.ui.composite.FormularChangeListener;
import org.nightlabs.base.ui.composite.FormularChangedEvent;
import org.nightlabs.base.ui.resource.SharedImages;
import org.nightlabs.base.ui.wizard.DynamicPathWizardPage;
import org.nightlabs.jdo.ObjectIDUtil;
import org.nightlabs.jfire.base.admin.ui.BaseAdminPlugin;
import org.nightlabs.jfire.base.admin.ui.resource.Messages;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class CreateConfigGroupPage extends DynamicPathWizardPage implements FormularChangeListener {

    private Text configGroupName;

    /**
	 * The configType of the Config
	 */
    private String configGroupType;

    public CreateConfigGroupPage(String title, String configGroupType) {
        super(CreateConfigGroupPage.class.getName(), title, SharedImages.getWizardPageImageDescriptor(BaseAdminPlugin.getDefault(), CreateConfigGroupPage.class));
        this.configGroupType = configGroupType;
    }

    @Override
    public Control createPageContents(Composite parent) {
        Formular f = new Formular(parent, SWT.NONE, this);
        configGroupName = f.addTextInput(Messages.getString("org.nightlabs.jfire.base.admin.ui.configgroup.CreateConfigGroupPage.configGroupName.labelText"), null);
        return f;
    }

    @Override
    public void onShow() {
        super.onShow();
        verifyInput();
    }

    protected void verifyInput() {
        if ("".equals(getConfigGroupName())) updateStatus(Messages.getString("org.nightlabs.jfire.base.admin.ui.configgroup.CreateConfigGroupPage.errorConfigGroupNameMissing")); else updateStatus(null);
    }

    @Override
    public boolean isPageComplete() {
        return !"".equals(getConfigGroupName());
    }

    public String getConfigGroupKey() {
        return ObjectIDUtil.makeValidIDString(configGroupName.getText(), true);
    }

    public String getConfigGroupType() {
        return configGroupType;
    }

    public String getConfigGroupName() {
        return configGroupName.getText();
    }

    public void formularChanged(FormularChangedEvent event) {
        if (event.getSource() == configGroupName) {
            String newStr = ObjectIDUtil.makeValidIDString(configGroupName.getText());
        }
        verifyInput();
    }
}
