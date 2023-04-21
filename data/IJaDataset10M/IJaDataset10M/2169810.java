package org.aspectme.weaver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.MethodVisitor;

public class MethodRecorder {

    protected MethodVisitor mv;

    private List<SingleInvocation> stored = new ArrayList<SingleInvocation>();

    private List<SingleInvocation> skipped;

    private static final int DELEGATE = 1;

    private static final int SKIP = 2;

    private int state = DELEGATE;

    public MethodRecorder(MethodVisitor mv) {
        this.mv = mv;
    }

    /**
	 * Creates a proxy that records all method instructions for later replay.
	 * 
	 * @return A proxy for MethodVisitor.
	 */
    public MethodVisitor getMethodVisitor() {
        return (MethodVisitor) Proxy.newProxyInstance(MethodVisitor.class.getClassLoader(), new Class[] { MethodVisitor.class }, new MethodInvocationProxy());
    }

    public void startSkipping() {
        skipped = new ArrayList<SingleInvocation>();
        state = SKIP;
    }

    public List<SingleInvocation> getSkipped() {
        return skipped;
    }

    public void undoSkipped(MethodVisitor obj) throws IllegalAccessException, InvocationTargetException {
        replay(obj, skipped);
        stored.addAll(skipped);
    }

    public void startDelegating() {
        state = DELEGATE;
    }

    public boolean isSkipping() {
        return state == SKIP;
    }

    /**
	 * Replays all method invocations.
	 */
    public void replay(MethodVisitor obj) {
        replay(obj, stored);
    }

    private void replay(MethodVisitor obj, List<SingleInvocation> invocations) {
        for (SingleInvocation invocation : invocations) {
            invocation.invoke(obj);
        }
    }

    /**
	 * Stores the information about each method invocation.
	 */
    class MethodInvocationProxy implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SingleInvocation invocation = new SingleInvocation(method, args);
            if (state == DELEGATE) {
                stored.add(invocation);
                return method.invoke(mv, args);
            } else if (state == SKIP) {
                skipped.add(invocation);
            }
            return null;
        }
    }

    protected void printSkipped() {
        ParameterExtractorMethodVisitor mv = new ParameterExtractorMethodVisitor();
        System.out.println("SKIPPED:");
        for (SingleInvocation invocation : skipped) {
            mv.invoke(invocation);
        }
        System.out.println(mv);
    }

    protected List<CodeChunk> getParametersFromSkippedInvocations(int numberOfParameters) {
        ParameterExtractorMethodVisitor mv = new ParameterExtractorMethodVisitor();
        for (SingleInvocation invocation : skipped) {
            mv.invoke(invocation);
        }
        return mv.getStack(numberOfParameters);
    }
}
