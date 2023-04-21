package nl.kbna.dioscuri.module.cpu;

import java.util.logging.Level;
import java.util.logging.Logger;
import nl.kbna.dioscuri.exception.ModuleException;

/**
 * Intel opcode EE<BR>
 * Output byte in AL to I/O port address specified by DX.<BR>
 * Flags modified: none
 */
public class Instruction_OUT_DXAL implements Instruction {

    private CPU cpu;

    byte data;

    int portAddress;

    private static Logger logger = Logger.getLogger("nl.kbna.dioscuri.module.cpu");

    /**
     * Class constructor
     */
    public Instruction_OUT_DXAL() {
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_OUT_DXAL(CPU processor) {
        cpu = processor;
    }

    /**
     * Output byte in AL to I/O port address in DX
     */
    public void execute() {
        try {
            portAddress = (((((int) cpu.dx[CPU.REGISTER_GENERAL_HIGH]) & 0xFF) << 8) + (((int) cpu.dx[CPU.REGISTER_GENERAL_LOW]) & 0xFF));
            if (portAddress == 1024 || portAddress == 1025 || portAddress == 1026 || portAddress == 1027) {
                logger.log(Level.CONFIG, "Instruction_OUT_DXAL targeted custom Bochs port " + portAddress + ". OUT ignored.");
            } else {
                cpu.setIOPortByte(portAddress, cpu.ax[CPU.REGISTER_GENERAL_LOW]);
            }
        } catch (ModuleException e) {
        }
    }
}
