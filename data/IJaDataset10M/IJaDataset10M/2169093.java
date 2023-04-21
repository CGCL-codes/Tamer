package swarm.activity;

import swarm.Selector;

public class ActionImpl {

    private int startingIndex;

    private boolean active;

    private Selector selector;

    private Object target;

    private Object[] params;

    private boolean hasParams;

    public void execute() {
        if (active) {
            selector.execute(target, params);
        }
    }
}
