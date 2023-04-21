package org.opennms.dashboard.client;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class Alarm implements IsSerializable {

    private String m_logMsg;

    private String m_descrption;

    private String m_severity;

    private int m_count;

    private String m_nodeLabel;

    private int m_nodeId;

    private String m_ipAddress;

    private String m_svcName;

    private Date m_firstEventTime;

    private Date m_lastEventTime;

    private boolean m_isDashboardRole;

    public Alarm() {
    }

    public Alarm(String severity, String nodeLabel, int nodeId, boolean isDashboardRole, String logMsg, String description, int count, Date firstEventTime, Date lastEventTime) {
        m_severity = severity;
        m_nodeLabel = nodeLabel;
        m_nodeId = nodeId;
        m_isDashboardRole = isDashboardRole;
        m_logMsg = logMsg;
        m_descrption = description;
        m_count = count;
        m_firstEventTime = firstEventTime;
        m_lastEventTime = lastEventTime;
    }

    public int getCount() {
        return m_count;
    }

    public void setCount(int count) {
        m_count = count;
    }

    public String getDescrption() {
        return m_descrption;
    }

    public void setDescrption(String descrption) {
        m_descrption = descrption;
    }

    public String getIpAddress() {
        return m_ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        m_ipAddress = ipAddress;
    }

    public int getNodeId() {
        return m_nodeId;
    }

    public void setNodeId(int nodeId) {
        m_nodeId = nodeId;
    }

    public String getNodeLabel() {
        return m_nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        m_nodeLabel = nodeLabel;
    }

    public String getSeverity() {
        return m_severity;
    }

    public void setSeverity(String severity) {
        m_severity = severity;
    }

    public String getSvcName() {
        return m_svcName;
    }

    public void setSvcName(String svcName) {
        m_svcName = svcName;
    }

    public Date getFirstEventTime() {
        return m_firstEventTime;
    }

    public void setFirstEventTime(Date firstEventTime) {
        m_firstEventTime = firstEventTime;
    }

    public Date getLastEventTime() {
        return m_lastEventTime;
    }

    public void setLastEventTime(Date lastEventTime) {
        m_lastEventTime = lastEventTime;
    }

    public String getLogMsg() {
        return m_logMsg;
    }

    public void setLogMsg(String logMsg) {
        m_logMsg = logMsg;
    }

    public void setIsDashboardRole(boolean isDashboardRole) {
        m_isDashboardRole = isDashboardRole;
    }

    public boolean getIsDashboardRole() {
        return m_isDashboardRole;
    }
}
