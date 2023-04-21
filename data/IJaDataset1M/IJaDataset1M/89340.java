package org.nexopenframework.management.agent.support;

/**
 * <p>datamining-batch using NexOpen</p>
 * 
 * <p>Comment here</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JavaSystemExitCallback implements SystemExitCallback {

    /**
	 * <p>Invokes {@link System#exit(int)}</p>
	 * 
	 * @see com.strands.datamining.batch.support.SystemExitCallback#exit(int)
	 */
    public void exit(final int status) {
        System.exit(status);
    }
}
