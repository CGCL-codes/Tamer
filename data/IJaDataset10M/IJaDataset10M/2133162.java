package jlibs.xml.sax.dog.expr.nodset;

import jlibs.core.lang.ImpossibleException;
import jlibs.xml.sax.dog.DataType;
import jlibs.xml.sax.dog.Scope;
import jlibs.xml.sax.dog.expr.Evaluation;
import jlibs.xml.sax.dog.expr.Expression;
import jlibs.xml.sax.dog.sniff.Event;

/**
 * @author Santhosh Kumar T
 */
public abstract class Positional extends Expression {

    public final boolean position;

    public Expression predicate;

    public Positional(boolean position) {
        this(DataType.NUMBER, position);
    }

    public Positional(DataType resultType, boolean position) {
        super(Scope.LOCAL, resultType);
        this.position = position;
    }

    @Override
    public final Object getResult() {
        throw new ImpossibleException();
    }

    @Override
    public Object getResult(Event event) {
        return new PositionalEvaluation(this, event.order(), event.positionTrackerStack.peekFirst());
    }

    protected Object translate(Double result) {
        return result;
    }
}

final class PositionalEvaluation extends Evaluation<Positional> {

    public PositionalEvaluation previous, next;

    private PositionTracker locationEval;

    protected PositionalEvaluation(Positional expression, long order, PositionTracker locationEval) {
        super(expression, order);
        this.locationEval = locationEval;
    }

    @Override
    public void start() {
        if (expression.position) locationEval.addPositionListener(this); else locationEval.addLastLitener(this);
    }

    private Object result;

    @Override
    public Object getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = expression.translate(result);
        fireFinished();
    }

    @Override
    public void finished(Evaluation evaluation) {
    }

    @Override
    protected void dispose() {
        if (expression.position) locationEval.removePositionListener(this); else locationEval.removeLastLitener(this);
    }
}
