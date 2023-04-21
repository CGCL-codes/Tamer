package drivers;

import static arcadeflex.libc.*;
import static arcadeflex.osdepend.*;
import static mame.commonH.*;
import static mame.cpuintrf.*;
import static mame.driverH.*;
import static mame.mame.*;
import static mame.inptport.*;
import static mame.osdependH.*;
import static vidhrdw.generic.*;
import static vidhrdw.exidy.*;
import static mame.memoryH.*;

public class venture {

    public static CharPtr venture_ir2 = new CharPtr();

    static int lcv0 = 0;

    static int lcv1 = 0;

    static int lcv2 = 0;

    static int lcv3 = 0;

    static void venture_pos_change() {
        int x, y;
        int[] cv = new int[4];
        if ((venture_ir2.read() & 0x80) == 0) {
            x = (236 - exidy_sprite1_xpos.read()) >> 3;
            y = (244 - exidy_sprite1_ypos.read()) >> 3;
            cv[0] = videoram.read((y << 5) + x);
            cv[1] = videoram.read((y << 5) + x + 1);
            cv[2] = videoram.read(((y + 1) << 5) + x);
            cv[3] = videoram.read(((y + 1) << 5) + x + 1);
            if (cv[0] != 0 || cv[1] != 0 || cv[2] != 0 || cv[3] != 0) {
                if ((cv[0] != lcv0) || (cv[1] != lcv1) || (cv[2] != lcv2) || (cv[3] != lcv3)) {
                    venture_ir2.write(0x80);
                    lcv0 = cv[0];
                    lcv1 = cv[1];
                    lcv2 = cv[2];
                    lcv3 = cv[3];
                }
            } else venture_ir2.write(0x00);
        }
        return;
    }

    public static WriteHandlerPtr venture_x_pos_change = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            exidy_sprite1_xpos.write(offset, data);
            venture_pos_change();
            return;
        }
    };

    public static WriteHandlerPtr venture_y_pos_change = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            exidy_sprite1_ypos.write(offset, data);
            venture_pos_change();
            return;
        }
    };

    public static ReadHandlerPtr venture_input_port_2_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int value;
            value = input_port_2_r.handler(offset) & 0x60;
            value = value | (venture_ir2.read(offset) & 0x9F);
            venture_ir2.write(offset, 0x00);
            return value;
        }
    };

    static MemoryReadAddress readmem[] = { new MemoryReadAddress(0x0000, 0x3fff, MRA_RAM), new MemoryReadAddress(0x4000, 0x43ff, MRA_RAM), new MemoryReadAddress(0x4800, 0x4fff, MRA_RAM), new MemoryReadAddress(0x5100, 0x5100, input_port_0_r), new MemoryReadAddress(0x5101, 0x5101, input_port_1_r), new MemoryReadAddress(0x5103, 0x5103, venture_input_port_2_r, venture_ir2), new MemoryReadAddress(0x8000, 0xffff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress writemem[] = { new MemoryWriteAddress(0x0000, 0x3fff, MWA_RAM), new MemoryWriteAddress(0x4000, 0x43ff, videoram_w, videoram, videoram_size), new MemoryWriteAddress(0x4800, 0x4fff, exidy_characterram_w, exidy_characterram), new MemoryWriteAddress(0x5000, 0x5000, venture_x_pos_change, exidy_sprite1_xpos), new MemoryWriteAddress(0x5040, 0x5040, venture_y_pos_change, exidy_sprite1_ypos), new MemoryWriteAddress(0x5080, 0x5080, MWA_RAM, exidy_sprite2_xpos), new MemoryWriteAddress(0x50C0, 0x50C0, MWA_RAM, exidy_sprite2_ypos), new MemoryWriteAddress(0x5100, 0x5100, MWA_RAM, exidy_sprite_no), new MemoryWriteAddress(0x5101, 0x5101, MWA_RAM, exidy_sprite_enable), new MemoryWriteAddress(0x8000, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static MemoryReadAddress sound_readmem[] = { new MemoryReadAddress(0x0000, 0x57ff, MRA_RAM), new MemoryReadAddress(0x5800, 0x7fff, MRA_ROM), new MemoryReadAddress(0x8000, 0xf7ff, MRA_RAM), new MemoryReadAddress(0xf800, 0xffff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress sound_writemem[] = { new MemoryWriteAddress(0x0000, 0x57ff, MWA_RAM), new MemoryWriteAddress(0x5800, 0x7fff, MWA_ROM), new MemoryWriteAddress(0x8000, 0xf7ff, MWA_RAM), new MemoryWriteAddress(0xf800, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static InputPort input_ports[] = { new InputPort(0xb0, new int[] { OSD_KEY_4, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0xff, new int[] { OSD_KEY_1, OSD_KEY_2, OSD_KEY_RIGHT, OSD_KEY_LEFT, OSD_KEY_CONTROL, OSD_KEY_UP, OSD_KEY_DOWN, OSD_KEY_3 }), new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, OSD_KEY_4, OSD_KEY_3, 0 }), new InputPort(-1) };

    static TrakPort trak_ports[] = { new TrakPort(-1) };

    static KEYSet keys[] = { new KEYSet(1, 5, "MOVE UP"), new KEYSet(1, 3, "MOVE LEFT"), new KEYSet(1, 2, "MOVE RIGHT"), new KEYSet(1, 6, "MOVE DOWN"), new KEYSet(1, 4, "FIRE BUTTON"), new KEYSet(-1) };

    static DSW dsw[] = { new DSW(0, 0x60, "LIVES", new String[] { "2", "3", "4", "5" }), new DSW(0, 0x06, "BONUS", new String[] { "20000", "30000", "40000", "50000" }), new DSW(0, 0x18, "COIN SELECT", new String[] { "1 COIN 4 CREDITS", "1 COIN 2 CREDITS", "2 COINS 1 CREDIT", "1 COIN 1 CREDIT" }), new DSW(-1) };

    static GfxLayout charlayout = new GfxLayout(8, 8, 256, 1, new int[] { 0 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8 }, 8 * 8);

    static GfxLayout spritelayout = new GfxLayout(16, 16, 16 * 4, 1, new int[] { 0 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 16 * 8 + 0, 16 * 8 + 1, 16 * 8 + 2, 16 * 8 + 3, 16 * 8 + 4, 16 * 8 + 5, 16 * 8 + 6, 16 * 8 + 7 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8, 8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8, 13 * 8, 14 * 8, 15 * 8 }, 8 * 32);

    static GfxDecodeInfo gfxdecodeinfo[] = { new GfxDecodeInfo(0, 0x4800, charlayout, 0, 6 * 2), new GfxDecodeInfo(1, 0x0000, spritelayout, (6 * 2) * 2, 2 * 2), new GfxDecodeInfo(-1) };

    static char palette[] = { 0x00, 0x00, 0x00, 0x80, 0x00, 0x80, 0x80, 0x00, 0x00, 0xf8, 0x64, 0xd8, 0x00, 0x80, 0x00, 0x00, 0x80, 0x80, 0x80, 0x80, 0x00, 0x80, 0x80, 0x80, 0xf8, 0x94, 0x44, 0x00, 0x00, 0xff, 0xff, 0x00, 0x00, 0xff, 0x00, 0xff, 0x00, 0xff, 0x00, 0x00, 0xff, 0xff, 0xff, 0xff, 0x00, 0xff, 0xff, 0xff };

    static final int black = 0;

    static final int darkpurple = 1;

    static final int darkred = 2;

    static final int pink = 3;

    static final int darkgreen = 4;

    static final int darkcyan = 5;

    static final int darkyellow = 6;

    static final int grey = 7;

    static final int orange = 8;

    static final int blue = 9;

    static final int red = 10;

    static final int purple = 11;

    static final int green = 12;

    static final int cyan = 13;

    static final int yellow = 14;

    static final int white = 15;

    static char colortable[] = { black, white, black, white, black, purple, black, purple, black, cyan, black, cyan, black, green, black, green, black, cyan, black, cyan, black, white, black, white, black, red, black, red, black, yellow, black, yellow };

    static final int c_text = 0;

    static final int c_wall = 1;

    static final int c_gbln = 2;

    static final int c_mstr = 3;

    static final int c_qbox = 4;

    static final int c_door = 5;

    static char venture_color_lookup[] = { c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_gbln, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_wall, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr, c_mstr };

    public static InitMachinePtr venture_init_machine = new InitMachinePtr() {

        public void handler() {
            char RAM[] = Machine.memory_region[0];
            RAM[0x8AF4] = 0xEA;
            RAM[0x8AF5] = 0xEA;
            RAM[0x8AF6] = 0xEA;
            RAM[0x7000] = 0xD8;
            RAM[0x7001] = 0x78;
            RAM[0x7002] = 0x4C;
            RAM[0x7003] = RAM[0xFFFC];
            RAM[0x7004] = RAM[0xFFFD];
            RAM[0xFFFC] = 0x00;
            RAM[0xFFFD] = 0x70;
            exidy_color_lookup.set(venture_color_lookup, 0);
        }
    };

    static MachineDriver venture_machine_driver = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_M6502, 1000000, 0, readmem, writemem, null, null, interrupt, 1), new MachineCPU(CPU_M6502 | CPU_AUDIO_CPU, 1000000, 2, sound_readmem, sound_writemem, null, null, interrupt, 1) }, 60, 10, venture_init_machine, 32 * 8, 32 * 8, new rectangle(0 * 8, 32 * 8 - 1, 0 * 8, 32 * 8 - 1), gfxdecodeinfo, sizeof(palette) / 3, sizeof(colortable), null, VIDEO_TYPE_RASTER | VIDEO_SUPPORTS_DIRTY, null, generic_vh_start, generic_vh_stop, exidy_vh_screenrefresh, null, null, null, null, null);

    /***************************************************************************
	
	  Game driver(s)
	
	***************************************************************************/
    static RomLoadPtr venture_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("13A-CPU", 0x8000, 0x1000, 0x891abe62);
            ROM_LOAD("12A-CPU", 0x9000, 0x1000, 0xac004ea6);
            ROM_LOAD("11A-CPU", 0xA000, 0x1000, 0x225ec9ee);
            ROM_LOAD("10A-CPU", 0xB000, 0x1000, 0x4c8a0c70);
            ROM_LOAD("9A-CPU", 0xC000, 0x1000, 0x4ec5e145);
            ROM_LOAD("8A-CPU", 0xD000, 0x1000, 0x25eac9e2);
            ROM_LOAD("7A-CPU", 0xE000, 0x1000, 0x2173eca5);
            ROM_LOAD("6A-CPU", 0xF000, 0x1000, 0x1714e61c);
            ROM_REGION(0x0800);
            ROM_LOAD("11D-CPU", 0x0000, 0x0800, 0xceb42d02);
            ROM_REGION(0x10000);
            ROM_LOAD("3a-ac", 0x5800, 0x0800, 0x6098790a);
            ROM_LOAD("4a-ac", 0x6000, 0x0800, 0x9bd6ad80);
            ROM_LOAD("5a-ac", 0x6800, 0x0800, 0xee5c9752);
            ROM_LOAD("6a-ac", 0x7000, 0x0800, 0x9559adbb);
            ROM_LOAD("7a-ac", 0x7800, 0x0800, 0x9c5601b0);
            ROM_LOAD("7a-ac", 0xF800, 0x0800, 0x9c5601b0);
            ROM_END();
        }
    };

    static HiscoreLoadPtr hiload = new HiscoreLoadPtr() {

        public int handler() {
            char[] RAM = Machine.memory_region[0];
            if ((memcmp(RAM, 0x0380, new char[] { 0x00, 0x06, 0x0C, 0x12, 0x18 }, 5) == 0) && (memcmp(RAM, 0x03A0, new char[] { 'D', 'J', 'S' }, 3) == 0)) {
                FILE f;
                if ((f = osd_fopen(Machine.gamedrv.name, null, OSD_FILETYPE_HIGHSCORE, 0)) != null) {
                    osd_fread(f, RAM, 0x0380, 5 + 6 * 5);
                    osd_fclose(f);
                }
                return 1;
            } else return 0;
        }
    };

    static HiscoreSavePtr hisave = new HiscoreSavePtr() {

        public void handler() {
            FILE f;
            char[] RAM = Machine.memory_region[0];
            if ((f = osd_fopen(Machine.gamedrv.name, null, OSD_FILETYPE_HIGHSCORE, 1)) != null) {
                osd_fwrite(f, RAM, 0x0380, 5 + 6 * 5);
                osd_fclose(f);
            }
        }
    };

    public static GameDriver venture_driver = new GameDriver("Venture", "venture", "MARC LAFONTAINE\nNICOLA SALMORIA\nBRIAN LEVINE\nMIKE BALFOUR", venture_machine_driver, venture_rom, null, null, null, input_ports, null, trak_ports, dsw, keys, null, palette, colortable, ORIENTATION_DEFAULT, hiload, hisave);
}
