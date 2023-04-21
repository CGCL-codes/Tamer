package net.sf.joyaop.impl.aspect;

import net.sf.cglib.reflect.FastMethod;
import net.sf.joyaop.Invocation;
import net.sf.joyaop.framework.AspectRuntimeException;
import net.sf.joyaop.framework.AspectizedClass;
import net.sf.joyaop.framework.RunnableAspect;
import net.sf.joyaop.impl.RunnableAspectInstance;
import net.sf.joyaop.util.ClassUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Shen Li
 */
public class DecoratorAspectImpl extends BaseInterceptorAspectImpl {

    private FastMethod method;

    protected boolean accept(Method method) {
        if (!super.accept(method)) {
            return false;
        }
        try {
            Method callback = getAspectClass().getMethod(method.getName(), method.getParameterTypes());
            return !Modifier.isAbstract(callback.getModifiers());
        } catch (NoSuchMethodException e) {
            if (method.getDeclaringClass().isInterface()) {
                throw new AspectRuntimeException("The decorator " + getAspectClass().getName() + " doesn't implement " + method.getDeclaringClass().getName());
            } else {
                throw new AspectRuntimeException("Illegal pointcut: the pointcut of the decorator could only pick out interface methods.");
            }
        }
    }

    protected void addInterceptor(AspectizedClass aspectizedClass, Method method) {
        DecoratorAspectImpl decorator = new DecoratorAspectImpl();
        decorator.setAspectClass(getAspectClass());
        decorator.setParameters(getParameters());
        decorator.setPointcut(getPointcut());
        decorator.setPrecedence(getPrecedence());
        decorator.setScope(getScope());
        decorator.method = ClassUtils.getFastMethod(getAspectClass(), method.getName(), method.getParameterTypes());
        aspectizedClass.addInterceptor(method, decorator);
    }

    public RunnableAspectInstance newRunnableInstance(Class originalClass) {
        return new DecoratorAspectInstance(this, originalClass, method);
    }

    public static class DecoratorAspectInstance extends RunnableAspectInstance {

        private static final long serialVersionUID = 0;

        private transient FastMethod method;

        public DecoratorAspectInstance(RunnableAspect aspect, Class originalClass, FastMethod method) {
            super(aspect, originalClass);
            this.method = method;
        }

        public Object invoke(Invocation invocation) throws Throwable {
            return ClassUtils.invokeFastMethod(method, getImplementation(), invocation.getArguments());
        }

        public boolean allowRecursiveInvocation() {
            return true;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeObject(method.getName());
            out.writeObject(method.getDeclaringClass());
            out.writeObject(method.getParameterTypes());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            String name = (String) in.readObject();
            Class clazz = (Class) in.readObject();
            Class[] paramTypes = (Class[]) in.readObject();
            method = ClassUtils.getFastMethod(clazz, name, paramTypes);
        }
    }
}
