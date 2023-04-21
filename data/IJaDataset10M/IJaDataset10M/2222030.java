package net.sf.stump.eclipse.configuration;

import java.io.IOException;
import net.sf.stump.eclipse.WicketPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * @author Joni Freeman
 */
public class ProjectPropertiesPage extends PropertyPage {

    private NatureSelection natureSelection;

    private WebResourceConfiguration webResourceConfiguration;

    public ProjectPropertiesPage() {
    }

    @Override
    protected Control createContents(Composite parent) {
        initStore();
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        natureSelection = new NatureSelection(composite, (IProject) getElement());
        webResourceConfiguration = new WebResourceConfiguration(composite, (IProject) getElement(), getPreferenceStore());
        return composite;
    }

    @Override
    public boolean performOk() {
        boolean b = natureSelection.performOk((IProject) getElement());
        b = b | webResourceConfiguration.performOk((IProject) getElement());
        WicketPlugin.getDefault().savePluginPreferences();
        return b;
    }

    private void initStore() {
        try {
            setPreferenceStore(ProjectPreferenceStore.store((IProject) getElement()));
        } catch (IOException e) {
            WicketPlugin.logAndDisplayError("Preference Store Error", "Can't open project specific preference store.", e);
        }
    }
}
