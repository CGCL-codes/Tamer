package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

/**
 * SIPUSH - Push short
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 *
 * @version $Id: SIPUSH.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class SIPUSH extends Instruction implements ConstantPushInstruction {

    private short b;

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    SIPUSH() {
    }

    public SIPUSH(short b) {
        super(org.apache.bcel.Constants.SIPUSH, (short) 3);
        this.b = b;
    }

    /**
     * Dump instruction as short code to stream out.
     */
    public void dump(DataOutputStream out) throws IOException {
        super.dump(out);
        out.writeShort(b);
    }

    /**
     * @return mnemonic for instruction
     */
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + b;
    }

    /**
     * Read needed data (e.g. index) from file.
     */
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        length = 3;
        b = bytes.readShort();
    }

    public Number getValue() {
        return new Integer(b);
    }

    /** @return Type.SHORT
     */
    public Type getType(ConstantPoolGen cp) {
        return Type.SHORT;
    }

    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    public void accept(Visitor v) {
        v.visitPushInstruction(this);
        v.visitStackProducer(this);
        v.visitTypedInstruction(this);
        v.visitConstantPushInstruction(this);
        v.visitSIPUSH(this);
    }
}
