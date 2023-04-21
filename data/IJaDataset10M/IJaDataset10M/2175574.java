package org.iqual.chaplin.tutor;

/**
 * @author Zbynek Slajchrt
 * @since May 24, 2009 2:04:55 PM
 */
public class SpeakerAlarmProgram {

    public static void main(String[] args) throws Exception {
        long pause = Long.parseLong(args[0]);
        int repeatCount = Integer.parseInt(args[1]);
        int beepIntensity = Integer.parseInt(args[2]);
        SpeakerAlarmContext alarmCtx = new SpeakerAlarmContext(pause, repeatCount, beepIntensity);
        alarmCtx.execute();
    }
}
