package moxie;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class SpyImpl<T> extends ObjectInterception<T> {

    private final T realObject;

    SpyImpl(T realObject, String name, MoxieFlags flags, List<Invocation> invocations) {
        super((Class<T>) realObject.getClass(), name, flags, instantiationStackTrace(name, flags), new Class[0], new Object[0]);
        this.realObject = realObject;
    }

    private static InstantiationStackTrace instantiationStackTrace(String name, MoxieFlags flags) {
        return flags.isTracing() ? new InstantiationStackTrace("spy object \"" + name + "\" was instantiated here") : null;
    }

    protected MethodBehavior defaultBehavior(InvocableAdapter invocable, final Object[] args, SuperInvoker superInvoker) {
        final Method method = ((MethodAdapter) invocable).getMethod();
        return new IdempotentMethodBehavior() {

            @Override
            protected void doInvoke() {
                try {
                    method.setAccessible(true);
                    result = method.invoke(realObject, args);
                } catch (IllegalAccessException e) {
                    thrown = new MoxieUnexpectedError("error calling target of spy object", e);
                } catch (InvocationTargetException e) {
                    thrown = e.getTargetException();
                } catch (Throwable t) {
                    thrown = t;
                }
            }
        };
    }
}
