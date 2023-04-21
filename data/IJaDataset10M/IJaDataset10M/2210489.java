package uk.ac.ebi.imexCentral.controller.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: local_admin
 * Date: 27-Sep-2006
 * Time: 15:37:19
 * To change this template use File | Settings | File Templates.
 */
public class AddRoleForm extends ActionForm {

    private String role;

    private String loginid;

    private Collection roles = null;

    private String[] roleSelect = null;

    public Collection getRoles() {
        return roles;
    }

    public void setRoles(Collection value) {
        roles = value;
    }

    public String[] getRoleSelect() {
        return roleSelect;
    }

    public void setRoleSelect(String[] value) {
        roleSelect = value;
    }

    /**
        * Reset all properties to their default values.
        *
        * @param mapping The mapping used to select this instance
        * @param request The servlet request we are processing
        */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.role = null;
        this.loginid = null;
        this.roles = new ArrayList();
        roles.add("ADMIN");
        roles.add("CURATOR");
        roles.add("REGISTERED");
        roles.add("UNKNOWN");
    }

    /**
     *  Validate all properties
     * @param mapping
     * @param request
     * @return   ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }
}
