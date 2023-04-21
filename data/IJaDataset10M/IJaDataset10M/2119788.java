package ca.cutterslade.match.scheduler;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

final class Executor {

    private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {

        private final ThreadFactory f = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = f.newThread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    int sum(Collection<? extends Callable<Integer>> cs) throws InterruptedException {
        List<Future<Integer>> fs = Lists.newArrayListWithCapacity(cs.size());
        for (Callable<Integer> c : cs) fs.add(service.submit(c));
        int sum = 0;
        for (Future<Integer> f : fs) try {
            sum += f.get().intValue();
        } catch (ExecutionException e) {
            throw new AssertionError(e);
        }
        return sum;
    }

    <T> ImmutableSet<T> interleaf(Iterable<? extends Callable<Set<T>>> cs) throws InterruptedException {
        List<Future<Set<T>>> fs = Lists.newArrayList();
        for (Callable<Set<T>> c : cs) fs.add(service.submit(c));
        List<Set<T>> results = Lists.newArrayList();
        for (Future<Set<T>> f : fs) try {
            results.add(f.get());
        } catch (ExecutionException e) {
            throw new AssertionError(e);
        }
        return ImmutableSet.copyOf(new AlternatingIterable<T>(results));
    }
}
