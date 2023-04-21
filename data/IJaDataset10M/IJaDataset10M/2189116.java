package net.sourceforge.obschet.gsm;

import net.sourceforge.obschet.event.IdEvent;
import net.sourceforge.obschet.event.TimeEvent;
import java.util.TimeZone;

/**
 *
 * @author alex
 */
public interface GsmEvent extends TimeEvent, IdEvent {

    public NetworkCode getNetworkCode();

    public TimeZone getTimeZone();

    public TimeZone getCommutatorTimeZone();

    public String getNetworkInfo();
}
