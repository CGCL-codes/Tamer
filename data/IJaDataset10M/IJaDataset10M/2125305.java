package alice.tuplecentre;

/**
 *
 * This class represents out communication input events of the VM
 *
 *
 * @version 1.0
 */
public class InputEvent_out extends InputEvent {

    public Tuple tuple;

    public InputEvent_out(long myid, AgentId aid, TupleCentreId tid, Tuple tuple) {
        super(myid, aid, tid);
        this.tuple = tuple;
    }

    public String getOperationName() {
        return "out";
    }

    public Tuple getTuple() {
        return tuple;
    }

    public int getOperationNumArguments() {
        return 1;
    }

    public void listen_action(TupleCentreVM vm) {
        vm.addTuple(tuple);
    }

    public String toString() {
        return "out(" + tuple + ")  from agent " + getAgentId();
    }
}
