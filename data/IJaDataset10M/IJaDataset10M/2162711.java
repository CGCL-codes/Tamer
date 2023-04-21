package jdos.cpu.core_normal;

import jdos.cpu.*;
import jdos.hardware.IO;
import jdos.hardware.Memory;
import jdos.hardware.Pic;
import jdos.misc.Log;
import jdos.types.LogSeverities;
import jdos.types.LogTypes;
import jdos.util.IntRef;

public class Prefix_66 extends Prefix_0f {

    private static final IntRef int_ref_1 = new IntRef(0);

    static {
        ops[0x201] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = ADDD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, ADDD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x203] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = ADDD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = ADDD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x205] = new OP() {

            public final int call() {
                reg_eax.dword = ADDD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x206] = new OP() {

            public final int call() {
                CPU.CPU_Push32(CPU.Segs_ESval);
                return HANDLED;
            }
        };
        ops[0x207] = new OP() {

            public final int call() {
                if (CPU.CPU_PopSegES(true)) return RUNEXCEPTION();
                return HANDLED;
            }
        };
        ops[0x209] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = ORD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, ORD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x20b] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = ORD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = ORD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x20d] = new OP() {

            public final int call() {
                reg_eax.dword = ORD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x20e] = new OP() {

            public final int call() {
                CPU.CPU_Push32(CPU.Segs_CSval);
                return HANDLED;
            }
        };
        ops[0x211] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = ADCD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, ADCD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x213] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = ADCD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = ADCD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x215] = new OP() {

            public final int call() {
                reg_eax.dword = ADCD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x216] = new OP() {

            public final int call() {
                CPU.CPU_Push32(CPU.Segs_SSval);
                return HANDLED;
            }
        };
        ops[0x217] = new OP() {

            public final int call() {
                if (CPU.CPU_PopSegSS(true)) return RUNEXCEPTION();
                CPU.CPU_Cycles++;
                return HANDLED;
            }
        };
        ops[0x219] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = SBBD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, SBBD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x21b] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = SBBD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = SBBD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x21d] = new OP() {

            public final int call() {
                reg_eax.dword = SBBD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x21e] = new OP() {

            public final int call() {
                CPU.CPU_Push32(CPU.Segs_DSval);
                return HANDLED;
            }
        };
        ops[0x21f] = new OP() {

            public final int call() {
                if (CPU.CPU_PopSegDS(true)) return RUNEXCEPTION();
                return HANDLED;
            }
        };
        ops[0x221] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = ANDD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, ANDD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x223] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = ANDD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = ANDD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x225] = new OP() {

            public final int call() {
                reg_eax.dword = ANDD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x229] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = SUBD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, SUBD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x22b] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = SUBD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = SUBD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x22d] = new OP() {

            public final int call() {
                reg_eax.dword = SUBD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x231] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    r.dword = XORD(Modrm.Getrd[rm].dword, r.dword);
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, XORD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
        };
        ops[0x233] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                Reg r = Modrm.Getrd[rm];
                if (rm >= 0xc0) {
                    r.dword = XORD(Modrm.GetEArd[rm].dword, r.dword);
                } else {
                    r.dword = XORD(Memory.mem_readd(getEaa(rm)), r.dword);
                }
                return HANDLED;
            }
        };
        ops[0x235] = new OP() {

            public final int call() {
                reg_eax.dword = XORD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x239] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    CMPD(Modrm.Getrd[rm].dword, Modrm.GetEArd[rm].dword);
                } else {
                    int eaa = getEaa(rm);
                    CMPD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa));
                }
                return HANDLED;
            }
        };
        ops[0x23b] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    CMPD(Modrm.GetEArd[rm].dword, Modrm.Getrd[rm].dword);
                } else {
                    CMPD(Memory.mem_readd(getEaa(rm)), Modrm.Getrd[rm].dword);
                }
                return HANDLED;
            }
        };
        ops[0x23d] = new OP() {

            public final int call() {
                CMPD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x240] = new OP() {

            public final int call() {
                reg_eax.dword = Instructions.INCD(reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x241] = new OP() {

            public final int call() {
                reg_ecx.dword = Instructions.INCD(reg_ecx.dword);
                return HANDLED;
            }
        };
        ops[0x242] = new OP() {

            public final int call() {
                reg_edx.dword = Instructions.INCD(reg_edx.dword);
                return HANDLED;
            }
        };
        ops[0x243] = new OP() {

            public final int call() {
                reg_ebx.dword = Instructions.INCD(reg_ebx.dword);
                return HANDLED;
            }
        };
        ops[0x244] = new OP() {

            public final int call() {
                reg_esp.dword = Instructions.INCD(reg_esp.dword);
                return HANDLED;
            }
        };
        ops[0x245] = new OP() {

            public final int call() {
                reg_ebp.dword = Instructions.INCD(reg_ebp.dword);
                return HANDLED;
            }
        };
        ops[0x246] = new OP() {

            public final int call() {
                reg_esi.dword = Instructions.INCD(reg_esi.dword);
                return HANDLED;
            }
        };
        ops[0x247] = new OP() {

            public final int call() {
                reg_edi.dword = Instructions.INCD(reg_edi.dword);
                return HANDLED;
            }
        };
        ops[0x248] = new OP() {

            public final int call() {
                reg_eax.dword = Instructions.DECD(reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x249] = new OP() {

            public final int call() {
                reg_ecx.dword = Instructions.DECD(reg_ecx.dword);
                return HANDLED;
            }
        };
        ops[0x24a] = new OP() {

            public final int call() {
                reg_edx.dword = Instructions.DECD(reg_edx.dword);
                return HANDLED;
            }
        };
        ops[0x24b] = new OP() {

            public final int call() {
                reg_ebx.dword = Instructions.DECD(reg_ebx.dword);
                return HANDLED;
            }
        };
        ops[0x24c] = new OP() {

            public final int call() {
                reg_esp.dword = Instructions.DECD(reg_esp.dword);
                return HANDLED;
            }
        };
        ops[0x24d] = new OP() {

            public final int call() {
                reg_ebp.dword = Instructions.DECD(reg_ebp.dword);
                return HANDLED;
            }
        };
        ops[0x24e] = new OP() {

            public final int call() {
                reg_esi.dword = Instructions.DECD(reg_esi.dword);
                return HANDLED;
            }
        };
        ops[0x24f] = new OP() {

            public final int call() {
                reg_edi.dword = Instructions.DECD(reg_edi.dword);
                return HANDLED;
            }
        };
        ops[0x250] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x251] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_ecx.dword);
                return HANDLED;
            }
        };
        ops[0x252] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_edx.dword);
                return HANDLED;
            }
        };
        ops[0x253] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_ebx.dword);
                return HANDLED;
            }
        };
        ops[0x254] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_esp.dword);
                return HANDLED;
            }
        };
        ops[0x255] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_ebp.dword);
                return HANDLED;
            }
        };
        ops[0x256] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_esi.dword);
                return HANDLED;
            }
        };
        ops[0x257] = new OP() {

            public final int call() {
                CPU.CPU_Push32(reg_edi.dword);
                return HANDLED;
            }
        };
        ops[0x258] = new OP() {

            public final int call() {
                reg_eax.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x259] = new OP() {

            public final int call() {
                reg_ecx.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25a] = new OP() {

            public final int call() {
                reg_edx.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25b] = new OP() {

            public final int call() {
                reg_ebx.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25c] = new OP() {

            public final int call() {
                reg_esp.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25d] = new OP() {

            public final int call() {
                reg_ebp.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25e] = new OP() {

            public final int call() {
                reg_esi.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x25f] = new OP() {

            public final int call() {
                reg_edi.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x260] = new OP() {

            public final int call() {
                int tmpesp = reg_esp.dword;
                int esp = reg_esp.dword;
                esp = CPU.CPU_Push32(esp, reg_eax.dword);
                esp = CPU.CPU_Push32(esp, reg_ecx.dword);
                esp = CPU.CPU_Push32(esp, reg_edx.dword);
                esp = CPU.CPU_Push32(esp, reg_ebx.dword);
                esp = CPU.CPU_Push32(esp, tmpesp);
                esp = CPU.CPU_Push32(esp, reg_ebp.dword);
                esp = CPU.CPU_Push32(esp, reg_esi.dword);
                esp = CPU.CPU_Push32(esp, reg_edi.dword);
                reg_esp.word(esp);
                return HANDLED;
            }
        };
        ops[0x261] = new OP() {

            public final int call() {
                reg_edi.dword = CPU.CPU_Pop32();
                reg_esi.dword = CPU.CPU_Pop32();
                reg_ebp.dword = CPU.CPU_Pop32();
                CPU.CPU_Pop32();
                reg_ebx.dword = CPU.CPU_Pop32();
                reg_edx.dword = CPU.CPU_Pop32();
                reg_ecx.dword = CPU.CPU_Pop32();
                reg_eax.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x262] = new OP() {

            public final int call() {
                int bound_min, bound_max;
                short rm = Fetchb();
                int eaa = getEaa(rm);
                bound_min = Memory.mem_readd(eaa);
                bound_max = Memory.mem_readd(eaa + 4);
                int rmrd = Modrm.Getrd[rm].dword;
                if (rmrd < bound_min || rmrd > bound_max) {
                    return EXCEPTION(5);
                }
                return HANDLED;
            }
        };
        ops[0x263] = new OP() {

            public final int call() {
                if (((CPU.cpu.pmode) && (CPU_Regs.flags & CPU_Regs.VM) != 0) || (!CPU.cpu.pmode)) return ILLEGAL_OPCODE;
                short rm = Fetchb();
                if (rm >= 0xc0) {
                    int_ref_1.value = Modrm.GetEArd[rm].dword;
                    CPU.CPU_ARPL(int_ref_1, Modrm.Getrw[rm].word());
                    Modrm.GetEArd[rm].dword = int_ref_1.value;
                } else {
                    int eaa = getEaa(rm);
                    int_ref_1.value = Memory.mem_readw(eaa);
                    CPU.CPU_ARPL(int_ref_1, Modrm.Getrw[rm].word());
                    Memory.mem_writed(eaa, int_ref_1.value);
                }
                return HANDLED;
            }
        };
        ops[0x268] = new OP() {

            public final int call() {
                CPU.CPU_Push32(Fetchd());
                return HANDLED;
            }
        };
        ops[0x269] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    int op3 = Fetchds();
                    Modrm.Getrd[rm].dword = DIMULD(Modrm.GetEArd[rm].dword, op3);
                } else {
                    int eaa = getEaa(rm);
                    int op3 = Fetchds();
                    Modrm.Getrd[rm].dword = DIMULD(Memory.mem_readd(eaa), op3);
                }
                return HANDLED;
            }
        };
        ops[0x26a] = new OP() {

            public final int call() {
                CPU.CPU_Push32(Fetchbs());
                return HANDLED;
            }
        };
        ops[0x26b] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    int op3 = Fetchbs();
                    Modrm.Getrd[rm].dword = DIMULD(Modrm.GetEArd[rm].dword, op3);
                } else {
                    int eaa = getEaa(rm);
                    int op3 = Fetchbs();
                    Modrm.Getrd[rm].dword = DIMULD(Memory.mem_readd(eaa), op3);
                }
                return HANDLED;
            }
        };
        ops[0x26d] = new OP() {

            public final int call() {
                if (CPU.CPU_IO_Exception(reg_edx.word(), 4)) return RUNEXCEPTION();
                DoString(R_INSD);
                return HANDLED;
            }
        };
        ops[0x26f] = new OP() {

            public final int call() {
                if (CPU.CPU_IO_Exception(reg_edx.word(), 4)) return RUNEXCEPTION();
                DoString(R_OUTSD);
                return HANDLED;
            }
        };
        ops[0x270] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_O());
                return CONTINUE;
            }
        };
        ops[0x271] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NO());
                return CONTINUE;
            }
        };
        ops[0x272] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_B());
                return CONTINUE;
            }
        };
        ops[0x273] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NB());
                return CONTINUE;
            }
        };
        ops[0x274] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_Z());
                return CONTINUE;
            }
        };
        ops[0x275] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NZ());
                return CONTINUE;
            }
        };
        ops[0x276] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_BE());
                return CONTINUE;
            }
        };
        ops[0x277] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NBE());
                return CONTINUE;
            }
        };
        ops[0x278] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_S());
                return CONTINUE;
            }
        };
        ops[0x279] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NS());
                return CONTINUE;
            }
        };
        ops[0x27a] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_P());
                return CONTINUE;
            }
        };
        ops[0x27b] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NP());
                return CONTINUE;
            }
        };
        ops[0x27c] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_L());
                return CONTINUE;
            }
        };
        ops[0x27d] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NL());
                return CONTINUE;
            }
        };
        ops[0x27e] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_LE());
                return CONTINUE;
            }
        };
        ops[0x27f] = new OP() {

            public final int call() {
                JumpCond32_b(Flags.TFLG_NLE());
                return CONTINUE;
            }
        };
        ops[0x281] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                if (rm >= 0xc0) {
                    Reg r = Modrm.GetEArd[rm];
                    int id = Fetchd();
                    switch(which) {
                        case 0x00:
                            r.dword = ADDD(id, r.dword);
                            break;
                        case 0x01:
                            r.dword = ORD(id, r.dword);
                            break;
                        case 0x02:
                            r.dword = ADCD(id, r.dword);
                            break;
                        case 0x03:
                            r.dword = SBBD(id, r.dword);
                            break;
                        case 0x04:
                            r.dword = ANDD(id, r.dword);
                            break;
                        case 0x05:
                            r.dword = SUBD(id, r.dword);
                            break;
                        case 0x06:
                            r.dword = XORD(id, r.dword);
                            break;
                        case 0x07:
                            CMPD(id, r.dword);
                            break;
                    }
                } else {
                    int eaa = getEaa(rm);
                    int id = Fetchd();
                    switch(which) {
                        case 0x00:
                            Memory.mem_writed(eaa, ADDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x01:
                            Memory.mem_writed(eaa, ORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x02:
                            Memory.mem_writed(eaa, ADCD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x03:
                            Memory.mem_writed(eaa, SBBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x04:
                            Memory.mem_writed(eaa, ANDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x05:
                            Memory.mem_writed(eaa, SUBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x06:
                            Memory.mem_writed(eaa, XORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x07:
                            CMPD(id, Memory.mem_readd(eaa));
                            break;
                    }
                }
                return HANDLED;
            }
        };
        ops[0x283] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                if (rm >= 0xc0) {
                    int id = Fetchbs();
                    Reg r = Modrm.GetEArd[rm];
                    switch(which) {
                        case 0x00:
                            r.dword = ADDD(id, r.dword);
                            break;
                        case 0x01:
                            r.dword = ORD(id, r.dword);
                            break;
                        case 0x02:
                            r.dword = ADCD(id, r.dword);
                            break;
                        case 0x03:
                            r.dword = SBBD(id, r.dword);
                            break;
                        case 0x04:
                            r.dword = ANDD(id, r.dword);
                            break;
                        case 0x05:
                            r.dword = SUBD(id, r.dword);
                            break;
                        case 0x06:
                            r.dword = XORD(id, r.dword);
                            break;
                        case 0x07:
                            CMPD(id, r.dword);
                            break;
                    }
                } else {
                    int eaa = getEaa(rm);
                    int id = Fetchbs();
                    switch(which) {
                        case 0x00:
                            Memory.mem_writed(eaa, ADDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x01:
                            Memory.mem_writed(eaa, ORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x02:
                            Memory.mem_writed(eaa, ADCD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x03:
                            Memory.mem_writed(eaa, SBBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x04:
                            Memory.mem_writed(eaa, ANDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x05:
                            Memory.mem_writed(eaa, SUBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x06:
                            Memory.mem_writed(eaa, XORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x07:
                            CMPD(id, Memory.mem_readd(eaa));
                            break;
                    }
                }
                return HANDLED;
            }
        };
        ops[0x285] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    TESTD(Modrm.Getrd[rm].dword, Modrm.GetEArd[rm].dword);
                } else {
                    int eaa = getEaa(rm);
                    TESTD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa));
                }
                return HANDLED;
            }
        };
        ops[0x287] = new OP() {

            public final int call() {
                short rm = Fetchb();
                Reg rd = Modrm.Getrd[rm];
                int oldrmrd = rd.dword;
                if (rm >= 0xc0) {
                    Reg eard = Modrm.GetEArd[rm];
                    rd.dword = eard.dword;
                    eard.dword = oldrmrd;
                } else {
                    int eaa = getEaa(rm);
                    int val = Memory.mem_readd(eaa);
                    Memory.mem_writed(eaa, oldrmrd);
                    rd.dword = val;
                }
                return HANDLED;
            }
        };
        ops[0x289] = new OP() {

            public final int call() {
                short rm = Fetchb();
                if (rm >= 0xc0) {
                    Modrm.GetEArd[rm].dword = Modrm.Getrd[rm].dword;
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, Modrm.Getrd[rm].dword);
                }
                return HANDLED;
            }
        };
        ops[0x28b] = new OP() {

            public final int call() {
                short rm = Fetchb();
                if (rm >= 0xc0) {
                    Modrm.Getrd[rm].dword = Modrm.GetEArd[rm].dword;
                } else {
                    int eaa = getEaa(rm);
                    Modrm.Getrd[rm].dword = Memory.mem_readd(eaa);
                }
                return HANDLED;
            }
        };
        ops[0x28c] = new OP() {

            public final int call() {
                short rm = Fetchb();
                int val;
                int which = (rm >> 3) & 7;
                switch(which) {
                    case 0x00:
                        val = CPU.Segs_ESval;
                        break;
                    case 0x01:
                        val = CPU.Segs_CSval;
                        break;
                    case 0x02:
                        val = CPU.Segs_SSval;
                        break;
                    case 0x03:
                        val = CPU.Segs_DSval;
                        break;
                    case 0x04:
                        val = CPU.Segs_FSval;
                        break;
                    case 0x05:
                        val = CPU.Segs_GSval;
                        break;
                    default:
                        Log.log(LogTypes.LOG_CPU, LogSeverities.LOG_ERROR, "CPU:8c:Illegal RM Byte");
                        return ILLEGAL_OPCODE;
                }
                if (rm >= 0xc0) {
                    Modrm.GetEArd[rm].dword = val;
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writew(eaa, val);
                }
                return HANDLED;
            }
        };
        ops[0x28d] = new OP() {

            public final int call() {
                short rm = Fetchb();
                base_ds = base_ss = 0;
                if (TEST_PREFIX_ADDR() != 0) {
                    Modrm.Getrd[rm].dword = getEaa32(rm);
                } else {
                    Modrm.Getrd[rm].dword = getEaa16(rm);
                }
                return HANDLED;
            }
        };
        ops[0x28f] = new OP() {

            public final int call() {
                int val = CPU.CPU_Pop32();
                short rm = Fetchb();
                if (rm >= 0xc0) {
                    Modrm.GetEArd[rm].dword = val;
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, val);
                }
                return HANDLED;
            }
        };
        ops[0x291] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_ecx.dword;
                reg_ecx.dword = temp;
                return HANDLED;
            }
        };
        ops[0x292] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_edx.dword;
                reg_edx.dword = temp;
                return HANDLED;
            }
        };
        ops[0x293] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_ebx.dword;
                reg_ebx.dword = temp;
                return HANDLED;
            }
        };
        ops[0x294] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_esp.dword;
                reg_esp.dword = temp;
                return HANDLED;
            }
        };
        ops[0x295] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_ebp.dword;
                reg_ebp.dword = temp;
                return HANDLED;
            }
        };
        ops[0x296] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_esi.dword;
                reg_esi.dword = temp;
                return HANDLED;
            }
        };
        ops[0x297] = new OP() {

            public final int call() {
                int temp = reg_eax.dword;
                reg_eax.dword = reg_edi.dword;
                reg_edi.dword = temp;
                return HANDLED;
            }
        };
        ops[0x298] = new OP() {

            public final int call() {
                reg_eax.dword = (short) reg_eax.word();
                return HANDLED;
            }
        };
        ops[0x299] = new OP() {

            public final int call() {
                if ((reg_eax.dword & 0x80000000) != 0) reg_edx.dword = 0xffffffff; else reg_edx.dword = 0;
                return HANDLED;
            }
        };
        ops[0x29a] = new OP() {

            public final int call() {
                int newip = Fetchd();
                int newcs = Fetchw();
                FillFlags();
                CPU.CPU_CALL(true, newcs, newip, GETIP());
                if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                    CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                    return CBRET_NONE;
                }
                return CONTINUE;
            }
        };
        ops[0x29c] = new OP() {

            public final int call() {
                if (CPU.CPU_PUSHF(true)) return RUNEXCEPTION();
                return HANDLED;
            }
        };
        ops[0x29d] = new OP() {

            public final int call() {
                if (CPU.CPU_POPF(true)) return RUNEXCEPTION();
                if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                    CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                    return DECODE_END;
                }
                if (CPU_PIC_CHECK) if (GETFLAG(IF) != 0 && Pic.PIC_IRQCheck != 0) return DECODE_END;
                return HANDLED;
            }
        };
        ops[0x2a1] = new OP() {

            public final int call() {
                reg_eax.dword = Memory.mem_readd(GetEADirect());
                return HANDLED;
            }
        };
        ops[0x2a3] = new OP() {

            public final int call() {
                Memory.mem_writed(GetEADirect(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x2a5] = new OP() {

            public final int call() {
                DoString(R_MOVSD);
                return HANDLED;
            }
        };
        ops[0x2a7] = new OP() {

            public final int call() {
                DoString(R_CMPSD);
                return HANDLED;
            }
        };
        ops[0x2a9] = new OP() {

            public final int call() {
                TESTD(Fetchd(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x2ab] = new OP() {

            public final int call() {
                DoString(R_STOSD);
                return HANDLED;
            }
        };
        ops[0x2ad] = new OP() {

            public final int call() {
                DoString(R_LODSD);
                return HANDLED;
            }
        };
        ops[0x2af] = new OP() {

            public final int call() {
                DoString(R_SCASD);
                return HANDLED;
            }
        };
        ops[0x2b8] = new OP() {

            public final int call() {
                reg_eax.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2b9] = new OP() {

            public final int call() {
                reg_ecx.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2ba] = new OP() {

            public final int call() {
                reg_edx.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2bb] = new OP() {

            public final int call() {
                reg_ebx.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2bc] = new OP() {

            public final int call() {
                reg_esp.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2bd] = new OP() {

            public final int call() {
                reg_ebp.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2be] = new OP() {

            public final int call() {
                reg_esi.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2bf] = new OP() {

            public final int call() {
                reg_edi.dword = Fetchd();
                return HANDLED;
            }
        };
        ops[0x2c1] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                if (rm >= 0xc0) {
                    int blah = Fetchb();
                    int val = blah & 0x1f;
                    if (val == 0) return HANDLED;
                    Reg r = Modrm.GetEArd[rm];
                    switch(which) {
                        case 0x00:
                            r.dword = ROLD(val, r.dword);
                            break;
                        case 0x01:
                            r.dword = RORD(val, r.dword);
                            break;
                        case 0x02:
                            r.dword = RCLD(val, r.dword);
                            break;
                        case 0x03:
                            r.dword = RCRD(val, r.dword);
                            break;
                        case 0x04:
                        case 0x06:
                            r.dword = SHLD(val, r.dword);
                            break;
                        case 0x05:
                            r.dword = SHRD(val, r.dword);
                            break;
                        case 0x07:
                            r.dword = SARD(val, r.dword);
                            break;
                    }
                } else {
                    int eaa = getEaa(rm);
                    int blah = Fetchb();
                    int val = blah & 0x1f;
                    if (val == 0) return HANDLED;
                    switch(which) {
                        case 0x00:
                            Memory.mem_writed(eaa, ROLD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x01:
                            Memory.mem_writed(eaa, RORD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x02:
                            Memory.mem_writed(eaa, RCLD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x03:
                            Memory.mem_writed(eaa, RCRD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x04:
                        case 0x06:
                            Memory.mem_writed(eaa, SHLD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x05:
                            Memory.mem_writed(eaa, SHRD(val, Memory.mem_readd(eaa)));
                            break;
                        case 0x07:
                            Memory.mem_writed(eaa, SARD(val, Memory.mem_readd(eaa)));
                            break;
                    }
                }
                return HANDLED;
            }
        };
        ops[0x2c2] = new OP() {

            public final int call() {
                int offset = Fetchw();
                reg_eip = CPU.CPU_Pop32();
                reg_esp.dword += offset;
                return CONTINUE;
            }
        };
        ops[0x2c3] = new OP() {

            public final int call() {
                reg_eip = CPU.CPU_Pop32();
                return CONTINUE;
            }
        };
        ops[0x2c4] = new OP() {

            public final int call() {
                short rm = Fetchb();
                if (rm >= 0xc0) return ILLEGAL_OPCODE;
                int eaa = getEaa(rm);
                int val = Memory.mem_readd(eaa);
                if (CPU.CPU_SetSegGeneralES(Memory.mem_readw(eaa + 4))) return RUNEXCEPTION();
                Modrm.Getrd[rm].dword = val;
                return HANDLED;
            }
        };
        ops[0x2c5] = new OP() {

            public final int call() {
                short rm = Fetchb();
                if (rm >= 0xc0) return ILLEGAL_OPCODE;
                int eaa = getEaa(rm);
                int val = Memory.mem_readd(eaa);
                if (CPU.CPU_SetSegGeneralDS(Memory.mem_readw(eaa + 4))) return RUNEXCEPTION();
                Modrm.Getrd[rm].dword = val;
                return HANDLED;
            }
        };
        ops[0x2c7] = new OP() {

            public final int call() {
                short rm = Fetchb();
                if (rm >= 0xc0) {
                    Modrm.GetEArd[rm].dword = Fetchd();
                } else {
                    int eaa = getEaa(rm);
                    Memory.mem_writed(eaa, Fetchd());
                }
                return HANDLED;
            }
        };
        ops[0x2c8] = new OP() {

            public final int call() {
                int bytes = Fetchw();
                int level = Fetchb();
                CPU.CPU_ENTER(true, bytes, level);
                return HANDLED;
            }
        };
        ops[0x2c9] = new OP() {

            public final int call() {
                reg_esp.dword &= CPU.cpu.stack.notmask;
                reg_esp.dword |= (reg_ebp.dword & CPU.cpu.stack.mask);
                reg_ebp.dword = CPU.CPU_Pop32();
                return HANDLED;
            }
        };
        ops[0x2ca] = new OP() {

            public final int call() {
                int words = Fetchw();
                FillFlags();
                CPU.CPU_RET(true, words, GETIP());
                return CONTINUE;
            }
        };
        ops[0x2cb] = new OP() {

            public final int call() {
                FillFlags();
                CPU.CPU_RET(true, 0, GETIP());
                return CONTINUE;
            }
        };
        ops[0x2cf] = new OP() {

            public final int call() {
                CPU.CPU_IRET(true, GETIP());
                if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                    CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                    return CBRET_NONE;
                }
                if (CPU_PIC_CHECK) if (GETFLAG(IF) != 0 && Pic.PIC_IRQCheck != 0) return CBRET_NONE;
                return CONTINUE;
            }
        };
        ops[0x2d1] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                GRP2D(rm, 1);
                return HANDLED;
            }
        };
        ops[0x2d3] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                GRP2D(rm, reg_ecx.low());
                return HANDLED;
            }
        };
        ops[0x2e0] = new OP() {

            public final int call() {
                if (TEST_PREFIX_ADDR() != 0) {
                    JumpCond32_b(reg_ecx.dword - 1 != 0 && !get_ZF());
                    reg_ecx.dword--;
                } else {
                    JumpCond32_b(reg_ecx.word() - 1 != 0 && !get_ZF());
                    reg_ecx.word(reg_ecx.word() - 1);
                }
                return CONTINUE;
            }
        };
        ops[0x2e1] = new OP() {

            public final int call() {
                if (TEST_PREFIX_ADDR() != 0) {
                    JumpCond32_b(reg_ecx.dword - 1 != 0 && get_ZF());
                    reg_ecx.dword--;
                } else {
                    JumpCond32_b(reg_ecx.word() - 1 != 0 && get_ZF());
                    reg_ecx.word(reg_ecx.word() - 1);
                }
                return CONTINUE;
            }
        };
        ops[0x2e2] = new OP() {

            public final int call() {
                if (TEST_PREFIX_ADDR() != 0) {
                    JumpCond32_b(reg_ecx.dword - 1 != 0);
                    reg_ecx.dword--;
                } else {
                    JumpCond32_b(reg_ecx.word() - 1 != 0);
                    reg_ecx.word(reg_ecx.word() - 1);
                }
                return CONTINUE;
            }
        };
        ops[0x2e3] = new OP() {

            public final int call() {
                JumpCond32_b((reg_ecx.dword & AddrMaskTable1[prefixes & PREFIX_ADDR]) == 0);
                return CONTINUE;
            }
        };
        ops[0x2e5] = new OP() {

            public final int call() {
                int port = Fetchb();
                if (CPU.CPU_IO_Exception(port, 4)) return RUNEXCEPTION();
                reg_eax.dword = IO.IO_ReadD(port);
                return HANDLED;
            }
        };
        ops[0x2e7] = new OP() {

            public final int call() {
                int port = Fetchb();
                if (CPU.CPU_IO_Exception(port, 4)) return RUNEXCEPTION();
                IO.IO_WriteD(port, reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x2e8] = new OP() {

            public final int call() {
                int addip = Fetchds();
                CPU.CPU_Push32(GETIP());
                SAVEIP();
                reg_eip += addip;
                return CONTINUE;
            }
        };
        ops[0x2e9] = new OP() {

            public final int call() {
                int addip = Fetchds();
                SAVEIP();
                reg_eip += addip;
                return CONTINUE;
            }
        };
        ops[0x2ea] = new OP() {

            public final int call() {
                int newip = Fetchd();
                int newcs = Fetchw();
                FillFlags();
                CPU.CPU_JMP(true, newcs, newip, GETIP());
                if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                    CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                    return CBRET_NONE;
                }
                return CONTINUE;
            }
        };
        ops[0x2eb] = new OP() {

            public final int call() {
                int addip = Fetchbs();
                SAVEIP();
                reg_eip += addip;
                return CONTINUE;
            }
        };
        ops[0x2ed] = new OP() {

            public final int call() {
                reg_eax.dword = IO.IO_ReadD(reg_edx.word());
                return HANDLED;
            }
        };
        ops[0x2ef] = new OP() {

            public final int call() {
                IO.IO_WriteD(reg_edx.word(), reg_eax.dword);
                return HANDLED;
            }
        };
        ops[0x2f7] = new OP() {

            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                switch(which) {
                    case 0x00:
                    case 0x01:
                        {
                            if (rm >= 0xc0) {
                                TESTD(Fetchd(), Modrm.GetEArd[rm].dword);
                            } else {
                                int eaa = getEaa(rm);
                                TESTD(Fetchd(), Memory.mem_readd(eaa));
                            }
                            break;
                        }
                    case 0x02:
                        {
                            if (rm >= 0xc0) {
                                Reg r = Modrm.GetEArd[rm];
                                r.dword = ~r.dword;
                            } else {
                                int eaa = getEaa(rm);
                                Memory.mem_writed(eaa, ~Memory.mem_readd(eaa));
                            }
                            break;
                        }
                    case 0x03:
                        {
                            if (rm >= 0xc0) {
                                Reg r = Modrm.GetEArd[rm];
                                r.dword = Instructions.Negd(r.dword);
                            } else {
                                int eaa = getEaa(rm);
                                Memory.mem_writed(eaa, Instructions.Negd(Memory.mem_readd(eaa)));
                            }
                            break;
                        }
                    case 0x04:
                        if (rm >= 0xc0) {
                            MULD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            MULD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x05:
                        if (rm >= 0xc0) {
                            IMULD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            IMULD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x06:
                        if (rm >= 0xc0) {
                            DIVD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            DIVD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x07:
                        if (rm >= 0xc0) {
                            IDIVD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            IDIVD(Memory.mem_readd(eaa));
                        }
                        break;
                }
                return HANDLED;
            }
        };
        ops[0x2ff] = new OP() {

            public final int call() {
                short rm = Fetchb();
                int which = (rm >> 3) & 7;
                switch(which) {
                    case 0x00:
                        if (rm >= 0xc0) {
                            Reg r = Modrm.GetEArd[rm];
                            r.dword = INCD(r.dword);
                        } else {
                            int eaa = getEaa(rm);
                            Memory.mem_writed(eaa, INCD(Memory.mem_readd(eaa)));
                        }
                        break;
                    case 0x01:
                        if (rm >= 0xc0) {
                            Reg r = Modrm.GetEArd[rm];
                            r.dword = DECD(r.dword);
                        } else {
                            int eaa = getEaa(rm);
                            Memory.mem_writed(eaa, DECD(Memory.mem_readd(eaa)));
                        }
                        break;
                    case 0x02:
                        {
                            int eip;
                            if (rm >= 0xc0) {
                                eip = Modrm.GetEArd[rm].dword;
                            } else {
                                int eaa = getEaa(rm);
                                eip = Memory.mem_readd(eaa);
                            }
                            CPU.CPU_Push32(GETIP());
                            reg_eip = eip;
                            return CONTINUE;
                        }
                    case 0x03:
                        {
                            if (rm >= 0xc0) return ILLEGAL_OPCODE;
                            int eaa = getEaa(rm);
                            int newip = Memory.mem_readd(eaa);
                            int newcs = Memory.mem_readw(eaa + 4);
                            FillFlags();
                            CPU.CPU_CALL(true, newcs, newip, GETIP());
                            if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                                CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                                return CBRET_NONE;
                            }
                            return CONTINUE;
                        }
                    case 0x04:
                        if (rm >= 0xc0) {
                            reg_eip = Modrm.GetEArd[rm].dword;
                        } else {
                            int eaa = getEaa(rm);
                            reg_eip = Memory.mem_readd(eaa);
                        }
                        return CONTINUE;
                    case 0x05:
                        {
                            if (rm >= 0xc0) return ILLEGAL_OPCODE;
                            int eaa = getEaa(rm);
                            int newip = Memory.mem_readd(eaa);
                            int newcs = Memory.mem_readw(eaa + 4);
                            FillFlags();
                            CPU.CPU_JMP(true, newcs, newip, GETIP());
                            if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                                CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                                return CBRET_NONE;
                            }
                            return CONTINUE;
                        }
                    case 0x06:
                        if (rm >= 0xc0) {
                            CPU.CPU_Push32(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            CPU.CPU_Push32(Memory.mem_readd(eaa));
                        }
                        break;
                    default:
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_CPU, LogSeverities.LOG_ERROR, "CPU:66:GRP5:Illegal call " + Integer.toString(which, 16));
                        return ILLEGAL_OPCODE;
                }
                return HANDLED;
            }
        };
    }
}
