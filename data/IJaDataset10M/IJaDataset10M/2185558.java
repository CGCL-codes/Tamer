package org.larz.dom3.dm;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.ISetup;
import org.eclipse.emf.ecore.resource.Resource;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Generated from StandaloneSetup.xpt!
 */
@SuppressWarnings("all")
public class DmStandaloneSetupGenerated implements ISetup {

    public Injector createInjectorAndDoEMFRegistration() {
        if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("ecore")) Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl());
        if (!Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().containsKey("xmi")) Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl());
        if (!EPackage.Registry.INSTANCE.containsKey(org.eclipse.xtext.XtextPackage.eNS_URI)) EPackage.Registry.INSTANCE.put(org.eclipse.xtext.XtextPackage.eNS_URI, org.eclipse.xtext.XtextPackage.eINSTANCE);
        Injector injector = createInjector();
        register(injector);
        return injector;
    }

    public Injector createInjector() {
        return Guice.createInjector(new org.larz.dom3.dm.DmRuntimeModule());
    }

    public void register(Injector injector) {
        if (!EPackage.Registry.INSTANCE.containsKey("http://www.larz.org/dom3/dm/Dm")) {
            EPackage.Registry.INSTANCE.put("http://www.larz.org/dom3/dm/Dm", org.larz.dom3.dm.dm.DmPackage.eINSTANCE);
        }
        org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
        org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector.getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("dm", resourceFactory);
        org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("dm", serviceProvider);
    }
}
