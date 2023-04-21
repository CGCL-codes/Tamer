package org.globus.gatekeeper.jobmanager;

import org.globus.gatekeeper.ServiceException;

public class ShellJobManagerService extends JobManagerService {

    public ShellJobManagerService() {
        super(new ShellJobManager());
    }

    public void setArguments(String[] args) throws ServiceException {
        ShellJobManager jobManager = (ShellJobManager) _jobManager;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-type")) {
                jobManager.setType(args[++i]);
            } else if (args[i].equalsIgnoreCase("-libexec")) {
                jobManager.setLibExecDirectory(args[++i]);
            }
        }
    }
}
