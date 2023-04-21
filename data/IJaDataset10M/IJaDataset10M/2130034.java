package de.crysandt.audio.mpeg7audio.msgs;

/**
 * @author <a href="mailto:crysandt@ient.rwth-aachen.de">Holger Crysandt</a>
 */
public class MsgAudioPower extends Msg {

    public final float power;

    public final boolean db_scale;

    public MsgAudioPower(int time, int duration, float power, boolean db_scale) {
        super(time, duration);
        this.power = power;
        this.db_scale = db_scale;
    }

    public String toString() {
        return super.toString() + "; power: " + power;
    }
}
