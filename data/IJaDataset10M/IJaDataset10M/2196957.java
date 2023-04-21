package dk.aau.hidenets.te.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;

public class GeneralPrefsEditor extends FieldEditorPreferencePage {

    private IntegerFieldEditor numDevices;

    public GeneralPrefsEditor() {
        super(FLAT);
        setTitle("General");
        setDescription("General preferences for the topology emulator. Also includes preferences global to multiple parts of the emulator.");
    }

    @Override
    protected void createFieldEditors() {
        numDevices = new IntegerFieldEditor(Prefs.GENERAL_NUM_DEVICES, "Number of devices:", getFieldEditorParent());
        numDevices.setValidRange(2, 20);
        addField(numDevices);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (event.getSource().equals(numDevices)) {
            setMessage("Restart required.", WARNING);
        }
    }
}
