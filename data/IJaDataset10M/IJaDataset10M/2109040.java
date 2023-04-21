package com.angel.mocks.data.generator.factory;

import com.angel.dao.generic.factory.DAOFactory;
import com.angel.mocks.data.generator.daos.RoleDAO;
import com.angel.mocks.data.generator.daos.UserDAO;
import com.angel.mocks.data.generator.daos.UserTypeDAO;
import com.angel.mocks.data.generator.daos.impl.RoleDAOHibernate;
import com.angel.mocks.data.generator.daos.impl.UserHibernateDAO;
import com.angel.mocks.data.generator.daos.impl.UserTypeHibernateDAO;

/**
 * @author William
 *
 */
public class TestHibernateDAOFactory extends DAOFactory {

    private static final long serialVersionUID = -630713914137962975L;

    public UserDAO getUserDAO() {
        return new UserHibernateDAO();
    }

    public UserTypeDAO getUserTypeDAO() {
        return new UserTypeHibernateDAO();
    }

    public RoleDAO getRoleDAO() {
        return new RoleDAOHibernate();
    }
}
