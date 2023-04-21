package net.kano.partypad.objects;

import net.kano.partypad.datatypes.Proportion;
import net.kano.partypad.datatypes.SoundBuffer;
import net.kano.partypad.notes.AbsoluteNote;
import net.kano.partypad.notes.Note;
import net.kano.partypad.notes.NoteTools;
import net.kano.partypad.pipeline.AbstractPipelineObject;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.PipelineTools;
import net.kano.partypad.pipeline.ports.AbstractOutputPort;
import net.kano.partypad.pipeline.ports.InputPort;
import net.kano.partypad.pipeline.ports.OutputParameters;
import net.kano.partypad.pipeline.ports.OutputPort;
import net.kano.partypad.pipeline.ports.PortInfo;
import net.kano.partypad.pipeline.ports.SimpleBoundedInputPort;
import java.nio.ShortBuffer;
import static net.kano.partypad.pipeline.PipelineTools.TYPEINFO_SOUNDBUFFER;
import static net.kano.partypad.pipeline.PipelineTools.TYPEINFO_PROPORTION;

/**
 * A class providing basic functionality of a single tone generator.
 */
public abstract class ToneGenerator extends AbstractPipelineObject {

    /** The volume (amplitude) input port. */
    private InputPort<Proportion> volume = new SimpleBoundedInputPort<Proportion>(TYPEINFO_PROPORTION, new PortInfo("amplitude", "Volume", this), Proportion.MAXIMUM);

    /** The wave frequency input port. */
    private InputPort<Double> frequency = new SimpleBoundedInputPort<Double>(PipelineTools.TYPEINFO_DOUBLE, new PortInfo("frequency", "Pitch", this), 0.0, 20000.0, new AbsoluteNote(Note.C, 4).getFrequency());

    /** A sound buffer. */
    private SoundBuffer soundBuffer = new SoundBuffer(512);

    /** The sound data output port. */
    private ToneOutputPort soundOutput = new ToneOutputPort();

    {
        getInputHolder().add(volume, frequency);
        getOutputHolder().add(soundOutput);
    }

    /**
     * Creates a new tone generator with no object information set. Object
     * information must be set with {@link #setObjectInfo(PipelineObjectInfo)}
     */
    protected ToneGenerator() {
    }

    /**
     * Creates a new tone generator with the given object information.
     *
     * @param objInfo object information for this pipeline object
     */
    protected ToneGenerator(PipelineObjectInfo objInfo) {
        super(objInfo);
    }

    /**
     * Returns the y-value for the wave with the given volume and phase.
     *
     * @param vol the amplitude of the wave
     * @param phase the phase in the cycle, a number from 0 to 1
     * @return the y-value for the wave at the specified point
     */
    protected abstract double computeValue(int vol, double phase);

    protected double incrPhase(double val) {
        return soundOutput.incrPhase(val);
    }

    private class ToneOutputPort extends AbstractOutputPort<SoundBuffer> {

        private double sampleRate = soundBuffer.getFormat().getSampleRate();

        private ShortBuffer buffer = soundBuffer.getBuffer();

        private double lastfreq = 0;

        private double freq = 0;

        private double phase = 0;

        private AbsoluteNote lastClosest = null;

        public ToneOutputPort() {
            super(TYPEINFO_SOUNDBUFFER, new PortInfo("sound-data", "Sound", "", ToneGenerator.this));
        }

        public SoundBuffer getValue(OutputParameters params) {
            AbsoluteNote closest = NoteTools.findClosestNote(freq);
            if (!closest.equals(lastClosest) || Math.abs(lastfreq - freq) > 100) {
                lastClosest = closest;
            }
            OutputPort<Double> pitchBinding = frequency.getCurrentBinding();
            OutputPort<Proportion> volumeBinding = volume.getCurrentBinding();
            freq = 0;
            if (pitchBinding != null) {
                Double value = pitchBinding.getValue(null);
                if (value != null) freq = value;
            }
            int vol = Short.MAX_VALUE;
            if (volumeBinding != null) {
                Proportion volBindingValue = volumeBinding.getValue(null);
                if (volBindingValue != null) {
                    vol = volBindingValue.coerceValueToInteger(0, Short.MAX_VALUE);
                }
            }
            assert vol <= Short.MAX_VALUE && vol >= 0 : vol;
            buffer.rewind();
            double samplesPerPeriod;
            while (buffer.hasRemaining()) {
                double fmod = (freq - lastfreq) * buffer.position() / buffer.limit();
                samplesPerPeriod = sampleRate / (freq + fmod);
                double val = computeValue(vol, phase);
                buffer.put((short) val);
                buffer.put((short) val);
                double singleSamplePeriodUnit = 1.0 / samplesPerPeriod;
                incrPhase(singleSamplePeriodUnit);
            }
            lastfreq = freq;
            return soundBuffer;
        }

        public double incrPhase(double amount) {
            phase += amount;
            while (phase >= 1) phase -= 1;
            while (phase < 0) phase += 1;
            return phase;
        }
    }
}
