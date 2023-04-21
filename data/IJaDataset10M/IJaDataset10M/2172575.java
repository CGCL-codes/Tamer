package fildiv.jremcntl.server.core.ext;

import fildiv.jremcntl.common.core.config.Command;
import fildiv.jremcntl.common.util.log.Logger;

public interface DeviceProxy extends Cloneable {

    void init(DeviceConnection cn, Command command, Logger logger);

    void processStdout(String data);

    void processStderr(String data);

    void processErrorOccurred(String cmdLine, Exception e);

    void processTerminated(String cmdLine, int exitCode);

    Object clone() throws CloneNotSupportedException;
}
