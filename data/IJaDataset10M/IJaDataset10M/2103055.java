package gavator.datastructure;

import gavator.core.*;

/**
 * Apply some operations to object of T type, but not change its type
 * @pattern Pipe and Filter (POSA1) 
 */
public interface Filter<T> extends UnaryFunction<T, T> {
}
