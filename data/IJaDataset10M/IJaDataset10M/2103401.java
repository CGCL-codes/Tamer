package org.plog4u.webbrowser.internal;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * A preference page that holds internet preferences.
 */
public class InternetPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    /**
	 * InternetPreferencePage constructor comment.
	 */
    public InternetPreferencePage() {
        super();
        noDefaultAndApplyButton();
    }

    /**
	 * Create the preference options.
	 *
	 * @param parent org.eclipse.swt.widgets.Composite
	 * @return org.eclipse.swt.widgets.Control
	 */
    protected Control createContents(Composite parent) {
        initializeDialogUnits(parent);
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
        layout.verticalSpacing = convertVerticalDLUsToPixels(4);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
        composite.setLayoutData(data);
        WorkbenchHelp.setHelp(composite, ContextIds.PREF_BROWSER);
        Label label = new Label(composite, SWT.WRAP);
        label.setText(WebBrowserUIPlugin.getResource("%preferenceInternetDescription"));
        data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);
        Dialog.applyDialogFont(composite);
        return composite;
    }

    /**
	 * Initializes this preference page using the passed desktop.
	 *
	 * @param desktop the current desktop
	 */
    public void init(IWorkbench workbench) {
    }
}
