package ru.aslanov.schedule.server.services;

import ru.aslanov.schedule.model.Entity;
import ru.aslanov.schedule.model.Operation;
import ru.aslanov.schedule.server.AbstractService;
import ru.aslanov.schedule.server.AccessDeniedException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceAware;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * Created: Feb 3, 2010 6:50:15 PM
 *
 * @author Sergey Aslanov
 */
@PersistenceAware
public abstract class ParentChildService<Child extends Entity, Parent extends Entity> extends AbstractService<Child> {

    private String parentRequestParam;

    private String parentRef;

    private String orderBy;

    protected ParentChildService(String parentRequestParam, String parentRef, String orderBy) {
        this.parentRequestParam = parentRequestParam;
        this.parentRef = parentRef;
        this.orderBy = orderBy;
    }

    protected abstract Class<Parent> getParentClass();

    @Override
    protected Object doFetch(HttpServletRequest request) throws Exception {
        PersistenceManager pm = getPersistanceManager();
        Query query = null;
        try {
            final String key = request.getParameter("encodedKey");
            Collection res;
            if (key == null) {
                final String parentKey = getParentKey(request);
                if (parentKey == null) throw new RuntimeException(parentRequestParam + " must be specified");
                checkParentViewAccess(parentKey, request);
                query = pm.newQuery(getEntityClass(), parentRef + " == :p");
                if (orderBy != null) {
                    query.setOrdering(orderBy);
                }
                res = (Collection) query.execute(parentKey);
            } else {
                final Child obj = pm.getObjectById(getEntityClass(), key);
                if (!checkPermission(obj, request, pm, Operation.VIEW)) {
                    throw new AccessDeniedException(request);
                }
                res = Collections.singleton(obj);
            }
            return convertToDataList(res, request, pm);
        } finally {
            if (query != null) query.closeAll();
        }
    }

    protected abstract void checkParentViewAccess(String parentKey, HttpServletRequest request) throws AccessDeniedException;

    @Override
    protected void setParameter(Child object, String paramName, String paramValue, PersistenceManager pm) throws Exception {
        if (parentRequestParam.equals(paramName)) {
        } else {
            super.setParameter(object, paramName, paramValue, pm);
        }
    }

    protected Parent getParent(PersistenceManager pm, HttpServletRequest request) {
        final String parentKey = getParentKey(request);
        final Parent parent = pm.getObjectById(getParentClass(), parentKey);
        return parent;
    }

    protected String getParentKey(HttpServletRequest request) {
        return request.getParameter(parentRequestParam);
    }
}
