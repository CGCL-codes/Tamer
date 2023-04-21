package net.sf.jawp.api.domain;

import java.io.Serializable;

/**
 * Operations associated with game speed.
 * 
 * Allows retrieving of game constants using time factor.
 * There are two independent variables that area associated with game speed:
 *	- game speed factor - how fast/slow is  the game as general
 *  - server time granurality - it in fact only says how often steps are performed - but do not affect
 *								overall speed
 * 
 * @author jarek
 */
public class GameSpeed implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long BASE_TIME_SLICE = 1000;

    /**
	 * game speed factor - 1.0f normal
	 */
    private final float speedFactor;

    private SpeedSetting speedSetting;

    /** Creates a new instance of GameSpeed */
    public GameSpeed() {
        this.speedSetting = SpeedSetting.NORMAL;
        this.speedFactor = speedSetting.getValue();
    }

    public GameSpeed(final SpeedSetting speed) {
        this.speedSetting = speed;
        this.speedFactor = speedSetting.getValue();
    }

    public final SpeedSetting getSpeedSetting() {
        return this.speedSetting;
    }

    /**
	 * 
	 * 
	 * @param timeSlice how often server step is performed ( interval in ms)
	 * @param speedFactor game speed factor (1.0f normal)
	 */
    public final float getProductionPerStep(final long timeSlice, final float production) {
        final float factor = speedFactor * getTimeSliceFactor(timeSlice);
        return production * factor;
    }

    /**
	 * returns base fleet speed for given game speed
	 */
    public final float getBaseFleetSpeed() {
        return this.speedFactor * Rules.BASE_FLEET_SPEED;
    }

    public final SpaceCoords adjustMovement(final long timeSlice, final SpaceCoords base) {
        final float factor = getTimeSliceFactor(timeSlice);
        return new SpaceCoords(base.getX() * factor, base.getY() * factor, base.getZ() * factor);
    }

    public final float getAdjustedStep(final long timeSlice) {
        return getTimeSliceFactor(timeSlice);
    }

    private float getTimeSliceFactor(final long timeSlice) {
        return (float) timeSlice / (float) BASE_TIME_SLICE;
    }

    public final float getRevealingFactor(final long timeSlice) {
        return Rules.REVEALING_FACTOR * this.speedFactor * getTimeSliceFactor(timeSlice);
    }
}
