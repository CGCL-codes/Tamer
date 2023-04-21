package at.HexLib.GUI.Listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import at.HexLib.GUI.gui.AutoCompleter;
import at.HexLib.GUI.main.HexEditSample;
import at.HexLib.GUI.main.HexLibContainer;
import at.HexLib.library.HexLib;

public class PropertiesListener implements PropertyChangeListener {

    HexEditSample hexParent = null;

    private HexLibContainer hexLibContainer;

    public PropertiesListener(HexEditSample hexEditorSample) {
        hexParent = hexEditorSample;
    }

    public PropertiesListener(HexLibContainer hexLibContainer) {
        this.hexLibContainer = hexLibContainer;
        hexParent = HexEditSample.masterReference;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (hexLibContainer == null) {
            if (propertyName.equals(AutoCompleter.ENTER_RELEASED)) {
                hexParent.btnLoad.doClick();
            }
        } else if (hexLibContainer != null) {
            if (propertyName.equals(HexLib.cursorProperty)) {
                hexParent.pnlCursor.setCursorPosIntoTextField();
            } else if (propertyName.equals(HexLib.contentChangedProperty)) {
                if (!hexParent.btnSave.isEnabled() && hexParent.hexEditor.isContentChanged()) {
                    hexParent.btnSave.setEnabled(true);
                }
                hexParent.hexEditor.updateChangeStatus();
            }
        }
    }
}
