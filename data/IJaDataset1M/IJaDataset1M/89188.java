package edu.whitman.halfway.jigs.gui.desque;

import java.util.EventObject;

public class FieldEvent extends EventObject {

    private String fieldKey;

    public FieldEvent(FieldPanelComponent source, String fieldKey) {
        super(source);
        this.fieldKey = fieldKey;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public FieldPanelComponent getComponent() {
        return (FieldPanelComponent) getSource();
    }
}
