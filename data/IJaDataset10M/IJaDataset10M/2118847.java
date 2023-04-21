package combasics.eventDispatcher;

import java.util.ArrayList;
import combasics.event.PropertyFileChangeEvent;
import combasics.listener.PropertyFileChangeListener;

/**
 * 
 * @author Yark Schroeder, Manuel Scholz
 *
 */
public interface IPropertyFileChangeEventDispatcher {

    /**
	 * F�gt einen Listener hinzu.
	 * 
	 * @param kListener
	 *            PropertyChangeListener, der Ereignisse empfangen will.
	 * @since 1.3
	 */
    public void addPropertyChangeListener(PropertyFileChangeListener kListener);

    /**
	 * Entfernt den angegebenen Listener.
	 * 
	 * @param kListener
	 *            PropertyChangeListener, der entfernt werden soll.
	 * @since 1.3
	 */
    public void removePropertyChangeListener(PropertyFileChangeListener kListener);

    /**
	 * Event wird an alle weitergeleitet, die als Listener registriert sind.
	 * 
	 * @param kEvent
	 *            Das Ereignis, das erzeugt wurde.
	 * @since 1.3
	 */
    public void dispatchPropertyChangeEvent(PropertyFileChangeEvent kEvent);

    /**
	 * Liste der Listener, die registriert sind, wird zur�ckgegeben.
	 * 
	 * @return Liste der Listener, die registriert sind.
	 * @since 1.3
	 */
    public ArrayList getPropertyChangeListener();
}
