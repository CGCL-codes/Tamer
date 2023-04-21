package ganymede.preferences;

import ganymede.Ganymede;
import ganymede.GanymedeUtilities;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ColorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ColorPreferencePage() {
        super(GRID);
        setPreferenceStore(Ganymede.getDefault().getPreferenceStore());
        setDescription("Color Preferences");
        initializeDefaults();
    }

    public static final String DEBUG_COLOR_KEY = "DEBUG_COLOR_KEY";

    public static final String INFO_COLOR_KEY = "INFO_COLOR_KEY";

    public static final String WARN_COLOR_KEY = "WARN_COLOR_KEY";

    public static final String ERROR_COLOR_KEY = "ERROR_COLOR_KEY";

    public static final String FATAL_COLOR_KEY = "FATAL_COLOR_KEY";

    public void init(IWorkbench workbench) {
    }

    public void createFieldEditors() {
        addField(new ColorFieldEditor(DEBUG_COLOR_KEY, "Debug", getFieldEditorParent()));
        addField(new ColorFieldEditor(INFO_COLOR_KEY, "Info", getFieldEditorParent()));
        addField(new ColorFieldEditor(WARN_COLOR_KEY, "Warn", getFieldEditorParent()));
        addField(new ColorFieldEditor(ERROR_COLOR_KEY, "Error", getFieldEditorParent()));
        addField(new ColorFieldEditor(FATAL_COLOR_KEY, "Fatal", getFieldEditorParent()));
    }

    private void initializeDefaults() {
        GanymedeUtilities.initColorDefaults();
    }

    public boolean performOk() {
        boolean ok = super.performOk();
        GanymedeUtilities.updateColors();
        return ok;
    }

    protected void performApply() {
        super.performApply();
        GanymedeUtilities.updateColors();
    }
}
