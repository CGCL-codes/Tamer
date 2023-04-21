package es.nom.morenojuarez.modulipse.ui.preferences;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import es.nom.morenojuarez.modulipse.ui.PluginUI;

/**
 * Preference page for Modula-2 code templates. 
 */
public class Modula2TemplatesPreferencePage extends TemplatePreferencePage implements IWorkbenchPreferencePage {

    /**
	 * Creates a new <code>Modula2TemplatesPreferencePage</code> object.
	 */
    public Modula2TemplatesPreferencePage() {
        super();
    }

    @Override
    public void init(IWorkbench workbench) {
        super.init(workbench);
        setPreferenceStore(PluginUI.getDefault().getPreferenceStore());
        setTemplateStore(PluginUI.getDefault().getTemplateStore());
        setContextTypeRegistry(PluginUI.getDefault().getContextTypeRegistry());
    }

    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        PluginUI.getDefault().savePluginPreferences();
        return ok;
    }
}
