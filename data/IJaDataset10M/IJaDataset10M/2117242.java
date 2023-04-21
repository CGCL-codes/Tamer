package org.slasoi.seval.prediction.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slasoi.models.scm.ServiceBuilder;
import org.slasoi.seval.prediction.service.IEvaluationResult;
import org.slasoi.seval.prediction.service.ISingleResult;

/**
 * A single evaluation result.
 * 
 * @author Benjamin Klatt
 * @author kuester
 * 
 */
public class EvaluationResult implements IEvaluationResult {

    /** The service builder that represents the service realization. */
    private ServiceBuilder builder = null;

    /** The list of individual response time results that constitute the overall evaluation result. */
    private List<ISingleResult> responseTimeResultSet = new ArrayList<ISingleResult>();

    /** The list of individual response time results that constitute the overall evaluation result. */
    private List<ISingleResult> reliabilityResultSet = new ArrayList<ISingleResult>();

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.IEvaluationResult#addResult(org.slasoi.seval.prediction.ISingleResult)
     */
    public final void addResult(final ISingleResult result) {
        responseTimeResultSet.add(result);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.common.IEvaluationResult#getBuilder()
     */
    public final ServiceBuilder getBuilder() {
        return builder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.common.IEvaluationResult#getBuilder()
     */
    public final void setBuilder(final ServiceBuilder builder) {
        this.builder = builder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.IEvaluationResult#getResults()
     */
    public final List<ISingleResult> getResults() {
        return responseTimeResultSet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.IEvaluationResult#getReliabilityResults()
     */
    public final List<ISingleResult> getReliabilityResults() {
        return reliabilityResultSet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.IEvaluationResult#getResponseTimeResults()
     */
    public List<ISingleResult> getResponseTimeResults() {
        return responseTimeResultSet;
    }

    /**
     * 
     * @return The string representation of an EvaluationResult object.
     */
    public String toString() {
        String s = "EvaluationResult";
        if (builder != null) {
            s += "for Builder ID: " + this.builder.getUuid() + "\n";
        }
        for (ISingleResult result : responseTimeResultSet) {
            s += " - " + result.toString() + "\n";
        }
        for (ISingleResult result : reliabilityResultSet) {
            s += " - " + result.toString() + "\n";
        }
        return s;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.IEvaluationResult#sort()
     */
    public final void sort() {
        Collections.sort(responseTimeResultSet);
        Collections.sort(reliabilityResultSet);
    }
}
