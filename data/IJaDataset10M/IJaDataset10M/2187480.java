package com.googlecode.g2re.jdbc;

/**
 *
 * @author Brad
 */
public class PreparedStatementParameter {

    private int position;

    private Object value;

    private int sqlType;

    public PreparedStatementParameter() {
    }

    public PreparedStatementParameter(int position, Object value, int sqlType) {
        this.position = position;
        this.value = value;
        this.sqlType = sqlType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
