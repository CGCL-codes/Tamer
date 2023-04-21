package org.slasoi.businessManager.common.ws.types;

import java.util.Date;

public class ActionType {

    private String correctiveAction;

    private Date time;

    public String getCorrectiveAction() {
        return correctiveAction;
    }

    public void setCorrectiveAction(String correctiveAction) {
        this.correctiveAction = correctiveAction;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
