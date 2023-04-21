package org.jbehave.core;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.jbehave.core.behaviour.BehaviourClass;
import org.jbehave.core.behaviour.BehaviourVerifier;
import org.jbehave.core.listener.PlainTextListener;
import org.jbehave.core.util.Timer;

/**
 * This is the entry point to run one or more {@link BehaviourClass}.
 * Classes are loaded using the ClassLoader injected, which defaults to the current context
 * ClassLoader.
 * 
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author Mauro Talevi
 */
public class BehaviourRunner {

    private boolean succeeded = true;

    private final PrintWriter writer;

    private ClassLoader classLoader;

    public BehaviourRunner(PrintStream out) {
        this(out, Thread.currentThread().getContextClassLoader());
    }

    public BehaviourRunner(PrintStream out, ClassLoader classLoader) {
        this.writer = new PrintWriter(out);
        this.classLoader = classLoader;
    }

    public boolean succeeded() {
        return succeeded;
    }

    public void verifyBehaviour(String behaviourLocator) throws ClassNotFoundException {
        String className = behaviourLocator;
        String methodName = "";
        int index = behaviourLocator.indexOf(':');
        if (index >= 0) {
            className = behaviourLocator.substring(0, index);
            methodName = behaviourLocator.substring(index + 1);
        }
        verifyBehaviour(classLoader.loadClass(className), methodName);
    }

    public void verifyBehaviour(Class classToVerify) {
        verifyBehaviour(classToVerify, "");
    }

    public void verifyBehaviour(Class classToVerify, String methodName) {
        PlainTextListener textListener = new PlainTextListener(new PrintWriter(writer), new Timer());
        BehaviourVerifier verifier = new BehaviourVerifier(textListener);
        verifier.verifyBehaviour(new BehaviourClass(classToVerify, methodName, verifier));
        textListener.printReport();
        succeeded = succeeded && !textListener.hasBehaviourFailures();
    }

    public static void main(String[] args) {
        try {
            BehaviourRunner run = new BehaviourRunner(System.out);
            for (int i = 0; i < args.length; i++) {
                run.verifyBehaviour(args[i]);
            }
            System.exit(run.succeeded() ? 0 : 1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
