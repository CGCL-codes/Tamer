package annone.engine.local.builder.nodes;

public class CallEventNode extends ExpressionNode {

    private long eventId;

    private ExpressionListNode arguments;

    public CallEventNode() {
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long methodId) {
        this.eventId = methodId;
    }

    public ExpressionListNode getArguments() {
        return arguments;
    }

    public void setArguments(ExpressionListNode argument) {
        this.arguments = argument;
        setChildren(argument);
    }
}
