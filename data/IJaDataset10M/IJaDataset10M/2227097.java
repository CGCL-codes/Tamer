package uk.org.ogsadai.activity.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.org.ogsadai.activity.block.TeeActivity;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.util.UniqueName;

/**
 * Transforms the activity pipeline by inserting tee activities for any activity
 * outputs that require it.
 * 
 * @author The OGSA-DAI Project Team
 */
public class AutomaticTee implements ActivityPipelineTransformation {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Logger for this class. */
    private static final DAILogger LOG = DAILogger.getLogger(AutomaticTee.class);

    /** A default name for the Tee activity that is being inserted. */
    public static final String NAME = "uk.org.ogsadai.Tee";

    /**
     * {@inheritDoc}
     */
    public void transform(final ActivityPipeline pipeline) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Graph before transformation: ");
            LOG.debug(pipeline.toString());
        }
        final Map inputsToTee = new HashMap();
        initialise(pipeline, inputsToTee);
        setOutputsNumReadersAttribute(pipeline, inputsToTee);
        for (Iterator i = inputsToTee.keySet().iterator(); i.hasNext(); ) {
            String pipeName = (String) i.next();
            Collection inputs = (Collection) inputsToTee.get(pipeName);
            ActivityDescriptor tee = new SimpleActivityDescriptor(NAME);
            tee.addInput(new ActivityInputStream(TeeActivity.INPUT_DATA, pipeName));
            pipeline.addActivity(tee);
            for (Iterator j = inputs.iterator(); j.hasNext(); ) {
                ActivityInputStream input = (ActivityInputStream) j.next();
                final String pipe = UniqueName.newName();
                tee.addOutput(new ActivityOutputStream(TeeActivity.OUTPUT_STREAMS, pipe));
                input.getTargetActivity().replaceInput(input, new ActivityInputStream(input.getInputName(), pipe));
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Transformed graph: ");
            LOG.debug(pipeline.toString());
        }
    }

    /**
     * Initialises the list of inputs to tee.
     * 
     * @param inputsToTee
     *            a map of pipe name to collection of activity input streams
     * @param pipeline
     *            a map of pipe name to activity output stream activity graph to
     *            search
     */
    private void initialise(final ActivityPipeline pipeline, final Map inputsToTee) {
        final ActivityIOVisitor visitor = new ActivityIOVisitor() {

            public void visitInputLiteral(ActivityInputLiteral input) {
            }

            public void visitInputStream(ActivityInputStream stream) {
                List inputs = null;
                if (inputsToTee.containsKey(stream.getPipeName())) {
                    inputs = (List) inputsToTee.get(stream.getPipeName());
                } else {
                    inputs = new ArrayList();
                    inputsToTee.put(stream.getPipeName(), inputs);
                }
                inputs.add(stream);
            }

            public void visitOutputStream(ActivityOutputStream stream) {
            }
        };
        ActivityUtilities.visitAllInputsAndOutputs(pipeline, visitor);
        final Set keys = new HashSet(inputsToTee.keySet());
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String pipeName = (String) i.next();
            Collection inputs = (Collection) inputsToTee.get(pipeName);
            if (inputs.size() < 2) {
                inputsToTee.remove(pipeName);
            }
        }
    }

    /**
     * Sets the number of readers attribute of each output in the activity
     * pipeline.
     *  
     * @param pipeline     activity pipeline
     * @param inputsToTee  mapping from pipename to a list of inputs that use
     *                     that pipe.  This map will only contain entries for
     *                     pipe that are associated with two or more inputs.  
     *                     In such cases there will be in entry in the list for
     *                     each input.  TODO: This structure needs cleaned up
     *                     if we eliminate automatic Tee insertion and instead
     *                     adopt the idea of multiply readable pipes. 
     */
    private void setOutputsNumReadersAttribute(final ActivityPipeline pipeline, final Map inputsToTee) {
        final ActivityIOVisitor visitor = new ActivityIOVisitor() {

            public void visitInputLiteral(ActivityInputLiteral input) {
            }

            public void visitInputStream(ActivityInputStream stream) {
                int numReaders = 1;
                String pipeName = stream.getPipeName();
                if (inputsToTee.containsKey(pipeName)) {
                    numReaders = ((List) inputsToTee.get(pipeName)).size();
                }
                stream.getAttributes().put(new Key("uk.org.ogsadai.NumberOfReaders"), new Integer(numReaders));
            }

            public void visitOutputStream(ActivityOutputStream stream) {
            }
        };
        ActivityUtilities.visitAllInputsAndOutputs(pipeline, visitor);
    }
}
