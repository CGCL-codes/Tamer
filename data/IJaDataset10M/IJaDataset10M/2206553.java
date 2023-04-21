package com.sl.eventlog.service.user;

import com.sl.eventlog.domain.user.UrlAuthority;
import com.sl.eventlog.dao.HibernateBaseDao;
import com.sl.eventlog.dao.user.UrlAuthorityDao;
import java.util.List;

public class UrlAuthorityServiceImpl implements UrlAuthorityService {

    private UrlAuthorityDao urlAuthorityDao;

    public UrlAuthorityServiceImpl(UrlAuthorityDao urlAuthorityDao) {
        this.urlAuthorityDao = urlAuthorityDao;
    }

    public List<UrlAuthority> getAllUrlAndAttributeDefinitions() {
        return urlAuthorityDao.list();
    }
}
