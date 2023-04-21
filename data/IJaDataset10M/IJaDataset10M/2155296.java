package icescrum2.dao.impl;

import icescrum2.dao.ICustomRoleDao;
import icescrum2.dao.model.ICustomRole;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomRoleDao extends HibernateDaoSupport implements ICustomRoleDao {

    public void addCustomRole(ICustomRole _customRole) {
        try {
            this.getHibernateTemplate().save(_customRole);
            this.getHibernateTemplate().flush();
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "saveCustomRole", _e);
        }
    }

    public void updateCustomRole(ICustomRole _customRole) {
        try {
            this.getHibernateTemplate().update(_customRole);
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "updateCustomRole", _e);
        }
    }

    public void deleteCustomRole(ICustomRole _customRole) {
        try {
            this.getHibernateTemplate().delete(_customRole);
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "deleteCustomRole", _e);
        }
    }

    public void deleteAllCustomRole(List<ICustomRole> _customRole) {
        try {
            this.getHibernateTemplate().deleteAll(_customRole);
        } catch (DataAccessException _e) {
            ExceptionManager.getInstance().manageDataAccessException(this.getClass().getName(), "deleteAllCustomRole", _e);
        }
    }
}
