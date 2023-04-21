package svc.core.event;

import java.util.EventObject;
import svc.core.cpu.Instruction;
import svc.core.memory.Address;

/**
 * Encapsulates useful information when an instruction is fetched or executed.
 * 
 * @author Allen Charlton
 */
public class InstructionEvent extends EventObject {

    private static final long serialVersionUID = -1942186671291183990L;

    public Instruction instruction;

    public Address location;

    public InstructionEvent(Instruction instruction, Address location) {
        super(location);
        this.instruction = instruction;
    }

    /**
	 * Returns the location of the instruction involved.
	 * 
	 * @return the memory address of the instruction
	 */
    public Address getInstructionLocation() {
        return (Address) getSource();
    }

    /**
	 * Returns the instruction involved in this event.
	 * 
	 * @return the instruction
	 */
    public Instruction getInstruction() {
        return instruction;
    }
}
