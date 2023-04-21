package com.googlecode.yoohoo.xmppcore.connection;

import java.util.Map;
import java.util.Set;
import org.osgi.framework.BundleContext;

public interface IConnectionContext {

    void setAttribute(Object key, Object value);

    Object getAttribute(Object key);

    Object getAttribute(Object key, Object defaultValue);

    Object removeAttribute(Object key);

    Set<Object> getAttributesKeys();

    Map<Object, Object> getAttributes();

    void write(Object message);

    boolean write(Object message, boolean sync);

    void close();

    boolean close(boolean sync);

    String getConnectorType();

    BundleContext getBundleContext();
}
