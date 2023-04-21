package org.jalcedo.runtime.dao.finder.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import org.jalcedo.runtime.dao.finder.FinderExecutor;
import org.jalcedo.runtime.dao.impl.GenericDaoJpaImpl;

public class FinderInvocationHandler<T, PK extends Serializable> implements InvocationHandler {

    private GenericDaoJpaImpl<T, PK> genericDao;

    public FinderInvocationHandler(Class<T> type, EntityManager entityManager) {
        this.genericDao = new GenericDaoJpaImpl<T, PK>(type, entityManager);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        FinderExecutor genericDao = (FinderExecutor) this.genericDao;
        String methodName = method.getName();
        if (methodName.startsWith("find")) {
            return genericDao.executeFinder(method, args);
        } else {
            return method.invoke(this.genericDao, args);
        }
    }
}
