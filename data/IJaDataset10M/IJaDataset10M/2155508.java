package de.crysandt.audio.mpeg7audio;

import de.crysandt.audio.mpeg7audio.msgs.*;
import de.crysandt.math.Function;

/**
 * @author <a href="mailto:crysandt@ient.rwth-aachen.de">Holger Crysandt</a>
 */
class AudioPower extends MsgSpeaker implements MsgListener {

    @SuppressWarnings("unused")
    private static final float QUANTIZATION_NOISE_16BIT = (float) Function.square(2.0 / Math.pow(2.0, 16)) / 12;

    private final boolean log_scale;

    public AudioPower(boolean log_scale) {
        this.log_scale = log_scale;
    }

    public void receivedMsg(Msg m) {
        if (m instanceof MsgResizer) receivedMsg((MsgResizer) m);
    }

    private void receivedMsg(MsgResizer m) {
        float[] signal = m.getSignal();
        float power = 0.0f;
        for (int i = 0; i < signal.length; ++i) power += signal[i] * signal[i];
        power /= signal.length;
        if (log_scale) power = 10.0f / (float) Function.LOG10 * (float) Math.log(power + Float.MIN_VALUE);
        send(new MsgAudioPower(m.time, m.duration, power, log_scale));
    }
}
