package de.crysandt.audio.mpeg7audio.msgs;

/**
 * @author <a href="mailto:micky78@email.it">Michele Bartolucci</a>
 */
public class MsgTemporalCentroid extends Msg {

    public float temporalCentroid;

    public MsgTemporalCentroid(int time, int duration, float temporalCentroid) {
        super(time, duration);
        this.temporalCentroid = temporalCentroid;
    }
}
