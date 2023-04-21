package jaxlib.java.asm.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import jaxlib.java.asm.AsmClass;
import jaxlib.java.asm.AsmConstantPool;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AsmAttributeEnclosingMethod.java 2270 2007-03-16 08:42:38Z joerg_wassmer $
 *
 * @todo    comment class
 */
public final class AsmAttributeEnclosingMethod extends AsmAttribute {

    public static final String NAME = "EnclosingMethod";

    private final String className;

    private final String methodName;

    private final String methodDescriptor;

    AsmAttributeEnclosingMethod(DataInput in, AsmConstantPool cp) throws IOException {
        super(NAME);
        int attributeLength = in.readInt();
        if (attributeLength != 4) throw new AsmAttributeException("attribute length(" + attributeLength + ") != 4");
        this.className = cp.getClassName(in.readChar());
        int methodIndex = in.readChar();
        if (methodIndex == 0) {
            this.methodName = null;
            this.methodDescriptor = null;
        } else {
            AsmConstantPool.NameAndType nt = cp.getConstantNameAndType(methodIndex);
            this.methodName = nt.getName();
            this.methodDescriptor = nt.getDescriptor();
        }
    }

    public final int getAttributeLength() {
        return 4;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodDescriptor() {
        return this.methodDescriptor;
    }

    public void writeAttributeData(AsmClass clazz, DataOutput out) throws IOException {
        AsmConstantPool cp = clazz.getConstantPool();
        out.writeChar(cp.addConstantClass(this.className));
        if (this.methodName == null) out.writeChar(0); else out.writeChar(cp.addConstantNameAndType(this.methodName, this.methodDescriptor));
    }
}
