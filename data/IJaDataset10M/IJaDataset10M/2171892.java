package net.sf.myway.importer.osm.net;

import net.sf.myway.base.db.BoundingBox;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Andreas Beckers
 * @version $Revision$
 */
public class ImportDataWizardPage extends WizardPage {

    private BoundingBox _boundingBox;

    public ImportDataWizardPage(String pageName, BoundingBox bb) {
        super(pageName);
        _boundingBox = bb;
        setTitle(pageName);
        setDescription("Download data from OpenStreetMap.org");
    }

    /**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText("Press finish to download data.");
        initialize();
        dialogChanged();
        setControl(container);
    }

    private void initialize() {
        if (_boundingBox == null) setErrorMessage("Es ist keine Region im Editor ge�ffnet. Bitte �ffnen Sie zun�chst" + " eine Region und rufen anschlie�end den Importer nochmals auf.");
        setPageComplete(_boundingBox != null);
    }

    /**
	 * Uses the standard container selection dialog to choose the new value for the container field.
	 */
    private void dialogChanged() {
        if (_boundingBox == null) setErrorMessage("Es ist keine Region im Editor ge�ffnet. Bitte �ffnen Sie zun�chst" + " eine Region und rufen anschlie�end den Importer nochmals auf.");
        setPageComplete(_boundingBox != null);
    }

    /**
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
    @Override
    public boolean canFlipToNextPage() {
        return false;
    }
}
