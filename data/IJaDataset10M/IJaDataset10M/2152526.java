package bd.com.escenic.flexilunch.dao;

import bd.com.escenic.flexilunch.model.Role;
import bd.com.escenic.flexilunch.model.RoleImpl;

/**
 * $Id: RoleDAO.java 15 2009-06-09 04:04:09Z shihab.uddin@gmail.com $.
 *
 * @author <a href="mailto:shihab.uddin@gmail.com">Shihab Uddin</a>
 * @version $Revision: 15 $
 */
public final class RoleDAO extends EntityDAO<Role> {

    public RoleDAO() {
        super(RoleImpl.class);
    }
}
