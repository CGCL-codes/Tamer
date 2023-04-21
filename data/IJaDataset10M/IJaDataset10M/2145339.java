package joc.internal;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class MethodCallSequence {

    private List<MethodCall> methodCalls = new ArrayList<MethodCall>();

    private List<MethodCall> allowAnywhere = new ArrayList<MethodCall>();

    private int currentCall = 0;

    private Logger logger = Logger.getLogger(getClass());

    public MethodCallSequence(MethodCall firstMethodCall) {
        methodCalls.add(firstMethodCall);
    }

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public List<MethodCall> getAllowAnywhere() {
        return allowAnywhere;
    }

    public void addCall(MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    public void addAllowAnywhere(MethodCall methodCall) {
        allowAnywhere.add(methodCall);
    }

    public boolean beginsWith(MethodCall firstMethodCall) {
        return firstMethodCall.equals(methodCalls.get(0));
    }

    public boolean verifyCall(MethodCall methodCall) {
        if (currentCall >= methodCalls.size()) {
            logger.debug("sequence exceeds maximum amount of calls, returning false");
            return false;
        }
        if (allowAnywhere.contains(methodCall)) {
            logger.debug("sequence contains allowAnywhere " + methodCall.getMethodName() + ", returning true");
            return true;
        }
        if (!methodCall.equals(methodCalls.get(currentCall))) {
            logger.debug("sequence expected " + methodCalls.get(currentCall).getMethodName() + " but got " + methodCall.getMethodName() + ", returning false");
            return false;
        }
        logger.debug("sequence got what it expected, returning true");
        currentCall++;
        return true;
    }

    public boolean isOnLastCall() {
        return currentCall >= methodCalls.size();
    }
}
