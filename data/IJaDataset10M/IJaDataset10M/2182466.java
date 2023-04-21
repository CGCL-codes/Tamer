package au.vermilion.desktop.machines;

import au.vermilion.utils.ExposedArrayList;
import au.vermilion.utils.VermilionUtils;
import au.vermilion.desktop.MachineBase;
import au.vermilion.desktop.ParameterBase;
import au.vermilion.desktop.PlaybackEvent;
import au.vermilion.desktop.parameters.ParameterSlider;

/**
 * A simple cross delay effect.
 */
public class MachineXDelay extends MachineBase {

    private static final int BUFF_SECONDS = 5;

    /**
     * The type for this machine is 'EFFECT'.
     */
    @Override
    public String getType() {
        return "EFFECT";
    }

    /**
     * The name for this machine is 'Loop Delay'.
     */
    @Override
    public String getName() {
        return "X-Delay";
    }

    private int buffSize;

    private float[][] ringBuffer;

    private ParameterSlider delay;

    private ParameterSlider feedback;

    private int ringPos = 0;

    /**
     * The mixing algorithm for this plugin simply copies the
     * 'A' buffer to the ring buffer and the ring buffer to the output.
     */
    @Override
    public void fillBuffer(int numSamples) {
        int offset = (int) (sampleRate * (delay.value / 10000.0f * BUFF_SECONDS));
        float fback = feedback.value / 8000.0f;
        float[] lOut = output[0];
        float[] rOut = output[1];
        float[] lIn = aInput[0];
        float[] rIn = aInput[1];
        float[] lRB = ringBuffer[0];
        float[] rRB = ringBuffer[1];
        for (int x = 0; x < numSamples; x++) {
            int ringWr = (ringPos + offset) % buffSize;
            int ringRd = (ringPos) % buffSize;
            lRB[ringWr] = rIn[x] + rRB[ringRd] * fback;
            rRB[ringWr] = lIn[x] + lRB[ringRd] * fback;
            lOut[x] = lRB[ringRd];
            rOut[x] = rRB[ringRd];
            ringPos++;
        }
    }

    /**
     * This machine does nothing on direct parameter changes.
     */
    @Override
    public void parameterChange(PlaybackEvent param) {
    }

    /**
     * This machine creates one parameter and a ringbuffer for the delay.
     */
    @Override
    public void initialise() {
        buffSize = sampleRate * BUFF_SECONDS;
        ringBuffer = new float[numChannels][buffSize];
    }

    /**
     * This machine has one parameter which it prints in seconds.
     */
    @Override
    public String printParameter(ParameterBase p) {
        if (p == delay) return VermilionUtils.valueDecimalPlaces(delay.value / 9999.0f * BUFF_SECONDS, 3);
        if (p == feedback) return VermilionUtils.valueAsDb(feedback.value / 8000.0f);
        return "";
    }

    @Override
    protected void createParameters(ExposedArrayList<ParameterBase> params) {
        delay = addSliderParameter(params, "Delay", -1, 1000);
        feedback = addSliderParameter(params, "Feedback", -1, 5000);
    }
}
