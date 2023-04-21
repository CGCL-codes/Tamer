package ru.nosport.matrixaria.modules.user.test;

import ru.nosport.matrixaria.modules.common.AppServerProcess;
import org.apache.commons.configuration.Configuration;

/**
 * User: vfabr
 * Date: 20.09.2006
 * Time: 16:10:26
 */
public class StubAppServerProcess1 implements AppServerProcess {

    private boolean isRunning;

    private Configuration conf;

    public StubAppServerProcess1(Configuration _conf) {
        this.conf = _conf;
    }

    public StubAppServerProcess1() {
        this.isRunning = false;
    }

    public boolean moduleStart() {
        this.isRunning = true;
        return this.isRunning;
    }

    public boolean moduleStop() {
        this.isRunning = false;
        return !this.isRunning;
    }

    public boolean isModuleRunning() {
        return this.isRunning;
    }

    public String moduleStat() {
        return "Running: " + this.isRunning;
    }

    public String getMyName() {
        return "I'am StubAppServerProcess1";
    }

    public String runAct() {
        return "I'am runing";
    }
}
