package architecture.ee.event;

import architecture.common.lifecycle.event.Event;

public class PluginStateChangeEvent extends Event {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6725688776619308652L;

    public enum State {

        INSTALLED, UNINSTALLED, UNLOADED, RESTART
    }

    ;

    private State state;

    public PluginStateChangeEvent(Object source, State state) {
        super(source);
    }

    public State getState() {
        return state;
    }
}
