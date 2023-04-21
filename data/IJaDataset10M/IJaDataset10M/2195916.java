package nl.kbna.dioscuri.module.cpu;

/**
	 * Intel opcode 8C<BR>
	 * Word-sized copy of memory/register (destination) from segment register (source).<BR>
	 * The addressbyte determines the source (rrr bits) and destination (sss bits).<BR>
	 * Flags modified: none
	 */
public class Instruction_MOV_EwSw implements Instruction {

    private CPU cpu;

    boolean operandWordSize = true;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte[] sourceRegister = new byte[2];

    byte[] destinationRegister = new byte[2];

    /**
	 * Class constructor
	 */
    public Instruction_MOV_EwSw() {
    }

    /**
	 * Class constructor specifying processor reference
	 * 
	 * @param processor	Reference to CPU class
	 */
    public Instruction_MOV_EwSw(CPU processor) {
        this();
        cpu = processor;
    }

    /**
	 * Word-sized copy of memory/register (destination) from register (source).<BR>
	 * Flags modified: none
	 */
    public void execute() {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        sourceRegister = (cpu.decodeSegmentRegister((addressByte & 0x38) >> 3));
        if (((addressByte >> 6) & 0x03) == 3) {
            destinationRegister = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
            System.arraycopy(sourceRegister, 0, destinationRegister, 0, sourceRegister.length);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceRegister);
        }
    }
}
