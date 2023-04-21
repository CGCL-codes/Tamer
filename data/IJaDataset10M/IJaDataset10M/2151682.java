package machine;

import static arcadeflex.libc.*;
import static mame.cpuintrf.*;
import static mame.driverH.*;
import static m6809.M6809H.*;
import static m6809.M6809.*;
import static mame.mame.*;

public class qix {

    public static CharPtr qix_sharedram = new CharPtr();

    static int lastcheck2;

    public static InterruptPtr qix_data_interrupt = new InterruptPtr() {

        public int handler() {
            if (yield_cpu != 0) return INT_NONE; else return INT_IRQ;
        }
    };

    public static ReadHandlerPtr qix_sharedram_r_1 = new ReadHandlerPtr() {

        public int handler(int offset) {
            int pc;
            pc = cpu_getpc();
            if (pc == 0xdd54 || pc == 0xcf16 || pc == 0xdd5a) {
                yield_cpu = 1;
                saved_icount = cpu_geticount();
                cpu_seticount(0);
            } else if (pc == 0xdd4b && RAM[0x8520] != 0) cpu_seticount(0);
            return qix_sharedram.read(offset);
        }
    };

    public static ReadHandlerPtr qix_sharedram_r_2 = new ReadHandlerPtr() {

        public int handler(int offset) {
            if (offset == 0x37d) {
                int count = cpu_geticount();
                if (lastcheck2 < count + 60 && cpu_getpc() == 0xc894) {
                    cpu_seticount(0);
                    lastcheck2 = 0x7fffffff;
                } else lastcheck2 = count;
            }
            return qix_sharedram.read(offset);
        }
    };

    public static WriteHandlerPtr qix_sharedram_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            qix_sharedram.write(offset, data);
        }
    };

    public static WriteHandlerPtr qix_video_firq_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            m6809context ctxt = (m6809context) cpucontext[1];
            ctxt.irq = INT_FIRQ;
        }
    };

    public static WriteHandlerPtr qix_data_firq_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            m6809context ctxt = (m6809context) cpucontext[0];
            ctxt.irq = INT_FIRQ;
        }
    };

    static char hack = 0x00;

    public static ReadHandlerPtr qix_scanline_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            if (hack == 0x00) hack = 0xff; else hack = 0x00;
            return hack;
        }
    };

    public static InitMachinePtr qix_init_machine = new InitMachinePtr() {

        public void handler() {
            m6809_Flags = M6809_FAST_OP | M6809_FAST_S;
            lastcheck2 = 0x7fffffff;
        }
    };
}
