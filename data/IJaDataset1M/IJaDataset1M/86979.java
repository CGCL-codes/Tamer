package org.obe.server.j2ee.repository;

import org.apache.commons.logging.Log;
import org.obe.client.api.repository.RepositoryException;
import org.obe.server.j2ee.ejb.AbstractEntityEJB;
import org.obe.spi.service.ServerConfig;
import org.obe.xpdl.model.misc.PublicationStatus;
import org.obe.xpdl.model.misc.RedefinableHeader;
import org.obe.xpdl.model.pkg.XPDLPackage;
import org.obe.xpdl.model.workflow.ProcessHeader;
import org.obe.xpdl.model.workflow.WorkflowProcess;
import org.wfmc.wapi.WMFilter;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Holds the persistent state of a process definition.
 *
 * @author Adrian Price
 * @ejb:bean type="CMP"
 * cmp-version="2.x"
 * name="ProcessDefinition"
 * display-name="OBE Process Definition"
 * jndi-name="org/obe/ejb/ProcessDefinition"
 * local-jndi-name="org/obe/ejb/ProcessDefinitionLocal"
 * primkey-field="processDefinitionId"
 * schema="PROCESSDEFINITION"
 * reentrant="False"
 * transaction-type="Container"
 * view-type="local"
 * @ejb:persistence table-name="OBEPROCESSDEFINITION"
 * @ejb:pk class="java.lang.String"
 * unchecked="true"
 * @ejb:home local-class="org.obe.server.j2ee.repository.ProcessDefinitionLocalHome"
 * local-extends="org.obe.server.j2ee.repository.ProcessDefinitionROHome,javax.ejb.EJBLocalHome"
 * @ejb:interface local-class="org.obe.server.j2ee.repository.ProcessDefinitionLocal"
 * local-extends="org.obe.server.j2ee.repository.ProcessDefinitionRO,javax.ejb.EJBLocalObject"
 * @ejb:permission unchecked="true"
 * @ejb:transaction type="Supports"
 * @weblogic:transaction-isolation ${transaction.isolation}
 * @ejb:finder signature="java.util.Collection findAll()"
 * query="SELECT OBJECT(p) FROM PROCESSDEFINITION AS p"
 * result-type-mapping="Local"
 * unchecked="true"
 * @ejb:finder signature="java.util.Collection findByName(java.lang.String name)"
 * query="SELECT OBJECT(p) FROM PROCESSDEFINITION AS p WHERE p.name=?1"
 * result-type-mapping="Local"
 * unchecked="true"
 * @ejb:finder signature="java.util.Collection findByName(java.lang.String name, java.util.Date when)"
 * query="SELECT OBJECT(p) FROM PROCESSDEFINITION AS p WHERE p.name=?1 AND p.state=1 AND p.publicationStatus <> 'UNDER_REVISION' AND (p.validFromDate IS NULL OR p.validFromDate > ?2) AND (p.validToDate IS NULL OR p.validToDate < ?2)"
 * result-type-mapping="Local"
 * unchecked="true"
 * @ejb:ejb-ref ejb-name="XPDLPackage"
 * view-type="local"
 * @weblogic:ejb-local-reference-description ejb-ref-name="ejb/XPDLPackage"
 * jndi-name="org/obe/ejb/PackageLocal"
 * @weblogic:cache concurrency-strategy="Database"
 * @weblogic:invalidation-target ejb-name="ProcessDefinitionRO"
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
 * @weblogic:pool max-beans-in-free-pool="200"
 * initial-beans-in-free-pool="0"
 * @weblogic:cache max-beans-in-cache="200"
 * idle-timeout-seconds="0"
 * concurrency-strategy="Database"
 * @weblogic:persistence delay-updates-until-end-of-tx="True"
 * @jboss:tuned-updates tune="true"
 * @jboss:container-configuration name="${xdoclet.jboss.container-configuration}"
 * @jboss:cache-invalidation value="True"
 * @jboss:cache-invalidation-config invalidation-group-name="ProcessDefinition"
 */
public abstract class ProcessDefinitionEJB extends AbstractEntityEJB {

    private static final long serialVersionUID = 3832600769148380488L;

    protected static final Log _logger = getLogger(ProcessDefinitionEJB.class);

    public ProcessDefinitionEJB() {
    }

    /**
     * @ejb:create-method
     */
    public String ejbCreate(WorkflowProcess workflow) throws CreateException {
        if (_logger.isDebugEnabled() && ServerConfig.isVerbose()) {
            _logger.debug("ejbCreate(" + workflow + ')');
        }
        setProcessDefinitionId(workflow.getId());
        _update(workflow);
        return null;
    }

    public void ejbPostCreate(WorkflowProcess workflow) {
        if (_logger.isDebugEnabled() && ServerConfig.isVerbose()) {
            _logger.debug("ejbPostCreate(" + workflow + ')');
        }
    }

    /**
     * Counts the number of matching process definitions.
     *
     * @param filter Process definition filter; can be <code>null</code>.
     * @return The number of matching process definitions.
     * @ejb:home-method
     */
    public int ejbHomeCount(WMFilter filter) throws RepositoryException {
        try {
            return ProcessDefinitionDAO.getInstance().count(filter);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Retrieves matching process definitions.
     *
     * @param filter Process definition filter; should not be <code>null</code>
     *               (use {@link ProcessDefinitionLocalHome#findAll} instead).
     * @return A collection of matching process definitions.
     * @ejb:home-method
     * @see ProcessDefinitionLocalHome#findAll
     */
    public Collection ejbHomeXfindByFilter(WMFilter filter) throws FinderException, RepositoryException {
        try {
            List procDefs = ProcessDefinitionDAO.getInstance().findByFilter(filter);
            ProcessDefinitionLocalHome home = (ProcessDefinitionLocalHome) _ctx.getEJBLocalHome();
            for (int i = 0; i < procDefs.size(); i++) {
                try {
                    procDefs.set(i, home.findByPrimaryKey((String) procDefs.get(i)));
                } catch (ObjectNotFoundException e) {
                    procDefs.remove(i--);
                }
            }
            return procDefs;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="PACKAGEID"
     */
    public abstract String getPackageId();

    /**
     * @ejb:persistence column-name="PACKAGEID"
     */
    public abstract void setPackageId(String packageId);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="PROCESSDEFINITIONID"
     */
    public abstract String getProcessDefinitionId();

    /**
     * @ejb:persistence column-name="PROCESSDEFINITIONID"
     */
    public abstract void setProcessDefinitionId(String processDefinitionId);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="NAME"
     */
    public abstract String getName();

    /**
     * @ejb:persistence column-name="NAME"
     */
    public abstract void setName(String name);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="DESCRIPTION"
     */
    public abstract String getDescription();

    /**
     * @ejb:persistence column-name="DESCRIPTION"
     */
    public abstract void setDescription(String description);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="AUTHOR"
     */
    public abstract String getAuthor();

    /**
     * @ejb:persistence column-name="AUTHOR"
     */
    public abstract void setAuthor(String author);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="STATUS"
     */
    public abstract String getPublicationStatus();

    /**
     * @ejb:persistence column-name="STATUS"
     */
    public abstract void setPublicationStatus(String status);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="CREATEDDATE"
     */
    public abstract Date getCreatedDate();

    /**
     * @ejb:persistence column-name="CREATEDDATE"
     */
    public abstract void setCreatedDate(Date createdDate);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="VALIDFROMDATE"
     */
    public abstract Date getValidFromDate();

    /**
     * @ejb:persistence column-name="VALIDFROMDATE"
     */
    public abstract void setValidFromDate(Date fromDate);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="VALIDTODATE"
     */
    public abstract Date getValidToDate();

    /**
     * @ejb:persistence column-name="VALIDTODATE"
     */
    public abstract void setValidToDate(Date toDate);

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="STATE"
     */
    public abstract int getState();

    /**
     * @ejb:interface-method
     * @ejb:persistence column-name="STATE"
     */
    public abstract void setState(int state);

    /**
     * @ejb:interface-method
     * @ejb:relation name="Package-ProcessDefinition"
     * role-name="ProcessDefinition-Has-A-Package"
     * cascade-delete="yes"
     * @weblogic:column-map key-column="PACKAGEID"
     * foreign-key-column="PACKAGEID"
     * @jboss:relation related-pk-field="packageId"
     * fk-column="PACKAGEID"
     */
    public abstract PackageLocal getXpdlPackage();

    public abstract void setXpdlPackage(PackageLocal pkg);

    /**
     * @ejb:interface-method
     */
    public PackageRO getXPDLPackageRO() {
        return getXpdlPackage();
    }

    /**
     * Updates the process definition fields from the workflow.
     *
     * @param workflow The workflow definition.
     * @ejb:interface-method
     */
    public void update(WorkflowProcess workflow) {
        _update(workflow);
    }

    protected final Log getLogger() {
        return _logger;
    }

    private void _update(WorkflowProcess workflow) {
        if (!getProcessDefinitionId().equals(workflow.getId())) throw new IllegalArgumentException("processDefinitionId mismatch");
        String packageId = getPackageId();
        if (packageId != null && !packageId.equals(workflow.getPackage().getId())) {
            throw new IllegalArgumentException("packageId mismatch");
        }
        XPDLPackage pkg = workflow.getPackage();
        RedefinableHeader redefHdr = workflow.getRedefinableHeader();
        String author = null;
        String pubStat = null;
        if (redefHdr != null) {
            author = redefHdr.getAuthor();
            PublicationStatus ps = redefHdr.getPublicationStatus();
            if (ps != null) pubStat = ps.toString();
        }
        redefHdr = pkg.getRedefinableHeader();
        if (redefHdr != null) {
            if (author == null) author = redefHdr.getAuthor();
            if (pubStat == null) {
                PublicationStatus ps = redefHdr.getPublicationStatus();
                if (ps != null) pubStat = ps.toString();
            }
        }
        ProcessHeader hdr = workflow.getProcessHeader();
        setName(workflow.getName());
        setDescription(hdr.getDescription());
        setAuthor(author);
        setPublicationStatus(pubStat);
        setCreatedDate(hdr.getCreated());
        setValidFromDate(hdr.getValidFrom());
        setValidToDate(hdr.getValidTo());
        setState(workflow.getState());
    }
}
