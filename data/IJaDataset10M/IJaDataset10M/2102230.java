package edu.arizona.cs.mbel.instructions;

/** Unconditional branch.<br>
  * Stack transition:<br>
  *   ... --> ...
  * @author Michael Stepp
  */
public class BR extends BranchInstruction {

    public static final int BR = 0x38;

    public static final int BR_S = 0x2B;

    protected static final int OPCODE_LIST[] = { BR, BR_S };

    /** Makes a BR object with the given target handle, possibly in short form
     * @param shortF true iff this is a short form instruction
     * @param ih the target handle
     */
    public BR(boolean shortF, InstructionHandle ih) throws InstructionInitException {
        super((shortF ? BR_S : BR), OPCODE_LIST, ih);
    }

    public boolean isShort() {
        return (getOpcode() == BR_S);
    }

    public String getName() {
        return (isShort() ? "br.s" : "br");
    }

    public int getLength() {
        return (super.getLength() + (isShort() ? 1 : 4));
    }

    protected void emit(edu.arizona.cs.mbel.ByteBuffer buffer, edu.arizona.cs.mbel.emit.ClassEmitter emitter) {
        super.emit(buffer, emitter);
        if (isShort()) buffer.put(getTarget()); else buffer.putINT32(getTarget());
    }

    public BR(int opcode, edu.arizona.cs.mbel.mbel.ClassParser parse) throws java.io.IOException, InstructionInitException {
        super(opcode, OPCODE_LIST);
        setTarget((isShort() ? parse.getMSILInputStream().readINT8() : parse.getMSILInputStream().readINT32()));
    }

    public boolean equals(Object o) {
        return (super.equals(o) && (o instanceof BR));
    }
}
