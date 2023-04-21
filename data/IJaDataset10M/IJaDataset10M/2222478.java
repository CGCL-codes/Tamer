package br.com.nix.adapterfactories;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import br.com.nix.model.Room;
import br.com.nix.propertysources.RoomPropertySource;

public class RoomAdapterFactory implements IAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IPropertySource.class) return new RoomPropertySource((Room) adaptableObject);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }
}
