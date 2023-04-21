package sessions;

import org.gjt.sp.jedit.EBComponent;

public final class SessionChanging extends SessionMessage {

    SessionChanging(EBComponent source, String oldSession, String newSession) {
        super(source, oldSession, newSession);
    }
}
