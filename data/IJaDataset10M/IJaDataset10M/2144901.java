package model.action;

public class Transaction extends Action {

    private IHistory actions = new History();

    public void add(IAction action) {
        actions.add(action);
    }

    public boolean execute() {
        if (isStarted() || (isUndone())) {
            boolean executed = actions.executeAll();
            if (executed) {
                setStatus("executed");
            } else {
                setStatus("error");
                actions.undoAll();
            }
            return executed;
        } else {
            throw new RuntimeException("A transaction must be either started or undone to be executed.");
        }
    }

    public boolean undo() {
        if (isExecuted()) {
            boolean undone = actions.undoAll();
            if (undone) {
                setStatus("undone");
            } else {
                setStatus("error");
                actions.executeAll();
            }
            return undone;
        } else {
            throw new RuntimeException("A transaction must be executed to be undone.");
        }
    }

    public boolean redo() {
        if (isUndone()) {
            return execute();
        } else {
            throw new RuntimeException("A transaction must be undone to be re-executed.");
        }
    }
}
