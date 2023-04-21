package fildiv.jremcntl.server.core;

import fildiv.jremcntl.common.core.config.Command;
import fildiv.jremcntl.server.core.env.JRemEnv;
import fildiv.jremcntl.server.core.ext.DeviceProxy;

public class LinuxCmdProcessor extends DefaultCmdProcessor {

    public LinuxCmdProcessor(JRemEnv env, ServerConnection cn, ServerListeners serverListener, Command command, CommandParameters parameters, DeviceProxy dp) {
        super(env, cn, serverListener, command, parameters, dp);
    }

    protected String[] getEnvironmentVars() {
        return null;
    }
}
