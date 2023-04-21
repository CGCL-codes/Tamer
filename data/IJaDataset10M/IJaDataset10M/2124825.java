package hpiC77Lt.bspfmvh4;

public interface BinaryOperation<T> {

    public abstract T make(final T t1, final T t2);

    public abstract T identity();

    public abstract T inverse(final T t) throws Uninvertable;
}
