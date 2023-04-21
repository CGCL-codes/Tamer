package edu.georgetown.nnj.filterblocks.flow;

import de.ifn_magdeburg.kazukazuj.K;
import edu.georgetown.nnj.data.NNJDataSource;

/**This template provides the spiral component of an orthogonal spatial 
 * decomposition.
 *
 * @author Kenta
 * @version 0.4.0
 */
public class NNJFlowTP extends NNJFlowTemplate {

    public NNJFlowTP(NNJDataSource source) {
        super(source);
    }

    public NNJFlowTP(NNJDataSource source, boolean filterActive) {
        super(source, filterActive);
    }

    protected NNJFlowTP() {
        super();
    }

    private double TEMPLATE_ANGLE = 2 * K.PI / 6;

    @Override
    protected void generateTemplateVector() {
        TEMPLATE_VECTOR = new double[this.getDataLayout().getMaxRing() + 1][];
        for (int ring = 1; ring < TEMPLATE_VECTOR.length; ring++) {
            double[][] uvs = this.getDataLayout().getNeighborVectorsFlowExtension(ring);
            TEMPLATE_VECTOR[ring] = new double[uvs.length];
            for (int k = 0; k < this.getDataLayout().getDirectionCount(ring); k++) {
                TEMPLATE_VECTOR[ring][k] = 0;
            }
            for (int k = this.getDataLayout().getDirectionCount(ring); k < uvs.length; k++) {
                TEMPLATE_VECTOR[ring][k] = 1;
            }
        }
    }

    @Override
    public double calculateMatchValue(int ring, double[][] pairwiseFlowVector) {
        return super.calculateMatchValue(ring, pairwiseFlowVector) * TEMPLATE_ANGLE;
    }

    @Override
    public double[] getTemplatePredictionVector(int ring, double matchValue) {
        return super.getTemplatePredictionVector(ring, matchValue / TEMPLATE_ANGLE);
    }
}
