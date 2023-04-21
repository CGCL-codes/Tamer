package fr.vtt.gattieres.gcs.gui.common;

import java.util.EventObject;

public class ValidationEvent extends EventObject {

    private ExitDialogType exitType;

    public ValidationEvent(Object source, ExitDialogType type) {
        super(source);
        this.exitType = type;
    }

    public ExitDialogType getExitType() {
        return exitType;
    }
}
