package g4mfs.impl.org.peertrust.event;

/**
 * <p>
 * Listens for the events generated in the systems. Any specific class 
 * must implement this class in order to receive event notifications.
 * </p><p>
 * $Id: PTEventListener.java,v 1.1 2005/11/30 10:35:13 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:13 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public interface PTEventListener {

    public void event(PTEvent event);
}
