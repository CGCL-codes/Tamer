package com.vircon.myajax.css;

import org.json.JSONException;
import org.json.JSONObject;
import com.vircon.myajax.web.event.ComponentCommand;

@SuppressWarnings("serial")
public class StyleCommand extends ComponentCommand {

    public StyleCommand(StyleChangeEvent event) {
        super(STYLE_CHANGE);
        setOperation(event.getOperation());
        setProperty(event.getObjectRef());
    }

    @Override
    protected void fillJSONObject(JSONObject jsObject) throws JSONException {
        jsObject.put("operation", getOperation());
        jsObject.put("propertyName", getProperty().getName());
        jsObject.put("propertyValue", getProperty().getValue());
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String aOperation) {
        operation = aOperation;
    }

    public StyleProperty getProperty() {
        return property;
    }

    public void setProperty(StyleProperty aProperty) {
        property = aProperty;
    }

    private String operation;

    private StyleProperty property;
}
