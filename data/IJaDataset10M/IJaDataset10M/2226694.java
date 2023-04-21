package org.variance.configuration;

import java.util.ArrayList;
import java.util.List;
import org.variance.DefaultSessionFactory;
import org.variance.SessionFactory;

/**
 *
 *
 * @todo Auto-generated doc stub for Configuration.
 *
 * @author Matthew Purland
 */
public class Configuration {

    protected List<SessionFactory> sessionFactoryList = new ArrayList<SessionFactory>();

    public void close() {
        for (SessionFactory sessionFactory : sessionFactoryList) {
            sessionFactory.close();
        }
        sessionFactoryList.clear();
    }

    public SessionFactory buildSessionFactory() {
        SessionFactory sessionFactory = new DefaultSessionFactory();
        sessionFactoryList.add(sessionFactory);
        return sessionFactory;
    }
}
