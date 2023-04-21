package com.dcivision.workflow.bean;

import com.dcivision.framework.bean.AbstractBaseObject;

/**
 * SetupOptionWorkflowCategory.java
 * 
 * This class is the serializable bean reflecting business logic uses.
 * It represents a system workflow category. Each category will have its own
 * related object binded to a workflow record.
 * 
 * @author Angus Shiu
 * @company DCIVision Limited
 * @creation date 25/10/2004
 * @version $Revision: 1.3 $
 */
public class SetupOptionWorkflowCategory extends AbstractBaseObject {

    static final long serialVersionUID = -7326581572929734642L;

    private String workflowCategoryName = null;

    private String status = null;

    private Integer displaySeq = null;

    private String sysInd = null;

    public SetupOptionWorkflowCategory() {
        super();
    }

    /**
   * @return Returns the workflowCategoryName.
   */
    public String getWorkflowCategoryName() {
        return (this.workflowCategoryName);
    }

    /**
   * @param workflowCategoryName The workflowCategoryName to set.
   */
    public void setWorkflowCategoryName(String workflowCategoryName) {
        this.workflowCategoryName = workflowCategoryName;
    }

    /**
   * @return Returns the status.
   */
    public String getStatus() {
        return (this.status);
    }

    /**
   * @param status The status to set.
   */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
   * @return Returns the displaySeq.
   */
    public Integer getDisplaySeq() {
        return (this.displaySeq);
    }

    /**
   * @param displaySeq The displaySeq to set.
   */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    /**
   * @return Returns the sysInd.
   */
    public String getSysInd() {
        return (this.sysInd);
    }

    /**
   * @param sysInd The sysInd to set.
   */
    public void setSysInd(String sysInd) {
        this.sysInd = sysInd;
    }
}
