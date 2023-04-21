package engine;

import java.util.List;

public class IndexedAggregate<T extends Assignable<T>> implements Aggregate<T>, Assignable<Aggregate<T>> {

    private static class OthersChoice implements Choice {

        @Override
        public boolean covers(int what) {
            return true;
        }
    }

    private static class IndexedAggregatePair<ST extends Assignable<ST>> {

        private Choice[] choices;

        private ST value;

        public IndexedAggregatePair(Choice[] choices, ST value) {
            this.choices = choices;
            this.value = value;
        }

        public Choice[] getChoices() {
            return choices;
        }

        public ST getValue() {
            return value;
        }
    }

    public IndexedAggregate(IndexedAggregatePair<T>[] pairs) {
    }

    @Override
    public void bindOther(SequenceAggregate<T> aggregate, TimingController controller) {
    }

    @Override
    public void fillOther(SequenceAggregate<T> aggregate, TimingController controller) {
    }

    @Override
    public List<T> qualifyFor(Range range) {
        return null;
    }

    @Override
    public void bind(Aggregate<T> other, TimingController controller) {
    }

    @Override
    public void setValue(Aggregate<T> other, TimingController controller) {
    }

    @Override
    public ReferenceRepresentation getReferenceRepresentation() {
        return null;
    }

    public static <ST extends Assignable<ST>> IndexedAggregate<ST> from(IndexedAggregatePair<ST>... pairs) {
        return new IndexedAggregate<ST>(pairs);
    }

    public static <ST extends Assignable<ST>> IndexedAggregatePair<ST> pair(ST value, Choice... choices) {
        return new IndexedAggregatePair<ST>(choices, value);
    }

    public static Choice others() {
        return new OthersChoice();
    }
}
