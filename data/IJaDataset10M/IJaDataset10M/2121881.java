package com.sun.gluegen;

import java.io.*;
import java.util.*;
import java.text.MessageFormat;
import com.sun.gluegen.cgram.types.*;

public class JavaEmitter implements GlueEmitter {

    private StructLayout layout;

    private TypeDictionary typedefDictionary;

    private TypeDictionary structDictionary;

    private Map canonMap;

    private JavaConfiguration cfg;

    /**
   * Style of code emission. Can emit everything into one class
   * (AllStatic), separate interface and implementing classes
   * (InterfaceAndImpl), only the interface (InterfaceOnly), or only
   * the implementation (ImplOnly).
   */
    public static final int ALL_STATIC = 1;

    public static final int INTERFACE_AND_IMPL = 2;

    public static final int INTERFACE_ONLY = 3;

    public static final int IMPL_ONLY = 4;

    /**
   * Access control for emitted Java methods.
   */
    public static final int ACC_PUBLIC = 1;

    public static final int ACC_PROTECTED = 2;

    public static final int ACC_PRIVATE = 3;

    public static final int ACC_PACKAGE_PRIVATE = 4;

    private PrintWriter javaWriter;

    private PrintWriter javaImplWriter;

    private PrintWriter cWriter;

    private MachineDescription machDesc32;

    private MachineDescription machDesc64;

    public void readConfigurationFile(String filename) throws Exception {
        cfg = createConfig();
        cfg.read(filename);
    }

    public void setMachineDescription(MachineDescription md32, MachineDescription md64) {
        if ((md32 == null) && (md64 == null)) {
            throw new RuntimeException("Must specify at least one MachineDescription");
        }
        machDesc32 = md32;
        machDesc64 = md64;
    }

    public void beginEmission(GlueEmitterControls controls) throws IOException {
        try {
            openWriters();
        } catch (Exception e) {
            throw new RuntimeException("Unable to open files for writing", e);
        }
        emitAllFileHeaders();
        for (Iterator iter = cfg.forcedStructs().iterator(); iter.hasNext(); ) {
            controls.forceStructEmission((String) iter.next());
        }
    }

    public void endEmission() {
        emitAllFileFooters();
        try {
            closeWriters();
        } catch (Exception e) {
            throw new RuntimeException("Unable to close open files", e);
        }
    }

    public void beginDefines() throws Exception {
        if (cfg.allStatic() || cfg.emitInterface()) {
            javaWriter().println();
        }
    }

    public void emitDefine(String name, String value, String optionalComment) throws Exception {
        if (cfg.allStatic() || cfg.emitInterface()) {
            if (!cfg.shouldIgnore(name)) {
                String type = null;
                try {
                    int radix;
                    String parseValue;
                    if (value.startsWith("0x") || value.startsWith("0X")) {
                        radix = 16;
                        parseValue = value.substring(2);
                    } else if (value.startsWith("0") && value.length() > 1) {
                        radix = 8;
                        parseValue = value.substring(1);
                    } else {
                        radix = 10;
                        parseValue = value;
                    }
                    long longVal = Long.parseLong(parseValue, radix);
                    type = "long";
                    if (longVal > Integer.MIN_VALUE && longVal < Integer.MAX_VALUE) {
                        type = "int";
                    }
                } catch (NumberFormatException e) {
                    try {
                        double dVal = Double.parseDouble(value);
                        type = "double";
                        if (dVal > Float.MIN_VALUE && dVal < Float.MAX_VALUE) {
                            type = "float";
                        }
                    } catch (NumberFormatException e2) {
                        throw new RuntimeException("Cannot emit define \"" + name + "\": value \"" + value + "\" cannot be assigned to a int, long, float, or double", e2);
                    }
                }
                if (type == null) {
                    throw new RuntimeException("Cannot emit define (2) \"" + name + "\": value \"" + value + "\" cannot be assigned to a int, long, float, or double");
                }
                if (optionalComment != null && optionalComment.length() != 0) {
                    javaWriter().println("  /** " + optionalComment + " */");
                }
                javaWriter().println("  public static final " + type + " " + name + " = " + value + ";");
            }
        }
    }

    public void endDefines() throws Exception {
    }

    public void beginFunctions(TypeDictionary typedefDictionary, TypeDictionary structDictionary, Map canonMap) throws Exception {
        this.typedefDictionary = typedefDictionary;
        this.structDictionary = structDictionary;
        this.canonMap = canonMap;
        if (cfg.allStatic() || cfg.emitInterface()) {
            javaWriter().println();
        }
    }

    public Iterator emitFunctions(List originalCFunctions) throws Exception {
        Set funcsToBindSet = new HashSet(100);
        for (Iterator cIter = originalCFunctions.iterator(); cIter.hasNext(); ) {
            FunctionSymbol cFunc = (FunctionSymbol) cIter.next();
            if (!funcsToBindSet.contains(cFunc)) {
                funcsToBindSet.add(cFunc);
            }
        }
        ArrayList funcsToBind = new ArrayList(funcsToBindSet.size());
        funcsToBind.addAll(funcsToBindSet);
        Collections.sort(funcsToBind, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((FunctionSymbol) o1).getName().compareTo(((FunctionSymbol) o2).getName());
            }

            public boolean equals(Object obj) {
                return obj.getClass() == this.getClass();
            }
        });
        ArrayList methodBindingEmitters = new ArrayList(2 * funcsToBind.size());
        for (Iterator iter = funcsToBind.iterator(); iter.hasNext(); ) {
            FunctionSymbol cFunc = (FunctionSymbol) iter.next();
            if (cfg.shouldIgnore(cFunc.getName())) {
                continue;
            }
            List allBindings = generateMethodBindingEmitters(cFunc);
            methodBindingEmitters.addAll(allBindings);
        }
        for (int i = 0; i < methodBindingEmitters.size(); ++i) {
            FunctionEmitter emitter = (FunctionEmitter) methodBindingEmitters.get(i);
            try {
                emitter.emit();
            } catch (Exception e) {
                throw new RuntimeException("Error while emitting binding for \"" + emitter.getName() + "\"", e);
            }
            emitter.getDefaultOutput().println();
        }
        return funcsToBind.iterator();
    }

    /**
   * Create the object that will read and store configuration information for
   * this JavaEmitter.
   */
    protected JavaConfiguration createConfig() {
        return new JavaConfiguration();
    }

    /**
   * Get the configuration information for this JavaEmitter.
   */
    protected JavaConfiguration getConfig() {
        return cfg;
    }

    /**
   * Generates the public emitters for this MethodBinding which will
   * produce either simply signatures (for the interface class, if
   * any) or function definitions with or without a body (depending on
   * whether or not the implementing function can go directly to
   * native code because it doesn't need any processing of the
   * outgoing arguments).
   */
    protected void generatePublicEmitters(MethodBinding binding, List allEmitters, boolean signatureOnly) {
        PrintWriter writer = ((signatureOnly || cfg.allStatic()) ? javaWriter() : javaImplWriter());
        if (cfg.manuallyImplement(binding.getName()) && !signatureOnly) {
            return;
        }
        int accessControl = cfg.accessControl(binding.getName());
        if (signatureOnly && (accessControl != ACC_PUBLIC)) {
            return;
        }
        boolean isUnimplemented = cfg.isUnimplemented(binding.getName());
        List prologue = cfg.javaPrologueForMethod(binding, false, false);
        List epilogue = cfg.javaEpilogueForMethod(binding, false, false);
        boolean needsBody = (isUnimplemented || (binding.needsNIOWrappingOrUnwrapping() || binding.signatureUsesJavaPrimitiveArrays()) || (prologue != null) || (epilogue != null));
        JavaMethodBindingEmitter emitter = new JavaMethodBindingEmitter(binding, writer, cfg.runtimeExceptionType(), !signatureOnly && needsBody, cfg.tagNativeBinding(), false, cfg.nioDirectOnly(binding.getName()), false, false, false, isUnimplemented);
        switch(accessControl) {
            case ACC_PUBLIC:
                emitter.addModifier(JavaMethodBindingEmitter.PUBLIC);
                break;
            case ACC_PROTECTED:
                emitter.addModifier(JavaMethodBindingEmitter.PROTECTED);
                break;
            case ACC_PRIVATE:
                emitter.addModifier(JavaMethodBindingEmitter.PRIVATE);
                break;
            default:
                break;
        }
        if (cfg.allStatic()) {
            emitter.addModifier(JavaMethodBindingEmitter.STATIC);
        }
        if (!isUnimplemented && !needsBody && !signatureOnly) {
            emitter.addModifier(JavaMethodBindingEmitter.NATIVE);
        }
        emitter.setReturnedArrayLengthExpression(cfg.returnedArrayLength(binding.getName()));
        emitter.setPrologue(prologue);
        emitter.setEpilogue(epilogue);
        allEmitters.add(emitter);
    }

    /**
   * Generates the private emitters for this MethodBinding. On the
   * Java side these will simply produce signatures for native
   * methods. On the C side these will create the emitters which will
   * write the JNI code to interface to the functions. We need to be
   * careful to make the signatures all match up and not produce too
   * many emitters which would lead to compilation errors from
   * creating duplicated methods / functions.
   */
    protected void generatePrivateEmitters(MethodBinding binding, List allEmitters) {
        if (cfg.manuallyImplement(binding.getName())) {
            return;
        }
        boolean hasPrologueOrEpilogue = ((cfg.javaPrologueForMethod(binding, false, false) != null) || (cfg.javaEpilogueForMethod(binding, false, false) != null));
        if (!cfg.isUnimplemented(binding.getName()) && (binding.needsNIOWrappingOrUnwrapping() || binding.signatureUsesJavaPrimitiveArrays() || hasPrologueOrEpilogue)) {
            PrintWriter writer = (cfg.allStatic() ? javaWriter() : javaImplWriter());
            if (!binding.signatureUsesJavaPrimitiveArrays()) {
                JavaMethodBindingEmitter emitter = new JavaMethodBindingEmitter(binding, writer, cfg.runtimeExceptionType(), false, cfg.tagNativeBinding(), true, cfg.nioDirectOnly(binding.getName()), true, true, false, false);
                emitter.addModifier(JavaMethodBindingEmitter.PRIVATE);
                if (cfg.allStatic()) {
                    emitter.addModifier(JavaMethodBindingEmitter.STATIC);
                }
                emitter.addModifier(JavaMethodBindingEmitter.NATIVE);
                emitter.setReturnedArrayLengthExpression(cfg.returnedArrayLength(binding.getName()));
                allEmitters.add(emitter);
                if (!cfg.nioDirectOnly(binding.getName()) && binding.signatureCanUseIndirectNIO()) {
                    emitter = new JavaMethodBindingEmitter(binding, writer, cfg.runtimeExceptionType(), false, cfg.tagNativeBinding(), true, false, true, false, true, false);
                    emitter.addModifier(JavaMethodBindingEmitter.PRIVATE);
                    if (cfg.allStatic()) {
                        emitter.addModifier(JavaMethodBindingEmitter.STATIC);
                    }
                    emitter.addModifier(JavaMethodBindingEmitter.NATIVE);
                    emitter.setReturnedArrayLengthExpression(cfg.returnedArrayLength(binding.getName()));
                    allEmitters.add(emitter);
                }
            }
        }
        if (!cfg.isUnimplemented(binding.getName()) && !binding.signatureUsesJavaPrimitiveArrays()) {
            MessageFormat returnValueCapacityFormat = null;
            MessageFormat returnValueLengthFormat = null;
            JavaType javaReturnType = binding.getJavaReturnType();
            if (javaReturnType.isNIOBuffer() || javaReturnType.isCompoundTypeWrapper()) {
                String capacity = cfg.returnValueCapacity(binding.getName());
                if (capacity != null) {
                    returnValueCapacityFormat = new MessageFormat(capacity);
                }
            } else if (javaReturnType.isArray() || javaReturnType.isArrayOfCompoundTypeWrappers()) {
                if (javaReturnType.isPrimitiveArray()) {
                    throw new RuntimeException("Primitive array return types not yet supported");
                }
                String len = cfg.returnValueLength(binding.getName());
                if (len != null) {
                    returnValueLengthFormat = new MessageFormat(len);
                }
            }
            CMethodBindingEmitter cEmitter = new CMethodBindingEmitter(binding, cWriter(), cfg.implPackageName(), cfg.implClassName(), true, cfg.allStatic(), (binding.needsNIOWrappingOrUnwrapping() || hasPrologueOrEpilogue), false);
            if (returnValueCapacityFormat != null) {
                cEmitter.setReturnValueCapacityExpression(returnValueCapacityFormat);
            }
            if (returnValueLengthFormat != null) {
                cEmitter.setReturnValueLengthExpression(returnValueLengthFormat);
            }
            cEmitter.setTemporaryCVariableDeclarations(cfg.temporaryCVariableDeclarations(binding.getName()));
            cEmitter.setTemporaryCVariableAssignments(cfg.temporaryCVariableAssignments(binding.getName()));
            allEmitters.add(cEmitter);
            if (binding.argumentsUseNIO() && binding.signatureCanUseIndirectNIO() && !cfg.nioDirectOnly(binding.getName())) {
                cEmitter = new CMethodBindingEmitter(binding, cWriter(), cfg.implPackageName(), cfg.implClassName(), true, cfg.allStatic(), binding.needsNIOWrappingOrUnwrapping(), true);
                if (returnValueCapacityFormat != null) {
                    cEmitter.setReturnValueCapacityExpression(returnValueCapacityFormat);
                }
                if (returnValueLengthFormat != null) {
                    cEmitter.setReturnValueLengthExpression(returnValueLengthFormat);
                }
                cEmitter.setTemporaryCVariableDeclarations(cfg.temporaryCVariableDeclarations(binding.getName()));
                cEmitter.setTemporaryCVariableAssignments(cfg.temporaryCVariableAssignments(binding.getName()));
                allEmitters.add(cEmitter);
            }
        }
    }

    /**
   * Generate all appropriate Java bindings for the specified C function
   * symbols.
   */
    protected List generateMethodBindingEmitters(FunctionSymbol sym) throws Exception {
        ArrayList allEmitters = new ArrayList();
        try {
            MethodBinding mb = bindFunction(sym, null, null, machDesc64);
            List bindings = expandMethodBinding(mb);
            for (Iterator iter = bindings.iterator(); iter.hasNext(); ) {
                MethodBinding binding = (MethodBinding) iter.next();
                if (cfg.allStatic() && binding.hasContainingType()) {
                    throw new IllegalArgumentException("Cannot create binding in AllStatic mode because method has containing type: \"" + binding + "\"");
                }
                if (cfg.emitInterface()) {
                    generatePublicEmitters(binding, allEmitters, true);
                }
                if (cfg.emitImpl()) {
                    generatePublicEmitters(binding, allEmitters, false);
                    generatePrivateEmitters(binding, allEmitters);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while generating bindings for \"" + sym + "\"", e);
        }
        return allEmitters;
    }

    public void endFunctions() throws Exception {
        if (cfg.allStatic() || cfg.emitInterface()) {
            emitCustomJavaCode(javaWriter(), cfg.className());
        }
        if (!cfg.allStatic() && cfg.emitImpl()) {
            emitCustomJavaCode(javaImplWriter(), cfg.implClassName());
        }
    }

    public void beginStructLayout() throws Exception {
    }

    public void layoutStruct(CompoundType t) throws Exception {
        getLayout().layout(t);
    }

    public void endStructLayout() throws Exception {
    }

    public void beginStructs(TypeDictionary typedefDictionary, TypeDictionary structDictionary, Map canonMap) throws Exception {
        this.typedefDictionary = typedefDictionary;
        this.structDictionary = structDictionary;
        this.canonMap = canonMap;
    }

    public void emitStruct(CompoundType structType, String alternateName) throws Exception {
        emitStructImpl(structType, alternateName, machDesc32, machDesc64, true, false);
        emitStructImpl(structType, alternateName, machDesc32, machDesc64, false, true);
        emitStructImpl(structType, alternateName, machDesc32, machDesc64, false, false);
    }

    public void emitStructImpl(CompoundType structType, String alternateName, MachineDescription md32, MachineDescription md64, boolean doBaseClass, boolean do32Bit) throws Exception {
        String name = structType.getName();
        if (name == null && alternateName != null) {
            name = alternateName;
        }
        if (name == null) {
            System.err.println("WARNING: skipping emission of unnamed struct \"" + structType + "\"");
            return;
        }
        if (cfg.shouldIgnore(name)) {
            return;
        }
        Type containingCType = canonicalize(new PointerType(SizeThunk.POINTER, structType, 0));
        JavaType containingType = typeToJavaType(containingCType, false, null);
        if (!containingType.isCompoundTypeWrapper()) {
            return;
        }
        String containingTypeName = containingType.getName();
        if ((md32 == null) || (md64 == null)) {
            throw new RuntimeException("Must supply both 32- and 64-bit MachineDescriptions to emitStructImpl");
        }
        String suffix = "";
        MachineDescription extMachDesc = md64;
        MachineDescription intMachDesc = null;
        if (!doBaseClass) {
            if (do32Bit) {
                intMachDesc = md32;
                suffix = "32";
            } else {
                intMachDesc = md64;
                suffix = "64";
            }
        }
        boolean needsNativeCode = false;
        if (doBaseClass) {
            for (int i = 0; i < structType.getNumFields(); i++) {
                if (structType.getField(i).getType().isFunctionPointer()) {
                    needsNativeCode = true;
                    break;
                }
            }
        }
        String structClassPkg = cfg.packageForStruct(name);
        PrintWriter writer = null;
        PrintWriter cWriter = null;
        try {
            writer = openFile(cfg.javaOutputDir() + File.separator + CodeGenUtils.packageAsPath(structClassPkg) + File.separator + containingTypeName + suffix + ".java");
            CodeGenUtils.emitAutogeneratedWarning(writer, this);
            if (needsNativeCode) {
                String nRoot = cfg.nativeOutputDir();
                if (cfg.nativeOutputUsesJavaHierarchy()) {
                    nRoot += File.separator + CodeGenUtils.packageAsPath(cfg.packageName());
                }
                cWriter = openFile(nRoot + File.separator + containingTypeName + "_JNI.c");
                CodeGenUtils.emitAutogeneratedWarning(cWriter, this);
                emitCHeader(cWriter, containingTypeName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to open files for emission of struct class", e);
        }
        writer.println();
        writer.println("package " + structClassPkg + ";");
        writer.println();
        writer.println("import java.nio.*;");
        writer.println();
        writer.println("import " + cfg.gluegenRuntimePackage() + ".*;");
        writer.println();
        List imports = cfg.imports();
        for (Iterator iter = imports.iterator(); iter.hasNext(); ) {
            writer.print("import ");
            writer.print(iter.next());
            writer.println(";");
        }
        List javadoc = cfg.javadocForClass(containingTypeName);
        for (Iterator iter = javadoc.iterator(); iter.hasNext(); ) {
            writer.println((String) iter.next());
        }
        writer.println();
        writer.print((doBaseClass ? "public " : "") + (doBaseClass ? "abstract " : "") + "class " + containingTypeName + suffix + " ");
        if (!doBaseClass) {
            writer.print("extends " + containingTypeName + " ");
        }
        boolean firstIteration = true;
        List userSpecifiedInterfaces = cfg.implementedInterfaces(containingTypeName);
        for (Iterator iter = userSpecifiedInterfaces.iterator(); iter.hasNext(); ) {
            if (firstIteration) {
                writer.print("implements ");
            }
            firstIteration = false;
            writer.print(iter.next());
            writer.print(" ");
        }
        writer.println("{");
        if (doBaseClass) {
            writer.println("  StructAccessor accessor;");
            writer.println();
        }
        writer.println("  public static int size() {");
        if (doBaseClass) {
            writer.println("    if (CPU.is32Bit()) {");
            writer.println("      return " + containingTypeName + "32" + ".size();");
            writer.println("    } else {");
            writer.println("      return " + containingTypeName + "64" + ".size();");
            writer.println("    }");
        } else {
            writer.println("    return " + structType.getSize(intMachDesc) + ";");
        }
        writer.println("  }");
        writer.println();
        if (doBaseClass) {
            writer.println("  public static " + containingTypeName + " create() {");
            writer.println("    return create(BufferFactory.newDirectByteBuffer(size()));");
            writer.println("  }");
            writer.println();
            writer.println("  public static " + containingTypeName + " create(java.nio.ByteBuffer buf) {");
            writer.println("    if (CPU.is32Bit()) {");
            writer.println("      return new " + containingTypeName + "32(buf);");
            writer.println("    } else {");
            writer.println("      return new " + containingTypeName + "64(buf);");
            writer.println("    }");
            writer.println("  }");
            writer.println();
            writer.println("  " + containingTypeName + "(java.nio.ByteBuffer buf) {");
            writer.println("    accessor = new StructAccessor(buf);");
            writer.println("  }");
            writer.println();
            writer.println("  public java.nio.ByteBuffer getBuffer() {");
            writer.println("    return accessor.getBuffer();");
            writer.println("  }");
        } else {
            writer.println("  " + containingTypeName + suffix + "(java.nio.ByteBuffer buf) {");
            writer.println("    super(buf);");
            writer.println("  }");
            writer.println();
        }
        for (int i = 0; i < structType.getNumFields(); i++) {
            Field field = structType.getField(i);
            Type fieldType = field.getType();
            if (!cfg.shouldIgnore(name + " " + field.getName())) {
                if (fieldType.isFunctionPointer()) {
                    if (doBaseClass) {
                        try {
                            FunctionType funcType = fieldType.asPointer().getTargetType().asFunction();
                            FunctionSymbol funcSym = new FunctionSymbol(field.getName(), funcType);
                            MethodBinding binding = bindFunction(funcSym, containingType, containingCType, machDesc64);
                            binding.findThisPointer();
                            writer.println();
                            JavaMethodBindingEmitter emitter = new JavaMethodBindingEmitter(binding, writer, cfg.runtimeExceptionType(), true, cfg.tagNativeBinding(), false, true, false, false, false, false);
                            emitter.addModifier(JavaMethodBindingEmitter.PUBLIC);
                            emitter.emit();
                            emitter = new JavaMethodBindingEmitter(binding, writer, cfg.runtimeExceptionType(), false, cfg.tagNativeBinding(), true, true, true, true, false, false);
                            emitter.addModifier(JavaMethodBindingEmitter.PRIVATE);
                            emitter.addModifier(JavaMethodBindingEmitter.NATIVE);
                            emitter.emit();
                            CMethodBindingEmitter cEmitter = new CMethodBindingEmitter(binding, cWriter, structClassPkg, containingTypeName, true, false, true, false);
                            cEmitter.emit();
                        } catch (Exception e) {
                            System.err.println("While processing field " + field + " of type " + name + ":");
                            throw (e);
                        }
                    }
                } else if (fieldType.isCompound()) {
                    if (fieldType.getName() == null) {
                        throw new RuntimeException("Anonymous structs as fields not supported yet (field \"" + field + "\" in type \"" + name + "\")");
                    }
                    writer.println();
                    writer.print("  public " + (doBaseClass ? "abstract " : "") + fieldType.getName() + " " + field.getName() + "()");
                    if (doBaseClass) {
                        writer.println(";");
                    } else {
                        writer.println(" {");
                        writer.println("    return " + fieldType.getName() + ".create(accessor.slice(" + field.getOffset(intMachDesc) + ", " + fieldType.getSize(intMachDesc) + "));");
                        writer.println("  }");
                    }
                } else if (fieldType.isArray()) {
                    if (!doBaseClass) {
                        System.err.println("WARNING: Array fields (field \"" + field + "\" of type \"" + name + "\") not implemented yet");
                    }
                } else {
                    JavaType internalJavaType = null;
                    JavaType externalJavaType = null;
                    try {
                        externalJavaType = typeToJavaType(fieldType, false, extMachDesc);
                        if (!doBaseClass) {
                            internalJavaType = typeToJavaType(fieldType, false, intMachDesc);
                        }
                    } catch (Exception e) {
                        System.err.println("Error occurred while creating accessor for field \"" + field.getName() + "\" in type \"" + name + "\"");
                        e.printStackTrace();
                        throw (e);
                    }
                    if (externalJavaType.isPrimitive()) {
                        String externalJavaTypeName = null;
                        String internalJavaTypeName = null;
                        externalJavaTypeName = externalJavaType.getName();
                        if (!doBaseClass) {
                            internalJavaTypeName = internalJavaType.getName();
                        }
                        if (isOpaque(fieldType)) {
                            externalJavaTypeName = compatiblePrimitiveJavaTypeName(fieldType, externalJavaType, extMachDesc);
                            if (!doBaseClass) {
                                internalJavaTypeName = compatiblePrimitiveJavaTypeName(fieldType, internalJavaType, intMachDesc);
                            }
                        }
                        String capitalized = null;
                        if (!doBaseClass) {
                            capitalized = "" + Character.toUpperCase(internalJavaTypeName.charAt(0)) + internalJavaTypeName.substring(1);
                        }
                        int slot = -1;
                        if (!doBaseClass) {
                            slot = slot(fieldType, (int) field.getOffset(intMachDesc), intMachDesc);
                        }
                        writer.println();
                        writer.print("  public " + (doBaseClass ? "abstract " : "") + containingTypeName + " " + field.getName() + "(" + externalJavaTypeName + " val)");
                        if (doBaseClass) {
                            writer.println(";");
                        } else {
                            writer.println(" {");
                            writer.print("    accessor.set" + capitalized + "At(" + slot + ", ");
                            if (!externalJavaTypeName.equals(internalJavaTypeName)) {
                                writer.print("(" + internalJavaTypeName + ") ");
                            }
                            writer.println("val);");
                            writer.println("    return this;");
                            writer.println("  }");
                        }
                        writer.println();
                        writer.print("  public " + (doBaseClass ? "abstract " : "") + externalJavaTypeName + " " + field.getName() + "()");
                        if (doBaseClass) {
                            writer.println(";");
                        } else {
                            writer.println(" {");
                            writer.print("    return ");
                            if (!externalJavaTypeName.equals(internalJavaTypeName)) {
                                writer.print("(" + externalJavaTypeName + ") ");
                            }
                            writer.println("accessor.get" + capitalized + "At(" + slot + ");");
                            writer.println("  }");
                        }
                    } else {
                        System.err.println("WARNING: Complicated fields (field \"" + field + "\" of type \"" + name + "\") not implemented yet");
                    }
                }
            }
        }
        if (doBaseClass) {
            emitCustomJavaCode(writer, containingTypeName);
        }
        writer.println("}");
        writer.flush();
        writer.close();
        if (needsNativeCode) {
            cWriter.flush();
            cWriter.close();
        }
    }

    public void endStructs() throws Exception {
    }

    private JavaType typeToJavaType(Type cType, boolean outgoingArgument, MachineDescription curMachDesc) {
        PointerType opt = cType.asPointer();
        if ((opt != null) && (opt.getTargetType().getName() != null) && (opt.getTargetType().getName().equals("JNIEnv"))) {
            return JavaType.createForJNIEnv();
        }
        TypeInfo info = cfg.typeInfo(cType, typedefDictionary);
        if (info != null) {
            return info.javaType();
        }
        Type t = cType;
        if (t.isInt() || t.isEnum()) {
            switch((int) t.getSize(curMachDesc)) {
                case 1:
                    return javaType(Byte.TYPE);
                case 2:
                    return javaType(Short.TYPE);
                case 4:
                    return javaType(Integer.TYPE);
                case 8:
                    return javaType(Long.TYPE);
                default:
                    throw new RuntimeException("Unknown integer type of size " + t.getSize(curMachDesc) + " and name " + t.getName());
            }
        } else if (t.isFloat()) {
            return javaType(Float.TYPE);
        } else if (t.isDouble()) {
            return javaType(Double.TYPE);
        } else if (t.isVoid()) {
            return javaType(Void.TYPE);
        } else {
            if (t.pointerDepth() > 0 || t.arrayDimension() > 0) {
                Type targetType;
                if (t.isPointer()) {
                    targetType = t.asPointer().getTargetType();
                } else {
                    targetType = t.asArray().getElementType();
                }
                if (t.pointerDepth() == 1 || t.arrayDimension() == 1) {
                    if (targetType.isVoid()) {
                        return JavaType.createForVoidPointer();
                    } else if (targetType.isInt()) {
                        switch((int) targetType.getSize(curMachDesc)) {
                            case 1:
                                return JavaType.createForCCharPointer();
                            case 2:
                                return JavaType.createForCShortPointer();
                            case 4:
                                return JavaType.createForCInt32Pointer();
                            case 8:
                                return JavaType.createForCInt64Pointer();
                            default:
                                throw new RuntimeException("Unknown integer array type of size " + t.getSize(curMachDesc) + " and name " + t.getName());
                        }
                    } else if (targetType.isFloat()) {
                        return JavaType.createForCFloatPointer();
                    } else if (targetType.isDouble()) {
                        return JavaType.createForCDoublePointer();
                    } else if (targetType.isCompound()) {
                        if (t.isArray()) {
                            throw new RuntimeException("Arrays of compound types not handled yet");
                        }
                        if (t.getName() != null && t.getName().equals("jobject")) {
                            return javaType(java.lang.Object.class);
                        }
                        String name = targetType.getName();
                        if (name == null) {
                            name = t.getName();
                            if (name == null) {
                                throw new RuntimeException("Couldn't find a proper type name for pointer type " + t);
                            }
                        }
                        return JavaType.createForCStruct(cfg.renameJavaType(name));
                    } else {
                        throw new RuntimeException("Don't know how to convert pointer/array type \"" + t + "\"");
                    }
                } else if (t.pointerDepth() == 2 || t.arrayDimension() == 2) {
                    Type bottomType;
                    if (targetType.isPointer()) {
                        bottomType = targetType.asPointer().getTargetType();
                    } else {
                        bottomType = targetType.asArray().getElementType();
                    }
                    if (bottomType.isPrimitive()) {
                        if (bottomType.isInt()) {
                            switch((int) bottomType.getSize(curMachDesc)) {
                                case 1:
                                    return javaType(ArrayTypes.byteBufferArrayClass);
                                case 2:
                                    return javaType(ArrayTypes.shortBufferArrayClass);
                                case 4:
                                    return javaType(ArrayTypes.intBufferArrayClass);
                                case 8:
                                    return javaType(ArrayTypes.longBufferArrayClass);
                                default:
                                    throw new RuntimeException("Unknown two-dimensional integer array type of element size " + bottomType.getSize(curMachDesc) + " and name " + bottomType.getName());
                            }
                        } else if (bottomType.isFloat()) {
                            return javaType(ArrayTypes.floatBufferArrayClass);
                        } else if (bottomType.isDouble()) {
                            return javaType(ArrayTypes.doubleBufferArrayClass);
                        } else {
                            throw new RuntimeException("Unexpected primitive type " + bottomType.getName() + " in two-dimensional array");
                        }
                    } else if (bottomType.isVoid()) {
                        return javaType(ArrayTypes.bufferArrayClass);
                    } else if (targetType.isPointer() && (targetType.pointerDepth() == 1) && targetType.asPointer().getTargetType().isCompound()) {
                        return JavaType.createForCArray(targetType);
                    } else {
                        throw new RuntimeException("Could not convert C type \"" + t + "\" " + "to appropriate Java type; need to add more support for " + "depth=2 pointer/array types [debug info: targetType=\"" + targetType + "\"]");
                    }
                } else {
                    throw new RuntimeException("Could not convert C pointer/array \"" + t + "\" to " + "appropriate Java type; types with pointer/array depth " + "greater than 2 are not yet supported [debug info: " + "pointerDepth=" + t.pointerDepth() + " arrayDimension=" + t.arrayDimension() + " targetType=\"" + targetType + "\"]");
                }
            } else {
                throw new RuntimeException("Could not convert C type \"" + t + "\" (class " + t.getClass().getName() + ") to appropriate Java type");
            }
        }
    }

    private static boolean isIntegerType(Class c) {
        return ((c == Byte.TYPE) || (c == Short.TYPE) || (c == Character.TYPE) || (c == Integer.TYPE) || (c == Long.TYPE));
    }

    private int slot(Type t, int byteOffset, MachineDescription curMachDesc) {
        if (t.isInt()) {
            switch((int) t.getSize(curMachDesc)) {
                case 1:
                case 2:
                case 4:
                case 8:
                    return byteOffset / (int) t.getSize(curMachDesc);
                default:
                    throw new RuntimeException("Illegal type");
            }
        } else if (t.isFloat()) {
            return byteOffset / 4;
        } else if (t.isDouble()) {
            return byteOffset / 8;
        } else if (t.isPointer()) {
            return byteOffset / curMachDesc.pointerSizeInBytes();
        } else {
            throw new RuntimeException("Illegal type " + t);
        }
    }

    private StructLayout getLayout() {
        if (layout == null) {
            layout = StructLayout.createForCurrentPlatform();
        }
        return layout;
    }

    protected PrintWriter openFile(String filename) throws IOException {
        File file = new File(filename);
        String parentDir = file.getParent();
        if (parentDir != null) {
            File pDirFile = new File(parentDir);
            pDirFile.mkdirs();
        }
        return new PrintWriter(new BufferedWriter(new FileWriter(file)));
    }

    private boolean isOpaque(Type type) {
        return (cfg.typeInfo(type, typedefDictionary) != null);
    }

    private String compatiblePrimitiveJavaTypeName(Type fieldType, JavaType javaType, MachineDescription curMachDesc) {
        Class c = javaType.getJavaClass();
        if (!isIntegerType(c)) {
            throw new RuntimeException("Can't yet handle opaque definitions of structs' fields to non-integer types (byte, short, int, long, etc.)");
        }
        switch((int) fieldType.getSize(curMachDesc)) {
            case 1:
                return "byte";
            case 2:
                return "short";
            case 4:
                return "int";
            case 8:
                return "long";
            default:
                throw new RuntimeException("Can't handle opaque definitions if the starting type isn't compatible with integral types");
        }
    }

    private void openWriters() throws IOException {
        String jRoot = null;
        if (cfg.allStatic() || cfg.emitInterface()) {
            jRoot = cfg.javaOutputDir() + File.separator + CodeGenUtils.packageAsPath(cfg.packageName());
        }
        String jImplRoot = null;
        if (!cfg.allStatic()) {
            jImplRoot = cfg.javaOutputDir() + File.separator + CodeGenUtils.packageAsPath(cfg.implPackageName());
        }
        String nRoot = cfg.nativeOutputDir();
        if (cfg.nativeOutputUsesJavaHierarchy()) {
            nRoot += File.separator + CodeGenUtils.packageAsPath(cfg.packageName());
        }
        if (cfg.allStatic() || cfg.emitInterface()) {
            javaWriter = openFile(jRoot + File.separator + cfg.className() + ".java");
        }
        if (!cfg.allStatic() && cfg.emitImpl()) {
            javaImplWriter = openFile(jImplRoot + File.separator + cfg.implClassName() + ".java");
        }
        if (cfg.emitImpl()) {
            cWriter = openFile(nRoot + File.separator + cfg.implClassName() + "_JNI.c");
        }
        if (javaWriter != null) {
            CodeGenUtils.emitAutogeneratedWarning(javaWriter, this);
        }
        if (javaImplWriter != null) {
            CodeGenUtils.emitAutogeneratedWarning(javaImplWriter, this);
        }
        if (cWriter != null) {
            CodeGenUtils.emitAutogeneratedWarning(cWriter, this);
        }
    }

    protected PrintWriter javaWriter() {
        if (!cfg.allStatic() && !cfg.emitInterface()) {
            throw new InternalError("Should not call this");
        }
        return javaWriter;
    }

    protected PrintWriter javaImplWriter() {
        if (cfg.allStatic() || !cfg.emitImpl()) {
            throw new InternalError("Should not call this");
        }
        return javaImplWriter;
    }

    protected PrintWriter cWriter() {
        if (!cfg.emitImpl()) {
            throw new InternalError("Should not call this");
        }
        return cWriter;
    }

    private void closeWriter(PrintWriter writer) throws IOException {
        writer.flush();
        writer.close();
    }

    private void closeWriters() throws IOException {
        if (javaWriter != null) {
            closeWriter(javaWriter);
        }
        if (javaImplWriter != null) {
            closeWriter(javaImplWriter);
        }
        if (cWriter != null) {
            closeWriter(cWriter);
        }
        javaWriter = null;
        javaImplWriter = null;
        cWriter = null;
    }

    /**
   * Returns the value that was specified by the configuration directive
   * "JavaOutputDir", or the default if none was specified.
   */
    protected String getJavaOutputDir() {
        return cfg.javaOutputDir();
    }

    /**
   * Returns the value that was specified by the configuration directive
   * "Package", or the default if none was specified.
   */
    protected String getJavaPackageName() {
        return cfg.packageName();
    }

    /**
   * Returns the value that was specified by the configuration directive
   * "ImplPackage", or the default if none was specified.
   */
    protected String getImplPackageName() {
        return cfg.implPackageName();
    }

    /**
   * Emit all the strings specified in the "CustomJavaCode" parameters of
   * the configuration file.
   */
    protected void emitCustomJavaCode(PrintWriter writer, String className) throws Exception {
        List code = cfg.customJavaCodeForClass(className);
        if (code.size() == 0) return;
        writer.println();
        writer.println("  // --- Begin CustomJavaCode .cfg declarations");
        for (Iterator iter = code.iterator(); iter.hasNext(); ) {
            writer.println((String) iter.next());
        }
        writer.println("  // ---- End CustomJavaCode .cfg declarations");
    }

    /**
   * Write out any header information for the output files (class declaration
   * and opening brace, import statements, etc).
   */
    protected void emitAllFileHeaders() throws IOException {
        try {
            if (cfg.allStatic() || cfg.emitInterface()) {
                String[] interfaces;
                List userSpecifiedInterfaces = null;
                if (cfg.emitInterface()) {
                    userSpecifiedInterfaces = cfg.extendedInterfaces(cfg.className());
                } else {
                    userSpecifiedInterfaces = cfg.implementedInterfaces(cfg.className());
                }
                interfaces = new String[userSpecifiedInterfaces.size()];
                userSpecifiedInterfaces.toArray(interfaces);
                final List intfDocs = cfg.javadocForClass(cfg.className());
                CodeGenUtils.EmissionCallback docEmitter = new CodeGenUtils.EmissionCallback() {

                    public void emit(PrintWriter w) {
                        for (Iterator iter = intfDocs.iterator(); iter.hasNext(); ) {
                            w.println((String) iter.next());
                        }
                    }
                };
                CodeGenUtils.emitJavaHeaders(javaWriter, cfg.packageName(), cfg.className(), cfg.gluegenRuntimePackage(), cfg.allStatic() ? true : false, (String[]) cfg.imports().toArray(new String[] {}), new String[] { "public" }, interfaces, null, docEmitter);
            }
            if (!cfg.allStatic() && cfg.emitImpl()) {
                final List implDocs = cfg.javadocForClass(cfg.implClassName());
                CodeGenUtils.EmissionCallback docEmitter = new CodeGenUtils.EmissionCallback() {

                    public void emit(PrintWriter w) {
                        for (Iterator iter = implDocs.iterator(); iter.hasNext(); ) {
                            w.println((String) iter.next());
                        }
                    }
                };
                String[] interfaces;
                List userSpecifiedInterfaces = null;
                userSpecifiedInterfaces = cfg.implementedInterfaces(cfg.implClassName());
                int additionalNum = 0;
                if (cfg.className() != null) {
                    additionalNum = 1;
                }
                interfaces = new String[additionalNum + userSpecifiedInterfaces.size()];
                userSpecifiedInterfaces.toArray(interfaces);
                if (additionalNum == 1) {
                    interfaces[userSpecifiedInterfaces.size()] = cfg.className();
                }
                CodeGenUtils.emitJavaHeaders(javaImplWriter, cfg.implPackageName(), cfg.implClassName(), cfg.gluegenRuntimePackage(), true, (String[]) cfg.imports().toArray(new String[] {}), new String[] { "public" }, interfaces, null, docEmitter);
            }
            if (cfg.emitImpl()) {
                PrintWriter cWriter = cWriter();
                emitCHeader(cWriter, cfg.implClassName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error emitting all file headers: cfg.allStatic()=" + cfg.allStatic() + " cfg.emitImpl()=" + cfg.emitImpl() + " cfg.emitInterface()=" + cfg.emitInterface(), e);
        }
    }

    protected void emitCHeader(PrintWriter cWriter, String className) {
        cWriter.println("#include <jni.h>");
        cWriter.println();
        if (getConfig().emitImpl()) {
            cWriter.println("#include <assert.h>");
            cWriter.println();
        }
        for (Iterator iter = cfg.customCCode().iterator(); iter.hasNext(); ) {
            cWriter.println((String) iter.next());
        }
        cWriter.println();
    }

    /**
   * Write out any footer information for the output files (closing brace of
   * class definition, etc).
   */
    protected void emitAllFileFooters() {
        if (cfg.allStatic() || cfg.emitInterface()) {
            javaWriter().println();
            javaWriter().println("} // end of class " + cfg.className());
        }
        if (!cfg.allStatic() && cfg.emitImpl()) {
            javaImplWriter().println();
            javaImplWriter().println("} // end of class " + cfg.implClassName());
        }
    }

    private JavaType javaType(Class c) {
        return JavaType.createForClass(c);
    }

    /** Maps the C types in the specified function to Java types through
      the MethodBinding interface. Note that the JavaTypes in the
      returned MethodBinding are "intermediate" JavaTypes (some
      potentially representing C pointers rather than true Java types)
      and must be lowered to concrete Java types before creating
      emitters for them. */
    private MethodBinding bindFunction(FunctionSymbol sym, JavaType containingType, Type containingCType, MachineDescription curMachDesc) {
        MethodBinding binding = new MethodBinding(sym, containingType, containingCType);
        binding.setRenamedMethodName(cfg.getJavaMethodRename(sym.getName()));
        if (cfg.returnsString(binding.getName())) {
            PointerType prt = sym.getReturnType().asPointer();
            if (prt == null || prt.getTargetType().asInt() == null || prt.getTargetType().getSize(curMachDesc) != 1) {
                throw new RuntimeException("Cannot apply ReturnsString configuration directive to \"" + sym + "\". ReturnsString requires native method to have return type \"char *\"");
            }
            binding.setJavaReturnType(javaType(java.lang.String.class));
        } else {
            binding.setJavaReturnType(typeToJavaType(sym.getReturnType(), false, curMachDesc));
        }
        List stringArgIndices = cfg.stringArguments(binding.getName());
        for (int i = 0; i < sym.getNumArguments(); i++) {
            Type cArgType = sym.getArgumentType(i);
            JavaType mappedType = typeToJavaType(cArgType, true, curMachDesc);
            if (stringArgIndices != null && stringArgIndices.contains(new Integer(i))) {
                if (mappedType.isCVoidPointerType() || mappedType.isCCharPointerType() || (mappedType.isArray() && mappedType.getJavaClass() == ArrayTypes.byteBufferArrayClass)) {
                    if (mappedType.getJavaClass() == ArrayTypes.byteBufferArrayClass) {
                        mappedType = javaType(ArrayTypes.stringArrayClass);
                    } else {
                        mappedType = javaType(String.class);
                    }
                } else {
                    throw new RuntimeException("Cannot apply ArgumentIsString configuration directive to " + "argument " + i + " of \"" + sym + "\": argument type is not " + "a \"void*\", \"char *\", or \"char**\" equivalent");
                }
            }
            binding.addJavaArgumentType(mappedType);
        }
        return binding;
    }

    private MethodBinding lowerMethodBindingPointerTypes(MethodBinding inputBinding, boolean convertToArrays, boolean[] canProduceArrayVariant) {
        MethodBinding result = inputBinding;
        boolean arrayPossible = false;
        for (int i = 0; i < inputBinding.getNumArguments(); i++) {
            JavaType t = inputBinding.getJavaArgumentType(i);
            if (t.isCPrimitivePointerType()) {
                if (t.isCVoidPointerType()) {
                    result = result.replaceJavaArgumentType(i, JavaType.forNIOBufferClass());
                } else if (t.isCCharPointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.byteArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIOByteBufferClass());
                    }
                } else if (t.isCShortPointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.shortArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIOShortBufferClass());
                    }
                } else if (t.isCInt32PointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.intArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIOIntBufferClass());
                    }
                } else if (t.isCInt64PointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.longArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIOLongBufferClass());
                    }
                } else if (t.isCFloatPointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.floatArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIOFloatBufferClass());
                    }
                } else if (t.isCDoublePointerType()) {
                    arrayPossible = true;
                    if (convertToArrays) {
                        result = result.replaceJavaArgumentType(i, javaType(ArrayTypes.doubleArrayClass));
                    } else {
                        result = result.replaceJavaArgumentType(i, JavaType.forNIODoubleBufferClass());
                    }
                } else {
                    throw new RuntimeException("Unknown C pointer type " + t);
                }
            }
        }
        JavaType t = result.getJavaReturnType();
        if (t.isCPrimitivePointerType()) {
            if (t.isCVoidPointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOByteBufferClass());
            } else if (t.isCCharPointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOByteBufferClass());
            } else if (t.isCShortPointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOShortBufferClass());
            } else if (t.isCInt32PointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOIntBufferClass());
            } else if (t.isCInt64PointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOLongBufferClass());
            } else if (t.isCFloatPointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIOFloatBufferClass());
            } else if (t.isCDoublePointerType()) {
                result = result.replaceJavaArgumentType(-1, JavaType.forNIODoubleBufferClass());
            } else {
                throw new RuntimeException("Unknown C pointer type " + t);
            }
        }
        if (canProduceArrayVariant != null) {
            canProduceArrayVariant[0] = arrayPossible;
        }
        return result;
    }

    protected List expandMethodBinding(MethodBinding binding) {
        List result = new ArrayList();
        boolean[] canProduceArrayVariant = new boolean[1];
        if (binding.signatureUsesCPrimitivePointers() || binding.signatureUsesCVoidPointers() || binding.signatureUsesCArrays()) {
            result.add(lowerMethodBindingPointerTypes(binding, false, canProduceArrayVariant));
            if (canProduceArrayVariant[0] && (binding.signatureUsesCPrimitivePointers() || binding.signatureUsesCArrays()) && !cfg.nioDirectOnly(binding.getName())) {
                result.add(lowerMethodBindingPointerTypes(binding, true, null));
            }
        } else {
            result.add(binding);
        }
        return result;
    }

    private String resultName() {
        return "_res";
    }

    private Type canonicalize(Type t) {
        Type res = (Type) canonMap.get(t);
        if (res != null) {
            return res;
        }
        canonMap.put(t, t);
        return t;
    }
}
