package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.util.StringMatcher;

/**
 * This InstructionVisitor initializes any constant <code>Class.forName</code> or
 * <code>.class</code> references of all classes it visits. More specifically,
 * it fills out the references of string constant pool entries that refer to a
 * class in the program class pool or in the library class pool.
 * <p>
 * It optionally prints notes if on usage of
 * <code>(SomeClass)Class.forName(variable).newInstance()</code>.
 * <p>
 * The class hierarchy must be initialized before using this visitor.
 *
 * @see ClassSuperHierarchyInitializer
 *
 * @author Eric Lafortune
 */
public class DynamicClassReferenceInitializer extends SimplifiedVisitor implements InstructionVisitor, ConstantVisitor, AttributeVisitor {

    public static final int X = InstructionSequenceMatcher.X;

    public static final int Y = InstructionSequenceMatcher.Y;

    public static final int Z = InstructionSequenceMatcher.Z;

    public static final int A = InstructionSequenceMatcher.A;

    public static final int B = InstructionSequenceMatcher.B;

    public static final int C = InstructionSequenceMatcher.C;

    public static final int D = InstructionSequenceMatcher.D;

    private final Constant[] CLASS_FOR_NAME_CONSTANTS = new Constant[] { new MethodrefConstant(1, 2, null, null), new ClassConstant(3, null), new NameAndTypeConstant(4, 5), new Utf8Constant(ClassConstants.INTERNAL_NAME_JAVA_LANG_CLASS), new Utf8Constant(ClassConstants.INTERNAL_METHOD_NAME_CLASS_FOR_NAME), new Utf8Constant(ClassConstants.INTERNAL_METHOD_TYPE_CLASS_FOR_NAME), new MethodrefConstant(1, 7, null, null), new NameAndTypeConstant(8, 9), new Utf8Constant(ClassConstants.INTERNAL_METHOD_NAME_NEW_INSTANCE), new Utf8Constant(ClassConstants.INTERNAL_METHOD_TYPE_NEW_INSTANCE), new MethodrefConstant(1, 11, null, null), new NameAndTypeConstant(12, 13), new Utf8Constant(ClassConstants.INTERNAL_METHOD_NAME_CLASS_GET_COMPONENT_TYPE), new Utf8Constant(ClassConstants.INTERNAL_METHOD_TYPE_CLASS_GET_COMPONENT_TYPE) };

    private final Instruction[] CONSTANT_CLASS_FOR_NAME_INSTRUCTIONS = new Instruction[] { new ConstantInstruction(InstructionConstants.OP_LDC, X), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0) };

    private final Instruction[] CLASS_FOR_NAME_CAST_INSTRUCTIONS = new Instruction[] { new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0), new ConstantInstruction(InstructionConstants.OP_INVOKEVIRTUAL, 6), new ConstantInstruction(InstructionConstants.OP_CHECKCAST, X) };

    private final Constant[] DOT_CLASS_JAVAC_CONSTANTS = new Constant[] { new MethodrefConstant(A, 1, null, null), new NameAndTypeConstant(B, 2), new Utf8Constant(ClassConstants.INTERNAL_METHOD_TYPE_DOT_CLASS_JAVAC) };

    private final Instruction[] DOT_CLASS_JAVAC_INSTRUCTIONS = new Instruction[] { new ConstantInstruction(InstructionConstants.OP_LDC, X), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0) };

    private final Constant[] DOT_CLASS_JIKES_CONSTANTS = new Constant[] { new MethodrefConstant(A, 1, null, null), new NameAndTypeConstant(B, 2), new Utf8Constant(ClassConstants.INTERNAL_METHOD_TYPE_DOT_CLASS_JIKES) };

    private final Instruction[] DOT_CLASS_JIKES_INSTRUCTIONS = new Instruction[] { new ConstantInstruction(InstructionConstants.OP_LDC, X), new SimpleInstruction(InstructionConstants.OP_ICONST_0), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0) };

    private final Instruction[] DOT_CLASS_JAVAC_IMPLEMENTATION_INSTRUCTIONS = new Instruction[] { new VariableInstruction(InstructionConstants.OP_ALOAD_0), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0), new SimpleInstruction(InstructionConstants.OP_ARETURN) };

    private final Instruction[] DOT_CLASS_JIKES_IMPLEMENTATION_INSTRUCTIONS = new Instruction[] { new VariableInstruction(InstructionConstants.OP_ALOAD_0), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0), new VariableInstruction(InstructionConstants.OP_ALOAD_1), new BranchInstruction(InstructionConstants.OP_IFNE, +6), new ConstantInstruction(InstructionConstants.OP_INVOKEVIRTUAL, 10), new SimpleInstruction(InstructionConstants.OP_ARETURN) };

    private final Instruction[] DOT_CLASS_JIKES_IMPLEMENTATION_INSTRUCTIONS2 = new Instruction[] { new VariableInstruction(InstructionConstants.OP_ALOAD_0), new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, 0), new ConstantInstruction(InstructionConstants.OP_INVOKEVIRTUAL, 10), new SimpleInstruction(InstructionConstants.OP_ARETURN) };

    private final ClassPool programClassPool;

    private final ClassPool libraryClassPool;

    private final WarningPrinter missingNotePrinter;

    private final WarningPrinter dependencyWarningPrinter;

    private final WarningPrinter notePrinter;

    private final StringMatcher noteExceptionMatcher;

    private final InstructionSequenceMatcher constantClassForNameMatcher = new InstructionSequenceMatcher(CLASS_FOR_NAME_CONSTANTS, CONSTANT_CLASS_FOR_NAME_INSTRUCTIONS);

    private final InstructionSequenceMatcher classForNameCastMatcher = new InstructionSequenceMatcher(CLASS_FOR_NAME_CONSTANTS, CLASS_FOR_NAME_CAST_INSTRUCTIONS);

    private final InstructionSequenceMatcher dotClassJavacMatcher = new InstructionSequenceMatcher(DOT_CLASS_JAVAC_CONSTANTS, DOT_CLASS_JAVAC_INSTRUCTIONS);

    private final InstructionSequenceMatcher dotClassJikesMatcher = new InstructionSequenceMatcher(DOT_CLASS_JIKES_CONSTANTS, DOT_CLASS_JIKES_INSTRUCTIONS);

    private final InstructionSequenceMatcher dotClassJavacImplementationMatcher = new InstructionSequenceMatcher(CLASS_FOR_NAME_CONSTANTS, DOT_CLASS_JAVAC_IMPLEMENTATION_INSTRUCTIONS);

    private final InstructionSequenceMatcher dotClassJikesImplementationMatcher = new InstructionSequenceMatcher(CLASS_FOR_NAME_CONSTANTS, DOT_CLASS_JIKES_IMPLEMENTATION_INSTRUCTIONS);

    private final InstructionSequenceMatcher dotClassJikesImplementationMatcher2 = new InstructionSequenceMatcher(CLASS_FOR_NAME_CONSTANTS, DOT_CLASS_JIKES_IMPLEMENTATION_INSTRUCTIONS2);

    private boolean isClassForNameInvocation;

    /**
     * Creates a new DynamicClassReferenceInitializer that optionally prints
     * warnings and notes, with optional class specifications for which never
     * to print notes.
     */
    public DynamicClassReferenceInitializer(ClassPool programClassPool, ClassPool libraryClassPool, WarningPrinter missingNotePrinter, WarningPrinter dependencyWarningPrinter, WarningPrinter notePrinter, StringMatcher noteExceptionMatcher) {
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.missingNotePrinter = missingNotePrinter;
        this.dependencyWarningPrinter = dependencyWarningPrinter;
        this.notePrinter = notePrinter;
        this.noteExceptionMatcher = noteExceptionMatcher;
    }

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
        instruction.accept(clazz, method, codeAttribute, offset, constantClassForNameMatcher);
        if (constantClassForNameMatcher.isMatching()) {
            clazz.constantPoolEntryAccept(constantClassForNameMatcher.matchedConstantIndex(X), this);
            classForNameCastMatcher.reset();
        }
        instruction.accept(clazz, method, codeAttribute, offset, classForNameCastMatcher);
        if (classForNameCastMatcher.isMatching()) {
            clazz.constantPoolEntryAccept(classForNameCastMatcher.matchedConstantIndex(X), this);
        }
        instruction.accept(clazz, method, codeAttribute, offset, dotClassJavacMatcher);
        if (dotClassJavacMatcher.isMatching() && isDotClassMethodref(clazz, dotClassJavacMatcher.matchedConstantIndex(0))) {
            clazz.constantPoolEntryAccept(dotClassJavacMatcher.matchedConstantIndex(X), this);
        }
        instruction.accept(clazz, method, codeAttribute, offset, dotClassJikesMatcher);
        if (dotClassJikesMatcher.isMatching() && isDotClassMethodref(clazz, dotClassJikesMatcher.matchedConstantIndex(0))) {
            clazz.constantPoolEntryAccept(dotClassJikesMatcher.matchedConstantIndex(X), this);
        }
    }

    /**
     * Fills out the link to the referenced class.
     */
    public void visitStringConstant(Clazz clazz, StringConstant stringConstant) {
        String externalClassName = stringConstant.getString(clazz);
        String internalClassName = ClassUtil.internalClassName(externalClassName);
        stringConstant.referencedClass = findClass(clazz.getName(), internalClassName);
    }

    /**
     * Prints out a note about the class cast to this class, if applicable.
     */
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {
        if (noteExceptionMatcher == null || !noteExceptionMatcher.matches(classConstant.getName(clazz))) {
            notePrinter.print(clazz.getName(), classConstant.getName(clazz), "Note: " + ClassUtil.externalClassName(clazz.getName()) + " calls '(" + ClassUtil.externalClassName(classConstant.getName(clazz)) + ")Class.forName(variable).newInstance()'");
        }
    }

    /**
     * Checks whether the referenced method is a .class method.
     */
    public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
        String methodType = methodrefConstant.getType(clazz);
        if (methodType.equals(ClassConstants.INTERNAL_METHOD_TYPE_DOT_CLASS_JAVAC) || methodType.equals(ClassConstants.INTERNAL_METHOD_TYPE_DOT_CLASS_JIKES)) {
            String methodName = methodrefConstant.getName(clazz);
            isClassForNameInvocation = methodName.equals(ClassConstants.INTERNAL_METHOD_NAME_DOT_CLASS_JAVAC) || methodName.equals(ClassConstants.INTERNAL_METHOD_NAME_DOT_CLASS_JIKES);
            if (isClassForNameInvocation) {
                return;
            }
            String className = methodrefConstant.getClassName(clazz);
            Clazz referencedClass = programClassPool.getClass(className);
            if (referencedClass != null) {
                referencedClass.methodAccept(methodName, methodType, new AllAttributeVisitor(this));
            }
        }
    }

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {
    }

    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        isClassForNameInvocation = isDotClassMethodCode(clazz, method, codeAttribute, dotClassJavacImplementationMatcher, 5) || isDotClassMethodCode(clazz, method, codeAttribute, dotClassJikesImplementationMatcher, 12) || isDotClassMethodCode(clazz, method, codeAttribute, dotClassJikesImplementationMatcher2, 8);
    }

    /**
     * Returns whether the given method reference corresponds to a .class
     * method, as generated by javac or by jikes.
     */
    private boolean isDotClassMethodref(Clazz clazz, int methodrefConstantIndex) {
        isClassForNameInvocation = false;
        clazz.constantPoolEntryAccept(methodrefConstantIndex, this);
        return isClassForNameInvocation;
    }

    /**
     * Returns whether the first whether the first instructions of the
     * given code attribute match with the given instruction matcher.
     */
    private boolean isDotClassMethodCode(Clazz clazz, Method method, CodeAttribute codeAttribute, InstructionSequenceMatcher codeMatcher, int codeLength) {
        if (codeAttribute.u4codeLength < codeLength) {
            return false;
        }
        codeMatcher.reset();
        codeAttribute.instructionsAccept(clazz, method, 0, codeLength, codeMatcher);
        return codeMatcher.isMatching();
    }

    /**
     * Returns the class with the given name, either for the program class pool
     * or from the library class pool, or <code>null</code> if it can't be found.
     */
    private Clazz findClass(String referencingClassName, String name) {
        if (ClassUtil.isInternalArrayType(name) && !ClassUtil.isInternalClassType(name)) {
            return null;
        }
        Clazz clazz = programClassPool.getClass(name);
        if (clazz == null) {
            clazz = libraryClassPool.getClass(name);
            if (clazz == null && missingNotePrinter != null) {
                missingNotePrinter.print(referencingClassName, name, "Note: " + ClassUtil.externalClassName(referencingClassName) + ": can't find dynamically referenced class " + ClassUtil.externalClassName(name));
            }
        } else if (dependencyWarningPrinter != null) {
            dependencyWarningPrinter.print(referencingClassName, name, "Warning: library class " + ClassUtil.externalClassName(referencingClassName) + " depends dynamically on program class " + ClassUtil.externalClassName(name));
        }
        return clazz;
    }
}
