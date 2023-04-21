package org.ximtec.igesture.core.composite.parameter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ximtec.igesture.core.Gesture;
import org.ximtec.igesture.core.composite.ConstraintTool;
import org.ximtec.igesture.core.composite.DefaultConstraintEntry;
import org.ximtec.igesture.io.IDeviceManager;

/**
 * @author Björn Puype, bpuype@gmail.com
 *
 */
public class DefaultConstraintParameters extends AbstractConstraintParameters {

    protected Calendar gestureTime;

    protected Calendar processingTime;

    public enum Config {

        GESTURE_TIME
    }

    private static final String GESTURE_TIME = "00:00:10.000";

    private static final String PROCESSING_TIME = "00:00:02.000";

    public DefaultConstraintParameters() {
        DEFAULT_CONFIGURATION.put(Config.GESTURE_TIME.name(), GESTURE_TIME);
        setterMapping.put(Config.GESTURE_TIME.name(), "setGestureTime");
        try {
            Date d = ConstraintTool.getDateFormatter().parse(GESTURE_TIME);
            gestureTime = Calendar.getInstance();
            gestureTime.setTime(d);
            d = ConstraintTool.getDateFormatter().parse(PROCESSING_TIME);
            processingTime = Calendar.getInstance();
            processingTime.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setGestureTime(String time) throws ParseException {
        Date d = ConstraintTool.getDateFormatter().parse(time);
        gestureTime = Calendar.getInstance();
        gestureTime.setTime(d);
    }

    public Calendar getGestureTime() {
        return gestureTime;
    }

    @Override
    public Map<String, Calendar> determineTimeWindows(List<DefaultConstraintEntry> gestures) {
        return null;
    }

    @Override
    public Set<String> generatePatterns(Map<String, String> charMapping, List<DefaultConstraintEntry> gestures) {
        return null;
    }

    @Override
    public boolean validateConditions(List<Gesture<?>> gestures, IDeviceManager manager) {
        return true;
    }
}
