package tests.util;

import java.util.Stack;

/**
 * A stack to store the parameters of a call, as well as the call stack.
 * 
 */
public class CallVerificationStack extends Stack<Object> {

    private static final long serialVersionUID = 1L;

    private static final CallVerificationStack _instance = new CallVerificationStack();

    private final Stack<StackTraceElement> callStack = new Stack<StackTraceElement>();

    /**
	 * Can't be instantiated.
	 */
    private CallVerificationStack() {
    }

    /**
	 * Gets the singleton instance.
	 * 
	 * @return the singleton instance
	 */
    public static CallVerificationStack getInstance() {
        return _instance;
    }

    /**
	 * Pushes the call stack.
	 */
    private void pushCallStack() {
        StackTraceElement[] eles = (new Throwable()).getStackTrace();
        int i;
        for (i = 1; i < eles.length; i++) {
            if (!eles[i].getClassName().equals(this.getClass().getName())) {
                break;
            }
        }
        this.callStack.push(eles[i]);
    }

    /**
	 * Gets the "current" calling class name.
	 * 
	 * @return the "current" calling class name
	 */
    public String getCurrentSourceClass() {
        return this.callStack.peek().getClassName();
    }

    /**
	 * Gets the "current" calling method name.
	 * 
	 * @return the "current" calling method name
	 */
    public String getCurrentSourceMethod() {
        return this.callStack.peek().getMethodName();
    }

    /**
	 * Clear the parameter stack and the call stack.
	 * 
	 */
    @Override
    public void clear() {
        this.callStack.clear();
        super.clear();
    }

    @Override
    public Object push(Object o) {
        pushCallStack();
        return super.push(o);
    }

    /**
	 * Pushes a boolean onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(boolean val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes a char onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(char val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes a double onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(double val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes a float onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(float val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes an int onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(int val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes a long onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(long val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pushes a short onto the top of this stack.
	 * 
	 * @param val
	 *            the value to push
	 */
    public void push(short val) {
        this.push(new BaseTypeWrapper(val));
    }

    /**
	 * Pop an object.
	 * 
	 * @return the object
	 */
    @Override
    public Object pop() {
        this.callStack.pop();
        return super.pop();
    }

    /**
	 * Pop a boolean.
	 * 
	 * @return the value
	 */
    public boolean popBoolean() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Boolean value = (Boolean) wrapper.getValue();
        return value.booleanValue();
    }

    /**
	 * Pop a char.
	 * 
	 * @return the value
	 */
    public char popChar() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Character value = (Character) wrapper.getValue();
        return value.charValue();
    }

    /**
	 * Pop a double.
	 * 
	 * @return the value
	 */
    public double popDouble() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Double value = (Double) wrapper.getValue();
        return value.doubleValue();
    }

    /**
	 * Pop a float.
	 * 
	 * @return the value
	 */
    public float popFloat() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Float value = (Float) wrapper.getValue();
        return value.floatValue();
    }

    /**
	 * Pop a int.
	 * 
	 * @return the value
	 */
    public int popInt() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Integer value = (Integer) wrapper.getValue();
        return value.intValue();
    }

    /**
	 * Pop a long.
	 * 
	 * @return the value
	 */
    public long popLong() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Long value = (Long) wrapper.getValue();
        return value.longValue();
    }

    /**
	 * Pop a short.
	 * 
	 * @return the value
	 */
    public short popShort() {
        BaseTypeWrapper wrapper = (BaseTypeWrapper) this.pop();
        Short value = (Short) wrapper.getValue();
        return value.shortValue();
    }

    class BaseTypeWrapper {

        private Object value;

        public BaseTypeWrapper(boolean val) {
            this.value = new Boolean(val);
        }

        public BaseTypeWrapper(byte val) {
            this.value = new Byte(val);
        }

        public BaseTypeWrapper(char val) {
            this.value = new Character(val);
        }

        public BaseTypeWrapper(double val) {
            this.value = new Double(val);
        }

        public BaseTypeWrapper(float val) {
            this.value = new Float(val);
        }

        public BaseTypeWrapper(int val) {
            this.value = new Integer(val);
        }

        public BaseTypeWrapper(long val) {
            this.value = new Long(val);
        }

        public BaseTypeWrapper(short val) {
            this.value = new Short(val);
        }

        public Object getValue() {
            return this.value;
        }
    }
}
