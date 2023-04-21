package edu.mit.csail.pag.smock;

import org.jmock.api.Invocation;
import org.jmock.internal.CaptureControl;
import org.jmock.internal.ExpectationCapture;
import edu.mit.csail.pag.amock.util.ClassName;
import java.util.*;

/**
 * Must be interned to work!
 */
public class CapturingClass implements CaptureControl {

    private static final Map<Class<?>, CapturingClass> CACHE = new HashMap<Class<?>, CapturingClass>();

    private final Class<?> cls;

    private ExpectationCapture builder = null;

    private CapturingClass(Class<?> cls) {
        this.cls = cls;
    }

    public static CapturingClass getCapturingClass(Class<?> c) {
        if (!CACHE.containsKey(c)) {
            CACHE.put(c, new CapturingClass(c));
        }
        return CACHE.get(c);
    }

    public void startCapturingExpectations(ExpectationCapture capture) {
        this.builder = capture;
    }

    public void stopCapturingExpectations() {
        this.builder = null;
    }

    public boolean isCapturingExpectations() {
        return this.builder != null;
    }

    public void recordInvocation(Invocation invocation) {
        builder.createExpectationFrom(invocation);
    }

    @Override
    public String toString() {
        return cls.toString();
    }
}
