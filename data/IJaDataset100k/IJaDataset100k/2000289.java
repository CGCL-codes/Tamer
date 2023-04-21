package org.remus.infomngmnt.birtreport.wizard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.remus.common.ui.image.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.remus.infomngmnt.birtreport.ReportActivator;
import org.remus.infomngmnt.birtreport.extension.IReportTemplate;
import org.remus.infomngmnt.birtreport.extension.ITemplateParameter;
import org.remus.infomngmnt.birtreport.extension.ParameterControlManager;
import org.remus.infomngmnt.birtreport.messages.Messages;
import org.remus.infomngmnt.birtreport.parameter.AbstractParameterControl;

public class ParameterSelectionPage extends WizardPage {

    private IReportTemplate template;

    private Composite container;

    private final Map<String, Control> templateMap = new HashMap<String, Control>();

    private final Map<String, Map<String, AbstractParameterControl>> controlMap = new HashMap<String, Map<String, AbstractParameterControl>>();

    private StackLayout stackLayout;

    /**
	 * @return the template
	 */
    public final IReportTemplate getTemplate() {
        return this.template;
    }

    /**
	 * @param template
	 *            the template to set
	 */
    public final void setTemplate(final IReportTemplate template) {
        this.template = template;
        if (this.container != null) {
            buildTemplateUi();
        }
    }

    private void buildTemplateUi() {
        if (this.templateMap.get(this.template.getId()) == null) {
            Composite composite = new Composite(this.container, SWT.NONE);
            composite.setLayout(new GridLayout(2, false));
            ITemplateParameter[] parameter = this.template.getParameter();
            Map<String, AbstractParameterControl> control2TemplateParameterMap = new HashMap<String, AbstractParameterControl>();
            for (ITemplateParameter iTemplateParameter : parameter) {
                String label = iTemplateParameter.getLabel();
                Label nameLabel = new Label(composite, SWT.NONE);
                if (label.trim().length() > 0) {
                    nameLabel.setText(label);
                } else {
                    nameLabel.setText(iTemplateParameter.getName());
                }
                nameLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
                Composite composite2 = new Composite(composite, SWT.NONE);
                composite2.setLayout(new GridLayout());
                composite2.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
                try {
                    AbstractParameterControl controlById = ParameterControlManager.getInstance().getControlById(iTemplateParameter.getParameterTypeId());
                    if (controlById != null) {
                        controlById.setOptions(iTemplateParameter.getParameterTypeParameters());
                        controlById.createPartControl(composite2);
                        control2TemplateParameterMap.put(iTemplateParameter.getName(), controlById);
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            this.controlMap.put(this.template.getId(), control2TemplateParameterMap);
            this.templateMap.put(this.template.getId(), composite);
        }
        Control control2 = this.templateMap.get(this.template.getId());
        this.stackLayout.topControl = control2;
        this.container.layout();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Map<String, String> getParameterValues() {
        Map<String, String> returnValue = new HashMap<String, String>();
        Map<String, AbstractParameterControl> map = this.controlMap.get(this.template.getId());
        Set<String> keySet = map.keySet();
        for (String string : keySet) {
            try {
                returnValue.put(string, map.get(string).getParameterValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    /**
	 * Create the wizard.
	 */
    public ParameterSelectionPage() {
        super("parameterSelection");
        setPageComplete(false);
    }

    /**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
    public void createControl(final Composite parent) {
        setTitle(Messages.ParameterSelectionPage_ReportParameterSelection);
        setDescription(Messages.ParameterSelectionPage_TemplateNeedsParameter);
        setImageDescriptor(ResourceManager.getPluginImageDescriptor(ReportActivator.getDefault(), "icons/create_report_wizard.gif"));
        this.container = new Composite(parent, SWT.NULL);
        this.container.setLayout(this.stackLayout = new StackLayout());
        buildTemplateUi();
        setControl(this.container);
        setPageComplete(true);
    }
}
