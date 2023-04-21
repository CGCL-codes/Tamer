package br.net.woodstock.rockframework.persistence.orm.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import br.net.woodstock.rockframework.domain.config.DomainLog;

public final class HibernatePersistenceHelper implements PersistenceHelper<Session> {

    private static HibernatePersistenceHelper instance = new HibernatePersistenceHelper();

    private static ThreadLocal<Session> session = new ThreadLocal<Session>();

    private SessionFactory factory;

    private HibernatePersistenceHelper() {
        super();
        this.factory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void close() {
        Session s = HibernatePersistenceHelper.session.get();
        if (s != null) {
            Transaction t = s.getTransaction();
            if (t.isActive()) {
                DomainLog.getInstance().getLog().warning("Session contains an active transaction, commiting transaction");
                t.commit();
            }
            s.flush();
            s.close();
            HibernatePersistenceHelper.session.set(null);
        }
    }

    @Override
    public Session get() {
        Session s = HibernatePersistenceHelper.session.get();
        if (s == null) {
            s = this.factory.openSession();
            HibernatePersistenceHelper.session.set(s);
        }
        return s;
    }

    public static HibernatePersistenceHelper getInstance() {
        return HibernatePersistenceHelper.instance;
    }
}
