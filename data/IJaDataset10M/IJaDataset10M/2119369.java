package q_impress.pmi.plugin.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import q_impress.pmi.plugin.Activator;

/**
 * The root preference page to set up Performance Modeling preferences.
 * @author Mauro Luigi Drago
 *
 */
public class RootPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public RootPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Performance modeling preferences");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    @Override
    public void createFieldEditors() {
        addField(new FileFieldEditor(PreferenceConstants.PRISM_CMD, "Prism command path :", true, getFieldEditorParent()));
        addField(new FileFieldEditor(PreferenceConstants.JMT_JAR_LOCATION, "JMT jar file path :", true, getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.JMT_JMODEL_MAINCLASS, "JMT JModel Main Class :", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
