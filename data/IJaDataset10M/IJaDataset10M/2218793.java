package net.sf.peervibes.test.events;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.PeriodicTimer;

/**
 * The <i>peer-to-peer communication</i> Send Timer.
 * <br>
 * Used by a the broadcast and route applications test sessions to control the period between random
 * decision to send a message.
 *
 * @version 0.1
 * @author Joao Leitao
 */
public class SendTimer extends PeriodicTimer {

    public SendTimer() {
        super();
    }

    public SendTimer(String timerID, long period, Channel channel, int dir, Session source, int qualifier) throws AppiaEventException, AppiaException {
        super(timerID, period, channel, dir, source, qualifier);
    }
}
