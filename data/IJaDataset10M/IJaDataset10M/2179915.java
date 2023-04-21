package org.wapps.persistence.impl.finder;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author hupe1980 at users.sourceforge.net
 *
 */
public interface IFinderExecutor<T> {

    List<T> executeFinder(Method method, Object[] queryArgs);

    T executeSelectSingle(Method method, Object[] queryArgs);

    long executeCount(Method method, Object[] queryArgs);

    int executeBulk(Method method, Object[] queryArgs);

    Class getType();
}
