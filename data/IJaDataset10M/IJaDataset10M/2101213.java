package com.uplexis.idealize.base.cache;

import com.uplexis.idealize.base.exceptions.IdealizeUnavailableResourceException;
import com.uplexis.idealize.base.loggers.IdealizeLogger;
import com.uplexis.idealize.hotspots.storable.BaseStorable;

/**
 * Implements a temp storage for constantly accessed data, and data "hard" to be
 * computed (such as recommendations, similarity matrixes, etc). This class is
 * implemented according to Singleton pattern.
 * 
 * @author Felipe Melo
 */
public final class Cache {

    private final String canonicalName = this.getClass().getCanonicalName();

    private IdealizeLogger logger = null;

    private static Cache uniqueInstance;

    @SuppressWarnings("unchecked")
    private BaseStorable data;

    /**
	 * Creates a new TempStorage from some BaseStorable data
	 */
    private Cache() {
        this.logger = IdealizeLogger.getInstance();
    }

    /**
	 * Get the singleton instance of TempStorage.
	 * 
	 * @param data
	 *            BaseStorable data from which TempStorage will be created or
	 *            null if the instance was already made.
	 * @return TempStorage unique instance
	 */
    public static final Cache getInstance() {
        if (uniqueInstance == null) uniqueInstance = new Cache();
        return uniqueInstance;
    }

    /**
	 * Standard getter for BaseStorable data.
	 * 
	 * @return BaseStorable object.
	 */
    @SuppressWarnings("unchecked")
    public final BaseStorable getData() {
        return data;
    }

    /**
	 * Updates the cached data
	 * 
	 * @param data
	 *            BaseStorable implementation
	 * @param serialize
	 *            boolean indicating if data must be serialized
	 */
    @SuppressWarnings("unchecked")
    public final synchronized void setData(BaseStorable data, boolean serialize) {
        this.data = data;
        if (serialize) {
            try {
                this.data.serialize();
            } catch (IdealizeUnavailableResourceException e) {
                this.logger.logError(this.canonicalName + ".setData: could not serialize data: " + e.getMessage(), e);
            }
            CacheObserver.getInstance().notifyCacheChange();
        }
    }
}
