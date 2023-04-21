package org.rubypeople.rdt.internal.ui.rubyeditor.templates;

import java.io.IOException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.rubypeople.rdt.internal.ui.RubyPlugin;

public class RubyTemplateAccess {

    /** Key to store custom templates. */
    private static final String CUSTOM_TEMPLATES_KEY = "org.rubypeople.rdt.ui.customtemplates";

    /** The shared instance. */
    private static RubyTemplateAccess fgInstance;

    /** The template store. */
    private TemplateStore fStore;

    /** The context type registry. */
    private ContributionContextTypeRegistry fRegistry;

    private RubyTemplateAccess() {
    }

    /**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
    public static RubyTemplateAccess getDefault() {
        if (fgInstance == null) {
            fgInstance = new RubyTemplateAccess();
        }
        return fgInstance;
    }

    /**
	 * Returns this plug-in's template store.
	 * 
	 * @return the template store of this plug-in instance
	 */
    public TemplateStore getTemplateStore() {
        if (fStore == null) {
            fStore = new ContributionTemplateStore(getContextTypeRegistry(), RubyPlugin.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
            try {
                fStore.load();
            } catch (IOException e) {
                RubyPlugin.log(e);
            }
        }
        return fStore;
    }

    /**
	 * Returns this plug-in's context type registry.
	 * 
	 * @return the context type registry for this plug-in instance
	 */
    public ContextTypeRegistry getContextTypeRegistry() {
        if (fRegistry == null) {
            fRegistry = new ContributionContextTypeRegistry();
            fRegistry.addContextType(RubyFileContextType.RUBYFILE_CONTEXT_TYPE);
        }
        return fRegistry;
    }

    public IPreferenceStore getPreferenceStore() {
        return RubyPlugin.getDefault().getPreferenceStore();
    }

    public void savePluginPreferences() {
        RubyPlugin.getDefault().savePluginPreferences();
    }
}
