package machine;

import static arcadeflex.osdepend.*;
import static Z80.Z80H.*;
import static Z80.Z80.*;
import static mame.driverH.*;
import static mame.osdependH.*;

public class carnival {

    static int coin;

    public static InterruptPtr carnival_interrupt = new InterruptPtr() {

        public int handler() {
            if (osd_key_pressed(OSD_KEY_3)) {
                if (coin == 0) {
                    coin = 1;
                    Z80_Reset();
                }
            } else coin = 0;
            return Z80_IGNORE_INT;
        }
    };
}
