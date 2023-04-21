package com.csft.market.domain;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.csft.hibernate.BaseHibernateDaoSupport;

@Repository
@Transactional
public class AnalysisDaoImpl extends BaseHibernateDaoSupport implements AnalysisDao {

    @Autowired
    public AnalysisDaoImpl(@Qualifier("sessionFactory") SessionFactory sf) {
        super();
        setSessionFactory(sf);
    }

    @Override
    public List<Analysis> getAnalysises(String ticker, String category) {
        Session s = getSession();
        Query q;
        if (ticker != null) {
            String sql = "from Analysis where ticker=:ticker and category=:category order by lastModifiedDate desc";
            q = s.createQuery(sql);
            q.setParameter("ticker", ticker);
            q.setParameter("category", category);
        } else {
            String sql = "from Analysis where category=:category order by lastModifiedDate desc";
            q = s.createQuery(sql);
            q.setParameter("category", category);
        }
        List<?> l = q.list();
        List<Analysis> as = new ArrayList<Analysis>();
        Analysis a = null;
        for (Object o : l) {
            a = (Analysis) o;
            as.add(a);
        }
        return as;
    }

    @Override
    public List<String> getRatedTickers() {
        Session s = getSession();
        Query q = s.createQuery("select distinct ticker from Analysis order by ticker");
        List<?> l = q.list();
        List<String> ts = new ArrayList<String>();
        for (Object o : l) {
            String t = (String) o;
            ts.add(t);
        }
        return ts;
    }
}
