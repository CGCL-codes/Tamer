package net.kano.partypad.objects;

import net.kano.partypad.datatypes.Proportion;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.PipelineTools;
import net.kano.partypad.pipeline.ports.InputPort;
import net.kano.partypad.pipeline.ports.OutputPort;
import net.kano.partypad.pipeline.ports.PortInfo;
import net.kano.partypad.pipeline.ports.SimpleBoundedInputPort;

/**
 * A tone generator which produces a "square" wave.
 */
public class SquareWaveGenerator extends ToneGenerator {

    /** Object information for a square wave generator object. */
    public static final PipelineObjectInfo OBJECT_INFO = new PipelineObjectInfo("square-generator", "Square wave generator", "Square", "Generates a constant square wave tone");

    private InputPort<Proportion> thresholdInput = new SimpleBoundedInputPort<Proportion>(PipelineTools.TYPEINFO_PROPORTION, new PortInfo("pulse-width", "Pulse width", "Pulse", null, this), Proportion.MAXIMUM);

    double lastPulseWidth = 0.5;

    {
        getInputHolder().add(thresholdInput);
    }

    /**
     * Creates a new square wave generator object.
     */
    public SquareWaveGenerator() {
        super(OBJECT_INFO);
    }

    protected double computeValue(int vol, double phase) {
        double pulseWidth = getPulseWidth();
        if (pulseWidth != lastPulseWidth) {
            phase = incrPhase(pulseWidth - lastPulseWidth);
            lastPulseWidth = pulseWidth;
        }
        if (phase > pulseWidth) return vol; else return -vol;
    }

    private double getPulseWidth() {
        OutputPort<Proportion> binding = thresholdInput.getCurrentBinding();
        if (binding == null) return 0.5; else return binding.getValue(null).getFloatValue() / 2;
    }
}
