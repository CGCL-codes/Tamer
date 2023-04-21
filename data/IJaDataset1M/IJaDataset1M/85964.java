package de.telekom.laboratories.tracking;

/**
 * An interface providing call-back methods about the tracking state of features from different frames.
 * @param Feature the type of the objects to be tracked.
 * @author Michael Nischt
 * @version 0.1
 */
public interface Observer<Feature> {

    /**
     * Called if a new feature is recognized.
     * @param current the newly recognized feature
     */
    void startedTracking(Feature current);

    /**
     * Called if two feature from successive tracking frames belong together. 
     * (See the individual {@link Tracker Tracker} {@link Trackers implemetations}, about their correlation rules.
     * @param last 
     * @param current 
     */
    void updatedTracking(Feature last, Feature current);

    /**
     * Called if a former recognized feature is not present anymore.
     * @param last the feature not present anymore
     */
    void finishedTracking(Feature last);

    /**
     * A implementation, which ignores the notification, 
     * but makes it easier to implement a single method only.
     * @param Feature the type of the objects to be tracked.
     */
    public abstract static class Adapter<Feature> implements Observer<Feature> {

        /**
         * To be used for <code>super>/code>-constructor in classes extending this one.
         */
        protected Adapter() {
        }

        public void startedTracking(Feature current) {
        }

        public void updatedTracking(Feature last, Feature current) {
        }

        public void finishedTracking(Feature last) {
        }
    }
}
