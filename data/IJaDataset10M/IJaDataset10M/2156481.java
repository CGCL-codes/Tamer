package org.netbeams.dsp.management.ui;

import java.util.List;

public class ActionUI {

    private String actionWidgetID;

    private List<String> inputValues;

    public String getActionWidgetID() {
        return actionWidgetID;
    }

    public void setActionWidgetID(String actionWidgetID) {
        this.actionWidgetID = actionWidgetID;
    }

    public List<String> getInputValues() {
        return inputValues;
    }

    public void setInputValues(List<String> inputValues) {
        this.inputValues = inputValues;
    }
}
