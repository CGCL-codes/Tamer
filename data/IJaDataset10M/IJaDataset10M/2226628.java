package org.mftech.dawn.runtime.services;

import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.mftech.dawn.runtime.client.synchronization.DawnElementTypeHelperFactory;
import org.mftech.dawn.runtime.client.synchronization.DawnResourceSetHelperFactory;
import org.mftech.dawn.runtime.client.synchronization.ResourceSynchronizer;
import org.mftech.dawn.runtime.client.synchronization.ResourceSyncronizerFactory;
import org.mftech.dawn.runtime.services.impl.DawnExtensionServiceImpl;

public interface DawnExtensionService {

    DawnExtensionService mfInstance = new DawnExtensionServiceImpl();

    public DawnElementTypeHelperFactory createDawnElementTypeHelperFactory(String pluginId);

    public DawnResourceSetHelperFactory createDawnResourceSetHelperFactory(String pluginId);
}
