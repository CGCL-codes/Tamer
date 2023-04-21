package prajna.geoviz;

/**
 * Interface for receiving geographic events. Any class which is interested in
 * processing geographic events should implement this interface. The class
 * should invoke the <code>addGeoListener</code> method on any source of
 * geographic events.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public interface GeoEventListener {

    /**
     * Receive a geographic event. This method is invoked when a Geographic
     * event occurs
     * 
     * @param evt the geographic event
     */
    public abstract void receiveGeoEvent(GeoEvent evt);
}
