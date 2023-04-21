package com.sebulli.fakturama.preferences;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the optional items settings
 * 
 * @author Gerd Bartelt
 */
public class OptionalItemsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
	 * Constructor
	 */
    public OptionalItemsPreferencePage() {
        super(GRID);
    }

    /**
	 * Creates the page's field editors.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
    @Override
    public void createFieldEditors() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.OPTIONAL_ITEMS_PREFERENCE_PAGE);
        addField(new BooleanFieldEditor("OPTIONALITEMS_USE", _("Use optional items"), getFieldEditorParent()));
        addField(new BooleanFieldEditor("OPTIONALITEMS_REPLACE_PRICE", _("Replace price"), getFieldEditorParent()));
        addField(new StringFieldEditor("OPTIONALITEMS_PRICE_REPLACEMENT", _("Price replacement"), getFieldEditorParent()));
        addField(new StringFieldEditor("OPTIONALITEMS_OPTIONALITEM_TEXT", _("Text \"Optional Item\""), getFieldEditorParent()));
    }

    /**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(_("Otional Items Settings"));
    }

    /**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
    public static void syncWithPreferencesFromDatabase(boolean write) {
        PreferencesInDatabase.syncWithPreferencesFromDatabase("OPTIONALITEMS_USE", write);
        PreferencesInDatabase.syncWithPreferencesFromDatabase("OPTIONALITEMS_REPLACE_PRICE", write);
        PreferencesInDatabase.syncWithPreferencesFromDatabase("OPTIONALITEMS_PRICE_REPLACEMENT", write);
        PreferencesInDatabase.syncWithPreferencesFromDatabase("OPTIONALITEMS_OPTIONALITEM_TEXT", write);
    }

    /**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
    public static void setInitValues(IEclipsePreferences node) {
        node.putBoolean("OPTIONALITEMS_USE", false);
        node.putBoolean("OPTIONALITEMS_REPLACE_PRICE", true);
        node.put("OPTIONALITEMS_PRICE_REPLACEMENT", "---");
        node.put("OPTIONALITEMS_OPTIONALITEM_TEXT", _("Optional Item:<br>"));
    }
}
