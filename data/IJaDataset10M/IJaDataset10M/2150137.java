package werkzeugkasten.factory.impl;

import werkzeugkasten.factory.AdapterFactory;
import werkzeugkasten.factory.AdapterManager;

public class DefaultAdapterManager implements AdapterManager {

    public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
        return null;
    }

    public <T> void register(AdapterFactory<T> factory, Class<T> atapterType) {
    }

    public <T> void unregister(AdapterFactory<T> factory) {
    }

    public <T> void unregister(AdapterFactory<T> factory, Class<T> atapterType) {
    }
}
