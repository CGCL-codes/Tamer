package org.openXpertya.dbPort;

import org.openXpertya.util.Util;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class Join {

    /**
     * Constructor de la clase ...
     *
     *
     * @param joinClause
     */
    public Join(String joinClause) {
        if (joinClause == null) {
            throw new IllegalArgumentException("Join - clause cannot be null");
        }
        evaluate(joinClause);
    }

    /** Descripción de Campos */
    private String m_joinClause;

    /** Descripción de Campos */
    private String m_mainTable;

    /** Descripción de Campos */
    private String m_mainAlias;

    /** Descripción de Campos */
    private String m_joinTable;

    /** Descripción de Campos */
    private String m_joinAlias;

    /** Descripción de Campos */
    private boolean m_left;

    /** Descripción de Campos */
    private String m_condition;

    /**
     * Descripción de Método
     *
     *
     * @param joinClause
     */
    private void evaluate(String joinClause) {
        m_joinClause = joinClause;
        int indexEqual = joinClause.indexOf('=');
        m_left = indexEqual < joinClause.indexOf("(+)");
        if (m_left) {
            m_mainAlias = joinClause.substring(0, Util.findIndexOf(joinClause, '.', '=')).trim();
            int end = joinClause.indexOf('.', indexEqual);
            if (end == -1) {
                end = joinClause.indexOf('(', indexEqual);
            }
            m_joinAlias = joinClause.substring(indexEqual + 1, end).trim();
        } else {
            int end = joinClause.indexOf('.', indexEqual);
            if (end == -1) {
                end = joinClause.length();
            }
            m_mainAlias = joinClause.substring(indexEqual + 1, end).trim();
            m_joinAlias = joinClause.substring(0, Util.findIndexOf(joinClause, '.', '(')).trim();
        }
        m_condition = Util.replace(joinClause, "(+)", "").trim();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getJoinClause() {
        return m_joinClause;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getMainAlias() {
        return m_mainAlias;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getJoinAlias() {
        return m_joinAlias;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isLeft() {
        return m_left;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getCondition() {
        return m_condition;
    }

    /**
     * Descripción de Método
     *
     *
     * @param mainTable
     */
    public void setMainTable(String mainTable) {
        if ((mainTable == null) || (mainTable.length() == 0)) {
            return;
        }
        m_mainTable = mainTable;
        if (m_mainAlias.equals(mainTable)) {
            m_mainAlias = "";
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getMainTable() {
        return m_mainTable;
    }

    /**
     * Descripción de Método
     *
     *
     * @param joinTable
     */
    public void setJoinTable(String joinTable) {
        if ((joinTable == null) || (joinTable.length() == 0)) {
            return;
        }
        m_joinTable = joinTable;
        if (m_joinAlias.equals(joinTable)) {
            m_joinAlias = "";
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getJoinTable() {
        return m_joinTable;
    }

    /**
     * Descripción de Método
     *
     *
     * @param first
     *
     * @return
     */
    public boolean isConditionOf(Join first) {
        if ((m_mainTable == null) && (first.getJoinTable().equals(m_joinTable) || first.getMainAlias().equals(m_joinTable))) {
            return true;
        }
        return false;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("Join[");
        sb.append(m_joinClause).append(" - Main=").append(m_mainTable).append("/").append(m_mainAlias).append(", Join=").append(m_joinTable).append("/").append(m_joinAlias).append(", Left=").append(m_left).append(", Condition=").append(m_condition).append("]");
        return sb.toString();
    }
}
