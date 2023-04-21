package org.nakedobjects.viewer.cli.context;

import org.nakedobjects.viewer.cli.Agent;
import org.nakedobjects.viewer.cli.Context;
import org.nakedobjects.viewer.cli.classes.ClassesAgent;
import java.util.Vector;

public class ContextManager {

    private final Vector contextStack = new Vector();

    private final ClassesAgent resourcesContext;

    public ContextManager(final ClassesAgent resourcesContext) {
        this.resourcesContext = resourcesContext;
        newContext((Context) null);
    }

    public void newContext() {
        newContext(current());
    }

    public void newContext(Agent agent) {
        newContext();
        current().addObject(agent);
    }

    private void newContext(Context parent) {
        Context context = new Context(parent, resourcesContext);
        contextStack.addElement(context);
    }

    public boolean canRemoveContext() {
        return contextStack.size() > 1;
    }

    public Context current() {
        return (Context) contextStack.lastElement();
    }

    public void removeContext() {
        contextStack.removeElementAt(contextStack.size() - 1);
    }
}
