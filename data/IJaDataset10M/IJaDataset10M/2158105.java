package bioweka.filters;

import bioweka.core.debuggers.Debugger;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.StreamableFilter;

/**
 * Abstract base class for {@linkplain weka.filters.StreamableFilter streamable}
 * filters.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.3 $
 */
public abstract class AbstractStreamableFilter extends AbstractDeterministicFilter implements StreamableFilter {

    /**
     * Initializes the streamable filter.
     */
    public AbstractStreamableFilter() {
        super();
    }

    /**
     * Transforms an instance and 
     * {@linkplain weka.filters.Filter#push(Instance) pushes} zero, one or more
     * instances to the output queue.
     * @param instance the input instance which should be transformed 
     * @throws Exception if the instance could not be transformed.
     */
    protected abstract void transform(Instance instance) throws Exception;

    /**
     * Indicates that a new filter batch has started.
     */
    protected abstract void newBatch();

    /**
     * {@inheritDoc}
     */
    public boolean input(Instance instance) throws Exception {
        Debugger debugger = getDebugger();
        debugger.enters("input", instance);
        Instances inputFormat = getInputFormat();
        if (inputFormat == null) {
            throw new NullPointerException("No input instance format defined");
        }
        if (m_NewBatch) {
            resetQueue();
            m_NewBatch = false;
            newBatch();
        }
        transform(instance);
        debugger.exits("input", Boolean.valueOf(true));
        return true;
    }
}
