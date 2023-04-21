package de.uni_leipzig.lots.webfrontend.formbeans;

import de.uni_leipzig.lots.webfrontend.formbeans.property.EnumProperty;
import de.uni_leipzig.lots.common.objects.Role;

/**
 * Form bean for changeing the current user role.
 *
 * @author Alexander Kiel
 * @version $Id: ChangeRoleForm.java,v 1.4 2007/10/23 06:29:38 mai99bxd Exp $
 */
public final class ChangeRoleForm extends AutoValidateFormSupport {

    public ChangeRoleForm() {
        registerProperty("role", role);
        role.setRequired(true);
    }

    protected EnumProperty role = new EnumProperty(Role.class);

    public String getRole() {
        return role.getValue();
    }

    public void setRole(String role) {
        this.role.setValue(role);
    }
}
