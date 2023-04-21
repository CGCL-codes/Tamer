package idv.takeshi.application.service;

import idv.takeshi.model.Role;
import java.util.List;

/**
 * A Service interface for Role Model operations.
 * @author takeshi.miao
 *
 */
public interface RoleService {

    /**
	 * Get all Role Models.
	 * @return
	 */
    public List<Role> getAll();
}
