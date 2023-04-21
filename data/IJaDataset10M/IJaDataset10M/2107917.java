package de.unkrig.loggifier;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.TraceSignatureVisitor;
import de.unkrig.commons.asm.InsnUtil;
import de.unkrig.commons.asm.OpcodeUtil;
import de.unkrig.commons.asm.Signature;
import de.unkrig.commons.asm.Types;
import de.unkrig.commons.io.charstream.UnexpectedCharacterException;
import de.unkrig.commons.io.objectstream.UnexpectedObjectException;
import de.unkrig.commons.lang.ExceptionUtil;
import de.unkrig.commons.util.Transformer;
import de.unkrig.loggifier.LoggifyingContentsTransformer.Action;
import de.unkrig.loggifier.expression.Expression;
import de.unkrig.loggifier.expression.Parser;
import de.unkrig.loggifier.loglevel.FirstMatchLevelCalculator;
import de.unkrig.loggifier.loglevel.LevelCalculator;

/**
 * Acceptors may throw {@link ExceptionTableEntryOffsetTooLargeException}s iff a try-catch block grows too large during
 * loggification.
 */
class LoggifyingClassAdapter extends ClassAdapter implements Opcodes {

    private static final String HELPER_CLASS_NAME = "de/unkrig/loggifier/runtime/Helper";

    static final MessageFormat ARITH_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.ARITH");

    static final MessageFormat ALOAD_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.ALOAD");

    static final MessageFormat ASTORE_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.ASTORE");

    static final MessageFormat CATCH_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.CATCH");

    static final MessageFormat CLINIT_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.CLINIT");

    static final MessageFormat CONST_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.CONST");

    static final MessageFormat CONVERT_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.CONVERT");

    static final MessageFormat ENTRY_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.ENTRY");

    static final MessageFormat GET_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.GET.{6}");

    static final MessageFormat INVOKE_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.INVOKE");

    static final MessageFormat LOAD_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.LOAD.{6}");

    static final MessageFormat NEW_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.NEW.{6}");

    static final MessageFormat PUT_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.PUT.{6}");

    static final MessageFormat RESULT_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.RESULT");

    static final MessageFormat RETURN_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.RETURN");

    static final MessageFormat STORE_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.STORE.{6}");

    static final MessageFormat SWITCH_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.SWITCH");

    static final MessageFormat THROW_LOGGER_NAME_FORMAT = new MessageFormat("{0}{1}{2}.{4}{5}.THROW");

    static final Object RULE_FIELD_NAME = "$$loggifierRules";

    private static final String LOGGIFY_ANNOTATION_DESCRIPTOR = "Lde/unkrig/loggifier/runtime/annotation/Loggify;";

    public static final InsnList NO_INSNS = new InsnList();

    LevelCalculator classLogLevelCalculator;

    final ErrorHandler errorHandler;

    /**
     * @see Opcodes
     */
    int thisClassAccess;

    /**
     * Internal name of the class being visited, e.g. '{@code pkg/MyClass}'.
     */
    String thisClassName;

    /**
     * Fully qualified name of the class being visited, e.g. '{@code pkg.MyClass}'.
     */
    String sourceClassName;

    String source;

    public MethodNode classInitializer;

    final SortedSet<String> loggerNames = new TreeSet<String>();

    /**
     * @param logLevelCalculators This map determines for which code actions logging code is injected, and on what
     *                            level the action is logged. <b>Must</b> contain entries for <b>all</b> values of
     *                            {@link Action}.
     */
    public LoggifyingClassAdapter(ClassVisitor cv, LevelCalculator logLevelCalculator, ErrorHandler errorHandler) {
        super(cv);
        this.classLogLevelCalculator = logLevelCalculator;
        this.errorHandler = errorHandler;
    }

    @Override
    public void visit(int version, int classAccess, String thisClassName, String optionalClassSignature, String optionalSuperclassName, String[] optionalImplementedInterfaceNames) {
        this.thisClassAccess = classAccess;
        this.thisClassName = thisClassName;
        this.sourceClassName = this.thisClassName.replace('/', '.');
        this.source = null;
        this.classInitializer = null;
        this.loggerNames.clear();
        super.visit(version, classAccess, thisClassName, optionalClassSignature, optionalSuperclassName, optionalImplementedInterfaceNames);
    }

    @Override
    public void visitSource(String source, String debug) {
        this.source = source;
        super.visitSource(source, debug);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String annotationDescriptor, final boolean visible) {
        AnnotationVisitor delegate = super.visitAnnotation(annotationDescriptor, visible);
        if (!annotationDescriptor.equals(LOGGIFY_ANNOTATION_DESCRIPTOR)) {
            return delegate;
        }
        return new FilterAnnotationVisitor(delegate) {

            @Override
            public AnnotationVisitor visitArray(String name) {
                return new FilterAnnotationVisitor(super.visitArray(name)) {

                    private final List<String> lines = new ArrayList<String>();

                    @Override
                    public void visit(String name, Object value) {
                        this.lines.add((String) value);
                        this.delegate.visit(name, value);
                    }

                    @Override
                    public void visitEnd() {
                        LoggifyingClassAdapter.this.classLogLevelCalculator = parseLoggifyAnnotation(this.lines.toArray(new String[this.lines.size()]), LoggifyingClassAdapter.this.classLogLevelCalculator, new ErrorHandler() {

                            @Override
                            public void handle(Throwable t) {
                                LoggifyingClassAdapter.this.errorHandler.handle(ExceptionUtil.wrap(("Parsing the @Loggify annotation of class '" + LoggifyingClassAdapter.this.sourceClassName + "'"), t));
                            }

                            @Override
                            public void handle(String message) {
                                LoggifyingClassAdapter.this.errorHandler.handle("Parsing the @Loggify annotation of class '" + LoggifyingClassAdapter.this.sourceClassName + "': " + message);
                            }
                        });
                        this.delegate.visitEnd();
                    }
                };
            }
        };
    }

    private static class FilterAnnotationVisitor implements AnnotationVisitor {

        protected final AnnotationVisitor delegate;

        public FilterAnnotationVisitor(AnnotationVisitor delegate) {
            this.delegate = delegate;
        }

        @Override
        public void visit(String name, Object value) {
            this.delegate.visit(name, value);
        }

        @Override
        public void visitEnum(String name, String desc, String value) {
            this.delegate.visitEnum(name, desc, value);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String desc) {
            return this.delegate.visitAnnotation(name, desc);
        }

        @Override
        public AnnotationVisitor visitArray(String name) {
            return this.delegate.visitArray(name);
        }

        @Override
        public void visitEnd() {
            this.delegate.visitEnd();
        }
    }

    @Override
    public void visitEnd() {
        LOGGIFY_CLINIT: {
            Level logLevel = this.classLogLevelCalculator.calculate(Action.CLINIT, this.thisClassAccess, this.sourceClassName, Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC, "<clinit>()", 1, null);
            if (logLevel == Level.OFF) break LOGGIFY_CLINIT;
            if (this.classInitializer == null) {
                this.classInitializer = new MethodNode(ACC_PUBLIC | ACC_STATIC, "<clinit>", "()V", null, null);
                this.classInitializer.instructions.add(new InsnNode(RETURN));
            }
            InsnList insns = this.classInitializer.instructions;
            insns.insertBefore(insns.getFirst(), generateLogging(getLoggerName(CLINIT_LOGGER_NAME_FORMAT), logLevel, "<clinit>()", 1, "CLINIT " + this.sourceClassName, InsnUtil.il(new InsnNode(ACONST_NULL)), this.classInitializer));
        }
        for (String loggerName : this.loggerNames) {
            new FieldNode(((this.thisClassAccess & ACC_INTERFACE) != 0 ? ACC_PUBLIC | ACC_STATIC | ACC_FINAL : ACC_PRIVATE | ACC_STATIC | ACC_FINAL), loggerNameToFieldName(loggerName), "Ljava/util/logging/Logger;", null, null).accept(this);
        }
        if (!this.loggerNames.isEmpty()) {
            if (this.classInitializer == null) {
                this.classInitializer = new MethodNode(ACC_PUBLIC | ACC_STATIC, "<clinit>", "()V", null, null);
                this.classInitializer.instructions.add(new InsnNode(RETURN));
            }
            InsnList il = new InsnList();
            for (String loggerName : this.loggerNames) {
                il.add(new LdcInsnNode(loggerName));
                il.add(new MethodInsnNode(INVOKESTATIC, "java/util/logging/Logger", "getLogger", "(Ljava/lang/String;)Ljava/util/logging/Logger;"));
                il.add(new FieldInsnNode(PUTSTATIC, this.thisClassName, loggerNameToFieldName(loggerName), "Ljava/util/logging/Logger;"));
            }
            InsnList insns = this.classInitializer.instructions;
            insns.insertBefore(insns.getFirst(), il);
        }
        if (this.classInitializer != null) {
            this.classInitializer.accept(this.cv);
            checkTryCatchBlocks(this.classInitializer);
        }
        super.visitEnd();
    }

    /**
     * As of version 3.3.1, ASM does not verify that the offsets in a {@link TryCatchBlockNode} are 0xffff or less.
     * Since the consequences are always fatal, we need to check for ourselves.
     *
     * @throw ExceptionTableEntryOffsetTooLargeException
     */
    public static void checkTryCatchBlocks(MethodNode methodNode) {
        @SuppressWarnings("unchecked") List<TryCatchBlockNode> tcbs = methodNode.tryCatchBlocks;
        for (TryCatchBlockNode tcbn : tcbs) {
            if (tcbn.start.getLabel().getOffset() > 0xffff || tcbn.end.getLabel().getOffset() > 0xffff || tcbn.handler.getLabel().getOffset() > 0xffff) {
                throw new ExceptionTableEntryOffsetTooLargeException("Bytecode of method '" + Signature.toString(methodNode) + "grew too large during loggification - please tighten the loggification rules for this method");
            }
        }
    }

    /**
     * Each logger needs a static field, and the name of that static field is derived from the logger name.
     */
    static String loggerNameToFieldName(String loggerName) {
        StringBuilder sb = new StringBuilder("__");
        for (char c : loggerName.toCharArray()) {
            switch(c) {
                case '_':
                    sb.append("__");
                    break;
                case '.':
                    sb.append("_dot_");
                    break;
                case '[':
                    sb.append("_lbracket_");
                    break;
                case ']':
                    sb.append("_rbracket_");
                    break;
                case '(':
                    sb.append("_lparen_");
                    break;
                case ')':
                    sb.append("_rparen_");
                    break;
                case '<':
                    sb.append("_lt_");
                    break;
                case '>':
                    sb.append("_gt_");
                    break;
                default:
                    if (Character.isJavaIdentifierPart(c)) {
                        sb.append(c);
                    } else {
                        sb.append('_').append((int) c).append('_');
                    }
            }
        }
        return sb.toString();
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        if (name.equals(RULE_FIELD_NAME) && value instanceof String) {
            LoggifyingClassAdapter.this.classLogLevelCalculator = parseLoggifyAnnotation(((String) value).split("\n"), LoggifyingClassAdapter.this.classLogLevelCalculator, new ErrorHandler() {

                @Override
                public void handle(Throwable t) {
                    LoggifyingClassAdapter.this.errorHandler.handle(ExceptionUtil.wrap(("Parsing the '" + RULE_FIELD_NAME + "' field of class '" + LoggifyingClassAdapter.this.sourceClassName + "'"), t));
                }

                @Override
                public void handle(String message) {
                    LoggifyingClassAdapter.this.errorHandler.handle("Parsing the '" + RULE_FIELD_NAME + "' field of class '" + LoggifyingClassAdapter.this.sourceClassName + "': " + message);
                }
            });
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(final int methodAccess, final String methodName, final String methodDescriptor, final String optionalMethodSignature, final String[] methodExceptions) {
        class LoggifyingMethodVisitor extends MethodNode {

            LevelCalculator methodLogLevelCalculator = LoggifyingClassAdapter.this.classLogLevelCalculator;

            /**
             * The (enhanced) name of method currently being visisted, e.g.
             * <pre>
             * meth(String, MyClass)
             * &lt;T extends IOException>meth(T, MyClass, java.lang.List&lt;T>)
             * </pre>
             * Notice that all type names are 'de-qualified', i.e. only their simple names appear.
             */
            private final String sourceMethodName = dequalify(Signature.toString(methodName, methodDescriptor, optionalMethodSignature));

            private LocalVariableFinder localVariableFinder;

            private CurrentLineProvider currentLineProvider;

            LoggifyingMethodVisitor() {
                super(methodAccess, methodName, methodDescriptor, optionalMethodSignature, methodExceptions);
            }

            @Override
            public AnnotationVisitor visitAnnotation(final String annotationDescriptor, final boolean visible) {
                AnnotationVisitor delegate = super.visitAnnotation(annotationDescriptor, visible);
                if (!annotationDescriptor.equals(LOGGIFY_ANNOTATION_DESCRIPTOR)) {
                    return delegate;
                }
                return new FilterAnnotationVisitor(delegate) {

                    @Override
                    public AnnotationVisitor visitArray(String name) {
                        return new FilterAnnotationVisitor(super.visitArray(name)) {

                            private final List<String> lines = new ArrayList<String>();

                            @Override
                            public void visit(String name, Object value) {
                                this.lines.add((String) value);
                                this.delegate.visit(name, value);
                            }

                            @Override
                            public void visitEnd() {
                                LoggifyingMethodVisitor.this.methodLogLevelCalculator = parseLoggifyAnnotation(this.lines.toArray(new String[this.lines.size()]), LoggifyingMethodVisitor.this.methodLogLevelCalculator, new ErrorHandler() {

                                    @Override
                                    public void handle(Throwable t) {
                                        LoggifyingClassAdapter.this.errorHandler.handle(ExceptionUtil.wrap(("Parsing the @Loggify annotation of method '" + methodName + "' of class '" + LoggifyingClassAdapter.this.sourceClassName + "'"), t));
                                    }

                                    @Override
                                    public void handle(String message) {
                                        LoggifyingClassAdapter.this.errorHandler.handle("Parsing the @Loggify annotation of method '" + methodName + "' of class '" + LoggifyingClassAdapter.this.sourceClassName + "': " + message);
                                    }
                                });
                                this.delegate.visitEnd();
                            }
                        };
                    }
                };
            }

            /**
             * @throw ExceptionTableEntryOffsetTooLargeException
             */
            @Override
            public void visitEnd() {
                try {
                    cook();
                    if ("<clinit>".equals(methodName)) {
                        LoggifyingClassAdapter.this.classInitializer = this;
                    } else {
                        @SuppressWarnings("synthetic-access") ClassVisitor cv = LoggifyingClassAdapter.this.cv;
                        accept(cv);
                        checkTryCatchBlocks(this);
                    }
                } catch (RuntimeException re) {
                    throw new RuntimeException(("Loggifying class '" + LoggifyingClassAdapter.this.sourceClassName + "', method '" + this.sourceMethodName + "': " + re.getMessage()), re);
                }
            }

            private void cook() {
                if (methodName.startsWith("access$")) return;
                final Map<LabelNode, Collection<LocalVariableNode>> lvstart, lvend;
                if (this.localVariables == null) {
                    lvstart = lvend = Collections.emptyMap();
                } else {
                    lvstart = new HashMap<LabelNode, Collection<LocalVariableNode>>();
                    lvend = new HashMap<LabelNode, Collection<LocalVariableNode>>();
                    @SuppressWarnings("unchecked") List<LocalVariableNode> lvs = this.localVariables;
                    for (LocalVariableNode lvn : lvs) {
                        {
                            Collection<LocalVariableNode> c = lvstart.get(lvn.start);
                            if (c == null) lvstart.put(lvn.start, c = new ArrayList<LocalVariableNode>());
                            c.add(lvn);
                        }
                        {
                            Collection<LocalVariableNode> c = lvend.get(lvn.end);
                            if (c == null) lvend.put(lvn.end, c = new ArrayList<LocalVariableNode>());
                            c.add(lvn);
                        }
                    }
                }
                class LocalVariableTrackingInsnIterator implements Iterator<AbstractInsnNode>, LocalVariableFinder, CurrentLineProvider {

                    @SuppressWarnings("unchecked")
                    final ListIterator<AbstractInsnNode> delegate = LoggifyingMethodVisitor.this.instructions.iterator();

                    final Map<Integer, LocalVariableNode> effectiveLocalVariables = new HashMap<Integer, LocalVariableNode>();

                    int currentLine;

                    @Override
                    public boolean hasNext() {
                        return this.delegate.hasNext();
                    }

                    @Override
                    public AbstractInsnNode next() {
                        AbstractInsnNode result = this.delegate.next();
                        switch(result.getType()) {
                            case AbstractInsnNode.LABEL:
                                LabelNode ln = (LabelNode) result;
                                {
                                    Collection<LocalVariableNode> c = lvstart.get(ln);
                                    if (c != null) {
                                        for (LocalVariableNode lvn : c) {
                                            LocalVariableNode prev = this.effectiveLocalVariables.put(Integer.valueOf(lvn.index), lvn);
                                            if (prev != null) {
                                                throw new IllegalStateException("More than one LV effctive with index " + lvn.index + ": '" + prev.name + "' and '" + lvn.name + "'");
                                            }
                                        }
                                    }
                                }
                                {
                                    Collection<LocalVariableNode> c = lvend.get(ln);
                                    if (c != null) {
                                        for (LocalVariableNode lvn : c) {
                                            if (this.effectiveLocalVariables.remove(Integer.valueOf(lvn.index)) != lvn) {
                                                throw new IllegalStateException("Inconsistent LV visibility for index " + lvn.index);
                                            }
                                        }
                                    }
                                }
                                break;
                            case AbstractInsnNode.LINE:
                                LineNumberNode lnn = (LineNumberNode) result;
                                this.currentLine = lnn.line;
                                break;
                            default:
                                ;
                        }
                        return result;
                    }

                    @Override
                    public void remove() {
                        this.delegate.remove();
                    }

                    @Override
                    public LocalVariableNode getLocalVariable(int index) {
                        return this.effectiveLocalVariables.get(Integer.valueOf(index));
                    }

                    @Override
                    public int getCurrentLine() {
                        return this.currentLine;
                    }
                }
                LocalVariableTrackingInsnIterator it = new LocalVariableTrackingInsnIterator();
                if (!(it.hasNext())) return;
                this.localVariableFinder = it;
                this.currentLineProvider = it;
                AbstractInsnNode insn = it.next();
                if ("<init>".equals(methodName)) {
                    int pendingNews = 0;
                    for (; ; ) {
                        int opcode = insn.getOpcode();
                        if (opcode == ATHROW) return;
                        insn = it.next();
                        if (opcode == INVOKESPECIAL) {
                            if (pendingNews == 0) break;
                            --pendingNews;
                        } else if (opcode == NEW) {
                            ++pendingNews;
                        }
                    }
                }
                while (insn.getOpcode() == -1) {
                    insn = it.next();
                }
                LOGGIFY_ENTRY: {
                    Level logLevel = getLogLevel(Action.ENTRY, null);
                    if (logLevel == Level.OFF) break LOGGIFY_ENTRY;
                    StringBuilder sb = new StringBuilder("ENTRY ");
                    Type[] parameterTypes = Type.getArgumentTypes(methodDescriptor);
                    Type[] localVariableTypes;
                    if ((methodAccess & ACC_STATIC) == 0) {
                        sb.append(" %");
                        localVariableTypes = new Type[parameterTypes.length + 1];
                        localVariableTypes[0] = Types.OBJECT_TYPE;
                        System.arraycopy(parameterTypes, 0, localVariableTypes, 1, parameterTypes.length);
                    } else {
                        localVariableTypes = parameterTypes;
                    }
                    sb.append(" args=(");
                    if (parameterTypes.length > 0) {
                        sb.append('%');
                        for (int i = parameterTypes.length - 1; i > 0; i--) {
                            sb.append(", %");
                        }
                    }
                    sb.append(")");
                    this.instructions.insertBefore(insn, generateLoggingWithLocalVariables(logLevel, getLoggerName(ENTRY_LOGGER_NAME_FORMAT), sb.toString(), 0, localVariableTypes, true));
                }
                EXAMINE_INSN: for (; ; ) {
                    int opcode = insn.getOpcode();
                    switch(opcode) {
                        case ACONST_NULL:
                        case ICONST_M1:
                        case ICONST_0:
                        case ICONST_1:
                        case ICONST_2:
                        case ICONST_3:
                        case ICONST_4:
                        case ICONST_5:
                        case LCONST_0:
                        case LCONST_1:
                        case FCONST_0:
                        case FCONST_1:
                        case FCONST_2:
                        case DCONST_0:
                        case DCONST_1:
                        case BIPUSH:
                        case SIPUSH:
                        case LDC:
                            this.instructions.insertBefore(insn, this.generateLogging(getLoggerName(CONST_LOGGER_NAME_FORMAT), getLogLevel(Action.CONST, null), (opcode == ACONST_NULL ? "CONST  null" : opcode <= ICONST_5 ? "CONST  " + Integer.toString(opcode - ICONST_0) : opcode <= LCONST_1 ? "CONST  " + (opcode - LCONST_0) + "L" : opcode <= FCONST_2 ? "CONST  " + (opcode - FCONST_0) + ".0F" : opcode <= DCONST_1 ? "CONST  " + (opcode - DCONST_0) + ".0D" : opcode <= SIPUSH ? "CONST  " + ((IntInsnNode) insn).operand : opcode == LDC ? "CONST  " + (((LdcInsnNode) insn).cst instanceof String ? '"' + (String) ((LdcInsnNode) insn).cst + '"' : ((LdcInsnNode) insn).cst.toString()) : "CONST  ???")));
                            insn = it.next();
                            break;
                        case ILOAD:
                        case LLOAD:
                        case FLOAD:
                        case DLOAD:
                        case ALOAD:
                            this.instructions.insertBefore(insn, generateLoggingWithLocalVariable(Action.LOAD, LOAD_LOGGER_NAME_FORMAT, "LOAD   @ ==> %", ((VarInsnNode) insn).var, Types.fromOpcode(opcode)));
                            insn = it.next();
                            break;
                        case IALOAD:
                        case LALOAD:
                        case FALOAD:
                        case DALOAD:
                        case AALOAD:
                        case BALOAD:
                        case CALOAD:
                        case SALOAD:
                            this.instructions.insertBefore(insn, generateLoggingWithOperands(getLogLevel(Action.ALOAD, null), getLoggerName(ALOAD_LOGGER_NAME_FORMAT), "ALOAD  %[%]", new Type[] { Types.fromOpcode(opcode), Type.INT_TYPE }));
                            insn = it.next();
                            break;
                        case ISTORE:
                        case LSTORE:
                        case FSTORE:
                        case DSTORE:
                        case ASTORE:
                            {
                                int localVariableIndex = ((VarInsnNode) insn).var;
                                insn = it.next();
                                this.instructions.insertBefore(insn, generateLoggingWithLocalVariable(Action.STORE, STORE_LOGGER_NAME_FORMAT, "STORE  @ <== %", localVariableIndex, Types.fromOpcode(opcode)));
                            }
                            break;
                        case IASTORE:
                        case LASTORE:
                        case FASTORE:
                        case DASTORE:
                        case AASTORE:
                        case BASTORE:
                        case CASTORE:
                        case SASTORE:
                            this.instructions.insertBefore(insn, generateLoggingWithOperands(getLogLevel(Action.ASTORE, null), getLoggerName(ASTORE_LOGGER_NAME_FORMAT), "ASTORE %[%] <== %", new Type[] { Types.fromOpcode(opcode), Type.INT_TYPE, Types.fromOpcode(opcode).getElementType() }));
                            insn = it.next();
                            break;
                        case IADD:
                        case LADD:
                        case FADD:
                        case DADD:
                        case ISUB:
                        case LSUB:
                        case FSUB:
                        case DSUB:
                        case IMUL:
                        case LMUL:
                        case FMUL:
                        case DMUL:
                        case IDIV:
                        case LDIV:
                        case FDIV:
                        case DDIV:
                        case IREM:
                        case LREM:
                        case FREM:
                        case DREM:
                        case IAND:
                        case LAND:
                        case IOR:
                        case LOR:
                        case IXOR:
                        case LXOR:
                        case LCMP:
                        case FCMPL:
                        case FCMPG:
                        case DCMPL:
                        case DCMPG:
                        case IF_ICMPEQ:
                        case IF_ICMPNE:
                        case IF_ICMPLT:
                        case IF_ICMPGE:
                        case IF_ICMPGT:
                        case IF_ICMPLE:
                            {
                                Type type = Types.fromOpcode(opcode);
                                this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.ARITH, null), getLoggerName(ARITH_LOGGER_NAME_FORMAT), (opcode <= DADD ? "ARITH  % + %" : opcode <= DSUB ? "ARITH  % - %" : opcode <= DMUL ? "ARITH  % * %" : opcode <= DDIV ? "ARITH  % / %" : opcode <= DREM ? "ARITH  % %% %" : opcode <= LAND ? "ARITH  % & %" : opcode <= LOR ? "ARITH  % | %" : opcode <= LXOR ? "ARITH  % ^ %" : opcode <= DCMPG ? "ARITH  % <=> %" : opcode == IF_ICMPEQ ? "ARITH  % == %" : opcode == IF_ICMPNE ? "ARITH  % != %" : opcode == IF_ICMPLT ? "ARITH  % < %" : opcode == IF_ICMPGE ? "ARITH  % >= %" : opcode == IF_ICMPGT ? "ARITH  % > %" : opcode == IF_ICMPLE ? "ARITH  % <= %" : "% ??? %"), new Type[] { type, type }));
                                insn = it.next();
                            }
                            break;
                        case INEG:
                        case LNEG:
                        case FNEG:
                        case DNEG:
                            this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.ARITH, null), getLoggerName(ARITH_LOGGER_NAME_FORMAT), "ARITH  -%", new Type[] { Types.fromOpcode(opcode) }));
                            insn = it.next();
                            break;
                        case IF_ACMPEQ:
                        case IF_ACMPNE:
                            this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.ARITH, null), getLoggerName(ARITH_LOGGER_NAME_FORMAT), opcode == IF_ACMPEQ ? "ARITH  % == null" : "ARITH  % != null", new Type[] { Types.OBJECT_TYPE }));
                            insn = it.next();
                            break;
                        case ISHL:
                        case LSHL:
                        case ISHR:
                        case LSHR:
                        case IUSHR:
                        case LUSHR:
                            this.instructions.insertBefore(insn, generateLoggingWithOperands(getLogLevel(Action.ARITH, null), getLoggerName(ARITH_LOGGER_NAME_FORMAT), (opcode <= LSHL ? "ARITH  % << %" : opcode <= LSHR ? "ARITH  % >> %" : opcode <= LUSHR ? "ARITH  % >>> %" : "xxx"), new Type[] { Types.fromOpcode(opcode), Type.INT_TYPE }));
                            insn = it.next();
                            break;
                        case IINC:
                            {
                                IincInsnNode iin = (IincInsnNode) insn;
                                this.instructions.insertBefore(insn, generateLoggingWithLocalVariable(Action.LOAD, LOAD_LOGGER_NAME_FORMAT, "LOAD   @ ==> %", iin.var, Types.fromOpcode(opcode)));
                                this.instructions.insertBefore(insn, generateLoggingWithLocalVariables(getLogLevel(Action.ARITH, null), getLoggerName(ARITH_LOGGER_NAME_FORMAT), "ARITH  % + " + iin.incr, iin.var, new Type[] { Type.INT_TYPE }, false));
                                insn = it.next();
                                this.instructions.insertBefore(insn, generateLoggingWithLocalVariable(Action.STORE, STORE_LOGGER_NAME_FORMAT, "STORE  @ <== %", iin.var, Type.INT_TYPE));
                            }
                            break;
                        case I2L:
                        case I2F:
                        case I2D:
                        case I2B:
                        case I2C:
                        case I2S:
                        case L2I:
                        case L2F:
                        case L2D:
                        case F2I:
                        case F2L:
                        case F2D:
                        case D2I:
                        case D2L:
                        case D2F:
                            this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.CONVERT, null), getLoggerName(CONVERT_LOGGER_NAME_FORMAT), "CONVRT % " + OpcodeUtil.getName(insn), new Type[] { Types.fromOpcode(opcode) }));
                            insn = it.next();
                            break;
                        case TABLESWITCH:
                        case LOOKUPSWITCH:
                            this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.SWITCH, null), getLoggerName(SWITCH_LOGGER_NAME_FORMAT), "SWITCH %", new Type[] { Type.INT_TYPE }));
                            insn = it.next();
                            break;
                        case IRETURN:
                        case LRETURN:
                        case FRETURN:
                        case DRETURN:
                        case ARETURN:
                        case RETURN:
                            this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.RETURN, null), getLoggerName(RETURN_LOGGER_NAME_FORMAT), "RETURN %", new Type[] { Type.getReturnType(methodDescriptor) }));
                            if (!(it.hasNext())) break EXAMINE_INSN;
                            insn = it.next();
                            break;
                        case GETSTATIC:
                            {
                                FieldInsnNode fin = (FieldInsnNode) insn;
                                String s = dequalify(Type.getObjectType(fin.owner).getClassName()) + "." + fin.name;
                                this.instructions.insertBefore(insn, this.generateLogging(getLoggerName(GET_LOGGER_NAME_FORMAT), getLogLevel(Action.GET, s), "GET    " + s + " ==> {0}", InsnUtil.oa(wrapPrimitive(InsnUtil.il(fin.clone(null)), fin.desc))));
                                insn = it.next();
                            }
                            break;
                        case GETFIELD:
                            {
                                FieldInsnNode fin = (FieldInsnNode) insn;
                                String s = dequalify(Type.getObjectType(fin.owner).getClassName()) + "." + fin.name;
                                this.instructions.insertBefore(insn, this.generateLogging(getLoggerName(GET_LOGGER_NAME_FORMAT), getLogLevel(Action.GET, s), InsnUtil.il(new VarInsnNode(ASTORE, this.maxLocals)), "GET    " + s + ": {0} ==> {1}", InsnUtil.oa(InsnUtil.il(new VarInsnNode(ALOAD, this.maxLocals)), wrapPrimitive(InsnUtil.il(new VarInsnNode(ALOAD, this.maxLocals), fin.clone(null)), fin.desc)), InsnUtil.il(new VarInsnNode(ALOAD, this.maxLocals))));
                                insn = it.next();
                            }
                            break;
                        case PUTSTATIC:
                            {
                                FieldInsnNode fin = (FieldInsnNode) insn;
                                insn = it.next();
                                String s = Type.getObjectType(fin.owner).getClassName() + "." + fin.name;
                                this.instructions.insertBefore(insn, this.generateLogging(getLoggerName(PUT_LOGGER_NAME_FORMAT), getLogLevel(Action.PUT, s), "PUT    " + s + " <== {0}", InsnUtil.oa(wrapPrimitive(InsnUtil.il(new FieldInsnNode(GETSTATIC, fin.owner, fin.name, fin.desc)), fin.desc))));
                            }
                            break;
                        case PUTFIELD:
                            {
                                FieldInsnNode fin = (FieldInsnNode) insn;
                                String s = Type.getObjectType(fin.owner).getClassName() + "." + fin.name;
                                this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.PUT, s), getLoggerName(PUT_LOGGER_NAME_FORMAT), "PUT    " + s + " % ==> %", new Type[] { Type.getObjectType(fin.owner), Type.getType(fin.desc) }));
                                insn = it.next();
                            }
                            break;
                        case INVOKEVIRTUAL:
                        case INVOKESPECIAL:
                        case INVOKESTATIC:
                        case INVOKEINTERFACE:
                            {
                                String calledMethodName = ((MethodInsnNode) insn).name;
                                String calledMethodDescriptor = ((MethodInsnNode) insn).desc;
                                String calledMethodOwner = ((MethodInsnNode) insn).owner;
                                LOGGIFY_INVOKE: {
                                    Level logLevel = getLogLevel(Action.INVOKE, null);
                                    if (logLevel == Level.OFF) break LOGGIFY_INVOKE;
                                    Type[] operandTypes;
                                    int idx = 0;
                                    StringBuilder format = (new StringBuilder("INVOKE ").append(dequalify(Signature.toString(calledMethodOwner, null))).append('.').append(dequalify(Signature.toString(calledMethodName, calledMethodDescriptor, null))).append(": "));
                                    Type[] parameterTypes = Type.getArgumentTypes(calledMethodDescriptor);
                                    if (opcode != INVOKESTATIC && !"<init>".equals(calledMethodName)) {
                                        operandTypes = new Type[1 + parameterTypes.length];
                                        operandTypes[idx++] = Type.getObjectType(calledMethodOwner);
                                        format.append("target=%, ");
                                    } else {
                                        operandTypes = new Type[parameterTypes.length];
                                    }
                                    format.append("args=(");
                                    for (int i = 0; i < parameterTypes.length; i++) {
                                        Type parameterType = parameterTypes[i];
                                        operandTypes[idx++] = parameterType;
                                        format.append(i == 0 ? "%" : ", %");
                                    }
                                    format.append(')');
                                    this.instructions.insertBefore(insn, this.generateLoggingWithOperands(logLevel, getLoggerName(INVOKE_LOGGER_NAME_FORMAT), format.toString(), operandTypes));
                                }
                                insn = it.next();
                                this.instructions.insertBefore(insn, this.generateLoggingWithOperands(getLogLevel(Action.RESULT, null), getLoggerName(RESULT_LOGGER_NAME_FORMAT), ("RESULT " + dequalify(Signature.toString(calledMethodOwner, null)) + '.' + dequalify(Signature.toString(calledMethodName, calledMethodDescriptor, null)) + " => %"), new Type[] { Type.getReturnType(calledMethodDescriptor) }));
                            }
                            break;
                        case NEW:
                            {
                                TypeInsnNode tin = (TypeInsnNode) insn;
                                insn = it.next();
                                String className = Type.getObjectType(tin.desc).getClassName();
                                this.instructions.insertBefore(insn, this.generateLogging(getLoggerName(NEW_LOGGER_NAME_FORMAT), getLogLevel(Action.NEW, className), "NEW    " + dequalify(className)));
                            }
                            break;
                        case ATHROW:
                            {
                                int tmpVar = this.maxLocals;
                                this.instructions.insertBefore(insn, LoggifyingClassAdapter.this.generateLoggingWithThrowable(getLoggerName(THROW_LOGGER_NAME_FORMAT), getLogLevel(Action.THROW, null), InsnUtil.il(new VarInsnNode(ASTORE, tmpVar)), this.sourceMethodName, this.currentLineProvider.getCurrentLine(), "THROW", InsnUtil.il(new VarInsnNode(ALOAD, tmpVar)), InsnUtil.il(new VarInsnNode(ALOAD, tmpVar)), this));
                                if (!it.hasNext()) break EXAMINE_INSN;
                                insn = it.next();
                            }
                            break;
                        default:
                            if (!it.hasNext()) break EXAMINE_INSN;
                            insn = it.next();
                            break;
                    }
                }
                @SuppressWarnings("unchecked") List<TryCatchBlockNode> tmp = this.tryCatchBlocks;
                for (TryCatchBlockNode tcbn : tmp) {
                    int tmpVar = this.maxLocals;
                    this.instructions.insert(tcbn.handler, LoggifyingClassAdapter.this.generateLoggingWithThrowable(getLoggerName(CATCH_LOGGER_NAME_FORMAT), getLogLevel(Action.CATCH, null), InsnUtil.il(new VarInsnNode(ASTORE, tmpVar)), this.sourceMethodName, this.currentLineProvider.getCurrentLine(), "CATCH  " + (tcbn.type == null ? "any" : tcbn.type.replace('/', '.')), InsnUtil.il(new VarInsnNode(ALOAD, tmpVar)), InsnUtil.il(new VarInsnNode(ALOAD, tmpVar)), this));
                }
            }

            private InsnList wrapPrimitive(InsnList il, String desc) {
                if ("Z".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;"));
                } else if ("B".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
                } else if ("C".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
                } else if ("D".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;"));
                } else if ("F".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;"));
                } else if ("I".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
                } else if ("J".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;"));
                } else if ("S".equals(desc)) {
                    il.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;"));
                }
                return il;
            }

            /**
             * @param format '@' is replaced with the variable name, '%' with the variable contents
             */
            private InsnList generateLoggingWithLocalVariable(Action action, MessageFormat loggerNameFormat, String format, int localVariableIndex, Type localVariableType) {
                LocalVariableNode lvn = this.localVariableFinder.getLocalVariable(localVariableIndex);
                String localVariableName = lvn != null ? lvn.name : "v" + localVariableIndex;
                Level logLevel = getLogLevel(action, localVariableName);
                if (logLevel == Level.OFF) return NO_INSNS;
                if (lvn != null) localVariableType = Type.getType(lvn.desc);
                {
                    int i = format.indexOf('@');
                    format = format.substring(0, i) + localVariableName + format.substring(i + 1);
                }
                return this.generateLoggingWithLocalVariables(logLevel, getLoggerName(loggerNameFormat, localVariableName), format, localVariableIndex, new Type[] { localVariableType }, false);
            }

            private Level getLogLevel(Action action, String custom) {
                return LoggifyingMethodVisitor.this.methodLogLevelCalculator.calculate(action, LoggifyingClassAdapter.this.thisClassAccess, LoggifyingClassAdapter.this.sourceClassName, methodAccess, this.sourceMethodName, this.currentLineProvider.getCurrentLine(), custom);
            }

            /**
             * Generates logging code that reports the top values on the operand stack.
             *
             * @param format Must contain exactly as many '%' characters as the size of {@code operandTypes}
             */
            private InsnList generateLoggingWithOperands(Level logLevel, String loggerName, String format, Type[] operandTypes) {
                if (logLevel == Level.OFF) return NO_INSNS;
                InsnList prefix = new InsnList();
                InsnList suffix = new InsnList();
                int idx = this.maxLocals;
                for (Type operandType : operandTypes) {
                    if (operandType != Type.VOID_TYPE) {
                        VarInsnNode insn = new VarInsnNode(operandType.getOpcode(ISTORE), idx);
                        if (prefix.size() == 0) {
                            prefix.add(insn);
                        } else {
                            prefix.insertBefore(prefix.getFirst(), insn);
                        }
                        suffix.add(new VarInsnNode(operandType.getOpcode(ILOAD), idx));
                        idx += operandType.getSize();
                    }
                }
                return this.generateLoggingWithLocalVariables(logLevel, loggerName, prefix, format, this.maxLocals, operandTypes, suffix, false);
            }

            /**
             * @see #generateLoggingWithLocalVariables(Level, String, InsnList, String, int, Type[], InsnList,
             *      boolean)
             */
            private InsnList generateLoggingWithLocalVariables(Level logLevel, String loggerName, String format, int firstLocalVariableIndex, Type[] localVariableTypes, boolean withNames) {
                return this.generateLoggingWithLocalVariables(logLevel, loggerName, null, format, firstLocalVariableIndex, localVariableTypes, null, withNames);
            }

            /**
             * Generates logging code that reports the values of local variables
             *
             * @param format Must contain exactly {@code localVariableTypes.length} '%' characters; '%%' is treated as
             *               a literal '%'
             */
            private InsnList generateLoggingWithLocalVariables(Level logLevel, String loggerName, InsnList optionalPrefix, String format, int firstLocalVariableIndex, Type[] localVariableTypes, InsnList optionalSuffix, boolean withNames) {
                if (logLevel == Level.OFF) return NO_INSNS;
                StringBuilder message = new StringBuilder();
                int idx = firstLocalVariableIndex;
                int pos = 0;
                int n = 0;
                List<InsnList> pushParameters = new ArrayList<InsnList>();
                for (Type localVariableType : localVariableTypes) {
                    int pos2 = format.indexOf('%', pos);
                    assert pos2 != -1;
                    while (pos2 + 1 < format.length() && format.charAt(pos2 + 1) == '%') {
                        message.append(format.substring(pos, pos2 + 1));
                        pos = pos2 + 2;
                        pos2 = format.indexOf('%', pos);
                    }
                    message.append(format.substring(pos, pos2));
                    if (localVariableType == Type.VOID_TYPE) {
                        message.append("void");
                    } else {
                        if (withNames) {
                            LocalVariableNode lv = this.localVariableFinder.getLocalVariable(idx);
                            if (lv != null && lv.name != null) {
                                message.append(lv.name).append('=');
                            }
                        }
                        message.append('{').append(n++).append('}');
                        pushParameters.add(wrapPrimitive(InsnUtil.il(new VarInsnNode(localVariableType.getOpcode(ILOAD), idx)), localVariableType.getDescriptor()));
                    }
                    pos = pos2 + 1;
                    idx += localVariableType.getSize();
                }
                assert format.indexOf('%', pos) == -1;
                message.append(format.substring(pos));
                return this.generateLogging(loggerName, logLevel, optionalPrefix, message.toString(), InsnUtil.oa(pushParameters.toArray(new InsnList[pushParameters.size()])), optionalSuffix);
            }

            private InsnList generateLogging(String loggerName, Level logLevel, String message) {
                return this.generateLogging(loggerName, logLevel, message, InsnUtil.il(new InsnNode(ACONST_NULL)));
            }

            private InsnList generateLogging(String loggerName, Level logLevel, String message, InsnList pushParameters) {
                return this.generateLogging(loggerName, logLevel, null, message, pushParameters, null);
            }

            private InsnList generateLogging(String loggerName, Level logLevel, InsnList optionalPrefix, String message, InsnList pushParameters, InsnList optionalSuffix) {
                return LoggifyingClassAdapter.this.generateLogging(loggerName, logLevel, optionalPrefix, this.sourceMethodName, this.currentLineProvider.getCurrentLine(), message, pushParameters, optionalSuffix, this);
            }

            private String getLoggerName(MessageFormat messageFormat) {
                return getLoggerName(messageFormat, null);
            }

            private String getLoggerName(MessageFormat messageFormat, String placeholder6) {
                String packagePrefix, unqualifiedTopLevelTypeName, nestedTypeSuffix;
                {
                    String[] sa = splitClassName(LoggifyingClassAdapter.this.thisClassName);
                    packagePrefix = sa[0];
                    unqualifiedTopLevelTypeName = sa[1];
                    nestedTypeSuffix = sa[2];
                }
                String returnTypeName;
                String parameterTypeNames;
                if (optionalMethodSignature != null) {
                    TraceSignatureVisitor tsv = new TraceSignatureVisitor(0);
                    new SignatureReader(optionalMethodSignature).accept(tsv);
                    returnTypeName = tsv.getReturnType().toString();
                    parameterTypeNames = tsv.getDeclaration();
                } else {
                    returnTypeName = Type.getReturnType(methodDescriptor).getClassName();
                    Type[] ats = Type.getArgumentTypes(methodDescriptor);
                    if (ats.length == 0) {
                        parameterTypeNames = "()";
                    } else {
                        StringBuilder sb = new StringBuilder("(").append(ats[0].getClassName());
                        for (int i = 1; i < ats.length; ++i) {
                            sb.append(", ").append(ats[i].getClassName());
                        }
                        parameterTypeNames = sb.append(')').toString();
                    }
                }
                returnTypeName = dequalify(returnTypeName);
                parameterTypeNames = dequalify(parameterTypeNames);
                return cookLoggerName(messageFormat.format(new Object[] { packagePrefix, unqualifiedTopLevelTypeName, nestedTypeSuffix, returnTypeName, methodName, parameterTypeNames, placeholder6 }));
            }
        }
        return new LoggifyingMethodVisitor();
    }

    static LevelCalculator parseLoggifyAnnotation(String[] lines, LevelCalculator parent, final ErrorHandler errorHandler) {
        final FirstMatchLevelCalculator result = new FirstMatchLevelCalculator(parent);
        for (final String line : lines) {
            Matcher m = LOGGIFY_ANNOTATION_PATTERN.matcher(line);
            if (!m.matches()) {
                errorHandler.handle("Rule \"" + line + "\" has invalid value format; must be " + "\"<action> [ ',' <action> ]... '=' <level> [ ':' <discriminator> ]\"");
                continue;
            }
            String[] actionNames = m.group(1).split("\\s*,\\s*");
            final EnumSet<Action> actions = EnumSet.noneOf(Action.class);
            for (String actionName : actionNames) {
                try {
                    if ("ALL".equals(actionName)) {
                        actions.addAll(EnumSet.allOf(Action.class));
                    } else {
                        actions.add(Action.valueOf(actionName));
                    }
                } catch (IllegalArgumentException iae) {
                    errorHandler.handle("Rule \"" + line + "\": Invalid action '" + actionName + "'; allowed values are '" + Arrays.toString(Action.values()) + "'");
                }
            }
            final Level level;
            String levelName = m.group(2);
            try {
                level = Level.parse(levelName);
            } catch (IllegalArgumentException iae) {
                errorHandler.handle("Rule \"" + line + "\": Invalid level '" + levelName + "'");
                continue;
            }
            final Expression<Boolean> discriminator;
            String discriminatorExpression = m.group(3);
            if (discriminatorExpression == null) {
                discriminator = TRUE_EXPRESSION;
            } else {
                try {
                    discriminator = new Parser(discriminatorExpression).parse();
                } catch (IOException e) {
                    errorHandler.handle(e);
                    continue;
                } catch (UnexpectedCharacterException uce) {
                    errorHandler.handle(uce);
                    continue;
                } catch (UnexpectedObjectException uoe) {
                    errorHandler.handle(uoe);
                    continue;
                }
            }
            result.add(new LevelCalculator() {

                @Override
                public Level calculate(final Action action, final int classAccess, final String className, final int methodAccess, final String methodName, final int lineNumber, final String custom) {
                    if (actions.contains(action) && discriminator.evaluate(new Transformer<String, String>() {

                        private final String optionalCustomName = (action == Action.LOAD || action == Action.STORE ? "name" : action == Action.GET || action == Action.PUT ? "field" : action == Action.NEW ? "type" : null);

                        @Override
                        public String transform(String in) {
                            if (in.equalsIgnoreCase("classaccess")) {
                                return classAccessToString(classAccess);
                            } else if (in.equalsIgnoreCase("class")) {
                                return className;
                            } else if (in.equalsIgnoreCase("methodaccess")) {
                                return methodAccessToString(methodAccess);
                            } else if (in.equalsIgnoreCase("method")) {
                                return methodName;
                            } else if (in.equalsIgnoreCase("line")) {
                                return Integer.toString(lineNumber);
                            } else if (in.equalsIgnoreCase(this.optionalCustomName)) {
                                return custom;
                            } else {
                                errorHandler.handle("Rule \"" + line + "\": Unknown variable '" + in + "' in discriminator expression; allowed variables are [class method line" + (this.optionalCustomName == null ? "" : ' ' + this.optionalCustomName) + "]");
                                return null;
                            }
                        }
                    })) return level;
                    return null;
                }
            });
        }
        return result;
    }

    static final Pattern LOGGIFY_ANNOTATION_PATTERN = Pattern.compile("([\\w, ]+)=(\\w+)(?::(.*))?");

    private static final Expression<Boolean> TRUE_EXPRESSION = new Expression<Boolean>() {

        @Override
        public Boolean evaluate(Transformer<String, String> variables) {
            return Boolean.TRUE;
        }
    };

    /**
     * Removes all qualifiers from all qualified names.
     * <p>
     * Example:
     * <p>
     * 'pkg.meth(java.lang.String)' => 'meth(String)'
     */
    static String dequalify(String s) {
        return DEQUALIFY.matcher(s).replaceAll("");
    }

    private static final Pattern DEQUALIFY = Pattern.compile("\\w+\\.");

    private String getLoggerName(MessageFormat messageFormat) {
        return cookLoggerName(messageFormat.format(splitClassName(this.thisClassName)));
    }

    static String[] splitClassName(String className) {
        int idx1 = 1 + className.lastIndexOf('/');
        String packagePrefix = className.substring(0, idx1).replace('/', '.');
        int idx2 = className.indexOf('$', idx1);
        String nestedTypeSuffix;
        String unqualifiedTopLevelTypeName;
        if (idx2 == -1) {
            unqualifiedTopLevelTypeName = className.substring(idx1);
            nestedTypeSuffix = "";
        } else {
            unqualifiedTopLevelTypeName = className.substring(idx1, idx2);
            nestedTypeSuffix = className.substring(idx2).replace('$', '.');
        }
        return new String[] { packagePrefix, unqualifiedTopLevelTypeName, nestedTypeSuffix };
    }

    /**
     * @see #generateLogging(Level, String, String, InsnList, InsnList, InsnList, InsnList, MethodVisitor)
     */
    InsnList generateLogging(String loggerName, Level logLevel, String sourceMethodName, int line, String message, InsnList pushParameters, MethodVisitor methodVisitor) {
        return this.generateLogging(loggerName, logLevel, null, sourceMethodName, line, message, pushParameters, null, methodVisitor);
    }

    /**
     * Generates code as follows:
     * <pre>
     *     [ <i>optional-prefix</i> ]
     *     Helper.logp(
     *         <i>this-class-name</i>.<i>logger-field-name</i>, // logger
     *         Level.<i>log-level-name</i>,                     // level
     *         <i>source-class-name</i>,                        // sourceClassName
     *         <i>source-method-name</i>,                       // sourceMethodName
     *         <i>source</i>,                                   // source
     *         <i>lineNumber</i>,                               // lineNumber
     *         <i>message</i>,                                  // message
     *         <i>push-parameters</i>                           // parameters
     *     );
     *     [ <i>optional-suffix</i> ]
     * </pre>
     */
    InsnList generateLogging(String loggerName, Level logLevel, InsnList optionalPrefix, String sourceMethodName, int lineNumber, String message, InsnList pushParameters, InsnList optionalSuffix, MethodVisitor methodVisitor) {
        if (logLevel == Level.OFF) return NO_INSNS;
        InsnList il = new InsnList();
        if (optionalPrefix != null) il.add(optionalPrefix);
        String loggerFieldName = loggerNameToFieldName(loggerName);
        il.add(new FieldInsnNode(GETSTATIC, this.thisClassName, loggerFieldName, "Ljava/util/logging/Logger;"));
        il.add(new FieldInsnNode(GETSTATIC, "java/util/logging/Level", logLevel.getName(), "Ljava/util/logging/Level;"));
        il.add(new LdcInsnNode(this.sourceClassName));
        il.add(new LdcInsnNode(sourceMethodName));
        il.add(new LdcInsnNode(this.source));
        il.add(new LdcInsnNode(lineNumber));
        il.add(new LdcInsnNode(message));
        il.add(pushParameters);
        il.add(new MethodInsnNode(INVOKESTATIC, HELPER_CLASS_NAME, "logp", ("(" + "Ljava/util/logging/Logger;" + "Ljava/util/logging/Level;" + "Ljava/lang/String;" + "Ljava/lang/String;" + "Ljava/lang/String;" + "I" + "Ljava/lang/String;" + "[Ljava/lang/Object;" + ")V")));
        if (optionalSuffix != null) il.add(optionalSuffix);
        return this.generateLogging(loggerName, logLevel, il);
    }

    /**
     * Generates code as follows:
     * <pre>
     *     [ <i>optional-prefix</i> ]
     *     Helper.logp(
     *         <i>this-class-name</i>.<i>logger-field-name</i>,
     *         Level.<i>log-level-name</i>,
     *         <i>source-class-name</i>,
     *         <i>source-method-name</i>,
     *         <i>source</i>,
     *         <i>line-number</i>,
     *         <i>message</i>,
     *         <i>push-throwable</i>
     *     );
     *     [ <i>optional-suffix</i> ]
     * </pre>
     */
    InsnList generateLoggingWithThrowable(String loggerName, Level logLevel, InsnList optionalPrefix, String sourceMethodName, int line, String message, InsnList pushThrowable, InsnList optionalSuffix, MethodVisitor methodVisitor) {
        if (logLevel == Level.OFF) return NO_INSNS;
        InsnList il = new InsnList();
        if (optionalPrefix != null) il.add(optionalPrefix);
        String loggerFieldName = loggerNameToFieldName(loggerName);
        il.add(new FieldInsnNode(GETSTATIC, this.thisClassName, loggerFieldName, "Ljava/util/logging/Logger;"));
        il.add(new FieldInsnNode(GETSTATIC, "java/util/logging/Level", logLevel.getName(), "Ljava/util/logging/Level;"));
        il.add(new LdcInsnNode(this.sourceClassName));
        il.add(new LdcInsnNode(sourceMethodName));
        il.add(new LdcInsnNode(this.source));
        il.add(new LdcInsnNode(line));
        il.add(new LdcInsnNode(message));
        il.add(pushThrowable);
        il.add(new MethodInsnNode(INVOKESTATIC, HELPER_CLASS_NAME, "logp", ("(" + "Ljava/util/logging/Logger;" + "Ljava/util/logging/Level;" + "Ljava/lang/String;" + "Ljava/lang/String;" + "Ljava/lang/String;" + "I" + "Ljava/lang/String;" + "Ljava/lang/Throwable;" + ")V")));
        if (optionalSuffix != null) il.add(optionalSuffix);
        return this.generateLogging(loggerName, logLevel, il);
    }

    /**
     * Generates code as follows:
     * <pre>
     * private static final ThreadLocal inLogging; // One JVM-global object.
     * 
     * // ...
     *
     * if (<i>this-class-name</i>.<i>logger-field-name</i>.isLoggable(Level.<i>log-level-name</i>)) {
     *     <i>insns</i>
     * }
     * </pre>
     */
    private InsnList generateLogging(String loggerName, Level logLevel, InsnList insns) {
        InsnList il = new InsnList();
        String loggerFieldName = loggerNameToFieldName(loggerName);
        LabelNode notLoggable = new LabelNode();
        il.add(new FieldInsnNode(GETSTATIC, this.thisClassName, loggerFieldName, "Ljava/util/logging/Logger;"));
        il.add(new FieldInsnNode(GETSTATIC, "java/util/logging/Level", logLevel.getName(), "Ljava/util/logging/Level;"));
        il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/logging/Logger", "isLoggable", "(Ljava/util/logging/Level;)Z"));
        il.add(new JumpInsnNode(IFEQ, notLoggable));
        il.add(insns);
        il.add(notLoggable);
        this.loggerNames.add(loggerName);
        return il;
    }

    /**
     * Some logging systems don't like particular special characters in logger names, so replace them.
     */
    static String cookLoggerName(String name) {
        return name.replace(' ', '_').replaceAll(",", "");
    }

    interface CurrentLineProvider {

        int getCurrentLine();
    }

    interface LocalVariableFinder {

        LocalVariableNode getLocalVariable(int index);
    }

    public static String classAccessToString(int access) {
        return accessToString(access, CLASS_ACCESSES);
    }

    public static String fieldAccessToString(int access) {
        return accessToString(access, FIELD_ACCESSES);
    }

    public static String methodAccessToString(int access) {
        return accessToString(access, METHOD_ACCESSES);
    }

    private static String accessToString(int access, Object[] table) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; ) {
            int a = (Integer) table[i++];
            String s = (String) table[i++];
            if ((access & a) != 0) sb.append(s);
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    private static final Object[] CLASS_ACCESSES = { Opcodes.ACC_PUBLIC, "public ", Opcodes.ACC_PRIVATE, "private ", Opcodes.ACC_PROTECTED, "protected ", Opcodes.ACC_FINAL, "final ", Opcodes.ACC_SUPER, "super ", Opcodes.ACC_INTERFACE, "interface ", Opcodes.ACC_ABSTRACT, "abstract ", Opcodes.ACC_SYNTHETIC, "synthetic ", Opcodes.ACC_ANNOTATION, "annotation ", Opcodes.ACC_ENUM, "enum ", Opcodes.ACC_DEPRECATED, "deprecated " };

    private static final Object[] FIELD_ACCESSES = { Opcodes.ACC_PUBLIC, "public ", Opcodes.ACC_PRIVATE, "private ", Opcodes.ACC_PROTECTED, "protected ", Opcodes.ACC_FINAL, "final ", Opcodes.ACC_STATIC, "static ", Opcodes.ACC_SUPER, "synchronized ", Opcodes.ACC_VOLATILE, "volatile ", Opcodes.ACC_TRANSIENT, "transient ", Opcodes.ACC_SYNTHETIC, "synthetic ", Opcodes.ACC_ENUM, "enum ", Opcodes.ACC_DEPRECATED, "deprecated " };

    private static final Object[] METHOD_ACCESSES = { Opcodes.ACC_PUBLIC, "public ", Opcodes.ACC_PRIVATE, "private ", Opcodes.ACC_PROTECTED, "protected ", Opcodes.ACC_FINAL, "final ", Opcodes.ACC_STATIC, "static ", Opcodes.ACC_SYNCHRONIZED, "synchronized ", Opcodes.ACC_BRIDGE, "bridge ", Opcodes.ACC_VARARGS, "varargs ", Opcodes.ACC_NATIVE, "native ", Opcodes.ACC_STRICT, "strict ", Opcodes.ACC_ABSTRACT, "abstract ", Opcodes.ACC_SYNTHETIC, "synthetic ", Opcodes.ACC_DEPRECATED, "deprecated " };

    public interface ErrorHandler {

        void handle(String message);

        void handle(Throwable t);

        ErrorHandler IGNORE = new ErrorHandler() {

            @Override
            public void handle(String message) {
            }

            @Override
            public void handle(Throwable t) {
            }
        };

        ErrorHandler STDERR = new ErrorHandler() {

            @Override
            public void handle(String message) {
                System.err.println(message);
            }

            @Override
            public void handle(Throwable t) {
                t.printStackTrace();
            }
        };
    }
}
