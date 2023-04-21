package jaxlib.lang;

import javax.annotation.Nullable;

/**
 * An operation which can be invoked on a specified target.
 * 
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: Invokable.java 2769 2010-01-17 07:40:38Z joerg_wassmer $
 */
public interface Invokable<R, V> {

    public V invoke(@Nullable R receiver, @Nullable Object... arguments) throws Exception;
}
