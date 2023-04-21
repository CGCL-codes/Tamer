package com.sebulli.fakturama.preferences;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import com.sebulli.fakturama.Activator;
import com.sebulli.fakturama.ContextHelpConstants;

/**
 * Preference page for the width of the table columns
 * 
 * @author Gerd Bartelt
 */
public class ColumnWidthDialogTextsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
	 * Constructor
	 */
    public ColumnWidthDialogTextsPreferencePage() {
        super(GRID);
    }

    /**
	 * Creates the page's field editors.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
    @Override
    public void createFieldEditors() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this.getControl(), ContextHelpConstants.COLUMN_WIDTH_PREFERENCE_PAGE);
        addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_TEXTS_NAME", _("Name"), getFieldEditorParent()));
        addField(new IntegerFieldEditor("COLUMNWIDTH_DIALOG_TEXTS_TEXT", _("Text"), getFieldEditorParent()));
    }

    /**
	 * Initializes this preference page for the given workbench.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(_("Column width of the dialog to select a text"));
    }

    /**
	 * Write or read the preference settings to or from the data base
	 * 
	 * @param write
	 *            TRUE: Write to the data base
	 */
    public static void syncWithPreferencesFromDatabase(boolean write) {
        PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_TEXTS_NAME", write);
        PreferencesInDatabase.syncWithPreferencesFromDatabase("COLUMNWIDTH_DIALOG_TEXTS_TEXT", write);
    }

    /**
	 * Set the default values for this preference page
	 * 
	 * @param node
	 *            The preference node
	 */
    public static void setInitValues(IEclipsePreferences node) {
        node.put("COLUMNWIDTH_DIALOG_TEXTS_NAME", "120");
        node.put("COLUMNWIDTH_DIALOG_TEXTS_TEXT", "200");
    }
}
