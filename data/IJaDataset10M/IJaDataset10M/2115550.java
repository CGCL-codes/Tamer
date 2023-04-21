package q.zik.basic.generator;

import q.lang.ParameterListener;
import q.zik.component.ADSR;
import q.zik.component.Dummy;
import q.zik.component.EffectUnit;
import q.zik.component.GeneratorUnit;
import q.zik.core.machine.definition.GeneratorDefinition;
import q.zik.core.machine.instance.Generator;
import q.zik.core.machine.instance.GeneratorTrack;
import q.zik.core.parameter.definition.MachineParameterDefinition;
import q.zik.core.parameter.definition.RangeDefinition.NoteDefinition;
import q.zik.lang.Gain;
import q.zik.lang.Note;
import q.zik.lang.Sample;

/**
 * Global settings definition.
 * 
 * @author ruyantq
 */
public abstract class QADSRSynthDefinition extends GeneratorDefinition {

    /**
     * Track parameters definition.
     * 
     * @author ruyantq
     */
    public abstract static class TrackDefinition<T extends QADSRSynthDefinition> extends GeneratorDefinition.GeneratorTrackDefinition<Generator<T>> {

        protected final MachineParameterDefinition<Gain> volume = addGainParameter("Volume", "Main volume", -40, 0, 40, -3);

        protected final NoteDefinition note = addNoteParameter("Note", "Note");

        @Override
        public GeneratorTrack<?, ?> buildInstance(final Generator<T> key) {
            return new Track<T>(this, key);
        }

        public EffectUnit buildPostEffect(final Track<T> track) {
            return Dummy.INSTANCE;
        }

        public abstract GeneratorUnit buildInnerGenerator(Track<T> track);
    }

    /**
     * Track engine.
     * 
     * @author ruyantq
     * @param <T> the type of oscillator.
     * @param <U> the machine definition.
     */
    public static final class Track<T extends QADSRSynthDefinition> extends GeneratorTrack<TrackDefinition<T>, Generator<T>> implements ParameterListener<Note> {

        private final ADSR adsr;

        ;

        protected final GeneratorUnit innerGenerator;

        protected final EffectUnit postEffect;

        private double currentValue;

        public Track(final TrackDefinition<T> definition, final Generator<T> machine) {
            super(definition, machine);
            innerGenerator = definition.buildInnerGenerator(this);
            postEffect = definition.buildPostEffect(this);
            final QADSRSynthDefinition def = machine.getDefinition();
            adsr = new ADSR(machine.getParameter(def.afactor), machine.getParameter(def.dfactor), machine.getParameter(def.svalue), machine.getParameter(def.sfactor), machine.getParameter(def.rfactor));
            getParameter(definition.note).addListener(this);
        }

        @Override
        public void controllerTick(final int _controllerTime) {
            final Double generate = adsr.generate(_controllerTime);
            if (generate != null) {
                currentValue = generate.doubleValue();
            }
        }

        @Override
        protected final Sample generate(final int time) {
            if (adsr.isOn()) {
                final Double generate = innerGenerator.generate(time);
                if (generate != null) {
                    final long sample = Math.round(generate.doubleValue() * currentValue * Sample.MAX_SAMPLE_VALUE);
                    return postEffect.process(new Sample(sample), time);
                }
            }
            return Sample.SILENCE;
        }

        @Override
        public final void stopWorking() {
            adsr.offMode();
        }

        @Override
        public void valueChanged(final ParameterChangeEvent<? extends Note> _parameter) {
            final Note noteValue = _parameter.getNewValue();
            if (noteValue.getNoteValue() > 0) {
                postEffect.clear();
                adsr.attackMode();
            } else {
                postEffect.clear();
                adsr.releaseMode();
            }
        }
    }

    protected final MachineParameterDefinition<Double> afactor = addRangeParameter("Attack raise factor", "Attack acceleration of the envelope.", "", 0.1d, 1d, 50, 0.5d);

    protected final MachineParameterDefinition<Double> dfactor = addRangeParameter("Decay factor", "Decay factor, after attack is finished.", "", 0.5d, 1d, 50, 0.93d);

    protected final MachineParameterDefinition<Gain> svalue = addGainParameter("Sustain volume", "Decay stops when the sustain volume is reached. Then the sustain decay is applied.", -60d, 0d, 60, -1d);

    protected final MachineParameterDefinition<Double> sfactor = addRangeParameter("Sustain decay factor", "Sustain decay factor.", "", 0.5d, 1d, 50, 0.99d);

    protected final MachineParameterDefinition<Double> rfactor = addRangeParameter("Release decay factor", "Release decay is applied when a \"off\" note is triggered.", "", 0.7d, 1d, 50, 0.5d);

    public QADSRSynthDefinition(final String name, final String _description, final TrackDefinition<?> trackDefinition) {
        super(name, _description, trackDefinition);
    }

    @Override
    public Generator<QADSRSynthDefinition> buildInstance() {
        return new Generator<QADSRSynthDefinition>(this);
    }
}
