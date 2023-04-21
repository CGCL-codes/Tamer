package net.sf.sql2java.common.conversion;

/**
 *
 * @author dge2
 */
public class DataType {

    private String sqlType;

    private String javaType;

    public DataType(String sqlType, String javaType) {
        this.sqlType = sqlType;
        this.javaType = javaType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }
}
