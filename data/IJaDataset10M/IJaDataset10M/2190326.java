package fr.esrf.tango.pogo.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import fr.esrf.tango.pogo.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class PogoPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private DirectoryFieldEditor templHomeEditor;

    private DirectoryFieldEditor superHomeEditor;

    private StringFieldEditor docPathEditor;

    public PogoPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Pogo environment variables settings");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    public void createFieldEditors() {
        templHomeEditor = new DirectoryFieldEditor(PreferenceConstants.TEMPL_HOME_PREF, "&TEMPL_HOME environment variable:", getFieldEditorParent());
        addField(templHomeEditor);
        superHomeEditor = new DirectoryFieldEditor(PreferenceConstants.SUPER_HOME_PREF, "&SUPER_HOME environment variable:", getFieldEditorParent());
        addField(superHomeEditor);
        docPathEditor = new StringFieldEditor(PreferenceConstants.CPP_DOC_PREF, "&CPP_DOC_PATH environment variable:", getFieldEditorParent());
        addField(docPathEditor);
    }

    /**
	 * Action performed after "ok" button from the preference page is pushed.
	 * New values are being saved and exported to the local machine set of environmental
	 * variables.  
	 */
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        String stringLabel[] = new String[3];
        stringLabel[0] = templHomeEditor.getStringValue();
        store.setValue(PreferenceConstants.TEMPL_HOME_PREF, stringLabel[0]);
        stringLabel[1] = docPathEditor.getStringValue();
        store.setValue(PreferenceConstants.CPP_DOC_PREF, stringLabel[1]);
        stringLabel[2] = superHomeEditor.getStringValue();
        store.setValue(PreferenceConstants.SUPER_HOME_PREF, stringLabel[2]);
        return true;
    }

    public void init(IWorkbench workbench) {
    }
}
