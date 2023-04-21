package jaque.functions;

/**
 * Interface for a function type which receives two parameters of type T, R and
 * returns a value of type Result.
 * 
 * @param <Result>
 *            the type of the return value.
 * @param <T>
 *            the type of the first parameter.
 * @param <R>
 *            the type of the second parameter.
 * 
 * @author <a href="mailto://object_streaming@googlegroups.com">Konstantin Triger</a>
 */
public interface Function2<Result, T, R> {

    public Result invoke(T t, R r) throws Throwable;
}
