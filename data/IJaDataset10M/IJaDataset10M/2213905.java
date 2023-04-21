package nzdis.opalpe.AMC.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Display a property file for the .rules files used in the ROK context
 * @author sylvie
 *
 */
public class RokPropertyPage extends PropertyPage {

    private static final String PATH_TITLE = "&Path:";

    private static final String PATH_TYPE = "&Type:";

    private static final String PATH_LOC = "&Location:";

    private static final String OWNER_TITLE = "&Owner:";

    private static final String OWNER_PROPERTY = "OWNER";

    private static final String DEFAULT_OWNER = "nzdis";

    private static final int TEXT_FIELD_WIDTH = 50;

    private Text ownerText;

    /**
	 * Constructor for SamplePropertyPage.
	 */
    public RokPropertyPage() {
        super();
    }

    private void addFirstSection(Composite parent) {
        Composite composite = createDefaultComposite(parent);
        Label pathLabel = new Label(composite, SWT.NONE);
        pathLabel.setText(PATH_TITLE);
        Text pathValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        pathValueText.setText(((IResource) getElement()).getFullPath().toString());
        Label typeLabel = new Label(composite, SWT.NONE);
        typeLabel.setText(PATH_TYPE);
        Text typeValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        typeValueText.setText("ROK rules file");
        Label locLabel = new Label(composite, SWT.NONE);
        locLabel.setText(PATH_LOC);
        Text LocValueText = new Text(composite, SWT.WRAP | SWT.READ_ONLY);
        LocValueText.setText(((IResource) getElement()).getLocation().toString());
    }

    private void addSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(gridData);
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

    /**
	 * @see PreferencePage#createContents(Composite)
	 */
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

    protected void performDefaults() {
        ownerText.setText(DEFAULT_OWNER);
    }

    public boolean performOk() {
        try {
            ((IResource) getElement()).setPersistentProperty(new QualifiedName("", OWNER_PROPERTY), ownerText.getText());
        } catch (CoreException e) {
            return false;
        }
        return true;
    }
}
