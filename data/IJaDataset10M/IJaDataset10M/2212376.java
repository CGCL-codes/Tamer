package org.jbehave.core.behaviour;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.jbehave.core.exception.JBehaveFrameworkError;
import org.jbehave.core.listener.BehaviourListener;
import org.jbehave.core.mock.Matcher;

/**
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 */
public class BehaviourClass implements Behaviour {

    private static final String BEHAVIOUR_METHOD_PREFIX = "should";

    private final Class classToVerify;

    private final BehaviourVerifier verifier;

    private Matcher methodFilter;

    public static final Matcher ALL_METHODS = new Matcher() {

        public boolean matches(Object arg) {
            return true;
        }
    };

    public static final Matcher BEHAVIOUR_METHODS = new Matcher() {

        public boolean matches(Object arg) {
            return ((Method) arg).getName().startsWith(BEHAVIOUR_METHOD_PREFIX);
        }
    };

    public BehaviourClass(Class classToVerify) {
        this(classToVerify, new BehaviourVerifier());
    }

    public BehaviourClass(Class classToVerify, BehaviourVerifier verifier) {
        this(classToVerify, "", verifier);
    }

    public BehaviourClass(Class classToVerify, final String methodName, BehaviourVerifier verifier) {
        this.classToVerify = classToVerify;
        this.verifier = verifier;
        if (methodName.length() == 0) {
            this.methodFilter = ALL_METHODS;
        } else {
            this.methodFilter = matchMethodName(methodName);
        }
    }

    private Matcher matchMethodName(final String methodName) {
        return new Matcher() {

            public boolean matches(Object arg) {
                return ((Method) arg).getName().equals(methodName);
            }
        };
    }

    public void verifyTo(BehaviourListener listener) {
        traverseMethodsWith(new MethodVerifier(verifier, listener));
    }

    public int countBehaviours() {
        MethodCounter counter = new MethodCounter();
        traverseMethodsWith(counter);
        return counter.total();
    }

    public BehaviourMethod[] getBehaviourMethods() {
        Set set = new HashSet();
        Method[] methods = getMethods(BEHAVIOUR_METHODS);
        for (int i = 0; i < methods.length; i++) {
            set.add(createBehaviourMethod(methods[i]));
        }
        return (BehaviourMethod[]) set.toArray(new BehaviourMethod[set.size()]);
    }

    public BehaviourMethod createBehaviourMethod(Method method) {
        return new BehaviourMethod(createInstance(), method);
    }

    private Method[] getMethods(Matcher methodFilter) {
        Set set = new HashSet();
        Method[] classMethods = classToVerify.getMethods();
        for (int i = 0; i < classMethods.length; i++) {
            Method method = classMethods[i];
            if (methodFilter.matches(method)) {
                set.add(method);
            }
        }
        return (Method[]) set.toArray(new Method[set.size()]);
    }

    private void traverseMethodsWith(MethodHandler methodHandler) {
        if (Behaviours.class.isAssignableFrom(classToVerify)) {
            Behaviours behaviours = (Behaviours) createInstance();
            Class[] nestedClasses = behaviours.getBehaviours();
            for (int i = 0; i < nestedClasses.length; i++) {
                methodHandler.handleClass(new BehaviourClass(nestedClasses[i], verifier));
            }
        }
        Method[] methods = getMethods(BEHAVIOUR_METHODS);
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (methodFilter.matches(method)) {
                methodHandler.handleMethod(createBehaviourMethod(method));
            }
        }
    }

    private Object createInstance() {
        try {
            return classToVerify.newInstance();
        } catch (Exception e) {
            throw new JBehaveFrameworkError("Unable to create instance of " + classToVerify.getName(), e);
        }
    }

    public String toString() {
        return "BehaviourClass: " + classToVerify.getName();
    }

    /**
     * @deprecated
     */
    public Class classToVerify() {
        return classToVerify;
    }

    private static class MethodCounter implements MethodHandler {

        int total = 0;

        public void handleClass(BehaviourClass behaviourClass) {
            total += behaviourClass.countBehaviours();
        }

        public void handleMethod(BehaviourMethod behaviourMethod) {
            total++;
        }

        public int total() {
            return total;
        }
    }
}
