package net.comtor.dao;

/**
 *
 * @author dwin
 */
public class ComtorJDBCForeingField extends ComtorJDBCField {

    private Class referencesClass;

    private String foreingColumn;

    private String referencesColumn[];

    private ComtorJDBCDaoDescriptor descriptor;

    /**
     * 
     * @param attributeName
     * @param columnName
     * @param type
     * @param getMethod
     * @param setMethod
     */
    public ComtorJDBCForeingField(Class clazz, String attributeName, String columnName, boolean isBoolean) {
        super(clazz, attributeName, columnName, isBoolean);
    }

    public String getForeingColumn() {
        return foreingColumn;
    }

    public void setForeingColumn(String foreingColumn) {
        this.foreingColumn = foreingColumn;
    }

    public Class getReferencesClass() {
        return referencesClass;
    }

    public void setReferencesClass(Class referencesClass) {
        this.referencesClass = referencesClass;
    }

    public String[] getReferencesColumn() {
        return referencesColumn;
    }

    public void setReferencesColumn(String referencesColumn[]) {
        this.referencesColumn = referencesColumn;
    }

    public ComtorJDBCDaoDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(ComtorJDBCDaoDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}
