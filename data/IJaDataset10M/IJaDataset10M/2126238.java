package org.candango.myfuses.core.verbs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.candango.myfuses.core.AbstractVerb;
import org.candango.myfuses.core.CircuitAction;
import org.candango.myfuses.core.Verb;
import org.candango.myfuses.util.file.MyFusesFileHandler;

public class DoVerb extends AbstractVerb {

    private String actionName;

    public void setParameters(HashMap<String, String> parameters) {
        if (parameters.containsKey("action")) {
            setActionName(parameters.get("action"));
        }
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Verb verb : getAction().getCircuit().getAction(getActionName()).getVerbs()) {
            verb.doAction(req, resp);
        }
    }
}
