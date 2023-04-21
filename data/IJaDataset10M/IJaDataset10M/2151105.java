package javax.faces.event;

import java.util.EventObject;

/**
 * @author Simon Lessard (latest modification by $Author: slessard $)
 * @version $Revision: 696523 $ $Date: 2008-09-24 18:31:37 -0400 (mer., 17 sept. 2008) $
 * 
 * @since 2.0
 */
public abstract class SystemEvent extends EventObject {

    public SystemEvent(Object source) {
        super(source);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof SystemEventListener;
    }

    public void processListener(FacesListener listener) {
        ((SystemEventListener) listener).processEvent(this);
    }
}
