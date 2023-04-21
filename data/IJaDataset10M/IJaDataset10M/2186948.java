package net.mlw.fball.gui.events;

import net.mlw.fball.event.Event;
import net.mlw.fball.gui.AppContext;

/**
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.1 $ $Date: 2004/03/01 15:31:43 $
 */
public class ErrorEvent implements Event {

    private String code;

    public ErrorEvent(String code) {
        this.code = code;
    }

    /** @see java.lang.Object#toString()
    */
    public String toString() {
        return AppContext.getMessage(code);
    }
}
