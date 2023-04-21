package spacewalklib.jbpm;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import spacewalklib.RhnConn;
import spacewalklib.RhnScriptEvent;

public class RhnJbpmIsScriptRun implements ActionHandler {

    public void execute(ExecutionContext executionContext) throws Exception {
        String satellite = (String) executionContext.getVariable("satellite_url");
        String user = (String) executionContext.getVariable("User");
        String password = (String) executionContext.getVariable("Password");
        String system = (String) executionContext.getVariable("System");
        Integer eventid = (Integer) executionContext.getVariable("script_number");
        RhnConn rhnconn = new RhnConn(satellite, user, password);
        RhnScriptEvent script = new RhnScriptEvent(eventid, rhnconn);
        if (script.isRun()) {
            executionContext.setVariable("isScriptRun", "true");
        } else {
            executionContext.setVariable("isScriptRun", "false");
        }
        executionContext.getNode().leave(executionContext);
    }
}
