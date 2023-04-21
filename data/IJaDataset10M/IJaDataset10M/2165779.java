package com.kwoksys.action.contracts;

import com.kwoksys.action.reports.ReportForm;

/**
 * Action form for Index page.
 */
public class ContractSearchForm extends ReportForm {

    private String cmd;

    private String contractName;

    private String description;

    private Integer stage = 0;

    private Integer stageFilter = 0;

    private Integer contractTypeId = 0;

    private Integer contractProviderId = 0;

    private String attrId;

    private String attrValue;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public Integer getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(Integer contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    public Integer getContractProviderId() {
        return contractProviderId;
    }

    public void setContractProviderId(Integer contractProviderId) {
        this.contractProviderId = contractProviderId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getStageFilter() {
        return stageFilter;
    }

    public void setStageFilter(Integer stageFilter) {
        this.stageFilter = stageFilter;
    }
}
