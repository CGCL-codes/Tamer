package jdos.cpu.instructions;

import jdos.cpu.CPU;
import jdos.cpu.CPU_Regs;
import jdos.cpu.Flags;
import jdos.hardware.Memory;

public class testPrefix_66 extends InstructionsTestCase {

    public void testAddEdGd() {
        runRegsd(0x201, 1001, 2, false, 1003, 2002);
        runRegd(0x201, 1, -2, false, -1);
        assertTrue(!Flags.get_CF());
        runRegd(0x201, 0x80000000, 0x80000000, false, 0);
        if (!jdos.cpu.core_dynamic.Compiler.alwayUseFastVersion) assertTrue(Flags.get_CF());
        CPU_Regs.SETFLAGBIT(CPU_Regs.CF, true);
        runRegd(0x201, 0x80000000, 0x80000000, false, 0);
        runRegd(0x201, 0x80000000, 0x00000001, false, 0x80000001);
    }

    public void testAddGdEd() {
        runRegsd(0x203, 1001, 2, true, 1003, 2002);
        runRegd(0x203, 1, -2, true, -1);
        assertTrue(!Flags.get_CF());
        runRegd(0x203, 0x80000000, 0x80000000, true, 0);
        if (!jdos.cpu.core_dynamic.Compiler.alwayUseFastVersion) assertTrue(Flags.get_CF());
        CPU_Regs.SETFLAGBIT(CPU_Regs.CF, true);
        runRegd(0x203, 0x80000000, 0x80000000, true, 0);
        runRegd(0x203, 0x80000000, 0x00000001, true, 0x80000001);
    }

    public void testAddEaxId() {
        newInstruction(0x205);
        CPU_Regs.reg_eax.word(1);
        pushId(2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.word() == 3);
        newInstruction(0x205);
        CPU_Regs.reg_eax.word(1);
        pushId(-2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword == 0xFFFFFFFF);
        assertTrue(!Flags.get_CF());
        newInstruction(0x205);
        CPU_Regs.reg_eax.dword(0x80000001);
        pushId(0x80000000);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword == 1);
        if (!jdos.cpu.core_dynamic.Compiler.alwayUseFastVersion) assertTrue(Flags.get_CF());
        CPU_Regs.SETFLAGBIT(CPU_Regs.CF, true);
        newInstruction(0x205);
        CPU_Regs.reg_eax.dword(0x80000002);
        pushId(0x80000000);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.word() == 2);
    }

    public void testPushES() {
        newInstruction(0x206);
        CPU_Regs.reg_esp.dword(0x100);
        CPU.Segs_ESval = 0xABCD;
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword == 0xFC);
        assertTrue(Memory.host_readd(CPU.Segs_SSphys + CPU_Regs.reg_esp.dword) == 0xABCD);
    }

    public void testPopES() {
        newInstruction(0x207);
        CPU_Regs.reg_esp.dword(0xFC);
        Memory.host_writed(CPU.Segs_SSphys + CPU_Regs.reg_esp.dword, 0x189EF);
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword == 0x100);
        assertTrue(CPU.Segs_ESval == 0x89EF);
    }

    public void testOrEdGd() {
        runRegsd(0x209, 1, 2, false, 3, 1);
        runRegd(0x209, 0, 0, false, 0);
        runRegd(0x209, 0xFFFFFFFF, 0, false, 0xFFFFFFFF);
        runRegd(0x209, 0, 0xFFFFFFFF, false, 0xFFFFFFFF);
        runRegd(0x209, 0xFF00FF00, 0x00FF00FF, false, 0xFFFFFFFF);
    }

    public void testOrGdEd() {
        runRegsd(0x20b, 1, 2, true, 3, 1);
        runRegd(0x20b, 0, 0, true, 0);
        runRegd(0x20b, 0xFFFFFF00, 0, true, 0xFFFFFF00);
        runRegd(0x20b, 0, 0xFFFF00FF, true, 0xFFFF00FF);
        runRegd(0x20b, 0xFF00F0F0, 0x00FF0F0F, true, 0xFFFFFFFF);
    }

    public void testOrEaxId() {
        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword = 1;
        pushId(2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword == 3);
        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword = 0;
        pushId(0);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword == 0);
        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword = 0xF00FF00F;
        pushId(0x0FF00FF0);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword == 0xFFFFFFFF);
    }

    public void testPushCS() {
        newInstruction(0x20E);
        CPU_Regs.reg_esp.dword(0x100);
        CPU.Segs_CSval = 0x1234ABCD;
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword == 0xFC);
        assertTrue(Memory.host_readd(CPU.Segs_SSphys + CPU_Regs.reg_esp.dword) == 0x1234ABCD);
    }
}
