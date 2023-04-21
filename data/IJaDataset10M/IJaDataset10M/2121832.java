package org.tigr.microarray.mev.cgh.CGHDataObj.Cluster.Experiment;

import org.tigr.microarray.mev.cluster.gui.Experiment;
import org.tigr.util.FloatMatrix;

/**
 *
 * @author  Adam Margolin
 * @author Raktim Sinha
 */
public class CGHExperiment extends Experiment {

    String[] annotations;

    public CGHExperiment(FloatMatrix matrix, int[] columns, String[] annotations) {
        super(matrix, columns);
        this.annotations = annotations;
    }

    /**
     * Constructs an <code>Experiment</code> with specified
     * matrix of ratio values, columns indices, and row indices
     */
    public CGHExperiment(FloatMatrix matrix, int[] columns, int[] rows) {
        super(matrix, columns, rows);
        this.annotations = annotations;
    }

    /** Getter for property annotations.
     * @return Value of property annotations.
     */
    public java.lang.String[] getAnnotations() {
        return this.annotations;
    }

    /** Setter for property annotations.
     * @param annotations New value of property annotations.
     */
    public void setAnnotations(java.lang.String[] annotations) {
        this.annotations = annotations;
    }
}
