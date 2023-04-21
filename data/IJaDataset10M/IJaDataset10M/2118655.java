package org.opennms.netmgt.snmp.mock;

public class AgentIndexException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int m_errorStatus;

    private int m_errorIndex;

    public AgentIndexException(int errorStatus, int errorIndex) {
        m_errorStatus = errorStatus;
        m_errorIndex = errorIndex;
    }

    public int getErrorStatus() {
        return m_errorStatus;
    }

    public int getErrorIndex() {
        return m_errorIndex;
    }
}
