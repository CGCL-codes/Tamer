package cz.cvut.phone.core.data.dao;

import javax.ejb.Local;

/**
 *
 * @author Frantisek Hradil
 */
@Local
public interface SettingsEntityDAOLocal {

    public cz.cvut.phone.core.data.entity.SettingsEntity findByCompanyId(java.lang.Integer companyID) throws java.lang.Exception;
}
