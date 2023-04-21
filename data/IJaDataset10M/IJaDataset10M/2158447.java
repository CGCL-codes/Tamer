package machine;

import static arcadeflex.osdepend.*;
import static mame.cpuintrf.*;
import static mame.osdependH.*;
import static mame.driverH.*;
import static vidhrdw.gottlieb.*;
import static mame.inptport.*;

public class gottlieb {

    static int test;

    static int test_switch;

    static int joystick2;

    static int joystick3;

    static int gottlieb_buttons() {
        int res = readinputport(1);
        if (osd_key_pressed(OSD_KEY_F1)) {
            while (osd_key_pressed(OSD_KEY_F1)) ;
            test ^= 1;
        }
        if (test != 0) res &= ~test_switch;
        return res;
    }

    public static ReadHandlerPtr qbert_IN1_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            test_switch = 0x40;
            return gottlieb_buttons();
        }
    };

    public static ReadHandlerPtr mplanets_IN1_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            test_switch = 0x80;
            return gottlieb_buttons();
        }
    };

    public static ReadHandlerPtr reactor_IN1_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            test_switch = 0x02;
            return gottlieb_buttons();
        }
    };

    public static ReadHandlerPtr krull_IN1_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            test_switch = 0x01;
            return gottlieb_buttons();
        }
    };

    public static ReadHandlerPtr stooges_IN1_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            test_switch = 0x01;
            return gottlieb_buttons();
        }
    };

    public static ReadHandlerPtr stooges_joysticks = new ReadHandlerPtr() {

        public int handler(int offset) {
            if (joystick2 != 0) return readinputport(5); else if (joystick3 != 0) return readinputport(6); else return readinputport(4);
        }
    };

    public static WriteHandlerPtr gottlieb_output = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            joystick2 = data & 0x20;
            joystick3 = data & 0x40;
            gottlieb_video_outputs(data);
        }
    };

    static int countdown = 0;

    public static ReadHandlerPtr mplanets_dial_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int res = 0;
            int speed = 2;
            int trak;
            if (countdown == 0) {
                countdown = 4;
                if (osd_key_pressed(OSD_KEY_Z)) res = -speed; else if (osd_key_pressed(OSD_KEY_X)) res = speed;
            }
            countdown--;
            return res;
        }
    };

    public static ReadHandlerPtr reactor_tb_H_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int res = 0x00;
            int speed = 2;
            int trak;
            if (osd_key_pressed(OSD_KEY_LEFT)) res = -speed; else if (osd_key_pressed(OSD_KEY_RIGHT)) res = speed;
            trak = input_trak_0_r.handler(0);
            if (trak == NO_TRAK) {
                return res;
            } else {
                return trak;
            }
        }
    };

    public static ReadHandlerPtr reactor_tb_V_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int res = 0x00;
            int speed = 2;
            int trak;
            if (osd_key_pressed(OSD_KEY_UP)) res = -speed; else if (osd_key_pressed(OSD_KEY_DOWN)) res = speed;
            trak = input_trak_1_r.handler(0);
            if (trak == NO_TRAK) {
                return res;
            } else {
                return trak;
            }
        }
    };

    public static ConversionPtr gottlieb_trakball = new ConversionPtr() {

        public int handler(int data) {
            if (data < -127) {
                return (-127);
            }
            if (data > 127) {
                return (127);
            }
            return (data);
        }
    };
}
