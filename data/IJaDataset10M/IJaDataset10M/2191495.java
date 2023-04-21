package net.sf.easyweb4j.model.converters.number;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

public class PrimitiveConverter extends AbstractNumberConverter {

    private Class<? extends Number> type;

    public PrimitiveConverter(Class<? extends Number> type) {
        super(type);
        this.type = type;
    }

    @Override
    protected Number convert(String value) {
        PropertyEditor editor = PropertyEditorManager.findEditor(type);
        editor.setAsText(value);
        return (Number) editor.getValue();
    }
}
