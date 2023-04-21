package net.sf.rcpforms.widgets2;

import java.beans.PropertyChangeListener;
import net.sf.rcpforms.common.model.PropertyChangeSupport2;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Display;

public interface IRCPBeanControl {

    public void registerPrivateResource(final Resource resource);

    public boolean disposeIfPrivate(final Resource resource);

    public PropertyChangeSupport2 getPCS();

    Display getDisplay();

    public void addPropertyChangeListener(final PropertyChangeListener listener);

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener);

    public void removePropertyChangeListener(final PropertyChangeListener listener);

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener);
}
