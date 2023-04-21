package deduced.analyzer;

import deduced.*;

/**
 * Analyzer
 * 
 * @author Duff
 */
public abstract class Analyzer {

    /** _destinationPointer */
    private PropertyPointerAnalyzer _destinationPointer;

    /** _ignoringUpdates */
    private boolean _ignoringUpdates = false;

    /**
     * constructor for Analyzer
     * 
     * @param destinationPointer
     */
    public Analyzer(PropertyPointerAnalyzer destinationPointer) {
        _destinationPointer = destinationPointer;
        _destinationPointer.addListener(new DestinationListener());
    }

    /**
     * updateAnalyzedValue
     */
    protected final void updateAnalyzedValue() {
        updateAnalyzedValue(getAnalyzedValue());
    }

    /**
     * updateAnalyzedValue
     * 
     * @param newValue
     */
    protected final void updateAnalyzedValue(Object newValue) {
        _ignoringUpdates = true;
        _destinationPointer.setPointedProperty(newValue);
        _ignoringUpdates = false;
    }

    /**
     * getAnalyzedValue
     * 
     * @return
     */
    public abstract Object getAnalyzedValue();

    /**
     * DestinationListener
     * 
     * @author Duff
     */
    private class DestinationListener implements PropertyListener {

        /**
         * (non-Javadoc)
         * 
         * @see deduced.PropertyListener#propertyChanged(deduced.PropertyChangeEvent)
         */
        public void propertyChanged(PropertyChangeEvent event) {
            if (isIgnoringUpdates()) {
                return;
            }
            updateAnalyzedValue();
        }
    }

    /**
     * @return Returns the ignoreUpdates.
     */
    protected final boolean isIgnoringUpdates() {
        return _ignoringUpdates;
    }

    /**
     * @param ignoreUpdates The ignoreUpdates to set.
     */
    protected final void setIgnoringUpdates(boolean ignoreUpdates) {
        _ignoringUpdates = ignoreUpdates;
    }
}
