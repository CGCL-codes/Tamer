package sh4;

import scd.Maple;
import memory.DMA;
import memory.Memory;

public class Intc {

    private int interruptList[];

    private static final int InterruptsSources = 41;

    private static int IRL0_IRL3_Prio[] = { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 2, 2, 1, 16 };

    public static final int SOURCE_NMI = 0;

    public static final int SOURCE_IRQ0 = 1;

    public static final int SOURCE_IRQ1 = 2;

    public static final int SOURCE_IRQ2 = 3;

    public static final int SOURCE_IRQ3 = 4;

    public static final int SOURCE_IRQ4 = 5;

    public static final int SOURCE_IRQ5 = 6;

    public static final int SOURCE_IRQ6 = 7;

    public static final int SOURCE_IRQ7 = 8;

    public static final int SOURCE_IRQ8 = 9;

    public static final int SOURCE_IRQ9 = 10;

    public static final int SOURCE_IRQ10 = 11;

    public static final int SOURCE_IRQ11 = 12;

    public static final int SOURCE_IRQ12 = 13;

    public static final int SOURCE_IRQ13 = 14;

    public static final int SOURCE_IRQ14 = 15;

    public static final int SOURCE_TMU0_UND = 16;

    public static final int SOURCE_TMU1_UND = 17;

    public static final int SOURCE_TMU2_UND = 18;

    public static final int SOURCE_TMU2_TICPI2 = 19;

    public static final int SOURCE_RTC_ATI = 20;

    public static final int SOURCE_RTC_PRI = 21;

    public static final int SOURCE_RTC_CUI = 22;

    public static final int SOURCE_SCI_ERI = 23;

    public static final int SOURCE_SCI_RXI = 24;

    public static final int SOURCE_SCI_TXI = 25;

    public static final int SOURCE_SCI_TEI = 26;

    public static final int SOURCE_WDT = 27;

    public static final int SOURCE_REF_RCMI = 28;

    public static final int SOURCE_REF_ROVI = 29;

    public static final int SOURCE_HITACHI_UDI = 30;

    public static final int SOURCE_GPIO = 31;

    public static final int SOURCE_DMTE0 = 32;

    public static final int SOURCE_DMTE1 = 33;

    public static final int SOURCE_DMTE2 = 34;

    public static final int SOURCE_DMTE3 = 35;

    public static final int SOURCE_DMAE = 36;

    public static final int SOURCE_SCIF_ERI = 37;

    public static final int SOURCE_SCIF_RXI = 38;

    public static final int SOURCE_SCIF_TXI = 39;

    public static final int SOURCE_SCIF_TEI = 40;

    public static final int EXC_IRQ9 = 0x0320;

    public static final int EXC_IRQA = 0x0340;

    public static final int EXC_IRQB = 0x0360;

    public static final int EXC_IRQC = 0x0380;

    public static final int EXC_IRQD = 0x03a0;

    public static final int ASIC_EVT_PVR_RENDERDONE = 0x0002;

    public static final int ASIC_EVT_PVR_SCANINT1 = 0x0003;

    public static final int ASIC_EVT_PVR_SCANINT2 = 0x0004;

    public static final int ASIC_EVT_PVR_VBLINT = 0x0005;

    public static final int ASIC_EVT_PVR_DMA = 0x0013;

    public static final int ASIC_EVT_PVR_PRIMOUTOFMEM = 0x0202;

    public static final int ASIC_EVT_PVR_MATOUTOFMEM = 0x0203;

    public static final int ASIC_EVT_GD_COMMAND = 0x0100;

    public static final int ASIC_EVT_GD_DMA = 0x000e;

    public static final int ASIC_EVT_MAPLE_DMA = 0x000c;

    public static final int ASIC_EVT_MAPLE_ERROR = 0x000d;

    public static final int ASIC_EVT_SPU_DMA = 0x000f;

    public static final int ASIC_EVT_SPU_IRQ = 0x0101;

    public static final int ASIC_EVT_G2_DMA0 = 0x000f;

    public static final int ASIC_EVT_G2_DMA1 = 0x0010;

    public static final int ASIC_EVT_G2_DMA2 = 0x0011;

    public static final int ASIC_EVT_G2_DMA3 = 0x0012;

    public static final int ASIC_EVT_EXP_8BIT = 0x0102;

    public static final int ASIC_EVT_EXP_PCI = 0x0103;

    private Sh4Context cpu;

    private int[] queuemasks = new int[3];

    public Intc(Sh4Context c) {
        cpu = c;
        interruptList = new int[InterruptsSources];
    }

    public int processSource(int source) {
        int v = 0;
        int ipra = Memory.regmapReadhandle16(MMREG.IPRA);
        int iprc = Memory.regmapReadhandle16(MMREG.IPRC);
        switch(source) {
            case SOURCE_TMU0_UND:
                v = ((ipra) >> 12) & 0xf;
                break;
            case SOURCE_TMU1_UND:
                v = ((ipra) >> 8) & 0xf;
                break;
            case SOURCE_TMU2_UND:
                v = ((ipra) >> 4) & 0xf;
                break;
            case SOURCE_DMTE0:
                v = ((iprc) >> 8) & 0xf;
                break;
            case SOURCE_DMTE1:
                v = ((iprc) >> 8) & 0xf;
                break;
            case SOURCE_DMTE2:
                v = ((iprc) >> 8) & 0xf;
                break;
            case SOURCE_DMTE3:
                v = ((iprc) >> 8) & 0xf;
                break;
            case SOURCE_DMAE:
                v = ((iprc) >> 8) & 0xf;
                break;
        }
        return v;
    }

    public void addInterrupts(int inttoadd) {
        int interrupt = 1 << (inttoadd & 0xff);
        int irq = inttoadd >>> 8;
        if ((queuemasks[irq] & interrupt) == 0) {
            queuemasks[irq] |= interrupt;
            switch(irq) {
                case 0:
                    Memory.ControlRegsDword.put(Memory.ASIC_ACK_A, interrupt);
                    return;
                case 1:
                    Memory.ControlRegsDword.put(Memory.ASIC_ACK_B, interrupt);
                    return;
                case 2:
                    Memory.ControlRegsDword.put(Memory.ASIC_ACK_B, interrupt);
                    return;
            }
        }
    }

    public final void processInterrupt(int source_irq) {
        Memory.regmapWritehandle32(MMREG.INTEVT, source_irq);
        System.out.println("Interrupt processed");
        if ((cpu.SR & Sh4Context.flagsRB) == 0) cpu.switch_gpr_banks();
        cpu.SSR = cpu.SR;
        cpu.SPC = cpu.PC;
        cpu.SGR = cpu.registers[15];
        cpu.SR |= Sh4Context.flagBL;
        cpu.SR |= Sh4Context.flagsRB;
        cpu.SR |= Sh4Context.flagMD;
        cpu.PC = cpu.VBR + 0x600;
    }

    public void acceptInterrupts() {
        if ((cpu.SR & Sh4Context.flagBL) == 0) {
            if (cpu.getIMASK() < IRL0_IRL3_Prio[SOURCE_IRQ9]) {
                if ((queuemasks[0] & Memory.ControlRegsDword.get(Memory.ASIC_IRQ9_A)) != 0 || (queuemasks[1] & Memory.ControlRegsDword.get(Memory.ASIC_IRQ9_B)) != 0 || (queuemasks[2] & Memory.ControlRegsDword.get(Memory.ASIC_IRQ9_C)) != 0) {
                    processInterrupt(EXC_IRQ9);
                    queuemasks[0] &= ~Memory.ControlRegsDword.get(Memory.ASIC_IRQ9_A);
                    return;
                }
            }
            if (cpu.getIMASK() < IRL0_IRL3_Prio[SOURCE_IRQ11]) {
                if ((queuemasks[0] & Memory.ControlRegsDword.get(Memory.ASIC_IRQB_A)) != 0 || (queuemasks[1] & Memory.ControlRegsDword.get(Memory.ASIC_IRQB_B)) != 0 || (queuemasks[2] & Memory.ControlRegsDword.get(Memory.ASIC_IRQB_C)) != 0) {
                    processInterrupt(EXC_IRQB);
                    queuemasks[1] &= ~Memory.ControlRegsDword.get(Memory.ASIC_IRQB_A);
                    return;
                }
            }
            if (cpu.getIMASK() < IRL0_IRL3_Prio[SOURCE_IRQ13]) {
                if ((queuemasks[0] & Memory.ControlRegsDword.get(Memory.ASIC_IRQD_A)) != 0 || (queuemasks[1] & Memory.ControlRegsDword.get(Memory.ASIC_IRQD_B)) != 0 || (queuemasks[2] & Memory.ControlRegsDword.get(Memory.ASIC_IRQD_C)) != 0) {
                    processInterrupt(EXC_IRQD);
                    queuemasks[2] &= ~Memory.ControlRegsDword.get(Memory.ASIC_IRQD_A);
                    return;
                }
            }
        }
        queuemasks[0] &= ~Memory.ControlRegsDword.get(Memory.ASIC_ACK_A);
    }
}
