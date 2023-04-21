package org.apache.felix.cm;

import java.io.File;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * The <code>MockBundleContext</code> is a dummy implementation of the
 * <code>BundleContext</code> interface. No methods are implemented here, that
 * is all methods have no effect and return <code>null</code> if a return value
 * is specified.
 * <p>
 * Extensions may overwrite methods as see fit.
 */
public class MockBundleContext implements BundleContext {

    private final Properties properties = new Properties();

    public void setProperty(String name, String value) {
        if (value == null) {
            properties.remove(name);
        } else {
            properties.setProperty(name, value);
        }
    }

    public void addBundleListener(BundleListener arg0) {
    }

    public void addFrameworkListener(FrameworkListener arg0) {
    }

    public void addServiceListener(ServiceListener arg0) {
    }

    public void addServiceListener(ServiceListener arg0, String arg1) throws InvalidSyntaxException {
    }

    public Filter createFilter(String arg0) throws InvalidSyntaxException {
        return null;
    }

    public ServiceReference[] getAllServiceReferences(String arg0, String arg1) throws InvalidSyntaxException {
        return null;
    }

    public Bundle getBundle() {
        return null;
    }

    public Bundle getBundle(long arg0) {
        return null;
    }

    public Bundle[] getBundles() {
        return new Bundle[0];
    }

    public File getDataFile(String arg0) {
        return null;
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public Object getService(ServiceReference arg0) {
        return null;
    }

    public ServiceReference getServiceReference(String arg0) {
        return null;
    }

    public ServiceReference[] getServiceReferences(String arg0, String arg1) throws InvalidSyntaxException {
        return null;
    }

    public Bundle installBundle(String arg0) throws BundleException {
        return null;
    }

    public Bundle installBundle(String arg0, InputStream arg1) throws BundleException {
        return null;
    }

    public ServiceRegistration registerService(String[] arg0, Object arg1, Dictionary arg2) {
        return null;
    }

    public ServiceRegistration registerService(String arg0, Object arg1, Dictionary arg2) {
        return null;
    }

    public void removeBundleListener(BundleListener arg0) {
    }

    public void removeFrameworkListener(FrameworkListener arg0) {
    }

    public void removeServiceListener(ServiceListener arg0) {
    }

    public boolean ungetService(ServiceReference arg0) {
        return false;
    }
}
