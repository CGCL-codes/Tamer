package org.nutz.repo.org.objectweb.asm;

/**
 * A {@link MethodVisitor} that generates methods in bytecode form. Each visit
 * method of this class appends the bytecode corresponding to the visited
 * instruction to a byte vector, in the order these methods are called.
 * 
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
class MethodWriter implements MethodVisitor {

    /**
     * Pseudo access flag used to denote constructors.
     */
    static final int ACC_CONSTRUCTOR = 262144;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is zero.
     */
    static final int SAME_FRAME = 0;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is 1
     */
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;

    /**
     * Reserved for future use
     */
    static final int RESERVED = 128;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is 1. Offset is bigger then 63;
     */
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;

    /**
     * Frame where current locals are the same as the locals in the previous
     * frame, except that the k last locals are absent. The value of k is given
     * by the formula 251-frame_type.
     */
    static final int CHOP_FRAME = 248;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is zero. Offset is bigger then 63;
     */
    static final int SAME_FRAME_EXTENDED = 251;

    /**
     * Frame where current locals are the same as the locals in the previous
     * frame, except that k additional locals are defined. The value of k is
     * given by the formula frame_type-251.
     */
    static final int APPEND_FRAME = 252;

    /**
     * Full frame
     */
    static final int FULL_FRAME = 255;

    /**
     * Indicates that the stack map frames must be recomputed from scratch. In
     * this case the maximum stack size and number of local variables is also
     * recomputed from scratch.
     * 
     * @see #compute
     */
    private static final int FRAMES = 0;

    /**
     * Indicates that the maximum stack size and number of local variables must
     * be automatically computed.
     * 
     * @see #compute
     */
    private static final int MAXS = 1;

    /**
     * Indicates that nothing must be automatically computed.
     * 
     * @see #compute
     */
    private static final int NOTHING = 2;

    /**
     * Next method writer (see {@link ClassWriter#firstMethod firstMethod}).
     */
    MethodWriter next;

    /**
     * The class writer to which this method must be added.
     */
    final ClassWriter cw;

    /**
     * Access flags of this method.
     */
    private int access;

    /**
     * The index of the constant pool item that contains the name of this
     * method.
     */
    private final int name;

    /**
     * The index of the constant pool item that contains the descriptor of this
     * method.
     */
    private final int desc;

    /**
     * The descriptor of this method.
     */
    private final String descriptor;

    /**
     * The signature of this method.
     */
    String signature;

    /**
     * If not zero, indicates that the code of this method must be copied from
     * the ClassReader associated to this writer in <code>cw.cr</code>. More
     * precisely, this field gives the index of the first byte to copied from
     * <code>cw.cr.b</code>.
     */
    int classReaderOffset;

    /**
     * If not zero, indicates that the code of this method must be copied from
     * the ClassReader associated to this writer in <code>cw.cr</code>. More
     * precisely, this field gives the number of bytes to copied from
     * <code>cw.cr.b</code>.
     */
    int classReaderLength;

    /**
     * Number of exceptions that can be thrown by this method.
     */
    int exceptionCount;

    /**
     * The exceptions that can be thrown by this method. More precisely, this
     * array contains the indexes of the constant pool items that contain the
     * internal names of these exception classes.
     */
    int[] exceptions;

    /**
     * The non standard attributes of the method.
     */
    private Attribute attrs;

    /**
     * The bytecode of this method.
     */
    private ByteVector code = new ByteVector();

    /**
     * Maximum stack size of this method.
     */
    private int maxStack;

    /**
     * Maximum number of local variables for this method.
     */
    private int maxLocals;

    /**
     * Number of stack map frames in the StackMapTable attribute.
     */
    private int frameCount;

    /**
     * The StackMapTable attribute.
     */
    private ByteVector stackMap;

    /**
     * The offset of the last frame that was written in the StackMapTable
     * attribute.
     */
    private int previousFrameOffset;

    /**
     * The last frame that was written in the StackMapTable attribute.
     * 
     * @see #frame
     */
    private int[] previousFrame;

    /**
     * Index of the next element to be added in {@link #frame}.
     */
    private int frameIndex;

    /**
     * The current stack map frame. The first element contains the offset of the
     * instruction to which the frame corresponds, the second element is the
     * number of locals and the third one is the number of stack elements. The
     * local variables start at index 3 and are followed by the operand stack
     * values. In summary frame[0] = offset, frame[1] = nLocal, frame[2] =
     * nStack, frame[3] = nLocal. All types are encoded as integers, with the
     * same format as the one used in {@link Label}, but limited to BASE types.
     */
    private int[] frame;

    /**
     * Number of elements in the exception handler list.
     */
    private int handlerCount;

    /**
     * The first element in the exception handler list.
     */
    private Handler firstHandler;

    /**
     * The last element in the exception handler list.
     */
    private Handler lastHandler;

    /**
     * Number of entries in the LocalVariableTable attribute.
     */
    private int localVarCount;

    /**
     * The LocalVariableTable attribute.
     */
    private ByteVector localVar;

    /**
     * Number of entries in the LocalVariableTypeTable attribute.
     */
    private int localVarTypeCount;

    /**
     * The LocalVariableTypeTable attribute.
     */
    private ByteVector localVarType;

    /**
     * Number of entries in the LineNumberTable attribute.
     */
    private int lineNumberCount;

    /**
     * The LineNumberTable attribute.
     */
    private ByteVector lineNumber;

    /**
     * The non standard attributes of the method's code.
     */
    private Attribute cattrs;

    /**
     * Indicates if some jump instructions are too small and need to be resized.
     */
    private boolean resize;

    /**
     * The number of subroutines in this method.
     */
    private int subroutines;

    /**
     * Indicates what must be automatically computed.
     * 
     * @see #FRAMES
     * @see #MAXS
     * @see #NOTHING
     */
    private final int compute;

    /**
     * A list of labels. This list is the list of basic blocks in the method,
     * i.e. a list of Label objects linked to each other by their
     * {@link Label#successor} field, in the order they are visited by
     * {@link MethodVisitor#visitLabel}, and starting with the first basic block.
     */
    private Label labels;

    /**
     * The previous basic block.
     */
    private Label previousBlock;

    /**
     * The current basic block.
     */
    private Label currentBlock;

    /**
     * The (relative) stack size after the last visited instruction. This size
     * is relative to the beginning of the current basic block, i.e., the true
     * stack size after the last visited instruction is equal to the
     * {@link Label#inputStackTop beginStackSize} of the current basic block
     * plus <tt>stackSize</tt>.
     */
    private int stackSize;

    /**
     * The (relative) maximum stack size after the last visited instruction.
     * This size is relative to the beginning of the current basic block, i.e.,
     * the true maximum stack size after the last visited instruction is equal
     * to the {@link Label#inputStackTop beginStackSize} of the current basic
     * block plus <tt>stackSize</tt>.
     */
    private int maxStackSize;

    /**
     * Constructs a new {@link MethodWriter}.
     * 
     * @param cw the class writer in which the method must be added.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name the method's name.
     * @param desc the method's descriptor (see {@link Type}).
     * @param signature the method's signature. May be <tt>null</tt>.
     * @param exceptions the internal names of the method's exceptions. May be
     *        <tt>null</tt>.
     * @param computeMaxs <tt>true</tt> if the maximum stack size and number
     *        of local variables must be automatically computed.
     * @param computeFrames <tt>true</tt> if the stack map tables must be
     *        recomputed from scratch.
     */
    MethodWriter(final ClassWriter cw, final int access, final String name, final String desc, final String signature, final String[] exceptions, final boolean computeMaxs, final boolean computeFrames) {
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        } else {
            cw.lastMethod.next = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);
        this.descriptor = desc;
        if (ClassReader.SIGNATURES) {
            this.signature = signature;
        }
        if (exceptions != null && exceptions.length > 0) {
            exceptionCount = exceptions.length;
            this.exceptions = new int[exceptionCount];
            for (int i = 0; i < exceptionCount; ++i) {
                this.exceptions[i] = cw.newClass(exceptions[i]);
            }
        }
        this.compute = computeFrames ? FRAMES : (computeMaxs ? MAXS : NOTHING);
        if (computeMaxs || computeFrames) {
            if (computeFrames && "<init>".equals(name)) {
                this.access |= ACC_CONSTRUCTOR;
            }
            int size = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
            if ((access & Opcodes.ACC_STATIC) != 0) {
                --size;
            }
            maxLocals = size;
            labels = new Label();
            labels.status |= Label.PUSHED;
            visitLabel(labels);
        }
    }

    public void visitAttribute(final Attribute attr) {
        if (attr.isCodeAttribute()) {
            attr.next = cattrs;
            cattrs = attr;
        } else {
            attr.next = attrs;
            attrs = attr;
        }
    }

    public void visitCode() {
    }

    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        if (!ClassReader.FRAMES || compute == FRAMES) {
            return;
        }
        if (type == Opcodes.F_NEW) {
            startFrame(code.length, nLocal, nStack);
            for (int i = 0; i < nLocal; ++i) {
                if (local[i] instanceof String) {
                    frame[frameIndex++] = Frame.OBJECT | cw.addType((String) local[i]);
                } else if (local[i] instanceof Integer) {
                    frame[frameIndex++] = ((Integer) local[i]).intValue();
                } else {
                    frame[frameIndex++] = Frame.UNINITIALIZED | cw.addUninitializedType("", ((Label) local[i]).position);
                }
            }
            for (int i = 0; i < nStack; ++i) {
                if (stack[i] instanceof String) {
                    frame[frameIndex++] = Frame.OBJECT | cw.addType((String) stack[i]);
                } else if (stack[i] instanceof Integer) {
                    frame[frameIndex++] = ((Integer) stack[i]).intValue();
                } else {
                    frame[frameIndex++] = Frame.UNINITIALIZED | cw.addUninitializedType("", ((Label) stack[i]).position);
                }
            }
            endFrame();
        } else {
            int delta;
            if (stackMap == null) {
                stackMap = new ByteVector();
                delta = code.length;
            } else {
                delta = code.length - previousFrameOffset - 1;
                if (delta < 0) {
                    if (type == Opcodes.F_SAME) {
                        return;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            switch(type) {
                case Opcodes.F_FULL:
                    stackMap.putByte(FULL_FRAME).putShort(delta).putShort(nLocal);
                    for (int i = 0; i < nLocal; ++i) {
                        writeFrameType(local[i]);
                    }
                    stackMap.putShort(nStack);
                    for (int i = 0; i < nStack; ++i) {
                        writeFrameType(stack[i]);
                    }
                    break;
                case Opcodes.F_APPEND:
                    stackMap.putByte(SAME_FRAME_EXTENDED + nLocal).putShort(delta);
                    for (int i = 0; i < nLocal; ++i) {
                        writeFrameType(local[i]);
                    }
                    break;
                case Opcodes.F_CHOP:
                    stackMap.putByte(SAME_FRAME_EXTENDED - nLocal).putShort(delta);
                    break;
                case Opcodes.F_SAME:
                    if (delta < 64) {
                        stackMap.putByte(delta);
                    } else {
                        stackMap.putByte(SAME_FRAME_EXTENDED).putShort(delta);
                    }
                    break;
                case Opcodes.F_SAME1:
                    if (delta < 64) {
                        stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME + delta);
                    } else {
                        stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED).putShort(delta);
                    }
                    writeFrameType(stack[0]);
                    break;
            }
            previousFrameOffset = code.length;
            ++frameCount;
        }
    }

    public void visitInsn(final int opcode) {
        code.putByte(opcode);
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, null, null);
            } else {
                int size = stackSize + Frame.SIZE[opcode];
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                noSuccessor();
            }
        }
    }

    public void visitIntInsn(final int opcode, final int operand) {
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, operand, null, null);
            } else if (opcode != Opcodes.NEWARRAY) {
                int size = stackSize + 1;
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        if (opcode == Opcodes.SIPUSH) {
            code.put12(opcode, operand);
        } else {
            code.put11(opcode, operand);
        }
    }

    public void visitVarInsn(final int opcode, final int var) {
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, var, null, null);
            } else {
                if (opcode == Opcodes.RET) {
                    currentBlock.status |= Label.RET;
                    currentBlock.inputStackTop = stackSize;
                    noSuccessor();
                } else {
                    int size = stackSize + Frame.SIZE[opcode];
                    if (size > maxStackSize) {
                        maxStackSize = size;
                    }
                    stackSize = size;
                }
            }
        }
        if (compute != NOTHING) {
            int n;
            if (opcode == Opcodes.LLOAD || opcode == Opcodes.DLOAD || opcode == Opcodes.LSTORE || opcode == Opcodes.DSTORE) {
                n = var + 2;
            } else {
                n = var + 1;
            }
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
        if (var < 4 && opcode != Opcodes.RET) {
            int opt;
            if (opcode < Opcodes.ISTORE) {
                opt = 26 + ((opcode - Opcodes.ILOAD) << 2) + var;
            } else {
                opt = 59 + ((opcode - Opcodes.ISTORE) << 2) + var;
            }
            code.putByte(opt);
        } else if (var >= 256) {
            code.putByte(196).put12(opcode, var);
        } else {
            code.put11(opcode, var);
        }
        if (opcode >= Opcodes.ISTORE && compute == FRAMES && handlerCount > 0) {
            visitLabel(new Label());
        }
    }

    public void visitTypeInsn(final int opcode, final String type) {
        Item i = cw.newClassItem(type);
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, code.length, cw, i);
            } else if (opcode == Opcodes.NEW) {
                int size = stackSize + 1;
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        code.put12(opcode, i.index);
    }

    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        Item i = cw.newFieldItem(owner, name, desc);
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, cw, i);
            } else {
                int size;
                char c = desc.charAt(0);
                switch(opcode) {
                    case Opcodes.GETSTATIC:
                        size = stackSize + (c == 'D' || c == 'J' ? 2 : 1);
                        break;
                    case Opcodes.PUTSTATIC:
                        size = stackSize + (c == 'D' || c == 'J' ? -2 : -1);
                        break;
                    case Opcodes.GETFIELD:
                        size = stackSize + (c == 'D' || c == 'J' ? 1 : 0);
                        break;
                    default:
                        size = stackSize + (c == 'D' || c == 'J' ? -3 : -2);
                        break;
                }
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        code.put12(opcode, i.index);
    }

    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        boolean itf = opcode == Opcodes.INVOKEINTERFACE;
        Item i = (opcode == Opcodes.INVOKEDYNAMIC) ? cw.newNameTypeItem(name, desc) : cw.newMethodItem(owner, name, desc, itf);
        int argSize = i.intVal;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, cw, i);
            } else {
                if (argSize == 0) {
                    argSize = Type.getArgumentsAndReturnSizes(desc);
                    i.intVal = argSize;
                }
                int size;
                if (opcode == Opcodes.INVOKESTATIC || opcode == Opcodes.INVOKEDYNAMIC) {
                    size = stackSize - (argSize >> 2) + (argSize & 0x03) + 1;
                } else {
                    size = stackSize - (argSize >> 2) + (argSize & 0x03);
                }
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        if (itf) {
            if (argSize == 0) {
                argSize = Type.getArgumentsAndReturnSizes(desc);
                i.intVal = argSize;
            }
            code.put12(Opcodes.INVOKEINTERFACE, i.index).put11(argSize >> 2, 0);
        } else {
            code.put12(opcode, i.index);
            if (opcode == Opcodes.INVOKEDYNAMIC) {
                code.putShort(0);
            }
        }
    }

    public void visitJumpInsn(final int opcode, final Label label) {
        Label nextInsn = null;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, null, null);
                label.getFirst().status |= Label.TARGET;
                addSuccessor(Edge.NORMAL, label);
                if (opcode != Opcodes.GOTO) {
                    nextInsn = new Label();
                }
            } else {
                if (opcode == Opcodes.JSR) {
                    if ((label.status & Label.SUBROUTINE) == 0) {
                        label.status |= Label.SUBROUTINE;
                        ++subroutines;
                    }
                    currentBlock.status |= Label.JSR;
                    addSuccessor(stackSize + 1, label);
                    nextInsn = new Label();
                } else {
                    stackSize += Frame.SIZE[opcode];
                    addSuccessor(stackSize, label);
                }
            }
        }
        if ((label.status & Label.RESOLVED) != 0 && label.position - code.length < Short.MIN_VALUE) {
            if (opcode == Opcodes.GOTO) {
                code.putByte(200);
            } else if (opcode == Opcodes.JSR) {
                code.putByte(201);
            } else {
                if (nextInsn != null) {
                    nextInsn.status |= Label.TARGET;
                }
                code.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1 : opcode ^ 1);
                code.putShort(8);
                code.putByte(200);
            }
            label.put(this, code, code.length - 1, true);
        } else {
            code.putByte(opcode);
            label.put(this, code, code.length - 1, false);
        }
        if (currentBlock != null) {
            if (nextInsn != null) {
                visitLabel(nextInsn);
            }
            if (opcode == Opcodes.GOTO) {
                noSuccessor();
            }
        }
    }

    public void visitLabel(final Label label) {
        resize |= label.resolve(this, code.length, code.data);
        if ((label.status & Label.DEBUG) != 0) {
            return;
        }
        if (compute == FRAMES) {
            if (currentBlock != null) {
                if (label.position == currentBlock.position) {
                    currentBlock.status |= (label.status & Label.TARGET);
                    label.frame = currentBlock.frame;
                    return;
                }
                addSuccessor(Edge.NORMAL, label);
            }
            currentBlock = label;
            if (label.frame == null) {
                label.frame = new Frame();
                label.frame.owner = label;
            }
            if (previousBlock != null) {
                if (label.position == previousBlock.position) {
                    previousBlock.status |= (label.status & Label.TARGET);
                    label.frame = previousBlock.frame;
                    currentBlock = previousBlock;
                    return;
                }
                previousBlock.successor = label;
            }
            previousBlock = label;
        } else if (compute == MAXS) {
            if (currentBlock != null) {
                currentBlock.outputStackMax = maxStackSize;
                addSuccessor(stackSize, label);
            }
            currentBlock = label;
            stackSize = 0;
            maxStackSize = 0;
            if (previousBlock != null) {
                previousBlock.successor = label;
            }
            previousBlock = label;
        }
    }

    public void visitLdcInsn(final Object cst) {
        Item i = cw.newConstItem(cst);
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(Opcodes.LDC, 0, cw, i);
            } else {
                int size;
                if (i.type == ClassWriter.LONG || i.type == ClassWriter.DOUBLE) {
                    size = stackSize + 2;
                } else {
                    size = stackSize + 1;
                }
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        int index = i.index;
        if (i.type == ClassWriter.LONG || i.type == ClassWriter.DOUBLE) {
            code.put12(20, index);
        } else if (index >= 256) {
            code.put12(19, index);
        } else {
            code.put11(Opcodes.LDC, index);
        }
    }

    public void visitIincInsn(final int var, final int increment) {
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(Opcodes.IINC, var, null, null);
            }
        }
        if (compute != NOTHING) {
            int n = var + 1;
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
        if ((var > 255) || (increment > 127) || (increment < -128)) {
            code.putByte(196).put12(Opcodes.IINC, var).putShort(increment);
        } else {
            code.putByte(Opcodes.IINC).put11(var, increment);
        }
    }

    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label[] labels) {
        int source = code.length;
        code.putByte(Opcodes.TABLESWITCH);
        code.putByteArray(null, 0, (4 - code.length % 4) % 4);
        dflt.put(this, code, source, true);
        code.putInt(min).putInt(max);
        for (int i = 0; i < labels.length; ++i) {
            labels[i].put(this, code, source, true);
        }
        visitSwitchInsn(dflt, labels);
    }

    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        int source = code.length;
        code.putByte(Opcodes.LOOKUPSWITCH);
        code.putByteArray(null, 0, (4 - code.length % 4) % 4);
        dflt.put(this, code, source, true);
        code.putInt(labels.length);
        for (int i = 0; i < labels.length; ++i) {
            code.putInt(keys[i]);
            labels[i].put(this, code, source, true);
        }
        visitSwitchInsn(dflt, labels);
    }

    private void visitSwitchInsn(final Label dflt, final Label[] labels) {
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(Opcodes.LOOKUPSWITCH, 0, null, null);
                addSuccessor(Edge.NORMAL, dflt);
                dflt.getFirst().status |= Label.TARGET;
                for (int i = 0; i < labels.length; ++i) {
                    addSuccessor(Edge.NORMAL, labels[i]);
                    labels[i].getFirst().status |= Label.TARGET;
                }
            } else {
                --stackSize;
                addSuccessor(stackSize, dflt);
                for (int i = 0; i < labels.length; ++i) {
                    addSuccessor(stackSize, labels[i]);
                }
            }
            noSuccessor();
        }
    }

    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        Item i = cw.newClassItem(desc);
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(Opcodes.MULTIANEWARRAY, dims, cw, i);
            } else {
                stackSize += 1 - dims;
            }
        }
        code.put12(Opcodes.MULTIANEWARRAY, i.index).putByte(dims);
    }

    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        ++handlerCount;
        Handler h = new Handler();
        h.start = start;
        h.end = end;
        h.handler = handler;
        h.desc = type;
        h.type = type != null ? cw.newClass(type) : 0;
        if (lastHandler == null) {
            firstHandler = h;
        } else {
            lastHandler.next = h;
        }
        lastHandler = h;
    }

    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        if (signature != null) {
            if (localVarType == null) {
                localVarType = new ByteVector();
            }
            ++localVarTypeCount;
            localVarType.putShort(start.position).putShort(end.position - start.position).putShort(cw.newUTF8(name)).putShort(cw.newUTF8(signature)).putShort(index);
        }
        if (localVar == null) {
            localVar = new ByteVector();
        }
        ++localVarCount;
        localVar.putShort(start.position).putShort(end.position - start.position).putShort(cw.newUTF8(name)).putShort(cw.newUTF8(desc)).putShort(index);
        if (compute != NOTHING) {
            char c = desc.charAt(0);
            int n = index + (c == 'J' || c == 'D' ? 2 : 1);
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
    }

    public void visitLineNumber(final int line, final Label start) {
        if (lineNumber == null) {
            lineNumber = new ByteVector();
        }
        ++lineNumberCount;
        lineNumber.putShort(start.position);
        lineNumber.putShort(line);
    }

    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (ClassReader.FRAMES && compute == FRAMES) {
            Handler handler = firstHandler;
            while (handler != null) {
                Label l = handler.start.getFirst();
                Label h = handler.handler.getFirst();
                Label e = handler.end.getFirst();
                String t = handler.desc == null ? "java/lang/Throwable" : handler.desc;
                int kind = Frame.OBJECT | cw.addType(t);
                h.status |= Label.TARGET;
                while (l != e) {
                    Edge b = new Edge();
                    b.info = kind;
                    b.successor = h;
                    b.next = l.successors;
                    l.successors = b;
                    l = l.successor;
                }
                handler = handler.next;
            }
            Frame f = labels.frame;
            Type[] args = Type.getArgumentTypes(descriptor);
            f.initInputFrame(cw, access, args, this.maxLocals);
            visitFrame(f);
            int max = 0;
            Label changed = labels;
            while (changed != null) {
                Label l = changed;
                changed = changed.next;
                l.next = null;
                f = l.frame;
                if ((l.status & Label.TARGET) != 0) {
                    l.status |= Label.STORE;
                }
                l.status |= Label.REACHABLE;
                int blockMax = f.inputStack.length + l.outputStackMax;
                if (blockMax > max) {
                    max = blockMax;
                }
                Edge e = l.successors;
                while (e != null) {
                    Label n = e.successor.getFirst();
                    boolean change = f.merge(cw, n.frame, e.info);
                    if (change && n.next == null) {
                        n.next = changed;
                        changed = n;
                    }
                    e = e.next;
                }
            }
            Label l = labels;
            while (l != null) {
                f = l.frame;
                if ((l.status & Label.STORE) != 0) {
                    visitFrame(f);
                }
                if ((l.status & Label.REACHABLE) == 0) {
                    Label k = l.successor;
                    int start = l.position;
                    int end = (k == null ? code.length : k.position) - 1;
                    if (end >= start) {
                        max = Math.max(max, 1);
                        for (int i = start; i < end; ++i) {
                            code.data[i] = Opcodes.NOP;
                        }
                        code.data[end] = (byte) Opcodes.ATHROW;
                        startFrame(start, 0, 1);
                        frame[frameIndex++] = Frame.OBJECT | cw.addType("java/lang/Throwable");
                        endFrame();
                    }
                }
                l = l.successor;
            }
            this.maxStack = max;
        } else if (compute == MAXS) {
            Handler handler = firstHandler;
            while (handler != null) {
                Label l = handler.start;
                Label h = handler.handler;
                Label e = handler.end;
                while (l != e) {
                    Edge b = new Edge();
                    b.info = Edge.EXCEPTION;
                    b.successor = h;
                    if ((l.status & Label.JSR) == 0) {
                        b.next = l.successors;
                        l.successors = b;
                    } else {
                        b.next = l.successors.next.next;
                        l.successors.next.next = b;
                    }
                    l = l.successor;
                }
                handler = handler.next;
            }
            if (subroutines > 0) {
                int id = 0;
                labels.visitSubroutine(null, 1, subroutines);
                Label l = labels;
                while (l != null) {
                    if ((l.status & Label.JSR) != 0) {
                        Label subroutine = l.successors.next.successor;
                        if ((subroutine.status & Label.VISITED) == 0) {
                            id += 1;
                            subroutine.visitSubroutine(null, (id / 32L) << 32 | (1L << (id % 32)), subroutines);
                        }
                    }
                    l = l.successor;
                }
                l = labels;
                while (l != null) {
                    if ((l.status & Label.JSR) != 0) {
                        Label L = labels;
                        while (L != null) {
                            L.status &= ~Label.VISITED2;
                            L = L.successor;
                        }
                        Label subroutine = l.successors.next.successor;
                        subroutine.visitSubroutine(l, 0, subroutines);
                    }
                    l = l.successor;
                }
            }
            int max = 0;
            Label stack = labels;
            while (stack != null) {
                Label l = stack;
                stack = stack.next;
                int start = l.inputStackTop;
                int blockMax = start + l.outputStackMax;
                if (blockMax > max) {
                    max = blockMax;
                }
                Edge b = l.successors;
                if ((l.status & Label.JSR) != 0) {
                    b = b.next;
                }
                while (b != null) {
                    l = b.successor;
                    if ((l.status & Label.PUSHED) == 0) {
                        l.inputStackTop = b.info == Edge.EXCEPTION ? 1 : start + b.info;
                        l.status |= Label.PUSHED;
                        l.next = stack;
                        stack = l;
                    }
                    b = b.next;
                }
            }
            this.maxStack = max;
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    public void visitEnd() {
    }

    /**
     * Adds a successor to the {@link #currentBlock currentBlock} block.
     * 
     * @param info information about the control flow edge to be added.
     * @param successor the successor block to be added to the current block.
     */
    private void addSuccessor(final int info, final Label successor) {
        Edge b = new Edge();
        b.info = info;
        b.successor = successor;
        b.next = currentBlock.successors;
        currentBlock.successors = b;
    }

    /**
     * Ends the current basic block. This method must be used in the case where
     * the current basic block does not have any successor.
     */
    private void noSuccessor() {
        if (compute == FRAMES) {
            Label l = new Label();
            l.frame = new Frame();
            l.frame.owner = l;
            l.resolve(this, code.length, code.data);
            previousBlock.successor = l;
            previousBlock = l;
        } else {
            currentBlock.outputStackMax = maxStackSize;
        }
        currentBlock = null;
    }

    /**
     * Visits a frame that has been computed from scratch.
     * 
     * @param f the frame that must be visited.
     */
    private void visitFrame(final Frame f) {
        int i, t;
        int nTop = 0;
        int nLocal = 0;
        int nStack = 0;
        int[] locals = f.inputLocals;
        int[] stacks = f.inputStack;
        for (i = 0; i < locals.length; ++i) {
            t = locals[i];
            if (t == Frame.TOP) {
                ++nTop;
            } else {
                nLocal += nTop + 1;
                nTop = 0;
            }
            if (t == Frame.LONG || t == Frame.DOUBLE) {
                ++i;
            }
        }
        for (i = 0; i < stacks.length; ++i) {
            t = stacks[i];
            ++nStack;
            if (t == Frame.LONG || t == Frame.DOUBLE) {
                ++i;
            }
        }
        startFrame(f.owner.position, nLocal, nStack);
        for (i = 0; nLocal > 0; ++i, --nLocal) {
            t = locals[i];
            frame[frameIndex++] = t;
            if (t == Frame.LONG || t == Frame.DOUBLE) {
                ++i;
            }
        }
        for (i = 0; i < stacks.length; ++i) {
            t = stacks[i];
            frame[frameIndex++] = t;
            if (t == Frame.LONG || t == Frame.DOUBLE) {
                ++i;
            }
        }
        endFrame();
    }

    /**
     * Starts the visit of a stack map frame.
     * 
     * @param offset the offset of the instruction to which the frame
     *        corresponds.
     * @param nLocal the number of local variables in the frame.
     * @param nStack the number of stack elements in the frame.
     */
    private void startFrame(final int offset, final int nLocal, final int nStack) {
        int n = 3 + nLocal + nStack;
        if (frame == null || frame.length < n) {
            frame = new int[n];
        }
        frame[0] = offset;
        frame[1] = nLocal;
        frame[2] = nStack;
        frameIndex = 3;
    }

    /**
     * Checks if the visit of the current frame {@link #frame} is finished, and
     * if yes, write it in the StackMapTable attribute.
     */
    private void endFrame() {
        if (previousFrame != null) {
            if (stackMap == null) {
                stackMap = new ByteVector();
            }
            writeFrame();
            ++frameCount;
        }
        previousFrame = frame;
        frame = null;
    }

    /**
     * Compress and writes the current frame {@link #frame} in the StackMapTable
     * attribute.
     */
    private void writeFrame() {
        int clocalsSize = frame[1];
        int cstackSize = frame[2];
        if ((cw.version & 0xFFFF) < Opcodes.V1_6) {
            stackMap.putShort(frame[0]).putShort(clocalsSize);
            writeFrameTypes(3, 3 + clocalsSize);
            stackMap.putShort(cstackSize);
            writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
            return;
        }
        int localsSize = previousFrame[1];
        int type = FULL_FRAME;
        int k = 0;
        int delta;
        if (frameCount == 0) {
            delta = frame[0];
        } else {
            delta = frame[0] - previousFrame[0] - 1;
        }
        if (cstackSize == 0) {
            k = clocalsSize - localsSize;
            switch(k) {
                case -3:
                case -2:
                case -1:
                    type = CHOP_FRAME;
                    localsSize = clocalsSize;
                    break;
                case 0:
                    type = delta < 64 ? SAME_FRAME : SAME_FRAME_EXTENDED;
                    break;
                case 1:
                case 2:
                case 3:
                    type = APPEND_FRAME;
                    break;
            }
        } else if (clocalsSize == localsSize && cstackSize == 1) {
            type = delta < 63 ? SAME_LOCALS_1_STACK_ITEM_FRAME : SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED;
        }
        if (type != FULL_FRAME) {
            int l = 3;
            for (int j = 0; j < localsSize; j++) {
                if (frame[l] != previousFrame[l]) {
                    type = FULL_FRAME;
                    break;
                }
                l++;
            }
        }
        switch(type) {
            case SAME_FRAME:
                stackMap.putByte(delta);
                break;
            case SAME_LOCALS_1_STACK_ITEM_FRAME:
                stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME + delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED:
                stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED).putShort(delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case SAME_FRAME_EXTENDED:
                stackMap.putByte(SAME_FRAME_EXTENDED).putShort(delta);
                break;
            case CHOP_FRAME:
                stackMap.putByte(SAME_FRAME_EXTENDED + k).putShort(delta);
                break;
            case APPEND_FRAME:
                stackMap.putByte(SAME_FRAME_EXTENDED + k).putShort(delta);
                writeFrameTypes(3 + localsSize, 3 + clocalsSize);
                break;
            default:
                stackMap.putByte(FULL_FRAME).putShort(delta).putShort(clocalsSize);
                writeFrameTypes(3, 3 + clocalsSize);
                stackMap.putShort(cstackSize);
                writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
        }
    }

    /**
     * Writes some types of the current frame {@link #frame} into the
     * StackMapTableAttribute. This method converts types from the format used
     * in {@link Label} to the format used in StackMapTable attributes. In
     * particular, it converts type table indexes to constant pool indexes.
     * 
     * @param start index of the first type in {@link #frame} to write.
     * @param end index of last type in {@link #frame} to write (exclusive).
     */
    private void writeFrameTypes(final int start, final int end) {
        for (int i = start; i < end; ++i) {
            int t = frame[i];
            int d = t & Frame.DIM;
            if (d == 0) {
                int v = t & Frame.BASE_VALUE;
                switch(t & Frame.BASE_KIND) {
                    case Frame.OBJECT:
                        stackMap.putByte(7).putShort(cw.newClass(cw.typeTable[v].strVal1));
                        break;
                    case Frame.UNINITIALIZED:
                        stackMap.putByte(8).putShort(cw.typeTable[v].intVal);
                        break;
                    default:
                        stackMap.putByte(v);
                }
            } else {
                StringBuffer buf = new StringBuffer();
                d >>= 28;
                while (d-- > 0) {
                    buf.append('[');
                }
                if ((t & Frame.BASE_KIND) == Frame.OBJECT) {
                    buf.append('L');
                    buf.append(cw.typeTable[t & Frame.BASE_VALUE].strVal1);
                    buf.append(';');
                } else {
                    switch(t & 0xF) {
                        case 1:
                            buf.append('I');
                            break;
                        case 2:
                            buf.append('F');
                            break;
                        case 3:
                            buf.append('D');
                            break;
                        case 9:
                            buf.append('Z');
                            break;
                        case 10:
                            buf.append('B');
                            break;
                        case 11:
                            buf.append('C');
                            break;
                        case 12:
                            buf.append('S');
                            break;
                        default:
                            buf.append('J');
                    }
                }
                stackMap.putByte(7).putShort(cw.newClass(buf.toString()));
            }
        }
    }

    private void writeFrameType(final Object type) {
        if (type instanceof String) {
            stackMap.putByte(7).putShort(cw.newClass((String) type));
        } else if (type instanceof Integer) {
            stackMap.putByte(((Integer) type).intValue());
        } else {
            stackMap.putByte(8).putShort(((Label) type).position);
        }
    }

    /**
     * Returns the size of the bytecode of this method.
     * 
     * @return the size of the bytecode of this method.
     */
    final int getSize() {
        if (classReaderOffset != 0) {
            return 6 + classReaderLength;
        }
        if (resize) {
            if (ClassReader.RESIZE) {
                resizeInstructions();
            } else {
                throw new RuntimeException("Method code too large!");
            }
        }
        int size = 8;
        if (code.length > 0) {
            cw.newUTF8("Code");
            size += 18 + code.length + 8 * handlerCount;
            if (localVar != null) {
                cw.newUTF8("LocalVariableTable");
                size += 8 + localVar.length;
            }
            if (localVarType != null) {
                cw.newUTF8("LocalVariableTypeTable");
                size += 8 + localVarType.length;
            }
            if (lineNumber != null) {
                cw.newUTF8("LineNumberTable");
                size += 8 + lineNumber.length;
            }
            if (stackMap != null) {
                boolean zip = (cw.version & 0xFFFF) >= Opcodes.V1_6;
                cw.newUTF8(zip ? "StackMapTable" : "StackMap");
                size += 8 + stackMap.length;
            }
            if (cattrs != null) {
                size += cattrs.getSize(cw, code.data, code.length, maxStack, maxLocals);
            }
        }
        if (exceptionCount > 0) {
            cw.newUTF8("Exceptions");
            size += 8 + 2 * exceptionCount;
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0 && ((cw.version & 0xFFFF) < Opcodes.V1_5 || (access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0)) {
            cw.newUTF8("Synthetic");
            size += 6;
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            cw.newUTF8("Deprecated");
            size += 6;
        }
        if (ClassReader.SIGNATURES && signature != null) {
            cw.newUTF8("Signature");
            cw.newUTF8(signature);
            size += 8;
        }
        if (attrs != null) {
            size += attrs.getSize(cw, null, 0, -1, -1);
        }
        return size;
    }

    /**
     * Puts the bytecode of this method in the given byte vector.
     * 
     * @param out the byte vector into which the bytecode of this method must be
     *        copied.
     */
    final void put(final ByteVector out) {
        int mask = Opcodes.ACC_DEPRECATED | ClassWriter.ACC_SYNTHETIC_ATTRIBUTE | ((access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / (ClassWriter.ACC_SYNTHETIC_ATTRIBUTE / Opcodes.ACC_SYNTHETIC));
        out.putShort(access & ~mask).putShort(name).putShort(desc);
        int attributeCount = 0;
        if (code.length > 0) {
            ++attributeCount;
        }
        if (exceptionCount > 0) {
            ++attributeCount;
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0 && ((cw.version & 0xFFFF) < Opcodes.V1_5 || (access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0)) {
            ++attributeCount;
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            ++attributeCount;
        }
        if (ClassReader.SIGNATURES && signature != null) {
            ++attributeCount;
        }
        if (attrs != null) {
            attributeCount += attrs.getCount();
        }
        out.putShort(attributeCount);
        if (code.length > 0) {
            int size = 12 + code.length + 8 * handlerCount;
            if (localVar != null) {
                size += 8 + localVar.length;
            }
            if (localVarType != null) {
                size += 8 + localVarType.length;
            }
            if (lineNumber != null) {
                size += 8 + lineNumber.length;
            }
            if (stackMap != null) {
                size += 8 + stackMap.length;
            }
            if (cattrs != null) {
                size += cattrs.getSize(cw, code.data, code.length, maxStack, maxLocals);
            }
            out.putShort(cw.newUTF8("Code")).putInt(size);
            out.putShort(maxStack).putShort(maxLocals);
            out.putInt(code.length).putByteArray(code.data, 0, code.length);
            out.putShort(handlerCount);
            if (handlerCount > 0) {
                Handler h = firstHandler;
                while (h != null) {
                    out.putShort(h.start.position).putShort(h.end.position).putShort(h.handler.position).putShort(h.type);
                    h = h.next;
                }
            }
            attributeCount = 0;
            if (localVar != null) {
                ++attributeCount;
            }
            if (localVarType != null) {
                ++attributeCount;
            }
            if (lineNumber != null) {
                ++attributeCount;
            }
            if (stackMap != null) {
                ++attributeCount;
            }
            if (cattrs != null) {
                attributeCount += cattrs.getCount();
            }
            out.putShort(attributeCount);
            if (localVar != null) {
                out.putShort(cw.newUTF8("LocalVariableTable"));
                out.putInt(localVar.length + 2).putShort(localVarCount);
                out.putByteArray(localVar.data, 0, localVar.length);
            }
            if (localVarType != null) {
                out.putShort(cw.newUTF8("LocalVariableTypeTable"));
                out.putInt(localVarType.length + 2).putShort(localVarTypeCount);
                out.putByteArray(localVarType.data, 0, localVarType.length);
            }
            if (lineNumber != null) {
                out.putShort(cw.newUTF8("LineNumberTable"));
                out.putInt(lineNumber.length + 2).putShort(lineNumberCount);
                out.putByteArray(lineNumber.data, 0, lineNumber.length);
            }
            if (stackMap != null) {
                boolean zip = (cw.version & 0xFFFF) >= Opcodes.V1_6;
                out.putShort(cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
                out.putInt(stackMap.length + 2).putShort(frameCount);
                out.putByteArray(stackMap.data, 0, stackMap.length);
            }
            if (cattrs != null) {
                cattrs.put(cw, code.data, code.length, maxLocals, maxStack, out);
            }
        }
        if (exceptionCount > 0) {
            out.putShort(cw.newUTF8("Exceptions")).putInt(2 * exceptionCount + 2);
            out.putShort(exceptionCount);
            for (int i = 0; i < exceptionCount; ++i) {
                out.putShort(exceptions[i]);
            }
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0 && ((cw.version & 0xFFFF) < Opcodes.V1_5 || (access & ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0)) {
            out.putShort(cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            out.putShort(cw.newUTF8("Deprecated")).putInt(0);
        }
        if (ClassReader.SIGNATURES && signature != null) {
            out.putShort(cw.newUTF8("Signature")).putInt(2).putShort(cw.newUTF8(signature));
        }
        if (attrs != null) {
            attrs.put(cw, null, 0, -1, -1, out);
        }
    }

    /**
     * Resizes and replaces the temporary instructions inserted by
     * {@link Label#resolve} for wide forward jumps, while keeping jump offsets
     * and instruction addresses consistent. This may require to resize other
     * existing instructions, or even to introduce new instructions: for
     * example, increasing the size of an instruction by 2 at the middle of a
     * method can increases the offset of an IFEQ instruction from 32766 to
     * 32768, in which case IFEQ 32766 must be replaced with IFNEQ 8 GOTO_W
     * 32765. This, in turn, may require to increase the size of another jump
     * instruction, and so on... All these operations are handled automatically
     * by this method. <p> <i>This method must be called after all the method
     * that is being built has been visited</i>. In particular, the
     * {@link Label Label} objects used to construct the method are no longer
     * valid after this method has been called.
     */
    private void resizeInstructions() {
        byte[] b = code.data;
        int u, v, label;
        int i, j;
        int[] allIndexes = new int[0];
        int[] allSizes = new int[0];
        boolean[] resize;
        int newOffset;
        resize = new boolean[code.length];
        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            u = 0;
            while (u < b.length) {
                int opcode = b[u] & 0xFF;
                int insert = 0;
                switch(ClassWriter.TYPE[opcode]) {
                    case ClassWriter.NOARG_INSN:
                    case ClassWriter.IMPLVAR_INSN:
                        u += 1;
                        break;
                    case ClassWriter.LABEL_INSN:
                        if (opcode > 201) {
                            opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                            label = u + readUnsignedShort(b, u + 1);
                        } else {
                            label = u + readShort(b, u + 1);
                        }
                        newOffset = getNewOffset(allIndexes, allSizes, u, label);
                        if (newOffset < Short.MIN_VALUE || newOffset > Short.MAX_VALUE) {
                            if (!resize[u]) {
                                if (opcode == Opcodes.GOTO || opcode == Opcodes.JSR) {
                                    insert = 2;
                                } else {
                                    insert = 5;
                                }
                                resize[u] = true;
                            }
                        }
                        u += 3;
                        break;
                    case ClassWriter.LABELW_INSN:
                        u += 5;
                        break;
                    case ClassWriter.TABL_INSN:
                        if (state == 1) {
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            insert = u & 3;
                            resize[u] = true;
                        }
                        u = u + 4 - (u & 3);
                        u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
                        break;
                    case ClassWriter.LOOK_INSN:
                        if (state == 1) {
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            insert = u & 3;
                            resize[u] = true;
                        }
                        u = u + 4 - (u & 3);
                        u += 8 * readInt(b, u + 4) + 8;
                        break;
                    case ClassWriter.WIDE_INSN:
                        opcode = b[u + 1] & 0xFF;
                        if (opcode == Opcodes.IINC) {
                            u += 6;
                        } else {
                            u += 4;
                        }
                        break;
                    case ClassWriter.VAR_INSN:
                    case ClassWriter.SBYTE_INSN:
                    case ClassWriter.LDC_INSN:
                        u += 2;
                        break;
                    case ClassWriter.SHORT_INSN:
                    case ClassWriter.LDCW_INSN:
                    case ClassWriter.FIELDORMETH_INSN:
                    case ClassWriter.TYPE_INSN:
                    case ClassWriter.IINC_INSN:
                        u += 3;
                        break;
                    case ClassWriter.ITFDYNMETH_INSN:
                        u += 5;
                        break;
                    default:
                        u += 4;
                        break;
                }
                if (insert != 0) {
                    int[] newIndexes = new int[allIndexes.length + 1];
                    int[] newSizes = new int[allSizes.length + 1];
                    System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
                    System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                    newIndexes[allIndexes.length] = u;
                    newSizes[allSizes.length] = insert;
                    allIndexes = newIndexes;
                    allSizes = newSizes;
                    if (insert > 0) {
                        state = 3;
                    }
                }
            }
            if (state < 3) {
                --state;
            }
        } while (state != 0);
        ByteVector newCode = new ByteVector(code.length);
        u = 0;
        while (u < code.length) {
            int opcode = b[u] & 0xFF;
            switch(ClassWriter.TYPE[opcode]) {
                case ClassWriter.NOARG_INSN:
                case ClassWriter.IMPLVAR_INSN:
                    newCode.putByte(opcode);
                    u += 1;
                    break;
                case ClassWriter.LABEL_INSN:
                    if (opcode > 201) {
                        opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                        label = u + readUnsignedShort(b, u + 1);
                    } else {
                        label = u + readShort(b, u + 1);
                    }
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    if (resize[u]) {
                        if (opcode == Opcodes.GOTO) {
                            newCode.putByte(200);
                        } else if (opcode == Opcodes.JSR) {
                            newCode.putByte(201);
                        } else {
                            newCode.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1 : opcode ^ 1);
                            newCode.putShort(8);
                            newCode.putByte(200);
                            newOffset -= 3;
                        }
                        newCode.putInt(newOffset);
                    } else {
                        newCode.putByte(opcode);
                        newCode.putShort(newOffset);
                    }
                    u += 3;
                    break;
                case ClassWriter.LABELW_INSN:
                    label = u + readInt(b, u + 1);
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    newCode.putByte(opcode);
                    newCode.putInt(newOffset);
                    u += 5;
                    break;
                case ClassWriter.TABL_INSN:
                    v = u;
                    u = u + 4 - (v & 3);
                    newCode.putByte(Opcodes.TABLESWITCH);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    j = readInt(b, u) - j + 1;
                    u += 4;
                    newCode.putInt(readInt(b, u - 4));
                    for (; j > 0; --j) {
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case ClassWriter.LOOK_INSN:
                    v = u;
                    u = u + 4 - (v & 3);
                    newCode.putByte(Opcodes.LOOKUPSWITCH);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    for (; j > 0; --j) {
                        newCode.putInt(readInt(b, u));
                        u += 4;
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case ClassWriter.WIDE_INSN:
                    opcode = b[u + 1] & 0xFF;
                    if (opcode == Opcodes.IINC) {
                        newCode.putByteArray(b, u, 6);
                        u += 6;
                    } else {
                        newCode.putByteArray(b, u, 4);
                        u += 4;
                    }
                    break;
                case ClassWriter.VAR_INSN:
                case ClassWriter.SBYTE_INSN:
                case ClassWriter.LDC_INSN:
                    newCode.putByteArray(b, u, 2);
                    u += 2;
                    break;
                case ClassWriter.SHORT_INSN:
                case ClassWriter.LDCW_INSN:
                case ClassWriter.FIELDORMETH_INSN:
                case ClassWriter.TYPE_INSN:
                case ClassWriter.IINC_INSN:
                    newCode.putByteArray(b, u, 3);
                    u += 3;
                    break;
                case ClassWriter.ITFDYNMETH_INSN:
                    newCode.putByteArray(b, u, 5);
                    u += 5;
                    break;
                default:
                    newCode.putByteArray(b, u, 4);
                    u += 4;
                    break;
            }
        }
        if (frameCount > 0) {
            if (compute == FRAMES) {
                frameCount = 0;
                stackMap = null;
                previousFrame = null;
                frame = null;
                Frame f = new Frame();
                f.owner = labels;
                Type[] args = Type.getArgumentTypes(descriptor);
                f.initInputFrame(cw, access, args, maxLocals);
                visitFrame(f);
                Label l = labels;
                while (l != null) {
                    u = l.position - 3;
                    if ((l.status & Label.STORE) != 0 || (u >= 0 && resize[u])) {
                        getNewOffset(allIndexes, allSizes, l);
                        visitFrame(l.frame);
                    }
                    l = l.successor;
                }
            } else {
                cw.invalidFrames = true;
            }
        }
        Handler h = firstHandler;
        while (h != null) {
            getNewOffset(allIndexes, allSizes, h.start);
            getNewOffset(allIndexes, allSizes, h.end);
            getNewOffset(allIndexes, allSizes, h.handler);
            h = h.next;
        }
        for (i = 0; i < 2; ++i) {
            ByteVector bv = i == 0 ? localVar : localVarType;
            if (bv != null) {
                b = bv.data;
                u = 0;
                while (u < bv.length) {
                    label = readUnsignedShort(b, u);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label);
                    writeShort(b, u, newOffset);
                    label += readUnsignedShort(b, u + 2);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label) - newOffset;
                    writeShort(b, u + 2, newOffset);
                    u += 10;
                }
            }
        }
        if (lineNumber != null) {
            b = lineNumber.data;
            u = 0;
            while (u < lineNumber.length) {
                writeShort(b, u, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, u)));
                u += 4;
            }
        }
        Attribute attr = cattrs;
        while (attr != null) {
            Label[] labels = attr.getLabels();
            if (labels != null) {
                for (i = labels.length - 1; i >= 0; --i) {
                    getNewOffset(allIndexes, allSizes, labels[i]);
                }
            }
            attr = attr.next;
        }
        code = newCode;
    }

    /**
     * Reads an unsigned short value in the given byte array.
     * 
     * @param b a byte array.
     * @param index the start index of the value to be read.
     * @return the read value.
     */
    static int readUnsignedShort(final byte[] b, final int index) {
        return ((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF);
    }

    /**
     * Reads a signed short value in the given byte array.
     * 
     * @param b a byte array.
     * @param index the start index of the value to be read.
     * @return the read value.
     */
    static short readShort(final byte[] b, final int index) {
        return (short) (((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF));
    }

    /**
     * Reads a signed int value in the given byte array.
     * 
     * @param b a byte array.
     * @param index the start index of the value to be read.
     * @return the read value.
     */
    static int readInt(final byte[] b, final int index) {
        return ((b[index] & 0xFF) << 24) | ((b[index + 1] & 0xFF) << 16) | ((b[index + 2] & 0xFF) << 8) | (b[index + 3] & 0xFF);
    }

    /**
     * Writes a short value in the given byte array.
     * 
     * @param b a byte array.
     * @param index where the first byte of the short value must be written.
     * @param s the value to be written in the given byte array.
     */
    static void writeShort(final byte[] b, final int index, final int s) {
        b[index] = (byte) (s >>> 8);
        b[index + 1] = (byte) s;
    }

    /**
     * Computes the future value of a bytecode offset. <p> Note: it is possible
     * to have several entries for the same instruction in the <tt>indexes</tt>
     * and <tt>sizes</tt>: two entries (index=a,size=b) and (index=a,size=b')
     * are equivalent to a single entry (index=a,size=b+b').
     * 
     * @param indexes current positions of the instructions to be resized. Each
     *        instruction must be designated by the index of its <i>last</i>
     *        byte, plus one (or, in other words, by the index of the <i>first</i>
     *        byte of the <i>next</i> instruction).
     * @param sizes the number of bytes to be <i>added</i> to the above
     *        instructions. More precisely, for each i < <tt>len</tt>,
     *        <tt>sizes</tt>[i] bytes will be added at the end of the
     *        instruction designated by <tt>indexes</tt>[i] or, if
     *        <tt>sizes</tt>[i] is negative, the <i>last</i> |<tt>sizes[i]</tt>|
     *        bytes of the instruction will be removed (the instruction size
     *        <i>must not</i> become negative or null).
     * @param begin index of the first byte of the source instruction.
     * @param end index of the first byte of the target instruction.
     * @return the future value of the given bytecode offset.
     */
    static int getNewOffset(final int[] indexes, final int[] sizes, final int begin, final int end) {
        int offset = end - begin;
        for (int i = 0; i < indexes.length; ++i) {
            if (begin < indexes[i] && indexes[i] <= end) {
                offset += sizes[i];
            } else if (end < indexes[i] && indexes[i] <= begin) {
                offset -= sizes[i];
            }
        }
        return offset;
    }

    /**
     * Updates the offset of the given label.
     * 
     * @param indexes current positions of the instructions to be resized. Each
     *        instruction must be designated by the index of its <i>last</i>
     *        byte, plus one (or, in other words, by the index of the <i>first</i>
     *        byte of the <i>next</i> instruction).
     * @param sizes the number of bytes to be <i>added</i> to the above
     *        instructions. More precisely, for each i < <tt>len</tt>,
     *        <tt>sizes</tt>[i] bytes will be added at the end of the
     *        instruction designated by <tt>indexes</tt>[i] or, if
     *        <tt>sizes</tt>[i] is negative, the <i>last</i> |<tt>sizes[i]</tt>|
     *        bytes of the instruction will be removed (the instruction size
     *        <i>must not</i> become negative or null).
     * @param label the label whose offset must be updated.
     */
    static void getNewOffset(final int[] indexes, final int[] sizes, final Label label) {
        if ((label.status & Label.RESIZED) == 0) {
            label.position = getNewOffset(indexes, sizes, 0, label.position);
            label.status |= Label.RESIZED;
        }
    }
}
