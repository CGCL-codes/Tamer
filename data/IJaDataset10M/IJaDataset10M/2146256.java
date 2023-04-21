package org.koossery.adempiere.core.contract.dto.workflow;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.workflow.IAD_WF_NodeDTO;

public class AD_WF_NodeDTO extends KTADempiereBaseDTO implements IAD_WF_NodeDTO {

    private static final long serialVersionUID = 1L;

    private String action;

    private int ad_Column_ID;

    private int ad_Form_ID;

    private int ad_Image_ID;

    private int ad_Process_ID;

    private int ad_Task_ID;

    private int ad_WF_Block_ID;

    private int ad_WF_Node_ID;

    private int ad_WF_Responsible_ID;

    private int ad_Window_ID;

    private int ad_Workflow_ID;

    private String attributeName;

    private String attributeValue;

    private int c_BPartner_ID;

    private BigDecimal cost;

    private String description;

    private String docAction;

    private int duration;

    private BigDecimal dynPriorityChange;

    private String dynPriorityUnit;

    private String email;

    private String emailRecipient;

    private String entityType;

    private String finishMode;

    private String help;

    private String joinElement;

    private int limit;

    private int movingTime;

    private String name;

    private int overlapUnits;

    private int priority;

    private int queuingTime;

    private int r_MailText_ID;

    private int s_Resource_ID;

    private int setupTime;

    private String splitElement;

    private String startMode;

    private String subflowExecution;

    private BigDecimal unitsCycles;

    private Timestamp validFrom;

    private Timestamp validTo;

    private String value;

    private int waitingTime;

    private int waitTime;

    private int workflow_ID;

    private int workingTime;

    private int xposition;

    private int yposition;

    private String isCentrallyMaintained;

    private String isMilestone;

    private String isSubcontracting;

    private String isActive;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getAd_Column_ID() {
        return ad_Column_ID;
    }

    public void setAd_Column_ID(int ad_Column_ID) {
        this.ad_Column_ID = ad_Column_ID;
    }

    public int getAd_Form_ID() {
        return ad_Form_ID;
    }

    public void setAd_Form_ID(int ad_Form_ID) {
        this.ad_Form_ID = ad_Form_ID;
    }

    public int getAd_Image_ID() {
        return ad_Image_ID;
    }

    public void setAd_Image_ID(int ad_Image_ID) {
        this.ad_Image_ID = ad_Image_ID;
    }

    public int getAd_Process_ID() {
        return ad_Process_ID;
    }

    public void setAd_Process_ID(int ad_Process_ID) {
        this.ad_Process_ID = ad_Process_ID;
    }

    public int getAd_Task_ID() {
        return ad_Task_ID;
    }

    public void setAd_Task_ID(int ad_Task_ID) {
        this.ad_Task_ID = ad_Task_ID;
    }

    public int getAd_WF_Block_ID() {
        return ad_WF_Block_ID;
    }

    public void setAd_WF_Block_ID(int ad_WF_Block_ID) {
        this.ad_WF_Block_ID = ad_WF_Block_ID;
    }

    public int getAd_WF_Node_ID() {
        return ad_WF_Node_ID;
    }

    public void setAd_WF_Node_ID(int ad_WF_Node_ID) {
        this.ad_WF_Node_ID = ad_WF_Node_ID;
    }

    public int getAd_WF_Responsible_ID() {
        return ad_WF_Responsible_ID;
    }

    public void setAd_WF_Responsible_ID(int ad_WF_Responsible_ID) {
        this.ad_WF_Responsible_ID = ad_WF_Responsible_ID;
    }

    public int getAd_Window_ID() {
        return ad_Window_ID;
    }

    public void setAd_Window_ID(int ad_Window_ID) {
        this.ad_Window_ID = ad_Window_ID;
    }

    public int getAd_Workflow_ID() {
        return ad_Workflow_ID;
    }

    public void setAd_Workflow_ID(int ad_Workflow_ID) {
        this.ad_Workflow_ID = ad_Workflow_ID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public int getC_BPartner_ID() {
        return c_BPartner_ID;
    }

    public void setC_BPartner_ID(int c_BPartner_ID) {
        this.c_BPartner_ID = c_BPartner_ID;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BigDecimal getDynPriorityChange() {
        return dynPriorityChange;
    }

    public void setDynPriorityChange(BigDecimal dynPriorityChange) {
        this.dynPriorityChange = dynPriorityChange;
    }

    public String getDynPriorityUnit() {
        return dynPriorityUnit;
    }

    public void setDynPriorityUnit(String dynPriorityUnit) {
        this.dynPriorityUnit = dynPriorityUnit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailRecipient() {
        return emailRecipient;
    }

    public void setEmailRecipient(String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFinishMode() {
        return finishMode;
    }

    public void setFinishMode(String finishMode) {
        this.finishMode = finishMode;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getJoinElement() {
        return joinElement;
    }

    public void setJoinElement(String joinElement) {
        this.joinElement = joinElement;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(int movingTime) {
        this.movingTime = movingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOverlapUnits() {
        return overlapUnits;
    }

    public void setOverlapUnits(int overlapUnits) {
        this.overlapUnits = overlapUnits;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQueuingTime() {
        return queuingTime;
    }

    public void setQueuingTime(int queuingTime) {
        this.queuingTime = queuingTime;
    }

    public int getR_MailText_ID() {
        return r_MailText_ID;
    }

    public void setR_MailText_ID(int r_MailText_ID) {
        this.r_MailText_ID = r_MailText_ID;
    }

    public int getS_Resource_ID() {
        return s_Resource_ID;
    }

    public void setS_Resource_ID(int s_Resource_ID) {
        this.s_Resource_ID = s_Resource_ID;
    }

    public int getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(int setupTime) {
        this.setupTime = setupTime;
    }

    public String getSplitElement() {
        return splitElement;
    }

    public void setSplitElement(String splitElement) {
        this.splitElement = splitElement;
    }

    public String getStartMode() {
        return startMode;
    }

    public void setStartMode(String startMode) {
        this.startMode = startMode;
    }

    public String getSubflowExecution() {
        return subflowExecution;
    }

    public void setSubflowExecution(String subflowExecution) {
        this.subflowExecution = subflowExecution;
    }

    public BigDecimal getUnitsCycles() {
        return unitsCycles;
    }

    public void setUnitsCycles(BigDecimal unitsCycles) {
        this.unitsCycles = unitsCycles;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidTo() {
        return validTo;
    }

    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getWorkflow_ID() {
        return workflow_ID;
    }

    public void setWorkflow_ID(int workflow_ID) {
        this.workflow_ID = workflow_ID;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public int getXposition() {
        return xposition;
    }

    public void setXposition(int xposition) {
        this.xposition = xposition;
    }

    public int getYposition() {
        return yposition;
    }

    public void setYposition(int yposition) {
        this.yposition = yposition;
    }

    public String getIsCentrallyMaintained() {
        return isCentrallyMaintained;
    }

    public void setIsCentrallyMaintained(String isCentrallyMaintained) {
        this.isCentrallyMaintained = isCentrallyMaintained;
    }

    public String getIsMilestone() {
        return isMilestone;
    }

    public void setIsMilestone(String isMilestone) {
        this.isMilestone = isMilestone;
    }

    public String getIsSubcontracting() {
        return isSubcontracting;
    }

    public void setIsSubcontracting(String isSubcontracting) {
        this.isSubcontracting = isSubcontracting;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
