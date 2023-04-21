package tico.components.events;

import java.util.EventListener;

/**
 * The listener interface for receiving order change events. The class that is
 * interested in processing an <code>OrderChangeEvent</code> implements this
 * interface, and the object created with that class is registered with a
 * component, using the component's <code>addOrderChangeListener</code> method.
 * When the <code>OrderChangeEvent</code> occurs, that object's
 * <code>orderChanged</code> method is invoked.
 *
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public interface OrderChangeListener extends EventListener {

    /**
	 * Invoked when an <code>OrderChangeEvent</code> occurs.
	 * 
	 * @param e The <code>OrderChangeEvent</code>
	 */
    public void orderChanged(OrderChangeEvent e);
}
