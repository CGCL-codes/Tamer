package it.unipi.miabot.data;

/**
 * The sonar and light data type interface.
 * 
 * @author Luca Benedetti
 */
public interface SonarAndLightScan {

    /**
   * Returns the normalized brightness seen by the back light sensor.
   * 
   * @return the normalized brightness seen by the back light sensor.
   */
    public double getBackBrightness();

    /**
   * Returns the nearest obstacle distance seen by back sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by back sonar, in cm.
   */
    public int getBackDistance();

    /**
   * Returns the normalized brightness seen by the back-left light sensor.
   * 
   * @return the normalized brightness seen by the back-left light sensor.
   */
    public double getBackLeftBrightness();

    /**
   * Returns the nearest obstacle distance seen by back-left sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by back-left sonar, in cm.
   */
    public int getBackLeftDistance();

    /**
   * Returns the normalized brightness seen by the back-right light sensor.
   * 
   * @return the normalized brightness seen by the back-right light sensor.
   */
    public double getBackRightBrightness();

    /**
   * Returns the nearest obstacle distance seen by back-right sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by back-right sonar, in cm.
   */
    public int getBackRightDistance();

    /**
   * Returns the normalized brightness seen by the front light sensor.
   * 
   * @return the normalized brightness seen by the front light sensor.
   */
    public double getFrontBrightness();

    /**
   * Returns the nearest obstacle distance seen by front sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by front sonar, in cm.
   */
    public int getFrontDistance();

    /**
   * Returns the normalized brightness seen by the front-left light sensor.
   * 
   * @return the normalized brightness seen by the front-left light sensor.
   */
    public double getFrontLeftBrightness();

    /**
   * Returns the nearest obstacle distance seen by front-left sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by front-left sonar, in cm.
   */
    public int getFrontLeftDistance();

    /**
   * Returns the normalized brightness seen by the front-right light sensor.
   * 
   * @return the normalized brightness seen by the front-right light sensor.
   */
    public double getFrontRightBrightness();

    /**
   * Returns the nearest obstacle distance seen by front-right sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by front-right sonar, in cm.
   */
    public int getFrontRightDistance();

    /**
   * Returns the normalized brightness seen by the left light sensor.
   * 
   * @return the normalized brightness seen by the left light sensor.
   */
    public double getLeftBrightness();

    /**
   * Returns the nearest obstacle distance seen by left sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by left sonar, in cm.
   */
    public int getLeftDistance();

    /**
   * Returns the normalized brightness seen by the right light sensor.
   * 
   * @return the normalized brightness seen by the right light sensor.
   */
    public double getRightBrightness();

    /**
   * Returns the nearest obstacle distance seen by right sonar, in cm.
   * 
   * @return the nearest obstacle distance seen by right sonar, in cm.
   */
    public int getRightDistance();

    /**
   * Returns the timestamp of this object's creation.
   * <p>
   * The timestamp is the one obtained by
   * <code>System.currentTimeMillis()</code>
   * 
   * @return the timestamp of this object's creation.
   */
    public long getTimestamp();
}
