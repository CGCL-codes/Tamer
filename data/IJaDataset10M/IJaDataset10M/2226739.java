package sndhrdw;

import static mame.driverH.*;
import static sndhrdw.sn76496H.*;
import static sndhrdw.sn76496.*;
import static arcadeflex.libc.*;
import static arcadeflex.osdepend.*;
import static mame.cpuintrf.*;
import static sndhrdw.generic.*;

public class tp84 {

    static final int TIMER_RATE = 512;

    public static ReadHandlerPtr tp84_sh_timer_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int clock;
            clock = cpu_gettotalcycles() / TIMER_RATE;
            return clock;
        }
    };

    public static InterruptPtr tp84_sh_interrupt = new InterruptPtr() {

        public int handler() {
            if (pending_commands != 0) return interrupt.handler(); else return ignore_interrupt.handler();
        }
    };

    static final int SND_CLOCK = 4000000;

    static final int CHIPS = 3;

    static final int TONE_LENGTH = 2000;

    static final int TONE_PERIOD = 4;

    static final int NOISE_LENGTH = 10000;

    static final int WAVE_AMPLITUDE = 70;

    static SN76496[] sn = new SN76496[CHIPS];

    static char[] tone;

    static char[] noise;

    public static WriteHandlerPtr tp84_sound1_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            SN76496Write(sn[0], data);
        }
    };

    public static WriteHandlerPtr tp84_sound2_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            SN76496Write(sn[1], data);
        }
    };

    public static WriteHandlerPtr tp84_sound3_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            SN76496Write(sn[2], data);
        }
    };

    public static WriteHandlerPtr tp84_sound4_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            SN76496Write(sn[3], data);
        }
    };

    public static ShStartPtr tp84_sh_start = new ShStartPtr() {

        public int handler() {
            int i, j;
            if ((tone = new char[TONE_LENGTH]) == null) return 1;
            if ((noise = new char[NOISE_LENGTH]) == null) {
                tone = null;
                return 1;
            }
            for (i = 0; i < TONE_LENGTH; i++) tone[i] = (char) (WAVE_AMPLITUDE * (char) (Math.sin(2 * Math.PI * i / TONE_PERIOD)));
            for (i = 0; i < NOISE_LENGTH; i++) noise[i] = (char) ((rand() % (2 * WAVE_AMPLITUDE)) - WAVE_AMPLITUDE);
            for (j = 0; j < CHIPS; j++) {
                sn[j] = new SN76496();
                sn[j].Clock = SND_CLOCK;
                SN76496Reset(sn[j]);
                for (i = 0; i < 3; i++) osd_play_sample(4 * j + i, tone, TONE_LENGTH, TONE_PERIOD * sn[j].Frequency[i], sn[j].Volume[i], 1);
                osd_play_sample(4 * j + 3, noise, NOISE_LENGTH, sn[j].NoiseShiftRate, sn[j].Volume[3], 1);
            }
            return 0;
        }
    };

    public static ShStopPtr tp84_sh_stop = new ShStopPtr() {

        public void handler() {
            noise = null;
            tone = null;
        }
    };

    public static ShUpdatePtr tp84_sh_update = new ShUpdatePtr() {

        public void handler() {
            int i, j;
            if (play_sound == 0) return;
            for (j = 0; j < CHIPS; j++) {
                SN76496Update(sn[j]);
                for (i = 0; i < 3; i++) osd_adjust_sample(4 * j + i, TONE_PERIOD * sn[j].Frequency[i], sn[j].Volume[i]);
                osd_adjust_sample(4 * j + 3, sn[j].NoiseShiftRate, sn[j].Volume[3]);
            }
        }
    };
}
