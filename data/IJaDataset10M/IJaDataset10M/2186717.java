package ijaux.hypergeom;

public interface MetricSpace<VectorType> {

    double distance(VectorType a, VectorType b);

    double norm(VectorType a);
}
