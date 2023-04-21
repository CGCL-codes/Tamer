package com.cjssolutions.plex.plugin.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class PlexProjectPage extends PropertyPage {

    private static final String PATH_TITLE = "Path:";

    private static final String OWNER_TITLE = "&Owner:";

    private static final String OWNER_PROPERTY = "OWNER";

    private static final String DEFAULT_OWNER = "John Doe";

    private static final int TEXT_FIELD_WIDTH = 50;

    private Text ownerText;

    /**
	 * Constructor for SamplePropertyPage.
	 */
    public PlexProjectPage() {
        super();
    }

    private void addFirstSection(Composite parent) {
        Composite composite = createDefaultComposite(parent);
        Label pathLabel = new Label(composite, SWT.NONE);
        pathLabel.setText(PATH_TITLE);
        Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        pathValueText.setText(((IResource) getElement()).getFullPath().toString());
    }

    private void addSecondSection(Composite parent) {
        Composite composite = createDefaultComposite(parent);
        Label ownerLabel = new Label(composite, SWT.NONE);
        ownerLabel.setText(OWNER_TITLE);
        ownerText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        GridData gd = new GridData();
        gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
        ownerText.setLayoutData(gd);
        try {
            String owner = ((IResource) getElement()).getPersistentProperty(new QualifiedName("", OWNER_PROPERTY));
            ownerText.setText((owner != null) ? owner : DEFAULT_OWNER);
        } catch (CoreException e) {
            ownerText.setText(DEFAULT_OWNER);
        }
    }

    private void addSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(gridData);
    }

    /**
	 * @see PreferencePage#createContents(Composite)
	 */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        composite.setLayoutData(data);
        addFirstSection(composite);
        addSeparator(composite);
        addSecondSection(composite);
        return composite;
    }

    private Composite createDefaultComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        composite.setLayoutData(data);
        return composite;
    }

    @Override
    protected void performDefaults() {
        ownerText.setText(DEFAULT_OWNER);
    }

    @Override
    public boolean performOk() {
        try {
            ((IResource) getElement()).setPersistentProperty(new QualifiedName("", OWNER_PROPERTY), ownerText.getText());
        } catch (CoreException e) {
            return false;
        }
        return true;
    }
}
