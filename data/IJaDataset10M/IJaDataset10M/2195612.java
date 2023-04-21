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

public class mtrap {

    public static CharPtr mtrap_i2r = new CharPtr();

    public static ReadHandlerPtr mtrap_input_port_2_r = new ReadHandlerPtr() {

        public int handler(int offset) {
            int value;
            value = input_port_2_r.handler(offset) & 0x60;
            value = value | (mtrap_i2r.read() & 0x9F);
            if (exidy_sprite1_xpos.read() == 0x78 && exidy_sprite1_ypos.read() == 0x38) value = value & 0xFB; else value = value | 0x04;
            if ((exidy_sprite1_xpos.read() + 0x10 < exidy_sprite2_xpos.read()) || (exidy_sprite1_xpos.read() > exidy_sprite2_xpos.read() + 0x10) || (exidy_sprite1_ypos.read() + 0x10 < exidy_sprite2_ypos.read()) || (exidy_sprite1_ypos.read() > exidy_sprite2_ypos.read() + 0x10) || ((exidy_sprite_enable.read() & 0x40) != 0)) {
                value = value & 0xEF;
            } else value = value | 0x10;
            return value;
        }
    };

    static MemoryReadAddress readmem[] = { new MemoryReadAddress(0x0000, 0x3fff, MRA_RAM), new MemoryReadAddress(0x4000, 0x43ff, MRA_RAM), new MemoryReadAddress(0x4800, 0x4fff, MRA_RAM), new MemoryReadAddress(0x5100, 0x5100, input_port_0_r), new MemoryReadAddress(0x5101, 0x5101, input_port_1_r), new MemoryReadAddress(0x5103, 0x5103, mtrap_input_port_2_r, mtrap_i2r), new MemoryReadAddress(0x5213, 0x5213, input_port_3_r), new MemoryReadAddress(0x8000, 0xffff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress writemem[] = { new MemoryWriteAddress(0x0000, 0x3fff, MWA_RAM), new MemoryWriteAddress(0x4000, 0x43ff, videoram_w, videoram, videoram_size), new MemoryWriteAddress(0x4800, 0x4fff, exidy_characterram_w, exidy_characterram), new MemoryWriteAddress(0x5000, 0x5000, MWA_RAM, exidy_sprite1_xpos), new MemoryWriteAddress(0x5040, 0x5040, MWA_RAM, exidy_sprite1_ypos), new MemoryWriteAddress(0x5080, 0x5080, MWA_RAM, exidy_sprite2_xpos), new MemoryWriteAddress(0x50C0, 0x50C0, MWA_RAM, exidy_sprite2_ypos), new MemoryWriteAddress(0x5100, 0x5100, MWA_RAM, exidy_sprite_no), new MemoryWriteAddress(0x5101, 0x5101, MWA_RAM, exidy_sprite_enable), new MemoryWriteAddress(0x8000, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static MemoryReadAddress sound_readmem[] = { new MemoryReadAddress(0x0000, 0x57ff, MRA_RAM), new MemoryReadAddress(0x5800, 0x7fff, MRA_ROM), new MemoryReadAddress(0x8000, 0xf7ff, MRA_RAM), new MemoryReadAddress(0xf800, 0xffff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress sound_writemem[] = { new MemoryWriteAddress(0x0000, 0x57ff, MWA_RAM), new MemoryWriteAddress(0x5800, 0x7fff, MWA_ROM), new MemoryWriteAddress(0x8000, 0xf7ff, MWA_RAM), new MemoryWriteAddress(0xf800, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static InputPort input_ports[] = { new InputPort(0xb0, new int[] { OSD_KEY_4, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0xff, new int[] { OSD_KEY_1, OSD_KEY_2, OSD_KEY_RIGHT, OSD_KEY_LEFT, OSD_KEY_CONTROL, OSD_KEY_UP, OSD_KEY_DOWN, OSD_KEY_3 }), new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, OSD_KEY_4, OSD_KEY_3, 0 }), new InputPort(0xff, new int[] { OSD_KEY_X, OSD_KEY_Z, 0, OSD_KEY_C, 0, 0, 0, 0 }), new InputPort(-1) };

    static TrakPort trak_ports[] = { new TrakPort(-1) };

    static KEYSet keys[] = { new KEYSet(1, 5, "MOVE UP"), new KEYSet(1, 3, "MOVE LEFT"), new KEYSet(1, 2, "MOVE RIGHT"), new KEYSet(1, 6, "MOVE DOWN"), new KEYSet(1, 4, "DOG BUTTON"), new KEYSet(3, 1, "RED DOOR BUTTON"), new KEYSet(3, 0, "YELLOW DOOR BUTTON"), new KEYSet(3, 3, "BLUE DOOR BUTTON"), new KEYSet(-1) };

    static DSW dsw[] = { new DSW(0, 0x60, "LIVES", new String[] { "2", "3", "4", "5" }), new DSW(0, 0x06, "BONUS", new String[] { "20000", "30000", "40000", "50000" }), new DSW(0, 0x18, "COIN SELECT", new String[] { "1 COIN 4 CREDITS", "1 COIN 2 CREDITS", "2 COINS 1 CREDIT", "1 COIN 1 CREDIT" }), new DSW(-1) };

    static GfxLayout charlayout = new GfxLayout(8, 8, 256, 1, new int[] { 0 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8 }, 8 * 8);

    static GfxLayout spritelayout = new GfxLayout(16, 16, 16 * 4, 1, new int[] { 0 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 16 * 8 + 0, 16 * 8 + 1, 16 * 8 + 2, 16 * 8 + 3, 16 * 8 + 4, 16 * 8 + 5, 16 * 8 + 6, 16 * 8 + 7 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8, 8 * 8, 9 * 8, 10 * 8, 11 * 8, 12 * 8, 13 * 8, 14 * 8, 15 * 8 }, 8 * 32);

    static GfxDecodeInfo gfxdecodeinfo[] = { new GfxDecodeInfo(0, 0x4800, charlayout, 0, 13 * 2), new GfxDecodeInfo(1, 0x0000, spritelayout, (13 * 2) * 2, 2 * 2), new GfxDecodeInfo(-1) };

    static char palette[] = { 0x00, 0x00, 0x00, 0x80, 0x00, 0x80, 0x80, 0x00, 0x00, 0xf8, 0x64, 0xd8, 0x00, 0x80, 0x00, 0x00, 0x80, 0x80, 0x80, 0x80, 0x00, 0x80, 0x80, 0x80, 0xf8, 0x94, 0x44, 0x00, 0x00, 0xff, 0xff, 0x00, 0x00, 0xff, 0x00, 0xff, 0x00, 0xff, 0x00, 0x00, 0xff, 0xff, 0xff, 0xff, 0x00, 0xff, 0xff, 0xff };

    static final int black = 0;

    static final int darkpurple = 1;

    static final int darkred = 2;

    static final int pink = 3;

    static final int darkgreen = 4;

    static final int darkcyan = 5;

    static final int darkyellow = 6;

    static final int darkwhite = 7;

    static final int orange = 8;

    static final int blue = 9;

    static final int red = 10;

    static final int purple = 11;

    static final int green = 12;

    static final int cyan = 13;

    static final int yellow = 14;

    static final int white = 15;

    static char colortable[] = { black, green, black, green, black, green, black, green, black, yellow, black, yellow, black, yellow, black, yellow, purple, white, purple, white, black, red, black, red, black, blue, black, blue, black, yellow, black, yellow, black, red, black, red, black, red, black, red, black, cyan, black, cyan, black, purple, black, purple, black, red, black, red, black, cyan, black, red, black, purple, black, purple };

    static final int c_text = 0;

    static final int c_path = 1;

    static final int c_dots = 2;

    static final int c_cats = 3;

    static final int c_ibox = 4;

    static final int c_rddr = 5;

    static final int c_bldr = 6;

    static final int c_yldr = 7;

    static final int c_bnus = 8;

    static final int c_bsct = 9;

    static final int c_xmse = 10;

    static final int c_hawk = 11;

    static final int c_dogs = 12;

    static char mtrap_color_lookup[] = { c_bldr, c_bldr, c_bldr, c_bldr, c_ibox, c_ibox, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_hawk, c_hawk, c_hawk, c_hawk, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_path, c_rddr, c_rddr, c_rddr, c_rddr, c_bsct, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_bnus, c_bnus, c_bnus, c_bnus, c_dogs, c_dogs, c_dogs, c_dogs, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_yldr, c_yldr, c_dots, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_text, c_yldr, c_yldr, c_xmse, c_xmse, c_xmse, c_xmse, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats, c_cats };

    public static InitMachinePtr mtrap_init_machine = new InitMachinePtr() {

        public void handler() {
            char RAM[] = Machine.memory_region[0];
            RAM[0xF439] = 0xEA;
            RAM[0xF43A] = 0xEA;
            RAM[0xF43B] = 0xEA;
            exidy_color_lookup.set(mtrap_color_lookup, 0);
        }
    };

    static MachineDriver mtrap_machine_driver = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_M6502, 1000000, 0, readmem, writemem, null, null, interrupt, 1), new MachineCPU(CPU_M6502 | CPU_AUDIO_CPU, 1000000, 2, sound_readmem, sound_writemem, null, null, interrupt, 1) }, 60, 10, mtrap_init_machine, 32 * 8, 32 * 8, new rectangle(0 * 8, 32 * 8 - 1, 0 * 8, 32 * 8 - 1), gfxdecodeinfo, sizeof(palette) / 3, sizeof(colortable), null, VIDEO_TYPE_RASTER | VIDEO_SUPPORTS_DIRTY, null, generic_vh_start, generic_vh_stop, exidy_vh_screenrefresh, null, null, null, null, null);

    /***************************************************************************
	
	  Game driver(s)
	
	***************************************************************************/
    static RomLoadPtr mtrap_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("mtl11a.bin", 0xA000, 0x1000, 0xb4e109f7);
            ROM_LOAD("mtl10a.bin", 0xB000, 0x1000, 0xe890bac6);
            ROM_LOAD("mtl9a.bin", 0xC000, 0x1000, 0x06628e86);
            ROM_LOAD("mtl8a.bin", 0xD000, 0x1000, 0xa12b0c55);
            ROM_LOAD("mtl7a.bin", 0xE000, 0x1000, 0xb5c75a2f);
            ROM_LOAD("mtl6a.bin", 0xF000, 0x1000, 0x2e7f499b);
            ROM_REGION(0x0800);
            ROM_LOAD("mtl11D.bin", 0x0000, 0x0800, 0x389ef2ec);
            ROM_REGION(0x10000);
            ROM_LOAD("mta5a.bin", 0x6800, 0x0800, 0xdc40685a);
            ROM_LOAD("mta6a.bin", 0x7000, 0x0800, 0x250b2f5f);
            ROM_LOAD("mta7a.bin", 0x7800, 0x0800, 0x3ba2700a);
            ROM_LOAD("mta7a.bin", 0xF800, 0x0800, 0x3ba2700a);
            ROM_END();
        }
    };

    static HiscoreLoadPtr hiload = new HiscoreLoadPtr() {

        public int handler() {
            char[] RAM = Machine.memory_region[0];
            if ((memcmp(RAM, 0x0380, new char[] { 0x00, 0x06, 0x0C, 0x12, 0x18 }, 5) == 0) && (memcmp(RAM, 0x03A0, new char[] { 'L', 'W', 'H' }, 3) == 0)) {
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

    public static GameDriver mtrap_driver = new GameDriver("Mouse Trap", "mtrap", "MARC LAFONTAINE\nBRIAN LEVINE\nMIKE BALFOUR", mtrap_machine_driver, mtrap_rom, null, null, null, input_ports, null, trak_ports, dsw, keys, null, palette, colortable, ORIENTATION_DEFAULT, hiload, hisave);
}
