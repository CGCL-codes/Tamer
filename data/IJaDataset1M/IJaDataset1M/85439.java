package org.jbfilter.impl;

import org.hibernate.Session;

public interface HibernateCallback<T> {

    T doInHibernate(Session session);
}
