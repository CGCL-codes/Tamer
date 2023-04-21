package org.jcryptool.visual.dsa.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * page to choose whether to use a new key or enter the parameters manually.
 * @author Michael Gaber
 */
public class DecryptSignPage extends WizardPage {

    /** unique pagename to get this page from inside a wizard. */
    private static final String PAGENAME = "Decrypt/Sign Page";

    /** title of this page, displayed in the head of the wizard. */
    private static final String TITLE = "Vorgehensweise wählen";

    /** Button for selecting to create a new key. */
    private Button newKeypairButton;

    /** Button for selecting to load an existing key. */
    private Button existingKeypairButton;

    /** selection listener that updates the buttons. */
    private final SelectionListener sl = new SelectionListener() {

        public void widgetSelected(SelectionEvent e) {
            getContainer().updateButtons();
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }
    };

    /**
	 * Constructor, setting name, title and description.
	 */
    public DecryptSignPage() {
        super(PAGENAME, TITLE, null);
        this.setDescription("Bitte wählen Sie ob Sie ein bestehendes Schlüsselpaar nutzen oder ein neues erzeugen wollen");
    }

    /**
	 * sets up all the UI stuff.
	 * @param parent the parent composite
	 */
    public final void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        newKeypairButton = new Button(composite, SWT.RADIO);
        newKeypairButton.setText("Neues Schlüsselpaar");
        newKeypairButton.setToolTipText("Wählen Sie dies, wenn Sie ein neues Schlüsselpaar erzeugen wollen");
        newKeypairButton.setSelection(true);
        newKeypairButton.setLayoutData(gd);
        newKeypairButton.addSelectionListener(sl);
        existingKeypairButton = new Button(composite, SWT.RADIO);
        existingKeypairButton.setText("Bestehendes Schlüsselpaar");
        existingKeypairButton.setToolTipText("Wählen Sie dies, wenn Sie ein bestehendes Schlüsselpaar aus dem Keystore laden möchten");
        existingKeypairButton.setLayoutData(gd);
        existingKeypairButton.addSelectionListener(sl);
        setControl(composite);
    }

    @Override
    public final IWizardPage getNextPage() {
        if (newKeypairButton.getSelection()) {
            return getWizard().getPage(NewKeypairPage.getPagename());
        } else {
            return getWizard().getPage(LoadKeypairPage.getPagename());
        }
    }

    /**
	 * getter for the pagename.
	 * @return the pagename
	 */
    public static String getPagename() {
        return PAGENAME;
    }

    /**
	 * convenience method for checking whether key creation is enabled or not.
	 * @return selection status of the corresponding checkbox
	 */
    public final boolean wantNewKey() {
        return newKeypairButton.getSelection();
    }
}
