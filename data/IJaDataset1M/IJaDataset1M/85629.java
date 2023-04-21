package org.jcvi.command.grid;

import org.ggf.drmaa.JobInfo;

/**
 * Created by IntelliJ IDEA.
 * User: aresnick
 * Date: Aug 10, 2010
 * Time: 11:47:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BatchGridJob extends GridJob {

    String getJobID();

    JobInfo getJobInfo();
}
