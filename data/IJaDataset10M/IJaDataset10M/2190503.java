package org.jucetice.javascript.extensions;

import org.jucetice.javascript.utils.Validator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrappedException;
import org.mozilla.javascript.Wrapper;

public class UnitExtension {

    /**
	 * 
	 * @param scope
	 * @param cx
	 */
    public static void register(Scriptable scope, Context cx) {
        ScriptableObject unitProto = (ScriptableObject) cx.newObject(scope);
        unitProto.defineFunctionProperties(new String[] { "executeTests", "throwException", "assertEquals", "assertNotEquals" }, UnitExtension.class, ScriptableObject.DONTENUM | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        ScriptableObject.putProperty(scope, "Unit", unitProto);
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static Object executeTests(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length == 1 && Validator.isValid(args[0]) && args[0] instanceof Scriptable) {
            StringBuilder buffer = new StringBuilder();
            Scriptable tests = (Scriptable) args[0];
            String lineSeparator = System.getProperty("line.separator");
            buffer.append("Executing tests cases: ");
            buffer.append(lineSeparator);
            buffer.append(lineSeparator);
            Object[] ids = tests.getIds();
            for (int i = 0; i < ids.length; i++) {
                String key = (String) ids[i];
                ScriptableObject function = (ScriptableObject) tests.get(key, tests);
                if (Validator.isValid(function) && function instanceof Function) {
                    Function func = (Function) function;
                    try {
                        func.call(cx, thisObj.getParentScope(), tests, Context.emptyArgs);
                    } catch (WrappedException ex) {
                        printUnitException(buffer, key, ex);
                    }
                }
            }
            return buffer.toString();
        }
        throw new IllegalArgumentException("Cannot call executeTests with wrong arguments");
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static void throwException(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws UnitException {
        String message = args.length >= 1 ? Context.toString(args[0]) : "Unit exception";
        Object argOne = args.length >= 2 ? args[1] : null;
        Object argTwo = args.length >= 3 ? args[2] : null;
        throw new UnitException(message, argOne, argTwo);
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static void assertEquals(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws UnitException {
        if (args.length != 3) throw new IllegalArgumentException("Cannot call assertEquals with " + args.length + " arguments");
        if (!checkEqual(args[1], args[2])) throw new UnitException(Context.toString(args[0]), args[1], args[2]);
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static void assertNotEquals(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws UnitException {
        if (args.length != 3) throw new IllegalArgumentException("Cannot call assertNotEquals with " + args.length + " arguments");
        if (checkEqual(args[1], args[2])) throw new UnitException(Context.toString(args[0]), args[1], args[2]);
    }

    /**
     * 
     * @author kraken
     *
     */
    static class UnitException extends Exception {

        Object arg1;

        Object arg2;

        public UnitException(String message, Object argument1, Object argument2) {
            super(message);
            this.arg1 = argument1;
            this.arg2 = argument2;
        }

        public Object getFirstObject() {
            return arg1;
        }

        public Object getSecondObject() {
            return arg2;
        }
    }

    /**
     * 
     * @param buffer
     * @param name
     * @param ex
     */
    protected static void printUnitException(StringBuilder buffer, String key, WrappedException ex) {
        String lineSeparator = System.getProperty("line.separator");
        buffer.append("Function: ");
        buffer.append(key);
        buffer.append(lineSeparator);
        if (ex != null && ex.getWrappedException() instanceof UnitException) {
            buffer.append("File: ");
            buffer.append(ex.sourceName());
            buffer.append(" @ ");
            buffer.append(ex.lineNumber());
            buffer.append(lineSeparator);
            UnitException wrapped = (UnitException) ex.getWrappedException();
            Object a = wrapped.getFirstObject();
            Object b = wrapped.getSecondObject();
            buffer.append("Exception: ");
            buffer.append(wrapped.getMessage());
            buffer.append(lineSeparator);
            buffer.append("Objects: ");
            buffer.append(lineSeparator);
            buffer.append(a.getClass().getName());
            buffer.append(": ");
            buffer.append(a.toString());
            buffer.append(lineSeparator);
            buffer.append(b.getClass().getName());
            buffer.append(": ");
            buffer.append(b.toString());
            buffer.append(lineSeparator);
        }
        buffer.append(lineSeparator);
    }

    /**
     * 
     * @param objectA
     * @param objectB
     * @return
     */
    protected static boolean checkEqual(Object objectA, Object objectB) {
        if (objectA == null) {
            return objectB == null;
        } else if (objectB == null) {
            return objectA == null;
        } else if (objectA == Undefined.instance) {
            return objectB == Undefined.instance;
        } else if (objectB == Undefined.instance) {
            return objectA == Undefined.instance;
        }
        if (objectA instanceof Wrapper) objectA = ((Wrapper) objectA).unwrap();
        if (objectB instanceof Wrapper) objectB = ((Wrapper) objectB).unwrap();
        if (objectA instanceof Number && objectB instanceof Number) {
            return ((Number) objectA).doubleValue() == ((Number) objectB).doubleValue();
        } else if (objectA instanceof String || objectB instanceof String) {
            return Context.toString(objectA).equals(Context.toString(objectB));
        } else {
            return objectA.equals(objectB);
        }
    }
}
