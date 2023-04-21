package com.alphacsp.cit.exec;

/**
 * @author Yoav Hakman
 */
public class DefaultProcessLauncher extends AbstractProcessLauncher {

    public Process exec() {
        return exec(getEnvironment(), getCommands());
    }
}
