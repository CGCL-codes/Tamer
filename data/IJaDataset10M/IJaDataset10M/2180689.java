package net.esle.sinadura.gui.ventanas.preferencias;

import net.esle.sinadura.gui.application.ResourceHelper;
import org.eclipse.jface.preference.FieldEditorPreferencePage;

/**
 * @author zylk.net
 */
public class SignAppearancePreferencesWindow extends FieldEditorPreferencePage {

    /**
	 * @param messages
	 */
    public SignAppearancePreferencesWindow() {
        super(FLAT);
    }

    /**
	 * Creates the field editors
	 */
    @Override
    protected void createFieldEditors() {
        AppearanceStringFieldEditor fe1 = new AppearanceStringFieldEditor("sign.appearance.reason", ResourceHelper.getLanguage().getString("sign.appearance.reason"), getFieldEditorParent());
        addField(fe1);
        AppearanceStringFieldEditor fe2 = new AppearanceStringFieldEditor("sign.appearance.location", ResourceHelper.getLanguage().getString("sign.appearance.location"), getFieldEditorParent());
        addField(fe2);
    }
}
