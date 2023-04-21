package uk.org.ogsadai.performance.execute.type;

import java.sql.SQLException;
import java.util.Random;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Workflow;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.performance.db.Experiment;
import uk.org.ogsadai.performance.db.Measurement;
import uk.org.ogsadai.performance.exception.ConfigurationException;
import uk.org.ogsadai.performance.util.ConfigReader;
import uk.org.ogsadai.performance.util.ConfigReaderFactory;
import uk.org.ogsadai.performance.util.Messages;
import uk.org.ogsadai.performance.workflow.ExecutableWorkflow;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * This class executes a workflow with 
 * <code>RequestExecutionType.SYNCHRONOUS</code> mode. The workflow will be 
 * executed repeatedly until the <code>stopExecution()</code> method is called. 
 * To simulate real clients, there is a random sleep between successive 
 * executions.
 * <p>
 * This class requires the following parameters to be defined.
 * </p>
 * <ul>
 * <li>
 * <code>max_random_sleep</code> :
 *     Random sleep will be between zero and this value. 
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class SynchronousExecutor extends ExecutorType {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2009.";

    /**
     * Max Random Sleep property key.
     */
    public static final String MAXRANDOMSLEEP_PROPERTY = "max_random_sleep";

    /**
     * Flag to show if the execution loop should continue or not.
     */
    private boolean mKeepRunning = true;

    /**
     * Successive executions are separated by a random sleep. The sleep is 
     * between 0 and this variable.
     */
    private int mRandomSleepMax;

    /**
     * Random number generator.
     */
    private static Random mGenerator = new Random();

    /**
     * Constructor.
     * 
     * @param executableWorkflow      workflow
     * @param experiment              experiment
     * 
     * @throws ConfigurationException
     */
    public SynchronousExecutor(final ExecutableWorkflow executableWorkflow, final Experiment experiment) throws ConfigurationException {
        super(executableWorkflow, experiment);
        ConfigReader configReader = ConfigReaderFactory.getConfigReader();
        mRandomSleepMax = configReader.getIntegerProperty(MAXRANDOMSLEEP_PROPERTY);
    }

    /**
     * Executes workflow.
     */
    public void run() {
        while (mKeepRunning && (!Thread.currentThread().isInterrupted())) {
            String status = null;
            RequestResource requestResource = null;
            long startTime = System.currentTimeMillis();
            long endTime = -1;
            try {
                long randomSleep = mGenerator.nextInt(mRandomSleepMax);
                Thread.sleep(randomSleep);
                startTime = System.currentTimeMillis();
                DataRequestExecutionResource drer = mExecutableWorkflow.getDRER();
                Workflow workflow = mExecutableWorkflow.getWorkflow();
                requestResource = drer.execute(workflow, RequestExecutionType.SYNCHRONOUS);
                endTime = System.currentTimeMillis();
                RequestStatus requestStatus = requestResource.getRequestStatus();
                status = (requestStatus.getExecutionStatus()).toString();
            } catch (RequestExecutionException e) {
                endTime = System.currentTimeMillis();
                RequestStatus requestStatus = e.getRequestResource().getLocalRequestStatus();
                status = (requestStatus.getExecutionStatus()).toString();
                e.printStackTrace();
            } catch (DAIException e) {
                endTime = System.currentTimeMillis();
                status = e.getMessage();
            } catch (Exception e) {
                endTime = System.currentTimeMillis();
                status = e.toString();
            } finally {
                Measurement measurement = new Measurement(mExperiment);
                measurement.setStartTime(startTime);
                measurement.setEndTime(endTime);
                measurement.setStatus(status);
                try {
                    measurement.save();
                } catch (Exception e) {
                    mLogger.info(Messages.EXCEPTION_OCCURED, e);
                }
            }
        }
    }

    /**
     * Stops execution of this workflow.
     * 
     * @throws SQLException
     */
    public void stopExecution() throws SQLException {
        mKeepRunning = false;
    }
}
