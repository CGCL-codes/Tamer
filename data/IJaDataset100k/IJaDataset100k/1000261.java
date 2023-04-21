package edu.arizona.cs.mbel.instructions;

/** Store argument.<br>
  * Stack transition:<br>
  *   ..., value --> ...
  * @author Michael Stepp
  */
public class STARG extends Instruction implements ShortFormInstruction {

    public static final int STARG = 0x0BFE;

    public static final int STARG_S = 0x10;

    protected static final int OPCODE_LIST[] = { STARG, STARG_S };

    private int argNumber;

    /** Makes a STARG object with the given argument number, possibly short form.
     * @param shortF true iff this is a short form instruction (i.e. 0<=value<256)
     * @param value the argument number
     */
    public STARG(boolean shortF, int value) throws InstructionInitException {
        super((shortF ? STARG_S : STARG), OPCODE_LIST);
        argNumber = (value & 0xFFFF);
        if (isShort() && argNumber > 0xFF) throw new InstructionInitException("STARG: short instruction must have 1-byte argument");
    }

    public boolean isShort() {
        return (getOpcode() == STARG_S);
    }

    /** Returns the argument number.
     */
    public int getArgumentNumber() {
        return argNumber;
    }

    public String getName() {
        return (isShort() ? "starg.s" : "starg");
    }

    public int getLength() {
        return (super.getLength() + (isShort() ? 1 : 2));
    }

    protected void emit(edu.arizona.cs.mbel.ByteBuffer buffer, edu.arizona.cs.mbel.emit.ClassEmitter emitter) {
        super.emit(buffer, emitter);
        if (isShort()) buffer.put(argNumber); else buffer.putINT16(argNumber);
    }

    public STARG(int opcode, edu.arizona.cs.mbel.mbel.ClassParser parse) throws java.io.IOException, InstructionInitException {
        super(opcode, OPCODE_LIST);
        if (opcode == STARG) argNumber = parse.getMSILInputStream().readUINT16(); else argNumber = parse.getMSILInputStream().readUINT8();
    }

    public boolean equals(Object o) {
        if (!(super.equals(o) && (o instanceof STARG))) return false;
        STARG starg = (STARG) o;
        return (argNumber == starg.argNumber);
    }
}
