package test.zmpp.instructions;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.zmpp.base.DefaultMemory;
import static org.junit.Assert.*;
import org.zmpp.base.Memory;
import org.zmpp.instructions.AbstractInstruction;
import org.zmpp.instructions.InstructionDecoder;
import org.zmpp.vm.Machine;
import test.zmpp.vm.MiniZorkSetup;

/**
 * This class contains tests for the InstructionDecoder class.
 *
 * @author Wei-ju Wu
 * @version 1.5
 */
@RunWith(JMock.class)
public class InstructionDecoderTest extends MiniZorkSetup {

    private Mockery context = new JUnit4Mockery();

    private InstructionDecoder decoder;

    private byte[] call_vs2 = { (byte) 0xec, 0x25, (byte) 0xbf, 0x3b, (byte) 0xf7, (byte) 0xa0, 0x10, 0x20, 0x01, 0x00 };

    private byte[] save_undo = { (byte) 0xbe, (byte) 0x09, (byte) 0xff, (byte) 0x00 };

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        decoder = new InstructionDecoder();
        decoder.initialize(machine);
    }

    /**
   * Tests for minizork's instructions. This is more of an integration test,
   * leave it here anyways.
   */
    @Test
    public void testMinizorkVarInstruction() {
        machine.pushStack((char) 0, (char) 1);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x37d9);
        assertEquals("CALL $1d9b, $3e88, $ffff -> (SP)", info.toString());
        assertEquals(9, info.getLength());
        AbstractInstruction info2 = (AbstractInstruction) decoder.decodeInstruction(0x37e2);
        assertEquals("STOREW (SP)[$0001], $00, $01", info2.toString());
        assertEquals(5, info2.getLength());
    }

    @Test
    public void testMinizorkBranch() {
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x3773);
        assertEquals("JZ G17[$0001]", info.toString());
        assertEquals(3, info.getLength());
    }

    @Test
    public void testMinizorkRet() {
        machine.call((char) 0x1bc5, 0x3e88, new char[] { 0xffff }, (char) 0);
        machine.setVariable((char) 5, (char) 7);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x37d5);
        assertEquals("RET L04[$0007]", info.toString());
        assertEquals(2, info.getLength());
    }

    @Test
    public void testMinizorkC1OP() {
        machine.call((char) 0x1bc5, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x379f);
        assertEquals("DEC $02", info.toString());
        assertEquals(2, info.getLength());
        AbstractInstruction info2 = (AbstractInstruction) decoder.decodeInstruction(0x3816);
        assertEquals("JUMP $ffc2", info2.toString());
        assertEquals(2, info.getLength());
        AbstractInstruction info3 = (AbstractInstruction) decoder.decodeInstruction(0x37c7);
        assertEquals("INC $03", info3.toString());
        assertEquals(2, info.getLength());
    }

    @Test
    public void testMinizorkC0Op() {
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x3788);
        assertEquals("RFALSE", info.toString().trim());
        assertEquals(1, info.getLength());
    }

    @Test
    public void testMinizorkLong() {
        machine.call((char) 0x1bc5, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x37c9);
        assertEquals("JE L02[$0000], L01[$0000]", info.toString());
        assertEquals(4, info.getLength());
    }

    @Test
    public void testMinizorkPrint() {
        machine.call((char) 0x1c13, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x393f);
        assertEquals("PRINT \"object\"", info.toString());
        assertEquals(5, info.getLength());
        machine.call((char) 0x2baf, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info2 = (AbstractInstruction) decoder.decodeInstruction(0x5761);
        assertEquals("PRINT_RET \"Ok.\"", info2.toString());
        assertEquals(5, info2.getLength());
    }

    @Test
    public void testMinizorkLongVar() {
        machine.setVariable((char) 0, (char) 0x4711);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x58d4);
        assertEquals("AND (SP)[$4711], $07ff -> (SP)", info.toString());
        assertEquals(6, info.getLength());
    }

    @Test
    public void testMinizorkJump() {
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x58f7);
        assertEquals("JUMP $fff4", info.toString());
        assertEquals(3, info.getLength());
    }

    @Test
    public void testMinizorkGetSibling() {
        machine.call((char) 0x368b, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x6dbd);
        assertEquals("GET_SIBLING L03[$0000] -> L03", info.toString());
        assertEquals(5, info.getLength());
    }

    @Test
    public void testJe3Operands() {
        machine.call((char) 0x368b, 0x3e88, new char[] { 0xffff }, (char) 0);
        AbstractInstruction info = (AbstractInstruction) decoder.decodeInstruction(0x6dc5);
        assertEquals("JE L03[$0000], L06[$0000], $1e", info.toString());
        assertEquals(7, info.getLength());
    }

    @Test
    public void testDecodeCallVs2() {
        final Memory call_vs2Mem = new DefaultMemory(call_vs2);
        final Machine machine4 = context.mock(Machine.class);
        InstructionDecoder decoder4 = new InstructionDecoder();
        decoder4.initialize(machine4);
        context.checking(new Expectations() {

            {
                atLeast(1).of(machine4).getVersion();
                will(returnValue(4));
                for (int i = 0; i <= 2; i++) {
                    atLeast(1).of(machine4).readUnsigned8(i);
                    will(returnValue(call_vs2Mem.readUnsigned8(i)));
                }
                atLeast(1).of(machine4).readUnsigned16(3);
                will(returnValue(call_vs2Mem.readUnsigned16(3)));
                for (int i = 5; i <= 9; i++) {
                    atLeast(1).of(machine4).readUnsigned8(i);
                    will(returnValue(call_vs2Mem.readUnsigned8(i)));
                }
                one(machine4).getVariable((char) 160);
                will(returnValue((char) 0x4711));
                one(machine4).getVariable((char) 1);
                will(returnValue((char) 0x4712));
            }
        });
        AbstractInstruction info = (AbstractInstruction) decoder4.decodeInstruction(0);
        assertEquals("CALL_VS2 $3bf7, G90[$4711], $10, $20, L00[$4712] -> (SP)", info.toString());
        assertEquals(10, info.getLength());
    }

    @Test
    public void testDecodeSaveUndo() {
        final Memory save_undoMem = new DefaultMemory(save_undo);
        final Machine machine5 = context.mock(Machine.class);
        InstructionDecoder decoder5 = new InstructionDecoder();
        decoder5.initialize(machine5);
        context.checking(new Expectations() {

            {
                atLeast(1).of(machine5).getVersion();
                will(returnValue(5));
                for (int i = 0; i < 4; i++) {
                    atLeast(1).of(machine5).readUnsigned8(i);
                    will(returnValue(save_undoMem.readUnsigned8(i)));
                }
            }
        });
        AbstractInstruction info = (AbstractInstruction) decoder5.decodeInstruction(0);
        assertEquals("SAVE_UNDO  -> (SP)", info.toString());
        assertEquals(4, info.getLength());
    }
}
