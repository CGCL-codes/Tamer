package genesis.accessControl.persistence.impl;

import java.util.Set;
import genesis.accessControl.api.AccessLevel;
import genesis.accessControl.persistence.api.AccessLevelDAO;
import genesis.common.persistence.api.PersistenceException;
import genesis.common.persistence.impl.JPABaseDAO;

public class JPAAccessLeveDAO extends JPABaseDAO<Long, AccessLevel> implements AccessLevelDAO {

    public JPAAccessLeveDAO() {
        super(AccessLevel.class);
    }

    public AccessLevel retrieveAccessLevelByCode(int code) throws PersistenceException {
        return null;
    }

    public Set<AccessLevel> retrieveAccessLevelByName(String name) throws PersistenceException {
        return null;
    }

    public long retrieveLastAccessLevelCode() throws PersistenceException {
        return 0;
    }
}
