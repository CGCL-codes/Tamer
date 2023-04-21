package be.lassi.tryout.drag;

public class Calculator {

    /**
     * The time for a full 0 to 100 fade at the fastest speed possible
     * (as defined in the sheet user interface in the upper right corner).
     * Low values mean high speed.
     */
    private float timeMaxSpeed;

    /**
     * The time for a full 0 to 100 fade at the slowest speed possible.
     * High values mean low speed.
     */
    private float timeMinSpeed;

    /**
     * Factor defining the fade speed somewhere between minimum and maximum
     * speed.  0f = minimum speed, 1f = maximum speed, 0.5f = min + max / 2
     */
    private float speedFactor;

    public void setSpeedFactor(final float speedFactor) {
        if (speedFactor < 0f || speedFactor > 1f) {
            throw new IllegalArgumentException();
        }
        this.speedFactor = speedFactor;
    }

    public void setTimeMaxSpeed(final float maxSpeedTime) {
        this.timeMaxSpeed = maxSpeedTime;
    }

    public void setTimeMinSpeed(final float minSpeedTime) {
        this.timeMinSpeed = minSpeedTime;
    }

    public float getTimeMaxSpeed() {
        return timeMaxSpeed;
    }

    public float getTimeMinSpeed() {
        return timeMinSpeed;
    }

    public float getValue(final float startValue, final int millisSinceStart) {
        float delta = millisSinceStart / (getSpeedTime() * 1000);
        float value = startValue + delta;
        if (value > 1f) {
            value = 1f;
        }
        return value;
    }

    /**
     * 
     * 
     * @return
     */
    public float getSpeedTime() {
        return (timeMinSpeed - timeMaxSpeed) * speedFactor + timeMaxSpeed;
    }
}
