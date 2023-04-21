package com.halware.nakedide.eclipse.core.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import com.halware.eclipseutil.util.Generics;
import com.halware.eclipseutil.util.StringUtil;
import com.halware.nakedide.eclipse.core.Constants;

public final class MethodUtils {

    public static final LinkedHashSet<String> ALL_PREFIXES = new LinkedHashSet<String>() {

        private static final long serialVersionUID = 1L;

        {
            addAll(Arrays.asList(Constants.COLLECTION_PREFIXES));
            addAll(Arrays.asList(Constants.PROPERTY_PREFIXES));
            addAll(Arrays.asList(Constants.ACTION_PREFIXES));
        }
    };

    private MethodUtils() {
    }

    public static IField checkExists(IField field) {
        return field.exists() ? field : null;
    }

    public static IMethod checkExists(IMethod method) {
        if (method == null) {
            return null;
        }
        return method.exists() ? method : null;
    }

    public static IMethod coalesce(IMethod... methods) {
        for (IMethod method : methods) {
            if (checkExists(method) != null) {
                return method;
            }
        }
        return null;
    }

    public static String accessorMethodNameFor(String propertyName) {
        return MethodUtils.prefixedMethodNameFor(Constants.PREFIX_GET, propertyName);
    }

    public static String mutatorMethodNameFor(String propertyName) {
        return MethodUtils.prefixedMethodNameFor(Constants.PREFIX_SET, propertyName);
    }

    public static String modifyMethodNameFor(String fieldName) {
        return prefixedMethodNameFor(Constants.PREFIX_MODIFY, fieldName);
    }

    public static String clearMethodNameFor(String propertyName) {
        return MethodUtils.prefixedMethodNameFor(Constants.PREFIX_CLEAR, propertyName);
    }

    public static String addToMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_ADD_TO, propertyName);
    }

    public static String removeFromMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_REMOVE_FROM, propertyName);
    }

    public static String disableMethodNameFor(String memberName) {
        return prefixedMethodNameFor(Constants.PREFIX_DISABLE, memberName);
    }

    public static String validateMethodNameFor(String memberName) {
        return prefixedMethodNameFor(Constants.PREFIX_VALIDATE, memberName);
    }

    public static String validateAddToMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_VALIDATE_ADD_TO, propertyName);
    }

    public static String validateRemoveFromMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_VALIDATE_REMOVE_FROM, propertyName);
    }

    public static String hideMethodNameFor(String memberName) {
        return prefixedMethodNameFor(Constants.PREFIX_HIDE, memberName);
    }

    public static String defaultMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_DEFAULT, propertyName);
    }

    public static String choicesMethodNameFor(String propertyName) {
        return prefixedMethodNameFor(Constants.PREFIX_CHOICES, propertyName);
    }

    public static String actionMethodNameFor(String memberName) {
        String prefixedBy = null;
        for (String prefix : Constants.ACTION_PREFIXES) {
            if (isPrefixed(memberName, prefix)) {
                prefixedBy = prefix;
                break;
            }
        }
        return prefixedBy == null ? memberName : unprefixed(memberName, prefixedBy);
    }

    public static String prefixedMethodNameFor(String prefix, IField field) {
        return prefix + MethodUtils.propertyNameUpperCaseFirst(field);
    }

    public static String prefixedMethodNameFor(String prefix, String propertyName) {
        return prefix + MethodUtils.propertyNameUpperCaseFirst(propertyName);
    }

    public static String propertyNameUpperCaseFirst(IField field) {
        return MethodUtils.propertyNameUpperCaseFirst(field.getElementName());
    }

    public static String propertyNameUpperCaseFirst(String propertyName) {
        return MethodUtils.upperCaseFirst(propertyName);
    }

    public static String upperCaseFirst(String val) {
        if (val == null || val.length() == 0) return val;
        String ret = val.substring(0, 1).toUpperCase();
        if (val.length() > 1) ret += val.substring(1);
        return ret;
    }

    public static String lowerCaseFirst(String val) {
        if (val == null || val.length() == 0) return val;
        String ret = val.substring(0, 1).toLowerCase();
        if (val.length() > 1) ret += val.substring(1);
        return ret;
    }

    public static boolean representsProperty(IField field) {
        if (field == null) {
            return false;
        }
        return !representsCollection(field);
    }

    private static IField findFieldWithSameScalarReturnTypeAsPrefixedMethod(IMethod method, String prefix) {
        return checkScalarReturnType(findFieldWithSameReturnTypeAsPrefixedMethod(method, prefix));
    }

    private static IField findFieldWithSameCollectionReturnTypeAsPrefixedMethod(IMethod method, String prefix) {
        return checkCollectionReturnType(findFieldWithSameReturnTypeAsPrefixedMethod(method, prefix));
    }

    /**
	 * Return type can be scalar or vector.
	 * 
	 * @param method
	 * @param prefix
	 * @return
	 */
    private static IField findFieldWithSameReturnTypeAsPrefixedMethod(IMethod method, String prefix) {
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        try {
            String propertyTypeSig = method.getReturnType();
            String fieldTypeSig = candidateField.getTypeSignature();
            if (!propertyTypeSig.equals(fieldTypeSig)) {
                return null;
            }
        } catch (JavaModelException e) {
            return null;
        }
        return candidateField;
    }

    private static IField findFieldWithStringReturnOfPrefixedMethod(IMethod method, String prefix) {
        try {
            String propertyType = method.getReturnType();
            if (!propertyType.equals("QString;")) {
                return null;
            }
        } catch (JavaModelException e) {
            return null;
        }
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        return candidateField;
    }

    private static IField findFieldWithVectorOfSameReturnTypeAsPrefixedMethod(IMethod method, String prefix) {
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        try {
            String propertyType = method.getReturnType();
            String fieldType = candidateField.getTypeSignature();
            String listOfFieldType = "QList<" + fieldType + ">;";
            if (listOfFieldType.equals(propertyType)) {
                return candidateField;
            }
            String arrayOfFieldType = "[QString;";
            if (arrayOfFieldType.equals(propertyType)) {
                return candidateField;
            }
        } catch (JavaModelException e) {
            return null;
        }
        return null;
    }

    private static IField findFieldWithListOfSameParamTypeAsPrefixedMethod(IMethod method, String prefix) {
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        String[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        try {
            String referencedType = parameterTypes[0];
            String fieldType = candidateField.getTypeSignature();
            String listOfFieldType = "QList<" + referencedType + ">;";
            if (!listOfFieldType.equals(fieldType)) {
                return null;
            }
        } catch (JavaModelException e) {
            return null;
        }
        return candidateField;
    }

    private static IField findFieldWithBooleanReturnOfPrefixedMethod(IMethod method, String prefix) {
        try {
            String propertyType = method.getReturnType();
            if (!propertyType.equals("Z")) {
                return null;
            }
        } catch (JavaModelException e) {
            return null;
        }
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        return candidateField;
    }

    private static IField findFieldWithParamSameScalarTypeAsPrefixedMethod(IMethod method, String prefix) {
        return checkScalarReturnType(findFieldWithParamSameTypeAsPrefixedMethod(method, prefix));
    }

    private static IField findFieldWithParamSameCollectionTypeAsPrefixedMethod(IMethod method, String prefix) {
        return checkCollectionReturnType(findFieldWithParamSameTypeAsPrefixedMethod(method, prefix));
    }

    private static IField checkScalarReturnType(IField field) {
        if (field == null) {
            return field;
        }
        try {
            return !isCollectionTypeQSig(field.getTypeSignature()) ? field : null;
        } catch (JavaModelException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static IField checkCollectionReturnType(IField field) {
        if (field == null) {
            return field;
        }
        try {
            return isCollectionTypeQSig(field.getTypeSignature()) ? field : null;
        } catch (JavaModelException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static IField findFieldWithParamSameTypeAsPrefixedMethod(IMethod method, String prefix) {
        String candidateFieldName = unprefixed(method, prefix);
        if (candidateFieldName == null) {
            return null;
        }
        String[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            return null;
        }
        IField candidateField = checkExists(method.getDeclaringType().getField(candidateFieldName));
        if (candidateField == null) {
            return null;
        }
        try {
            String propertyType = parameterTypes[0];
            String fieldType = candidateField.getTypeSignature();
            if (propertyType.equals(fieldType)) {
                return candidateField;
            }
        } catch (JavaModelException e) {
        }
        return null;
    }

    /**
	 * Returns the {@link IField}, if any, for the property represented
	 * by the supplied {@link IMethod}.
	 * 
	 * @param method
	 * @return
	 */
    public static IField fieldForMethodOfProperty(IMethod method) {
        IField field = null;
        field = findFieldWithSameScalarReturnTypeAsPrefixedMethod(method, Constants.PREFIX_GET);
        if (field == null) {
            field = findFieldWithParamSameScalarTypeAsPrefixedMethod(method, Constants.PREFIX_SET);
        }
        if (field == null) {
            field = findFieldWithParamSameScalarTypeAsPrefixedMethod(method, Constants.PREFIX_MODIFY);
        }
        if (field == null) {
            if (returnsVoid(method) && hasZeroParameters(method)) {
                IMethod accessorMethod = findAccessorMethodForPrefixedMethod(method, Constants.PREFIX_CLEAR);
                if (method != null) {
                    return fieldForMethodOfProperty(accessorMethod);
                }
            }
        }
        if (field == null) {
            field = findFieldWithStringReturnOfPrefixedMethod(method, Constants.PREFIX_DISABLE);
        }
        if (field == null) {
            field = findFieldWithStringReturnOfPrefixedMethod(method, Constants.PREFIX_VALIDATE);
            if (field != null) {
                field = findFieldWithParamSameScalarTypeAsPrefixedMethod(method, Constants.PREFIX_VALIDATE);
            }
        }
        if (field == null) {
            field = findFieldWithBooleanReturnOfPrefixedMethod(method, Constants.PREFIX_HIDE);
        }
        if (field == null) {
            field = findFieldWithSameScalarReturnTypeAsPrefixedMethod(method, Constants.PREFIX_DEFAULT);
        }
        if (field == null) {
            field = findFieldWithVectorOfSameReturnTypeAsPrefixedMethod(method, Constants.PREFIX_CHOICES);
        }
        if (field != null) {
            return representsProperty(field) ? field : null;
        }
        return null;
    }

    public static boolean hasZeroParameters(IMethod method) {
        return method.getNumberOfParameters() == 0;
    }

    public static boolean returnsVoid(IMethod method) {
        String returnType;
        try {
            returnType = method.getReturnType();
            return Signature.SIG_VOID.equals(returnType);
        } catch (JavaModelException e) {
            return false;
        }
    }

    /**
     * For clearXxx(), "clear", returns getXxx(): SomeType.
     * 
     * <p>
     * Doesn't validate the type, but does ensure that there are
     * no parameters.
     * 
     * @param method
     * @param prefix
     * @return
     */
    public static IMethod findAccessorMethodForPrefixedMethod(IMethod method, String prefix) {
        String unprefixed = unprefixed(method, prefix);
        String accessorMethod = Constants.PREFIX_GET + StringUtil.titleCase(unprefixed);
        IMethod[] methods;
        try {
            methods = method.getDeclaringType().getMethods();
            for (IMethod candidateMethod : methods) {
                if (candidateMethod.getElementName().equals(accessorMethod) && hasZeroParameters(candidateMethod) && !returnsVoid(candidateMethod)) {
                    return candidateMethod;
                }
            }
        } catch (JavaModelException e) {
        }
        return null;
    }

    public static String calcPropertyName(String newMethodName, IMethod method) {
        return calcCorrespondingFieldName(newMethodName, method, Constants.PROPERTY_PREFIXES);
    }

    public static String calcCollectionName(String newMethodName, IMethod method) {
        return calcCorrespondingFieldName(newMethodName, method, Constants.COLLECTION_PREFIXES);
    }

    private static String calcCorrespondingFieldName(String newMethodName, IMethod method, String[] prefixCandidates) {
        String methodName = method.getElementName();
        for (String prefix : prefixCandidates) {
            if (methodName.startsWith(prefix)) {
                if (!(newMethodName.startsWith(prefix))) {
                    return null;
                }
                return unprefixed(newMethodName, prefix);
            }
        }
        return null;
    }

    public static String getPrefix(IMethod method) {
        return getPrefix(method.getElementName());
    }

    public static String getPrefix(String methodName) {
        for (String prefix : ALL_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    public static String getPropertyPrefix(String methodName) {
        for (String prefix : Constants.PROPERTY_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    public static String getCollectionPrefix(String methodName) {
        for (String prefix : Constants.COLLECTION_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    public static String getActionPrefix(String methodName) {
        for (String prefix : Constants.ACTION_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    /**
	 * Parses type signature of a {@link IMethod} in order to
	 * determine whether it represents a property.
	 *
	 * <p>
	 * 
	 * @param method
	 * @return
	 * @throws JavaModelException 
	 */
    public static boolean representsProperty(IMethod method) {
        return fieldForMethodOfProperty(method) != null;
    }

    /**
	 * As per {@link #unprefixed(String, String)}, but for the
	 * supplied method's element name (@link IMethod#getElementName()}.
	 */
    public static String unprefixed(IMethod method, String prefix) {
        String methodName = method.getElementName();
        return unprefixed(methodName, prefix);
    }

    /**
	 * Strips off prefix and returns the corresponding property name
	 * (lower case first).
	 */
    public static String unprefixed(String methodName, String prefix) {
        if (isPrefixed(methodName, prefix)) {
            String suffix = methodName.substring(prefix.length());
            return lowerCaseFirst(suffix);
        }
        return null;
    }

    private static boolean isPrefixed(String methodName, String prefix) {
        return methodName.startsWith(prefix) && methodName.length() > prefix.length();
    }

    /**
	 * Parses type signature of a {@link IField} in order to
	 * determine whether it represents a collection or not
	 * (using {@link #isCollectionTypeQSig(String)}.
	 * 
	 * @param field
	 * @return
	 */
    public static boolean representsCollection(IField field) {
        if (field == null) {
            return false;
        }
        try {
            return isCollectionTypeQSig(field.getTypeSignature());
        } catch (JavaModelException e) {
            return false;
        }
    }

    public static boolean representsCollection(IMethod method) {
        return fieldForMethodOfCollection(method) != null;
    }

    /**
	 * Returns the {@link IField}, if any, for the collection represented
	 * by the supplied {@link IMethod}.
	 * 
	 * @param method
	 * @return
	 */
    public static IField fieldForMethodOfCollection(IMethod method) {
        IField field = null;
        field = findFieldWithSameCollectionReturnTypeAsPrefixedMethod(method, Constants.PREFIX_GET);
        if (field == null) {
            field = findFieldWithParamSameCollectionTypeAsPrefixedMethod(method, Constants.PREFIX_SET);
        }
        if (field == null) {
            field = findFieldWithListOfSameParamTypeAsPrefixedMethod(method, Constants.PREFIX_ADD_TO);
        }
        if (field == null) {
            field = findFieldWithListOfSameParamTypeAsPrefixedMethod(method, Constants.PREFIX_REMOVE_FROM);
        }
        if (field == null) {
            field = findFieldWithStringReturnOfPrefixedMethod(method, Constants.PREFIX_DISABLE);
        }
        if (field == null) {
            field = findFieldWithStringReturnOfPrefixedMethod(method, Constants.PREFIX_VALIDATE_ADD_TO);
            if (field != null) {
                field = findFieldWithListOfSameParamTypeAsPrefixedMethod(method, Constants.PREFIX_VALIDATE_ADD_TO);
            }
        }
        if (field == null) {
            field = findFieldWithStringReturnOfPrefixedMethod(method, Constants.PREFIX_VALIDATE_REMOVE_FROM);
            if (field != null) {
                field = findFieldWithListOfSameParamTypeAsPrefixedMethod(method, Constants.PREFIX_VALIDATE_REMOVE_FROM);
            }
        }
        if (field == null) {
            field = findFieldWithBooleanReturnOfPrefixedMethod(method, Constants.PREFIX_HIDE);
        }
        if (field != null) {
            return representsCollection(field) ? field : null;
        }
        return null;
    }

    /**
	 * Whether the supplied (unresolved) type signature represents 
     * a collection or not.
	 * 
	 * <p>
	 * For example, the following would be considered as collections of
	 * <tt>Order</tt>:
	 * <ul>
	 * <li> <tt>QList&lt;QOrder;>;</tt>
	 * <li> <tt>Qjava.util.Set&lt;QOrder;>;</tt>
	 * <li> <tt>QMap&lt;Qcom.mycompany.Order;>;</tt>
	 * </ul>
	 * and would therefore <tt>true</tt>.
	 * 
	 * @param field
	 * @return
	 */
    public static boolean isCollectionTypeQSig(String typeSignature) {
        Matcher matcher = Constants.COLLECTION_TYPE_UNRESOLVED_SIGNATURE_PATTERN.matcher(typeSignature);
        if (!matcher.matches()) {
            return false;
        }
        String candidateCollectionType = matcher.group(1);
        return isCollectionType(candidateCollectionType);
    }

    /**
	 * Parses type signature of a {@link IField} (presumed to represent
	 * a collection) and returns the referenced type.
	 * 
	 * <p>
	 * For example, the following would be considered as collections of
	 * <tt>Order</tt>:
	 * <ul>
	 * <li> <tt>QList&lt;QOrder;>;</tt>
	 * <li> <tt>Qjava.util.Set&lt;QOrder;>;</tt>
	 * <li> <tt>QMap&lt;Qcom.mycompany.Order;>;</tt>
	 * </ul>
	 * and would therefore return <tt>Order</tt>.
	 * 
	 * <p>
	 * If not a collection type signature, then returns <tt>null</tt>.
	 * 
	 * @param field
	 * @return
	 */
    public static String collectionType(IField field) {
        try {
            return collectionType(field.getTypeSignature());
        } catch (JavaModelException e) {
            return null;
        }
    }

    /**
     * Returns the type of a collection, eg <tt>Order</tt> within
     * <tt>Set&lt;Order></tt>.
     * 
     * @param typeSignature
     * @return
     */
    public static String collectionType(String typeSignature) {
        Matcher matcher = Constants.COLLECTION_TYPE_UNRESOLVED_SIGNATURE_PATTERN.matcher(typeSignature);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(2);
    }

    /**
	 * Similar to {@link #collectionType(String)}, but returns the
	 * referenced type <i>signature</i>.
	 * 
	 * @param typeSignature
	 * @return
	 */
    public static String collectionTypeQSig(IField field) {
        try {
            return collectionTypeQSig(field.getTypeSignature());
        } catch (JavaModelException e) {
            return null;
        }
    }

    public static String collectionTypeQSig(String typeSignature) {
        String referencedType = collectionType(typeSignature);
        if (referencedType == null) {
            return null;
        }
        return toUnresolvedSignature(referencedType);
    }

    public static boolean isBuiltInValueType(String candidateValueType) {
        if (candidateValueType == null) {
            return false;
        }
        candidateValueType = stripGeneric(candidateValueType);
        return Constants.BUILT_IN_VALUE_TYPES.containsKey(candidateValueType);
    }

    public static boolean isBuiltInValueType(MethodDeclaration methodDeclaration) {
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return false;
        }
        ITypeBinding returnType = methodBinding.getReturnType();
        if (returnType == null) {
            return false;
        }
        return isBuiltInValueType(returnType.getQualifiedName());
    }

    public static boolean isEntityType(MethodDeclaration methodDeclaration) {
        return hasAnnotationOnReturnType(methodDeclaration, "javax.persistence.Entity");
    }

    public static boolean isValueType(MethodDeclaration methodDeclaration) {
        return hasAnnotationOnReturnType(methodDeclaration, "org.nakedobjects.applib.annotation.Value");
    }

    private static boolean hasAnnotationOnReturnType(MethodDeclaration methodDeclaration, String annotationTypeName) {
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return false;
        }
        return TypeUtils.hasAnnotation(methodBinding.getReturnType(), annotationTypeName);
    }

    public static boolean isStringValueType(MethodDeclaration methodDeclaration) {
        IMethodBinding methodBinding = methodDeclaration.resolveBinding();
        if (methodBinding == null) {
            return false;
        }
        ITypeBinding returnType = methodBinding.getReturnType();
        if (returnType == null) {
            return false;
        }
        return "java.lang.String".equals(returnType.getQualifiedName());
    }

    public static boolean isCollectionType(String candidateCollectionType) {
        if (candidateCollectionType == null) {
            return false;
        }
        candidateCollectionType = stripGeneric(candidateCollectionType);
        return Constants.COLLECTION_TYPES.containsKey(candidateCollectionType);
    }

    private static String stripGeneric(String candidateCollectionType) {
        String[] split = candidateCollectionType.split("[<]");
        return split[0];
    }

    public static boolean representsAction(IMethod method) {
        return !representsCollection(method) && !representsProperty(method);
    }

    public static String toUnresolvedSignature(String typeName) {
        return "Q" + typeName + ";";
    }

    @SuppressWarnings("unused")
    private static String getFieldName(FieldDeclaration field) {
        List<VariableDeclarationFragment> fragments = Generics.asT(field.fragments());
        if (fragments.size() == 0) {
            return null;
        }
        return fragments.get(0).getName().getFullyQualifiedName();
    }

    public static boolean in(String str, String[] strings) {
        for (String strCandidate : strings) {
            if (strCandidate.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPublic(MethodDeclaration methodDeclaration) {
        if ((methodDeclaration.getModifiers() & Flags.AccPublic) == Flags.AccPublic) {
            return true;
        }
        return false;
    }
}
