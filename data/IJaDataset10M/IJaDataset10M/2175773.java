package codesounding.jsyn.wire;

import java.io.*;
import com.softsynth.jsyn.*;

/**************
** WARNING - this code automatically generated by Wire.
** The real source is probably a Wire patch.
** Do NOT edit this file unless you copy it to another directory and change the name.
** Otherwise it is likely to get clobbered the next time you
** export Java source code from Wire.
**
** Wire is available from: http://www.softsynth.com/wire/
*/
public class Poink extends SynthNote {

    SynthEnvelope misc0;

    EnvelopePlayer envAmp;

    public SynthInput modRate;

    public SynthInput modDepth;

    AddUnit add1;

    Filter_LowPass lowPass1;

    PinkNoise pinkNse;

    SineOscillator sinOsc;

    public Poink() {
        this(Synth.getSharedContext());
    }

    public Poink(SynthContext synthContext) {
        super(synthContext);
        double[] misc0Data = { 0.0020971492121427122, 0.9708333333333333, 0.014405080866456338, 0.7083333333333334, 0.04420240199624746, 0.2833333333333333, 0.03948747911664774, 0.1, 0.15522475125437774, 0.0 };
        misc0 = new SynthEnvelope(synthContext, misc0Data);
        misc0Data = null;
        misc0.setSustainLoop(-1, -1);
        misc0.setReleaseLoop(-1, -1);
        add(envAmp = new EnvelopePlayer(synthContext));
        add(add1 = new AddUnit(synthContext));
        add(lowPass1 = new Filter_LowPass(synthContext));
        add(pinkNse = new PinkNoise(synthContext));
        add(sinOsc = new SineOscillator(synthContext));
        envAmp.rate.set(0, 1.0);
        envAmp.output.connect(pinkNse.amplitude);
        addPort(frequency = add1.inputA, "frequency");
        frequency.setup(0.0, 1318.5140149711385, 4000.0);
        addPort(output = lowPass1.output, "output");
        addPort(modRate = sinOsc.frequency, "modRate");
        modRate.setup(0.0, 42.81022719240156, 30.0);
        addPort(modDepth = sinOsc.amplitude, "modDepth");
        modDepth.setup(0.0, 63.550486567117744, 400.0);
        add1.output.connect(lowPass1.frequency);
        addPort(amplitude = envAmp.amplitude, "amplitude");
        amplitude.setup(0.0, 0.5, 1.0);
        lowPass1.Q.set(0, 17.385387420654297);
        lowPass1.amplitude.set(0, 1.0);
        pinkNse.output.connect(lowPass1.input);
        sinOsc.phase.set(0, 0.06871350109577179);
        sinOsc.output.connect(add1.inputB);
    }

    public void setStage(int time, int stage) {
        switch(stage) {
            case 0:
                envAmp.envelopePort.clear(time);
                envAmp.envelopePort.queueOn(time, misc0);
                start(time);
                break;
            case 1:
                envAmp.envelopePort.queueOff(time, misc0);
                break;
            default:
                break;
        }
    }
}