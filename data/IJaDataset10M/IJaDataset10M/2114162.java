package org.parallelj.mda.rt.controlflow.engine.loop;

import java.util.Iterator;
import org.parallelj.mda.rt.controlflow.engine.Engine;
import org.parallelj.mda.rt.controlflow.engine.Flow;
import org.parallelj.mda.rt.controlflow.engine.Instruction;
import org.parallelj.mda.rt.controlflow.engine.Join;
import org.parallelj.mda.rt.controlflow.engine.Place;
import org.parallelj.mda.rt.controlflow.engine.Split;
import org.parallelj.mda.rt.controlflow.engine.Step;

public class ForEachLoop<E extends Engine> extends Instruction<E> {

    /**
	 * Corresponds to the state of this {@link ForEachLoop}.
	 * 
	 * <p>
	 * If the state {@link Place#contains(short[])} contains no token (empty),
	 * that means that no action within this {@link ForEachLoop} has been
	 * started.
	 * </p>
	 * <p>
	 * If the state contains tokens, the iteration has been started.
	 * </p>
	 * 
	 */
    Place state = new Place();

    /**
	 * Corresponds to the difference between the number of tokens consumed by
	 * the first step and the number of tokens produced by the last one.
	 */
    Place diff = new Place();

    public ForEachLoop(Flow<E> flow, final Step<E, ?> first, final Step<E, ?> last) {
        super(flow);
        if (first == null || last == null) {
            throw new IllegalArgumentException("join or split is null");
        }
        flow.addPlace(this.state);
        flow.addPlace(this.diff);
        first.setJoin(this.newJoin(first.getJoin()));
        last.setFork(this.newSplit(last.getSplit()));
    }

    Join<E> newJoin(final Join<E> join) {
        return new Join<E>(this.getFlow()) {

            @Override
            public boolean isEnabled(E engine) {
                return hasNext(engine) && (state.contains(this.getMarking(engine)) || join.isEnabled(engine));
            }

            @Override
            public void join(E engine) {
                if (!state.contains(this.getMarking(engine))) {
                    join.join(engine);
                    state.produce(this.getMarking(engine));
                }
                diff.produce(this.getMarking(engine));
            }
        };
    }

    Split<E> newSplit(final Split<E> split) {
        return new Split<E>(this.getFlow()) {

            @Override
            public void split(E engine) {
                diff.consume(this.getMarking(engine));
                if ((diff.isEmpty(this.getMarking(engine))) && !hasNext(engine)) {
                    reset(engine);
                    split.split(engine);
                }
            }
        };
    }

    public void reset(E engine) {
        this.state.init(this.getMarking(engine));
        this.diff.init(this.getMarking(engine));
    }

    boolean hasNext(E engine) {
        Iterator iterator = this.getInputElement(engine);
        return (iterator == null) ? false : iterator.hasNext();
    }

    /**
	 * Return the element to iterate on.
	 * 
	 * This method must be overriden bu subclasses.
	 * 
	 * @param engine
	 * @return the iterator or <code>null</code>.
	 */
    protected Iterator getInputElement(E engine) {
        return null;
    }
}
