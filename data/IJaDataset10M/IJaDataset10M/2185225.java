package search.example.srch.impl;

import java.util.HashMap;
import java.util.Map;
import search.example.srch.Document;
import search.example.srch.Field;
import search.example.util.StringConstants;

public class DocumentImpl implements Document {

    private Map<String, Field> fields = new HashMap<String, Field>();

    public void add(Field field) {
        fields.put(field.getName(), field);
    }

    public void addText(String name, String value) {
        if (value != null) {
            fields.put(name, new Field(name, value, true));
        }
    }

    public String get(String name) {
        Field field = fields.get(name);
        if (field == null) {
            return StringConstants.BLANK;
        }
        return field.getValue();
    }

    public String[] getValues(String name) {
        Field field = fields.get(name);
        if (field == null) {
            return new String[] { StringConstants.BLANK };
        }
        return field.getValues();
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public void setFields(Map<String, Field> fields) {
        this.fields = fields;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringConstants.OPEN_CURLY_BRACE);
        int i = 0;
        for (Field field : fields.values()) {
            if (i > 0) {
                sb.append(StringConstants.COMMA);
                sb.append(StringConstants.SPACE);
            }
            sb.append(field.getName());
            sb.append(StringConstants.EQUAL);
            sb.append(field.getValue());
        }
        sb.append(StringConstants.CLOSE_CURLY_BRACE);
        return sb.toString();
    }
}
