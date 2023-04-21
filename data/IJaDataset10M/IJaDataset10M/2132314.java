package org.jmlnitrate.transformation.inbound;

import java.io.BufferedReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmlnitrate.transformation.BaseTransformation;
import org.jmlnitrate.transformation.TransformationParameter;
import org.jmlnitrate.util.CacheManager;

/**
 *  This class represents the Base class for <b>ALL</b> {@link
 *  InboundTransformation}.
 *
 * @author  Arthur Copeland
 * @version  $Revision: 3 $
 */
public abstract class BaseInboundTransformation extends BaseTransformation implements InboundTransformation {

    /**
     *  Holds the logger
     */
    private static final Log logger = LogFactory.getLog(BaseInboundTransformation.class);

    /**
     *  Holds the buffer size in bytes 94 bytes
     */
    private final int BUFFER_SIZE = 94 * 1;

    /**
     *  Default Ctor.
     */
    public BaseInboundTransformation() {
        super();
    }

    /**
     *  Retrieves the Constructor Array from the raw string request. The request of
     *  the String must be: <br>
     *  <pre>
     *  null - if empty / default ctor
     *
     *  class type for ctor ^
     * </pre>
     *
     * @param  ctor the Raw Ctor Request to convert
     * @return  an Object Array in the valid format for the {@link
     *      org.jmlnitrate.core.Kernel#createKernelProcess(String, String,
     *      Object[], Object[])}
     * @throws  Exception if error happens
     */
    protected Object[] getConstructor(String ctor) throws Exception {
        Object[] ctorArray = new Object[2];
        if (ctor == null || ctor.equals("")) {
            ctor = null;
        } else {
            throw new UnsupportedOperationException("Not Yet implemented....");
        }
        return ctorArray;
    }

    /**
     *  Retrieves the Method Array to execute from a method arguments arrays
     *  describing the Methods. <br>
     *  Method Array Format:
     *  <ol>
     *    <li> **Method Name - String <b>Mandatory</b>
     *    <li> **Static method - Boolean <b>Mandatory</b>
     *    <li> Method Argument Type - String Optional [0..*]
     *  </ol>
     *  <br>
     *  Method Argument Array Format:
     *  <ol>
     *    <li>
     *  </ol>
     *
     *
     * @param  method Array describing the method to create
     * @param  arguments Array of arguments needed for the Method
     * @return  an Object Array in the valid format for the {@link
     *      org.jmlnitrate.core.Kernel#createKernelProcess(String, String,
     *      Object[], Object[])}
     * @throws  Exception if error happens
     */
    protected Object[] getMethod(Object[] method, Object[] arguments) throws Exception {
        Object[] executeMethod = new Object[4];
        if (method == null || method.length < 2 || (arguments != null && arguments.length < 1)) {
            logger.error("arguments must not be null....");
            throw new NullPointerException("arguments must not be null....");
        }
        String methodName = (String) method[0];
        Boolean staticMethod = Boolean.valueOf((String) method[1]);
        if (method.length == 2) {
            executeMethod[0] = methodName;
            executeMethod[1] = null;
            executeMethod[2] = null;
            executeMethod[3] = staticMethod;
        } else {
            int typeSize = method.length;
            Class[] methodTypes = new Class[typeSize - 2];
            for (int t = 2, mt = 0; t < typeSize; t++, mt++) {
                methodTypes[mt] = getClass((String) method[t]);
            }
            int argSize = arguments.length;
            Object[] methodArgs = new Object[argSize];
            boolean isArray = false;
            for (int i = 0; i < argSize; i++) {
                Object[] parsedArgValues = (Object[]) arguments[i];
                if (parsedArgValues.length == 0) {
                    methodArgs[i] = null;
                } else {
                    isArray = parsedArgValues[0].getClass().isArray();
                    if (isArray) {
                        int asize = parsedArgValues.length;
                        Object array = Array.newInstance(methodTypes[i], asize);
                        methodTypes[i] = array.getClass();
                        for (int a = 0; a < asize; a++) {
                            Object value = null;
                            Object[] aparams = (Object[]) parsedArgValues[a];
                            int apsize = aparams.length;
                            if (apsize > 1) {
                                Object[] actorArgs = { aparams[0] };
                                Class[] actorTypes = { getClass((String) aparams[2]) };
                                Class actorClass = getClass((String) aparams[1]);
                                Constructor actor = CacheManager.getCtor(actorClass, actorTypes);
                                value = actor.newInstance(actorArgs);
                            } else {
                                value = aparams[0];
                            }
                            Array.set(array, a, value);
                        }
                        methodArgs[i] = array;
                    } else {
                        int psize = parsedArgValues.length;
                        if (psize > 1) {
                            Object[] ctorArgs = { parsedArgValues[0] };
                            Class[] ctorTypes = { getClass((String) parsedArgValues[2]) };
                            Class ctorClass = getClass((String) parsedArgValues[1]);
                            Constructor ctor = CacheManager.getCtor(ctorClass, ctorTypes);
                            methodArgs[i] = ctor.newInstance(ctorArgs);
                        } else {
                            Object pvalue = parsedArgValues[0];
                            methodArgs[i] = pvalue;
                        }
                    }
                }
            }
            executeMethod[0] = methodName;
            executeMethod[1] = methodTypes;
            executeMethod[2] = methodArgs;
            executeMethod[3] = staticMethod;
        }
        return executeMethod;
    }

    /**
     *  Retrieves the Method Array from the raw string request. The request of the
     *  String must be: <br>
     *  <pre>
     * </pre>
     *
     * @param  method the Raw Method Request to convert
     * @return  an Object Array in the valid format for the {@link
     *      org.jmlnitrate.core.Kernel#createKernelProcess(String, String,
     *      Object[], Object[])}
     * @throws  Exception if error happens
     */
    protected Object[] getMethod(String method) throws Exception {
        if (method == null || method.equals("")) {
            logger.error("Parameter " + TransformationParameter.METHOD.getName() + "must be supplied....");
            throw new NullPointerException("Parameter " + TransformationParameter.METHOD.getName() + " must be supplied....");
        }
        return parseParameterRequest(method);
    }

    /**
     *  Retrieves the Method Array from the raw string request. The request of the
     *  String must be: <br>
     *  <pre>
     * </pre>
     *
     * @param  arguments The Array of Arguments needed for the Method
     * @return  an Object Array in the valid format for the {@link
     *      org.jmlnitrate.core.Kernel#createKernelProcess(String, String,
     *      Object[], Object[])}
     * @throws  Exception if error happens
     */
    protected Object[] getMethodArguments(Object[] arguments) throws Exception {
        if ((arguments != null && arguments.length != 0) && arguments.length < 1) {
            logger.error("Parameter " + TransformationParameter.METHOD_ARGUMENT.getName() + "must be supplied....");
            throw new NullPointerException("Parameter " + TransformationParameter.METHOD_ARGUMENT.getName() + " must be supplied....");
        }
        if (arguments == null || (arguments != null && arguments.length == 0)) {
            return null;
        } else {
            int size = arguments.length;
            Object[] args = new Object[size];
            for (int i = 0; i < size; i++) {
                Object arg = arguments[i];
                if (arg.getClass().isArray()) {
                    Object[] argArray = (Object[]) arg;
                    int as = argArray.length;
                    if (as == 1) {
                        args[i] = parseParameterRequest((String) argArray[0]);
                    } else {
                        Object[] ta = new Object[as];
                        for (int a = 0; a < as; a++) {
                            ta[a] = parseParameterRequest((String) argArray[a]);
                        }
                        args[i] = ta;
                    }
                } else {
                    args[i] = parseParameterRequest((String) arg);
                }
            }
            return args;
        }
    }

    /**
     *  Returns the Class Type for a Primitive type.
     *
     * @param  classType Description of the Parameter
     * @return  the Class Type
     * @exception  Exception Description of the Exception
     */
    private Class getClass(String classType) throws Exception {
        TransformationParameter classParameter = TransformationParameter.getByName(classType);
        Class type = null;
        if (classParameter != null) {
            if (classParameter.equals(TransformationParameter.INT)) {
                type = Integer.TYPE;
            } else if (classParameter.equals(TransformationParameter.LONG)) {
                type = Long.TYPE;
            } else if (classParameter.equals(TransformationParameter.FLOAT)) {
                type = Float.TYPE;
            } else if (classParameter.equals(TransformationParameter.DOUBLE)) {
                type = Double.TYPE;
            } else if (classParameter.equals(TransformationParameter.BOOLEAN)) {
                type = Boolean.TYPE;
            } else if (classParameter.equals(TransformationParameter.CHAR)) {
                type = Character.TYPE;
            } else if (classParameter.equals(TransformationParameter.BYTE)) {
                type = Byte.TYPE;
            } else {
                logger.error("Not a valid Class Type...");
                throw new IllegalArgumentException("Not a valid Class Type...");
            }
        } else {
            type = CacheManager.getClass(classType);
        }
        return type;
    }

    /**
     *  Returns an Array of a Class Type
     *
     * @return  and Array
     */
    private Object getArray() {
        return null;
    }

    /**
     *  Parses a Parameter Request String and return a array of the tokens in the
     *  string. The default delimeters are ^ and ;
     *
     * @param  rawRequest the Parameter Request
     * @return  Array of the tokens
     * @throws  Exception if an error happens
     */
    private Object[] parseParameterRequest(String rawRequest) throws Exception {
        StreamTokenizer tokenStream = new StreamTokenizer(new BufferedReader(new StringReader(rawRequest), BUFFER_SIZE));
        tokenStream.resetSyntax();
        tokenStream.lowerCaseMode(false);
        tokenStream.eolIsSignificant(false);
        tokenStream.wordChars(32, 58);
        tokenStream.wordChars(60, 93);
        tokenStream.wordChars(95, 126);
        tokenStream.wordChars(10, 10);
        tokenStream.wordChars(13, 13);
        tokenStream.quoteChar(94);
        ArrayList tokens = new ArrayList();
        int result = tokenStream.ttype;
        while (result != StreamTokenizer.TT_EOF) {
            result = tokenStream.nextToken();
            switch(result) {
                case StreamTokenizer.TT_EOF:
                    break;
                default:
                    tokens.add(tokenStream.sval);
                    break;
            }
        }
        return tokens.toArray();
    }
}
