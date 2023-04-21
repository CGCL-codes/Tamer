package org.imogene.admin.contrib;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.imogene.studio.contrib.ui.generation.GenerationWizardPage;

public class SpecificWizardPage extends WizardPage implements GenerationWizardPage, SelectionListener {

    private static final String GECKO_18 = "gecko1_8";

    private static final String IE6 = "ie6";

    private static final String IE8 = "ie8";

    private static final String SAFARI = "safari";

    private static final String OPERA = "opera";

    private static final String SEPARATOR = ",";

    private static final String PROPERTY_KEY = "navigators";

    public SpecificWizardPage() {
        super("Web application properties");
        setTitle("Web application properties");
        setDescription("Specific properties for the web generation process.");
    }

    private Button ie6Button;

    private Button ie8Button;

    private Button operaButton;

    private Button gecko18Button;

    private Button safariButton;

    @Override
    public void createControl(Composite parent) {
        Composite mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        mainComposite.setLayout(new GridLayout());
        Group navigatorGroup = new Group(mainComposite, SWT.BORDER);
        navigatorGroup.setText("Select the target web browsers");
        navigatorGroup.setLayout(new GridLayout());
        navigatorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        gecko18Button = new Button(navigatorGroup, SWT.CHECK);
        gecko18Button.setText("Firefox (Mozilla, ...)");
        gecko18Button.setSelection(true);
        gecko18Button.addSelectionListener(this);
        ie6Button = new Button(navigatorGroup, SWT.CHECK);
        ie6Button.setText("Internet Eplorer 6");
        ie6Button.setSelection(true);
        ie6Button.addSelectionListener(this);
        ie8Button = new Button(navigatorGroup, SWT.CHECK);
        ie8Button.setText("Internet Eplorer 8");
        ie8Button.setSelection(true);
        ie8Button.addSelectionListener(this);
        safariButton = new Button(navigatorGroup, SWT.CHECK);
        safariButton.setText("Chrome, Safari");
        safariButton.setSelection(true);
        safariButton.addSelectionListener(this);
        operaButton = new Button(navigatorGroup, SWT.CHECK);
        operaButton.setText("Opera");
        operaButton.setSelection(true);
        operaButton.addSelectionListener(this);
        setControl(mainComposite);
        setPageComplete(true);
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> prop = new HashMap<String, String>();
        prop.put(PROPERTY_KEY, getProperty());
        return prop;
    }

    private String getProperty() {
        StringBuffer buffer = new StringBuffer();
        if (gecko18Button.getSelection()) buffer.append(GECKO_18).append(SEPARATOR);
        if (operaButton.getSelection()) buffer.append(OPERA).append(SEPARATOR);
        if (safariButton.getSelection()) buffer.append(SAFARI).append(SEPARATOR);
        if (ie6Button.getSelection()) buffer.append(IE6).append(SEPARATOR);
        if (ie8Button.getSelection()) buffer.append(IE8).append(SEPARATOR);
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private boolean validate() {
        boolean valid = gecko18Button.getSelection() || operaButton.getSelection() || safariButton.getSelection() || ie6Button.getSelection() || ie8Button.getSelection();
        if (!valid) {
            setErrorMessage("You have to choose at least one web browser.");
            setPageComplete(false);
        } else {
            setErrorMessage(null);
            setPageComplete(true);
        }
        return valid;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        validate();
    }
}
