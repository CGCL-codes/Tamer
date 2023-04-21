package org.iqual.chaplin.tutor;

import org.iqual.chaplin.Binder;
import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * @author Zbynek Slajchrt
 * @since May 24, 2009 2:04:13 PM
 */
@Binder
public class AlarmContext {

    final Lamp lamp = LampFactory.getLamp();

    final Alarm alarm = $();

    long pause;

    int repeatCount;

    public AlarmContext(long pause, int repeatCount) {
        this.pause = pause;
        this.repeatCount = repeatCount;
    }

    public void execute() throws Exception {
        alarm.runAlarm();
    }
}
