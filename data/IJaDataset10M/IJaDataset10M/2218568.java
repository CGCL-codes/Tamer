package org.openremote.controller.component;

import java.util.ArrayList;
import java.util.List;
import org.openremote.controller.command.NoStatusCommand;
import org.openremote.controller.command.StatusCommand;

/**
 * Super class of all components
 * 
 * @author Handy.Wang 2009-12-31
 */
public abstract class Component {

    private Sensor sensor;

    /** The Constant REF_ATTRIBUTE_NAME. */
    public static final String REF_ATTRIBUTE_NAME = "ref";

    public static final String INCLUDE_ELEMENT_NAME = "include";

    public static final String INCLUDE_TYPE_ATTRIBUTE_NAME = "type";

    public static final String COMMAND_ELEMENT_NAME = "command";

    public static final String STATUS_ELEMENT_NAME = "status";

    public static final String INCLUDE_TYPE_SENSOR = "sensor";

    protected List<String> availableActions;

    /**
    * Instantiates a new Component.
    */
    public Component() {
        super();
        setSensor(new Sensor(new NoStatusCommand()));
        availableActions = new ArrayList<String>();
        availableActions.addAll(getAvailableActions());
    }

    /** All available actions of sub controls */
    protected abstract List<String> getAvailableActions();

    public boolean isValidActionWith(String actionParam) {
        for (String action : availableActions) {
            if (action.equalsIgnoreCase(actionParam)) {
                return true;
            }
        }
        return false;
    }

    /**
    * Gets the status command.
    * 
    * @return the status command
    */
    public StatusCommand getStatusCommand() {
        return sensor.getStatusCommand();
    }

    protected Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
