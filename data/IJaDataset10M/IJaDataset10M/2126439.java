package org.greatlogic.gae.dao;

import com.google.appengine.repackaged.com.google.common.collect.*;
import java.lang.reflect.*;
import java.util.*;
import org.greatlogic.gae.*;
import org.greatlogic.gae.dao.DAOEnums.EDAOAction;

class DAOConstructors {

    private Map<Class<? extends DAOBase>, Constructor<? extends DAOBase>> _constructorByDAOClassMap;

    DAOConstructors() {
        _constructorByDAOClassMap = Maps.newHashMap();
    }

    Constructor<? extends DAOBase> getConstructor(final Class<? extends DAOBase> daoClass) {
        Constructor<? extends DAOBase> result;
        synchronized (_constructorByDAOClassMap) {
            result = _constructorByDAOClassMap.get(daoClass);
            if (result == null && !_constructorByDAOClassMap.containsKey(daoClass)) {
                try {
                    result = daoClass.getDeclaredConstructor(EDAOAction.class, Object.class, String[].class);
                    _constructorByDAOClassMap.put(daoClass, result);
                } catch (Exception e) {
                    GLLog.warning("Error attempting to get the constructor for:" + daoClass, e);
                    _constructorByDAOClassMap.put(daoClass, null);
                }
            }
        }
        return result;
    }
}
