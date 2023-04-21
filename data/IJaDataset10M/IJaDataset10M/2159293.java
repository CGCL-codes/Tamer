package edu.arizona.cs.mbel.instructions;

/** Reference any type. Loads the type out of a typed reference.<br>
  * Stack transition:<br>
  *   ..., TypedRef --> ..., type
  * @author Michael Stepp
  */
public class REFANYTYPE extends Instruction {

    public static final int REFANYTYPE = 0x1DFE;

    protected static final int OPCODE_LIST[] = { REFANYTYPE };

    public REFANYTYPE() throws InstructionInitException {
        super(REFANYTYPE, OPCODE_LIST);
    }

    public String getName() {
        return "refanytype";
    }

    public REFANYTYPE(int opcode, edu.arizona.cs.mbel.mbel.ClassParser parse) throws java.io.IOException, InstructionInitException {
        super(opcode, OPCODE_LIST);
    }

    public boolean equals(Object o) {
        return (super.equals(o) && (o instanceof REFANYTYPE));
    }
}
