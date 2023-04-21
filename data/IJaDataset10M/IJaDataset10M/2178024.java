package net.sf.brightside.mobilestock.service.hibernate;

import java.util.List;
import net.sf.brightside.mobilestock.metamodel.api.MonitoringSession;
import net.sf.brightside.mobilestock.metamodel.api.Share;
import net.sf.brightside.mobilestock.service.api.DeleteMonitoringSessionOnly;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class DeleteMonitoringSessionOnlyImpl extends HibernateDaoSupport implements DeleteMonitoringSessionOnly<Boolean> {

    Logger log = Logger.getLogger(DeleteMonitoringSessionOnly.class);

    private MonitoringSession ms;

    public void setMonitoringSession(MonitoringSession msb) {
        this.ms = msb;
    }

    public Session provideManager() {
        return getSessionFactory().getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Boolean execute() {
        provideManager().update(getMonitoringSession());
        log.info("Size: " + getMonitoringSession().getShares().size());
        List<Share> shares = getMonitoringSession().getShares();
        int size = getMonitoringSession().getShares().size();
        for (int i = size - 1; i >= 0; i--) {
            Share share = shares.get(i);
            provideManager().update(share);
            getMonitoringSession().getShares().remove(share);
            provideManager().saveOrUpdate(share);
        }
        provideManager().update(getMonitoringSession().getUser());
        getMonitoringSession().setUser(null);
        provideManager().update(getMonitoringSession());
        provideManager().delete(getMonitoringSession());
        return true;
    }

    public MonitoringSession getMonitoringSession() {
        return this.ms;
    }
}
