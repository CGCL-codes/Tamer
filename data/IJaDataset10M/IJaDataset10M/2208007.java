package org.crucy.persistence.tablemodel.beandao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Performs logging informations when a persistent operation is called.
 * @author Thibault
 *
 * @param <T>
 */
public class LoggerBeanSaver<T> extends AbstractBeanSaver<T> {

    /** Serialization ID. */
    private static final long serialVersionUID = 5123975136153307642L;

    /** Logger. */
    private static Log logger = LogFactory.getLog(LoggerBeanSaver.class);

    /**
	 * @return the logger
	 */
    private static Log getLogger() {
        return logger;
    }

    /**
	 * Notifies the DAO he should commit every action from a given set.
	 * @see org.crucy.persistence.tablemodel.beandao.AbstractBeanSaver#
	 * commitDAO()
	 */
    @Override
    protected final void commitDAO() {
        getLogger().info("commitDAO");
    }

    /**
	 * @param bean to delete
	 * @see org.crucy.persistence.tablemodel.beandao.AbstractBeanSaver#
	 * delete(java.lang.Object)
	 */
    @Override
    protected final void delete(final T bean) {
        getLogger().info("deleteBean " + bean);
    }

    /**
	 * Rollback every pending unsaved operation.
	 * @see org.crucy.persistence.tablemodel.beandao.AbstractBeanSaver#
	 * rollbackDAO()
	 */
    @Override
    protected final void rollbackDAO() {
        getLogger().info("rollbackDAO");
    }

    /**
	 * @param bean Bean to save
	 * @see org.crucy.persistence.tablemodel.beandao.AbstractBeanSaver#
	 * save(java.lang.Object)
	 */
    @Override
    protected final void save(final T bean) {
        getLogger().info("save " + bean);
    }

    /**
	 * @param bean Bean to update.
	 * @see org.crucy.persistence.tablemodel.beandao.AbstractBeanSaver#
	 * update(java.lang.Object)
	 */
    @Override
    protected final void update(final T bean) {
        getLogger().info("update " + bean);
    }
}
