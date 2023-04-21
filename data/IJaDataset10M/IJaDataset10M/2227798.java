package combasics.listener;

import combasics.event.PropertyFileChangeEvent;

/**
 * 
 * @author Yark Schroeder, Manuel Scholz
 *
 */
public interface PropertyFileChangeListener {

    public void processPropertyChange(PropertyFileChangeEvent kEvent);
}
