package q.zik.basic.generator;

import q.zik.core.machine.definition.GeneratorDefinition;
import q.zik.core.machine.instance.Generator;
import q.zik.core.machine.instance.GeneratorTrack;
import q.zik.lang.Sample;

public class QBeatDefinition extends GeneratorDefinition {

    public static class TrackDefinition extends GeneratorDefinition.GeneratorTrackDefinition<Generator<QBeatDefinition>> {

        @Override
        public GeneratorTrack<?, ?> buildInstance(final Generator<QBeatDefinition> _machine) {
            return new Track(this, _machine);
        }
    }

    public static class Track extends GeneratorTrack<TrackDefinition, Generator<QBeatDefinition>> {

        public Track(final TrackDefinition _definition, final Generator<QBeatDefinition> _machine) {
            super(_definition, _machine);
        }

        @Override
        protected Sample generate(final int _time) {
            return Sample.SILENCE;
        }
    }

    public QBeatDefinition() {
        super("QBeat", "Beat generator. Not implemented yet - do not use.", new TrackDefinition());
    }

    @Override
    public Generator<QBeatDefinition> buildInstance() {
        return new Generator<QBeatDefinition>(this);
    }
}
