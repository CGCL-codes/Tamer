package org.td4j.core.internal.reflect;

import java.lang.reflect.Constructor;
import java.util.List;
import javax.swing.SwingUtilities;
import ch.miranet.commons.TK;

public class ConstructorOperation extends AbstractOperation {

    private final Constructor<?> constructor;

    private final List<InvokationParameter> parameters;

    public ConstructorOperation(Constructor<?> constructor, String... paramNames) {
        this.constructor = TK.Objects.assertNotNull(constructor, "constructor");
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        parameters = createInvokationParameters(paramNames, constructor.getParameterTypes());
    }

    @Override
    public Object invoke(Object... args) {
        Object instance = null;
        try {
            instance = constructor.newInstance(args);
        } catch (final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    throw new RuntimeException(e);
                }
            });
        }
        return instance;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public List<InvokationParameter> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getReturnItemType() {
        return constructor.getDeclaringClass();
    }

    @Override
    public String toString() {
        final String name = constructor.getDeclaringClass().getSimpleName();
        final String paramNames = paramNamesToString(parameters);
        return "+ " + (TK.Strings.isEmpty(paramNames) ? name : String.format("%1$s: %2$s", name, paramNames));
    }
}
