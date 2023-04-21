package org.jactr.eclipse.ui.preferences;

import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.jactr.eclipse.ui.UIPlugin;

/**
 * @see org.eclipse.jface.preference.PreferencePage
 */
public class TemplatesPreferencePage extends TemplatePreferencePage implements IWorkbenchPreferencePage {

    public TemplatesPreferencePage() {
        setPreferenceStore(UIPlugin.getDefault().getPreferenceStore());
        setTemplateStore(UIPlugin.getDefault().getTemplateStore());
        setContextTypeRegistry(UIPlugin.getDefault().getContextTypeRegistry());
    }

    @Override
    protected boolean isShowFormatterSetting() {
        return true;
    }

    @Override
    public void performApply() {
        UIPlugin.getDefault().savePluginPreferences();
        super.performApply();
    }
}
