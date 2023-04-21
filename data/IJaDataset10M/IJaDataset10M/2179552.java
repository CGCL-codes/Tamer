package org.swana.daemon;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.swana.dao.VisitOriginDao;
import org.swana.pojo.VisitOrigin;

/**
 * The cache fo retrieveing origin source.
 * @author Wang Yuxing
 */
public class PageReferrerCache {

    public static String BEAN_NAME = "pageReferrerCache";

    Logger logger = null;

    private VisitOriginDao originSourceDao = null;

    private Map<String, Long> map = new HashMap<String, Long>();

    public PageReferrerCache() {
        logger = Logger.getLogger(this.getClass().getName());
        logger.setAdditivity(false);
        logger.debug("\n" + Calendar.getInstance().getTime() + " PageReferrerCache is ready \n");
    }

    /**
	 * Retrieve page id from cache; if not exist, find in DB and store in cache; if still not 
	 * exist, add new entry to DB and cache.
	 * @return always return a referrer id.
	 */
    public synchronized Long retrieve(String address) {
        if (address == null || address.length() == 0) {
            return 0L;
        }
        Long pid = map.get(address);
        if (pid != null) {
            logger.debug("-");
            return pid;
        }
        VisitOrigin os = originSourceDao.findOriginSourceByAddress(address);
        if (os != null) {
            map.put(address, os.getId());
            logger.debug("=");
            return os.getId();
        }
        os = new VisitOrigin(address);
        originSourceDao.create(os);
        map.put(address, os.getId());
        logger.debug("#");
        return os.getId();
    }

    public VisitOriginDao getOriginSourceDao() {
        return originSourceDao;
    }

    public void setOriginSourceDao(VisitOriginDao originSourceDao) {
        this.originSourceDao = originSourceDao;
    }
}
