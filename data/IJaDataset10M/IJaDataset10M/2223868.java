package com.codemonster.surinam.core.framework;

import com.codemonster.surinam.export.core.ServiceFinder;
import com.codemonster.surinam.export.framework.ServiceBlock;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is designed to make it easy to acquire ServiceFinders for any given Service Block. Finders
 * are cached, shared and tied to individual SBs so the finder you get will depend on the SB you give
 * while multiple requests given the same SB returns the same Service Finder.
 */
public class ServiceFinderFactory {

    private static ConcurrentHashMap<ServiceBlock, ServiceFinder> finderMap = new ConcurrentHashMap<ServiceBlock, ServiceFinder>();

    private ServiceFinderFactory() {
    }

    /**
     * We will use the factory to create instances of ServiceFinders, which use the given ServiceBlock
     * as it's context. The design of this implies that there might be more than one ServiceBlock one but
     * the current restriction keeps the relationship one to one.
     *
     * @param serviceBlock This is the Service Block that the finder will use for its searches.
     * @return Returns the ServiceFinder instance.
     */
    public static ServiceFinder getFinder(ServiceBlock serviceBlock) {
        ServiceFinder finder = finderMap.get(serviceBlock);
        if (null == finder) {
            finder = new ServiceFinderImpl(serviceBlock);
            finderMap.put(serviceBlock, finder);
        }
        return finder;
    }
}
