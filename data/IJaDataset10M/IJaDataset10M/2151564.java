package org.dinopolis.gpstool.alarm;

/**
 * A LeavePositionAlarmCondition is informed about any changes by the use
 * of a property change event. If the condition is true (a
 * specific position resp. area is left), it calls the trigger
 * and passes the action that was previously set.
 *
 * @author Christof Dallermassl
 * @version $Revision: 5 $
 */
public class LeavePositionAlarmCondition extends PositionAlarmCondition {

    /**
 * Constructs an alarm condition that triggers an action if the gps
 * position is near the position iven here.
 *
 * @param latitude
 * @param longitude
 * @param distance in meters
 */
    public LeavePositionAlarmCondition(float latitude, float longitude, float distance) {
        super(latitude, longitude, distance);
    }

    /**
 * Returns true, if the given distance is less than the alarm distance.
 * This method may be overriden by subclasses that trigger on a distance.
 *
 * @param distance the distance to the current gps position.
 */
    public boolean isAlarmDistance(double distance) {
        return (distance >= distance_);
    }
}
