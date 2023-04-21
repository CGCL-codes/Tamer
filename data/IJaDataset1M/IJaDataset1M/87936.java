package it.ilz.hostingjava.scheduler;

import it.ilz.hostingjava.listener.Context;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * This is a <i>job</i> that collects all applications names inside
 * a desired folder (hopefully: webapps) and creates a simple
 * robot.txt file google compliant.
 * </p>
 *
 * @author Tommaso Mazza
 * @since october 2006
 * @version 1.0
 */
public class MakeRobotFileJob implements Job {

    private Log logger = LogFactory.getLog(MakeRobotFileJob.class);

    private String WEBAPPS_FOLDER = null;

    static StringBuffer sb;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        computeRobot();
    }

    public void computeRobot() {
        try {
            StringBuffer sb = new StringBuffer();
            File[] app = (new File(WEBAPPS_FOLDER)).listFiles();
            if (app != null) for (File f : app) {
                if (f.getName().startsWith("-")) {
                    logger.debug("Adding application " + f.getName());
                    sb.append("http://www.hostingjava.it/" + f.getName().substring(0, f.getName().length() - ".xml".length()) + "/\n");
                } else {
                    logger.debug("Skipping application " + f.getName());
                }
            }
            MakeRobotFileJob.sb = sb;
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
