package org.opennms.netmgt.model;

import java.util.Set;

/**
 * Really a container class for persisting arrangements of status definitions
 * created by the user.
 * 
 * Perhaps a new package called model.config is in order.
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 *
 */
public class AggregateStatusView {

    private Integer m_id;

    private String m_name;

    private String m_tableName;

    private String m_columnName;

    private String m_columnValue;

    private Set<AggregateStatusDefinition> m_statusDefinitions;

    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getTableName() {
        return m_tableName;
    }

    public void setTableName(String tableName) {
        m_tableName = tableName;
    }

    public String getColumnName() {
        return m_columnName;
    }

    public void setColumnName(String columnName) {
        m_columnName = columnName;
    }

    public String getColumnValue() {
        return m_columnValue;
    }

    public void setColumnValue(String columnValue) {
        m_columnValue = columnValue;
    }

    public Set<AggregateStatusDefinition> getStatusDefinitions() {
        return m_statusDefinitions;
    }

    public void setStatusDefinitions(Set<AggregateStatusDefinition> statusDefinitions) {
        m_statusDefinitions = statusDefinitions;
    }

    /**
     * Good for debug logs and viewing in a debugger.
     */
    public String toString() {
        StringBuffer result = new StringBuffer(50);
        result.append("AggregateStatusView { id: ");
        result.append(m_id);
        result.append(", name: ");
        result.append(m_name);
        result.append(", tableName: ");
        result.append(m_tableName);
        result.append(", columnName: ");
        result.append(m_columnName);
        result.append(", columnValue: ");
        result.append(m_columnValue);
        result.append(" }");
        return result.toString();
    }
}
