package au.vermilion.desktop.machines;

import au.vermilion.utils.ExposedArrayList;
import au.vermilion.utils.VermilionUtils;
import au.vermilion.desktop.MachineBase;
import au.vermilion.desktop.ParameterBase;
import au.vermilion.desktop.PlaybackEvent;
import au.vermilion.desktop.parameters.ParameterSlider;

/**
 * A sinusoidal distortion effect which maps values from the 'A' input to
 * the output buffer.
 */
public class MachineBender extends MachineBase {

    private ParameterSlider amount;

    /**
     * The type for this machine is 'EFFECT'.
     */
    @Override
    public String getType() {
        return "EFFECT";
    }

    /**
     * The name for this machine is 'Bender'.
     */
    @Override
    public String getName() {
        return "Bender";
    }

    /**
     * The mixing algorithm for this plugin simply maps each input value
     * and writes it to the output.
     */
    @Override
    public void fillBuffer(int numSamples) {
        float modAmt = (float) (amount.value + 1) / 1000.0f;
        float ampAmt = 1.0f;
        if (modAmt < Math.PI / 2) ampAmt = (float) (1.0 / Math.sin(modAmt));
        float[] lOut = output[0];
        float[] rOut = output[1];
        float[] lIn = aInput[0];
        float[] rIn = aInput[1];
        for (int x = 0; x < numSamples; x++) {
            lOut[x] = (float) (Math.sin(lIn[x] * modAmt) * ampAmt);
            rOut[x] = (float) (Math.sin(rIn[x] * modAmt) * ampAmt);
        }
    }

    /**
     * This machine does nothing on direct parameter changes.
     */
    @Override
    public void parameterChange(PlaybackEvent param) {
    }

    /**
     * The setup for this machine does nothing.
     */
    @Override
    public void initialise() {
    }

    @Override
    protected void createParameters(ExposedArrayList<ParameterBase> params) {
        amount = addSliderParameter(params, "Amount", -1, 1000);
    }

    /**
     * This machine has one parameter which it prints as a percentage.
     */
    @Override
    public String printParameter(ParameterBase p) {
        if (p == amount) return VermilionUtils.valueSigFigures(amount.value / 100.0f, 2);
        return "";
    }
}
