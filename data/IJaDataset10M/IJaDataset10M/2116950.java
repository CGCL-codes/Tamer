package com.extjs.serverside.form;

import java.util.HashMap;
import java.util.Map;

public class Component implements Renderable {

    String javascriptClass;

    String fieldName;

    Map<String, Object> values = new HashMap<String, Object>();

    public Component(String javascriptClass) {
        super();
        this.javascriptClass = javascriptClass;
    }

    public Component(String javascriptClass, String fieldName) {
        super();
        this.javascriptClass = javascriptClass;
        this.fieldName = fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setValue(String key, Object obj) {
        values.put(key, obj);
    }

    protected String renderJavascriptConstructor() {
        StringBuilder s = new StringBuilder();
        s.append("var " + getFieldName() + " = new " + javascriptClass + "(" + renderConstructorParameters() + "{\n");
        for (String key : values.keySet()) {
            s.append("    " + key + " : " + renderValue(values.get(key)) + ",\n");
        }
        s.append("});\n");
        return s.toString();
    }

    public String renderComponent() {
        return renderJavascriptConstructor();
    }

    public String renderAddToContainer(RenderableContainer form) {
        return "";
    }

    public String renderConstructorParameters() {
        return "";
    }

    private String renderValue(Object object) {
        if (object instanceof Number) {
            return object.toString();
        }
        if (object instanceof Boolean) {
            return object.toString();
        }
        if (object instanceof String) {
            return "'" + ((String) object).replace("'", "\\'") + "'";
        }
        return "'unknown type :" + object + "'";
    }
}
