package org.openlogbooks.service;

import org.openlogbooks.model.Role;

public interface RoleManager extends Manager<Role, Long> {

    public Role findRole(String rolename);
}
