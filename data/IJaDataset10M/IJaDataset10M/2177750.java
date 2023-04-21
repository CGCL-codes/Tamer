package com.opensymphony.workflow.spi.ejb;

/**
 * Local home interface for CurrentStep.
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author <a href="mailto:plightbo@hotmail.com">Pat Lightbody</a>
 */
public interface CurrentStepLocalHome extends javax.ejb.EJBLocalHome {

    public static final String COMP_NAME = "java:comp/env/ejb/CurrentStep";

    public static final String JNDI_NAME = "CurrentStep";

    public com.opensymphony.workflow.spi.ejb.CurrentStepLocal create(long entryId, int stepId, java.lang.String owner, java.sql.Timestamp startDate, java.sql.Timestamp dueDate, java.lang.String status) throws javax.ejb.CreateException;

    public java.util.Collection findByEntryId(long entryId) throws javax.ejb.FinderException;

    public com.opensymphony.workflow.spi.ejb.CurrentStepLocal findByPrimaryKey(java.lang.Long pk) throws javax.ejb.FinderException;
}
