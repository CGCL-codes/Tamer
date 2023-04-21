package leesoft.toolbox.export.templates;

public class Field {

    String field;

    String fieldName;

    public Field(String field, String fieldName) {
        this.field = field;
        this.fieldName = fieldName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
