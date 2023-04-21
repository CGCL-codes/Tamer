package com.ynhenc.topis.mobile.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ynhenc.topis.mobile.web.*;

public class RoadDAO extends MobileDAO<Road> {

    @Override
    public RoadList getList(WebBean webBean, Page page) {
        log.debug("finding roadList");
        try {
            String namedQuery = "roadList";
            Session session = this.getSession();
            Query query = session.getNamedQuery(namedQuery);
            String name = webBean.getKeyWord();
            if (name == null) {
                name = "";
            }
            name = "%" + name + "%";
            query.setString("name", name);
            query.setString("id", webBean.getId());
            query.setString("id", webBean.getId());
            String queryString = query.getQueryString();
            boolean localDebug = false;
            if (localDebug) {
                this.debug("queryString: " + queryString);
            }
            List list = query.list();
            RoadList listFilter = new RoadList();
            this.getListByPage(list, page, listFilter);
            this.closeSession(session);
            return listFilter;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
}
