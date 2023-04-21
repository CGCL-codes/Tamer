package alp;

/**
 * @author niki.waibel{@}gmx.net
 */
public final class SessionStateChangedEvent extends SessionEvent {

    private Session.state state;

    SessionStateChangedEvent(Session.state state) {
        super(SessionEvent.Type.stateChanged);
        this.state = state;
    }

    public Session.state getState() {
        return state;
    }
}
