package org.octave.ide.currdir.spi;

import java.beans.PropertyChangeListener;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Heiko Hofer
 */
public interface CurrentDirectoryProvider {

    public static final String PROPERTY_CURRENT_DIRECTORY = "CurrentDirectory";

    public FileObject getCurrentDirectory();

    public void addPropertyChangeListener(PropertyChangeListener listner);

    public void removePropertyChangeListener(PropertyChangeListener listner);
}
