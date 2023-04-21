package jimo.osgi.api;

import java.util.EventObject;

public class IdleEvent extends EventObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3651206346826123136L;

    public IdleEvent(Object source) {
        super(source);
    }
}
