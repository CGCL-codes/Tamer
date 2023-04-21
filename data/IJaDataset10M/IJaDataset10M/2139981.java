package com.objectfab.tools.junitdoclet;

import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import java.util.Properties;
import java.util.LinkedList;

public class DefaultTestingStrategy extends DefaultConfigurableStrategy implements TestingStrategy, JUnitDocletProperties {

    protected static final String TESTSUITE_SUITE_METHOD_NAME = "suite";

    protected static final String JUNIT_TEST_CLASS_NAME = "junit.framework.Test";

    protected static final String ACCESSOR_STARTS_WITH[][] = { { "set", "get" }, { "set", "is" } };

    protected static int INDEX_SET = 0;

    protected static int INDEX_GET = 1;

    private static String[] requiredStrings = null;

    public static final String[] MINIMUM_MARKER_SET = { VALUE_MARKER_IMPORT_BEGIN, VALUE_MARKER_IMPORT_END, VALUE_MARKER_EXTENDS_IMPLEMENTS_BEGIN, VALUE_MARKER_EXTENDS_IMPLEMENTS_END, VALUE_MARKER_CLASS_BEGIN, VALUE_MARKER_CLASS_END };

    public void init() {
        super.init();
        setProperties(null);
    }

    public boolean isTestablePackage(PackageDoc doc, NamingStrategy naming) {
        boolean returnValue;
        returnValue = (doc != null);
        returnValue = returnValue && (naming != null) && !naming.isTestPackageName(doc.name());
        return returnValue;
    }

    public boolean isTestableClass(ClassDoc doc, NamingStrategy naming) {
        boolean returnValue;
        returnValue = (doc != null);
        returnValue = returnValue && !doc.isAbstract();
        returnValue = returnValue && !doc.isInterface();
        returnValue = returnValue && !doc.isProtected();
        returnValue = returnValue && !doc.isPrivate();
        returnValue = returnValue && !isInnerClass(doc);
        returnValue = returnValue && doc.isPublic();
        returnValue = returnValue && !isATest(doc);
        returnValue = returnValue && (naming != null) && !naming.isTestClassName(doc.qualifiedName());
        returnValue = returnValue && !hasSuiteMethod(doc);
        return returnValue;
    }

    public boolean isTestableMethod(MethodDoc doc) {
        boolean returnValue;
        returnValue = (doc != null);
        returnValue = returnValue && !doc.isAbstract();
        returnValue = returnValue && !doc.isProtected();
        returnValue = returnValue && !doc.isPrivate();
        returnValue = returnValue && doc.isPublic();
        return returnValue;
    }

    public boolean codeTestSuite(PackageDoc[] packageDocs, int indexPackage, NamingStrategy naming, StringBuffer newCode, Properties properties) {
        boolean returnValue;
        Properties addProps;
        String template;
        returnValue = (packageDocs != null);
        returnValue = returnValue && (indexPackage >= 0);
        returnValue = returnValue && (indexPackage < packageDocs.length);
        returnValue = returnValue && (naming != null);
        returnValue = returnValue && (newCode != null);
        returnValue = returnValue && (properties != null);
        if (returnValue) {
            returnValue = isTestablePackage(packageDocs[indexPackage], naming);
            if (returnValue) {
                addProps = getTestSuiteProperties(packageDocs, indexPackage, naming, properties);
                template = getTemplate(addProps, "testsuite", addProps.getProperty(TEMPLATE_NAME));
                newCode.append(StringHelper.replaceVariables(template, addProps));
            }
        } else {
            printError("DefaultTestingStrategy.codeTestSuite() parameter error");
        }
        return returnValue;
    }

    public boolean codeTestCase(ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, StringBuffer newCode, Properties properties) {
        boolean returnValue;
        Properties addProps;
        String template;
        returnValue = (classDoc != null);
        returnValue = returnValue && (packageDoc != null);
        returnValue = returnValue && (naming != null);
        returnValue = returnValue && (newCode != null);
        returnValue = returnValue && (properties != null);
        if (returnValue) {
            returnValue = isTestableClass(classDoc, naming);
            if (returnValue) {
                addProps = getTestCaseProperties(classDoc, packageDoc, naming, properties);
                template = getTemplate(addProps, "testcase", addProps.getProperty(TEMPLATE_NAME));
                newCode.append(StringHelper.replaceVariables(template, addProps));
            }
        } else {
            printError("DefaultTestingStrategy.codeTestCase() parameter error");
        }
        return returnValue;
    }

    public boolean codeTest(MethodDoc[] methodDocs, int index, ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, StringBuffer newCode, Properties properties) {
        boolean returnValue;
        Properties addProps;
        String template;
        returnValue = (methodDocs != null);
        returnValue = returnValue && (index >= 0);
        returnValue = returnValue && (index < methodDocs.length);
        returnValue = returnValue && (classDoc != null);
        returnValue = returnValue && (packageDoc != null);
        returnValue = returnValue && (naming != null);
        returnValue = returnValue && (newCode != null);
        returnValue = returnValue && (properties != null);
        if (returnValue) {
            returnValue = isTestableMethod(methodDocs[index]);
            if (returnValue) {
                addProps = getTestProperties(methodDocs, index, classDoc, packageDoc, naming, properties);
                if (addProps != null) {
                    template = getTemplate(addProps, "testmethod", addProps.getProperty(TEMPLATE_NAME));
                    newCode.append(StringHelper.replaceVariables(template, addProps));
                }
            }
        } else {
            printError("DefaultTestingStrategy.codeTestCase() parameter error");
        }
        return returnValue;
    }

    public Properties getTestSuiteProperties(PackageDoc[] packageDocs, int indexPackage, NamingStrategy naming, Properties properties) {
        Properties returnValue = new Properties(properties);
        returnValue.setProperty(TESTSUITE_PACKAGE_NAME, naming.getTestPackageName(packageDocs[indexPackage].name()));
        returnValue.setProperty(TESTSUITE_CLASS_NAME, naming.getTestSuiteName(packageDocs[indexPackage].name()));
        returnValue.setProperty(TEMPLATE_NAME, TEMPLATE_ATTRIBUTE_DEFAULT);
        returnValue.setProperty(TESTSUITE_ADD_TESTSUITES, getTestSuiteAddTestSuites(packageDocs, indexPackage, naming, properties));
        returnValue.setProperty(TESTSUITE_ADD_TESTCASES, getTestSuiteAddTestCases(packageDocs, indexPackage, naming, properties));
        returnValue.setProperty(TESTSUITE_IMPORTS, getTestSuiteImports(packageDocs, indexPackage, naming, properties));
        returnValue.setProperty(PACKAGE_NAME, packageDocs[indexPackage].name());
        return returnValue;
    }

    /**
     * Comment on DBC:<br>
     * \@pre (classDoc != null) && (packageDoc != null) && (naming != null) && (properties != null) <br>
     * \@post return != null <br>
     *
     * @return new Properties instance with all properties for parameter 'properties'
     *           and test case specific properties
     */
    public Properties getTestCaseProperties(ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, Properties properties) {
        Properties returnValue = new Properties(properties);
        returnValue.setProperty(TESTCASE_PACKAGE_NAME, naming.getTestPackageName(packageDoc.name()));
        returnValue.setProperty(TESTCASE_CLASS_NAME, naming.getTestCaseName(classDoc.name()));
        returnValue.setProperty(TESTCASE_INSTANCE_NAME, classDoc.name().toLowerCase());
        returnValue.setProperty(TESTCASE_INSTANCE_TYPE, classDoc.qualifiedName());
        returnValue.setProperty(TESTCASE_TESTMETHODS, getTestMethods(classDoc, packageDoc, naming, returnValue));
        returnValue.setProperty(TESTCASE_METHOD_UNMATCHED, VALUE_METHOD_UNMATCHED_NAME);
        returnValue.setProperty(TEMPLATE_NAME, TEMPLATE_ATTRIBUTE_DEFAULT);
        returnValue.setProperty(PACKAGE_NAME, packageDoc.name());
        returnValue.setProperty(CLASS_NAME, classDoc.name());
        return returnValue;
    }

    private String getTestMethods(ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, Properties properties) {
        StringBuffer sb;
        MethodDoc[] methodDocs;
        methodDocs = classDoc.methods();
        sb = new StringBuffer();
        for (int i = 0; i < methodDocs.length; i++) {
            if (isTestableMethod(methodDocs[i])) {
                codeTest(methodDocs, i, classDoc, packageDoc, naming, sb, properties);
            }
        }
        return sb.toString();
    }

    /**
     * Comment on DBC:<br>
     * \@pre (methodDoc != null) && (classDoc != null) && (packageDoc != null) && (naming != null) && (properties != null) <br>
     *
     * @return if the method specified by 'index' needs a test, new Properties instance with all properties for parameter 'properties'
     *           and test method specific properties;
     *           null if the method specified by 'index' needs no test
     */
    public Properties getTestProperties(MethodDoc[] methodDocs, int index, ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, Properties properties) {
        Properties returnValue = null;
        StringBuffer signature = null;
        Parameter[] parameters = null;
        returnValue = getTestAccessorProperties(methodDocs, index, classDoc, packageDoc, naming, properties);
        if ((returnValue == properties) && (returnValue != null)) {
            if (isFirstTestableMethodWithName(methodDocs, index)) {
                returnValue = new Properties(properties);
                returnValue.setProperty(TESTMETHOD_NAME, naming.getTestMethodName(methodDocs[index].name()));
                returnValue.setProperty(TEMPLATE_NAME, TEMPLATE_ATTRIBUTE_DEFAULT);
                returnValue.setProperty(METHOD_NAME, methodDocs[index].name());
                signature = new StringBuffer("");
                for (int i = 0; i < getNumberOfParameters(methodDocs[index]); i++) {
                    parameters = methodDocs[index].parameters();
                    if (i > 0) {
                        signature.append(", ");
                    }
                    signature.append(parameters[i].typeName());
                }
                returnValue.setProperty(METHOD_SIGNATURE, signature.toString());
            } else {
                returnValue = null;
            }
        }
        return returnValue;
    }

    public String getTestSuiteAddTestSuites(PackageDoc[] packageDocs, int indexPackage, NamingStrategy naming, Properties properties) {
        StringBuffer sb;
        String template;
        Properties addProps;
        PackageDoc[] subPackages;
        sb = new StringBuffer();
        addProps = new Properties(properties);
        template = getTemplate(properties, ADD_TESTSUITE_TO_TESTSUITE, TEMPLATE_ATTRIBUTE_DEFAULT);
        subPackages = getDirectSubPackages(packageDocs, indexPackage);
        for (int i = 0; i < subPackages.length; i++) {
            if (isTestablePackage(subPackages[i], naming)) {
                addProps.setProperty(ADD_TESTSUITE_NAME, naming.getTestSuiteName(subPackages[i].name()));
                addProps.setProperty(TESTSUITE_PACKAGE_NAME, naming.getTestPackageName(subPackages[i].name()));
                sb.append(StringHelper.replaceVariables(template, addProps));
            }
        }
        return sb.toString();
    }

    public String getTestSuiteAddTestCases(PackageDoc[] packageDocs, int indexPackage, NamingStrategy naming, Properties properties) {
        StringBuffer sb;
        String template;
        Properties addProps;
        ClassDoc[] classes;
        sb = new StringBuffer();
        addProps = new Properties(properties);
        template = getTemplate(properties, ADD_TESTCASE_TO_TESTSUITE, TEMPLATE_ATTRIBUTE_DEFAULT);
        classes = packageDocs[indexPackage].ordinaryClasses();
        for (int i = 0; i < classes.length; i++) {
            if (isTestableClass(classes[i], naming)) {
                addProps.setProperty(ADD_TESTCASE_NAME, naming.getTestCaseName(classes[i].name()));
                addProps.setProperty(TESTSUITE_PACKAGE_NAME, naming.getTestPackageName(packageDocs[indexPackage].name()));
                sb.append(StringHelper.replaceVariables(template, addProps));
            }
        }
        return sb.toString();
    }

    public String getTestSuiteImports(PackageDoc[] packageDocs, int indexPackage, NamingStrategy naming, Properties properties) {
        StringBuffer sb;
        String template;
        Properties addProps;
        PackageDoc[] subPackages;
        sb = new StringBuffer();
        addProps = new Properties(properties);
        template = getTemplate(properties, ADD_IMPORT_TESTSUITE, TEMPLATE_ATTRIBUTE_DEFAULT);
        subPackages = getDirectSubPackages(packageDocs, indexPackage);
        for (int i = 0; i < subPackages.length; i++) {
            if (isTestablePackage(subPackages[i], naming)) {
                addProps.setProperty(ADD_TESTSUITE_NAME, naming.getTestSuiteName(subPackages[i].name()));
                addProps.setProperty(TESTSUITE_PACKAGE_NAME, naming.getTestPackageName(subPackages[i].name()));
                sb.append(StringHelper.replaceVariables(template, addProps));
            }
        }
        return sb.toString();
    }

    public boolean isFirstTestableMethodWithName(MethodDoc[] methodDocs, int index) {
        boolean returnValue = true;
        String reference;
        reference = methodDocs[index].name();
        for (int i = 0; (i < index) && returnValue; i++) {
            if (reference.equals(methodDocs[i].name()) && isTestableMethod(methodDocs[i])) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    public int countTestableMethodsWithName(MethodDoc[] methodDocs, String methodName) {
        int returnValue = 0;
        for (int i = 0; (i < methodDocs.length); i++) {
            if (methodName.equals(methodDocs[i].name()) && isTestableMethod(methodDocs[i])) {
                returnValue++;
            }
        }
        return returnValue;
    }

    /**
     * Builds accessor specific properties if the method specified by 'index' is an accessor method.
     *
     * @return if specfied method is an set accessor, returns properties with all properties from
     *         parameter 'properties' and accessor specific properties;
     *         if specfied method is an get accessor, return null;
     *         if  specfied method is not an accessor, returns parameter 'properties' unchanged
     */
    public Properties getTestAccessorProperties(MethodDoc[] methodDocs, int index, ClassDoc classDoc, PackageDoc packageDoc, NamingStrategy naming, Properties properties) {
        Properties returnValue = null;
        String testMethodName;
        String methodName;
        int indexAccessorPair;
        int indexArray;
        String accessedPropertyName;
        String setAccessorName;
        String getAccessorName;
        String testsByType;
        String accessorTypeName;
        Parameter[] parameters;
        methodName = methodDocs[index].name();
        indexAccessorPair = getAccessorPairIndex(methodDocs, index);
        if (indexAccessorPair >= 0) {
            if ((methodName.startsWith(ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_SET])) && (isFirstTestableMethodWithName(methodDocs, index))) {
                accessedPropertyName = getAccessedPropertyName(methodName, indexAccessorPair);
                if ((accessedPropertyName != null) && (accessedPropertyName.length() > 0)) {
                    testMethodName = naming.getTestAccessorName(ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_SET], ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_GET], accessedPropertyName);
                    setAccessorName = ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_SET] + accessedPropertyName;
                    getAccessorName = ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_GET] + accessedPropertyName;
                    parameters = methodDocs[index].parameters();
                    if ((parameters != null) && (parameters.length == 1)) {
                        accessorTypeName = parameters[0].typeName();
                        indexArray = accessorTypeName.indexOf("[]");
                        if (indexArray == -1) {
                            testsByType = getAccessorTestsByType(properties, TEMPLATE_ATTRIBUTE_DEFAULT, accessorTypeName);
                        } else {
                            testsByType = getAccessorTestsByType(properties, TEMPLATE_ATTRIBUTE_ARRAY, accessorTypeName.substring(0, indexArray));
                        }
                        returnValue = new Properties(properties);
                        returnValue.setProperty(ACCESSOR_TESTS, testsByType);
                        returnValue.setProperty(ACCESSOR_NAME, testMethodName);
                        returnValue.setProperty(ACCESSOR_SET_NAME, setAccessorName);
                        returnValue.setProperty(ACCESSOR_GET_NAME, getAccessorName);
                        returnValue.setProperty(ACCESSOR_TYPE_NAME, accessorTypeName);
                        returnValue.setProperty(TESTMETHOD_NAME, testMethodName);
                        returnValue.setProperty(TEMPLATE_NAME, TEMPLATE_ATTRIBUTE_ACCESSOR);
                        returnValue.setProperty(METHOD_NAME, methodDocs[index].name());
                    }
                } else {
                    returnValue = properties;
                }
            }
            if (methodName.startsWith(ACCESSOR_STARTS_WITH[indexAccessorPair][INDEX_GET])) {
                returnValue = null;
            }
        } else {
            returnValue = properties;
        }
        return returnValue;
    }

    /**
     * A method is considered an accessor if (i) method name starts with certain prefixes,
     * (ii) prefix is followed by a property name (that is longer than the empyt string ""),
     * (iii) there are methods with this property name for both 'get' and 'set' prefixes,
     * (iv) number of parameters for the get method is 0 and number of parameter for the set method is 1.
     *
     * @return -1 = not both accessors found or not an accessor method,
     *         0 or above = index of prefix in ACCESSOR_STARTS_WITH method of the method specified by 'index'
     */
    public int getAccessorPairIndex(MethodDoc[] methodDocs, int index) {
        int returnValue = -1;
        String accessedPropertyName;
        String setAccessorName;
        String getAccessorName;
        boolean foundSet = false;
        boolean foundGet = false;
        boolean exactlyOneParamSet = true;
        boolean exactlyZeroParamGet = true;
        if (isTestableMethod(methodDocs[index])) {
            for (int i = 0; (returnValue == -1) && (i < ACCESSOR_STARTS_WITH.length); i++) {
                accessedPropertyName = getAccessedPropertyName(methodDocs[index].name(), i);
                if ((accessedPropertyName != null) && (accessedPropertyName.length() > 0)) {
                    setAccessorName = ACCESSOR_STARTS_WITH[i][INDEX_SET] + accessedPropertyName;
                    getAccessorName = ACCESSOR_STARTS_WITH[i][INDEX_GET] + accessedPropertyName;
                    for (int j = 0; (returnValue == -1) && (j < methodDocs.length); j++) {
                        if (isTestableMethod(methodDocs[j])) {
                            if (getAccessorName.equals(methodDocs[j].name())) {
                                foundGet |= true;
                                exactlyZeroParamGet &= (getNumberOfParameters(methodDocs[j]) == 0);
                            } else if (setAccessorName.equals(methodDocs[j].name())) {
                                foundSet |= true;
                                exactlyOneParamSet &= (getNumberOfParameters(methodDocs[j]) == 1);
                            }
                        }
                    }
                    if (foundGet && foundSet && exactlyOneParamSet && exactlyZeroParamGet) {
                        returnValue = i;
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * Comment on DBC:<br>
     * \@pre methodDoc != null <br>
     */
    private static int getNumberOfParameters(MethodDoc methodDoc) {
        if (methodDoc.parameters() != null) {
            return methodDoc.parameters().length;
        } else {
            return 0;
        }
    }

    /**
     * @return name of accessed property if 'accessorMethodName' starts with an accessor prefix
     *     specified by 'indexAccessorPair' (see field ACCESSOR_STARTS_WITH),
     *     null in all other cases.
     */
    public String getAccessedPropertyName(String accessorMethodName, int indexAccessorPair) {
        String returnValue = null;
        String prefix;
        if ((accessorMethodName != null) && (accessorMethodName.length() > 0)) {
            for (int setOrGet = 0; ((returnValue == null) && (setOrGet < ACCESSOR_STARTS_WITH[indexAccessorPair].length)); setOrGet++) {
                prefix = ACCESSOR_STARTS_WITH[indexAccessorPair][setOrGet];
                if (accessorMethodName.startsWith(prefix)) {
                    returnValue = accessorMethodName.substring(prefix.length());
                }
            }
        }
        return returnValue;
    }

    public String getAccessorTestsByType(Properties properties, String templateAttribute, String type) {
        String returnValue = null;
        String template;
        Properties addProps;
        if (TEMPLATE_ATTRIBUTE_DEFAULT.equals(templateAttribute)) {
            returnValue = properties.getProperty(ACCESSOR_TESTS + "." + type);
        }
        if (returnValue == null) {
            template = getTemplate(properties, ACCESSOR_TESTS, templateAttribute);
            addProps = new Properties(properties);
            addProps.put(ACCESSOR_TYPE_NAME, type);
            returnValue = StringHelper.replaceVariables(template, addProps);
        }
        if (returnValue != null) {
            returnValue = returnValue.trim();
        }
        return returnValue;
    }

    public boolean isInnerClass(ClassDoc doc) {
        boolean returnValue = false;
        if (doc != null) {
            returnValue = (-1 < doc.name().indexOf("."));
        }
        return returnValue;
    }

    public boolean isATest(ClassDoc doc) {
        boolean returnValue = false;
        ClassDoc temp;
        String tempName;
        ClassDoc interfaces[];
        temp = doc;
        while (!returnValue && (temp != null)) {
            tempName = temp.qualifiedName();
            if (tempName.equals(JUNIT_TEST_CLASS_NAME)) {
                returnValue = true;
            } else {
                interfaces = temp.interfaces();
                for (int i = 0; ((interfaces != null) && (i < interfaces.length)); i++) {
                    tempName = interfaces[i].qualifiedName();
                    if (tempName.equals(JUNIT_TEST_CLASS_NAME)) {
                        returnValue = true;
                    }
                }
            }
            temp = temp.superclass();
        }
        return returnValue;
    }

    public boolean hasSuiteMethod(ClassDoc doc) {
        boolean returnValue = false;
        MethodDoc[] methods = doc.methods();
        for (int i = 0; !returnValue && (i < methods.length); i++) {
            MethodDoc method = methods[i];
            returnValue |= TESTSUITE_SUITE_METHOD_NAME.equals(method.name()) && method.isStatic();
        }
        return returnValue;
    }

    public PackageDoc[] getDirectSubPackages(PackageDoc[] packageDocs, int indexCurrentPackage) {
        LinkedList list;
        String subStart;
        String tempPackageName;
        list = new LinkedList();
        subStart = packageDocs[indexCurrentPackage].name() + ".";
        for (int i = 0; i < packageDocs.length; i++) {
            tempPackageName = packageDocs[i].name();
            if ((i != indexCurrentPackage) && tempPackageName.startsWith(subStart) && (-1 == tempPackageName.indexOf(".", subStart.length()))) {
                list.add(packageDocs[i]);
            }
        }
        return (PackageDoc[]) list.toArray(new PackageDoc[0]);
    }

    public boolean isValid(String code) {
        return hasAllRequiredStrings(code) && isValidStructure(code);
    }

    public boolean hasAllRequiredStrings(String code) {
        boolean returnValue = true;
        if (requiredStrings == null) {
            requiredStrings = new String[MINIMUM_MARKER_SET.length];
            for (int i = 0; i < MINIMUM_MARKER_SET.length; i++) {
                requiredStrings[i] = MINIMUM_MARKER_SET[i].trim();
            }
        }
        for (int i = 0; i < requiredStrings.length; i++) {
            if (code.indexOf(requiredStrings[i]) == -1) {
                returnValue = false;
            }
        }
        return returnValue;
    }

    public boolean isValidStructure(String code) {
        boolean returnValue = true;
        int indexBegin;
        int indexEnd;
        int indexContentBegin;
        int indexContentEnd;
        String markDescription;
        if (code != null) {
            indexBegin = code.indexOf(VALUE_MARKER_BEGIN);
            indexEnd = code.indexOf(VALUE_MARKER_END);
            while (returnValue && (indexBegin < indexEnd) && (indexBegin > -1)) {
                markDescription = code.substring(indexBegin + VALUE_MARKER_BEGIN.length(), code.indexOf("\n", indexBegin));
                indexEnd = indexBegin + VALUE_MARKER_BEGIN.length();
                do {
                    indexEnd = code.indexOf(VALUE_MARKER_END + markDescription, indexEnd);
                } while ((indexEnd > 0) && (Character.isWhitespace(code.charAt(indexEnd))));
                if (indexEnd > -1) {
                    indexContentBegin = code.indexOf("\n", indexBegin + VALUE_MARKER_BEGIN.length());
                    indexContentEnd = code.lastIndexOf("\n", indexEnd);
                    if (indexContentBegin < indexContentEnd) {
                        returnValue = isValidStructure(code.substring(indexContentBegin, indexContentEnd));
                    }
                } else {
                    returnValue = false;
                }
                indexBegin = code.indexOf(VALUE_MARKER_BEGIN, indexEnd + 1);
                indexEnd = code.indexOf(VALUE_MARKER_END, indexEnd + 1);
            }
            returnValue = returnValue && (indexBegin * indexEnd > 0);
            returnValue = returnValue && ((indexBegin < 0) || (indexBegin < indexEnd));
        } else {
            printError("DefaultTestingStrategy.isValidStructure() code == null");
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Merges all markers from inCode into inOutCode. In the end all markers from oldCode
     * will be in newCode as well. If nessesary some new generated default content in
     * newCode gets overwritten. If some markers are not in newCode any more, they will
     * be moved to testVault, a special test method.
     *
     * @param inOutCode points to the in-out StringBuffer with the new code
     * @param inCode holds all markers to be merged into to newCode
     * @param fullClassName is used only for the error message, if anything goes wrong.
     * @return true if successfully merged, false if old code contains no JUnitDoclet markers.
     */
    public boolean merge(StringBuffer inOutCode, StringBuffer inCode, String fullClassName) {
        boolean returnValue = true;
        String newContent;
        String oldContent;
        String markDescription;
        String markContent;
        int oldIndexLeft;
        int oldIndexRight;
        int insertFromIndex;
        int insertToIndex;
        StringBuffer unmatched;
        if (inOutCode != null) {
            if (inCode != null) {
                oldContent = inCode.toString();
                unmatched = new StringBuffer();
                oldIndexLeft = oldContent.indexOf(VALUE_MARKER_BEGIN, 0);
                oldIndexRight = oldContent.indexOf("\n", oldIndexLeft) + "\n".length();
                if (isValid(oldContent)) {
                    while ((oldIndexRight > -1) && (oldIndexLeft > -1)) {
                        markDescription = oldContent.substring(oldIndexLeft + VALUE_MARKER_BEGIN.length(), oldIndexRight).trim();
                        oldIndexLeft = oldIndexRight;
                        oldIndexRight = oldContent.indexOf(VALUE_MARKER_END + markDescription, oldIndexLeft);
                        oldIndexRight = oldContent.lastIndexOf("\n", oldIndexRight) + "\n".length();
                        markContent = oldContent.substring(oldIndexLeft, oldIndexRight);
                        newContent = inOutCode.toString();
                        insertFromIndex = 0;
                        do {
                            insertFromIndex = newContent.indexOf(VALUE_MARKER_BEGIN + markDescription, insertFromIndex);
                            if (insertFromIndex > -1) {
                                insertFromIndex = insertFromIndex + VALUE_MARKER_BEGIN.length() + markDescription.length();
                            }
                        } while ((insertFromIndex > -1) && (!Character.isWhitespace(newContent.charAt(insertFromIndex))));
                        if (insertFromIndex > -1) {
                            while ((insertFromIndex - 1 < newContent.length()) && (newContent.charAt(insertFromIndex - 1) != '\n')) {
                                insertFromIndex++;
                            }
                            insertToIndex = newContent.indexOf(VALUE_MARKER_END + markDescription, insertFromIndex);
                        } else {
                            insertToIndex = -1;
                        }
                        while ((insertToIndex > 0) && (newContent.charAt(insertToIndex - 1) == ' ')) {
                            insertToIndex--;
                        }
                        if ((insertFromIndex != -1) && (insertToIndex != -1)) {
                            if (containsCodeOrComment(markContent)) {
                                inOutCode.replace(insertFromIndex, insertToIndex, markContent);
                            }
                        } else {
                            if (containsCodeOrComment(markContent)) {
                                unmatched.append(VALUE_MARKER_BEGIN + markDescription);
                                unmatched.append("\n");
                                unmatched.append(markContent);
                                unmatched.append(VALUE_MARKER_END + markDescription);
                                unmatched.append("\n");
                            }
                        }
                        oldIndexLeft = oldContent.indexOf(VALUE_MARKER_BEGIN, oldIndexRight);
                        oldIndexRight = oldContent.indexOf("\n", oldIndexLeft) + "\n".length();
                    }
                    if (unmatched.length() > 0) {
                        newContent = inOutCode.toString();
                        insertToIndex = newContent.lastIndexOf(VALUE_METHOD_UNMATCHED_NAME);
                        while ((insertToIndex > 0) && (newContent.charAt(insertToIndex - 1) != '\n')) {
                            insertToIndex--;
                        }
                        if (insertToIndex != -1) {
                            inOutCode.insert(insertToIndex, unmatched.toString());
                        }
                    }
                    if (hasUnmatchedMarkers(inOutCode.toString())) {
                        printWarning("Class " + fullClassName + " contains unmatched tests.");
                    }
                } else {
                    printWarning("Class " + fullClassName + " was not generated by JUnitDoclet. It's not overwritten.\n" + "Please rename and start JUnitDoclet again.");
                    returnValue = false;
                }
            }
        } else {
            printError("DefaultTestingStrategy.merge() inOutCode == null");
        }
        return returnValue;
    }

    public boolean containsCodeOrComment(String markContent) {
        boolean returnValue = false;
        char ch;
        if ((markContent != null) && (markContent.length() > 0)) {
            for (int i = 0; (!returnValue && (i < markContent.length())); i++) {
                ch = markContent.charAt(i);
                returnValue = !Character.isWhitespace(ch);
            }
        }
        return returnValue;
    }

    public boolean hasUnmatchedMarkers(String code) {
        boolean returnValue = false;
        int beginUnmatched;
        int endUnmatched;
        int tempUnmatched;
        beginUnmatched = StringHelper.indexOfTwoPartString(code, VALUE_MARKER_METHOD_BEGIN, VALUE_METHOD_UNMATCHED_NAME_MARKER, 0);
        endUnmatched = StringHelper.indexOfTwoPartString(code, VALUE_MARKER_METHOD_END, VALUE_METHOD_UNMATCHED_NAME_MARKER, beginUnmatched);
        if ((beginUnmatched != -1) && (endUnmatched != -1) && (endUnmatched > beginUnmatched)) {
            tempUnmatched = beginUnmatched + VALUE_MARKER_METHOD_BEGIN.length() + VALUE_METHOD_UNMATCHED_NAME.length();
            while ('\n' != code.charAt(tempUnmatched)) {
                tempUnmatched++;
            }
            while ((tempUnmatched < endUnmatched) && (Character.isWhitespace(code.charAt(tempUnmatched)))) {
                tempUnmatched++;
            }
            if (tempUnmatched < endUnmatched) {
                returnValue = true;
            }
        }
        return returnValue;
    }
}
