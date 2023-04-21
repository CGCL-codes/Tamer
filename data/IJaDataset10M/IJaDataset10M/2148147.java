package commonapp.eval;

import common.log.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
   This class is used in conjunction with {@link EvalExpr} to evaluate
   functional and field expressions.
*/
public class EvalFunc {

    /** Field name/value backing store. */
    private EvalValue myEvalValue = null;

    /** Current error message. */
    private String myErrorMessage = null;

    /**
     Constructs a new EvalFunc.

     @param theEvalValue the evaluation name/value backing store.
  */
    public EvalFunc(EvalValue theEvalValue) {
        myEvalValue = theEvalValue;
    }

    /**
     Returns the error message associated with the last call to getFieldValue()
     or getFuncValue().

     @return the error message associated with the last call to getFieldValue()
     or getFuncValue().
  */
    public final String getErrorMessage() {
        return myErrorMessage;
    }

    public final Object getFieldValue(String theReference) {
        Object value = null;
        myErrorMessage = null;
        if (theReference.indexOf('.') >= 0) {
            try {
                String[] parts = EvalUtils.parseLast(theReference, ".");
                Class<?> targetClass = getClass(parts[0]);
                if (targetClass != null) {
                    Field targetField = null;
                    targetField = targetClass.getField(parts[1]);
                    Object target = getInstance(targetClass, new Object[0]);
                    value = targetField.get(target);
                } else {
                    parts = EvalUtils.parseFirst(theReference, ".");
                    Object target = myEvalValue.getEvalValue(parts[0]);
                    if (target != null) {
                        if (parts[1].equals("length") && target.getClass().getName().indexOf('[') >= 0) {
                            value = Integer.valueOf(Array.getLength(target));
                        } else {
                            Field targetField = target.getClass().getField(parts[1]);
                            value = targetField.get(target);
                        }
                    }
                }
            } catch (Exception e) {
                Log.main.println(Log.FAULT, e.getMessage());
            }
        }
        if (value == null) {
            value = myEvalValue.getEvalValue(theReference);
        }
        if (value == null) {
            value = "";
        }
        return value;
    }

    public final Object getFuncValue(String theReference, Object[] theArgs) {
        Object value = null;
        myErrorMessage = null;
        Class<?> targetClass = getClass(theReference);
        if (targetClass != null) {
            value = getInstance(targetClass, theArgs);
        } else if (theReference.indexOf('.') >= 0) {
            String[] parts = EvalUtils.parseLast(theReference, ".");
            targetClass = getClass(parts[0]);
            if (targetClass != null) {
                Object target = getInstance(targetClass, new Object[0]);
                if (target != null) {
                    value = invoke(target, parts[1], theArgs);
                } else {
                    value = invoke(targetClass, parts[1], theArgs);
                }
            } else {
                parts = EvalUtils.parseFirst(theReference, ".");
                Object target = myEvalValue.getEvalValue(parts[0]);
                if (target != null) {
                    value = invoke(target, parts[1], theArgs);
                }
            }
        }
        return value;
    }

    private Class<?> getClass(String theName) {
        Class<?> value = null;
        try {
            value = Class.forName(theName);
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    private Object getInstance(Class<?> theClass, Object[] theArgs) {
        Object value = null;
        Class<?>[] constructorClasses = EvalUtils.getArgClasses(theArgs);
        Constructor<?> targetConstructor = null;
        try {
            targetConstructor = theClass.getConstructor(constructorClasses);
        } catch (Exception e) {
            try {
                Constructor<?>[] consts = theClass.getConstructors();
                int numConsts = (consts == null) ? 0 : consts.length;
                int numArgs = (theArgs == null) ? 0 : theArgs.length;
                for (int c = 0; targetConstructor == null && c < numConsts; c++) {
                    Class<?>[] argsRecv = consts[c].getParameterTypes();
                    if (checkArgMatch(numArgs, theArgs, argsRecv)) {
                        targetConstructor = consts[c];
                    }
                }
            } catch (Exception e1) {
                Log.main.println(Log.FAULT, e1.getMessage());
                myErrorMessage = e1.getMessage();
            }
        }
        if (targetConstructor != null) {
            try {
                value = targetConstructor.newInstance(theArgs);
            } catch (Exception e) {
                myErrorMessage = e.getMessage();
            }
        }
        return value;
    }

    private Object invoke(Object theTarget, String theMethodName, Object[] theArgs) {
        Object value = null;
        Class<?> targetClass = theTarget.getClass();
        if (targetClass.getName().equals("java.lang.Class")) {
            targetClass = (Class<?>) theTarget;
        }
        Method targetMethod = null;
        targetMethod = getMethod(targetClass, theMethodName, theArgs);
        if (targetMethod != null) {
            try {
                value = targetMethod.invoke(theTarget, theArgs);
                if (value == null && targetMethod.getReturnType() == Void.TYPE) {
                    value = "";
                }
            } catch (Exception e) {
                Log.main.println(Log.FAULT, e.getMessage());
                myErrorMessage = e.getMessage();
            }
        }
        return value;
    }

    /**
     Finds a method for the specified class that has a signature that matches
     the specified method arguments.

     @param theClass the target class.

     @param theMethodName the unqualified method name.

     @param theArg the actual method invocation arguments.

     @return the matching method else return null and set the error message
     if the method can't be found.
  */
    private Method getMethod(Class<?> theClass, String theMethodName, Object[] theArgs) {
        Method targetMethod = null;
        Class<?>[] argClasses = EvalUtils.getArgClasses(theArgs);
        int numArgs = (theArgs == null) ? 0 : theArgs.length;
        try {
            targetMethod = theClass.getMethod(theMethodName, argClasses);
        } catch (Exception e) {
            try {
                targetMethod = null;
                Method[] methods = theClass.getMethods();
                int numMethods = (methods == null) ? 0 : methods.length;
                for (int m = 0; targetMethod == null && m < numMethods; m++) {
                    String name = methods[m].getName();
                    if (!name.equals(theMethodName)) {
                        continue;
                    }
                    Class<?>[] argsRecv = methods[m].getParameterTypes();
                    if (checkArgMatch(numArgs, theArgs, argsRecv)) {
                        targetMethod = methods[m];
                    }
                }
            } catch (Exception e1) {
                targetMethod = null;
            }
        }
        if (targetMethod == null) {
            myErrorMessage = "can't find method " + theClass.getName() + "." + theMethodName + "()";
        }
        return targetMethod;
    }

    private boolean checkArgMatch(int theNumArgs, Object[] theArgs, Class<?>[] theParams) {
        boolean match = true;
        int numParam = (theParams == null) ? 0 : theParams.length;
        if (numParam != theNumArgs) {
            match = false;
        } else {
            for (int a = 0; match && a < numParam; a++) {
                if (theParams[a].isInstance(theArgs[a])) {
                    continue;
                }
                String className = theArgs[a].getClass().getName();
                if (theParams[a] == Integer.TYPE) {
                    if (!className.equals("java.lang.Integer")) {
                        match = false;
                    }
                } else if (theParams[a] == Double.TYPE) {
                    if (!className.equals("java.lang.Double") && !className.equals("java.lang.Integer")) {
                        match = false;
                    }
                } else if (theParams[a] == Character.TYPE) {
                    if (!className.equals("java.lang.Character")) {
                        match = false;
                    }
                } else if (theParams[a] == Long.TYPE) {
                    if (!className.equals("java.lang.Long")) {
                        match = false;
                    }
                } else if (theParams[a] == Boolean.TYPE) {
                    if (!className.equals("java.lang.Boolean")) {
                        match = false;
                    }
                } else {
                    match = false;
                }
            }
        }
        return match;
    }
}
