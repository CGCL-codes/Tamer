package net.sourceforge.javautil.job;

import java.util.Map;

/**
 * The basic contract for job definitions in this framework.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface Job {

    /**
	 * @return The name of the group this job belongs to, or null if it is in the global group
	 */
    String getGroup();

    /**
	 * @return The name of the job
	 */
    String getName();

    /**
	 * @return Meta data related to this job
	 */
    Map<String, Object> getMetaData();

    /**
	 * @return A schedule that will determine when the job should be executed
	 */
    JobSchedule getSchedule();

    /**
	 * @param metaData Per instance meta data, can override default {@link #getMetaData()} settings, may be null
	 * @return An executable instance of this job definition
	 */
    JobInstance createInstance(Map<String, Object> metaData);
}
