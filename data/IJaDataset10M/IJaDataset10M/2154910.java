package de.lema.client.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import de.lema.client.activator.Activator;
import de.lema.client.eclipse.i18n.Messages;

public class LemaMainPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private static final String ID = "de.lema.client.ui.preferences.LemaMainPreferencesPage";

    public LemaMainPreferencesPage() {
        super(GRID);
    }

    @Override
    public void createFieldEditors() {
        Composite fieldEditorParent = getFieldEditorParent();
        addField(new PathEditor("id", Messages.LemaMainPreferencesPage_PathEditor_Message, Messages.LemaMainPreferencesPage_PathEditor_Titel, fieldEditorParent));
    }

    public void init(final IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription(Messages.LemaMainPreferencesPage_Titel);
    }
}
