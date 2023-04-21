package org.squabble.dao;

import java.util.List;
import org.squabble.domain.Privilege;

public interface PrivilegeDAO {

    public Privilege getPrivilege(Long id);

    public Privilege getPrivilege(String name);

    public List<Privilege> getPrivileges();
}
