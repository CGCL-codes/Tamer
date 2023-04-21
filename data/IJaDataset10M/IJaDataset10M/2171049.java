package de.mpiwg.vspace.digilib.preferences.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.mpiwg.vspace.digilib.Activator;
import de.mpiwg.vspace.digilib.Constants;
import de.mpiwg.vspace.util.PropertyHandler;
import de.mpiwg.vspace.util.PropertyHandlerRegistry;

public class InfoWizardPage extends WizardPage {

    private PropertyHandler handler;

    private Text tUrl;

    private Text tDescription;

    private Text tTitle;

    private String sUrl = "";

    private String sDescription = "";

    private String sTitle = "";

    protected InfoWizardPage(String pageName) {
        super(pageName);
        handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
    }

    public void createControl(Composite parent) {
        Composite plate = new Composite(parent, SWT.NONE);
        plate.setLayout(new GridLayout(2, false));
        setTitle(handler.getProperty("_new_wizard_info_page_title"));
        setDescription(handler.getProperty("_new_wizard_info_page_description"));
        Label lTitle = new Label(plate, SWT.NONE);
        lTitle.setText(handler.getProperty("_new_wizard_info_title_label"));
        tTitle = new Text(plate, SWT.BORDER);
        tTitle.setText(sTitle);
        {
            GridData data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            tTitle.setLayoutData(data);
        }
        tTitle.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setPageComplete(validate());
            }
        });
        Label lURL = new Label(plate, SWT.NONE);
        lURL.setText(handler.getProperty("_new_wizard_info_url_label"));
        tUrl = new Text(plate, SWT.BORDER);
        tUrl.setText(sUrl);
        {
            GridData data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            tUrl.setLayoutData(data);
        }
        Label lDescription = new Label(plate, SWT.NONE);
        lDescription.setText(handler.getProperty("_new_wizard_info_description_label"));
        tDescription = new Text(plate, SWT.BORDER);
        tDescription.setText(sDescription);
        {
            GridData data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            tDescription.setLayoutData(data);
        }
        setControl(plate);
        setPageComplete(validate());
    }

    protected boolean validate() {
        if (tTitle.getText() == null || tTitle.getText().trim().equals("")) {
            setErrorMessage(handler.getProperty("_new_wizard_info_no_title_error"));
            return false;
        }
        setErrorMessage(null);
        return true;
    }

    public String getViewerTitle() {
        return tTitle.getText();
    }

    public String getViewerURL() {
        return tUrl.getText();
    }

    public String getViewerDescription() {
        return tDescription.getText();
    }

    public void setViewerTitle(String title) {
        sTitle = title;
    }

    public void setViewerURL(String url) {
        sUrl = url;
    }

    public void setViewerDescription(String desc) {
        sDescription = desc;
    }
}
