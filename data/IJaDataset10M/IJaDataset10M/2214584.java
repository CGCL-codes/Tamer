package org.osgi.framework;

public abstract interface ServiceRegistration {

    ServiceReference getReference();

    void unregister();
}
