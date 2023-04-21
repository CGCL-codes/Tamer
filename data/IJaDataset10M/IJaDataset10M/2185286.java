package de.crysandt.audio.mpeg7audio;

import de.crysandt.audio.mpeg7audio.msgs.*;

/**
 * @author <a href="mailto:crysandt@ient.rwth-aachen.de">Holger Crysandt</a>
 */
class AudioWaveform extends MsgSpeaker implements MsgListener {

    public AudioWaveform() {
        super();
    }

    public void receivedMsg(Msg msg) {
        if (msg instanceof MsgResizer) {
            MsgResizer m = (MsgResizer) msg;
            float[] signal = m.getSignal();
            float min = signal[0];
            float max = signal[0];
            for (int i = 1; i < signal.length; ++i) {
                min = Math.min(min, signal[i]);
                max = Math.max(max, signal[i]);
            }
            send(new MsgAudioWaveform(m.time, m.duration, min, max));
        }
    }
}
