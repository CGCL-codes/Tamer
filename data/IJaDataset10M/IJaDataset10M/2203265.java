package nl.kbna.dioscuri.module.cpu;

/**
 * Intel opcode CD<BR>
 * Call to Interrupt Procedure.<BR>
 * The immediate byte specifies the index (0 - 255) within the Interrupt Descriptor Table (IDT).<BR>
 * Flags modified: IF, TF, AC
 */
public class Instruction_INT_Ib implements Instruction {

    private CPU cpu;

    boolean operandWordSize;

    byte index;

    int offset;

    byte[] newCS;

    byte[] newIP;

    /**
     * Class constructor
     */
    public Instruction_INT_Ib() {
        operandWordSize = true;
        index = 0;
        offset = 0;
        newCS = new byte[2];
        newIP = new byte[2];
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_INT_Ib(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Call the interrupt procedure based on the interrupt vector in the IDT.<BR>
     */
    public void execute() {
        index = cpu.getByteFromCode();
        if (index <= 255) {
            cpu.setWordToStack(Util.booleansToBytes(cpu.flags));
            cpu.flags[CPU.REGISTER_FLAGS_IF] = false;
            cpu.flags[CPU.REGISTER_FLAGS_TF] = false;
            cpu.setWordToStack(cpu.cs);
            cpu.setWordToStack(cpu.ip);
            cpu.cs = new byte[] { 0x00, 0x00 };
            offset = index * 4;
            cpu.ip = new byte[] { (byte) ((offset >> 8) & 0xFF), (byte) (offset & 0xFF) };
            newIP = cpu.getWordFromCode();
            offset += 2;
            cpu.ip = new byte[] { (byte) ((offset >> 8) & 0xFF), (byte) (offset & 0xFF) };
            newCS = cpu.getWordFromCode();
            cpu.cs[CPU.REGISTER_SEGMENT_LOW] = newCS[CPU.REGISTER_LOW];
            cpu.cs[CPU.REGISTER_SEGMENT_HIGH] = newCS[CPU.REGISTER_HIGH];
            cpu.ip[CPU.REGISTER_LOW] = newIP[CPU.REGISTER_LOW];
            cpu.ip[CPU.REGISTER_HIGH] = newIP[CPU.REGISTER_HIGH];
        } else {
        }
    }
}
