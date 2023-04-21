package dioscuri.module.cpu;

import dioscuri.AbstractInstructionTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Bram Lohman\n@author Bart Kiers
 */
public class Instruction_DEC_DXTest extends AbstractInstructionTest {

    /**
     * @throws Exception
     */
    public Instruction_DEC_DXTest() throws Exception {
        super(80448, "DEC_DX.bin");
    }

    /**
     *
     */
    @Test
    public void testExecute() {
        String DX_ERROR = "DX contains wrong value";
        String OF_ERROR = "OF incorrect";
        String SF_ERROR = "SF incorrect";
        String ZF_ERROR = "ZF incorrect";
        String AF_ERROR = "AF incorrect";
        String PF_ERROR = "PF incorrect";
        String CF_ERROR = "CF incorrect";
        assertFalse(OF_ERROR, cpu.getFlagValue('O'));
        assertFalse(SF_ERROR, cpu.getFlagValue('S'));
        assertFalse(ZF_ERROR, cpu.getFlagValue('Z'));
        assertFalse(AF_ERROR, cpu.getFlagValue('A'));
        assertFalse(PF_ERROR, cpu.getFlagValue('P'));
        assertFalse(CF_ERROR, cpu.getFlagValue('C'));
        cpu.startDebug();
        assertTrue(AF_ERROR, cpu.getFlagValue('A'));
        cpu.startDebug();
        assertEquals(DX_ERROR, (byte) 0x80, cpu.getRegisterValue("DX")[0]);
        assertEquals(DX_ERROR, (byte) 0x00, cpu.getRegisterValue("DX")[1]);
        cpu.startDebug();
        assertTrue(OF_ERROR, cpu.getFlagValue('O'));
        assertFalse(SF_ERROR, cpu.getFlagValue('S'));
        cpu.startDebug();
        assertEquals(DX_ERROR, (byte) 0x00, cpu.getRegisterValue("DX")[0]);
        assertEquals(DX_ERROR, (byte) 0x01, cpu.getRegisterValue("DX")[1]);
        cpu.startDebug();
        assertFalse(OF_ERROR, cpu.getFlagValue('O'));
        assertTrue(ZF_ERROR, cpu.getFlagValue('Z'));
        cpu.startDebug();
    }
}
