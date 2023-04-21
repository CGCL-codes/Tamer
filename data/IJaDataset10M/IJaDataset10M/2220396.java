package org.nakedobjects.viewer.nuthatch;

public class ShowKnownObjectsDispatcher implements Dispatcher {

    public void execute(Command command, ContextTree contextTree, View view) {
        contextTree.objects(view);
    }

    public String getHelp() {
        return "Show all the previously used objects";
    }

    public String getNames() {
        return "objects all";
    }

    public boolean isAvailable(ContextTree contextTree) {
        return true;
    }

    public boolean isValid(Command command, ContextTree contextTree) {
        return true;
    }
}
