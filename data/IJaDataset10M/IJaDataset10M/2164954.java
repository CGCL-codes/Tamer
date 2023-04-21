package hpiC77Lt.bspfmvh4;

public interface Metric<M, R> {

    public abstract R distance(M m1, M m2);

    public abstract Real<R> real();
}
