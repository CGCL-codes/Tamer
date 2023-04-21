package org.koossery.adempiere.core.contract.dto.workflow;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.workflow.IAD_WF_NodeNextDTO;

public class AD_WF_NodeNextDTO extends KTADempiereBaseDTO implements IAD_WF_NodeNextDTO {

    private static final long serialVersionUID = 1L;

    private int ad_WF_Next_ID;

    private int ad_WF_Node_ID;

    private int ad_WF_NodeNext_ID;

    private String description;

    private String entityType;

    private int seqNo;

    private String transitionCode;

    private String isStdUserWorkflow;

    private String isActive;

    public int getAd_WF_Next_ID() {
        return ad_WF_Next_ID;
    }

    public void setAd_WF_Next_ID(int ad_WF_Next_ID) {
        this.ad_WF_Next_ID = ad_WF_Next_ID;
    }

    public int getAd_WF_Node_ID() {
        return ad_WF_Node_ID;
    }

    public void setAd_WF_Node_ID(int ad_WF_Node_ID) {
        this.ad_WF_Node_ID = ad_WF_Node_ID;
    }

    public int getAd_WF_NodeNext_ID() {
        return ad_WF_NodeNext_ID;
    }

    public void setAd_WF_NodeNext_ID(int ad_WF_NodeNext_ID) {
        this.ad_WF_NodeNext_ID = ad_WF_NodeNext_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getTransitionCode() {
        return transitionCode;
    }

    public void setTransitionCode(String transitionCode) {
        this.transitionCode = transitionCode;
    }

    public String getIsStdUserWorkflow() {
        return isStdUserWorkflow;
    }

    public void setIsStdUserWorkflow(String isStdUserWorkflow) {
        this.isStdUserWorkflow = isStdUserWorkflow;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
