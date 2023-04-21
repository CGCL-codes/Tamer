package uk.org.ogsadai.activity.generic;

import java.util.Map;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;

/**
 *
 * @author The ADMIRE Project Team.
 */
public interface ScriptActivity {

    /**
	 * Processes the activity to completion. This method blocks until
     * the processing is complete.
     * 
	 * @param inputs
	 * @param outputs
	 *
	 * @throws ActivityUserException
	 * If the settings specified by the user prevent processing from completing.
     * @throws ActivityProcessingException
     * If an internal error prevents processing from completing. 
	 * @throws ActivityTerminatedException
	 * If activity processing is terminated at an intermediate stage. This occurs 
	 * when the thread processing the activity is interrupted.
	 */
    void process(Map<String, BlockReader> inputs, Map<String, BlockWriter> outputs) throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException;
}
