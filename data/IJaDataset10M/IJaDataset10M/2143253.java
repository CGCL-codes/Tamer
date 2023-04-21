package org.iqual.chaplin.tutor;

import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * @author Zbynek Slajchrt
 * @since May 24, 2009 3:25:52 PM
 */
public class SensorProgram {

    public static void main(String[] args) {
        long pause = Long.parseLong(args[0]);
        int repeatCount = Integer.parseInt(args[1]);
        int beepIntensity = Integer.parseInt(args[2]);
        String location = args[3];
        SpeakerAlarmContext alarmCtx = new SpeakerAlarmContext(pause, repeatCount, beepIntensity);
        SensorContext sensorCtx = new SensorContext(location);
        SignalEmitter emitter = $(sensorCtx, alarmCtx);
        emitter.listen();
    }
}
