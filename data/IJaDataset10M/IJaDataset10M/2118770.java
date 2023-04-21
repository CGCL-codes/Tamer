package apollo.gui;

import java.util.*;
import apollo.gui.schemes.*;
import apollo.gui.event.*;
import apollo.gui.Controller;

/**
 * Registers as an observer for a FeatureTierss object 
 * and relays changes in the PropertyScheme 
 * to TypesChangedListeners registered with a particular controller.
 */
public class TypesObserver implements Observer, ControlledObjectI {

    Controller controller;

    PropertyScheme tiers;

    public TypesObserver(PropertyScheme tiers, Controller c) {
        tiers.addObserver(this);
        this.tiers = tiers;
        setController(c);
        update(tiers, null);
    }

    public void update(Observable obs, Object arg) {
        fireTiersChangedEvent((PropertyScheme) obs);
    }

    public void setController(Controller c) {
        controller = c;
    }

    public Controller getController() {
        return controller;
    }

    public Object getControllerWindow() {
        return null;
    }

    public boolean needsAutoRemoval() {
        return false;
    }

    public void fireTiersChangedEvent(PropertyScheme changed) {
        TypesChangedEvent evt = new TypesChangedEvent(this, changed);
        controller.handleTiersChangedEvent(evt);
    }

    public void Finalize() {
        tiers.deleteObserver(this);
    }
}
