package com.openbravo.data.loader.query;

public class LinkQBFParameter {

    private String tableName;

    private String fieldName;

    private String alise;

    private QBFParameter qbfParameter;

    public LinkQBFParameter(String tableName, String fieldName) {
        this.tableName = tableName;
        this.fieldName = fieldName;
    }

    public String getAlise() {
        return alise;
    }

    public void setAlise(String alise) {
        this.alise = alise;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public QBFParameter getQbfParameter() {
        return qbfParameter;
    }

    public void setQbfParameter(QBFParameter qbfParameter) {
        this.qbfParameter = qbfParameter;
    }
}
