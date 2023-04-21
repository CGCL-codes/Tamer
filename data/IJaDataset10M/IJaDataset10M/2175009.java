package ftm;

import sao.DML.*;

public class MatchTableField {

    private DMLTable m_baseTable;

    private DMLField m_baseField;

    private DMLTable m_childTable;

    private DMLField m_childField;

    public MatchTableField(DMLTable baseTable, DMLField baseField, DMLTable childTable, DMLField childField) {
        m_baseTable = baseTable;
        m_baseField = baseField;
        m_childTable = childTable;
        m_childField = childField;
    }

    public String toString() {
        if (m_baseTable == null || m_baseField == null || m_childTable == null || m_childField == null) return ("none.none << none.none");
        return (m_baseTable.getTableName() + "." + m_baseField.getFieldName() + " << " + m_childTable.getTableName() + "." + m_childField.getFieldName());
    }

    public DMLTable getBaseTable() {
        return (m_baseTable);
    }

    public DMLField getBaseField() {
        return (m_baseField);
    }

    public DMLTable getChildTable() {
        return (m_childTable);
    }

    public DMLField getChildField() {
        return (m_childField);
    }
}
