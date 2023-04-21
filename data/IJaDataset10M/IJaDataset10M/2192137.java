package maze.commons.ee.hibernate.impl;

import maze.commons.ee.hibernate.HibernateSaver;
import maze.commons.ee.hibernate.HibernateTransactional;
import maze.commons.ee.hibernate.HibernateTransactionalMan;
import maze.commons.ee.hibernate.HibernateTransactionalManFactory;
import org.hibernate.Session;

/**
 * @author Normunds Mazurs
 * 
 */
public class HibernateSaverImpl<X> extends HibernateUpdaterBase implements HibernateSaver<X> {

    public HibernateSaverImpl(final HibernateTransactionalManFactory hibernateTransactionalManFactory) {
        super(hibernateTransactionalManFactory);
    }

    public HibernateSaverImpl() {
        this(DefHibernateTransactionalManFactoryImpl.getInstance());
    }

    protected X save(final X x, final Session session) {
        session.save(x);
        return x;
    }

    @Override
    public X save(final X x) {
        assert x != null;
        final X saved;
        final HibernateTransactionalMan txaMan = createTxaMan();
        try {
            final HibernateTransactional txa = beginTxa(txaMan);
            saved = save(x, getSession(txa));
            commit(txa);
        } finally {
            end(txaMan);
        }
        return saved;
    }
}
