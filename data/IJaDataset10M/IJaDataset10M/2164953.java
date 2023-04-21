package com.centraview.report.valueobject;

public class WhereClauseVO {

    private String subRelationshipQuery;

    private String tableName;

    private String fieldName;

    private String displayName;

    private int sequenceNumber;

    private int searchTableId;

    private String isOnTable;

    private String realTableName;

    private String relationshipQuery;

    private int tableId;

    private int fieldId;

    public WhereClauseVO() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIsOnTable() {
        return isOnTable;
    }

    public String getRelationshipQuery() {
        return relationshipQuery;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getTableName() {
        return tableName;
    }

    /**
   * @return subRelationshipQuery Query Condition.
   */
    public String getSubRelationShipQuery() {
        return this.subRelationshipQuery;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIsOnTable(String isOnTable) {
        this.isOnTable = isOnTable;
    }

    public void setRealTableName(String realTableName) {
        this.realTableName = realTableName;
    }

    public void setRelationshipQuery(String relationshipQuery) {
        this.relationshipQuery = relationshipQuery;
    }

    public void setSearchTableId(int searchTableId) {
        this.searchTableId = searchTableId;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRealTableName() {
        return realTableName;
    }

    public int getSearchTableId() {
        return searchTableId;
    }

    /**
   * @return Returns the fieldId.
   */
    public int getFieldId() {
        return this.fieldId;
    }

    /**
   * @param fieldId The fieldId to set.
   */
    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    /**
   * @return Returns the tableId.
   */
    public int getTableId() {
        return this.tableId;
    }

    /**
   * @param tableId The tableId to set.
   */
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    /**
   * @param subRelationshipQuery The subRelationshipQuery to set.
   */
    public void setSubRelationShipQuery(String subRelationshipQuery) {
        this.subRelationshipQuery = subRelationshipQuery;
    }
}
