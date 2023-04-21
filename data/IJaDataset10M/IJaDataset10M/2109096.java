package uk.org.ogsadai.dqp.execute.workflow;

import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activities.generic.TraversableSingleActivityOutput;

/**
 * Implementations of this interface add activities to a builder which serialise
 * and deserialise tuple lists into a certain format.
 *
 * @author The OGSA-DAI Project Team.
 */
public interface TupleSerialiser {

    /**
     * Sets the size of a block that is produced by the serialiser. A block
     * could be a byte array or a character array, depending on the
     * implementation.
     * 
     * @param blockSize
     *            size of blocks
     */
    public void setBlockSize(int blockSize);

    /**
     * Adds serialiser activities to the given builder. The data is streamed
     * from the output.
     * 
     * @param output
     *            output providing the serialised tuple list
     * @param builder
     *            builds a pipeline workflow
     * @return the output producing the serialised tuple list
     */
    public TraversableSingleActivityOutput addSerialiser(SingleActivityOutput output, PipelineWorkflowBuilder builder);

    /**
     * Adds deserialiser activities to the given builder. The input data is
     * streamed from the output.
     * 
     * @param output
     *            output providing the deserialised tuple list
     * @param builder
     *            builds a pipeline workflow
     * @return the output producing the deserialised tuple list
     */
    public TraversableSingleActivityOutput addDeserialiser(SingleActivityOutput output, PipelineWorkflowBuilder builder);
}
