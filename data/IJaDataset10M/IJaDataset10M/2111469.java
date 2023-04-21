package org.makagiga.commons;

import java.beans.PropertyChangeListener;
import java.security.BasicPermission;
import java.text.ParseException;

/**
 * @since 2.0
 */
public final class SecureProperty<T> implements PropertyAccess<T> {

    private PropertyAccess<T> impl;

    /**
	 * @throws NullPointerException If @p property is @c null
	 */
    public SecureProperty(final PropertyAccess<T> property) {
        impl = TK.checkNull(property, "property");
    }

    /**
	 * @throws SecurityException If access is not permitted
	 */
    public void checkRead() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(new SecureProperty.Permission("read"));
    }

    /**
	 * @throws SecurityException If access is not permitted
	 */
    public void checkWrite() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(new SecureProperty.Permission("write"));
    }

    /**
	 * Returns the non-null property guarded by this secure property.
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public PropertyAccess<T> getProperty() {
        checkRead();
        return impl;
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 * 
	 * @since 2.4
	 */
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        checkWrite();
        impl.addPropertyChangeListener(l);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 * 
	 * @since 2.4
	 */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        checkRead();
        return impl.getPropertyChangeListeners();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 * 
	 * @since 2.4
	 */
    public void removePropertyChangeListener(final PropertyChangeListener l) {
        checkWrite();
        impl.removePropertyChangeListener(l);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public void clear() {
        checkWrite();
        impl.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            SecureProperty<T> sp = (SecureProperty<T>) super.clone();
            sp.impl = (PropertyAccess<T>) impl.clone();
            return sp;
        } catch (CloneNotSupportedException exception) {
            throw new WTFError(exception);
        }
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public int compareTo(final PropertyAccess<T> another) {
        checkRead();
        return impl.compareTo(another);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    @Override
    public boolean equals(final Object o) {
        checkRead();
        if (o == this) return true;
        return impl.equals(o);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public T get() {
        checkRead();
        return impl.get();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public void set(final T value) {
        checkWrite();
        impl.set(value);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public T getDefaultValue() {
        checkRead();
        return impl.getDefaultValue();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public boolean isDefaultValue() {
        checkRead();
        return impl.isDefaultValue();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public void setDefaultValue(final T value) {
        checkWrite();
        impl.setDefaultValue(value);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 * 
	 * @since 2.4
	 */
    public Class<T> getType() {
        checkRead();
        return impl.getType();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    @Override
    public int hashCode() {
        checkRead();
        return impl.hashCode();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public boolean isNull() {
        checkRead();
        return impl.isNull();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    public void parse(final String value) throws ParseException {
        checkWrite();
        impl.parse(value);
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 * 
	 * @since 2.4
	 */
    public void reset() {
        checkWrite();
        impl.reset();
    }

    /**
	 * @inheritDoc
	 * 
	 * @throws SecurityException If access is not permitted
	 */
    @Override
    public String toString() {
        checkRead();
        return impl.toString();
    }

    /**
	 * A security permission used by the @ref SecureProperty.checkRead and @ref SecureProperty.checkWrite.
	 */
    public static final class Permission extends BasicPermission {

        private Permission(final String name) {
            super(name);
        }
    }
}
