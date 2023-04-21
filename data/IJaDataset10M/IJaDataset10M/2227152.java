package net.jadoth.collections.functions;

import net.jadoth.lang.branching.ThrowBreak;
import net.jadoth.lang.branching.ThrowContinue;
import net.jadoth.lang.branching.ThrowReturn;
import net.jadoth.lang.functional.aggregates.LoopAggregate;
import net.jadoth.lang.functional.controlflow.LoopPredicate;

public class AggregateCount<E> implements LoopAggregate<E, Integer> {

    private int count = 0;

    private final LoopPredicate<? super E> predicate;

    public AggregateCount(final LoopPredicate<? super E> predicate) {
        super();
        this.predicate = predicate;
    }

    @Override
    public void apply(final E element) throws ThrowBreak, ThrowContinue, ThrowReturn {
        if (this.predicate.apply(element)) {
            this.count++;
        }
    }

    @Override
    public LoopAggregate<E, Integer> reset() {
        this.count = 0;
        return this;
    }

    @Override
    public Integer yield() {
        return this.count;
    }
}
