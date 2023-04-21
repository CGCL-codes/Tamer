package sketch;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sketch.check.Check;
import sketch.util.Checker;

public class MethodExecOutcome {

    /**
	 * The result object. It is null for a void method
	 * */
    public Object outcome = null;

    /**
	 * The return type
	 * */
    public Class<?> retType = null;

    /**
	 * The thrown exception
	 * */
    public Throwable exception = null;

    /**
	 * a list of randoop style checks, such as checking the basic contract in Java programs
	 * */
    public List<Check> checks = new LinkedList<Check>();

    /**
	 * a set of expected (allowable) exceptions
	 * */
    private Set<Class<?>> expected_exceptions = new LinkedHashSet<Class<?>>();

    /**
	 * A place holder constructor
	 * */
    public MethodExecOutcome() {
    }

    /**
	 * 
	 * */
    public void setOutcomeValue(Object outcome) {
        this.outcome = outcome;
    }

    public void setOutcomeValue(Class<?> retType, Object outcome) {
        Checker.checkNull(retType, "The return type could not be null.");
        if (retType == void.class) {
            Checker.checkTrue(outcome == null, "no return value for void method.");
        }
        Checker.checkTrue(this.outcome == null && this.retType == null, "Both outcome " + "and return type could only been set once");
        this.retType = retType;
        this.outcome = outcome;
    }

    public void setThrownException(Throwable exception) {
        Checker.checkNull(exception, "The throwable exception could not be null!");
        Checker.checkTrue(this.exception == null, "The exception field could only be set once!");
        this.exception = exception;
    }

    public void setExpectedExceptions(Class<?>[] exceptionClasses) {
        Checker.checkNull(exceptionClasses, "Class argument could not be null!");
        for (Class<?> clazz : exceptionClasses) {
            Checker.checkNull(clazz, "The exception class could not be null!");
            this.expected_exceptions.add(clazz);
        }
    }
}
