package org.xtext.cg2009;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.ISetup;
import org.eclipse.emf.ecore.resource.Resource;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Generated from StandaloneSetup.xpt!
 */
public class EntitiesStandaloneSetup implements ISetup {

    public static void doSetup() {
        new EntitiesStandaloneSetup().createInjectorAndDoEMFRegistration();
    }

    public Injector createInjectorAndDoEMFRegistration() {
        org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();
        Injector injector = createInjector();
        register(injector);
        return injector;
    }

    public Injector createInjector() {
        return Guice.createInjector(new org.xtext.cg2009.EntitiesRuntimeModule());
    }

    public void register(Injector injector) {
        if (!EPackage.Registry.INSTANCE.containsKey("http://www.xtext.org/cg2009/Entities")) {
            EPackage.Registry.INSTANCE.put("http://www.xtext.org/cg2009/Entities", org.xtext.cg2009.entities.EntitiesPackage.eINSTANCE);
        }
        org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("entity", resourceFactory);
    }
}
