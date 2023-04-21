package org.obe.server.j2ee.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.OBERuntimeException;
import org.obe.client.api.repository.RepositoryException;
import org.obe.server.j2ee.ejb.AbstractEntityEJB;
import org.obe.server.util.ObjectUtil;
import org.obe.spi.service.ServerConfig;
import org.wfmc.audit.WMAAuditEntry;
import org.wfmc.audit.WMAEventCode;
import org.wfmc.wapi.WMFilter;
import org.wfmc.wapi.WMProcessInstanceState;
import javax.ejb.CreateException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Used to perform non-transactional audit logging.
 *
 * @author Adrian Price
 * @ejb:bean type="CMP"
 * cmp-version="2.x"
 * name="AuditEntry"
 * display-name="OBE Audit Entry"
 * local-jndi-name="org/obe/ejb/AuditEntryLocal"
 * primkey-field="auditId"
 * reentrant="False"
 * schema="AUDITENTRY"
 * transaction-type="Container"
 * view-type="local"
 * @ejb:home local-class="org.obe.server.j2ee.audit.AuditEntryLocalHome"
 * local-extends="javax.ejb.EJBLocalHome"
 * @ejb:interface local-class="org.obe.server.j2ee.audit.AuditEntryLocal"
 * local-extends="javax.ejb.EJBLocalObject"
 * @ejb:persistence table-name="OBEAUDITENTRY"
 * @ejb:pk class="java.lang.Long"
 * unchecked="true"
 * @ejb:permission unchecked="true"
 * @ejb:transaction type="Supports"
 * @weblogic:transaction-isolation ${transaction.isolation}
 * @ejb:resource-ref res-name="jdbc/TxDataSource"
 * res-type="javax.sql.DataSource"
 * res-auth="Container"
 * @jboss:resource-manager res-man-class="javax.sql.DataSource"
 * res-man-name="jdbc/TxDataSource"
 * res-man-jndi-name="java:/${xdoclet.DataSource}"
 * @weblogic:resource-description res-ref-name="jdbc/TxDataSource"
 * jndi-name="${xdoclet.DataSource}"
 * @weblogic:data-source-name java:comp/env/jdbc/TxDataSource
 * @jboss:create-table false
 * @weblogic:pool max-beans-in-free-pool="10"
 * initial-beans-in-free-pool="0"
 * @weblogic:cache max-beans-in-cache="10"
 * idle-timeout-seconds="0"
 * concurrency-strategy="Database"
 * @weblogic:persistence delay-updates-until-end-of-tx="True"
 * @jboss:tuned-updates tune="false"
 * @jboss:container-configuration name="${xdoclet.jboss.container-configuration}"
 * @jboss:cache-invalidation value="True"
 * @jboss:cache-invalidation-config invalidation-group-name="AuditEntry"
 */
public abstract class AuditEntryEJB extends AbstractEntityEJB {

    private static final long serialVersionUID = 6451902064891238641L;

    private static final Log _logger = LogFactory.getLog(AuditEntryEJB.class);

    public AuditEntryEJB() {
    }

    /**
     * @ejb:create-method
     */
    public Long ejbCreate(WMAAuditEntry entry) throws CreateException {
        if (_logger.isDebugEnabled() && ServerConfig.isVerbose()) {
            _logger.debug("ejbCreate(" + entry + ')');
        }
        setAuditId(new Long(AuditEntryDAO.getInstance().getNewId()));
        setProcessDefinitionId(entry.getProcessDefinitionId());
        setActivityDefinitionId(entry.getActivityDefinitionId());
        setInitialProcessInstanceId(entry.getInitialProcessInstanceId());
        setCurrentProcessInstanceId(entry.getCurrentProcessInstanceId());
        setActivityInstanceId(entry.getActivityInstanceId());
        setWorkItemId(entry.getWorkItemId());
        setProcessState(entry.getProcessState());
        setEventCode(entry.getEventCode());
        setDomainId(entry.getDomainId());
        setNodeId(entry.getNodeId());
        setUserId(entry.getUserId());
        setRoleId(entry.getRoleId());
        setTimestamp(entry.getTimestamp());
        setAuditEntry(entry);
        return null;
    }

    public void ejbPostCreate(WMAAuditEntry entry) throws CreateException {
        if (_logger.isDebugEnabled() && ServerConfig.isVerbose()) {
            _logger.debug("ejbPostCreate() pk=" + getAuditId());
        }
    }

    /**
     * Finds the audit entries matching a user-supplied criterion.
     *
     * @param filter Filter criterion.
     * @return The matching audit records.
     * @throws RepositoryException If the filter type is unsupported.
     * @ejb:home-method
     */
    public WMAAuditEntry[] ejbHomeXfindByFilter(WMFilter filter) throws RepositoryException {
        try {
            return AuditEntryDAO.getInstance().findByFilter(filter);
        } catch (SQLException e) {
            throw new OBERuntimeException(e);
        }
    }

    /**
     * Finds the audit entries matching a user-supplied criterion.
     *
     * @param filter Filter criterion.
     * @return The matching audit records.
     * @throws RepositoryException If the filter type is unsupported.
     * @ejb:home-method
     */
    public int ejbHomeDeleteByFilter(WMFilter filter) throws RepositoryException {
        try {
            return AuditEntryDAO.getInstance().deleteByFilter(filter);
        } catch (SQLException e) {
            throw new OBERuntimeException(e);
        }
    }

    /**
     * @ejb:persistence column-name="AUDITENTRYID"
     */
    public abstract Long getAuditId();

    /**
     * @ejb:persistence column-name="AUDITENTRYID"
     */
    public abstract void setAuditId(Long id);

    /**
     * @ejb:persistence column-name="PROCESSDEFINITIONID"
     */
    public abstract String getProcessDefinitionId();

    /**
     * @ejb:persistence column-name="PROCESSDEFINITIONID"
     */
    public abstract void setProcessDefinitionId(String procDefId);

    /**
     * @ejb:persistence column-name="ACTIVITYDEFINITIONID"
     */
    public abstract String getActivityDefinitionId();

    /**
     * @ejb:persistence column-name="ACTIVITYDEFINITIONID"
     */
    public abstract void setActivityDefinitionId(String actDefId);

    /**
     * @ejb:persistence column-name="INITIALPROCESSINSTANCEID"
     */
    public abstract String getInitialProcessInstanceId();

    /**
     * @ejb:persistence column-name="INITIALPROCESSINSTANCEID"
     */
    public abstract void setInitialProcessInstanceId(String initProcInstId);

    /**
     * @ejb:persistence column-name="CURRENTPROCESSINSTANCEID"
     */
    public abstract String getCurrentProcessInstanceId();

    /**
     * @ejb:persistence column-name="CURRENTPROCESSINSTANCEID"
     */
    public abstract void setCurrentProcessInstanceId(String curProcInstId);

    /**
     * @ejb:persistence column-name="ACTIVITYINSTANCEID"
     */
    public abstract String getActivityInstanceId();

    /**
     * @ejb:persistence column-name="ACTIVITYINSTANCEID"
     */
    public abstract void setActivityInstanceId(String actInstId);

    /**
     * @ejb:persistence column-name="WORKITEMID"
     */
    public abstract String getWorkItemId();

    /**
     * @ejb:persistence column-name="WORKITEMID"
     */
    public abstract void setWorkItemId(String workItemId);

    /**
     * @ejb:persistence column-name="PROCESSSTATE"
     */
    public abstract Integer getProcessStateCmp();

    /**
     * @ejb:persistence column-name="PROCESSSTATE"
     */
    public abstract void setProcessStateCmp(Integer procInstState);

    /**
     * @ejb:persistence column-name="EVENT"
     */
    public abstract int getEventCodeCmp();

    /**
     * @ejb:persistence column-name="EVENT"
     */
    public abstract void setEventCodeCmp(int eventCode);

    /**
     * @ejb:persistence column-name="DOMAINID"
     */
    public abstract String getDomainId();

    /**
     * @ejb:persistence column-name="DOMAINID"
     */
    public abstract void setDomainId(String domainId);

    /**
     * @ejb:persistence column-name="NODEID"
     */
    public abstract String getNodeId();

    /**
     * @ejb:persistence column-name="NODEID"
     */
    public abstract void setNodeId(String nodeId);

    /**
     * @ejb:persistence column-name="USERID"
     */
    public abstract String getUserId();

    /**
     * @ejb:persistence column-name="USERID"
     */
    public abstract void setUserId(String userId);

    /**
     * @ejb:persistence column-name="ROLEID"
     */
    public abstract String getRoleId();

    /**
     * @ejb:persistence column-name="ROLEID"
     */
    public abstract void setRoleId(String roleId);

    /**
     * @ejb:persistence column-name="AUDITDATE"
     */
    public abstract Date getTimestamp();

    /**
     * @ejb:persistence column-name="AUDITDATE"
     */
    public abstract void setTimestamp(Date timestamp);

    /**
     * @ejb:persistence column-name="AUDITENTRY"
     */
    public abstract byte[] getAuditEntryCmp();

    /**
     * @ejb:persistence column-name="AUDITENTRY"
     */
    public abstract void setAuditEntryCmp(byte[] serializedData);

    private void setAuditEntry(WMAAuditEntry entry) {
        try {
            setAuditEntryCmp(ObjectUtil.serializeToByteArray(entry));
        } catch (IOException e) {
            throw new OBERuntimeException(e);
        }
    }

    public String getProcessState() {
        Integer processState = getProcessStateCmp();
        return processState == null ? null : WMProcessInstanceState.valueOf(processState.intValue()).toString();
    }

    public void setProcessState(String processState) {
        setProcessStateCmp(processState == null ? null : new Integer(WMProcessInstanceState.valueOf(processState).value()));
    }

    public WMAEventCode getEventCode() {
        return WMAEventCode.valueOf(getEventCodeCmp());
    }

    public void setEventCode(WMAEventCode eventCode) {
        setEventCodeCmp((eventCode == null ? WMAEventCode.UNKNOWN : eventCode).value());
    }

    protected Log getLogger() {
        return _logger;
    }
}
