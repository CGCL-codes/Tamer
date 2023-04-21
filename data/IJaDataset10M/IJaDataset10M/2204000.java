package sndhrdw;

import static arcadeflex.libc.*;
import static sndhrdw.pokeyH.*;

public class pokey {

    static char[] rng = new char[MAXPOKEYS];

    static void Pokey_sound_init(int freq17, int playback_freq, int num_pokeys) {
        int chip;
        for (chip = 0; chip < MAXPOKEYS; chip++) {
            rng[chip] = 1;
        }
    }

    static void Update_pokey_sound(int addr, int val, int chip, int gain) {
        switch(addr & 0x0f) {
            case SKCTL_C:
                if ((val & 0x03) != 0) rng[chip] = 1; else rng[chip] = 0;
                break;
            default:
                System.out.println(addr & 0x0f);
                break;
        }
    }

    static char[] random = new char[MAXPOKEYS];

    static int Read_pokey_regs(int addr, int chip) {
        switch(addr & 0x0f) {
            case RANDOM_C:
                if (rng[chip] == 1) {
                    random[chip] = (char) (rand() & 0xFF);
                }
                return random[chip];
            default:
                System.out.println("POKEY UNKNOWN");
                return 0;
        }
    }
}
