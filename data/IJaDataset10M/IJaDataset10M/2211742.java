package com.mindtree.techworks.insight.reporting.jobs;

import java.io.Serializable;
import java.util.Map;
import com.mindtree.techworks.insight.reporting.jobs.message.JobMessage;

/**
 * A <code>Job</code> is an action that is invoked by a <code>Verifier</code>
 * when it meets its criteria. A <code>Job</code> is called when the Insight
 * action the <code>Verifier</code> is monitoring meets the criterions set in
 * the <code>Verifier</code>.
 * <p>
 * Jobs may be executed in <i>Synchronous (Blocking)</i> or <i>Asynchronous
 * (Non-Blocking)</i> mode depending on the implementation of the
 * <code>Job</code>. Hence, in particular cases where the <code>Job</code>
 * is invoked even before the entire Insight Action (like Filtering) is
 * completed, may block the original action for executing the <code>Job</code>.
 * When Insight is running in the interactive GUI mode, this might not only
 * block the user requested action, but also freeze up the UI. In such
 * scenarios, the <code>Verifier</code> might start the job execution in its
 * own <code>Thread</code>.
 * </p>
 * <p>
 * A <code>Job</code> may support any number of <code>JobMessage</code>s,
 * which can be checked by calling the <code>#getSupportedMessageTypes()</code>
 * or the <code>#isMessageTypeSupported(String)</code> methods. All
 * <code>Job</code>s must support atleast the <code>DefaultJobMessage</code>
 * type.
 * </p>
 * <p>
 * If the <code>#execute(JobMessage)</code> method is called with an
 * unsupported message type, the method would then throw an
 * <code>UnsupportedMessageTypeException</code>. Any exception conditions
 * during the execution of the <code>Job</code> would cause a
 * <code>JobExecutionException</code> to be thrown.
 * </p>
 * <p>
 * All <code>Job</code>s are uniquely identified by the fully qualified class
 * name of the implementing class and should be returned in the
 * <code>#getUID()</code> method.
 * </p>
 * <p>
 * A <code>Job</code> may be serialized (possibly as part of a
 * <code>Verifier</code>) for later reincarnation and execution. The format
 * for the serialized <code>Job</code> is mentioned in the documentation of
 * {@link com.mindtree.techworks.insight.reporting.jobs.JobPersistanceHandler JobPersistanceHandler}.
 * <code>JobPersistanceHandler</code> also handles the serialization and
 * deserialization of the <code>Jobs</code>. It gets the fields to be
 * serialized for the <code>Job</code> from the
 * <code>#getSerializableFields()</code> methods and it deserializes the
 * fields on the <code>Job</code> by calling the
 * <code>#setDeserializedField(String, String)</code>. All <code>Job</code>s
 * must provide a no-arguments public constructor for them to be deserialized.
 * </p>
 * 
 * @see com.mindtree.techworks.insight.reporting.jobs.message.JobMessage
 * @see com.mindtree.techworks.insight.reporting.verifiers.Verifier
 * @see com.mindtree.techworks.insight.reporting.jobs.JobPersistanceHandler
 * 
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 27 $ $Date: 2007-12-16 06:58:03 -0500 (Sun, 16 Dec 2007) $
 * @since Insight 1.5
 */
public interface Job extends Serializable {

    /**
	 * Returns the array of all different message types supported by the
	 * <code>Job</code>. The values returned would be the same as returned by
	 * the <code>getJobMessageType()</code> methods of each of the 
	 * <code>JobMessage</code> types supported by the <code>Job</code>.
	 * <p>
	 * All <code>Job</code>s are required to support atleast the 
	 * <code>DefaultJobMessage</code>.
	 * 
	 * @see JobMessage#getJobMessageType()
	 * @return An array of the types of the <code>JobMessage</code>s supported.
	 */
    public String[] getSupportedMessageTypes();

    /**
	 * Checks if the particular <code>JobMessage</code>, as identified by the
	 * message type, is supported by the <code>Job</code>. The value of the
	 * parameter should be the same as that returned by
	 * <code>JobMessage#getJobMessageType()</code>.
	 * 
	 * @see JobMessage#getJobMessageType()
	 * @param messageType
	 *            The message type to check.
	 * @return <code>true</code> if the message type is supported or
	 *         <code>false</code>
	 * 
	 */
    public boolean isMessageTypeSupported(String messageType);

    /**
	 * Executes the job with the data supplied in the <code>JobMessage</code>
	 * passed to the method. The action performed on calling the method depends
	 * on the implementation of the <code>Job</code>.
	 * <p>
	 * Jobs may be executed asynchronously, or synchronously depending on the
	 * implementation of the <code>Job</code>. The method contract for a
	 * <code>Job</code> does not specify that. However, <code>Verifiers</code>
	 * calling the <code>#executeJob(JobMessage)</code> method while the
	 * action they are monitoring need to aware that <i>blocking</i> jobs may
	 * hold up (or fail) the action they are monitoring. In such scenarios, the
	 * <code>Verifier</code> may as an additional performance feature despatch
	 * all calls on the <code>Job</code>s in a seperate asynchronous
	 * <code>Thread</code>.
	 * </p>
	 * 
	 * @param jobMessage
	 *            The message used for executing the <code>Job</code>.
	 * @throws UnsupportedMessageTypeException
	 *             If the <code>JobMessage</code> passed in is not supported
	 *             by the <code>Job</code>.
	 * @throws JobExecutionException
	 *             If there is problem executing the <code>Job</code>.
	 */
    public void execute(JobMessage jobMessage) throws UnsupportedMessageTypeException, JobExecutionException;

    /**
	 * Returns the 'display name' of the <code>Job</code> to be displayed to the
	 * user.
	 * 
	 * @return The display name of the job.
	 */
    public String getDisplayName();

    /**
	 * Returns the unique identifier for this <code>Job</code> - the fully 
	 * qualified name of the class implementing the interface.
	 * 
	 * @return The unique identifier of the Job.
	 */
    public String getUID();

    /**
	 * A <code>Map</code> of all fields that might be necessary for
	 * serializing the <code>Job</code>. This data will later used for
	 * recreating a <code>Job</code> instance from the serialized data.
	 * <p>
	 * Unlike <code>Verifiers</code> the data passed in the map may not be
	 * <code>array</code>s or <code>Collection</code>s.
	 * </p>
	 * 
	 * @see JobPersistanceHandler
	 * @return A map containing all fields that need to be persisted in the
	 *         <code>Job</code>.
	 */
    public Map getSerializableFields();

    /**
	 * Sets the deserialized field in the <code>Job</code>. This method would
	 * be called during the deserialization of a <code>Job</code> from the 
	 * store.
	 * 
	 * @see JobPersistanceHandler
	 * @param fieldName The name of the field being deserialized.
	 * @param fieldValue The value of the field being deserialized.
	 */
    public void setDeserializedField(String fieldName, String fieldValue);
}
