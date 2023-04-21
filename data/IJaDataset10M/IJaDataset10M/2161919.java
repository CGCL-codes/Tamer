package org.deved.antlride.stringtemplate.internal.ui.preferences;

import org.deved.antlride.stringtemplate.ui.StringTemplateUI;
import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ui.PreferencesAdapter;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class StringTemplatePreferencePage extends AbstractConfigurationBlockPropertyAndPreferencePage {

    private static final String PREFERENCE_PAGE_ID = "org.deved.antlride.stringtemplate.ui.preferences";

    @Override
    protected AbstractOptionsBlock createOptionsBlock(IStatusChangeListener context, IProject project, IWorkbenchPreferenceContainer container) {
        return new StringTemplateBlock(context, project, container);
    }

    @Override
    protected String getHelpId() {
        return null;
    }

    @Override
    protected String getProjectHelpId() {
        return null;
    }

    @Override
    protected void setDescription() {
    }

    @Override
    protected void setPreferenceStore() {
        PreferencesAdapter preferences = new PreferencesAdapter(StringTemplateUI.getDefault().getPluginPreferences());
        setPreferenceStore(preferences);
    }

    @Override
    protected String getPreferencePageId() {
        return PREFERENCE_PAGE_ID;
    }

    @Override
    protected String getPropertyPageId() {
        return null;
    }
}
