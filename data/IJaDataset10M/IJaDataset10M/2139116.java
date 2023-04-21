package org.gjt.sp.jedit.bsh;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class BshMethod implements java.io.Serializable {

    NameSpace declaringNameSpace;

    Modifiers modifiers;

    private String name;

    private Class creturnType;

    private String[] paramNames;

    private int numArgs;

    private Class[] cparamTypes;

    BSHBlock methodBody;

    private Method javaMethod;

    private Object javaObject;

    BshMethod(BSHMethodDeclaration method, NameSpace declaringNameSpace, Modifiers modifiers) {
        this(method.name, method.returnType, method.paramsNode.getParamNames(), method.paramsNode.paramTypes, method.blockNode, declaringNameSpace, modifiers);
    }

    BshMethod(String name, Class returnType, String[] paramNames, Class[] paramTypes, BSHBlock methodBody, NameSpace declaringNameSpace, Modifiers modifiers) {
        this.name = name;
        this.creturnType = returnType;
        this.paramNames = paramNames;
        if (paramNames != null) this.numArgs = paramNames.length;
        this.cparamTypes = paramTypes;
        this.methodBody = methodBody;
        this.declaringNameSpace = declaringNameSpace;
        this.modifiers = modifiers;
    }

    BshMethod(Method method, Object object) {
        this(method.getName(), method.getReturnType(), null, method.getParameterTypes(), null, null, null);
        this.javaMethod = method;
        this.javaObject = object;
    }

    public Class[] getParameterTypes() {
        return cparamTypes;
    }

    public String[] getParameterNames() {
        return paramNames;
    }

    public Class getReturnType() {
        return creturnType;
    }

    public Modifiers getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    /**
		Invoke the declared method with the specified arguments and interpreter
		reference.  This is the simplest form of invoke() for BshMethod 
		intended to be used in reflective style access to bsh scripts.
	*/
    public Object invoke(Object[] argValues, Interpreter interpreter) throws EvalError {
        return invoke(argValues, interpreter, null, null, false);
    }

    /**
		Invoke the bsh method with the specified args, interpreter ref,
		and callstack.
		callerInfo is the node representing the method invocation
		It is used primarily for debugging in order to provide access to the 
		text of the construct that invoked the method through the namespace.
		@param callerInfo is the BeanShell AST node representing the method 
			invocation.  It is used to print the line number and text of 
			errors in EvalError exceptions.  If the node is null here error
			messages may not be able to point to the precise location and text
			of the error.
		@param callstack is the callstack.  If callstack is null a new one
			will be created with the declaring namespace of the method on top
			of the stack (i.e. it will look for purposes of the method 
			invocation like the method call occurred in the declaring 
			(enclosing) namespace in which the method is defined).
	*/
    public Object invoke(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo) throws EvalError {
        return invoke(argValues, interpreter, callstack, callerInfo, false);
    }

    /**
		Invoke the bsh method with the specified args, interpreter ref,
		and callstack.
		callerInfo is the node representing the method invocation
		It is used primarily for debugging in order to provide access to the 
		text of the construct that invoked the method through the namespace.
		@param callerInfo is the BeanShell AST node representing the method 
			invocation.  It is used to print the line number and text of 
			errors in EvalError exceptions.  If the node is null here error
			messages may not be able to point to the precise location and text
			of the error.
		@param callstack is the callstack.  If callstack is null a new one
			will be created with the declaring namespace of the method on top
			of the stack (i.e. it will look for purposes of the method 
			invocation like the method call occurred in the declaring 
			(enclosing) namespace in which the method is defined).
		@param overrideNameSpace 
			When true the method is executed in the namespace on the top of the
			stack instead of creating its own local namespace.  This allows it
			to be used in constructors.
	*/
    Object invoke(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo, boolean overrideNameSpace) throws EvalError {
        if (argValues != null) for (int i = 0; i < argValues.length; i++) if (argValues[i] == null) throw new Error("HERE!");
        if (javaMethod != null) try {
            return Reflect.invokeMethod(javaMethod, javaObject, argValues);
        } catch (ReflectError e) {
            throw new EvalError("Error invoking Java method: " + e, callerInfo, callstack);
        } catch (InvocationTargetException e2) {
            throw new TargetError("Exception invoking imported object method.", e2, callerInfo, callstack, true);
        }
        if (modifiers != null && modifiers.hasModifier("synchronized")) {
            Object lock;
            if (declaringNameSpace.isClass) {
                try {
                    lock = declaringNameSpace.getClassInstance();
                } catch (UtilEvalError e) {
                    throw new InterpreterError("Can't get class instance for synchronized method.");
                }
            } else lock = declaringNameSpace.getThis(interpreter);
            synchronized (lock) {
                return invokeImpl(argValues, interpreter, callstack, callerInfo, overrideNameSpace);
            }
        } else return invokeImpl(argValues, interpreter, callstack, callerInfo, overrideNameSpace);
    }

    private Object invokeImpl(Object[] argValues, Interpreter interpreter, CallStack callstack, SimpleNode callerInfo, boolean overrideNameSpace) throws EvalError {
        Class returnType = getReturnType();
        Class[] paramTypes = getParameterTypes();
        if (callstack == null) callstack = new CallStack(declaringNameSpace);
        if (argValues == null) argValues = new Object[] {};
        if (argValues.length != numArgs) {
            throw new EvalError("Wrong number of arguments for local method: " + name, callerInfo, callstack);
        }
        NameSpace localNameSpace;
        if (overrideNameSpace) localNameSpace = callstack.top(); else {
            localNameSpace = new NameSpace(declaringNameSpace, name);
            localNameSpace.isMethod = true;
        }
        localNameSpace.setNode(callerInfo);
        for (int i = 0; i < numArgs; i++) {
            if (paramTypes[i] != null) {
                try {
                    argValues[i] = Types.castObject(argValues[i], paramTypes[i], Types.ASSIGNMENT);
                } catch (UtilEvalError e) {
                    throw new EvalError("Invalid argument: " + "`" + paramNames[i] + "'" + " for method: " + name + " : " + e.getMessage(), callerInfo, callstack);
                }
                try {
                    localNameSpace.setTypedVariable(paramNames[i], paramTypes[i], argValues[i], null);
                } catch (UtilEvalError e2) {
                    throw e2.toEvalError("Typed method parameter assignment", callerInfo, callstack);
                }
            } else {
                if (argValues[i] == Primitive.VOID) throw new EvalError("Undefined variable or class name, parameter: " + paramNames[i] + " to method: " + name, callerInfo, callstack); else try {
                    localNameSpace.setLocalVariable(paramNames[i], argValues[i], interpreter.getStrictJava());
                } catch (UtilEvalError e3) {
                    throw e3.toEvalError(callerInfo, callstack);
                }
            }
        }
        if (!overrideNameSpace) callstack.push(localNameSpace);
        Object ret = methodBody.eval(callstack, interpreter, true);
        CallStack returnStack = callstack.copy();
        if (!overrideNameSpace) callstack.pop();
        ReturnControl retControl = null;
        if (ret instanceof ReturnControl) {
            retControl = (ReturnControl) ret;
            if (retControl.kind == retControl.RETURN) ret = ((ReturnControl) ret).value; else throw new EvalError("'continue' or 'break' in method body", retControl.returnPoint, returnStack);
            if (returnType == Void.TYPE && ret != Primitive.VOID) throw new EvalError("Cannot return value from void method", retControl.returnPoint, returnStack);
        }
        if (returnType != null) {
            if (returnType == Void.TYPE) return Primitive.VOID;
            try {
                ret = Types.castObject(ret, returnType, Types.ASSIGNMENT);
            } catch (UtilEvalError e) {
                SimpleNode node = callerInfo;
                if (retControl != null) node = retControl.returnPoint;
                throw e.toEvalError("Incorrect type returned from method: " + name + e.getMessage(), node, callstack);
            }
        }
        return ret;
    }

    public boolean hasModifier(String name) {
        return modifiers != null && modifiers.hasModifier(name);
    }

    public String toString() {
        return "Scripted Method: " + StringUtil.methodString(name, getParameterTypes());
    }
}
