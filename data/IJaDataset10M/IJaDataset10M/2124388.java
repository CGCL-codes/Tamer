package eu.pisolutions.closure;

import eu.pisolutions.lang.Validations;

/**
 * {@link eu.pisolutions.closure.Closure} that executes a <code>Closure</code> a specific number of times.
 *
 * @author Laurent Pireyn
 */
public final class ForLoopClosure<T> extends ClosureWrapper<T> {

    private final int count;

    public ForLoopClosure(Closure<? super T> closure, int count) {
        super(closure);
        Validations.greaterThanOrEqualTo(count, 0, "count");
        this.count = count;
    }

    @Override
    public void execute(T input) {
        for (int i = 0; i < this.count; ++i) {
            this.closure.execute(input);
        }
    }
}
