package org.tm4j.topicmap.hibernate;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorBase;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.net.LocatorResolutionException;
import org.tm4j.net.MalformedLocatorException;
import org.tm4j.net.URILocator;
import org.tm4j.net.URILocatorHelper;
import org.tm4j.topicmap.TopicMapRuntimeException;
import java.util.List;

/**
 * A URI locator implementation for the Hibernate backend that does not
 * persist itself in the backend store. This is the default implementation
 * created by the LocatorFactory implementation. It can only be persisted
 * by a call to the package-scope makePersistent() method which
 * returns a URILocatorImpl.
 */
public class TransientURILocatorImpl extends LocatorBase implements URILocator {

    private String m_address;

    private LocatorFactoryImpl m_factory;

    TransientURILocatorImpl(String address, LocatorFactoryImpl factory) throws MalformedLocatorException {
        m_address = URILocatorHelper.normalizeAddress(address);
        m_factory = factory;
    }

    URILocatorImpl makePersistent() {
        return m_factory.createPersistentLocator(this);
    }

    public String getNotation() {
        return "URI";
    }

    public String getAddress() {
        return m_address;
    }

    public String getAuthority() {
        try {
            return URILocatorHelper.getAuthority(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public String getFragment() {
        try {
            return URILocatorHelper.getFragment(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public List getPath() {
        try {
            return URILocatorHelper.getPath(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public String getPathString() {
        try {
            return URILocatorHelper.getPathString(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public String getQuery() {
        try {
            return URILocatorHelper.getQuery(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public String getScheme() {
        try {
            return URILocatorHelper.getScheme(m_address);
        } catch (MalformedLocatorException ex) {
            throw new TopicMapRuntimeException("INTERNAL ERROR: Unexpected MalformedLocatorException", ex);
        }
    }

    public Locator copy() throws LocatorFactoryException {
        return m_factory.createLocator("URI", m_address);
    }

    public boolean equals(Locator loc) {
        if (loc == null) {
            return false;
        }
        if (loc.getNotation().equalsIgnoreCase("URI")) {
            try {
                return getAddress().equals(URILocatorHelper.normalizeAddress(loc.getAddress()));
            } catch (Exception ex) {
            }
        }
        return false;
    }

    public Locator resolveRelative(String rel) throws LocatorResolutionException {
        try {
            String resolvedAddress = URILocatorHelper.resolveRelative(m_address, rel);
            return m_factory.createLocator("URI", resolvedAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new LocatorResolutionException(ex.toString());
        }
    }

    public Locator resolveRelative(Locator rel) throws LocatorResolutionException {
        if (!isSupportedNotation(rel.getNotation())) {
            throw new LocatorResolutionException("Expected 'URI' notation relative locator.");
        }
        return resolveRelative(rel.getAddress());
    }

    public boolean isSupportedNotation(String notation) {
        return notation.equalsIgnoreCase("URI");
    }

    public String normalizeAddress(String address) throws MalformedLocatorException {
        return URILocatorHelper.normalizeAddress(address);
    }

    public int hashCode() {
        String hashString = "URI:" + getAddress();
        return hashString.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof Locator) {
            return getAddress().equals(((Locator) other).getAddress());
        }
        return false;
    }
}
