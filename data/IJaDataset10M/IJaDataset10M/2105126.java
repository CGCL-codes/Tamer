package alice.tuplecentre;

/**
 *
 * This class represents rd communication input events of the VM
 *
 *
 * @version 1.0
 */
public class InputEvent_rd extends PendingQueryEvent {

    private TupleTemplate template;

    public InputEvent_rd(long myid, AgentId aid, TupleCentreId tid, TupleTemplate templ) {
        super(myid, aid, tid);
        template = templ;
    }

    public String getOperationName() {
        return "rd";
    }

    public int getOperationNumArguments() {
        return 1;
    }

    public TupleTemplate getTupleTemplate() {
        return template;
    }

    public OutputEvent speak_action(TupleCentreVM vm) {
        Tuple tuple = vm.readMatchingTuple(template);
        if (tuple != null) {
            return new OutputEvent_rd(getId(), getAgentId(), getTupleCentreId(), tuple);
        } else {
            return null;
        }
    }

    public String toString() {
        return "rd(" + template + ")  from agent " + getAgentId();
    }
}
