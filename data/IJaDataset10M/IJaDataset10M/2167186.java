package com.kwoksys.action.hardware;

import org.apache.struts.action.ActionForm;

/**
 * ActionForm class for hardware components.
 */
public class HardwareComponentForm extends ActionForm {

    private Integer hardwareId;

    private Integer compId;

    private String compDescription;

    private Integer hardwareComponentType;

    private boolean hasCustomFields = false;

    public Integer getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(Integer hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getHardwareComponentType() {
        return hardwareComponentType;
    }

    public void setHardwareComponentType(Integer hardwareComponentType) {
        this.hardwareComponentType = hardwareComponentType;
    }

    public String getCompDescription() {
        return compDescription;
    }

    public void setCompDescription(String compDescription) {
        this.compDescription = compDescription;
    }

    public boolean hasCustomFields() {
        return hasCustomFields;
    }

    public void setHasCustomFields(boolean hasCustomFields) {
        this.hasCustomFields = hasCustomFields;
    }
}
