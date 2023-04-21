package net.gpslite.visual;

import net.gpslite.business.types.Location;
import net.gpslite.business.types.SimpleLocation;

/**
 * The class describes visual device being the representation of real watched device. 
 * The real watched devices are represented by the visual objects on the web client. Behavior and state of that visual objects
 * are described here. 
 * @author Jan Sobocinski
 *
 */
public class VisualDevice implements LocationListener, RequestListener, AlarmListener {

    /**
	 * Unique device identifier.
	 */
    private String deviceId;

    /**
	 * Contains most actual location of a device.
	 */
    private Location location;

    /**
	 * Contains basic information about most actual location of a device.
	 */
    private SimpleLocation simpleLocation;

    private String color;

    /**
	 * Contains the icon of visual device as path to icon.
	 */
    private String icon;

    private String deviceConfiguration;

    /**
	 * Defines either SimpleLocation or Location is used for determining geographical device position.
	 */
    private boolean isSimpleLocationUsed;

    /**
	 * Returns Location History
	 * @return TODO:
	 */
    public Object getLocationHistory() {
        return null;
    }

    /**
	 * Returns Request History
	 * @return TODO:
	 */
    public Object getRequestHistory() {
        return null;
    }

    /**
	 * Returns Alarm History
	 * @return TODO:
	 */
    public Object getAlarmHistory() {
        return null;
    }

    @Override
    public void alarmUpdated() {
    }

    @Override
    public void requestUpdated() {
    }

    @Override
    public void locationUpdated() {
    }
}
