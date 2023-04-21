package org.acs.elated.lucene;

import org.acs.elated.exception.InternalException;
import org.apache.log4j.Logger;

/**
 * @author ACS Tech Center
 *
 */
public class LuceneFactory {

    private static Logger logger = (Logger) Logger.getInstance(LuceneFactory.class);

    /**
	 * Returns LuceneMgr depending on the value
	 * of lucene.LuceneMgr set in the vm parameters
	 * @return LuceneMgr
	 * @throws InternalException
	 */
    public static LuceneMgr getLuceneMgr() throws InternalException {
        if (logger.isDebugEnabled()) {
            logger.debug("getLuceneMgr()entering - ");
        }
        String className = System.getProperty("lucene.LuceneMgr", "org.acs.elated.lucene.LuceneMgr");
        logger.info("getLuceneMgr() - " + "loading class:" + className);
        try {
            Class classLoader = Class.forName(className);
            return (LuceneMgr) classLoader.newInstance();
        } catch (InstantiationException ex) {
            logger.fatal("getLuceneMgr()error - " + "Class could not be instantiated:" + className, ex);
        } catch (IllegalAccessException ille) {
            logger.fatal("getLuceneMgr()error - " + "Class could not be accessed:" + className, ille);
        } catch (ClassNotFoundException e) {
            logger.fatal("getLuceneMgr()error - " + "Class could not be loaded:" + className, e);
        }
        return new LuceneMgr();
    }

    /**
	 * Returns Search depending on the value
	 * of lucene.Search set in the vm parameters
	 * @return Search
	 */
    public static Search getSearch() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSearch()entering - ");
        }
        String className = System.getProperty("lucene.Search", "org.acs.elated.lucene.Search");
        logger.info("getSearch() - " + "loading class:" + className);
        try {
            Class classLoader = Class.forName(className);
            return (Search) classLoader.newInstance();
        } catch (InstantiationException ex) {
            logger.fatal("getSearch()error - " + "Class could not be instantiated:" + className, ex);
        } catch (IllegalAccessException ille) {
            logger.fatal("getSearch()error - " + "Class could not be accessed:" + className, ille);
        } catch (ClassNotFoundException e) {
            logger.fatal("getSearch()error - " + "Class could not be loaded:" + className, e);
        }
        return new Search();
    }
}
