package net.infordata.ifw2.web.ctrl;

/**
 * Common {@link IFlowEndState}. 
 * @author valentino.proietti
 */
public class FlowEndState extends FlowState implements IFlowEndState {

    private static final long serialVersionUID = 1L;

    public FlowEndState(String id) {
        super(id);
        if (this instanceof IFlowRequestProcessor) throw new IllegalStateException("An end state cannot process requests");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FlowEndState)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
