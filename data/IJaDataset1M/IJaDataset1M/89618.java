package serp.bytecode;

import java.io.*;
import serp.bytecode.visitor.*;

/**
 * The <code>ret</code> instruction is used in the implementation of finally.
 *
 * @author Abe White
 */
public class RetInstruction extends LocalVariableInstruction {

    RetInstruction(Code owner) {
        super(owner, Constants.RET);
    }

    int getLength() {
        return super.getLength() + 1;
    }

    public boolean equalsInstruction(Instruction other) {
        if (this == other) return true;
        if (!(other instanceof RetInstruction)) return false;
        return super.equalsInstruction(other);
    }

    public void acceptVisit(BCVisitor visit) {
        visit.enterRetInstruction(this);
        visit.exitRetInstruction(this);
    }

    void read(DataInput in) throws IOException {
        super.read(in);
        setLocal(in.readUnsignedByte());
    }

    void write(DataOutput out) throws IOException {
        super.write(out);
        out.writeByte(getLocal());
    }
}
