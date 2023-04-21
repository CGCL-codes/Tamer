package avm;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class TextboxAdapter implements WidgetAdapter {

    static {
        AVMEngine.getAdapterForWidgetType().put("textbox", new TextboxAdapter());
        AVMEngine.getWidgetTypeForComponentType().put(JTextArea.class, "textbox");
    }

    @Override
    public JComponent New() {
        return new JTextArea();
    }

    @Override
    public String[] getAllPropertyNames() {
        String[] propertyNames = new String[] { "content", "editable", "background", "foreground", "font" };
        return propertyNames;
    }

    @Override
    public String getPropertyType(String propertyName) {
        String type = null;
        if (propertyName.equals("content")) {
            type = "String";
        } else if (propertyName.equals("editable")) {
            type = "boolean";
        } else if (propertyName.equals("background")) {
            type = "Color";
        } else if (propertyName.equals("foreground")) {
            type = "Color";
        } else if (propertyName.equals("font")) {
            type = "Font";
        } else {
        }
        return type;
    }

    @Override
    public Object getProperty(JComponent widget, String propertyName) {
        Object value = null;
        JTextArea textArea = (JTextArea) widget;
        if (propertyName.equals("content")) {
            value = textArea.getText();
        } else if (propertyName.equals("editable")) {
            value = textArea.isEditable();
        } else if (propertyName.equals("background")) {
            value = textArea.getBackground();
        } else if (propertyName.equals("foreground")) {
            value = textArea.getForeground();
        } else if (propertyName.equals("font")) {
            value = textArea.getFont();
        } else {
        }
        return value;
    }

    @Override
    public void setProperty(JComponent widget, String propertyName, Object value) {
        JTextArea textArea = (JTextArea) widget;
        if (propertyName.equals("content")) {
            textArea.setText((String) value);
        } else if (propertyName.equals("editable")) {
            textArea.setEditable((Boolean) value);
        } else if (propertyName.equals("background")) {
            textArea.setBackground((Color) value);
        } else if (propertyName.equals("foreground")) {
            textArea.setForeground((Color) value);
        } else if (propertyName.equals("font")) {
            textArea.setFont((Font) value);
        } else {
        }
    }

    @Override
    public Object getPropertyDefaultValue(String propertyName) {
        Object defaultValue = null;
        if (propertyName.equals("content")) {
            defaultValue = "";
        } else if (propertyName.equals("editable")) {
            defaultValue = true;
        } else if (propertyName.equals("background")) {
            defaultValue = Color.WHITE;
        } else if (propertyName.equals("foreground")) {
            defaultValue = Color.BLACK;
        } else if (propertyName.equals("font")) {
            defaultValue = new Font("Arial", Font.PLAIN, 12);
        } else {
        }
        return defaultValue;
    }

    @Override
    public boolean hasPropertyDefaultValue(JComponent widget, String propertyName) {
        Object value = getProperty(widget, propertyName);
        Object defaultValue = getPropertyDefaultValue(propertyName);
        if (value == null && defaultValue == null) {
            return true;
        } else if (value != null && value.equals(defaultValue)) {
            return true;
        }
        return false;
    }

    @Override
    public void resetPropertyToDefaultValue(JComponent widget, String propertyName) {
        setProperty(widget, propertyName, getPropertyDefaultValue(propertyName));
    }

    @Override
    public Object getValue(JComponent widget) {
        return getProperty(widget, "content");
    }

    @Override
    public void setValue(JComponent widget, Object value) {
        setProperty(widget, "content", value);
    }
}
