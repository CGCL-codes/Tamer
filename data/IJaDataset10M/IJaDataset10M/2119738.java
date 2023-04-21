package astcentric.structure.basic;

/**
 * Event which occurs after an AST in the role of an 
 * {@link ASTListener} has received an event from an AST of one of the
 * references of its nodes.  
 */
public final class ASTEventInReference extends ASTEvent {

    private final ASTEvent _event;

    ASTEventInReference(AST source, ASTEvent event) {
        super(source);
        if (event == null) {
            throw new IllegalArgumentException("Undefined event.");
        }
        _event = event;
    }

    /**
   * Returns the event which occurred in an AST referred by the
   * source AST.
   * 
   * @return never <code>null</code>.
   */
    public ASTEvent getEventInReference() {
        return _event;
    }

    @Override
    public int getChainLength() {
        ASTEvent event = this;
        int length = 0;
        while (event instanceof ASTEventInReference) {
            ++length;
            event = ((ASTEventInReference) event).getEventInReference();
        }
        return length;
    }

    @Override
    public ASTEvent getOriginalEvent() {
        ASTEvent event = this;
        while (event instanceof ASTEventInReference) {
            event = ((ASTEventInReference) event).getEventInReference();
        }
        return event;
    }
}
