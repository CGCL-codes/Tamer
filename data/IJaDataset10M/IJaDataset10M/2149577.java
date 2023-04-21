package bioweka.filters;

import weka.core.FastVector;
import weka.core.Instances;
import bioweka.core.debuggers.Debugger;

/**
 * Abstract base class for deterministic filters, i.e. filters that determine
 * the output format at the moment the input format is given, thus the
 * {@link #setInputFormat(Instances)} method returns <code>true</code>.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractDeterministicFilter extends AbstractBioWekaFilter {

    /**
     * Initializes the deterministic filter.
     */
    public AbstractDeterministicFilter() {
        super();
    }

    /**
     * Lists the attributes for the output format.
     * @param attributes a vector which should be filled with 
     * {@link weka.core.Attribute} objects
     * @throws Exception if the attributes could not be listed. 
     */
    protected abstract void listAttributes(FastVector attributes) throws Exception;

    /**
     * {@inheritDoc}
     */
    public boolean setInputFormat(Instances instanceInfo) throws Exception {
        Debugger debugger = getDebugger();
        debugger.enters("setInputFormat", instanceInfo);
        super.setInputFormat(instanceInfo);
        FastVector attributes = new FastVector();
        listAttributes(attributes);
        Instances outputFormat = createDataSet(instanceInfo, attributes);
        setOutputFormat(outputFormat);
        debugger.config("outputFormat", outputFormat);
        outputFormatPeek().setClassIndex(instanceInfo.classIndex());
        debugger.exits("setInputFormat", Boolean.valueOf(true));
        return true;
    }
}
