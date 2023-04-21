package de.tuberlin.cs.cis.ocl.type.reflect;

import java.lang.reflect.Method;
import de.tuberlin.cs.cis.ocl.type.Classifier;
import de.tuberlin.cs.cis.ocl.type.ExpressionType;
import de.tuberlin.cs.cis.ocl.type.Property;
import de.tuberlin.cs.cis.ocl.type.Type;
import de.tuberlin.cs.cis.ocl.type.UndefinedFeatureException;

/**
 * Reflects type informations on java classes representing the
 * ocl types. Therefore adjusts missing features of the java type
 * system. 
 * 
 * <br/><br/>
 * Note:
 * The class itself needs major refactoring (but I do not have the time). 
 * The possiblities to adapt the type system are rudimentar. The mapping 
 * of ocl to java and vice versa is hardcoded. The type-interfaces must
 * therefore be seen as final. Changes can result in unintenial 
 * runtime results.  
 * 
 * @author fchabar
 *
 */
public class ReflOpTypeResolver {

    /**	the operation names that result in type t */
    private static final String[] T_OP_NAMES = { "sum", "any", "at", "first", "last" };

    /**	the operation names that result in a collection type */
    private static final String[] COL_OP_NAMES = { "asSequence", "asBag", "asSet", "select", "reject", "sortedBy", "intersection", "minus", "including", "excluding", "union", "symmetricDifference", "subSequence", "append", "prepend" };

    /** the operation names that called on an integer result in 
	 * an integer instead of a real
	 */
    private static final String[] INT_OP_NAMES = { "abs", "negation" };

    /**
	 * Maps an ocl operation name to its java method name. The parameter
	 * are used to differ between equal-named ocl operations.
	 * @param oclName the ocl operation name.
	 * @param params the ocl parameters.
	 * @return the java method name.
	 */
    protected static String javaMethodName(String oclName, Type[] params) {
        return javaMethodName(oclName, params != null ? params.length : 0);
    }

    /**
	 * Maps an ocl operation name to its java method name. The number of 
	 * parameters are used to differ between equal-named ocl operations
	 * (convenient in OCL).
	 * @param oclName the ocl operation name.
	 * @param params the ocl parameters.
	 * @return the java method name.
	 */
    protected static String javaMethodName(String oclName, int paramCount) {
        String jName = oclName;
        if ("=".equals(oclName)) {
            jName = "eq";
        } else if ("<>".equals(oclName)) {
            jName = "neq";
        } else if ("+".equals(oclName)) {
            jName = "plus";
        } else if ("-".equals(oclName)) {
            if (paramCount == 1) {
                jName = "minus";
            } else {
                jName = "negation";
            }
        } else if ("*".equals(oclName)) {
            jName = "multiplication";
        } else if ("/".equals(oclName)) {
            jName = "division";
        } else if ("<".equals(oclName)) {
            jName = "less";
        } else if (">".equals(oclName)) {
            jName = "more";
        } else if ("<=".equals(oclName)) {
            jName = "lessOrEqual";
        } else if (">=".equals(oclName)) {
            jName = "moreOrEqual";
        }
        return jName;
    }

    /**
	 * Maps a java method name to the name of the corresponding ocl
	 * operation.
	 * @param javaName a method name.
	 * @return the correspoding ocl name.
	 */
    public static String oclOpName(String javaName) {
        String oclName = javaName;
        if ("eq".equals(oclName)) {
            oclName = "=";
        } else if ("neq".equals(oclName)) {
            oclName = "<>";
        } else if ("plus".equals(oclName)) {
            oclName = "+";
        } else if ("minus".equals(oclName) || "negation".equals(oclName)) {
            oclName = "-";
        } else if ("multiplication".equals(oclName)) {
            oclName = "*";
        } else if ("division".equals(oclName)) {
            oclName = "/";
        } else if ("less".equals(oclName)) {
            oclName = "<";
        } else if ("more".equals(oclName)) {
            oclName = ">";
        } else if ("lessOrEqual".equals(oclName)) {
            oclName = "<=";
        } else if ("moreOrEqual".equals(oclName)) {
            oclName = ">=";
        }
        return oclName;
    }

    /**
	 * Determines the ocl return type of a method called on a
	 * collection. This is needed because the java type system 
	 * does not allow to parameterize collections. If a method 
	 * is reflected, its return type may for example be a 
	 * collection type. In order to set the element type of the
	 * collection this method is called. 
	 * @param type the type the operation is called on.
	 * @param operation the operation (method)
	 * @param opParams the parameters
	 * @param defaultType the default type, if the type must not
	 * be changed.
	 * @return the filtered return type.
	 */
    private static Type filterCollectionReturnType(Type type, Method operation, Type[] opParams, Type defaultType) {
        String name = operation.getName();
        Class[] params = operation.getParameterTypes();
        Type colT = type;
        Type t = colT.getElementType();
        Type evalType = null;
        boolean exprP = params != null && params.length == 1 && params[0].equals(OclExpression.class);
        if (exprP) {
            evalType = ((ExpressionType) opParams[0]).getEvaluationType();
        }
        if (exprP) {
            if ("iterate".equals(name)) {
                return evalType;
            }
        }
        for (int i = 0; i < T_OP_NAMES.length; i++) {
            String tName = T_OP_NAMES[i];
            if (tName.equals(name)) {
                return t;
            }
        }
        if (defaultType.isCollectionType()) {
            colT = defaultType;
            for (int i = 0; i < COL_OP_NAMES.length; i++) {
                String colTName = COL_OP_NAMES[i];
                if (colTName.equals(name)) {
                    return Type.getCollectionOfType(colT, t);
                }
            }
            if (exprP) {
                if ("collect".equals(name)) {
                    return Type.getCollectionOfType(colT, evalType);
                }
            }
        }
        return defaultType;
    }

    /**
	 * Determines the ocl type of the return type of an
	 * operation called on a classifier. Missing features
	 * in the java type system are adjusted.
	 * Following methods are precised:
	 * OclType:
	 * attributes() : Set(String)
	 * associationEnds() : Set(String)
	 * operations() : Set(String)
	 * supertypes() : Set(OclType)
	 * allSupertypes() : Set(OclType)
	 * allInstances() : Set(t)
	 * @param type the classifier
	 * @param operation the operation
	 * @param params ther parameters
	 * @param defaultType the default type
	 * @return the filterd return type.
	 */
    private static Type filterClassifierReturnType(Type type, Method operation, Type[] params, Type defaultType) {
        String name = operation.getName();
        if (defaultType.isCollectionType() && defaultType.conforms(Type.ASet(null)) && (params == null || params.length == 0)) {
            if ("attributes".equals(name) || "associationEnds".equals(name) || "operations".equals(name)) {
                return Type.ASet(Type.AString);
            } else if ("supertypes".equals(name) || "allSupertypes".equals(name)) {
                return Type.ASet(Classifier.OclType);
            } else if ("allInstances".equals(name)) {
                return Type.ASet(((Classifier) type).getInstanceType());
            }
        }
        return defaultType;
    }

    /**
	 * Determines the return type of an ocl operation called on the
	 * type Integer. The type Integer defines methods that should 
	 * return Integers but can not be overwritten in the java type 
	 * system (abs, min, max).
	 * @param opName the operation name
	 * @param params the operation paramteters
	 * @param defaultType the default type
	 * @return the return type.
	 */
    private static Type filterIntegerReturnType(String opName, Class[] params, Type defaultType) {
        if (params == null || params.length == 0) {
            for (int i = 0; i < INT_OP_NAMES.length; i++) {
                if (INT_OP_NAMES[i].equals(opName)) {
                    return Type.AnInteger;
                }
            }
        }
        return defaultType;
    }

    /**
	 * Determines the return type of an ocl operation.
	 * @param type the type the operation is called on.
	 * @param operation the operation name
	 * @param opParams the operation parameters
	 * @return the return type.
	 */
    private static Type oclReturnType(Type type, Method operation, Type[] opParams) {
        Type returnType = oclType(operation.getReturnType());
        String name = operation.getName();
        Class[] params = operation.getParameterTypes();
        type = ExpressionType.evaluateType(type);
        if (type.isCollectionType()) {
            returnType = filterCollectionReturnType(type, operation, opParams, returnType);
        }
        if (type.conforms(Type.AnInteger)) {
            returnType = filterIntegerReturnType(name, params, returnType);
        }
        if (type.isClassifier()) {
            returnType = filterClassifierReturnType(type, operation, opParams, returnType);
        }
        if ("oclAsType".equals(name)) {
            Type p = ExpressionType.evaluateType(opParams[0]);
            if (p instanceof Classifier) {
                if (type.isClassifier()) {
                    return p;
                } else {
                    return ((Classifier) p).getInstanceType();
                }
            }
        }
        return returnType;
    }

    /**
	 * Determines the type described by the specified class.
	 * @param t a class describing an OCL type.
	 * @return the ocl type.
	 */
    public static Type oclType(Class t) {
        Type type = null;
        if (OclCollection.class.isAssignableFrom(t)) {
            if (OclBag.class.equals(t)) {
                type = Type.ABag(null);
            } else if (OclSet.class.equals(t)) {
                type = Type.ASet(null);
            } else if (OclSequence.class.equals(t)) {
                type = Type.ASequence(null);
            } else {
                type = Type.ACollection(null);
            }
        } else if (OclExpression.class.isAssignableFrom(t)) {
            type = new ExpressionType(null);
        } else {
            if (OclBoolean.class.isAssignableFrom(t)) {
                type = Type.ABoolean;
            } else if (OclInteger.class.isAssignableFrom(t)) {
                type = Type.AnInteger;
            } else if (OclReal.class.isAssignableFrom(t)) {
                type = Type.AReal;
            } else if (OclString.class.isAssignableFrom(t)) {
                type = Type.AString;
            } else if (OclEnumeration.class.isAssignableFrom(t)) {
                type = Type.AnEnumeration;
            } else if (OclState.class.isAssignableFrom(t)) {
                type = Type.AnOclState;
            } else if (OclAny.class.isAssignableFrom(t)) {
                type = Type.AnOclAny;
            } else if (OclType.class.isAssignableFrom(t)) {
                type = Classifier.OclType;
            }
        }
        return type;
    }

    /**
	 * Determines the class describing the specified type.
	 * @param t an OCL type.
	 * @return the class describing the type.
	 */
    public static Class javaReflectionClass(Type t) {
        if (t.isClassifier()) {
            return OclType.class;
        }
        if (t instanceof ExpressionType) {
            return OclExpression.class;
        } else if (t.isCollectionType()) {
            Type e = t.getElementType();
            if (t.conforms(Type.ABag(e))) {
                return OclBag.class;
            } else if (t.conforms(Type.ASet(e))) {
                return OclSet.class;
            } else if (t.conforms(Type.ASequence(e))) {
                return OclSequence.class;
            } else {
                return OclCollection.class;
            }
        } else {
            if (t.conforms(Type.ABoolean)) {
                return OclBoolean.class;
            } else if (t.conforms(Type.AnInteger)) {
                return OclInteger.class;
            } else if (t.conforms(Type.AReal)) {
                return OclReal.class;
            } else if (t.conforms(Type.AString)) {
                return OclString.class;
            } else if (t.conforms(Type.AnEnumeration)) {
                return OclEnumeration.class;
            } else if (t.conforms(Type.AnOclState)) {
                return OclState.class;
            } else if (t.conforms(Type.AnOclAny)) {
                return OclAny.class;
            } else {
                return Object.class;
            }
        }
    }

    /**
	 * Reflects an operation on the specified type.
	 * @param type the type
	 * @param name the name of the operation
	 * @param params the parameters of the operation
	 * @return the property/operation
	 * @throws UndefinedFeatureException if the operation
	 * cannot be resolved on the specified type.
	 */
    public static Property reflectOperation(Type type, String name, Type[] params) throws UndefinedFeatureException {
        if (ignore(name)) {
            throw new UndefinedFeatureException();
        }
        Class refType = javaReflectionClass(type);
        String jName = javaMethodName(name, params);
        Method[] methods = refType.getMethods();
        Method op = null;
        for (int i = 0; i < methods.length; i++) {
            if (methodMatchs(type, methods[i], jName, params)) {
                op = methods[i];
                break;
            }
        }
        if (op == null) {
            throw new UndefinedFeatureException();
        }
        Type returnType = oclReturnType(type, op, params);
        return new Property(returnType, op);
    }

    /**
	 * Determines if a method is specified by the given name, and parameters.
	 * @param type the type defining the operation/method
	 * @param method a method
	 * @param name the name of the searched operation
	 * @param params the parameters of the searched operation
	 * @return
	 */
    private static boolean methodMatchs(Type type, Method method, String name, Type[] params) {
        Class[] javaParams = method.getParameterTypes();
        String mName = method.getName();
        boolean matchs = method.getName().equals(name);
        if (javaParams == null || params == null) {
            matchs = matchs && (javaParams == null || javaParams.length == 0) && (params == null || params.length == 0);
        } else {
            matchs = matchs && javaParams.length == params.length;
        }
        for (int j = 0; matchs && j < javaParams.length; j++) {
            matchs = parameterMatchs(type, params[j], javaParams[j]);
        }
        return matchs;
    }

    /**
	 * Determines if a parameter of an operation matchs a java type.
	 * @param type the type the operation is called on.
	 * @param param the paramter
	 * @param expectedParam the expected java type.
		 * @return true in case of a match, false otherwise.
	 */
    private static boolean parameterMatchs(Type type, Type param, Class expectedParam) {
        Type matchingParam = param;
        boolean matchs = expectedParam.isAssignableFrom(javaReflectionClass(param));
        if (!matchs && param instanceof ExpressionType) {
            matchs = expectedParam.isAssignableFrom(javaReflectionClass(ExpressionType.evaluateType(param)));
        }
        if (!matchs && type.isCollectionType() && expectedParam.equals(Object.class)) {
            matchs = ExpressionType.evaluateType(param).conforms(ExpressionType.evaluateType(type.getElementType()));
        }
        if (param.isCollectionType() && type.isCollectionType()) {
            matchs = matchs && param.getElementType().equals(type.getElementType());
        }
        return matchs;
    }

    /**
	 * Tests if a specified operation name must be ignored. An operation name
	 * must be ignored if the name matchs a java name but there is no 
	 * corresponding ocl operation (could happen because of the renaming to
	 * java conform method names).
	 * @param methodName a method name
	 * @return true if the method name must be ignored, false otherwise.
	 */
    private static boolean ignore(String methodName) {
        return "equals".equals(methodName) || "toString".equals(methodName) || "notEquals".equals(methodName) || "plus".equals(methodName) || "minus".equals(methodName) || "negation".equals(methodName) || "multiplication".equals(methodName) || "division".equals(methodName) || "less".equals(methodName) || "more".equals(methodName) || "lessOrEqual".equals(methodName) || "moreOrEqual".equals(methodName) || methodName.startsWith("get");
    }
}
