package astcentric.structure.basic;

/**
 * Event which occurs after name or properties of an AST have been changed.
 */
public class ASTChangedEvent extends ASTEvent {

    ASTChangedEvent(AST source) {
        super(source);
    }
}
