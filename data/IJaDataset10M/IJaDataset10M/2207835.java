package net.sourceforge.taggerplugin.resource;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IAdapterFactory;

public class TaggedMarkerAdapterFactory implements IAdapterFactory {

    private static final Class[] ADAPTERS = { ITaggedMarker.class };

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        return (new TaggedResourceMarker((IMarker) adaptableObject));
    }

    public Class[] getAdapterList() {
        return (ADAPTERS);
    }
}
