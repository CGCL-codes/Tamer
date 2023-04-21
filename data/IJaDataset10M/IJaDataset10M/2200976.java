package com.intel.gpe.tsi.common;

import java.io.Serializable;

/**
 * @author dnpetrov
 * @version $Id.$
 */
public class Process implements Serializable {

    private static final long serialVersionUID = -6638712576252518107L;

    private String uuid;

    private String stdinName, stdoutName, stderrName, workDir;

    public Process(String uuid, String stdinName, String stdoutName, String stderrName, String workDir) {
        super();
        this.uuid = uuid;
        this.stdinName = stdinName;
        this.stdoutName = stdoutName;
        this.stderrName = stderrName;
        this.workDir = workDir;
    }

    public String getStderrName() {
        return stderrName;
    }

    public void setStderrName(String stderrName) {
        this.stderrName = stderrName;
    }

    public String getStdinName() {
        return stdinName;
    }

    public void setStdinName(String stdinName) {
        this.stdinName = stdinName;
    }

    public String getStdoutName() {
        return stdoutName;
    }

    public void setStdoutName(String stdoutName) {
        this.stdoutName = stdoutName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }
}
