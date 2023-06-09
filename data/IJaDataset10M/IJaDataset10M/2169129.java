package de.uniwue.dltk.textmarker.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.ui.PreferencesAdapter;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.AbstractTodoTaskOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import de.uniwue.dltk.textmarker.core.TextMarkerNature;
import de.uniwue.dltk.textmarker.core.TextMarkerPlugin;

/**
 * @author Martin Toepfer
 * 
 */
public class TextMarkerTodoTaskPreferencePage extends AbstractConfigurationBlockPropertyAndPreferencePage {

    static final PreferenceKey CASE_SENSITIVE = AbstractTodoTaskOptionsBlock.createCaseSensitiveKey(TextMarkerPlugin.PLUGIN_ID);

    static final PreferenceKey ENABLED = AbstractTodoTaskOptionsBlock.createEnabledKey(TextMarkerPlugin.PLUGIN_ID);

    static final PreferenceKey TAGS = AbstractTodoTaskOptionsBlock.createTagKey(TextMarkerPlugin.PLUGIN_ID);

    protected PreferenceKey[] getPreferenceKeys() {
        return new PreferenceKey[] { TAGS, ENABLED, CASE_SENSITIVE };
    }

    @Override
    protected String getHelpId() {
        return null;
    }

    @Override
    protected void setDescription() {
        setDescription(TextMarkerPreferencesMessages.TodoTaskDescription);
    }

    protected Preferences getPluginPreferences() {
        return TextMarkerPlugin.getDefault().getPluginPreferences();
    }

    @Override
    protected AbstractOptionsBlock createOptionsBlock(IStatusChangeListener newStatusChangedListener, IProject project, IWorkbenchPreferenceContainer container) {
        return new AbstractTodoTaskOptionsBlock(newStatusChangedListener, project, getPreferenceKeys(), container) {

            @Override
            protected PreferenceKey getTags() {
                return TAGS;
            }

            @Override
            protected PreferenceKey getEnabledKey() {
                return ENABLED;
            }

            @Override
            protected PreferenceKey getCaseSensitiveKey() {
                return CASE_SENSITIVE;
            }
        };
    }

    @Override
    protected String getNatureId() {
        return TextMarkerNature.NATURE_ID;
    }

    @Override
    protected String getProjectHelpId() {
        return null;
    }

    @Override
    protected void setPreferenceStore() {
        setPreferenceStore(new PreferencesAdapter(TextMarkerPlugin.getDefault().getPluginPreferences()));
    }

    @Override
    protected String getPreferencePageId() {
        return "de.uniwue.dltk.textmarker.preferences.todo";
    }

    @Override
    protected String getPropertyPageId() {
        return "de.uniwue.dltk.textmarker.propertyPage.todo";
    }
}
