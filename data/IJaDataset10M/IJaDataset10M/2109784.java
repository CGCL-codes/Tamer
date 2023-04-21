package jlibs.xml.sax.dog.expr;

/**
 * @author Santhosh Kumar T
 */
public abstract class Evaluation<X extends Expression> extends EvaluationListener {

    public final X expression;

    public final long order;

    protected Evaluation(X expression, long order) {
        this.expression = expression;
        this.order = order;
    }

    public abstract void start();

    public abstract Object getResult();

    protected EvaluationListener listener;

    public void addListener(EvaluationListener listener) {
        this.listener = listener;
    }

    public void removeListener(EvaluationListener listener) {
        if (this.listener == listener) {
            this.listener = null;
            dispose();
        }
    }

    protected void fireFinished() {
        listener.finished(this);
    }

    protected void dispose() {
    }
}
