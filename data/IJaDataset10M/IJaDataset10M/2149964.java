package gnu.trove.function;

/**
 * Interface for functions that accept and return one Object reference.
 * <p/>
 * Created: Mon Nov  5 22:19:36 2001
 *
 * @author Eric D. Friedman
 * @version $Id: TObjectFunction.java,v 1.1.2.1 2009/09/06 17:02:19 upholderoftruth Exp $
 */
public interface TObjectFunction<T, R> {

    /**
     * Execute this function with <tt>value</tt>
     *
     * @param value an <code>Object</code> input
     * @return an <code>Object</code> result
     */
    public R execute(T value);
}
