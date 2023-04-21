package org.icenigrid.gridsam.integration.common;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.icenigrid.gridsam.core.ConfigurationException;
import org.icenigrid.gridsam.core.JobInstance;
import org.icenigrid.gridsam.core.JobInstanceChangeListener;
import org.icenigrid.gridsam.core.JobManager;
import org.icenigrid.gridsam.core.JobManagerException;
import org.icenigrid.gridsam.core.JobState;
import org.icenigrid.gridsam.core.SubmissionException;
import org.icenigrid.gridsam.core.UnknownJobException;
import org.icenigrid.gridsam.core.UnsupportedFeatureException;
import org.icenigrid.gridsam.core.plugin.connector.data.VFSSupport;
import org.icenigrid.gridsam.core.plugin.manager.DefaultJobManager;
import EDU.oswego.cs.dl.util.concurrent.Latch;

/**
 * MultiStageJobSubmissionTestCase testcase
 */
public class MultiStageJobSubmissionTestCase extends AbstractJobManagerTestCase {

    /**
     * logger
     */
    private static Log sLog = LogFactory.getLog(MultiStageJobSubmissionTestCase.class);

    /**
     * create a new AbstractJobSubmissionTestCase
     *
     * @param s
     */
    public MultiStageJobSubmissionTestCase(String s) {
        super(s);
    }

    public void testRepeatedStage() {
        long xMaxWait = System.getProperty("acceptable.wait") == null ? 1000 * 60 : Long.parseLong(System.getProperty("acceptable.wait"));
        JobInstance xInstance = assertSuccessfulSubmission(getJSDLString(System.getProperty("basedir") + "/data/examples/sleep.jsdl"), xMaxWait);
        sLog.info("finished job instance = " + xInstance);
        assertEquals("expected number of execution", new Integer(6), xInstance.getProperties().get("execute.count"));
    }

    /**
     * get the jobmanager instance for testing
     *
     * @return JobManager instance
     */
    protected JobManager createJobManager() throws ConfigurationException {
        return new DefaultJobManager(new String[] { "org/icenigrid/gridsam/resource/config/common.xml", "org/icenigrid/gridsam/resource/config/embedded.xml", "org/icenigrid/gridsam/integration/common/config.xml" });
    }
}
