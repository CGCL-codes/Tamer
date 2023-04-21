package skycastle.huntergatherers.model.location;

/**
 * @author Hans H�ggstr�m
 */
public interface LocationChangeListener {

    /**
     * Called when the specified location has changed.
     * 
     * @param location the changed location.
     */
    void onPositionChange(Location location);
}
