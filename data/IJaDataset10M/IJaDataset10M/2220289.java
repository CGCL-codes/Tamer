package com.griddynamics.convergence.demo.utils.exec;

import java.io.IOException;

public interface ProcessExecutor {

    public Process execute(ExecCommand command) throws IOException;
}
