package org.eclipse.jdt.internal.compiler.lookup;

import java.util.ArrayList;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.util.SimpleLookupTable;

public class BinaryTypeBinding extends ReferenceBinding {

    protected ReferenceBinding superclass;

    protected ReferenceBinding enclosingType;

    protected ReferenceBinding[] superInterfaces;

    protected FieldBinding[] fields;

    protected MethodBinding[] methods;

    protected ReferenceBinding[] memberTypes;

    protected TypeVariableBinding[] typeVariables;

    protected LookupEnvironment environment;

    protected SimpleLookupTable storedAnnotations = null;

    static Object convertMemberValue(Object binaryValue, LookupEnvironment env, char[][][] missingTypeNames) {
        if (binaryValue == null) return null;
        if (binaryValue instanceof Constant) return binaryValue;
        if (binaryValue instanceof ClassSignature) return env.getTypeFromSignature(((ClassSignature) binaryValue).getTypeName(), 0, -1, false, null, missingTypeNames);
        if (binaryValue instanceof IBinaryAnnotation) return createAnnotation((IBinaryAnnotation) binaryValue, env, missingTypeNames);
        if (binaryValue instanceof EnumConstantSignature) {
            EnumConstantSignature ref = (EnumConstantSignature) binaryValue;
            ReferenceBinding enumType = (ReferenceBinding) env.getTypeFromSignature(ref.getTypeName(), 0, -1, false, null, missingTypeNames);
            enumType = (ReferenceBinding) resolveType(enumType, env, false);
            return enumType.getField(ref.getEnumConstantName(), false);
        }
        if (binaryValue instanceof Object[]) {
            Object[] objects = (Object[]) binaryValue;
            int length = objects.length;
            if (length == 0) return objects;
            Object[] values = new Object[length];
            for (int i = 0; i < length; i++) values[i] = convertMemberValue(objects[i], env, missingTypeNames);
            return values;
        }
        throw new IllegalStateException();
    }

    static AnnotationBinding createAnnotation(IBinaryAnnotation annotationInfo, LookupEnvironment env, char[][][] missingTypeNames) {
        IBinaryElementValuePair[] binaryPairs = annotationInfo.getElementValuePairs();
        int length = binaryPairs == null ? 0 : binaryPairs.length;
        ElementValuePair[] pairs = length == 0 ? Binding.NO_ELEMENT_VALUE_PAIRS : new ElementValuePair[length];
        for (int i = 0; i < length; i++) pairs[i] = new ElementValuePair(binaryPairs[i].getName(), convertMemberValue(binaryPairs[i].getValue(), env, missingTypeNames), null);
        char[] typeName = annotationInfo.getTypeName();
        ReferenceBinding annotationType = env.getTypeFromConstantPoolName(typeName, 1, typeName.length - 1, false, missingTypeNames);
        return new UnresolvedAnnotationBinding(annotationType, pairs, env);
    }

    public static AnnotationBinding[] createAnnotations(IBinaryAnnotation[] annotationInfos, LookupEnvironment env, char[][][] missingTypeNames) {
        int length = annotationInfos == null ? 0 : annotationInfos.length;
        AnnotationBinding[] result = length == 0 ? Binding.NO_ANNOTATIONS : new AnnotationBinding[length];
        for (int i = 0; i < length; i++) result[i] = createAnnotation(annotationInfos[i], env, missingTypeNames);
        return result;
    }

    public static TypeBinding resolveType(TypeBinding type, LookupEnvironment environment, boolean convertGenericToRawType) {
        switch(type.kind()) {
            case Binding.PARAMETERIZED_TYPE:
                ((ParameterizedTypeBinding) type).resolve();
                break;
            case Binding.WILDCARD_TYPE:
            case Binding.INTERSECTION_TYPE:
                return ((WildcardBinding) type).resolve();
            case Binding.ARRAY_TYPE:
                resolveType(((ArrayBinding) type).leafComponentType, environment, convertGenericToRawType);
                break;
            case Binding.TYPE_PARAMETER:
                ((TypeVariableBinding) type).resolve();
                break;
            case Binding.GENERIC_TYPE:
                if (convertGenericToRawType) return environment.convertUnresolvedBinaryToRawType(type);
                break;
            default:
                if (type instanceof UnresolvedReferenceBinding) return ((UnresolvedReferenceBinding) type).resolve(environment, convertGenericToRawType);
                if (convertGenericToRawType) return environment.convertUnresolvedBinaryToRawType(type);
                break;
        }
        return type;
    }

    /**
 * Default empty constructor for subclasses only.
 */
    protected BinaryTypeBinding() {
    }

    /**
 * Standard constructor for creating binary type bindings from binary models (classfiles)
 * @param packageBinding
 * @param binaryType
 * @param environment
 */
    public BinaryTypeBinding(PackageBinding packageBinding, IBinaryType binaryType, LookupEnvironment environment) {
        this.compoundName = CharOperation.splitOn('/', binaryType.getName());
        computeId();
        this.tagBits |= TagBits.IsBinaryBinding;
        this.environment = environment;
        this.fPackage = packageBinding;
        this.fileName = binaryType.getFileName();
        char[] typeSignature = environment.globalOptions.sourceLevel >= ClassFileConstants.JDK1_5 ? binaryType.getGenericSignature() : null;
        this.typeVariables = typeSignature != null && typeSignature.length > 0 && typeSignature[0] == '<' ? null : Binding.NO_TYPE_VARIABLES;
        this.sourceName = binaryType.getSourceName();
        this.modifiers = binaryType.getModifiers();
        if ((binaryType.getTagBits() & TagBits.HierarchyHasProblems) != 0) this.tagBits |= TagBits.HierarchyHasProblems;
        if (binaryType.isAnonymous()) {
            this.tagBits |= TagBits.AnonymousTypeMask;
        } else if (binaryType.isLocal()) {
            this.tagBits |= TagBits.LocalTypeMask;
        } else if (binaryType.isMember()) {
            this.tagBits |= TagBits.MemberTypeMask;
        }
        char[] enclosingTypeName = binaryType.getEnclosingTypeName();
        if (enclosingTypeName != null) {
            this.enclosingType = environment.getTypeFromConstantPoolName(enclosingTypeName, 0, -1, true, null);
            this.tagBits |= TagBits.MemberTypeMask;
            this.tagBits |= TagBits.HasUnresolvedEnclosingType;
            if (enclosingType().isStrictfp()) this.modifiers |= ClassFileConstants.AccStrictfp;
            if (enclosingType().isDeprecated()) this.modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
        }
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#availableMethods()
 */
    public FieldBinding[] availableFields() {
        if ((this.tagBits & TagBits.AreFieldsComplete) != 0) return this.fields;
        if ((this.tagBits & TagBits.AreFieldsSorted) == 0) {
            int length = this.fields.length;
            if (length > 1) ReferenceBinding.sortFields(this.fields, 0, length);
            this.tagBits |= TagBits.AreFieldsSorted;
        }
        FieldBinding[] availableFields = new FieldBinding[this.fields.length];
        int count = 0;
        for (int i = 0; i < this.fields.length; i++) {
            try {
                availableFields[count] = resolveTypeFor(this.fields[i]);
                count++;
            } catch (AbortCompilation a) {
            }
        }
        if (count < availableFields.length) System.arraycopy(availableFields, 0, availableFields = new FieldBinding[count], 0, count);
        return availableFields;
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding#availableMethods()
 */
    public MethodBinding[] availableMethods() {
        if ((this.tagBits & TagBits.AreMethodsComplete) != 0) return this.methods;
        if ((this.tagBits & TagBits.AreMethodsSorted) == 0) {
            int length = this.methods.length;
            if (length > 1) ReferenceBinding.sortMethods(this.methods, 0, length);
            this.tagBits |= TagBits.AreMethodsSorted;
        }
        MethodBinding[] availableMethods = new MethodBinding[this.methods.length];
        int count = 0;
        for (int i = 0; i < this.methods.length; i++) {
            try {
                availableMethods[count] = resolveTypesFor(this.methods[i]);
                count++;
            } catch (AbortCompilation a) {
            }
        }
        if (count < availableMethods.length) System.arraycopy(availableMethods, 0, availableMethods = new MethodBinding[count], 0, count);
        return availableMethods;
    }

    void cachePartsFrom(IBinaryType binaryType, boolean needFieldsAndMethods) {
        try {
            this.typeVariables = Binding.NO_TYPE_VARIABLES;
            this.superInterfaces = Binding.NO_SUPERINTERFACES;
            this.memberTypes = Binding.NO_MEMBER_TYPES;
            IBinaryNestedType[] memberTypeStructures = binaryType.getMemberTypes();
            if (memberTypeStructures != null) {
                int size = memberTypeStructures.length;
                if (size > 0) {
                    this.memberTypes = new ReferenceBinding[size];
                    for (int i = 0; i < size; i++) this.memberTypes[i] = this.environment.getTypeFromConstantPoolName(memberTypeStructures[i].getName(), 0, -1, false, null);
                    this.tagBits |= TagBits.HasUnresolvedMemberTypes;
                }
            }
            long sourceLevel = this.environment.globalOptions.sourceLevel;
            char[] typeSignature = null;
            if (sourceLevel >= ClassFileConstants.JDK1_5) {
                typeSignature = binaryType.getGenericSignature();
                this.tagBits |= binaryType.getTagBits();
            }
            char[][][] missingTypeNames = binaryType.getMissingTypeNames();
            if (typeSignature == null) {
                char[] superclassName = binaryType.getSuperclassName();
                if (superclassName != null) {
                    this.superclass = this.environment.getTypeFromConstantPoolName(superclassName, 0, -1, false, missingTypeNames);
                    this.tagBits |= TagBits.HasUnresolvedSuperclass;
                }
                this.superInterfaces = Binding.NO_SUPERINTERFACES;
                char[][] interfaceNames = binaryType.getInterfaceNames();
                if (interfaceNames != null) {
                    int size = interfaceNames.length;
                    if (size > 0) {
                        this.superInterfaces = new ReferenceBinding[size];
                        for (int i = 0; i < size; i++) this.superInterfaces[i] = this.environment.getTypeFromConstantPoolName(interfaceNames[i], 0, -1, false, missingTypeNames);
                        this.tagBits |= TagBits.HasUnresolvedSuperinterfaces;
                    }
                }
            } else {
                SignatureWrapper wrapper = new SignatureWrapper(typeSignature);
                if (wrapper.signature[wrapper.start] == '<') {
                    wrapper.start++;
                    this.typeVariables = createTypeVariables(wrapper, true, missingTypeNames);
                    wrapper.start++;
                    this.tagBits |= TagBits.HasUnresolvedTypeVariables;
                    this.modifiers |= ExtraCompilerModifiers.AccGenericSignature;
                }
                TypeVariableBinding[] typeVars = Binding.NO_TYPE_VARIABLES;
                char[] methodDescriptor = binaryType.getEnclosingMethod();
                if (methodDescriptor != null) {
                    MethodBinding enclosingMethod = findMethod(methodDescriptor, missingTypeNames);
                    typeVars = enclosingMethod.typeVariables;
                }
                this.superclass = (ReferenceBinding) this.environment.getTypeFromTypeSignature(wrapper, typeVars, this, missingTypeNames);
                this.tagBits |= TagBits.HasUnresolvedSuperclass;
                this.superInterfaces = Binding.NO_SUPERINTERFACES;
                if (!wrapper.atEnd()) {
                    java.util.ArrayList types = new java.util.ArrayList(2);
                    do {
                        types.add(this.environment.getTypeFromTypeSignature(wrapper, typeVars, this, missingTypeNames));
                    } while (!wrapper.atEnd());
                    this.superInterfaces = new ReferenceBinding[types.size()];
                    types.toArray(this.superInterfaces);
                    this.tagBits |= TagBits.HasUnresolvedSuperinterfaces;
                }
            }
            if (needFieldsAndMethods) {
                createFields(binaryType.getFields(), sourceLevel, missingTypeNames);
                createMethods(binaryType.getMethods(), sourceLevel, missingTypeNames);
            }
            if (this.environment.globalOptions.storeAnnotations) setAnnotations(createAnnotations(binaryType.getAnnotations(), this.environment, missingTypeNames));
        } finally {
            if (this.fields == null) this.fields = Binding.NO_FIELDS;
            if (this.methods == null) this.methods = Binding.NO_METHODS;
        }
    }

    private void createFields(IBinaryField[] iFields, long sourceLevel, char[][][] missingTypeNames) {
        this.fields = Binding.NO_FIELDS;
        if (iFields != null) {
            int size = iFields.length;
            if (size > 0) {
                this.fields = new FieldBinding[size];
                boolean use15specifics = sourceLevel >= ClassFileConstants.JDK1_5;
                boolean isViewedAsDeprecated = isViewedAsDeprecated();
                boolean hasRestrictedAccess = hasRestrictedAccess();
                int firstAnnotatedFieldIndex = -1;
                for (int i = 0; i < size; i++) {
                    IBinaryField binaryField = iFields[i];
                    char[] fieldSignature = use15specifics ? binaryField.getGenericSignature() : null;
                    TypeBinding type = fieldSignature == null ? this.environment.getTypeFromSignature(binaryField.getTypeName(), 0, -1, false, this, missingTypeNames) : this.environment.getTypeFromTypeSignature(new SignatureWrapper(fieldSignature), Binding.NO_TYPE_VARIABLES, this, missingTypeNames);
                    FieldBinding field = new FieldBinding(binaryField.getName(), type, binaryField.getModifiers() | ExtraCompilerModifiers.AccUnresolved, this, binaryField.getConstant());
                    if (firstAnnotatedFieldIndex < 0 && this.environment.globalOptions.storeAnnotations && binaryField.getAnnotations() != null) {
                        firstAnnotatedFieldIndex = i;
                    }
                    field.id = i;
                    if (use15specifics) field.tagBits |= binaryField.getTagBits();
                    if (isViewedAsDeprecated && !field.isDeprecated()) field.modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                    if (hasRestrictedAccess) field.modifiers |= ExtraCompilerModifiers.AccRestrictedAccess;
                    if (fieldSignature != null) field.modifiers |= ExtraCompilerModifiers.AccGenericSignature;
                    this.fields[i] = field;
                }
                if (firstAnnotatedFieldIndex >= 0) {
                    for (int i = firstAnnotatedFieldIndex; i < size; i++) {
                        IBinaryField binaryField = iFields[i];
                        this.fields[i].setAnnotations(createAnnotations(binaryField.getAnnotations(), this.environment, missingTypeNames));
                    }
                }
            }
        }
    }

    private MethodBinding createMethod(IBinaryMethod method, long sourceLevel, char[][][] missingTypeNames) {
        int methodModifiers = method.getModifiers() | ExtraCompilerModifiers.AccUnresolved;
        if (sourceLevel < ClassFileConstants.JDK1_5) methodModifiers &= ~ClassFileConstants.AccVarargs;
        ReferenceBinding[] exceptions = Binding.NO_EXCEPTIONS;
        TypeBinding[] parameters = Binding.NO_PARAMETERS;
        TypeVariableBinding[] typeVars = Binding.NO_TYPE_VARIABLES;
        AnnotationBinding[][] paramAnnotations = null;
        TypeBinding returnType = null;
        final boolean use15specifics = sourceLevel >= ClassFileConstants.JDK1_5;
        char[] methodSignature = use15specifics ? method.getGenericSignature() : null;
        if (methodSignature == null) {
            char[] methodDescriptor = method.getMethodDescriptor();
            int numOfParams = 0;
            char nextChar;
            int index = 0;
            while ((nextChar = methodDescriptor[++index]) != ')') {
                if (nextChar != '[') {
                    numOfParams++;
                    if (nextChar == 'L') while ((nextChar = methodDescriptor[++index]) != ';') {
                    }
                }
            }
            int startIndex = (method.isConstructor() && isMemberType() && !isStatic()) ? 1 : 0;
            int size = numOfParams - startIndex;
            if (size > 0) {
                parameters = new TypeBinding[size];
                if (this.environment.globalOptions.storeAnnotations) paramAnnotations = new AnnotationBinding[size][];
                index = 1;
                int end = 0;
                for (int i = 0; i < numOfParams; i++) {
                    while ((nextChar = methodDescriptor[++end]) == '[') {
                    }
                    if (nextChar == 'L') while ((nextChar = methodDescriptor[++end]) != ';') {
                    }
                    if (i >= startIndex) {
                        parameters[i - startIndex] = this.environment.getTypeFromSignature(methodDescriptor, index, end, false, this, missingTypeNames);
                        if (paramAnnotations != null) paramAnnotations[i - startIndex] = createAnnotations(method.getParameterAnnotations(i), this.environment, missingTypeNames);
                    }
                    index = end + 1;
                }
            }
            char[][] exceptionTypes = method.getExceptionTypeNames();
            if (exceptionTypes != null) {
                size = exceptionTypes.length;
                if (size > 0) {
                    exceptions = new ReferenceBinding[size];
                    for (int i = 0; i < size; i++) exceptions[i] = this.environment.getTypeFromConstantPoolName(exceptionTypes[i], 0, -1, false, missingTypeNames);
                }
            }
            if (!method.isConstructor()) returnType = this.environment.getTypeFromSignature(methodDescriptor, index + 1, -1, false, this, missingTypeNames);
        } else {
            methodModifiers |= ExtraCompilerModifiers.AccGenericSignature;
            SignatureWrapper wrapper = new SignatureWrapper(methodSignature);
            if (wrapper.signature[wrapper.start] == '<') {
                wrapper.start++;
                typeVars = createTypeVariables(wrapper, false, missingTypeNames);
                wrapper.start++;
            }
            if (wrapper.signature[wrapper.start] == '(') {
                wrapper.start++;
                if (wrapper.signature[wrapper.start] == ')') {
                    wrapper.start++;
                } else {
                    java.util.ArrayList types = new java.util.ArrayList(2);
                    while (wrapper.signature[wrapper.start] != ')') types.add(this.environment.getTypeFromTypeSignature(wrapper, typeVars, this, missingTypeNames));
                    wrapper.start++;
                    int numParam = types.size();
                    parameters = new TypeBinding[numParam];
                    types.toArray(parameters);
                    if (this.environment.globalOptions.storeAnnotations) {
                        paramAnnotations = new AnnotationBinding[numParam][];
                        for (int i = 0; i < numParam; i++) paramAnnotations[i] = createAnnotations(method.getParameterAnnotations(i), this.environment, missingTypeNames);
                    }
                }
            }
            returnType = this.environment.getTypeFromTypeSignature(wrapper, typeVars, this, missingTypeNames);
            if (!wrapper.atEnd() && wrapper.signature[wrapper.start] == '^') {
                java.util.ArrayList types = new java.util.ArrayList(2);
                do {
                    wrapper.start++;
                    types.add(this.environment.getTypeFromTypeSignature(wrapper, typeVars, this, missingTypeNames));
                } while (!wrapper.atEnd() && wrapper.signature[wrapper.start] == '^');
                exceptions = new ReferenceBinding[types.size()];
                types.toArray(exceptions);
            } else {
                char[][] exceptionTypes = method.getExceptionTypeNames();
                if (exceptionTypes != null) {
                    int size = exceptionTypes.length;
                    if (size > 0) {
                        exceptions = new ReferenceBinding[size];
                        for (int i = 0; i < size; i++) exceptions[i] = this.environment.getTypeFromConstantPoolName(exceptionTypes[i], 0, -1, false, missingTypeNames);
                    }
                }
            }
        }
        MethodBinding result = method.isConstructor() ? new MethodBinding(methodModifiers, parameters, exceptions, this) : new MethodBinding(methodModifiers, method.getSelector(), returnType, parameters, exceptions, this);
        if (this.environment.globalOptions.storeAnnotations) result.setAnnotations(createAnnotations(method.getAnnotations(), this.environment, missingTypeNames), paramAnnotations, isAnnotationType() ? convertMemberValue(method.getDefaultValue(), this.environment, missingTypeNames) : null, this.environment);
        if (use15specifics) result.tagBits |= method.getTagBits();
        result.typeVariables = typeVars;
        for (int i = 0, length = typeVars.length; i < length; i++) typeVars[i].declaringElement = result;
        return result;
    }

    /**
 * Create method bindings for binary type, filtering out <clinit> and synthetics
 */
    private void createMethods(IBinaryMethod[] iMethods, long sourceLevel, char[][][] missingTypeNames) {
        int total = 0, initialTotal = 0, iClinit = -1;
        int[] toSkip = null;
        if (iMethods != null) {
            total = initialTotal = iMethods.length;
            boolean keepBridgeMethods = sourceLevel < ClassFileConstants.JDK1_5 && this.environment.globalOptions.complianceLevel >= ClassFileConstants.JDK1_5;
            for (int i = total; --i >= 0; ) {
                IBinaryMethod method = iMethods[i];
                if ((method.getModifiers() & ClassFileConstants.AccSynthetic) != 0) {
                    if (keepBridgeMethods && (method.getModifiers() & ClassFileConstants.AccBridge) != 0) continue;
                    if (toSkip == null) toSkip = new int[iMethods.length];
                    toSkip[i] = -1;
                    total--;
                } else if (iClinit == -1) {
                    char[] methodName = method.getSelector();
                    if (methodName.length == 8 && methodName[0] == '<') {
                        iClinit = i;
                        total--;
                    }
                }
            }
        }
        if (total == 0) {
            this.methods = Binding.NO_METHODS;
            return;
        }
        boolean isViewedAsDeprecated = isViewedAsDeprecated();
        boolean hasRestrictedAccess = hasRestrictedAccess();
        this.methods = new MethodBinding[total];
        if (total == initialTotal) {
            for (int i = 0; i < initialTotal; i++) {
                MethodBinding method = createMethod(iMethods[i], sourceLevel, missingTypeNames);
                if (isViewedAsDeprecated && !method.isDeprecated()) method.modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                if (hasRestrictedAccess) method.modifiers |= ExtraCompilerModifiers.AccRestrictedAccess;
                this.methods[i] = method;
            }
        } else {
            for (int i = 0, index = 0; i < initialTotal; i++) {
                if (iClinit != i && (toSkip == null || toSkip[i] != -1)) {
                    MethodBinding method = createMethod(iMethods[i], sourceLevel, missingTypeNames);
                    if (isViewedAsDeprecated && !method.isDeprecated()) method.modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
                    if (hasRestrictedAccess) method.modifiers |= ExtraCompilerModifiers.AccRestrictedAccess;
                    this.methods[index++] = method;
                }
            }
        }
    }

    private TypeVariableBinding[] createTypeVariables(SignatureWrapper wrapper, boolean assignVariables, char[][][] missingTypeNames) {
        char[] typeSignature = wrapper.signature;
        int depth = 0, length = typeSignature.length;
        int rank = 0;
        ArrayList variables = new ArrayList(1);
        depth = 0;
        boolean pendingVariable = true;
        createVariables: {
            for (int i = 1; i < length; i++) {
                switch(typeSignature[i]) {
                    case '<':
                        depth++;
                        break;
                    case '>':
                        if (--depth < 0) break createVariables;
                        break;
                    case ';':
                        if ((depth == 0) && (i + 1 < length) && (typeSignature[i + 1] != ':')) pendingVariable = true;
                        break;
                    default:
                        if (pendingVariable) {
                            pendingVariable = false;
                            int colon = CharOperation.indexOf(':', typeSignature, i);
                            char[] variableName = CharOperation.subarray(typeSignature, i, colon);
                            variables.add(new TypeVariableBinding(variableName, this, rank++, this.environment));
                        }
                }
            }
        }
        TypeVariableBinding[] result;
        variables.toArray(result = new TypeVariableBinding[rank]);
        if (assignVariables) this.typeVariables = result;
        for (int i = 0; i < rank; i++) {
            initializeTypeVariable(result[i], result, wrapper, missingTypeNames);
        }
        return result;
    }

    public ReferenceBinding enclosingType() {
        if ((this.tagBits & TagBits.HasUnresolvedEnclosingType) == 0) return this.enclosingType;
        this.enclosingType = (ReferenceBinding) resolveType(this.enclosingType, this.environment, false);
        this.tagBits &= ~TagBits.HasUnresolvedEnclosingType;
        return this.enclosingType;
    }

    public FieldBinding[] fields() {
        if ((this.tagBits & TagBits.AreFieldsComplete) != 0) return this.fields;
        if ((this.tagBits & TagBits.AreFieldsSorted) == 0) {
            int length = this.fields.length;
            if (length > 1) ReferenceBinding.sortFields(this.fields, 0, length);
            this.tagBits |= TagBits.AreFieldsSorted;
        }
        for (int i = this.fields.length; --i >= 0; ) resolveTypeFor(this.fields[i]);
        this.tagBits |= TagBits.AreFieldsComplete;
        return this.fields;
    }

    private MethodBinding findMethod(char[] methodDescriptor, char[][][] missingTypeNames) {
        int index = -1;
        while (methodDescriptor[++index] != '(') {
        }
        char[] selector = new char[index];
        System.arraycopy(methodDescriptor, 0, selector, 0, index);
        TypeBinding[] parameters = Binding.NO_PARAMETERS;
        int numOfParams = 0;
        char nextChar;
        while ((nextChar = methodDescriptor[++index]) != ')') {
            if (nextChar != '[') {
                numOfParams++;
                if (nextChar == 'L') while ((nextChar = methodDescriptor[++index]) != ';') {
                }
            }
        }
        int startIndex = 0;
        if (numOfParams > 0) {
            parameters = new TypeBinding[numOfParams];
            index = 1;
            int end = 0;
            for (int i = 0; i < numOfParams; i++) {
                while ((nextChar = methodDescriptor[++end]) == '[') {
                }
                if (nextChar == 'L') while ((nextChar = methodDescriptor[++end]) != ';') {
                }
                if (i >= startIndex) {
                    parameters[i - startIndex] = this.environment.getTypeFromSignature(methodDescriptor, index, end, false, this, missingTypeNames);
                }
                index = end + 1;
            }
        }
        return CharOperation.equals(selector, TypeConstants.INIT) ? this.enclosingType.getExactConstructor(parameters) : this.enclosingType.getExactMethod(selector, parameters, null);
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.lookup.TypeBinding#genericTypeSignature()
 */
    public char[] genericTypeSignature() {
        return computeGenericTypeSignature(this.typeVariables);
    }

    public MethodBinding getExactConstructor(TypeBinding[] argumentTypes) {
        if ((this.tagBits & TagBits.AreMethodsSorted) == 0) {
            int length = this.methods.length;
            if (length > 1) ReferenceBinding.sortMethods(this.methods, 0, length);
            this.tagBits |= TagBits.AreMethodsSorted;
        }
        int argCount = argumentTypes.length;
        long range;
        if ((range = ReferenceBinding.binarySearch(TypeConstants.INIT, this.methods)) >= 0) {
            nextMethod: for (int imethod = (int) range, end = (int) (range >> 32); imethod <= end; imethod++) {
                MethodBinding method = this.methods[imethod];
                if (method.parameters.length == argCount) {
                    resolveTypesFor(method);
                    TypeBinding[] toMatch = method.parameters;
                    for (int iarg = 0; iarg < argCount; iarg++) if (toMatch[iarg] != argumentTypes[iarg]) continue nextMethod;
                    return method;
                }
            }
        }
        return null;
    }

    public MethodBinding getExactMethod(char[] selector, TypeBinding[] argumentTypes, CompilationUnitScope refScope) {
        if ((this.tagBits & TagBits.AreMethodsSorted) == 0) {
            int length = this.methods.length;
            if (length > 1) ReferenceBinding.sortMethods(this.methods, 0, length);
            this.tagBits |= TagBits.AreMethodsSorted;
        }
        int argCount = argumentTypes.length;
        boolean foundNothing = true;
        long range;
        if ((range = ReferenceBinding.binarySearch(selector, this.methods)) >= 0) {
            nextMethod: for (int imethod = (int) range, end = (int) (range >> 32); imethod <= end; imethod++) {
                MethodBinding method = this.methods[imethod];
                foundNothing = false;
                if (method.parameters.length == argCount) {
                    resolveTypesFor(method);
                    TypeBinding[] toMatch = method.parameters;
                    for (int iarg = 0; iarg < argCount; iarg++) if (toMatch[iarg] != argumentTypes[iarg]) continue nextMethod;
                    return method;
                }
            }
        }
        if (foundNothing) {
            if (isInterface()) {
                if (superInterfaces().length == 1) {
                    if (refScope != null) refScope.recordTypeReference(this.superInterfaces[0]);
                    return this.superInterfaces[0].getExactMethod(selector, argumentTypes, refScope);
                }
            } else if (superclass() != null) {
                if (refScope != null) refScope.recordTypeReference(this.superclass);
                return this.superclass.getExactMethod(selector, argumentTypes, refScope);
            }
        }
        return null;
    }

    public FieldBinding getField(char[] fieldName, boolean needResolve) {
        if ((this.tagBits & TagBits.AreFieldsSorted) == 0) {
            int length = this.fields.length;
            if (length > 1) ReferenceBinding.sortFields(this.fields, 0, length);
            this.tagBits |= TagBits.AreFieldsSorted;
        }
        FieldBinding field = ReferenceBinding.binarySearch(fieldName, this.fields);
        return needResolve && field != null ? resolveTypeFor(field) : field;
    }

    /**
 *  Rewrite of default getMemberType to avoid resolving eagerly all member types when one is requested
 */
    public ReferenceBinding getMemberType(char[] typeName) {
        for (int i = this.memberTypes.length; --i >= 0; ) {
            ReferenceBinding memberType = this.memberTypes[i];
            if (memberType instanceof UnresolvedReferenceBinding) {
                char[] name = memberType.sourceName;
                int prefixLength = this.compoundName[this.compoundName.length - 1].length + 1;
                if (name.length == (prefixLength + typeName.length)) if (CharOperation.fragmentEquals(typeName, name, prefixLength, true)) return this.memberTypes[i] = (ReferenceBinding) resolveType(memberType, this.environment, false);
            } else if (CharOperation.equals(typeName, memberType.sourceName)) {
                return memberType;
            }
        }
        return null;
    }

    public MethodBinding[] getMethods(char[] selector) {
        if ((this.tagBits & TagBits.AreMethodsComplete) != 0) {
            long range;
            if ((range = ReferenceBinding.binarySearch(selector, this.methods)) >= 0) {
                int start = (int) range, end = (int) (range >> 32);
                int length = end - start + 1;
                if ((this.tagBits & TagBits.AreMethodsComplete) != 0) {
                    MethodBinding[] result;
                    System.arraycopy(this.methods, start, result = new MethodBinding[length], 0, length);
                    return result;
                }
            }
            return Binding.NO_METHODS;
        }
        if ((this.tagBits & TagBits.AreMethodsSorted) == 0) {
            int length = this.methods.length;
            if (length > 1) ReferenceBinding.sortMethods(this.methods, 0, length);
            this.tagBits |= TagBits.AreMethodsSorted;
        }
        long range;
        if ((range = ReferenceBinding.binarySearch(selector, this.methods)) >= 0) {
            int start = (int) range, end = (int) (range >> 32);
            int length = end - start + 1;
            MethodBinding[] result = new MethodBinding[length];
            for (int i = start, index = 0; i <= end; i++, index++) result[index] = resolveTypesFor(this.methods[i]);
            return result;
        }
        return Binding.NO_METHODS;
    }

    public boolean hasMemberTypes() {
        return this.memberTypes.length > 0;
    }

    public TypeVariableBinding getTypeVariable(char[] variableName) {
        TypeVariableBinding variable = super.getTypeVariable(variableName);
        variable.resolve();
        return variable;
    }

    private void initializeTypeVariable(TypeVariableBinding variable, TypeVariableBinding[] existingVariables, SignatureWrapper wrapper, char[][][] missingTypeNames) {
        int colon = CharOperation.indexOf(':', wrapper.signature, wrapper.start);
        wrapper.start = colon + 1;
        ReferenceBinding type, firstBound = null;
        if (wrapper.signature[wrapper.start] == ':') {
            type = this.environment.getResolvedType(TypeConstants.JAVA_LANG_OBJECT, null);
        } else {
            type = (ReferenceBinding) this.environment.getTypeFromTypeSignature(wrapper, existingVariables, this, missingTypeNames);
            firstBound = type;
        }
        variable.modifiers |= ExtraCompilerModifiers.AccUnresolved;
        variable.superclass = type;
        ReferenceBinding[] bounds = null;
        if (wrapper.signature[wrapper.start] == ':') {
            java.util.ArrayList types = new java.util.ArrayList(2);
            do {
                wrapper.start++;
                types.add(this.environment.getTypeFromTypeSignature(wrapper, existingVariables, this, missingTypeNames));
            } while (wrapper.signature[wrapper.start] == ':');
            bounds = new ReferenceBinding[types.size()];
            types.toArray(bounds);
        }
        variable.superInterfaces = bounds == null ? Binding.NO_SUPERINTERFACES : bounds;
        if (firstBound == null) {
            firstBound = variable.superInterfaces.length == 0 ? null : variable.superInterfaces[0];
        }
        variable.firstBound = firstBound;
    }

    /**
 * Returns true if a type is identical to another one,
 * or for generic types, true if compared to its raw type.
 */
    public boolean isEquivalentTo(TypeBinding otherType) {
        if (this == otherType) return true;
        if (otherType == null) return false;
        switch(otherType.kind()) {
            case Binding.WILDCARD_TYPE:
            case Binding.INTERSECTION_TYPE:
                return ((WildcardBinding) otherType).boundCheck(this);
            case Binding.RAW_TYPE:
                return otherType.erasure() == this;
        }
        return false;
    }

    public boolean isGenericType() {
        return this.typeVariables != Binding.NO_TYPE_VARIABLES;
    }

    public boolean isHierarchyConnected() {
        return (this.tagBits & (TagBits.HasUnresolvedSuperclass | TagBits.HasUnresolvedSuperinterfaces)) == 0;
    }

    public int kind() {
        if (this.typeVariables != Binding.NO_TYPE_VARIABLES) return Binding.GENERIC_TYPE;
        return Binding.TYPE;
    }

    public ReferenceBinding[] memberTypes() {
        if ((this.tagBits & TagBits.HasUnresolvedMemberTypes) == 0) return this.memberTypes;
        for (int i = this.memberTypes.length; --i >= 0; ) this.memberTypes[i] = (ReferenceBinding) resolveType(this.memberTypes[i], this.environment, false);
        this.tagBits &= ~TagBits.HasUnresolvedMemberTypes;
        return this.memberTypes;
    }

    public MethodBinding[] methods() {
        if ((this.tagBits & TagBits.AreMethodsComplete) != 0) return this.methods;
        if ((this.tagBits & TagBits.AreMethodsSorted) == 0) {
            int length = this.methods.length;
            if (length > 1) ReferenceBinding.sortMethods(this.methods, 0, length);
            this.tagBits |= TagBits.AreMethodsSorted;
        }
        for (int i = this.methods.length; --i >= 0; ) resolveTypesFor(this.methods[i]);
        this.tagBits |= TagBits.AreMethodsComplete;
        return this.methods;
    }

    private FieldBinding resolveTypeFor(FieldBinding field) {
        if ((field.modifiers & ExtraCompilerModifiers.AccUnresolved) == 0) return field;
        TypeBinding resolvedType = resolveType(field.type, this.environment, true);
        field.type = resolvedType;
        if ((resolvedType.tagBits & TagBits.HasMissingType) != 0) {
            field.tagBits |= TagBits.HasMissingType;
        }
        field.modifiers &= ~ExtraCompilerModifiers.AccUnresolved;
        return field;
    }

    MethodBinding resolveTypesFor(MethodBinding method) {
        if ((method.modifiers & ExtraCompilerModifiers.AccUnresolved) == 0) return method;
        if (!method.isConstructor()) {
            TypeBinding resolvedType = resolveType(method.returnType, this.environment, true);
            method.returnType = resolvedType;
            if ((resolvedType.tagBits & TagBits.HasMissingType) != 0) {
                method.tagBits |= TagBits.HasMissingType;
            }
        }
        for (int i = method.parameters.length; --i >= 0; ) {
            TypeBinding resolvedType = resolveType(method.parameters[i], this.environment, true);
            method.parameters[i] = resolvedType;
            if ((resolvedType.tagBits & TagBits.HasMissingType) != 0) {
                method.tagBits |= TagBits.HasMissingType;
            }
        }
        for (int i = method.thrownExceptions.length; --i >= 0; ) {
            ReferenceBinding resolvedType = (ReferenceBinding) resolveType(method.thrownExceptions[i], this.environment, true);
            method.thrownExceptions[i] = resolvedType;
            if ((resolvedType.tagBits & TagBits.HasMissingType) != 0) {
                method.tagBits |= TagBits.HasMissingType;
            }
        }
        for (int i = method.typeVariables.length; --i >= 0; ) {
            method.typeVariables[i].resolve();
        }
        method.modifiers &= ~ExtraCompilerModifiers.AccUnresolved;
        return method;
    }

    AnnotationBinding[] retrieveAnnotations(Binding binding) {
        return AnnotationBinding.addStandardAnnotations(super.retrieveAnnotations(binding), binding.getAnnotationTagBits(), this.environment);
    }

    SimpleLookupTable storedAnnotations(boolean forceInitialize) {
        if (forceInitialize && this.storedAnnotations == null) {
            if (!this.environment.globalOptions.storeAnnotations) return null;
            this.storedAnnotations = new SimpleLookupTable(3);
        }
        return this.storedAnnotations;
    }

    public ReferenceBinding superclass() {
        if ((this.tagBits & TagBits.HasUnresolvedSuperclass) == 0) return this.superclass;
        this.superclass = (ReferenceBinding) resolveType(this.superclass, this.environment, true);
        this.tagBits &= ~TagBits.HasUnresolvedSuperclass;
        if (this.superclass.problemId() == ProblemReasons.NotFound) this.tagBits |= TagBits.HierarchyHasProblems;
        return this.superclass;
    }

    public ReferenceBinding[] superInterfaces() {
        if ((this.tagBits & TagBits.HasUnresolvedSuperinterfaces) == 0) return this.superInterfaces;
        for (int i = this.superInterfaces.length; --i >= 0; ) {
            this.superInterfaces[i] = (ReferenceBinding) resolveType(this.superInterfaces[i], this.environment, true);
            if (this.superInterfaces[i].problemId() == ProblemReasons.NotFound) this.tagBits |= TagBits.HierarchyHasProblems;
        }
        this.tagBits &= ~TagBits.HasUnresolvedSuperinterfaces;
        return this.superInterfaces;
    }

    public TypeVariableBinding[] typeVariables() {
        if ((this.tagBits & TagBits.HasUnresolvedTypeVariables) == 0) return this.typeVariables;
        for (int i = this.typeVariables.length; --i >= 0; ) this.typeVariables[i].resolve();
        this.tagBits &= ~TagBits.HasUnresolvedTypeVariables;
        return this.typeVariables;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (isDeprecated()) buffer.append("deprecated ");
        if (isPublic()) buffer.append("public ");
        if (isProtected()) buffer.append("protected ");
        if (isPrivate()) buffer.append("private ");
        if (isAbstract() && isClass()) buffer.append("abstract ");
        if (isStatic() && isNestedType()) buffer.append("static ");
        if (isFinal()) buffer.append("final ");
        if (isEnum()) buffer.append("enum "); else if (isAnnotationType()) buffer.append("@interface "); else if (isClass()) buffer.append("class "); else buffer.append("interface ");
        buffer.append((this.compoundName != null) ? CharOperation.toString(this.compoundName) : "UNNAMED TYPE");
        if (this.typeVariables == null) {
            buffer.append("<NULL TYPE VARIABLES>");
        } else if (this.typeVariables != Binding.NO_TYPE_VARIABLES) {
            buffer.append("<");
            for (int i = 0, length = this.typeVariables.length; i < length; i++) {
                if (i > 0) buffer.append(", ");
                if (this.typeVariables[i] == null) {
                    buffer.append("NULL TYPE VARIABLE");
                    continue;
                }
                char[] varChars = this.typeVariables[i].toString().toCharArray();
                buffer.append(varChars, 1, varChars.length - 2);
            }
            buffer.append(">");
        }
        buffer.append("\n\textends ");
        buffer.append((this.superclass != null) ? this.superclass.debugName() : "NULL TYPE");
        if (this.superInterfaces != null) {
            if (this.superInterfaces != Binding.NO_SUPERINTERFACES) {
                buffer.append("\n\timplements : ");
                for (int i = 0, length = this.superInterfaces.length; i < length; i++) {
                    if (i > 0) buffer.append(", ");
                    buffer.append((this.superInterfaces[i] != null) ? this.superInterfaces[i].debugName() : "NULL TYPE");
                }
            }
        } else {
            buffer.append("NULL SUPERINTERFACES");
        }
        if (this.enclosingType != null) {
            buffer.append("\n\tenclosing type : ");
            buffer.append(this.enclosingType.debugName());
        }
        if (this.fields != null) {
            if (this.fields != Binding.NO_FIELDS) {
                buffer.append("\n/*   fields   */");
                for (int i = 0, length = this.fields.length; i < length; i++) buffer.append((this.fields[i] != null) ? "\n" + this.fields[i].toString() : "\nNULL FIELD");
            }
        } else {
            buffer.append("NULL FIELDS");
        }
        if (this.methods != null) {
            if (this.methods != Binding.NO_METHODS) {
                buffer.append("\n/*   methods   */");
                for (int i = 0, length = this.methods.length; i < length; i++) buffer.append((this.methods[i] != null) ? "\n" + this.methods[i].toString() : "\nNULL METHOD");
            }
        } else {
            buffer.append("NULL METHODS");
        }
        if (this.memberTypes != null) {
            if (this.memberTypes != Binding.NO_MEMBER_TYPES) {
                buffer.append("\n/*   members   */");
                for (int i = 0, length = this.memberTypes.length; i < length; i++) buffer.append((this.memberTypes[i] != null) ? "\n" + this.memberTypes[i].toString() : "\nNULL TYPE");
            }
        } else {
            buffer.append("NULL MEMBER TYPES");
        }
        buffer.append("\n\n\n");
        return buffer.toString();
    }

    MethodBinding[] unResolvedMethods() {
        return this.methods;
    }
}
