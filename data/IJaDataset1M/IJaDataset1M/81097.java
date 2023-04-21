package org.tagbox.engine.procedure;

import org.tagbox.engine.Component;
import org.tagbox.engine.Configuration;
import org.tagbox.engine.TagEnvironment;
import org.tagbox.engine.TagBoxException;
import org.tagbox.engine.action.EvaluateAction;
import org.w3c.dom.Element;
import org.tagbox.util.Log;

/**
 * calls a procedure with the parameters passed.
 * the action first tries to find the procedure in the TagEnvironment,
 * if that fails it checks if there is a matching module to load it from.
 */
public class UnloadModulesAction extends EvaluateAction {

    public void init(Component ctxt, Element config) throws TagBoxException {
    }

    public void process(Element e, TagEnvironment env) throws TagBoxException {
        String name = e.getAttribute("name");
        if (name.equals("")) {
            env.getConfiguration().unloadModules();
        } else {
            name = evaluate(name, env, e);
            env.getConfiguration().unloadModule(name);
        }
        e.getParentNode().removeChild(e);
    }
}
