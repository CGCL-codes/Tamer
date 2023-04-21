package com.roxwood.jodo.ejb;

/**
 * This is the local-home interface for Task enterprise bean.
 */
public interface TaskLocalHome extends javax.ejb.EJBLocalHome {

    /**
     *
     */
    com.roxwood.jodo.ejb.TaskLocal findByPrimaryKey(java.lang.Integer key) throws javax.ejb.FinderException;

    public com.roxwood.jodo.ejb.TaskLocal create(java.lang.Integer taskid, java.lang.String taskname, java.io.Serializable details, java.lang.String keywords, java.lang.String url, java.sql.Timestamp startdate, java.sql.Timestamp duedate, java.lang.Integer priority, java.lang.Integer percentcomplete, java.lang.Boolean completed, com.roxwood.jodo.ejb.AccountLocal accountid, com.roxwood.jodo.ejb.ProjectLocal projectid) throws javax.ejb.CreateException;

    java.util.Collection findByTaskid(java.lang.Integer taskid) throws javax.ejb.FinderException;

    java.util.Collection findByTaskname(java.lang.String taskname) throws javax.ejb.FinderException;

    java.util.Collection findByKeywords(java.lang.String keywords) throws javax.ejb.FinderException;

    java.util.Collection findByUrl(java.lang.String url) throws javax.ejb.FinderException;

    java.util.Collection findByPriority(java.lang.Integer priority) throws javax.ejb.FinderException;

    java.util.Collection findByPercentcomplete(java.lang.Integer percentcomplete) throws javax.ejb.FinderException;

    java.util.Collection findByCompleted(java.lang.Boolean completed) throws javax.ejb.FinderException;

    com.roxwood.jodo.ejb.TaskLocal create(com.roxwood.jodo.ejb.TaskDTO taskData, com.roxwood.jodo.ejb.ProjectDTO projectData, com.roxwood.jodo.ejb.AccountDTO accountData) throws javax.ejb.CreateException, javax.ejb.FinderException;
}
