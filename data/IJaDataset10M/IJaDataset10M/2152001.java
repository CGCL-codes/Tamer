package edu.ucsb.ccs.jcontractor.extras.doclet;

import barat.*;
import barat.reflect.*;
import barat.collections.*;
import com.sun.tools.doclets.standard.*;
import com.sun.tools.doclets.*;
import com.sun.javadoc.*;
import java.io.*;
import java.util.*;

/**
 * Provides utility methods for use by the jContractorDoclet. Not intended to be
 * instantiated.
 *
 * @author David P. White
 * @version %I%, %G%
 *
 */
class jContractorDocletUtil {

    private static jContractorSourcePath sourcePath = null;

    private jContractorDocletUtil() {
    }

    /**
     * @return Singleton source path object.
     */
    static jContractorSourcePath getSourcePath() {
        if (null != sourcePath) {
            return sourcePath;
        }
        String[][] options = jContractorDoclet.getOptions();
        String s = null;
        for (int i = 0; i < options.length; i++) {
            if ("-sourcepath".equals(options[i][0])) {
                s = options[i][1];
                sourcePath = new jContractorSourcePath(s);
                break;
            }
        }
        if (null == sourcePath) {
            sourcePath = new jContractorSourcePath();
        }
        return sourcePath;
    }

    private static File findContractClass(ClassDoc classDoc) {
        String s = classDoc.position().file().getAbsoluteFile().getParentFile().getAbsolutePath();
        return findFile(s + File.separator + classDoc.name() + "_CONTRACT.java");
    }

    /**
     * Finds the contract class for the specified class using the source path.
     *
     *
     * @param className Qualified class name
     *
     * @return null if none can be located.
     */
    static File findContractClass(String className) {
        if (null == className) {
            return null;
        }
        ClassDoc[] specifiedClasses = jContractorDoclet.getRoot().specifiedClasses();
        for (int i = 0; i < specifiedClasses.length; i++) {
            if (className.equals(specifiedClasses[i].qualifiedName())) {
                return findContractClass(specifiedClasses[i]);
            }
        }
        String contractClassName = className + "_CONTRACT";
        String contractClassFileName = DirectoryManager.getPath(contractClassName) + ".java";
        return findFileOnSourcePath(contractClassFileName);
    }

    private static File findFile(String fullyQualifiedFileName) {
        File f = new File(fullyQualifiedFileName);
        if (f.exists() && f.isFile()) {
            return f;
        } else {
            return null;
        }
    }

    private static File findFileOnSourcePath(String fileName) {
        if (null == fileName) {
            return null;
        }
        File[] pathElements = getSourcePath().pathElements();
        for (int i = 0; i < pathElements.length; i++) {
            File f = new File(pathElements[i], fileName);
            if (f.exists() && f.isFile()) {
                return f;
            }
        }
        return null;
    }

    /**
     * @return An array identical to that specified less any classes with
     * names ending in _CONTRACT
     */
    static ClassDoc[] removeContractClasses(ClassDoc[] arr) {
        ClassDoc[] result = null;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (isContractClass(arr[i])) {
                count++;
            }
        }
        result = new ClassDoc[arr.length - count];
        count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (isContractClass(arr[i])) {
                continue;
            }
            result[count] = arr[i];
            count++;
        }
        return result;
    }

    /**
     * @return True if the method's name ends with "_Precondition", "_Invairant",
     * or "_PostCondition". Otherwise False.
     */
    static boolean isContractMethod(ExecutableMemberDoc method) {
        boolean result = false;
        if (method.qualifiedName().endsWith("_Precondition")) {
            result = true;
        }
        if (method.qualifiedName().endsWith("_Postcondition")) {
            result = true;
        }
        if (method.qualifiedName().endsWith("_Invariant")) {
            result = true;
        }
        return result;
    }

    /**
     * @return True if the class' name ends with "_CONTRACT", else False.
     */
    static boolean isContractClass(ClassDoc classdoc) {
        return classdoc.qualifiedName().endsWith("_CONTRACT");
    }

    /**
     * @return The parsed implementation of a class' jcontractor invariant
     * method or null if none is found.
     */
    static ConcreteMethod getClassInvariant(ConcreteMethodList list) {
        return findConditionMethod(false, false, "_Invariant()", list);
    }

    /**
     * Searchs for and returns the parsed implementation of a precondition method which
     * matches the specified characterisitics.
     *
     * @param isStatic Must the precondition method be static?
     * @param isPrivate Must the precondition method be private?
     * @param methodName The name of the method for which we are trying to find a precondtion
     * @param methodSignature The signature of the method for which we are trying
     * to find a precondition
     * @param list The set of parsed implementations of the methods in a given class
     *
     * @return null if none is found.
     */
    static ConcreteMethod getMethodPrecondition(boolean isStatic, boolean isPrivate, String methodName, String methodSignature, ConcreteMethodList list) {
        String preconditionSignature = methodName + "_Precondition" + methodSignature;
        return findConditionMethod(isStatic, isPrivate, preconditionSignature, list);
    }

    /**
     * Searchs for and returns the parsed implementation of a postcondition method which
     * matches the specified characterisitics.
     *
     * @param isStatic Must the precondition method be static?
     * @param isPrivate Must the precondition method be private?
     * @param methodName The name of the method for which we are trying to find a precondtion
     * @param methodSignature The signature of the method for which we are trying
     * to find a precondition
     * @param list The set of parsed implementations of the methods in a given class
     *
     * @return null if none is found.
     */
    static ConcreteMethod getMethodPostcondition(boolean isStatic, boolean isPrivate, String methodType, String methodName, String methodSignature, ConcreteMethodList list) {
        methodType = convertPrimative(methodType);
        if ("()".equals(methodSignature)) {
            methodSignature = "(" + methodType + ")";
        } else {
            int i = methodSignature.indexOf(')');
            methodSignature = methodSignature.substring(0, i) + "," + methodType + ")";
        }
        String postconditionSignature = methodName + "_Postcondition" + methodSignature;
        return findConditionMethod(isStatic, isPrivate, postconditionSignature, list);
    }

    private static ConcreteMethod findConditionMethod(boolean isStatic, boolean isPrivate, String methodSignature, ConcreteMethodList list) {
        ConcreteMethodIterator iterator = list.iterator();
        if (null != iterator) {
            while (iterator.hasNext()) {
                ConcreteMethod method = iterator.next();
                String s = method.getName();
                String t = buildParameters(method);
                if (methodSignature.equals(s + t)) {
                    if (!"boolean".equals(method.getResultType().qualifiedName())) {
                        continue;
                    }
                    if (isStatic) {
                        if (!method.isStatic()) {
                            continue;
                        } else {
                            if (method.isStatic()) {
                                continue;
                            }
                        }
                    }
                    if (isPrivate) {
                        if (!method.isPrivate()) {
                            continue;
                        } else {
                            if (!method.isProtected()) {
                                continue;
                            }
                        }
                    }
                    return method;
                }
            }
        }
        return null;
    }

    private static String buildParameters(ConcreteMethod method) {
        boolean firstTime = true;
        String result = "(";
        ParameterList list = method.getParameters();
        barat.reflect.Parameter[] parameters = list.toArray();
        for (int i = 0; i < parameters.length; i++) {
            if (!firstTime) {
                result = result + ",";
            } else {
                firstTime = false;
            }
            result = result + parameters[i].getType().qualifiedName();
        }
        return result + ")";
    }

    private static String convertPrimative(String type) {
        if ("void".equals(type)) {
            return Void.class.getName();
        }
        return type;
    }

    /**
     * @return As a string less some minor editing/formatting (in the case of a one-line
     * method only), the source code of the specified method
     */
    static String getMethodBody(ConcreteMethod method) {
        jContractorOutputVisitor visitor = new jContractorOutputVisitor();
        Block block = method.getBody();
        block.accept(visitor);
        String s = visitor.toString().trim();
        s = s.substring(1).trim();
        AStatementList statementList = block.getStatements();
        if (1 == statementList.size()) {
            int i = s.indexOf("return");
            if (i != -1) {
                s = s.substring(i + 7).trim();
            }
            i = s.lastIndexOf(";");
            if (i != -1) {
                s = s.substring(0, i).trim();
            }
        }
        return s;
    }
}
