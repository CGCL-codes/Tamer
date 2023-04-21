package com.etymgiko.spaceshipshop.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import javax.servlet.http.HttpServletRequest;

/**
 * Form for creating new ship.
 *
 * @author Ivan Holub
 */
public class NewShipForm extends ActionForm {

    /**
     * Name.
     */
    private String name;

    /**
     * Typename.
     */
    private String typename;

    /**
     * Manufacturer.
     */
    private String manufacturer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        if ((name == null) || (name.length() < 1)) {
            errors.add("name", new ActionMessage("errors.ship.name.required"));
        }
        if ((typename == null) || (typename.length() < 1)) {
            errors.add("typename", new ActionMessage("errors.ship.typename.required"));
        }
        if ((manufacturer == null) || (manufacturer.length() < 1)) {
            errors.add("manufacturer", new ActionMessage("errors.ship.manufacturer.required"));
        }
        return errors;
    }
}
