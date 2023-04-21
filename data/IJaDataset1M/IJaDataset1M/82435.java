package net.sourceforge.notepod.view.swing.action;

import javax.swing.JComboBox;
import net.sourceforge.notepod.ctrl.action.ControllerAction;
import net.sourceforge.notepod.log.LogSingleton;
import net.sourceforge.notepod.model.DirectoryInterface;

public class SwingAction_UpdateModelFromComboBox extends ControllerAction {

    JComboBox comboBox;

    public SwingAction_UpdateModelFromComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public void action() {
        int index = this.comboBox.getSelectedIndex();
        LogSingleton.getInstance().debug("combo index : " + index);
        if (index < 0) {
            return;
        }
        DirectoryInterface selectedDirectory = this.getController().getModel().getRootDirectory().getDirectoryByIndex(index);
        if (selectedDirectory != null) {
            this.getController().getModel().setCurrentSelectedDirectory(selectedDirectory);
        }
    }
}
