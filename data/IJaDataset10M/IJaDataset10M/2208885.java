package org.imogene.model.core.presentation.custom;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 
 * @author MEDES-IMPS
 *
 */
public class CustomAdapterFactoryContentProvider extends AdapterFactoryContentProvider {

    /**
	 * 
	 * @param adapterFactory
	 */
    public CustomAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource) {
        return new CustomPropertySource(object, itemPropertySource);
    }
}
