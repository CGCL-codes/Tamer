package co.edu.unal.ungrid.services.client.applet.subtraction.model;

import co.edu.unal.ungrid.grid.master.JobResult;
import co.edu.unal.ungrid.similarity.SimilarityMeasure;
import co.edu.unal.ungrid.transformation.Transform;

public class SubtractionJobResult extends JobResult {

    private static final long serialVersionUID = 1L;

    public SubtractionJobResult(final String sUsrId) {
        this(sUsrId, null, null);
    }

    public SubtractionJobResult(final String sUsrId, final Transform t, final SimilarityMeasure<Double> m) {
        super(sUsrId, t);
        this.m = m;
    }

    public SimilarityMeasure<Double> m;
}
