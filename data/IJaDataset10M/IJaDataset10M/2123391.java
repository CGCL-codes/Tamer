package uk.ac.shef.wit.trex.task.runner;

import uk.ac.shef.wit.trex.TrexException;
import uk.ac.shef.wit.trex.representation.Representation;
import uk.ac.shef.wit.trex.task.BatchTask;

public class BatchTaskRunnerImplMemoryCached extends BatchTaskRunnerImplDecorator {

    protected Representation _representation;

    public BatchTaskRunnerImplMemoryCached(final BatchTaskRunnerImpl implementation) {
        super(implementation);
    }

    public Representation generateRepresentation(final BatchTask task) throws TrexException {
        if (_representation == null) _representation = _impl.generateRepresentation(task);
        reportRepresentation(task, _representation);
        return _representation;
    }
}
