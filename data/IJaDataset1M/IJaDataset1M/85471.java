package org.eclipse.ui.internal.editors.text;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.editors.text.ITextEditorHelpContextIds;

/**
 * Quick Diff preference page.
 * <p>
 * Note: Must be public since it is referenced from plugin.xml
 * </p>
 *
 * @since 3.0
 */
public class QuickDiffPreferencePage extends AbstractConfigurationBlockPreferencePage {

    protected String getHelpId() {
        return ITextEditorHelpContextIds.TEXT_EDITOR_PREFERENCE_PAGE;
    }

    protected Label createDescriptionLabel(Composite parent) {
        return null;
    }

    protected void setDescription() {
        String description = TextEditorMessages.QuickDiffConfigurationBlock_description;
        setDescription(description);
    }

    protected void setPreferenceStore() {
        setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
    }

    protected IPreferenceConfigurationBlock createConfigurationBlock(OverlayPreferenceStore overlayPreferenceStore) {
        return new QuickDiffConfigurationBlock(overlayPreferenceStore);
    }
}
