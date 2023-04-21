package org.ourgrid.worker.ui.async.servicesetup;

import org.ourgrid.common.ui.servicesetup.AbstractUnixServiceSetupStrategy;

/**
 *
 */
public class WorkerUnixServiceSetupStrategy extends AbstractUnixServiceSetupStrategy {

    @Override
    protected String getServiceHomeToken() {
        return "WORKER_HOME";
    }

    @Override
    protected String getServiceName() {
        return "ourgrid-worker";
    }

    @Override
    protected String getTemplateFilePath() {
        return SCRIPTS_PATH + "workerservice-template.sh";
    }
}
