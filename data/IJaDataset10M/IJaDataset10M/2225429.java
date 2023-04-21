package net.sourceforge.notepod.ctrl.action;

import net.sourceforge.notepod.log.LogSingleton;

public class ControllerAction_SaveToLocal extends ControllerAction {

    @Override
    public void action() {
        if (this.getController().askConfirmation("Do you want to save to a file ?")) {
            LogSingleton.getInstance().debug("Action : save local file...");
            this.getController().getLocalDao().save(this.getController().getModel().getRootDirectory());
        }
    }
}
