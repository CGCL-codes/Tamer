package it.univpm.deit.audio;

import de.crysandt.audio.mpeg7audio.msgs.*;

/**
 * @author Guido Raparo
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DcOffset extends MsgSpeaker implements MsgListener {

    private int channel = 1;

    private int signal_length = 0;

    private float signal_max = -5;

    private float mean = -5;

    private float sum = 0;

    private float dco = -3;

    public DcOffset() {
        super();
    }

    public void receivedMsg(Msg m) {
        if (m instanceof MsgResizer) receivedMsg((MsgResizer) m);
        if (m instanceof MsgEndOfSignal) receivedMsg((MsgEndOfSignal) m);
    }

    private void receivedMsg(MsgResizer m) {
        float[] s = m.getSignal();
        signal_length += s.length;
        for (int i = 0; i < s.length; i++) {
            sum += s[i];
            s[i] = Math.abs(s[i]);
            if (s[i] > signal_max) signal_max = s[i];
        }
    }

    public void receivedMsg(MsgEndOfSignal meos) {
        int time, duration;
        time = meos.time;
        duration = meos.duration;
        mean = sum / signal_length;
        dco = mean / signal_max;
        if (dco < -1) dco = -1;
        if (dco > 1) dco = 1;
        send(new MsgDcOffset(time, duration, channel, dco));
        send(meos);
    }
}
