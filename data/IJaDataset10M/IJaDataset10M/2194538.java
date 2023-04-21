package org.jmlspecs.jml4.fspv.theory;

/**
 * @author karabot
 *
 */
public class TheoryExpression {

    public static final TheoryExpression[] EMPTY = new TheoryExpression[0];

    public boolean withSideEffects = false;

    public final TheoryType type;

    public TheoryExpression(TheoryType type) {
        this.type = type;
    }

    public Object visit(TheoryVisitor visitor) {
        return visitor.accept(this);
    }
}
