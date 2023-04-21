package machine;

import static arcadeflex.libc.*;
import static arcadeflex.osdepend.*;
import static mame.mame.*;
import static mame.driverH.*;
import static mame.osdependH.*;
import static M6502.M6502H.*;

public class mystston {

    static int coin;

    public static InterruptPtr mystston_interrupt = new InterruptPtr() {

        public int handler() {
            if (osd_key_pressed(OSD_KEY_3)) {
                if (coin == 0) {
                    coin = 1;
                    return INT_NMI;
                }
            } else coin = 0;
            return INT_IRQ;
        }
    };
}
