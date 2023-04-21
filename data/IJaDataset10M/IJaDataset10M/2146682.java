package com.ivis.xprocess.framework.exceptions;

import com.ivis.xprocess.core.properties.CoreMsg;

public class MoveException extends Exception {

    private static final long serialVersionUID = -4922560445317971565L;

    public enum Reason {

        OFFLINE_VCS_MOVE_ATTEMPTED(CoreMsg.offline_vcs_move_attempted), ELEMENT_ALREADY_MOVED(CoreMsg.element_already_moved), CANT_MOVE_XCHANGEELEMENTCONTAINERS_INTO_NON_CONTAINERCONTAINER(""), CANT_MOVE_XCHANGEELEMENTCONTAINERS(""), XCHANGEELEMENTS_CAN_ONLY_MOVE_TO_XCHANGEELEMENTCONTAINERS(""), CANT_MOVE_TASK_TO_PROJECT_PATTERN_ROOT(""), OTHER(""), CANT_MOVE_FOLDER_TO_THIS_TARGET("Cannot move the folder to this target.");

        private String message;

        Reason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private Reason reason;

    public MoveException(Reason reason, Throwable cause) {
        super(cause);
        this.reason = reason;
    }

    public MoveException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        if (getReason() == Reason.OTHER) {
            return getCause().getMessage();
        }
        return getReason().getMessage();
    }
}
