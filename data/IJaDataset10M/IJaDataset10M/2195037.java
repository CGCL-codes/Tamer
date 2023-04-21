package com.liferay.counter.service;

import com.liferay.portal.SystemException;
import java.util.List;

/**
 * <a href="CounterLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CounterLocalServiceUtil {

    public static List getNames() throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        return counterService.getNames();
    }

    public static long increment() throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        return counterService.increment();
    }

    public static long increment(String name) throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        return counterService.increment(name);
    }

    public static long increment(String name, int size) throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        return counterService.increment(name, size);
    }

    public static void rename(String oldName, String newName) throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        counterService.rename(oldName, newName);
    }

    public static void reset(String name) throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        counterService.reset(name);
    }

    public static void reset(String name, long size) throws SystemException {
        CounterLocalService counterService = CounterLocalServiceFactory.getService();
        counterService.reset(name, size);
    }
}
