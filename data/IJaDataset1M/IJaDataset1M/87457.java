package kr.ac.ssu.imc.durubi.report.viewer.components;

import javax.swing.*;

public class DRQCookingTableData {

    private boolean[] show = null;

    private String[] field = null;

    private String[] table = null;

    private int[] function = null;

    private String[] functionItem = null;

    private int[] sort = null;

    private String[] sortItem = null;

    private int[] condition = null;

    private String[] conditionItem = null;

    private String[] value = null;

    private int[] variable = null;

    private String[] variableItem = null;

    private int[] adjCondition = null;

    private String[] adjConditionItem = null;

    private boolean[] key = null;

    private int[] columnCountOfTable = null;

    private int columnCount;

    private int aboutXml;

    public DRQCookingTableData(int columnCount) {
        show = new boolean[columnCount];
        field = new String[columnCount];
        table = new String[columnCount];
        function = new int[columnCount];
        functionItem = new String[columnCount];
        sort = new int[columnCount];
        sortItem = new String[columnCount];
        condition = new int[columnCount];
        conditionItem = new String[columnCount];
        value = new String[columnCount];
        variable = new int[columnCount];
        variableItem = new String[columnCount];
        adjCondition = new int[columnCount];
        adjConditionItem = new String[columnCount];
        key = new boolean[columnCount];
        columnCountOfTable = new int[columnCount];
        this.columnCount = columnCount;
    }

    public void setShowField(boolean showField, int i) {
        show[i] = showField;
    }

    public void setShowField(boolean[] showField) {
        show = showField;
    }

    public boolean[] getShowField() {
        return show;
    }

    public boolean getShowField(int i) {
        return show[i];
    }

    public void setFieldField(String fieldField, int i) {
        field[i] = fieldField;
    }

    public void setFieldField(String[] fieldField) {
        field = fieldField;
    }

    public String[] getFieldField() {
        return field;
    }

    public String getFieldField(int i) {
        return field[i];
    }

    public void setTableField(String tableField, int i) {
        table[i] = tableField;
    }

    public void setTableField(String[] tableField) {
        table = tableField;
    }

    public String[] getTableField() {
        return table;
    }

    public String getTableField(int i) {
        return table[i];
    }

    public void setFunctionField(int functionField, int i) {
        function[i] = functionField;
    }

    public void setFunctionField(int[] functionField) {
        function = functionField;
    }

    public int[] getFunctionField() {
        return function;
    }

    public int getFunctionField(int i) {
        return function[i];
    }

    public void setFunctionItem(String functionField, int i) {
        functionItem[i] = functionField;
    }

    public void setFunctionItem(String[] functionField) {
        functionItem = functionField;
    }

    public String[] getFunctionItem() {
        return functionItem;
    }

    public String getFunctionItem(int i) {
        return functionItem[i];
    }

    public void setSortField(int sortField, int i) {
        sort[i] = sortField;
    }

    public void setSortField(int[] sortField) {
        sort = sortField;
    }

    public int[] getSortField() {
        return sort;
    }

    public int getSortField(int i) {
        return sort[i];
    }

    public void setSortItem(String sortField, int i) {
        sortItem[i] = sortField;
    }

    public void setSortItem(String[] sortField) {
        sortItem = sortField;
    }

    public String[] getSortItem() {
        return sortItem;
    }

    public String getSortItem(int i) {
        return sortItem[i];
    }

    public void setConditionField(int conditionField, int i) {
        condition[i] = conditionField;
    }

    public void setConditionField(int[] conditionField) {
        condition = conditionField;
    }

    public void setConditionItem(String com, int i) {
        conditionItem[i] = com;
    }

    public void setConditionItem(String[] com) {
        conditionItem = com;
    }

    public int[] getConditionField() {
        return condition;
    }

    public String[] getConditionItem() {
        return conditionItem;
    }

    public int getConditionField(int i) {
        return condition[i];
    }

    public String getConditionItem(int i) {
        return conditionItem[i];
    }

    public void setValueField(String valueField, int i) {
        value[i] = valueField;
    }

    public void setValueField(String[] valueField) {
        value = valueField;
    }

    public String[] getValueField() {
        return value;
    }

    public String getValueField(int i) {
        return value[i];
    }

    public void setVariableField(int variableField, int i) {
        variable[i] = variableField;
    }

    public void setVariableField(int[] variableField) {
        variable = variableField;
    }

    public int[] getVariableField() {
        return variable;
    }

    public int getVariableField(int i) {
        return variable[i];
    }

    public void setVariableItem(String variableField, int i) {
        variableItem[i] = variableField;
    }

    public void setVariableItem(String[] variableField) {
        variableItem = variableField;
    }

    public String[] getVariableItem() {
        return variableItem;
    }

    public String getVariableItem(int i) {
        return variableItem[i];
    }

    public void setAdjConditionField(int adjConditionField, int i) {
        adjCondition[i] = adjConditionField;
    }

    public void setAdjConditionField(int[] adjConditionField) {
        adjCondition = adjConditionField;
    }

    public void setAdjConditionItem(String com, int i) {
        adjConditionItem[i] = com;
    }

    public void setAdjConditionItem(String[] com) {
        adjConditionItem = com;
    }

    public int[] getAdjConditionField() {
        return adjCondition;
    }

    public String[] getAdjConditionItem() {
        return adjConditionItem;
    }

    public int getAdjConditionField(int i) {
        return adjCondition[i];
    }

    public String getAdjConditionItem(int i) {
        return adjConditionItem[i];
    }

    public void setKeyField(boolean keyField, int i) {
        key[i] = keyField;
    }

    public void setKeyField(boolean[] keyField) {
        key = keyField;
    }

    public boolean[] getKeyField() {
        return key;
    }

    public boolean getKeyField(int i) {
        return key[i];
    }

    public int getColumnCountOfTable(int i) {
        return columnCountOfTable[i];
    }

    public void setColumnCountOfTable(int count, int i) {
        columnCountOfTable[i] = count;
    }

    public void setColumnCountOfTable(int[] count) {
        columnCountOfTable = count;
    }

    public int getColumnCount() {
        return columnCount;
    }

    /** ���� ��ü�� ������ ��� XML ��Ҹ� �����Ѵ�.
   *  @param oDocument XML ��ť��Ʈ ��ü.
   *  @return XML ������Ʈ ��ü.
   */
    public org.w3c.dom.Element createElementNode(org.w3c.dom.Document tDocument) {
        org.w3c.dom.Element tElement = tDocument.createElement("rdqCookingTableData");
        tElement.setAttribute("show", new Boolean(getShowField(aboutXml)).toString());
        tElement.setAttribute("field", getFieldField(aboutXml));
        tElement.setAttribute("table", getTableField(aboutXml));
        tElement.setAttribute("function", Integer.toString(getFunctionField(aboutXml)));
        tElement.setAttribute("functionItem", getFunctionItem(aboutXml));
        tElement.setAttribute("sort", Integer.toString(getSortField(aboutXml)));
        tElement.setAttribute("sortItem", getSortItem(aboutXml));
        tElement.setAttribute("condition", Integer.toString(getConditionField(aboutXml)));
        tElement.setAttribute("conditionItem", getConditionItem(aboutXml));
        tElement.setAttribute("value", getValueField(aboutXml));
        tElement.setAttribute("variable", Integer.toString(getVariableField(aboutXml)));
        tElement.setAttribute("variableItem", getVariableItem(aboutXml));
        tElement.setAttribute("adjCondition", Integer.toString(getAdjConditionField(aboutXml)));
        tElement.setAttribute("adjConditionItem", getAdjConditionItem(aboutXml));
        tElement.setAttribute("key", new Boolean(getKeyField(aboutXml)).toString());
        tElement.setAttribute("columnCountOfTable", Integer.toString(getColumnCountOfTable(aboutXml)));
        return tElement;
    }

    public void setAboutXml(int i) {
        aboutXml = i;
    }
}
