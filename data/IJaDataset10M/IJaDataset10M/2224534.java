package jtmsmon.util;

/**
 *
 * @author th
 */
public class Timing {

    /** Creates a new instance of Timing */
    public Timing() {
        startTime = timestamp = System.currentTimeMillis();
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public double getDiffToTimestamp() {
        return ((double) System.currentTimeMillis()) - timestamp;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public double getStartTime() {
        return startTime;
    }

    /**
   * Method description
   *
   *
   * @return
   */
    public double getTimestamp() {
        return timestamp;
    }

    /**
   * Method description
   *
   */
    public void setTimestamp() {
        timestamp = System.currentTimeMillis();
    }

    /**
   * Method description
   *
   *
   * @param prefix
   */
    public void showDiffToStartTime(String prefix) {
        double t = System.currentTimeMillis();
        prefix += " " + ((t - startTime) / 1000.0 + "s");
        System.err.println("+++ " + prefix);
    }

    /**
   * Method description
   *
   *
   * @param prefix
   * @param refreshTimestamp
   */
    public void showDiffToTimestamp(String prefix, boolean refreshTimestamp) {
        double t = System.currentTimeMillis();
        prefix += " " + ((t - timestamp) / 1000.0 + "s");
        if (refreshTimestamp) {
            timestamp = t;
        }
        System.err.println("+++ " + prefix);
    }

    /** Field description */
    private double startTime;

    /** Field description */
    private double timestamp;
}
