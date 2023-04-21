package fitlibrary.valueAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import fit.Parse;
import fit.exception.FitFailureException;

/**
 * A MetaTypeAdapter doesn't interpret the contents of a cell as text, but in some other way.
 * For example, A TreeTyepAdapter interprets the contents as an HTML list and converts it into a ListTree.
 */
public abstract class MetaValueAdapter extends ValueAdapter {

    private Class type;

    private Method method;

    private Field field;

    private Object target;

    protected MetaValueAdapter(Class type, Method method, Field field) {
        this.type = type;
        this.method = method;
        this.field = field;
    }

    protected Object callReflectively(String methodName, Object[] args, Class[] argTypes, Object object) {
        try {
            Method reflectiveMethod = type.getMethod(methodName, argTypes);
            return reflectiveMethod.invoke(object, args);
        } catch (SecurityException e) {
            error(methodName, argTypes, e);
        } catch (NoSuchMethodException e) {
            error(methodName, argTypes, e);
        } catch (IllegalArgumentException e) {
            error(methodName, argTypes, e);
        } catch (IllegalAccessException e) {
            error(methodName, argTypes, e);
        } catch (InvocationTargetException e) {
            error(methodName, argTypes, e.getTargetException());
        }
        return null;
    }

    private void error(String methodName, Class[] argTypes, Throwable ex) throws FitFailureException {
        String args = Arrays.asList(argTypes).toString();
        args = "(" + args.substring(1, args.length() - 1) + ")";
        String problem = "Problem with accessing " + methodName + args + " of class " + type.getName() + ": " + ex;
        throw new FitFailureException(problem);
    }

    public void setTarget(Object element) {
        this.target = element;
    }

    public Object get() throws Exception {
        if (field != null) return field.get(target);
        if (method != null) return method.invoke(target, new Object[] {});
        return null;
    }

    public String getString() throws Exception {
        return get().toString();
    }

    public boolean matches(Parse cell, Object result) throws Exception {
        return equals(parse(cell), result);
    }

    public boolean equals(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }
}
