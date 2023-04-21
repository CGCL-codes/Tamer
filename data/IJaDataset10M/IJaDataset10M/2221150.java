package org.mxeclipse.preferences;

import matrix.util.MatrixException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.utils.MxEclipseUtils;

/**
 * This class represents a preference page for MxObject view that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into
 * JFace that allows us to create a page that is small and knows how to save, restore and apply
 * itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */
public class MxObjectPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private boolean triggerOffChanged;

    public MxObjectPreference() {
        super(FieldEditorPreferencePage.GRID);
        setPreferenceStore(MxEclipsePlugin.getDefault().getPreferenceStore());
    }

    /**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
	 * manipulate various types of preferences. Each field editor knows how to save and restore
	 * itself.
	 */
    public void createFieldEditors() {
        addField(new BooleanFieldEditor(PreferenceConstants.TRIGGER_OFF, "Global trigger off", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.OBJECT_SEARCH_LIMIT, "Object Search Limit (0 for unlimitted):", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.HISTORY_LIMIT, "History Limit (0 for unlimitted):", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

    protected IPreferenceStore doGetPreferenceStore() {
        return super.doGetPreferenceStore();
    }

    /**
	 *
	 * If trigger on/off checkbox value is changed, execute trigger on/off command
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
	 */
    @Override
    public boolean performOk() {
        boolean retVal = super.performOk();
        if (triggerOffChanged) {
            try {
                MxEclipseUtils.triggerOnOff();
            } catch (MatrixException e) {
                Status status = new Status(IStatus.ERROR, "MxEclipse", 0, e.getMessage(), e);
                ErrorDialog.openError(null, "Error when creating actions", e.getMessage(), status);
                return false;
            }
        }
        return retVal;
    }

    /**
	 * Mark trigger on/off checkbox as changed
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
    }
}
