package uk.org.ogsadai.activity.workflow;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityBase;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;

/**
 * An activity for testing purposes that consumes input data from the first 
 * input in its context, and produces output data at its first output. The
 * output data consists of the input data prepended with a string value 
 * specified by the constructor. The blocks must be strings. 
 * 
 * @author The OGSA-DAI Project Team
 */
public class BodyActivity extends ActivityBase implements Activity {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2007";

    public void process() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        BlockReader input = getInput();
        BlockWriter output = getOutput();
        Object block;
        try {
            while ((block = input.read()) != ControlBlock.NO_MORE_DATA) {
                final String inputBlock = (String) block;
                final String outputBlock = new String(inputBlock);
                output.write(outputBlock);
            }
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeClosedException e) {
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        }
    }
}
