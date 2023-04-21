package machine;

import static Z80.Z80H.*;
import static Z80.Z80.*;
import static mame.driverH.*;

public class invaders {

    static int shift_data1, shift_data2, shift_amount;

    public static ReadHandlerPtr invaders_shift_data_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            return ((((shift_data1 << 8) | shift_data2) << shift_amount) >> 8) & 0xff;
        }
    };

    public static WriteHandlerPtr invaders_shift_amount_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            shift_amount = data;
        }
    };

    public static WriteHandlerPtr invaders_shift_data_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            shift_data2 = shift_data1;
            shift_data1 = data;
        }
    };

    static int count;

    public static InterruptPtr invaders_interrupt = new InterruptPtr() {

        public int handler() {
            count++;
            if ((count & 1) != 0) return 0x00cf; else {
                Z80_Regs R = new Z80_Regs();
                Z80_GetRegs(R);
                R.IFF2 = 1;
                Z80_SetRegs(R);
                return 0x00d7;
            }
        }
    };
}
