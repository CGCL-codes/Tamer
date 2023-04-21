package org.openremote.controller.component.onlysensorycomponent;

import java.util.ArrayList;
import java.util.List;
import org.openremote.controller.command.NoStatusCommand;
import org.openremote.controller.component.Component;
import org.openremote.controller.component.Sensor;
import org.openremote.controller.component.Sensory;

/**
 * 
 * @author Handy
 *
 */
public class Image extends Component implements Sensory {

    public Image() {
        super();
        setSensor(new Sensor(new NoStatusCommand()));
    }

    @Override
    protected List<String> getAvailableActions() {
        List<String> availableActions = new ArrayList<String>();
        availableActions.add("status");
        return availableActions;
    }

    @Override
    public int fetchSensorID() {
        return getSensor().getSensorID();
    }
}
