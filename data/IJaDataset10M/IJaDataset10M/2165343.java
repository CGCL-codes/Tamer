package star.trek.voyager.module.benz.engine_v8;

import org.jiopi.framework.ControlPanel;
import org.jiopi.framework.annotation.module.Instantiation;
import org.jiopi.framework.annotation.module.RegisterModule;
import org.jiopi.framework.annotation.module.SocketControlPanel;
import star.trek.voyager.blueprint.engine.Engine;
import star.trek.voyager.blueprint.engine.StatusOutput;
import star.trek.voyager.blueprint.engine_socket.EngineSocket;
import star.trek.voyager.blueprint.status.Status;

@RegisterModule
@Instantiation(initMethod = "init")
public class EngineV8 implements Engine {

    @SocketControlPanel(module = "voyager.pojo.status_checker", controlpanel = "star.trek.voyager.module.pojo.status_checker.StatusChecker")
    private ControlPanel statusChecker;

    private StatusOutput statusOutput = null;

    private EngineSocket engineSocket = null;

    private EngineSocket tempEngineSocket = null;

    public void init() {
        if (statusChecker != null) statusChecker.operate("init", void.class);
        engineSocket = tempEngineSocket;
        tempEngineSocket = null;
    }

    public void flameout() {
        if (statusOutput != null) statusOutput.output(null);
    }

    public void retrofire() {
        if (statusOutput != null) statusOutput.output("benz engine v8 started");
    }

    public void socket(EngineSocket engineSocket) {
        this.tempEngineSocket = engineSocket;
    }

    public void socket(StatusOutput statusOutput) {
        this.statusOutput = statusOutput;
    }

    public void socket(Status statusAbleModule) {
        if (statusChecker != null) statusChecker.operate("addModule", void.class, statusAbleModule); else System.out.println(statusAbleModule.checkStatus() + "\n-----");
    }

    public String checkStatus() {
        if (engineSocket != null && statusChecker != null) return "====EngineV8 status : ok====\n" + statusChecker.operate("checkStatus", String.class) + " ====over Engine V8====\n"; else if (engineSocket != null) return "EngineV8 status : statusChecker not exist";
        return "EngineV8 status : EngineSocket not Connect!\n\n";
    }

    public String toString() {
        return this.getClass().getName() + "-0.1.0.0";
    }
}
