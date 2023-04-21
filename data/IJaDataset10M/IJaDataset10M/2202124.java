package org.gridtrust.trs.v2.dao.logic;

import org.gridtrust.trs.v2.dao.DaoFactory;
import org.gridtrust.trs.v2.dao.jdbc.DaoFactoryJdbc;

/**
 * Implementation for of the DaoFacade.
 * 
 * @author Silvano Riz
 */
public class DaoFacadeImpl implements DaoFacade {

    public static final int JDBC = 1;

    /**
	 * {@inheritDoc}
	 */
    public DaoFactory getDaoFactory(int type) {
        DaoFactory daoFactory = null;
        switch(type) {
            case JDBC:
                daoFactory = new DaoFactoryJdbc();
                break;
            default:
                daoFactory = new DaoFactoryJdbc();
                break;
        }
        return daoFactory;
    }
}
