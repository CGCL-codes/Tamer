package com.springbook.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EditBikeForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String manufacturer;

    private String model;

    private int frame;

    private String serialNo;

    private double weight;

    private String status;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        manufacturer = null;
        model = null;
        frame = 0;
        status = null;
        weight = 0;
        serialNo = null;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        String mappingName = mapping.getPath();
        if (mappingName.equalsIgnoreCase("/submitBike")) {
            if (manufacturer == null || manufacturer.trim().length() == 0) {
                errors.add("bike", new ActionMessage("error.manufacturer.required"));
            }
            if (model == null || model.trim().length() == 0) {
                errors.add("bike", new ActionMessage("error.model.required"));
            }
        }
        return errors;
    }
}
