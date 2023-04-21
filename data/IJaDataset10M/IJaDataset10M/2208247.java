package de.matthiasmann.twl.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthias Mann
 */
public abstract class AbstractMathInterpreter implements SimpleMathParser.Interpreter {

    public interface Function {

        public Object execute(Object... args);
    }

    private final ArrayList<Object> stack;

    private final HashMap<String, Function> functions;

    public AbstractMathInterpreter() {
        this.stack = new ArrayList<Object>();
        this.functions = new HashMap<String, Function>();
        registerFunction("min", new FunctionMin());
        registerFunction("max", new FunctionMax());
    }

    public void registerFunction(String name, Function function) {
        if (function == null) {
            throw new NullPointerException("function");
        }
        functions.put(name, function);
    }

    public Number execute(String str) throws ParseException {
        stack.clear();
        SimpleMathParser.interpret(str, this);
        if (stack.size() != 1) {
            throw new IllegalStateException("Expected one return value on the stack");
        }
        return popNumber();
    }

    public int[] executeIntArray(String str) throws ParseException {
        stack.clear();
        int count = SimpleMathParser.interpretArray(str, this);
        if (stack.size() != count) {
            throw new IllegalStateException("Expected " + count + " return values on the stack");
        }
        int[] result = new int[count];
        for (int i = count; i-- > 0; ) {
            result[i] = popNumber().intValue();
        }
        return result;
    }

    public <T> T executeCreateObject(String str, Class<T> type) throws ParseException {
        stack.clear();
        int count = SimpleMathParser.interpretArray(str, this);
        if (stack.size() != count) {
            throw new IllegalStateException("Expected " + count + " return values on the stack");
        }
        if (count == 1 && type.isInstance(stack.get(0))) {
            return type.cast(stack.get(0));
        }
        for (Constructor<?> c : type.getConstructors()) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == count) {
                boolean match = true;
                for (int i = 0; i < count; i++) {
                    if (!ClassUtils.isParamCompatible(params[i], stack.get(i))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    try {
                        return type.cast(c.newInstance(stack.toArray(new Object[count])));
                    } catch (Exception ex) {
                        Logger.getLogger(AbstractMathInterpreter.class.getName()).log(Level.SEVERE, "can't instanciate object", ex);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Can't construct a " + type + " from expression: \"" + str + "\"");
    }

    protected void push(Object obj) {
        stack.add(obj);
    }

    protected Object pop() {
        int size = stack.size();
        if (size == 0) {
            throw new IllegalStateException("stack underflow");
        }
        return stack.remove(size - 1);
    }

    protected Number popNumber() {
        Object obj = pop();
        if (obj instanceof Number) {
            return (Number) obj;
        }
        throw new IllegalStateException("expected number on stack - found: " + ((obj != null) ? obj.getClass() : "null"));
    }

    public void loadConst(Number n) {
        push(n);
    }

    public void add() {
        Number b = popNumber();
        Number a = popNumber();
        boolean isFloat = isFloat(a) || isFloat(b);
        if (isFloat) {
            push(a.floatValue() + b.floatValue());
        } else {
            push(a.intValue() + b.intValue());
        }
    }

    public void sub() {
        Number b = popNumber();
        Number a = popNumber();
        boolean isFloat = isFloat(a) || isFloat(b);
        if (isFloat) {
            push(a.floatValue() - b.floatValue());
        } else {
            push(a.intValue() - b.intValue());
        }
    }

    public void mul() {
        Number b = popNumber();
        Number a = popNumber();
        boolean isFloat = isFloat(a) || isFloat(b);
        if (isFloat) {
            push(a.floatValue() * b.floatValue());
        } else {
            push(a.intValue() * b.intValue());
        }
    }

    public void div() {
        Number b = popNumber();
        Number a = popNumber();
        boolean isFloat = isFloat(a) || isFloat(b);
        if (isFloat) {
            if (Math.abs(b.floatValue()) == 0) {
                throw new IllegalStateException("division by zero");
            }
            push(a.floatValue() / b.floatValue());
        } else {
            if (b.intValue() == 0) {
                throw new IllegalStateException("division by zero");
            }
            push(a.intValue() / b.intValue());
        }
    }

    public void accessArray() {
        Number idx = popNumber();
        Object obj = pop();
        if (obj == null) {
            throw new IllegalStateException("null pointer");
        }
        if (!obj.getClass().isArray()) {
            throw new IllegalStateException("array expected");
        }
        try {
            push(Array.get(obj, idx.intValue()));
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalStateException("array index out of bounds", ex);
        }
    }

    public void accessField(String field) {
        Object obj = pop();
        if (obj == null) {
            throw new IllegalStateException("null pointer");
        }
        Object result = accessField(obj, field);
        push(result);
    }

    protected Object accessField(Object obj, String field) {
        try {
            if (obj.getClass().isArray()) {
                if ("length".equals(field)) {
                    return Array.getLength(obj);
                }
            } else {
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    if (pd.getName().equals(field)) {
                        return pd.getReadMethod().invoke(obj);
                    }
                }
            }
            throw new IllegalStateException("unknown field '" + field + "' of class '" + obj.getClass() + "'");
        } catch (Throwable ex) {
            throw new IllegalStateException("error accessing field '" + field + "' of class '" + obj.getClass() + "'", ex);
        }
    }

    public void callFunction(String name, int args) {
        Object[] values = new Object[args];
        for (int i = args; i-- > 0; ) {
            values[i] = pop();
        }
        Function function = functions.get(name);
        if (function == null) {
            throw new IllegalArgumentException("Unknown function");
        }
        push(function.execute(values));
    }

    protected static boolean isFloat(Number n) {
        return !(n instanceof Integer);
    }

    public abstract static class NumberFunction implements Function {

        protected abstract Object execute(int... values);

        protected abstract Object execute(float... values);

        public Object execute(Object... args) {
            for (Object o : args) {
                if (!(o instanceof Integer)) {
                    float[] values = new float[args.length];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = ((Number) args[i]).floatValue();
                    }
                    return execute(values);
                }
            }
            int[] values = new int[args.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = ((Number) args[i]).intValue();
            }
            return execute(values);
        }
    }

    static class FunctionMin extends NumberFunction {

        @Override
        protected Object execute(int... values) {
            int result = values[0];
            for (int i = 1; i < values.length; i++) {
                result = Math.min(result, values[i]);
            }
            return result;
        }

        @Override
        protected Object execute(float... values) {
            float result = values[0];
            for (int i = 1; i < values.length; i++) {
                result = Math.min(result, values[i]);
            }
            return result;
        }
    }

    static class FunctionMax extends NumberFunction {

        @Override
        protected Object execute(int... values) {
            int result = values[0];
            for (int i = 1; i < values.length; i++) {
                result = Math.max(result, values[i]);
            }
            return result;
        }

        @Override
        protected Object execute(float... values) {
            float result = values[0];
            for (int i = 1; i < values.length; i++) {
                result = Math.max(result, values[i]);
            }
            return result;
        }
    }
}
