package edu.arizona.cs.mbel.instructions;

/** Breakpoint for debugging.<br>
  * Stack transition:<br>
  *   ... --> ...
  * @author Michael Stepp
  */
public class BREAK extends Instruction {

    public static final int BREAK = 0x01;

    protected static final int OPCODE_LIST[] = { BREAK };

    public BREAK() throws InstructionInitException {
        super(BREAK, OPCODE_LIST);
    }

    public String getName() {
        return "break";
    }

    public BREAK(int opcode, edu.arizona.cs.mbel.mbel.ClassParser parse) throws java.io.IOException, InstructionInitException {
        super(opcode, OPCODE_LIST);
    }

    public boolean equals(Object o) {
        return (super.equals(o) && (o instanceof BREAK));
    }
}
