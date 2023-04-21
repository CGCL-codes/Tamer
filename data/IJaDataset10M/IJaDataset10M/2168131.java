package sessions;

import java.util.Vector;
import org.gjt.sp.jedit.EBComponent;
import org.gjt.sp.jedit.EBMessage;

/**
 * This message is sent out on EditBus <i>after</i> the individual
 * <code>SessionPropertyChanged</code> and
 * <code>SessionPropertyRemoved</code> messages
 * have been sent.
 * This message is sent as the last step after the session properties
 * dialog has been closed with "Ok", or applied with "Apply".
 */
public final class SessionPropertiesChanged extends EBMessage {

    SessionPropertiesChanged(EBComponent source, Session session) {
        super(source);
        this.session = session;
    }

    public final SessionManager getSessionManager() {
        return (SessionManager) getSource();
    }

    /**
	 * Return the session bound to this message.
	 */
    public final Session getSession() {
        return session;
    }

    public String paramString() {
        return super.paramString() + ",session=" + session;
    }

    private Session session;
}
