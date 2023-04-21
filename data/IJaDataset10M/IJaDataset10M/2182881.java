package org.jcvi.primerDesign;

import org.ggf.drmaa.DrmaaException;

/**
 * Created by IntelliJ IDEA.
 * User: aresnick
 * Date: Jul 28, 2010
 * Time: 5:25:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PrimerDesignerExecutorService {

    void startJobs() throws DrmaaException;

    void waitForCompletion();

    boolean isFinished();
}
