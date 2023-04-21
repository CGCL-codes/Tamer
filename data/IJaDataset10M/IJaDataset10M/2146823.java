package gov.nasa.jpf.jvm;

import java.io.PrintStream;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.util.HashData;
import org.apache.bcel.Constants;

/**
 * Describes a stack frame.
 *
 * implementation is based on the fact that each Java method has a fixed size
 * operand stack (overrun actually checked by a real VM), and the heuristics that
 *  (a) stack / local operations are frequent
 *  (b) stack / local sizes are typically small (both < 10)
 * hence a BitSet is not too useful
 */
public class StackFrame implements Constants, Cloneable {

    protected int top;

    /** top index of the operand stack (NOT size) */
    protected int thisRef = -1;

    /** local[0] can change, but we have to keep 'this' */
    protected int[] operands;

    /** the operand stack */
    protected boolean[] isOperandRef;

    /** This array can be used to store attributes (e.g. variable names) for
   * operands. We don't do anything with this except of preserving it (across
   * dups etc.), so it's pretty much up to the VM listeners what's stored
   *
   * these are set on demand
   */
    protected Object[] operandAttr;

    protected Object[] localAttr;

    protected int[] locals;

    /** the local variables */
    protected boolean[] isLocalRef;

    /** which local slots hold references */
    protected Instruction pc;

    /** the next insn to execute (program counter) */
    protected MethodInfo mi;

    /** which method is executed in this frame */
    public boolean hasReferenceOperand(int n, int objRef) {
        int[] op = operands;
        boolean[] ref = isOperandRef;
        for (int i = 0, j = top - n + 1; i < n && j >= 0; i++, j++) {
            if (ref[j] && (op[j] == objRef)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Creates a new stack frame for a given method.
   * 'isDirect' specifies if this method was called directly by the VM, i.e. there is
   * no corresponding INVOKE insn in the underlying stack frame (for instance, that's
   * important to know for handling return values and computing the next pc)
   * 'caller' is the calling stack frame (if any)
   */
    public StackFrame(MethodInfo m, StackFrame caller) {
        mi = m;
        pc = mi.getInstruction(0);
        int nOperands = mi.getMaxStack();
        operands = new int[nOperands];
        isOperandRef = new boolean[nOperands];
        top = -1;
        int nargs = mi.getArgumentsSize();
        int nlocals = (pc == null) ? nargs : mi.getMaxLocals();
        locals = new int[nlocals];
        isLocalRef = new boolean[nlocals];
        if ((nargs > 0) && (caller != null)) {
            int[] a = caller.operands;
            boolean[] r = caller.isOperandRef;
            for (int i = 0, j = caller.top - nargs + 1; i < nargs; i++, j++) {
                locals[i] = a[j];
                isLocalRef[i] = r[j];
            }
            if (!mi.isStatic()) {
                thisRef = locals[0];
            }
            if (caller.operandAttr != null) {
                operandAttr = new Object[operands.length];
                localAttr = new Object[locals.length];
                Object[] oa = caller.operandAttr;
                for (int i = 0, j = caller.top - nargs + 1; i < nargs; i++, j++) {
                    localAttr[i] = oa[j];
                }
            }
        }
    }

    public StackFrame(MethodInfo m, int objRef) {
        this(m, null);
        thisRef = objRef;
        locals[0] = thisRef;
        isLocalRef[0] = true;
    }

    /**
   * Creates an empty stack frame. Used by clone.
   */
    protected StackFrame() {
    }

    /**
   * return the object reference for an instance method to be called (we are still in the
   * caller's frame). This only makes sense after all params have been pushed, before the
   * INVOKEx insn is executed
   */
    public int getCalleeThis(MethodInfo mi) {
        return getCalleeThis(mi.getArgumentsSize());
    }

    /**
   * return reference of called object in the context of the caller
   * (i.e. we are in the caller frame)
   */
    public int getCalleeThis(int size) {
        int i = size - 1;
        if (top < i) {
            return -1;
        }
        return operands[top - i];
    }

    public Object getLocalOrFieldValue(String id) {
        String[] localNames = mi.getLocalVariableNames();
        for (int i = 0; i < localNames.length; i++) {
            if (localNames[i].equals(id)) {
                return getLocalValueObject(i);
            }
        }
        return getFieldValue(id);
    }

    public Object getLocalValueObject(int i) {
        String[] localTypes = mi.getLocalVariableTypes();
        if (localTypes != null) {
            String type = localTypes[i];
            if ("boolean".equals(type)) {
                return locals[i] != 0 ? Boolean.TRUE : Boolean.FALSE;
            } else if ("byte".equals(type)) {
                return new Byte((byte) locals[i]);
            } else if ("char".equals(type)) {
                return new Character((char) locals[i]);
            } else if ("short".equals(type)) {
                return new Short((short) locals[i]);
            } else if ("int".equals(type)) {
                return new Integer(locals[i]);
            } else if ("float".equals(type)) {
                return new Float(Float.intBitsToFloat(locals[i]));
            } else if ("long".equals(type)) {
                return new Long(Types.intsToLong(locals[i], locals[i + 1]));
            } else if ("double".equals(type)) {
                return new Double(Double.longBitsToDouble(Types.intsToLong(locals[i], locals[i + 1])));
            } else {
                if (locals[i] != -1) {
                    return DynamicArea.getHeap().get(locals[i]);
                }
            }
        }
        return null;
    }

    public Object getFieldValue(String id) {
        if (thisRef != -1) {
            ElementInfo ei = DynamicArea.getHeap().get(thisRef);
            Object v = ei.getFieldValueObject(id);
            if (v != null) {
                return v;
            }
        }
        return mi.getClassInfo().getStaticFieldValueObject(id);
    }

    public ClassInfo getClassInfo() {
        return mi.getClassInfo();
    }

    public String getClassName() {
        return mi.getClassInfo().getName();
    }

    public String getSourceFile() {
        return mi.getClassInfo().getSourceFileName();
    }

    public boolean isDirectCallFrame() {
        return false;
    }

    public int getLine() {
        return mi.getLineNumber(pc);
    }

    public void setOperandAttr(Object attr) {
        setOperandAttr(0, attr);
    }

    public void setOperandAttr(int offset, Object attr) {
        if (operandAttr == null && attr != null) {
            operandAttr = new Object[operands.length];
            localAttr = new Object[locals.length];
        }
        if (operandAttr != null) {
            operandAttr[top - offset] = attr;
        }
    }

    public void setLongOperandAttr(Object attr) {
        setOperandAttr(1, attr);
    }

    public void setLocalAttr(int index, Object attr) {
        if (index < locals.length) {
            if (localAttr == null && attr != null) {
                operandAttr = new Object[operands.length];
                localAttr = new Object[locals.length];
            }
            if (localAttr != null) {
                localAttr[index] = attr;
            }
        }
    }

    public Object[] getArgumentAttrs(MethodInfo miCallee) {
        if (operandAttr != null) {
            int nArgs = miCallee.getNumberOfArguments();
            byte[] at = miCallee.getArgumentTypes();
            Object[] attrs;
            if (!miCallee.isStatic()) {
                attrs = new Object[nArgs + 1];
                attrs[0] = getOperandAttr(miCallee.getArgumentsSize() - 1);
            } else {
                attrs = new Object[nArgs];
            }
            for (int i = nArgs - 1, off = 0, j = attrs.length - 1; i >= 0; i--, j--) {
                byte argType = at[i];
                if (argType == Types.T_LONG || argType == Types.T_DOUBLE) {
                    attrs[j] = getOperandAttr(off + 1);
                    off += 2;
                } else {
                    attrs[j] = getOperandAttr(off);
                    off++;
                }
            }
            return attrs;
        } else {
            return null;
        }
    }

    public int getAbsOperand(int idx) {
        return operands[idx];
    }

    public boolean isAbsOperandRef(int idx) {
        return isOperandRef[idx];
    }

    public Object getLongOperandAttr() {
        return getOperandAttr(1);
    }

    public boolean hasOperandAttrs() {
        return operandAttr != null;
    }

    public boolean hasLocalAtts() {
        return localAttr != null;
    }

    public Object getOperandAttr() {
        if ((top >= 0) && (operandAttr != null)) {
            return operandAttr[top];
        } else {
            return null;
        }
    }

    public Object getOperandAttr(int offset) {
        if ((top >= offset) && (operandAttr != null)) {
            return operandAttr[top - offset];
        } else {
            return null;
        }
    }

    public void setOperand(int offset, int v, boolean ref) {
        int i = top - offset;
        operands[i] = v;
        isOperandRef[i] = ref;
    }

    public Object getLocalAttr(int index) {
        if ((index < locals.length) && (localAttr != null)) {
            return localAttr[index];
        } else {
            return null;
        }
    }

    public void setLocalVariable(int index, int v, boolean ref) {
        boolean activateGc = (isLocalRef[index] && (locals[index] != -1));
        locals[index] = v;
        isLocalRef[index] = ref;
        if (ref) {
            if (v != -1) activateGc = true;
        }
        if (activateGc) {
            JVM.getVM().getSystemState().activateGC();
        }
    }

    public int getLocalVariable(int i) {
        return locals[i];
    }

    public int getLocalVariable(String name) {
        int idx = getLocalVariableOffset(name);
        if (idx >= 0) {
            return getLocalVariable(idx);
        } else {
            throw new JPFException("local variable not found: " + name);
        }
    }

    public int getLocalVariableCount() {
        return locals.length;
    }

    public String[] getLocalVariableNames() {
        return mi.getLocalVariableNames();
    }

    public boolean isLocalVariableRef(int idx) {
        return isLocalRef[idx];
    }

    public String getLocalVariableType(String name) {
        String[] lNames = mi.getLocalVariableNames();
        String[] lTypes = mi.getLocalVariableTypes();
        if ((lNames != null) && (lTypes != null)) {
            for (int i = 0, l = lNames.length; i < l; i++) {
                if (name.equals(lNames[i])) {
                    return lTypes[i];
                }
            }
        }
        return null;
    }

    int[] getLocalVariables() {
        return locals;
    }

    public void setLongLocalVariable(int index, long v) {
        locals[index] = Types.hiLong(v);
        isLocalRef[index] = false;
        index++;
        locals[index] = Types.loLong(v);
        isLocalRef[index] = false;
    }

    public long getLongLocalVariable(int i) {
        return Types.intsToLong(locals[i + 1], locals[i]);
    }

    public long getLongLocalVariable(String name) {
        int idx = getLocalVariableOffset(name);
        if (idx >= 0) {
            return getLongLocalVariable(idx);
        } else {
            throw new JPFException("long local variable not found: " + name);
        }
    }

    public MethodInfo getMethodInfo() {
        return mi;
    }

    public String getMethodName() {
        return mi.getName();
    }

    public boolean isOperandRef(int idx) {
        return isOperandRef[top - idx];
    }

    public boolean isOperandRef() {
        return isOperandRef[top];
    }

    public void setPC(Instruction newpc) {
        pc = newpc;
    }

    public Instruction getPC() {
        return pc;
    }

    public void advancePC() {
        int i = pc.getOffset() + 1;
        if (i < mi.getNumberOfInstructions()) {
            pc = mi.getInstruction(i);
        } else {
            pc = null;
        }
    }

    public int getTopPos() {
        return top;
    }

    public String getStackTraceInfo() {
        StringBuilder sb = new StringBuilder(128);
        ClassInfo ciMi = mi.getClassInfo();
        if (ciMi != null) {
            sb.append(mi.getClassInfo().getName());
            sb.append('.');
        }
        sb.append(mi.getName());
        if (pc != null) {
            if (ciMi != null) {
                sb.append('(');
                sb.append(pc.getFilePos());
                sb.append(')');
            } else {
                sb.append("(Synthetic)");
            }
        } else {
            sb.append("(Native Method)");
        }
        return sb.toString();
    }

    /**
   * if this is an instance method, return the reference of the corresponding object
   * (note this only has to be in slot 0 upon entry)
   */
    public int getThis() {
        return thisRef;
    }

    public void clearOperandStack() {
        top = -1;
    }

    public StackFrame clone() {
        try {
            StackFrame sf = (StackFrame) super.clone();
            sf.operands = operands.clone();
            sf.isOperandRef = isOperandRef.clone();
            if (operandAttr != null) {
                sf.operandAttr = operandAttr.clone();
            }
            sf.locals = locals.clone();
            sf.isLocalRef = isLocalRef.clone();
            if (localAttr != null) {
                sf.localAttr = localAttr.clone();
            }
            return sf;
        } catch (CloneNotSupportedException cnsx) {
            throw new JPFException(cnsx);
        }
    }

    public void dup() {
        int t = top;
        top++;
        operands[top] = operands[t];
        isOperandRef[top] = isOperandRef[t];
        if (operandAttr != null) {
            operandAttr[top] = operandAttr[t];
        }
    }

    public void dup2() {
        int td = top + 1;
        int ts = top - 1;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td++;
        ts++;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        top = td;
    }

    public void dup2_x1() {
        int b, c;
        boolean bRef, cRef;
        Object bAnn = null, cAnn = null;
        int ts, td;
        ts = top - 1;
        td = top + 1;
        operands[td] = b = operands[ts];
        isOperandRef[td] = bRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = bAnn = operandAttr[ts];
        }
        ts = top;
        td++;
        operands[td] = c = operands[ts];
        isOperandRef[td] = cRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = cAnn = operandAttr[ts];
        }
        ts = top - 2;
        td = top;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td = ts;
        operands[td] = b;
        isOperandRef[td] = bRef;
        if (operandAttr != null) {
            operandAttr[td] = bAnn;
        }
        td++;
        operands[td] = c;
        isOperandRef[td] = cRef;
        if (operandAttr != null) {
            operandAttr[td] = cAnn;
        }
        top += 2;
    }

    public void dup2_x2() {
        int c, d;
        boolean cRef, dRef;
        Object cAnn = null, dAnn = null;
        int ts, td;
        ts = top - 1;
        td = top + 1;
        operands[td] = c = operands[ts];
        isOperandRef[td] = cRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = cAnn = operandAttr[ts];
        }
        ts = top;
        td++;
        operands[td] = d = operands[ts];
        isOperandRef[td] = dRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = dAnn = operandAttr[ts];
        }
        ts = top - 3;
        td = top - 1;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        ts++;
        td = top;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td = ts;
        operands[td] = c;
        isOperandRef[td] = cRef;
        if (operandAttr != null) {
            operandAttr[td] = cAnn;
        }
        td++;
        operands[td] = d;
        isOperandRef[td] = dRef;
        if (operandAttr != null) {
            operandAttr[td] = dAnn;
        }
        top += 2;
    }

    public void dup_x1() {
        int b;
        boolean bRef;
        Object bAnn = null;
        int ts, td;
        ts = top;
        td = top + 1;
        operands[td] = b = operands[ts];
        isOperandRef[td] = bRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = bAnn = operandAttr[ts];
        }
        ts = top - 1;
        td = top;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td = ts;
        operands[td] = b;
        isOperandRef[td] = bRef;
        if (operandAttr != null) {
            operandAttr[td] = bAnn;
        }
        top++;
    }

    public void dup_x2() {
        int c;
        boolean cRef;
        Object cAnn = null;
        int ts, td;
        ts = top;
        td = top + 1;
        operands[td] = c = operands[ts];
        isOperandRef[td] = cRef = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = cAnn = operandAttr[ts];
        }
        ts = top - 1;
        td = top;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td = ts;
        ts--;
        operands[td] = operands[ts];
        isOperandRef[td] = isOperandRef[ts];
        if (operandAttr != null) {
            operandAttr[td] = operandAttr[ts];
        }
        td = ts;
        operands[td] = c;
        isOperandRef[td] = cRef;
        if (operandAttr != null) {
            operandAttr[td] = cAnn;
        }
        top++;
    }

    public boolean equals(Object object) {
        StackFrame sf = (StackFrame) object;
        if (pc != sf.pc) {
            return false;
        }
        if (mi != sf.mi) {
            return false;
        }
        int[] l = sf.locals;
        boolean[] lr = sf.isLocalRef;
        int nlocals = locals.length;
        if (nlocals != l.length) {
            return false;
        }
        for (int idx = 0; idx < nlocals; idx++) {
            if ((locals[idx] != l[idx]) || (isLocalRef[idx] != lr[idx])) {
                return false;
            }
        }
        int[] o = sf.operands;
        boolean[] or = sf.isOperandRef;
        if (top != sf.top) {
            return false;
        }
        for (int idx = 0; idx <= top; idx++) {
            if ((operands[idx] != o[idx]) || (isOperandRef[idx] != or[idx])) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyRef() {
        for (int i = 0; i <= top; i++) {
            if (isOperandRef[i]) {
                return true;
            }
        }
        for (int i = 0, l = locals.length; i < l; i++) {
            if (isLocalRef[i]) {
                return true;
            }
        }
        return false;
    }

    public void hash(HashData hd) {
        Object[] attrs;
        int[] v;
        v = locals;
        for (int i = 0, l = v.length; i < l; i++) {
            hd.add(v[i]);
        }
        attrs = localAttr;
        if (attrs != null) {
            for (int i = 0, l = attrs.length; i < l; i++) {
                hd.add(attrs[i]);
            }
        }
        v = operands;
        for (int i = 0; i <= top; i++) {
            hd.add(v[i]);
        }
        attrs = operandAttr;
        if (attrs != null) {
            for (int i = 0, l = attrs.length; i < l; i++) {
                hd.add(attrs[i]);
            }
        }
    }

    public int hashCode() {
        HashData hd = new HashData();
        hash(hd);
        return hd.getValue();
    }

    /**
   * mark all objects reachable from local or operand stack positions containing
   * references. Done during phase1 marking of threads (the stack is one of the
   * Thread gc roots)
   */
    public void markThreadRoots(int tid) {
        DynamicArea heap = DynamicArea.getHeap();
        for (int i = 0; i <= top; i++) {
            if (isOperandRef[i]) {
                heap.markThreadRoot(operands[i], tid);
            }
        }
        for (int i = 0, l = locals.length; i < l; i++) {
            if (isLocalRef[i]) {
                heap.markThreadRoot(locals[i], tid);
            }
        }
    }

    /**
   * this includes locals and pc. Just for debugging
   */
    public void printStackContent() {
        PrintStream pw = System.err;
        pw.print("\tat ");
        pw.print(mi.getCompleteName());
        if (pc != null) {
            pw.println(":" + pc.getPosition());
        } else {
            pw.println();
        }
        pw.println("\t  Operand stack is:");
        for (int i = 0; i <= top; i++) {
            pw.print("\t    ");
            if (isOperandRef[i]) {
                pw.print("#");
            }
            pw.println(operands[i]);
        }
        pw.println("\t  Local variables are:");
        for (int i = 0, l = locals.length; i < l; i++) {
            pw.print("\t    ");
            if (isLocalRef[i]) {
                pw.print("#");
            }
            pw.println("" + locals[i]);
        }
    }

    public void printStackTrace() {
        System.err.println(getStackTraceInfo());
    }

    public void swap() {
        int t = top - 1;
        int v = operands[top];
        boolean isRef = isOperandRef[top];
        operands[top] = operands[t];
        isOperandRef[top] = isOperandRef[t];
        operands[t] = v;
        isOperandRef[t] = isRef;
        if (operandAttr != null) {
            Object attr = operandAttr[top];
            operandAttr[top] = operandAttr[t];
            operandAttr[t] = attr;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StackFrame[");
        sb.append(mi.getUniqueName());
        sb.append(",top=");
        sb.append(top);
        sb.append(",operands=[");
        for (int i = 0; i <= top; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(operands[i]);
            if (operandAttr != null && operandAttr[i] != null) {
                sb.append('(');
                sb.append(operandAttr[i]);
                sb.append(')');
            }
        }
        sb.append("],locals=[");
        for (int i = 0; i < locals.length; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(locals[i]);
            if ((localAttr != null) && (localAttr[i] != null)) {
                sb.append('(');
                sb.append(localAttr[i]);
                sb.append(')');
            }
        }
        sb.append("],pc=");
        sb.append(pc.getPosition());
        sb.append(",oRefs=");
        for (int i = 0; i <= top; i++) {
            sb.append(isOperandRef[i] ? 'R' : '-');
        }
        sb.append(",lRefs=");
        for (int i = 0; i < locals.length; i++) {
            sb.append(isLocalRef[i] ? 'R' : '-');
        }
        sb.append(']');
        return sb.toString();
    }

    public long longPeek() {
        return Types.intsToLong(operands[top], operands[top - 1]);
    }

    public long longPeek(int n) {
        int i = top - n;
        return Types.intsToLong(operands[i], operands[i - 1]);
    }

    public void longPush(long v) {
        push(Types.hiLong(v));
        push(Types.loLong(v));
    }

    public void doublePush(double v) {
        push(Types.hiDouble(v));
        push(Types.loDouble(v));
    }

    public double doublePop() {
        int i = top;
        int lo = operands[i--];
        int hi = operands[i--];
        if (operandAttr != null) {
            i = top;
            operandAttr[i--] = null;
            operandAttr[i--] = null;
        }
        top = i;
        return Types.intsToDouble(lo, hi);
    }

    public long longPop() {
        int i = top;
        int lo = operands[i--];
        int hi = operands[i--];
        if (operandAttr != null) {
            i = top;
            operandAttr[i--] = null;
            operandAttr[i--] = null;
        }
        top = i;
        return Types.intsToLong(lo, hi);
    }

    public int peek() {
        return operands[top];
    }

    public int peek(int offset) {
        return operands[top - offset];
    }

    public void pop(int n) {
        int t = top - n;
        for (int i = top; i > t; i--) {
            if (isOperandRef[i] && (operands[i] != -1)) {
                JVM.getVM().getSystemState().activateGC();
                break;
            }
        }
        if (operandAttr != null) {
            for (int i = top; i > t; i--) {
                operandAttr[i] = null;
            }
        }
        top = t;
    }

    public int pop() {
        int v = operands[top];
        if (isOperandRef[top]) {
            if (v != -1) {
                JVM.getVM().getSystemState().activateGC();
            }
        }
        if (operandAttr != null) {
            operandAttr[top] = null;
        }
        top--;
        return v;
    }

    public void pushLocal(int index) {
        top++;
        operands[top] = locals[index];
        isOperandRef[top] = isLocalRef[index];
        if (localAttr != null) {
            operandAttr[top] = localAttr[index];
        }
    }

    public void pushLongLocal(int index) {
        int t = top;
        operands[++t] = locals[index];
        isOperandRef[t] = false;
        operands[++t] = locals[index + 1];
        isOperandRef[t] = false;
        if (operandAttr != null) {
            operandAttr[t - 1] = localAttr[index];
            operandAttr[t] = null;
        }
        top = t;
    }

    public void storeOperand(int index) {
        locals[index] = operands[top];
        isLocalRef[index] = isOperandRef[top];
        if (localAttr != null) {
            localAttr[index] = operandAttr[top];
            operandAttr[top] = null;
        }
        top--;
    }

    public void storeLongOperand(int index) {
        int t = top - 1;
        int i = index;
        locals[i] = operands[t];
        isLocalRef[i] = false;
        locals[++i] = operands[t + 1];
        isLocalRef[i] = false;
        if (localAttr != null) {
            localAttr[index] = operandAttr[t];
            localAttr[i] = null;
            operandAttr[t] = null;
            operandAttr[t + 1] = null;
        }
        top -= 2;
    }

    public void push(int v) {
        top++;
        operands[top] = v;
        isOperandRef[top] = false;
    }

    public void pushRef(int ref) {
        top++;
        operands[top] = ref;
        isOperandRef[top] = true;
        if (ref != -1) {
            JVM.getVM().getSystemState().activateGC();
        }
    }

    public void push(int v, boolean ref) {
        top++;
        operands[top] = v;
        isOperandRef[top] = ref;
        if (ref && (v != -1)) {
            JVM.getVM().getSystemState().activateGC();
        }
    }

    public int getLocalVariableOffset(String name) {
        String[] lNames = mi.getLocalVariableNames();
        String[] lTypes = mi.getLocalVariableTypes();
        int offset = 0;
        for (int i = 0, l = lNames.length; i < l; ) {
            if (name.equals(lNames[i])) {
                return offset;
            } else if (lTypes[i].charAt(0) != '?') {
                int typeSize = Types.getTypeSize(lTypes[i]);
                offset += typeSize;
                i += typeSize;
            }
        }
        return -1;
    }
}
