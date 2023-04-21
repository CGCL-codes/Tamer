package uk.org.ogsadai.client.toolkit.activities.generic;

import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.SingleActivityInput;

/**
 * Interface used for traversable single activity outputs.  A traversable output
 * allows discovery of the connected inputs and also supports editing of the
 * output.
 *
 * @author The OGSA-DAI Project Team
 */
public interface TraversableSingleActivityOutput extends SingleActivityOutput {

    /**
     * Gets the activity to which this output belongs.
     * 
     * @return the activity to which this output belongs.
     */
    Activity getActivity();

    /**
     * Gets the inputs that are connected to this output.
     * 
     * @return the connected inputs.
     */
    SingleActivityInput[] getConnectedInputs();

    /**
     * Disconnects this output from all of its inputs.  If the output was 
     * connected to any inputs belonging to a <tt>GenericActivity</tt> then 
     * those outputs will also be informed of the disconnection.
     */
    void disconnect();

    /**
     * Adds a new connection to the output.  This method should be called only
     * by an input that is now connected to this output.  Consequently the
     * input is not informed of the new connection.
     * 
     * @param input input to connect to.
     */
    void addConnection(SingleActivityInput input);

    /**
     * Disconnects the specified input from the output. This method should be 
     * called only by an input that is now disconnected to this output.  
     * Consequently the input is not informed of the disconnection.
     * 
     * @param input input to be disconnected from.
     */
    void removeConnection(SingleActivityInput input);
}
