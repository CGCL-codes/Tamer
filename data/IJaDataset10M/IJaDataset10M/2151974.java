package algorithms.centralityAlgorithms;

public class DefaultTrafficMatrix extends AbsTrafficMatrix {

    private static final long serialVersionUID = 1L;

    public DefaultTrafficMatrix(int size) {
        m_size = size;
    }

    public double getWeight(int i, int j) {
        if (i == j) return 0; else return 1;
    }

    @Override
    public void setWeight(int i, int j, double w) {
        throw new UnsupportedOperationException("Method setWeight is not supported in DefaultTrafficMatrix.");
    }
}
