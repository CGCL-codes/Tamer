package com.liferay.counter.service;

import com.liferay.portal.SystemException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * <a href="CounterServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CounterServiceUtil {

    public static List getNames() throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        return counterService.getNames();
    }

    public static long increment() throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        return counterService.increment();
    }

    public static long increment(String name) throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        return counterService.increment(name);
    }

    public static long increment(String name, int size) throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        return counterService.increment(name, size);
    }

    public static void rename(String oldName, String newName) throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        counterService.rename(oldName, newName);
    }

    public static void reset(String name) throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        counterService.reset(name);
    }

    public static void reset(String name, long size) throws RemoteException, SystemException {
        CounterService counterService = CounterServiceFactory.getService();
        counterService.reset(name, size);
    }
}
