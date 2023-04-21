package uk.co.hellfresh.expression.functions;

import java.util.Stack;
import java.util.HashMap;
import uk.co.hellfresh.expression.Messages;

/**
 * @author Niall Jackson
 *
 */
public class Sine implements Function {

    private HashMap<String, Object> environment;

    public Sine(HashMap<String, Object> env) {
        environment = env;
    }

    public String getAssocToken() {
        return "sin";
    }

    public int expectedNumOfArguments() {
        return 1;
    }

    public boolean operate(Stack<Object> argumentStack) throws IllegalArgumentException {
        Object arg = argumentStack.pop();
        if (arg instanceof String) {
            double theta = Double.valueOf((String) arg);
            if (!(Boolean) environment.get("radians")) {
                theta = theta * Math.PI / 180;
            }
            double ans = Math.sin(theta);
            argumentStack.push(Double.toString(ans));
        } else {
            throw new IllegalArgumentException(Messages.getString("unsuitable_arguments") + " sine: " + argumentStack);
        }
        return true;
    }

    public Function getInverse() {
        return new Arcsine(environment);
    }
}
