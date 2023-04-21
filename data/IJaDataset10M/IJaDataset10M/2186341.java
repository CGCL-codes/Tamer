package flex.messaging.util;

import flex.messaging.io.TypeMarshaller;
import flex.messaging.io.TypeMarshallingContext;
import flex.messaging.MessageException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Method;

/**
 * A utility class used to find a suitable method based on matching
 * signatures to the types of set of arguments. Since the arguments
 * may be from more loosely typed environments such as ActionScript,
 * a translator can be employed to handle type conversion. Note that
 * there isn't a great guarantee for which method will be selected
 * when several overloaded methods match very closely through the use
 * of various combinations of generic types.
 *
 * @exclude
 */
public class MethodMatcher {

    private final Map<MethodKey, Method> methodCache = new HashMap<MethodKey, Method>();

    private static final int ARGUMENT_CONVERSION_ERROR = 10006;

    private static final int CANNOT_INVOKE_METHOD = 10007;

    /**
     * Default constructor.
     */
    public MethodMatcher() {
    }

    /**
     * Utility method that searches a class for a given method, taking
     * into account the supplied parameters when evaluating overloaded
     * method signatures.
     *
     * @param c The class.
     * @param methodName Desired method to search for
     * @param parameters Required to distinguish between overloaded methods of the same name
     * @return The best-match <tt>Method</tt>.
     */
    public Method getMethod(Class c, String methodName, List parameters) {
        Match bestMatch = new Match(methodName);
        Class[] suppliedParamTypes = paramTypes(parameters);
        MethodKey methodKey = new MethodKey(c, methodName, suppliedParamTypes);
        Method method = null;
        if (methodCache.containsKey(methodKey)) {
            method = methodCache.get(methodKey);
            String thisMethodName = method.getName();
            bestMatch.matchedMethodName = thisMethodName;
        } else {
            try {
                method = c.getMethod(methodName, suppliedParamTypes);
                synchronized (methodCache) {
                    Method method2 = methodCache.get(methodKey);
                    if (method2 == null) methodCache.put(methodKey, method); else method = method2;
                }
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
            if (method == null) {
                Method[] methods = c.getMethods();
                for (Method thisMethod : c.getMethods()) {
                    String thisMethodName = thisMethod.getName();
                    if (!thisMethodName.equalsIgnoreCase(methodName)) continue;
                    Match currentMatch = new Match(methodName);
                    currentMatch.matchedMethodName = thisMethodName;
                    if (bestMatch.matchedMethodName == null) bestMatch = currentMatch;
                    Class[] desiredParamTypes = thisMethod.getParameterTypes();
                    currentMatch.methodParamTypes = desiredParamTypes;
                    if (desiredParamTypes.length != suppliedParamTypes.length) continue;
                    currentMatch.matchedByNumberOfParams = true;
                    if (!bestMatch.matchedByNumberOfParams && bestMatch.matchedParamCount == 0) bestMatch = currentMatch;
                    convertParams(parameters, desiredParamTypes, currentMatch, false);
                    if (currentMatch.matchedParamCount >= bestMatch.matchedParamCount && currentMatch.exactMatchedParamCount >= bestMatch.exactMatchedParamCount) bestMatch = currentMatch;
                    if (currentMatch.matchedParamCount == desiredParamTypes.length && bestMatch == currentMatch) {
                        method = thisMethod;
                        synchronized (methodCache) {
                            Method method2 = methodCache.get(methodKey);
                            if (method2 == null || method2 != method) methodCache.put(methodKey, method); else method = method2;
                        }
                    }
                }
            }
        }
        if (method == null) {
            methodNotFound(methodName, suppliedParamTypes, bestMatch);
        } else if (bestMatch.paramTypeConversionFailure != null) {
            MessageException me = new MessageException();
            me.setMessage(ARGUMENT_CONVERSION_ERROR);
            me.setCode("Server.Processing");
            me.setRootCause(bestMatch.paramTypeConversionFailure);
            throw me;
        }
        Class<?>[] desiredParamTypes = method.getParameterTypes();
        bestMatch.methodParamTypes = desiredParamTypes;
        convertParams(parameters, desiredParamTypes, bestMatch, true);
        return method;
    }

    /**
     * Utility method to convert a collection of parameters to desired types. We keep track
     * of the progress of the conversion to allow callers to gauge the success of the conversion.
     * This is important for ranking overloaded-methods and debugging purposes.
     *
     * @param parameters actual parameters for an invocation
     * @param desiredParamTypes classes in the signature of a potential match for the invocation
     * @param currentMatch the currently best known match
     * @param convert determines whether the actual conversion should take place.
     */
    public static void convertParams(List parameters, Class[] desiredParamTypes, Match currentMatch, boolean convert) {
        int matchCount = 0;
        int exactMatchCount = 0;
        currentMatch.matchedParamCount = 0;
        currentMatch.convertedSuppliedTypes = new Class[desiredParamTypes.length];
        TypeMarshaller marshaller = TypeMarshallingContext.getTypeMarshaller();
        for (int i = 0; i < desiredParamTypes.length; i++) {
            Object param = parameters.get(i);
            if (param != null) {
                Object obj = null;
                Class objClass = null;
                if (marshaller != null) {
                    try {
                        obj = marshaller.convert(param, desiredParamTypes[i]);
                    } catch (MessageException ex) {
                        currentMatch.paramTypeConversionFailure = ex;
                        break;
                    } catch (ClassCastException ex) {
                        currentMatch.paramTypeConversionFailure = ex;
                        break;
                    }
                } else {
                    obj = param;
                }
                currentMatch.convertedSuppliedTypes[i] = (obj != null ? (objClass = obj.getClass()) : null);
                if (obj == null || isAssignableFrom(desiredParamTypes[i], objClass)) {
                    if (isAssignableFrom(desiredParamTypes[i], param.getClass())) exactMatchCount++;
                    if (convert) parameters.set(i, obj);
                    matchCount++;
                } else {
                    break;
                }
            } else {
                matchCount++;
            }
        }
        currentMatch.matchedParamCount = matchCount;
        currentMatch.exactMatchedParamCount = exactMatchCount;
    }

    private static boolean isAssignableFrom(Class first, Class second) {
        return (first.isAssignableFrom(second)) || (first == Integer.TYPE && Integer.class.isAssignableFrom(second)) || (first == Double.TYPE && Double.class.isAssignableFrom(second)) || (first == Long.TYPE && Long.class.isAssignableFrom(second)) || (first == Boolean.TYPE && Boolean.class.isAssignableFrom(second)) || (first == Character.TYPE && Character.class.isAssignableFrom(second)) || (first == Float.TYPE && Float.class.isAssignableFrom(second)) || (first == Short.TYPE && Short.class.isAssignableFrom(second)) || (first == Byte.TYPE && Byte.class.isAssignableFrom(second));
    }

    /**
     * Utility method that iterates over a collection of input
     * parameters to determine their types while logging
     * the class names to create a unique identifier for a
     * method signature.
     *
     * @param parameters - A list of supplied parameters.
     * @return An array of <tt>Class</tt> instances indicating the class of each corresponding parameter.
     */
    public static Class[] paramTypes(List parameters) {
        Class[] paramTypes = new Class[parameters.size()];
        for (int i = 0; i < paramTypes.length; i++) {
            Object p = parameters.get(i);
            paramTypes[i] = p == null ? Object.class : p.getClass();
        }
        return paramTypes;
    }

    /**
     * Utility method to provide more detailed information in the event that a search
     * for a specific method failed for the service class.
     *
     * @param methodName         the name of the missing method
     * @param suppliedParamTypes the types of parameters supplied for the search
     * @param bestMatch          the best match found during the search
     */
    public static void methodNotFound(String methodName, Class[] suppliedParamTypes, Match bestMatch) {
        int errorCode = CANNOT_INVOKE_METHOD;
        Object[] errorParams = new Object[] { methodName };
        String errorDetailVariant = "0";
        Object[] errorDetailParams = new Object[] { methodName };
        if (bestMatch.matchedMethodName != null) {
            errorCode = CANNOT_INVOKE_METHOD;
            errorParams = new Object[] { bestMatch.matchedMethodName };
            int suppliedParamCount = suppliedParamTypes.length;
            int expectedParamCount = bestMatch.methodParamTypes != null ? bestMatch.methodParamTypes.length : 0;
            if (suppliedParamCount != expectedParamCount) {
                errorDetailVariant = "1";
                errorDetailParams = new Object[] { new Integer(suppliedParamCount), new Integer(expectedParamCount) };
            } else {
                String suppliedTypes = bestMatch.listTypes(suppliedParamTypes);
                String convertedTypes = bestMatch.listConvertedTypes();
                String expectedTypes = bestMatch.listExpectedTypes();
                if (expectedTypes != null) {
                    if (suppliedTypes != null) {
                        if (convertedTypes != null) {
                            errorDetailVariant = "2";
                            errorDetailParams = new Object[] { expectedTypes, suppliedTypes, convertedTypes };
                        } else {
                            errorDetailVariant = "3";
                            errorDetailParams = new Object[] { expectedTypes, suppliedTypes };
                        }
                    } else {
                        errorDetailVariant = "4";
                        errorDetailParams = new Object[] { expectedTypes };
                    }
                } else {
                    errorDetailVariant = "5";
                    errorDetailParams = new Object[] { suppliedTypes };
                }
            }
        }
        MessageException ex = new MessageException();
        ex.setMessage(errorCode, errorParams);
        ex.setCode(MessageException.CODE_SERVER_RESOURCE_UNAVAILABLE);
        if (errorDetailVariant != null) ex.setDetails(errorCode, errorDetailVariant, errorDetailParams);
        if (bestMatch.paramTypeConversionFailure != null) ex.setRootCause(bestMatch.paramTypeConversionFailure);
        throw ex;
    }

    /**
     * A utility class to help rank methods in the search
     * for a best match, given a name and collection of
     * input parameters.
     */
    public static class Match {

        /**
         * Constructor.         
         * @param name the name of the method to match
         */
        public Match(String name) {
            this.methodName = name;
        }

        /**
         * Returns true if desired and found method names match.         
         * @return true if desired and found method names match
         */
        public boolean matchedExactlyByName() {
            if (matchedMethodName != null) return matchedMethodName.equals(methodName); else return false;
        }

        /**
         * Returns true if desired and found method names match only when case is ignored.         
         * @return true if desired and found method names match only when case is ignored  
         */
        public boolean matchedLooselyByName() {
            if (matchedMethodName != null) return (!matchedExactlyByName() && matchedMethodName.equalsIgnoreCase(methodName)); else return false;
        }

        /**
         * Lists the classes in the signature of the method matched.
         * @return the classes in the signature of the method matched
         */
        public String listExpectedTypes() {
            return listTypes(methodParamTypes);
        }

        /**
         * Lists the classes corresponding to actual invocation parameters once they have been
         * converted as best they could to match the classes in the invoked method's signature.
         * 
         * @return the classes corresponding to actual invocation parameters once they have been
         * converted as best they could to match the classes in the invoked method's signature
         */
        public String listConvertedTypes() {
            return listTypes(convertedSuppliedTypes);
        }

        /**
         * Creates a string representation of the class names in the array of types passed into
         * this method.
         * 
         * @param types an array of types whose names are to be listed
         * @return a string representation of the class names in the array of types
         */
        public String listTypes(Class[] types) {
            if (types != null && types.length > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < types.length; i++) {
                    if (i > 0) sb.append(", ");
                    Class c = types[i];
                    if (c != null) {
                        if (c.isArray()) {
                            c = c.getComponentType();
                            sb.append(c.getName()).append("[]");
                        } else {
                            sb.append(c.getName());
                        }
                    } else sb.append("null");
                }
                return sb.toString();
            }
            return null;
        }

        final String methodName;

        String matchedMethodName;

        boolean matchedByNumberOfParams;

        int matchedParamCount;

        int exactMatchedParamCount;

        Class[] methodParamTypes;

        Class[] convertedSuppliedTypes;

        Exception paramTypeConversionFailure;
    }
}
