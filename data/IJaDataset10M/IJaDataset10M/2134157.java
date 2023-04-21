package dsp.basicinput.inputs;

import dsp.DataChunk;
import dsp.exception.RecoverableInputProblem;
import dsp.basicinput.SinusoidSignalData;

/**
 * @author Flaviu
 *
 */
public class SinusoidSignal extends AbstractSignal {

    protected final double factor[];

    protected final double mean;

    protected SinusoidSignalData sinusoidData[];

    protected final int harmonics;

    public SinusoidSignal(double mean, SinusoidSignalData sinusoidData[], int harmonics, double samplingFreq, int samples) {
        super(samplingFreq, samples);
        this.mean = mean;
        this.sinusoidData = sinusoidData;
        this.harmonics = harmonics;
        factor = new double[harmonics];
        for (int i = 0; i < harmonics; ++i) factor[i] = 2 * Math.PI * sinusoidData[i].freqData / samplingFreq;
    }

    public DataChunk getNext() throws RecoverableInputProblem {
        double value = mean;
        for (int i = 0; i < harmonics; ++i) value += sinusoidData[i].amplitData * Math.sin(factor[i] * counter + sinusoidData[i].phaseData);
        data.setTo(value);
        ++counter;
        return data;
    }
}
