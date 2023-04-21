package org.rubypeople.rdt.internal.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rubypeople.rdt.internal.ui.RubyPlugin;
import org.rubypeople.rdt.ui.PreferenceConstants;

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
public class RdocRiPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public RdocRiPreferencePage() {
        super(GRID);
        setPreferenceStore(RubyPlugin.getDefault().getPreferenceStore());
        setDescription(PreferencesMessages.RiPreferencePage_description_label);
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    public void createFieldEditors() {
        addField(new FileFieldEditor(PreferenceConstants.RDOC_PATH, PreferencesMessages.RiPreferencePage_rdocpath_label, getFieldEditorParent()));
        addField(new FileFieldEditor(PreferenceConstants.RI_PATH, PreferencesMessages.RiPreferencePage_ripath_label, getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
