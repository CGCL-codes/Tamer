package org.koossery.adempiere.core.contract.dto.generated;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class C_ActivityDTO extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private int c_Activity_ID;

    private String description;

    private String help;

    private String name;

    private String value;

    private String isSummary;

    private String isActive;

    public int getC_Activity_ID() {
        return c_Activity_ID;
    }

    public void setC_Activity_ID(int c_Activity_ID) {
        this.c_Activity_ID = c_Activity_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsSummary() {
        return isSummary;
    }

    public void setIsSummary(String isSummary) {
        this.isSummary = isSummary;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
