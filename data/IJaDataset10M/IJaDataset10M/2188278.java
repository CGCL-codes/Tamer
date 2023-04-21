package net.sf.joafip.store.entity.conversion;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ReplacementFieldDef {

    private final String fieldName;

    private final String fieldClass;

    public ReplacementFieldDef(final String fieldName, final String fieldClass) {
        super();
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldClass() {
        return fieldClass;
    }
}
