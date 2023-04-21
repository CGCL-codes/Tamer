package org.sqlanyware.sqlwclient.api.services.hibernate;

import org.hibernate.SessionFactory;
import org.sqlanyware.sqlwclient.api.services.IService;

public interface ISessionFactoryProvider extends IService {

    SessionFactory getSessionFactory();
}
