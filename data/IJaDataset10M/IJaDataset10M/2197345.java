package org.perlipse.internal.ui.templates;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.jface.preference.IPreferenceStore;
import org.perlipse.internal.ui.PerlUIPlugin;

/**
 * provides access to the perl template store
 */
public class PerlTemplateAccess extends ScriptTemplateAccess {

    private static PerlTemplateAccess self;

    public static String CUSTOM_TEMPLATE_KEY = "org.perlipse.ui.templates";

    public static PerlTemplateAccess getInstance() {
        if (self == null) {
            self = new PerlTemplateAccess();
        }
        return self;
    }

    @Override
    protected String getContextTypeId() {
        return PerlUniversalTemplateContextType.TPL_CONTEXT_TYPE_ID;
    }

    @Override
    protected String getCustomTemplatesKey() {
        return PerlTemplateAccess.CUSTOM_TEMPLATE_KEY;
    }

    @Override
    protected IPreferenceStore getPreferenceStore() {
        return PerlUIPlugin.getPlugin().getPreferenceStore();
    }
}
