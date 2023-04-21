package sk.sigp.tetras.frontend.utils;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;

public class OpenSessionInViewFilter extends org.springframework.orm.hibernate3.support.OpenSessionInViewFilter {

    @Override
    protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        Session ses = super.getSession(sessionFactory);
        ses.setFlushMode(FlushMode.AUTO);
        return ses;
    }

    @Override
    protected void closeSession(Session session, SessionFactory sessionFactory) {
        session.flush();
        super.closeSession(session, sessionFactory);
    }
}
