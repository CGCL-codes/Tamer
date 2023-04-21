package org.nexopenframework.ide.eclipse.jst.datamodel.web.struts2;

import java.util.Arrays;
import java.util.List;
import org.eclipse.jst.j2ee.internal.plugin.J2EEUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage;
import org.nexopenframework.ide.eclipse.jst.Messages;
import org.nexopenframework.ide.eclipse.ui.NexOpenUIActivator;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p></p>
 * 
 * @see org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class Struts2FacetInstallPage extends AbstractFacetWizardPage {

    private static final List STRUTS2_VERSIONS = Arrays.asList(NexOpenUIActivator.STRUTS2_VERSIONS);

    /***/
    private Struts2FacetInstallConfig config;

    /***/
    private Combo strutsVersion;

    /***/
    private Text urlPattern;

    public Struts2FacetInstallPage() {
        super("webcomponent.struts2.install.page");
        setTitle(Messages.getString("webcomponent.struts2.page.title"));
        setDescription(Messages.getString("webcomponent.struts2.page.description"));
        setImageDescriptor(J2EEUIPlugin.getDefault().getImageDescriptor("war_wiz"));
    }

    /**
	 * 
	 * @see org.eclipse.wst.common.project.facet.ui.IFacetWizardPage#setConfig(java.lang.Object)
	 */
    public void setConfig(final Object config) {
        this.config = (Struts2FacetInstallConfig) config;
    }

    /**
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.LEFT);
        composite.setLayout(new GridLayout(1, false));
        final Label label_url = new Label(composite, SWT.NONE);
        label_url.setLayoutData(gdhfill());
        label_url.setText("Struts action extension:");
        this.urlPattern = new Text(composite, SWT.BORDER);
        this.urlPattern.setLayoutData(gdhfill());
        this.urlPattern.setText(this.config.getActionExtension());
        final Label label_v = new Label(composite, SWT.NONE);
        label_v.setLayoutData(gdhfill());
        label_v.setText("Struts version:");
        strutsVersion = new Combo(composite, SWT.BORDER);
        strutsVersion.setLayoutData(gdhfill());
        strutsVersion.setItems((String[]) STRUTS2_VERSIONS.toArray(new String[STRUTS2_VERSIONS.size()]));
        strutsVersion.setText(strutsVersion.getItem(0));
        this.setControl(composite);
    }

    /**
	 * 
	 * @see org.eclipse.wst.common.project.facet.ui.AbstractFacetWizardPage#transferStateToConfig()
	 */
    public void transferStateToConfig() {
        this.config.setActionExtension(this.urlPattern.getText());
        this.config.setVersion(strutsVersion.getText());
    }

    private GridData gdhfill() {
        return new GridData(GridData.FILL_HORIZONTAL);
    }
}
