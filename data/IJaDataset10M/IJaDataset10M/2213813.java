package cz.vse.keg.patomat.xd.transformation.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import cz.vse.keg.patomat.xd.transformation.XDTransformationPlugin;

/**
 * Transformation preference page
 * 
 * @author Ondrej Zamazal
 * 
 */
public class TransformationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public TransformationPreferencePage() {
        super(GRID);
        setPreferenceStore(XDTransformationPlugin.getDefault().getPreferenceStore());
        setDescription("Within this section you can setup the Transformation wizard.");
    }

    /**
	 * Creates the field editors.
	 */
    public void createFieldEditors() {
        addField(new DirectoryFieldEditor(PreferenceConstants.P_TRANSFORMATION1, "WordNet directory", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceConstants.P_TRANSFORMATION2, "Language model of POS tagger", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
