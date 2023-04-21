package com.informa.unitils.datasetfactories.valuemappers;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

public class PropertyEditorMapper extends PropertyEditorSupport implements ValueMapper {

    private PropertyEditor propertyEditor;

    private String prefix;

    public PropertyEditorMapper(String prefix, Class<? extends PropertyEditor> propertyEditorClass) {
        try {
            this.propertyEditor = propertyEditorClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PropertyEditorMapper(String prefix, PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
        this.prefix = prefix;
    }

    public boolean handles(String value) {
        return value.startsWith(prefix);
    }

    public Object map(String value) {
        propertyEditor.setAsText(value);
        return propertyEditor.getValue();
    }
}
