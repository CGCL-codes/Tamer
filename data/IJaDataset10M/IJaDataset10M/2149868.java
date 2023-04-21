package pikes.ecma;

public class Assignment extends PrintableExpression implements IAssignmentExpression {

    private ILeftHandSideExpression leftHandSide = null;

    private IAssignmentExpression rightHandSide = null;

    public Assignment(final ILeftHandSideExpression leftHandSide, final IAssignmentExpression rightHandSide) {
        super();
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public Assignment(final CharSequence leftHandSide, final CharSequence rightHandSide) {
        this.leftHandSide = new Identifier(leftHandSide);
        this.rightHandSide = new Identifier(rightHandSide);
    }

    public Assignment(final ILeftHandSideExpression leftHandSide, final CharSequence rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = new Identifier(rightHandSide);
    }

    public void visit(final SyntaxTreeVisitor syntaxTreeVisitor) throws EcmaSyntaxVisitingException {
        leftHandSide.visit(syntaxTreeVisitor);
        syntaxTreeVisitor.equalSign();
        rightHandSide.visit(syntaxTreeVisitor);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Assignment) {
            Assignment thatAssignment = (Assignment) obj;
            return (this.leftHandSide.equals(thatAssignment.leftHandSide) && (this.rightHandSide.equals(thatAssignment.rightHandSide)));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return leftHandSide.hashCode() + rightHandSide.hashCode();
    }
}
