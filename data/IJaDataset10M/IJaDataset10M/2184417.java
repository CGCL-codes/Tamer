package com.coyousoft.adsys.logicimpl;

import static com.coyousoft.adsys.constant.Dao.emailOpenedLogDao;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sysolar.sun.cache.CacheList;
import com.coyousoft.adsys.entity.EmailOpenedLog;
import com.coyousoft.adsys.logic.EmailOpenedLogLogic;

public class EmailOpenedLogLogicImpl implements EmailOpenedLogLogic {

    private static final int CACHE_TIME = 180 * 1000;

    private static final CacheList<EmailOpenedLog> cacheListForCreate;

    private static final CacheList<EmailOpenedLog> cacheListForFetch;

    private static final Pattern pDomain = Pattern.compile("http[s]?://([\\w\\.]+).*");

    static {
        cacheListForCreate = new CacheList<EmailOpenedLog>(CACHE_TIME, 600);
        cacheListForFetch = new CacheList<EmailOpenedLog>(CACHE_TIME, 100);
    }

    public synchronized void create(EmailOpenedLog emailOpenedLog) throws SQLException {
        if (null != emailOpenedLog.getFromDomain()) {
            Matcher mDomain = pDomain.matcher(emailOpenedLog.getFromDomain());
            if (mDomain.find()) {
                emailOpenedLog.setFromDomain(mDomain.group(1));
            }
        }
        if (0 == emailOpenedLogDao.updateOpenedNum(emailOpenedLog.getEmailId(), emailOpenedLog.getClientIp())) {
            boolean added = false;
            for (EmailOpenedLog old : cacheListForCreate) {
                if (old.getEmailId().equals(emailOpenedLog.getEmailId()) && old.getClientIp().equals(emailOpenedLog.getClientIp())) {
                    added = true;
                    old.setUpdatedDate(new Date());
                    break;
                }
            }
            if (!added) {
                cacheListForCreate.add(emailOpenedLog);
            }
        }
        if (cacheListForCreate.isExpired()) {
            emailOpenedLogDao.create(cacheListForCreate);
            cacheListForCreate.clear();
        }
    }

    public synchronized void flush() throws SQLException {
        emailOpenedLogDao.create(cacheListForCreate);
    }

    public List<EmailOpenedLog> fetchList(int offset, int limit) throws Exception {
        synchronized (cacheListForFetch) {
            if (cacheListForFetch.isExpired()) {
                cacheListForFetch.clear();
            }
            if (cacheListForFetch.size() == 0) {
                cacheListForFetch.addAll(emailOpenedLogDao.fetchList(offset, limit));
            }
            return cacheListForFetch;
        }
    }
}
