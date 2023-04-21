package alice.tuplecentre.core;

/**
 * Represents output events of the tuple centre virtual machine
 *
 *
 * @author aricci
 */
public class OutputEvent extends Event {

    private InputEvent inputEvent;

    private boolean isLinking;

    public OutputEvent(InputEvent ev) {
        super(ev.getId(), ev.getOperation(), ev.getReactingTC(), ev.getTime());
        inputEvent = ev;
        isLinking = ev.isLinking();
    }

    public void setIsLinking(boolean flag) {
        isLinking = flag;
    }

    public boolean isLinking() {
        return isLinking;
    }

    public InputEvent getInputEvent() {
        return inputEvent;
    }

    public boolean isInput() {
        return false;
    }

    public boolean isOutput() {
        return true;
    }

    public boolean isInternal() {
        return false;
    }

    public String toString() {
        return "output_event(agentId(" + getId() + "),operation(" + (this.getOperation().isResultDefined() ? getOperation().getResult() : getOperation()) + "))";
    }
}
