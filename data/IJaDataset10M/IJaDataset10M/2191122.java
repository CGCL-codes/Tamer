package net.java.slee.resource.diameter.rx;

import java.io.IOException;
import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;

/**
 * An RxServerSessionActivity represents a an auth session for Rx servers.
 *
 * A single RxServerSessionActivity will be created for the Diameter session.
 * All requests received for the session will be fired as events on the same
 * RxServerSessionActivity.
 *s
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxServerSessionActivity extends RxSessionActivity {

    AAAnswer createAAAnswer();

    SessionTerminationAnswer createSessionTermAnswer();

    void sendAAAnswer(AAAnswer aaa) throws IOException;

    void sendSessionTermAnswer(SessionTerminationAnswer sta) throws IOException;

    void sendReAuthRequest(ReAuthRequest rar) throws IOException;

    void sendAbortSessionRequest(AbortSessionRequest asr) throws IOException;
}
