package org.eclipse.uml2.uml.edit.providers;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;

public class UMLResourceItemProviderAdapterFactory extends ResourceItemProviderAdapterFactory {

    public UMLResourceItemProviderAdapterFactory() {
        super();
    }

    @Override
    public Adapter createResourceAdapter() {
        return new UMLResourceItemProvider(this);
    }
}
