package net.entropysoft.dashboard.plugin.dashboard.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class ModelObject extends PlatformObject implements IPropertySource {

    /** An empty property descriptor. */
    private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];

    /** The property change support delegate */
    private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

    /** 
	 * Attach a non-null PropertyChangeListener to this object.
	 * @param l a non-null PropertyChangeListener instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        pcsDelegate.addPropertyChangeListener(l);
    }

    /** 
	 * Remove a PropertyChangeListener from this component.
	 * @param l a PropertyChangeListener instance
	 */
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            pcsDelegate.removePropertyChangeListener(l);
        }
    }

    /** 
	 * Report a property change to registered listeners (for example edit parts).
	 * @param property the programmatic name of the property that changed
	 * @param oldValue the old value of this property
	 * @param newValue the new value of this property
	 */
    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    /**
	 * Returns a value for this property source that can be edited in a property sheet.
	 * <p>My personal rule of thumb:</p>
	 * <ul>
	 * <li>model elements should return themselves and</li> 
	 * <li>custom IPropertySource implementations (like DimensionPropertySource in the GEF-logic
	 * example) should return an editable value.</li>
	 * </ul>
	 * <p>Override only if necessary.</p>
	 * @return this instance
	 */
    public Object getEditableValue() {
        return this;
    }

    /**
	 * Children should override this. The default implementation returns an empty array.
	 */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return EMPTY_ARRAY;
    }

    /**
	 * Children should override this. The default implementation returns null.
	 */
    public Object getPropertyValue(Object id) {
        return null;
    }

    /**
	 * Children should override this. The default implementation returns false.
	 */
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
	 * Children should override this. The default implementation does nothing.
	 */
    public void resetPropertyValue(Object id) {
    }

    /**
	 * Children should override this. The default implementation does nothing.
	 */
    public void setPropertyValue(Object id, Object value) {
    }
}
