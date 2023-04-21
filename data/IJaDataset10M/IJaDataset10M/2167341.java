package oss.jthinker.swingutils;

import oss.jthinker.util.Trigger;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Adapter of {@link Component} to {@link Trigger}
 * interface with {@link Point} as a unit of state. It's important to keep
 * in mind that central point of component is used as it's "point state",
 * not anything else. Another important point is that trigger is using
 * asynchronous AWT event queue as information source, that's why
 * update of component's bounds doesn't cause immediate update of the trigger.
 * 
 * @author iappel
 */
public final class ComponentLocationTrigger extends Trigger<Point> {

    private final Component component;

    /**
     * Extension of {@link ComponentAdapter} that fires state update
     * on component's location/size change.
     */
    private class PositionChangeListener extends ComponentAdapter {

        @Override
        public void componentMoved(ComponentEvent e) {
            updateLocation();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            updateLocation();
        }
    }

    /**
     * Creates a new instance of ComponentTrigger around provided component.
     * 
     * @param base component to attach trigger to
     */
    public ComponentLocationTrigger(Component base) {
        if (base == null) {
            throw new IllegalArgumentException("Null component not allowed");
        }
        component = base;
        component.addComponentListener(new PositionChangeListener());
        updateLocation();
    }

    /** 
     * Immediately calculates component's location and updates trigger state
     * accordingly.
     */
    public void updateLocation() {
        Point newValue = WindowUtils.computeCenterPoint(component);
        setState(newValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComponentLocationTrigger) {
            ComponentLocationTrigger ct = (ComponentLocationTrigger) obj;
            return component.equals(ct.component);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (component == null) {
            return 0;
        } else {
            return component.hashCode();
        }
    }
}
