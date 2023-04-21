package pl.clareo.coroutines.user;

/**
 * Interface used to control coroutine's execution. Its purpose is to further
 * decouple coroutine's caller (client of {@link CoIterator}) from the
 * coroutine's logic. Caller may use {@link CoIterator#with(Controler)} method
 * and consume results without burden of sending appropriate values to
 * underlying coroutine - this task being imposed on this <code>Controler</code>
 * 
 * @author Marcin Rzeźnicki
 */
public interface Controler<E, A> {

    /**
     * Called when coroutine has been closed
     */
    void closed();

    /**
     * Generate value sent to coroutine on the first
     * {@link Coroutines#yield(Object) yield}
     * 
     * @return value to be sent to coroutine
     */
    A init();

    /**
     * Called when coroutine has been closed because it was unable to generate
     * more results
     */
    void noNextElement();

    /**
     * Generates the next value sent to coroutine after some
     * {@link Coroutines#yield() yield} has generated <code>produced</code>
     * 
     * @param produced
     *            last yielded value
     * @return value to be sent to coroutine
     * @throws IllegalStateException
     *             <code>Controler</code> may throw this exception to forcibly
     *             close underlying coroutine
     */
    A respondTo(E produced) throws IllegalStateException;
}
