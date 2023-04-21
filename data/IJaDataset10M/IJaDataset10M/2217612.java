package org.happy.commons.patterns.executeable.decorators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.happy.commons.patterns.decorator.Decorator_1x0Impl;
import org.happy.commons.patterns.executeable.Executable_1x2;
import org.happy.commons.patterns.executeable.decorators.ManyRetryExecutable_1x2.ExecutableFactory_1x2;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableListIterator;

/**
 * decorates an executable with many-retries methods. The executable will be tried to be executed for defined number of retries and then fails.
 * @author Andreas Hollmann
 *
 * @param <R>
 * @param <P>
 */
public class ManyRetryExecutable_1x2<R, P> implements Executable_1x2<R, P> {

    /**
	 * the factory creates executables for new retry
	 * @author Andreas Hollmann
	 * @param <R> return type of executable
	 * @param <P> parameter of executable
	 */
    public static interface ExecutableFactory_1x2<R, P> {

        public Executable_1x2<R, P> createExeceutable();
    }

    /**
	 * the same executeable will be executed many times after it fails
	 * decorates an executable with many-retries methods. The executable will be tried to be executed for defined number of retries and then fails.
	 * @param decorated
	 * @param maxRetryNumber
	 * @return
	 */
    public static <R, P> ManyRetryExecutable_1x2<R, P> of(final Executable_1x2<R, P> decorated, int maxRetryNumber) {
        return of(new ExecutableFactory_1x2<R, P>() {

            @Override
            public Executable_1x2<R, P> createExeceutable() {
                return decorated;
            }
        }, maxRetryNumber);
    }

    public static <R, P> ManyRetryExecutable_1x2<R, P> of(ExecutableFactory_1x2<R, P> fac, int maxRetryNumber) {
        return new ManyRetryExecutable_1x2<R, P>(fac, maxRetryNumber);
    }

    private int maxTryNumber;

    private ExecutableFactory_1x2<R, P> factory;

    private AtomicInteger tryCounter = new AtomicInteger(0);

    /**
	 * constructor
	 * @param decorated factory the factory which creates new executeable for every retry
	 * @param maxTryNumber after defined number of retries a IllegalStateException will be thrown. The IllegalStateException forwards thrown Exception in the execute method
	 */
    public ManyRetryExecutable_1x2(ExecutableFactory_1x2<R, P> factory, int maxTryNumber) {
        Preconditions.checkNotNull(factory);
        Preconditions.checkArgument(0 < maxTryNumber);
        this.factory = factory;
        this.maxTryNumber = maxTryNumber;
    }

    @Override
    public R execute(P parameter) {
        List<Throwable> thrownExceptions = new ArrayList<Throwable>();
        while (true) {
            tryCounter.incrementAndGet();
            try {
                Executable_1x2<R, P> executable = this.factory.createExeceutable();
                return executable.execute(parameter);
            } catch (Throwable t) {
                thrownExceptions.add(t);
                if (maxTryNumber < tryCounter.get()) {
                    throw new ToManyRetriesException("To Many Retries were done! maxRetries: " + this.maxTryNumber + "; made retries: " + tryCounter.get(), thrownExceptions);
                }
            }
        }
    }

    /**
	 * gets the number of made tries
	 * @return
	 */
    public int getTryNumber() {
        return this.tryCounter.get();
    }

    /**
	 * gets the max allowed number of tries 
	 * @return
	 */
    public int getMaxTryNumber() {
        return maxTryNumber;
    }

    /**
	 * this exception will be thrown if to many retries were done
	 * @author Andreas Hollmann
	 *
	 */
    public static class ToManyRetriesException extends RuntimeException {

        private static final long serialVersionUID = 7733153035129661168L;

        private List<Throwable> thrownExceptionsList = new ArrayList<Throwable>();

        public ToManyRetriesException(String message, List<Throwable> thrownExceptionsList) {
            super(message);
            this.thrownExceptionsList.addAll(thrownExceptionsList);
        }

        public ToManyRetriesException() {
            super();
        }

        public ToManyRetriesException(String message, boolean enableSuppression, boolean writableStackTrace) {
            super(message, null, enableSuppression, writableStackTrace);
        }

        public List<Throwable> getThrownExceptionsList() {
            return thrownExceptionsList;
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
            System.err.println("coused by folowing exceptions:");
            for (Throwable t : thrownExceptionsList) {
                t.printStackTrace();
            }
        }
    }
}
