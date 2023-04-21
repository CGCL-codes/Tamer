package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.dao.schema.Schema;
import org.yass.dao.schema.Schema00;
import org.yass.util.persistence.EntityManagerFactoryProxy;

/**
 * 
 * @author svenduzont
 */
public class DaoHelper {

    private static final DaoHelper instance = new DaoHelper();

    private static final Log LOG = LogFactory.getLog(DaoHelper.class);

    private static Schema schema;

    /**
	 * @return the instance
	 */
    public static final DaoHelper getInstance() {
        return instance;
    }

    private DaoHelper() {
        schema = new Schema00(EntityManagerFactoryProxy.getInstance().createEntityManager());
    }

    public boolean checkDataBase(final boolean create) {
        try {
            return schema.execute(create);
        } catch (final Exception e) {
            LOG.fatal(e);
            return false;
        }
    }
}
