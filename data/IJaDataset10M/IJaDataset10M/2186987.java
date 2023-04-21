package org.richfaces.tarkus.fmk.gui.filter;

public class Filter {

    private String columnName;

    private String columnLabel;

    private String operator;

    private Object dataValue;

    private String displayValue;

    /**
   * Filter
   */
    public Filter() {
        super();
    }

    /**
   * TODO gjaboulay Add a comment
   * @param columnName
   * @param columnLabel
   * @param operator
   * @param dataValue
   * @param displayValue
   */
    public Filter(String columnName, String columnLabel, String operator, Object dataValue, String displayValue) {
        super();
        this.columnName = columnName;
        this.columnLabel = columnLabel;
        this.operator = operator;
        this.dataValue = dataValue;
        this.displayValue = displayValue;
    }

    /**
   * copy
   * @return Filter
   */
    public Filter copy() {
        Filter filter = new Filter(columnName, columnLabel, operator, dataValue, displayValue);
        return filter;
    }

    /**
   * @return the column name
   */
    public String getColumnName() {
        return columnName;
    }

    /**
   * @param columnName the column name to set
   */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
   * @return the data value
   */
    public Object getDataValue() {
        return dataValue;
    }

    /**
   * @param dataValue the data value to set
   */
    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    /**
   * @return the operator
   */
    public String getOperator() {
        return operator;
    }

    /**
   * @param operator the operator to set
   */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
   * @return the display value
   */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
   * @param displayValue the display value to set
   */
    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
   * @return the column label
   */
    public String getColumnLabel() {
        return columnLabel;
    }

    /**
   * @param columnLabel the column label to set
   */
    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }
}
