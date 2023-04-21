package net.sourceforge.javabits.error;

/**
 * @author Jochen Kuhnle
 */
public class RuntimeExceptionErrorHandler extends AbstractErrorHandler {

    private final Severity threshold;

    public RuntimeExceptionErrorHandler(Severity threshold) {
        this.threshold = threshold;
    }

    @Override
    protected Object handleProblem(Severity severity, Problem problem) {
        if (severity.ordinal() >= threshold.ordinal()) {
            String text = problem.toString();
            throw new RuntimeException(text, problem.getCause());
        }
        return getDefaultValue();
    }
}
