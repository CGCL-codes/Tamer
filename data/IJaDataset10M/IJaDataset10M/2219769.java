package net.sourceforge.javabits.function;

/**
 * @author Jochen Kuhnle
 */
public class EvaluationException extends RuntimeException {

    private Object param;

    public Object getParam() {
        return this.param;
    }

    public EvaluationException(Object param) {
        this.param = param;
    }

    public EvaluationException(Throwable cause, Object param) {
        super(cause);
        this.param = param;
    }
}
