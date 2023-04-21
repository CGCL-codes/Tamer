package instructions;

import org.apache.log4j.Logger;
import machine.MachineState;

public class InstrIn extends Instr {

    private InstrInType type;

    private static final Logger logger = Logger.getLogger(InstrIn.class);

    private final int rd;

    public InstrIn(MachineState machine, int rd, InstrInType type) throws MalformedInstruction {
        super(machine);
        if (rd < 0 || rd > 31) {
            throw new MalformedInstruction("Invalid register number rd(" + rd + ")");
        }
        this.type = type;
        this.rd = rd;
    }

    @Override
    public String toString() {
        return "IN r" + rd + " " + type;
    }

    @Override
    public void execute() throws RuntimeError {
        logger.debug("Reading in " + type + " from the io space into register r(" + rd + ")");
        this.event.setPC(this.machine.getPC() + 1);
        int value;
        if (type == InstrInType.SPL) {
            value = this.machine.getStackPointer() & 0xFF;
        } else if (type == InstrInType.SPH) {
            value = this.machine.getStackPointer() & 0xFF00;
            value >>= 8;
        } else {
            throw new RuntimeError("Unsupported InstrInType of " + type);
        }
        this.event.setRd(rd, value);
    }
}
