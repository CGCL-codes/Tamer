package de.mcb.bumbler.modes;

import de.mcb.bumbler.BumblerFX;
import static de.mcb.bumbler.BumblerFX.*;

/**
 * User: Max
 * Date: 12.10.2010
 * Time: 14:07:51
 */
public class ModeTimedImpl extends AbstractMode {

    private double oldSamplePos = 0;

    private double realSamplePos = 0;

    public ModeTimedImpl(BumblerFX fx) {
        super(fx);
    }

    public float[] processReplacing(float[] inBuffer, int sampleFrames, long samplesOn, long samplesOff, double samplePos) {
        switch(fx.getMidiStatus()) {
            case MIDI_FADE_IN:
                fx.setParameterAutomated(PARAM_BYPASS, BYPASS_ON);
                fx.setMidiStatus(MIDI_STATUS_ON);
                break;
            case MIDI_FADE_OUT:
                fx.setParameterAutomated(PARAM_BYPASS, BYPASS_OFF);
                fx.setMidiStatus(MIDI_STATUS_OFF);
                break;
        }
        float[] outBuffer = new float[inBuffer.length];
        long samplesSum = samplesOn + samplesOff;
        if (samplePos == oldSamplePos) {
            realSamplePos += sampleFrames;
            if (realSamplePos >= samplePos + samplesSum) realSamplePos = samplePos;
        } else {
            realSamplePos = samplePos;
        }
        long rest = (long) (realSamplePos % samplesSum);
        long remain;
        boolean on;
        if (rest < samplesOn) {
            on = true;
            remain = samplesOn - rest;
        } else {
            on = false;
            remain = samplesSum - rest;
        }
        for (int i = 0; i < sampleFrames; i++) {
            if (remain == -1) {
                on = !on;
                if (on) {
                    remain = samplesOn;
                } else {
                    remain = samplesOff;
                }
            }
            float v = inBuffer[i];
            if ((remain < FADE) && (remain >= 0)) {
                if ((samplesOff > 0) && (samplesOn > 0)) {
                    float fader = ((float) remain / FADE);
                    float vOut = fader * v;
                    float vIn = (1 - fader) * v;
                    if (!on) vOut = 0; else vIn = 0;
                    v = (vOut + vIn);
                } else if (samplesOff > 0) {
                    v = 0;
                }
            } else {
                if (!on) v = 0;
            }
            outBuffer[i] = v;
            remain--;
        }
        oldSamplePos = samplePos;
        return outBuffer;
    }
}
