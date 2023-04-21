package jtk.project4.fleet.domain;

import java.util.Date;

public class RbField {

    private String tableName;

    private String fieldName;

    private String fieldAlias;

    private Character selectable;

    private Character searchable;

    private Character sortable;

    private Character visible;

    private String dataType;

    private Character autoSearch;

    private Character mandatory;

    private Character lookUp;

    private String lookUpTable;

    private String lookUpFieldAlias;

    private String lookUpField;

    private Date createDate;

    private Integer createId;

    private Date modifyDate;

    private Integer modifyId;

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

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public Character getSelectable() {
        return selectable;
    }

    public void setSelectable(Character selectable) {
        this.selectable = selectable;
    }

    public Character getSearchable() {
        return searchable;
    }

    public void setSearchable(Character searchable) {
        this.searchable = searchable;
    }

    public Character getSortable() {
        return sortable;
    }

    public void setSortable(Character sortable) {
        this.sortable = sortable;
    }

    public Character getVisible() {
        return visible;
    }

    public void setVisible(Character visible) {
        this.visible = visible;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Character getAutoSearch() {
        return autoSearch;
    }

    public void setAutoSearch(Character autoSearch) {
        this.autoSearch = autoSearch;
    }

    public Character getMandatory() {
        return mandatory;
    }

    public void setMandatory(Character mandatory) {
        this.mandatory = mandatory;
    }

    public Character getLookUp() {
        return lookUp;
    }

    public void setLookUp(Character lookUp) {
        this.lookUp = lookUp;
    }

    public String getLookUpTable() {
        return lookUpTable;
    }

    public void setLookUpTable(String lookUpTable) {
        this.lookUpTable = lookUpTable;
    }

    public String getLookUpFieldAlias() {
        return lookUpFieldAlias;
    }

    public void setLookUpFieldAlias(String lookUpFieldAlias) {
        this.lookUpFieldAlias = lookUpFieldAlias;
    }

    public String getLookUpField() {
        return lookUpField;
    }

    public void setLookUpField(String lookUpField) {
        this.lookUpField = lookUpField;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getModifyId() {
        return modifyId;
    }

    public void setModifyId(Integer modifyId) {
        this.modifyId = modifyId;
    }
}
