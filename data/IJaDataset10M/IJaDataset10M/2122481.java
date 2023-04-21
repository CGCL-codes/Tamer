package com.acv.service.cache;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface CacheManager {

    /**
	 * This method returns the items left.
	 *
	 * @return number of items left.
	 */
    public int getItemsLeft();

    /**
	 * Gets the items total.
	 *
	 * @return the items total
	 */
    public int getItemsTotal();

    /**
	 * This method will flush the EHCache.
	 */
    public void flushEHCache();

    /**
	 * @param sc
	 * @param request
	 */
    public void flushOSCache(ServletContext sc, HttpServletRequest request);

    /**
	 * This method will flush the OS cache.
	 *
	 * @param keys
	 *            the keys
	 * @param groups
	 *            the groups
	 * @param scope
	 *            the scope
	 */
    public void flushOSCache(List<String> keys, List<String> groups, String scope);

    /**
	 * This method will flush the cache for configuration table.
	 */
    public void flushConfigurationCache();

    /**
	 * This method will flush the cache for I18n table.
	 */
    public void flushI18nCache();
}
