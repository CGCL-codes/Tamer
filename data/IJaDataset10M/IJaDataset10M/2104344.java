package org.jdeluxe.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jdeluxe.Activator;
import org.jdeluxe.i18n.ResourceManager;

/**
 * The Class DirectoriesPreferencePage.
 */
public class DirectoriesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /** The res. */
    ResourceManager res = ResourceManager.getInstance();

    /**
	 * Instantiates a new directories preference page.
	 */
    public DirectoriesPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("The default directories can be changed here");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    public void createFieldEditors() {
        addField(new StringFieldEditor(PrefConstants.DIRECTORY_JDX, "Standard JDX directory to create", getFieldEditorParent()));
        addField(new StringFieldEditor(PrefConstants.DIRECTORY_XSD, "Standard XSD directory to create", getFieldEditorParent()));
        addField(new StringFieldEditor(PrefConstants.DIRECTORY_DOC, "Standard doc directory to create", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
