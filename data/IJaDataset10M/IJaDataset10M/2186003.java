package instructions;

import java.util.Map;
import machine.MachineState;
import org.apache.log4j.Logger;

/**
 * RET - Return from Subroutine
 * @author Ryan Moore
 *
 */
public class InstrRet extends Instr {

    private static final Logger logger = Logger.getLogger(InstrRet.class);

    public InstrRet(MachineState machine) {
        super(machine);
    }

    @Override
    public String toString() {
        return "RET";
    }

    @Override
    public void execute() {
        logger.debug("Executing a function return");
        int bottomMost = this.machine.getStack(this.machine.getStackPointer() + 1);
        int topMost = this.machine.getStack(this.machine.getStackPointer() + 2);
        topMost = topMost << 8;
        int realPC = topMost | bottomMost;
        logger.debug("The PC returned from the stack is (0x" + Integer.toHexString(realPC) + ")");
        this.event.setPC(realPC);
        this.event.setStackPointer(this.machine.getStackPointer() + 2);
        if (logger.isTraceEnabled()) {
            Map<Integer, Integer> stack = machine.getStack();
            logger.trace("Print the values on the stack.");
            logger.trace("Stack pointer - 0x" + Integer.toHexString(machine.getStackPointer()));
            logger.trace("---------------------------------");
            for (Integer key : stack.keySet()) {
                logger.trace("0x" + Integer.toHexString(key) + " - " + stack.get(key));
            }
        }
    }
}
