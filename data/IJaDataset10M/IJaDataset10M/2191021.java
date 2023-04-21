package org.dynamo.database.reverse.config.reader.internal;

import java.io.IOException;
import java.util.Collections;
import org.dynamo.database.reverse.emf.configuration.configuration.Configuration;
import org.dynamo.database.reverse.emf.configuration.configuration.ConfigurationFactory;
import org.dynamo.database.reverse.emf.configuration.configuration.ConfigurationPackage;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class ConfigLoader {

    private static ResourceSet initResourceSet() {
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getPackageRegistry().put(ConfigurationPackage.eNS_URI, ConfigurationPackage.eINSTANCE);
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        return resourceSet;
    }

    public static ConfigurationFactory initModel() {
        return ConfigurationFactory.eINSTANCE;
    }

    public static void save(String path, Configuration configuration) {
        ResourceSet resourceSet = initResourceSet();
        URI fileURI = URI.createPlatformResourceURI(path, true);
        Resource resource = resourceSet.createResource(fileURI);
        resource.getContents().add(configuration);
        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration load(String path) {
        ResourceSet resourceSet = initResourceSet();
        Resource load_resource = resourceSet.getResource(URI.createPlatformResourceURI(path, true), true);
        Configuration configuration = (Configuration) load_resource.getContents().get(0);
        return configuration;
    }

    public static Configuration load(IFile path) {
        ResourceSet resourceSet = initResourceSet();
        URI uri = URI.createPlatformResourceURI(path.getFullPath().toString(), true);
        Resource load_resource = resourceSet.getResource(uri, true);
        Configuration configuration = (Configuration) load_resource.getContents().get(0);
        return configuration;
    }
}
