package org.verus.ngl.utilities.marc;

/**
 *
 * @author root
 */
public class NGLControlField {

    /** Creates a new instance of NGLControlField */
    public NGLControlField() {
    }

    private String controlTag = "";

    private String data = "";

    private Boolean mandatory = false;

    public String getControlTag() {
        return controlTag;
    }

    public void setControlTag(String controlTag) {
        this.controlTag = controlTag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }
}
