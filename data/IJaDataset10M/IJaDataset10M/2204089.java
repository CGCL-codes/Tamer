package org.nakedobjects.viewer.nuthatch.action;

import org.nakedobjects.viewer.nuthatch.Command;
import org.nakedobjects.viewer.nuthatch.ContextTree;
import org.nakedobjects.viewer.nuthatch.Dispatcher;
import org.nakedobjects.viewer.nuthatch.View;

public class ReviewActionDispatcher implements Dispatcher {

    public void execute(Command command, ContextTree contextTree, View view) {
        ActionContext action = (ActionContext) contextTree.getContext(ActionContext.class);
        action.review(view);
    }

    public String getHelp() {
        return "Show the current state of an action invocation";
    }

    public String getNames() {
        return "review";
    }

    public boolean isAvailable(ContextTree contextTree) {
        return contextTree.containsContext(ActionContext.class);
    }
}
