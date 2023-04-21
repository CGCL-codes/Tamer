package kz.simplex.photobox.action;

import kz.simplex.photobox.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("roleHome")
public class RoleHome extends EntityHome<Role> {

    public void setRoleId(Integer id) {
        setId(id);
    }

    public Integer getRoleId() {
        return (Integer) getId();
    }

    @Override
    protected Role createInstance() {
        Role role = new Role();
        return role;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Role getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
