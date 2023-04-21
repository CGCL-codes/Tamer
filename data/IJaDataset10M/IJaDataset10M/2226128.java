package org.netbeans.modules.flexbean.project.module.libraries;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.netbeans.spi.project.libraries.LibraryImplementation;
import org.netbeans.spi.project.libraries.LibraryProvider;
import org.netbeans.spi.project.libraries.support.LibrariesSupport;

/**
 *
 * @author arnaud
 */
public final class LibraryProviderImpl implements LibraryProvider {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public LibraryImplementation[] getLibraries() {
        return new LibraryImplementation[] { LibrariesSupport.createLibraryImplementation(LibraryTypeProviderImpl.LIBRARY_TYPE, new String[] { "classpath" }) };
    }
}
