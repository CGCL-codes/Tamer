package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.FieldInfo;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;

/**
 * Get static field from class
 * ..., => ..., value 
 */
public class GETSTATIC extends StaticFieldInstruction {

    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo ti) {
        ClassInfo clsInfo = getClassInfo();
        if (clsInfo == null) {
            return ti.createAndThrowException("java.lang.NoClassDefFoundError", className);
        }
        FieldInfo fi = getFieldInfo();
        if (fi == null) {
            return ti.createAndThrowException("java.lang.NoSuchFieldException", (className + '.' + fname));
        }
        ClassInfo ci = fi.getClassInfo();
        if (!mi.isClinit(ci) && requiresClinitCalls(ti, ci)) {
            return ti.getPC();
        }
        ElementInfo ei = ks.sa.get(ci.getName());
        if (isNewPorFieldBoundary(ti)) {
            if (createAndSetFieldCG(ss, ei, ti)) {
                return this;
            }
        }
        switch(size) {
            case 1:
                int ival = ei.getIntField(fi);
                ti.push(ival, fi.isReference());
                break;
            case 2:
                long lval = ei.getLongField(fi);
                ti.longPush(lval);
                break;
            default:
                throw new JPFException("invalid field type");
        }
        Object attr = ei.getFieldAttr(fi);
        if (attr != null) {
            ti.setOperandAttrNoClone(attr);
        }
        return getNext(ti);
    }

    public int getLength() {
        return 3;
    }

    public int getByteCode() {
        return 0xB2;
    }
}
