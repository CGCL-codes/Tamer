package org.jdiameter.common.api.app.rx;

import java.util.concurrent.ScheduledFuture;
import org.jdiameter.api.rx.ServerRxSession;

/**
 * Diameter 3GPP IMS Rx Reference Point Server Additional listener
 * Actions for FSM
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface IServerRxSessionContext {

    public void sessionSupervisionTimerExpired(ServerRxSession session);

    /**
   * This is called always when Tcc starts
   * @param session
   * @param future
   */
    public void sessionSupervisionTimerStarted(ServerRxSession session, ScheduledFuture future);

    public void sessionSupervisionTimerReStarted(ServerRxSession session, ScheduledFuture future);

    public void sessionSupervisionTimerStopped(ServerRxSession session, ScheduledFuture future);
}
