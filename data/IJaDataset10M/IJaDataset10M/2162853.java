package com.gwtent.client.ui.model.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.gwtent.client.ui.model.Field;
import com.gwtent.client.ui.model.Fields;
import com.gwtent.client.ui.model.Value;

public class FieldsImpl implements Fields {

    private String caption;

    private List<Field> fields;

    public FieldsImpl() {
        fields = new ArrayList<Field>();
    }

    public Field addFieldInfo(String fieldName, String caption, boolean require, String desc, Value value) {
        Field info = new FieldImpl(caption, require, desc, value);
        addField(info);
        return info;
    }

    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
