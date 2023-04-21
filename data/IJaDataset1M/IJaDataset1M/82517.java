package com.ag.promanagement.dto;

import com.ag.presentation.backEndBeans.CargoTypeView;
import com.ag.promanagement.CargoType;
import java.io.Serializable;
import java.util.Date;
import javax.faces.event.ActionEvent;

/**
*
*
* @author Zathura Code Generator http://code.google.com/p/zathura
*
*/
public class CargoTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private String id;

    private boolean rowSelected = false;

    private CargoTypeView cargoTypeView;

    private CargoType cargoType;

    public CargoType getCargoType() {
        return cargoType;
    }

    public String listener_update(ActionEvent e) {
        try {
            cargoTypeView.action_modifyWitDTO(((id == null) || id.equals("")) ? null : new Long(id), ((type == null) || type.equals("")) ? null : new String(type));
            rowSelected = !rowSelected;
        } catch (Exception ex) {
            return "";
        }
        return "";
    }

    public void listener_cancel(ActionEvent e) {
        id = cargoType.getId().toString();
        type = (cargoType.getType() != null) ? cargoType.getType().toString() : null;
        rowSelected = !rowSelected;
    }

    /**
    * <p>Bound to commandLink actionListener in the ui that renders/unrenders
        * the Customer details for editing.</p>
        */
    public void toggleSelected(ActionEvent e) {
        id = cargoType.getId().toString();
        type = (cargoType.getType() != null) ? cargoType.getType().toString() : null;
        rowSelected = !rowSelected;
    }

    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    public CargoTypeView getCargoTypeView() {
        return cargoTypeView;
    }

    public void setCargoTypeView(CargoTypeView cargoTypeView) {
        this.cargoTypeView = cargoTypeView;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(boolean rowSelected) {
        this.rowSelected = rowSelected;
    }
}
