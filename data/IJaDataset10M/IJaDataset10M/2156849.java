package com.avatal.persistency.dao.rightmanagement;

import com.avatal.persistency.dao.exception.DAOException;

/**
 * @author m0550
 *
 * Created on 20.05.2003
 */
public class MethodDAOFactory {

    /** user dao provider key */
    public static final String KEY_METHOD_DATA_SOURCE = "method.data.source";

    /** RDBMS user dao provider */
    public static final int USER_DATA_SOURCE_RDBMS = 1;

    /** LDAP user dao provider */
    public static final int USER_DATA_SOURCE_LDAP = 2;

    /** an handle to the unique UserDAOFactory instance. */
    private static MethodDAOFactory instance = null;

    public static MethodDAOFactory getInstance() {
        if (instance == null) {
            instance = new MethodDAOFactory();
        }
        return instance;
    }

    public MethodDAO getMethodDAO(int provider) throws DAOException {
        switch(provider) {
            case USER_DATA_SOURCE_RDBMS:
                return RDBMSMethodDAO.getInstance();
            case USER_DATA_SOURCE_LDAP:
                return LDAPMethodDAO.getInstance();
        }
        return null;
    }
}
