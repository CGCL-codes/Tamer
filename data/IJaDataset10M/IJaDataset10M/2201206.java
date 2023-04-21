package raptor.connector.fics.pref;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import raptor.Raptor;
import raptor.international.L10n;
import raptor.pref.PreferenceKeys;
import raptor.pref.fields.LabelFieldEditor;
import raptor.pref.fields.ListFieldEditor;

public class FicsRightClickGamesMenu extends FieldEditorPreferencePage {

    protected static L10n local = L10n.getInstance();

    public FicsRightClickGamesMenu() {
        super(FLAT);
        setTitle(local.getString("ficsRClkGP1"));
        setPreferenceStore(Raptor.getInstance().getPreferences());
    }

    @Override
    protected void createFieldEditors() {
        addField(new LabelFieldEditor("none", WordUtils.wrap(local.getString("ficsRClkGP2"), 70) + "\n ", getFieldEditorParent()));
        addField(new ListFieldEditor(PreferenceKeys.FICS_GAME_COMMANDS, local.getString("ficsRClkGP3"), getFieldEditorParent(), ',', 75));
    }
}
