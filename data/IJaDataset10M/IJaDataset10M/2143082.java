package org.magicbox.ibatis;

import org.magicbox.dao.AmministratoreCentroDao;
import org.magicbox.domain.Amministratore;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * Implementazione per l' accesso al repository degli amministratori dei centri
 * 
 * @author Massimiliano Dess� (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class AmministratoreCentroDaoImpl extends SqlMapClientDaoSupport implements AmministratoreCentroDao {

    public Amministratore getAmministratoreCentro(long idCentro) throws DataAccessException {
        return (Amministratore) getSqlMapClientTemplate().queryForObject("getAdminCentro", idCentro);
    }

    public boolean updateAmministratoreCentro(Amministratore admin) throws DataAccessException {
        return getSqlMapClientTemplate().update("updateAdminCentro", admin) == 1 ? true : false;
    }
}
