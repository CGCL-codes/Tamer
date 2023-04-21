package net.sf.brightside.stockswatcher.server.service.hibernate;

import net.sf.brightside.stockswatcher.server.core.spring.IBeansProvider;
import net.sf.brightside.stockswatcher.server.metamodel.Share;
import net.sf.brightside.stockswatcher.server.metamodel.ShareHistory;
import net.sf.brightside.stockswatcher.server.service.api.hibernate.ICreateShareHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class CreateShareHistoryImpl extends HibernateDaoSupport implements ICreateShareHistory {

    private IBeansProvider beansProvider;

    public IBeansProvider getBeansProvider() {
        return beansProvider;
    }

    public void setBeansProvider(IBeansProvider beansProvider) {
        this.beansProvider = beansProvider;
    }

    public Session getManager() {
        SessionFactory sf = getSessionFactory();
        return sf.getCurrentSession();
    }

    @Transactional
    public ShareHistory createHistory(Share share) {
        ShareHistory shareHistory = beansProvider.getBean(ShareHistory.class);
        shareHistory.setShare(share);
        getManager().saveOrUpdate(shareHistory);
        return shareHistory;
    }
}
