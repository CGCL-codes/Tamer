package jaxlib.io;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Simple extension of the {@link Callable} interface narrowing the type of exceptions to {@link IOException}.
 * Useful where the {@link #call()} method may be invoked directly.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: IOTask.java 3022 2011-12-07 06:31:51Z joerg_wassmer $
 */
public interface IOTask<V> extends Callable<V> {

    @Override
    public V call() throws IOException;
}
