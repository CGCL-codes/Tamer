package dioscuri.interfaces;

/**
 *
 */
public interface Updateable extends Module {

    /**
     * Get the update interval.
     *
     * @return the update interval
     */
    int getUpdateInterval();

    /**
     * Defines the interval between subsequent updates
     *
     * @param interval the interval between subsequent updates in ms.
     */
    void setUpdateInterval(int interval);

    /**
     * Update the module.
     */
    void update();
}
