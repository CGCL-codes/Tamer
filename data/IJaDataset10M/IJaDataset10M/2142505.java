package com.gwtaf.portal.client.part;

import com.gwtaf.portal.client.factory.GenericRegistry;

public class PortalPartRegistry extends GenericRegistry<IPortalPart, IPortalPartDescriptor> implements IPortalPartRegistry {

    private static final IPortalPartRegistry instance = new PortalPartRegistry();

    public static IPortalPartRegistry get() {
        return instance;
    }

    public PortalPartRegistry() {
    }
}
