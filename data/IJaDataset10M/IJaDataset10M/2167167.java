package org.ps.seishin.cluster;

public class NodeState implements Comparable<NodeState> {

    public enum State {

        ONLINE, MAINTENANCE, OFFLINE
    }

    ;

    private State st;

    public NodeState(State state) {
        this.st = state;
    }

    public void setState(State state) {
        this.st = state;
    }

    public State getState() {
        return this.st;
    }

    public int compareTo(NodeState other) {
        if (other.getState() == this.st) {
            return 0;
        }
        if (this.st.ordinal() > other.getState().ordinal()) {
            return -1;
        } else {
            return 1;
        }
    }
}
