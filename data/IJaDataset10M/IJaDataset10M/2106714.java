package com.bhms.module.users.users.service;

import java.util.List;
import com.bhms.module.users.users.mapper.Roles;

public interface RoleService {

    public List<Roles> searchRole(Roles roles);

    public void findRole(Roles roles);

    public void editRole(Roles roles);

    public void deleteRole(Roles roles);
}
