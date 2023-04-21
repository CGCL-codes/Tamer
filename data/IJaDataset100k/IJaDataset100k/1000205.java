package com.corratech.opensuite.persist.acl;

import org.apache.log4j.Logger;
import com.corratech.opensuite.api.SessionManager;
import com.corratech.opensuite.persist.api.AuthorityTypeDao;
import com.corratech.opensuite.persist.dao.AbstractDao;

/**
 * @author aleksandr.kryzhak
 *
 */
public class AuthorityTypeDaoImpl extends AbstractDao<AuthorityType> implements AuthorityTypeDao {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(AuthorityTypeDaoImpl.class);

    private static final String INSTANCE_NAME = AuthorityType.class.getName();

    public AuthorityTypeDaoImpl() {
    }

    public AuthorityTypeDaoImpl(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getInstanceClassName() {
        return INSTANCE_NAME;
    }
}
