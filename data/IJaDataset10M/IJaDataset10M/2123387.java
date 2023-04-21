package cross.datastructures.threads;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * Allows the creation of a CompletionService which can create an iterator over
 * the results of the Callables results. There is no guaranteed order in which
 * results are returned, so you will need to provide the callable with some kind
 * of id to associate it later on.
 * 
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 * 
 */
public class CallableCompletionService<T> implements Iterable<T> {

    private class ResultIterator implements Iterator<T> {

        private int elements = 0;

        private final CompletionService<T> ecs;

        public ResultIterator(final CompletionService<T> ecs, final int elements) {
            this.ecs = ecs;
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            if (this.elements > 0) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            try {
                final T t = this.ecs.take().get();
                this.elements--;
                return t;
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } catch (final ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final CompletionService<T> ecs;

    private int callables = 0;

    /**
	 * Initializes this CallableCompletionService to use the specified Executor
	 * for execution of tasks.
	 * 
	 * @param e
	 */
    public CallableCompletionService(final Executor e) {
        this.ecs = new ExecutorCompletionService<T>(e);
    }

    @Override
    public Iterator<T> iterator() {
        return new ResultIterator(this.ecs, this.callables);
    }

    /**
	 * Submits a collection of Callables to this objects CompletionService. This
	 * method returns immediately. Use the iterator provided to receive results
	 * in undetermined order.
	 * 
	 * @param c
	 */
    public void submit(final Collection<Callable<T>> c) {
        this.callables = c.size();
        for (final Callable<T> s : c) {
            this.ecs.submit(s);
        }
    }
}
