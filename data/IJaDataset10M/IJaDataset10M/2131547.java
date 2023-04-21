package org.plazmaforge.framework.datawarehouse.transfer;

public class ColumnMapper {

    private ColumnDef sourceColumn;

    private ColumnDef targetColumn;

    private String value;

    private String nullValue;

    private String expValue;

    public ColumnDef getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(ColumnDef sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public ColumnDef getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(ColumnDef targetColumn) {
        this.targetColumn = targetColumn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public String getExpValue() {
        return expValue;
    }

    public void setExpValue(String expValue) {
        this.expValue = expValue;
    }
}
