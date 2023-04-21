package net.jadoth.lang.functional;

public interface Executing<T, O extends Procedure<? super T>> {

    public Executing<T, O> execute(O procedure);

    /**
	 * Wrapper class that extends {@link Executing} and wraps a subject of type T that procedures shall be
	 * executed on.
	 * <p>
	 * By using an executor instance, an instance not implementing {@link Executing} can be passed to a context
	 * expecting an {@link Executing} instance. Through this abstraction, logic can be written that can be
	 * equally executed on single objects (via this wrapper) or multiple objects (via X-collections).
	 * <p>
	 * <u>Example</u>:<code><pre> someRegistryLogic.register(persons);
	 * someRegistryLogic.register(new Exector<Person>(singlePerson));
	 * </pre></code>
	 *
	 * @author Thomas M�nz
	 *
	 */
    public final class Executor<T> implements Executing<T, Procedure<? super T>> {

        private final T subject;

        public Executor(final T subject) {
            super();
            this.subject = subject;
        }

        @Override
        public Executor<T> execute(final Procedure<? super T> procedure) {
            procedure.apply(this.subject);
            return this;
        }
    }
}
