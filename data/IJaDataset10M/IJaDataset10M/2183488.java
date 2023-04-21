package jorgan.memory.state;

import java.util.ArrayList;
import java.util.List;
import jorgan.disposition.Combination;
import jorgan.disposition.Continuous;
import jorgan.disposition.Reference;
import jorgan.disposition.Switch;

/**
 * The state of a {@link Combination}.
 */
public class CombinationState {

    private long id;

    private List<ReferenceState<?>> references = new ArrayList<ReferenceState<?>>();

    public CombinationState(Combination combination) {
        this.id = combination.getId();
    }

    public boolean isFor(Combination combination) {
        return this.id == combination.getId();
    }

    public void clear(int index) {
        for (ReferenceState<?> state : references) {
            state.clear(index);
        }
    }

    public void swap(int index1, int index2) {
        for (ReferenceState<?> state : references) {
            state.swap(index1, index2);
        }
    }

    private ReferenceState<?> getState(Reference<?> reference) {
        for (ReferenceState<?> state : references) {
            if (state.isFor(reference.getElement())) {
                return state;
            }
        }
        ReferenceState<?> state;
        if (reference.getElement() instanceof Switch) {
            state = new SwitchReferenceState((Switch) reference.getElement());
        } else if (reference.getElement() instanceof Continuous) {
            state = new ContinuousReferenceState((Continuous) reference.getElement());
        } else {
            throw new Error();
        }
        references.add(state);
        return state;
    }

    public void read(Combination combination, int index) {
        for (Reference<?> reference : combination.getReferences()) {
            ReferenceState<?> state = getState(reference);
            state.read(reference, index);
        }
    }

    public void read(Reference<?> reference, int index) {
        ReferenceState<?> state = getState(reference);
        state.read(reference, index);
    }

    public void write(Combination combination, int index) {
        for (Reference<?> reference : combination.getReferences()) {
            ReferenceState<?> state = getState(reference);
            state.write(reference, index);
        }
    }

    public Object get(Reference<?> reference, int index) {
        ReferenceState<?> state = getState(reference);
        return state.get(index);
    }
}
