package org.coinjema.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.InjectorNameResolver;

/**
 * 
 * @author Michael Stover (mstover1@apache.org)
 *
 * @param <R>
 */
public class DependencyFunctor<R> extends Functor {

    String injectedLabel;

    String methodLabel;

    String injectedClassLabel;

    String alias;

    boolean hasDefault = false, isAliased;

    InjectorNameResolver nameResolver;

    public String getInjectedLabel() {
        return injectedLabel;
    }

    public InjectorNameResolver getNameResolver() {
        return nameResolver;
    }

    public String getMethodLabel() {
        return methodLabel;
    }

    public Class getInjectedClass() {
        return this.types[0];
    }

    public boolean hasDefault() {
        return hasDefault;
    }

    public String getAliasLabel() {
        return alias;
    }

    public boolean isAliased() {
        return isAliased;
    }

    /**
     * @param methodName
     * @param types
     */
    public DependencyFunctor(Class objClass, Method method, CoinjemaDependency depAnn) {
        super(method.getName(), (Class[]) method.getParameterTypes());
        if (types == null || types.length != 1) throw new RuntimeException("Dependency Injector method must have 1 parameter");
        determineInjectedLabel(depAnn, types);
        determineMethodLabel(methodName, depAnn);
        determineAliasLabel(depAnn);
        hasDefault = depAnn.hasDefault();
        nameResolver = new InjectorNameResolver(objClass, this);
    }

    public DependencyFunctor(Class objClass, Method method) {
        super(method.getName(), (Class[]) method.getParameterTypes());
        if (types == null || types.length != 1) throw new RuntimeException("Dependency Injector method must have 1 parameter");
        determineInjectedLabel(null, types);
        determineMethodLabel(methodName, null);
        determineAliasLabel(null);
        hasDefault = true;
        nameResolver = new InjectorNameResolver(objClass, this);
    }

    /**
     * @param methodName
     * @param depAnn
     */
    private void determineMethodLabel(String methodName, CoinjemaDependency depAnn) {
        if (depAnn != null && !depAnn.method().equals("")) {
            methodLabel = depAnn.method();
        } else {
            methodLabel = methodName;
        }
    }

    private void determineAliasLabel(CoinjemaDependency depAnn) {
        if (depAnn != null && !(depAnn.alias().length() == 0)) {
            alias = depAnn.alias();
            isAliased = true;
        } else isAliased = false;
    }

    /**
     * @param depAnn
     * @param types
     */
    private void determineInjectedLabel(CoinjemaDependency depAnn, Class... types) {
        if (depAnn != null && !depAnn.type().equals("")) {
            injectedLabel = depAnn.type();
        } else {
            injectedLabel = types[0].getSimpleName();
        }
    }

    @Override
    public R invoke(Object invokee, Object... args) {
        try {
            if (methodToInvoke == null) {
                methodToInvoke = createMethod(invokee, types);
            }
            return (R) methodToInvoke.invoke(invokee, args);
        } catch (Exception e) {
            throw new RuntimeException("Trying to invoke method: " + methodToInvoke.getName() + " on object: " + invokee + " with arguments " + Arrays.asList(args), e);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((alias == null) ? 0 : alias.hashCode());
        result = PRIME * result + ((injectedClassLabel == null) ? 0 : injectedClassLabel.hashCode());
        result = PRIME * result + ((injectedLabel == null) ? 0 : injectedLabel.hashCode());
        result = PRIME * result + (isAliased ? 1231 : 1237);
        result = PRIME * result + ((methodLabel == null) ? 0 : methodLabel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DependencyFunctor other = (DependencyFunctor) obj;
        if (alias == null) {
            if (other.alias != null) return false;
        } else if (!alias.equals(other.alias)) return false;
        if (injectedClassLabel == null) {
            if (other.injectedClassLabel != null) return false;
        } else if (!injectedClassLabel.equals(other.injectedClassLabel)) return false;
        if (injectedLabel == null) {
            if (other.injectedLabel != null) return false;
        } else if (!injectedLabel.equals(other.injectedLabel)) return false;
        if (isAliased != other.isAliased) return false;
        if (methodLabel == null) {
            if (other.methodLabel != null) return false;
        } else if (!methodLabel.equals(other.methodLabel)) return false;
        return true;
    }
}
