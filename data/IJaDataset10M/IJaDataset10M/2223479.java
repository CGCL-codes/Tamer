package net.sf.joafip.store.entity.export_import.in;

import net.sf.joafip.store.entity.classinfo.ClassInfo;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class FieldNameTypeDeleted {

    private final String fieldName;

    private final ClassInfo fieldType;

    private final boolean deletedField;

    public FieldNameTypeDeleted(final String fieldName, final ClassInfo fieldType, final boolean deletedField) {
        super();
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.deletedField = deletedField;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ClassInfo getFieldType() {
        return fieldType;
    }

    public boolean isDeletedField() {
        return deletedField;
    }
}
