package org.kalypso.nofdpidss.hydraulic.computation.view.preferences;

import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.hydraulic.computation.NofdpHydraulicComputationPlugin;
import org.kalypso.nofdpidss.hydraulic.computation.i18n.Messages;

/**
 * @author kuch
 */
public class NofdpPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

    public static final String SOBEK_INSTALLATION_DIR = "SOBEK_INSTALLATION_DIR";

    public NofdpPreferencesPage() {
        setPreferenceStore(NofdpHydraulicComputationPlugin.getDefault().getPreferenceStore());
    }

    /**
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
    public void init(final IWorkbench workbench) {
    }

    /**
   * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
   */
    @Override
    protected Control createContents(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        Group grSobekInstallationDir = new Group(composite, SWT.NONE);
        grSobekInstallationDir.setLayout(new GridLayout(2, false));
        grSobekInstallationDir.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        grSobekInstallationDir.setText(Messages.NofdpPreferencesPage_0);
        final Text textSobekInstallationDir = new Text(grSobekInstallationDir, SWT.BORDER | SWT.READ_ONLY);
        textSobekInstallationDir.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        Button buttonSobekInstalltionDir = new Button(grSobekInstallationDir, SWT.PUSH);
        buttonSobekInstalltionDir.setText("...");
        buttonSobekInstalltionDir.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                IPath path = browse();
                String value;
                if (path != null) value = path.toOSString(); else value = "";
                getPreferenceStore().setValue(SOBEK_INSTALLATION_DIR, value);
                textSobekInstallationDir.setText(value);
                checkPage();
            }
        });
        textSobekInstallationDir.setText(getPreferenceStore().getString(SOBEK_INSTALLATION_DIR));
        checkPage();
        return composite;
    }

    protected boolean checkPage() {
        String sobekInstallationDir = getPreferenceStore().getString(SOBEK_INSTALLATION_DIR);
        if (sobekInstallationDir == null || "".equals(sobekInstallationDir.trim())) {
            setErrorMessage(Messages.NofdpPreferencesPage_1);
            setMessage(null);
            return false;
        }
        Path path = new Path(sobekInstallationDir);
        File file = path.toFile();
        if (!file.exists()) {
            setErrorMessage(Messages.NofdpPreferencesPage_2);
            setMessage(null);
            return false;
        }
        File openMiDir = new File(file, "PROGRAMS/openmi");
        if (!openMiDir.exists()) {
            setErrorMessage(Messages.NofdpPreferencesPage_3);
            setMessage(null);
            return false;
        }
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    private IPath browse() {
        final DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
        final String result = dialog.open();
        if (result == null) return null;
        return new Path(result);
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        final PreferenceStore mystore = new PreferenceStore(NofdpPreferencesPage.class.getName());
        try {
            mystore.load();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        setPreferenceStore(mystore);
        return mystore;
    }

    public IPath getSobekInstallationDirectory() throws CoreException {
        if (!checkPage()) throw new CoreException(StatusUtilities.createErrorStatus(Messages.NofdpPreferencesPage_4));
        String sobekInstallationDir = getPreferenceStore().getString(SOBEK_INSTALLATION_DIR);
        return new Path(sobekInstallationDir);
    }
}
