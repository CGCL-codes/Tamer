package org.inqle.experiment.rapidminer;

import org.inqle.data.rdf.RDF;
import org.inqle.data.rdf.jenabean.BasicJenabean;
import org.inqle.data.rdf.jenabean.IBasicJenabean;
import thewebsemantic.Namespace;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.performance.PerformanceVector;

/**
 * @author David Donohue
 * Apr 22, 2008
 */
@Namespace(RDF.INQLE)
public class ExperimentResult extends BasicJenabean {

    private PerformanceVector performanceVector;

    private LearningCycle learningCycle;

    private OperatorException operatorException;

    public PerformanceVector getPerformanceVector() {
        return performanceVector;
    }

    public void setPerformanceVector(PerformanceVector performanceVector) {
        this.performanceVector = performanceVector;
    }

    public void clone(ExperimentResult copyFieldsFrom) {
        setLearningCycle(copyFieldsFrom.getLearningCycle());
        setPerformanceVector(copyFieldsFrom.getPerformanceVector());
    }

    @Override
    public IBasicJenabean createClone() {
        ExperimentResult newExperimentResult = new ExperimentResult();
        newExperimentResult.clone(this);
        return newExperimentResult;
    }

    @Override
    public IBasicJenabean createReplica() {
        ExperimentResult newExperimentResult = new ExperimentResult();
        newExperimentResult.replicate(this);
        return newExperimentResult;
    }

    public LearningCycle getLearningCycle() {
        return learningCycle;
    }

    public void setLearningCycle(LearningCycle learningCycle) {
        this.learningCycle = learningCycle;
    }

    public OperatorException getOperatorException() {
        return operatorException;
    }

    public void setOperatorException(OperatorException operatorException) {
        this.operatorException = operatorException;
    }
}
