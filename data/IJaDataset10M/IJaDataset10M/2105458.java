package pl.kwiecienm.cvms.component;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import pl.kwiecienm.cvms.model.Role;

/**
 * For administrator only.
 * 
 * @author kwiecienm
 */
@Scope(ScopeType.APPLICATION)
@Name("roleList")
public class RoleList {

    /**
	 * @return
	 */
    @Unwrap
    public Role[] getRoleList() {
        return Role.values();
    }
}
