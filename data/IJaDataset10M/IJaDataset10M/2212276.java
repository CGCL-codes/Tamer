package mikera.tyrant;

import mikera.engine.Thing;

/**
 * @author Mike
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface EventHandler {

    public boolean handle(Thing t, Event e);
}
