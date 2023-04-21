package edu.arizona.cs.mbel.instructions;

/** Return from a method call.<br>
  * Stack transition:<br>
  *   [retval from callee stack] --> [retVal from caller stack]
  * @author Michael Stepp
  */
public class RET extends Instruction {

    public static final int RET = 0x2A;

    protected static final int OPCODE_LIST[] = { RET };

    public RET() throws InstructionInitException {
        super(RET, OPCODE_LIST);
    }

    public String getName() {
        return "ret";
    }

    public RET(int opcode, edu.arizona.cs.mbel.mbel.ClassParser parse) throws java.io.IOException, InstructionInitException {
        super(opcode, OPCODE_LIST);
    }

    public boolean equals(Object o) {
        return (super.equals(o) && (o instanceof RET));
    }
}
