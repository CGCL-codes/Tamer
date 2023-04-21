package drivers;

import static arcadeflex.libc.*;
import static mame.commonH.*;
import static mame.cpuintrf.*;
import static mame.driverH.*;
import static mame.mame.*;
import static mame.inptport.*;
import static mame.osdependH.*;
import static machine.elevator.*;
import static vidhrdw.generic.*;
import static vidhrdw.taito.*;
import static sndhrdw._8910intf.*;
import static sndhrdw.generic.*;
import static sndhrdw.elevator.*;

public class elevator {

    static MemoryReadAddress readmem[] = { new MemoryReadAddress(0x8000, 0x87ff, MRA_RAM), new MemoryReadAddress(0xc400, 0xcfff, MRA_RAM), new MemoryReadAddress(0x0000, 0x7fff, MRA_ROM), new MemoryReadAddress(0xd408, 0xd408, input_port_0_r), new MemoryReadAddress(0xd409, 0xd409, input_port_1_r), new MemoryReadAddress(0xd40b, 0xd40b, input_port_2_r), new MemoryReadAddress(0xd40c, 0xd40c, input_port_3_r), new MemoryReadAddress(0xd40a, 0xd40a, input_port_4_r), new MemoryReadAddress(0xd40f, 0xd40f, AY8910_read_port_0_r), new MemoryReadAddress(0xd404, 0xd404, taito_gfxrom_r), new MemoryReadAddress(0x8801, 0x8801, elevator_protection_t_r), new MemoryReadAddress(0x8800, 0x8800, elevator_protection_r), new MemoryReadAddress(-1) };

    static MemoryWriteAddress writemem[] = { new MemoryWriteAddress(0x8000, 0x87ff, MWA_RAM), new MemoryWriteAddress(0xc400, 0xc7ff, videoram_w, videoram, videoram_size), new MemoryWriteAddress(0xc800, 0xcbff, taito_videoram2_w, taito_videoram2), new MemoryWriteAddress(0xcc00, 0xcfff, taito_videoram3_w, taito_videoram3), new MemoryWriteAddress(0xd100, 0xd17f, MWA_RAM, spriteram, spriteram_size), new MemoryWriteAddress(0xd000, 0xd01f, MWA_RAM, taito_colscrolly1), new MemoryWriteAddress(0xd020, 0xd03f, MWA_RAM, taito_colscrolly2), new MemoryWriteAddress(0xd040, 0xd05f, MWA_RAM, taito_colscrolly3), new MemoryWriteAddress(0xd500, 0xd500, MWA_RAM, taito_scrollx1), new MemoryWriteAddress(0xd501, 0xd501, MWA_RAM, taito_scrolly1), new MemoryWriteAddress(0xd502, 0xd502, MWA_RAM, taito_scrollx2), new MemoryWriteAddress(0xd503, 0xd503, MWA_RAM, taito_scrolly2), new MemoryWriteAddress(0xd504, 0xd504, MWA_RAM, taito_scrollx3), new MemoryWriteAddress(0xd505, 0xd505, MWA_RAM, taito_scrolly3), new MemoryWriteAddress(0xd506, 0xd507, taito_colorbank_w, taito_colorbank), new MemoryWriteAddress(0xd509, 0xd50a, MWA_RAM, taito_gfxpointer), new MemoryWriteAddress(0xd50b, 0xd50b, sound_command_w), new MemoryWriteAddress(0xd50d, 0xd50d, MWA_NOP), new MemoryWriteAddress(0xd200, 0xd27f, taito_paletteram_w, taito_paletteram), new MemoryWriteAddress(0x9000, 0xbfff, taito_characterram_w, taito_characterram), new MemoryWriteAddress(0xd50e, 0xd50e, elevatob_bankswitch_w), new MemoryWriteAddress(0xd40e, 0xd40e, AY8910_control_port_0_w), new MemoryWriteAddress(0xd40f, 0xd40f, AY8910_write_port_0_w), new MemoryWriteAddress(0xd300, 0xd300, MWA_RAM, taito_video_priority), new MemoryWriteAddress(0xd600, 0xd600, MWA_RAM, taito_video_enable), new MemoryWriteAddress(0x8800, 0x8800, MWA_RAM, elevator_protection), new MemoryWriteAddress(0x0000, 0x7fff, MWA_ROM), new MemoryWriteAddress(-1) };

    static MemoryReadAddress sound_readmem[] = { new MemoryReadAddress(0x4000, 0x43ff, MRA_RAM), new MemoryReadAddress(0x5000, 0x5000, sound_command_r), new MemoryReadAddress(0xe000, 0xe000, MWA_NOP), new MemoryReadAddress(0x0000, 0x1fff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress sound_writemem[] = { new MemoryWriteAddress(0x4000, 0x43ff, MWA_RAM), new MemoryWriteAddress(0x4800, 0x4800, AY8910_control_port_1_w), new MemoryWriteAddress(0x4801, 0x4801, AY8910_write_port_1_w), new MemoryWriteAddress(0x4802, 0x4802, AY8910_control_port_2_w), new MemoryWriteAddress(0x4803, 0x4803, AY8910_write_port_2_w), new MemoryWriteAddress(0x4804, 0x4804, AY8910_control_port_3_w), new MemoryWriteAddress(0x4805, 0x4805, AY8910_write_port_3_w), new MemoryWriteAddress(0x0000, 0x1fff, MWA_ROM), new MemoryWriteAddress(-1) };

    static InputPort input_ports[] = { new InputPort(0xff, new int[] { OSD_KEY_LEFT, OSD_KEY_RIGHT, OSD_KEY_DOWN, OSD_KEY_UP, OSD_KEY_CONTROL, OSD_KEY_ALT, 0, 0 }), new InputPort(0xff, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0xff, new int[] { 0, 0, 0, 0, 0, 0, OSD_KEY_1, OSD_KEY_2 }), new InputPort(0xef, new int[] { 0, 0, 0, 0, OSD_KEY_3, 0, 0, 0 }), new InputPort(0x7f, new int[] { 0, 0, 0, 0, 0, OSD_KEY_F2, 0, 0 }), new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0x7f, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(-1) };

    static TrakPort trak_ports[] = { new TrakPort(-1) };

    static KEYSet[] keys = { new KEYSet(0, 3, "MOVE UP"), new KEYSet(0, 0, "MOVE LEFT"), new KEYSet(0, 1, "MOVE RIGHT"), new KEYSet(0, 2, "MOVE DOWN"), new KEYSet(0, 4, "FIRE"), new KEYSet(0, 5, "JUMP"), new KEYSet(-1) };

    static DSW dsw[] = { new DSW(4, 0x18, "LIVES", new String[] { "6", "5", "4", "3" }, 1), new DSW(4, 0x03, "BONUS", new String[] { "25000", "20000", "15000", "10000" }, 1), new DSW(6, 0x03, "DIFFICULTY", new String[] { "HARDEST", "HARD", "MEDIUM", "EASY" }, 1), new DSW(4, 0x04, "FREE PLAY", new String[] { "ON", "OFF" }, 1), new DSW(6, 0x40, "DEMO MODE", new String[] { "ON", "OFF" }, 1), new DSW(6, 0x10, "COIN DISPLAY", new String[] { "NO", "YES" }, 1), new DSW(6, 0x20, "YEAR DISPLAY", new String[] { "NO", "YES" }, 1), new DSW(-1) };

    static GfxLayout charlayout = new GfxLayout(8, 8, 256, 3, new int[] { 512 * 8 * 8, 256 * 8 * 8, 0 }, new int[] { 7, 6, 5, 4, 3, 2, 1, 0 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8 }, 8 * 8);

    static GfxLayout spritelayout = new GfxLayout(16, 16, 64, 3, new int[] { 128 * 16 * 16, 64 * 16 * 16, 0 }, new int[] { 7, 6, 5, 4, 3, 2, 1, 0, 8 * 8 + 7, 8 * 8 + 6, 8 * 8 + 5, 8 * 8 + 4, 8 * 8 + 3, 8 * 8 + 2, 8 * 8 + 1, 8 * 8 + 0 }, new int[] { 0 * 8, 1 * 8, 2 * 8, 3 * 8, 4 * 8, 5 * 8, 6 * 8, 7 * 8, 16 * 8, 17 * 8, 18 * 8, 19 * 8, 20 * 8, 21 * 8, 22 * 8, 23 * 8 }, 32 * 8);

    static GfxDecodeInfo gfxdecodeinfo[] = { new GfxDecodeInfo(0, 0x9000, charlayout, 0, 16), new GfxDecodeInfo(0, 0x9000, spritelayout, 0, 8), new GfxDecodeInfo(0, 0xa800, charlayout, 0, 8), new GfxDecodeInfo(0, 0xa800, spritelayout, 0, 8), new GfxDecodeInfo(-1) };

    static char color_prom[] = { 0x01, 0xFF, 0x01, 0xFF, 0x01, 0xFA, 0x01, 0xF8, 0x01, 0xE2, 0x01, 0xD8, 0x01, 0xD1, 0x01, 0xC7, 0x01, 0xB6, 0x01, 0xB1, 0x01, 0xA4, 0x01, 0x92, 0x01, 0x89, 0x01, 0x6D, 0x01, 0x24, 0x01, 0x20, 0x00, 0xDB, 0x00, 0xA7, 0x00, 0x9C, 0x00, 0x98, 0x00, 0x92, 0x00, 0x87, 0x00, 0x80, 0x00, 0x59, 0x00, 0x53, 0x00, 0x49, 0x00, 0x40, 0x00, 0x3F, 0x00, 0x1A, 0x00, 0x19, 0x00, 0x11, 0x00, 0x0B, 0x00, 0x0A, 0x00, 0x08, 0x00, 0x07, 0x00, 0x03, 0x00, 0x00 };

    static MachineDriver machine_driver = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_Z80, 3072000, 0, readmem, writemem, null, null, interrupt, 1), new MachineCPU(CPU_Z80 | CPU_AUDIO_CPU, 3000000, 3, sound_readmem, sound_writemem, null, null, elevator_sh_interrupt, 5) }, 60, elevator_init_machine, 32 * 8, 32 * 8, new rectangle(0 * 8, 32 * 8 - 1, 2 * 8, 30 * 8 - 1), gfxdecodeinfo, 37, 16 * 8, taito_vh_convert_color_prom, VIDEO_TYPE_RASTER | VIDEO_MODIFIES_PALETTE, null, elevator_vh_start, taito_vh_stop, taito_vh_screenrefresh, null, null, elevator_sh_start, AY8910_sh_stop, AY8910_sh_update);

    /***************************************************************************

	  Game driver(s)

	***************************************************************************/
    static RomLoadPtr elevator_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("ea-ic69.bin", 0x0000, 0x1000, 0x24e277ef);
            ROM_LOAD("ea-ic68.bin", 0x1000, 0x1000, 0x13702e39);
            ROM_LOAD("ea-ic67.bin", 0x2000, 0x1000, 0x46f52646);
            ROM_LOAD("ea-ic66.bin", 0x3000, 0x1000, 0xe22fe57e);
            ROM_LOAD("ea-ic65.bin", 0x4000, 0x1000, 0xc10691d7);
            ROM_LOAD("ea-ic64.bin", 0x5000, 0x1000, 0x8913b293);
            ROM_LOAD("ea-ic55.bin", 0x6000, 0x1000, 0x1cabda08);
            ROM_LOAD("ea-ic54.bin", 0x7000, 0x1000, 0xf4647b4f);
            ROM_REGION(0x10000);
            ROM_LOAD("ea-ic4.bin", 0x0000, 0x1000, 0x55446482);
            ROM_REGION(0x80000);
            ROM_LOAD("ea-ic1.bin", 0x0000, 0x1000, 0xbbbb3fba);
            ROM_LOAD("ea-ic2.bin", 0x1000, 0x1000, 0x639cc2fd);
            ROM_LOAD("ea-ic3.bin", 0x2000, 0x1000, 0x61317eea);
            ROM_LOAD("ea-ic4.bin", 0x3000, 0x1000, 0x55446482);
            ROM_LOAD("ea-ic5.bin", 0x4000, 0x1000, 0x77895c0f);
            ROM_LOAD("ea-ic6.bin", 0x5000, 0x1000, 0x9a1b6901);
            ROM_LOAD("ea-ic7.bin", 0x6000, 0x1000, 0x839112ec);
            ROM_LOAD("ea-ic8.bin", 0x7000, 0x1000, 0xdb7ff692);
            ROM_REGION(0x100000);
            ROM_LOAD("ea-ic70.bin", 0x0000, 0x1000, 0x6d5f57cb);
            ROM_LOAD("ea-ic71.bin", 0x1000, 0x1000, 0xf0a769a1);
            ROM_END();
        }
    };

    static RomLoadPtr elevatob_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("ea69.bin", 0x0000, 0x1000, 0x66baa214);
            ROM_LOAD("ea-ic68.bin", 0x1000, 0x1000, 0x13702e39);
            ROM_LOAD("ea-ic67.bin", 0x2000, 0x1000, 0x46f52646);
            ROM_LOAD("ea66.bin", 0x3000, 0x1000, 0xb88f3383);
            ROM_LOAD("ea-ic65.bin", 0x4000, 0x1000, 0xc10691d7);
            ROM_LOAD("ea-ic64.bin", 0x5000, 0x1000, 0x8913b293);
            ROM_LOAD("ea55.bin", 0x6000, 0x1000, 0xd546923e);
            ROM_LOAD("ea54.bin", 0x7000, 0x1000, 0x963ec5a5);
            ROM_LOAD("-1", 0xe000, 0x1000, 0);
            ROM_LOAD("ea52.bin", 0xf000, 0x1000, 0x44b1314a);
            ROM_REGION(0x1000);
            ROM_LOAD("ea-ic4.bin", 0x0000, 0x1000, 0x55446482);
            ROM_REGION(0x8000);
            ROM_LOAD("ea-ic1.bin", 0x0000, 0x1000, 0xbbbb3fba);
            ROM_LOAD("ea-ic2.bin", 0x1000, 0x1000, 0x639cc2fd);
            ROM_LOAD("ea-ic3.bin", 0x2000, 0x1000, 0x61317eea);
            ROM_LOAD("ea-ic4.bin", 0x3000, 0x1000, 0x55446482);
            ROM_LOAD("ea-ic5.bin", 0x4000, 0x1000, 0x77895c0f);
            ROM_LOAD("ea-ic6.bin", 0x5000, 0x1000, 0x9a1b6901);
            ROM_LOAD("ea-ic7.bin", 0x6000, 0x1000, 0x839112ec);
            ROM_LOAD("ea08.bin", 0x7000, 0x1000, 0x67ebf7c1);
            ROM_REGION(0x10000);
            ROM_LOAD("ea-ic70.bin", 0x0000, 0x1000, 0x6d5f57cb);
            ROM_LOAD("ea-ic71.bin", 0x1000, 0x1000, 0xf0a769a1);
            ROM_END();
        }
    };

    static HiscoreLoadPtr hiload = new HiscoreLoadPtr() {

        public int handler(String name) {
            char RAM[] = Machine.memory_region[0];
            if (memcmp(RAM, 0x8350, new char[] { 0x00, 0x00, 0x01 }, 3) == 0) {
                FILE f;
                if ((f = fopen(name, "rb")) != null) {
                    fread(RAM, 0x8350, 1, 3, f);
                    fclose(f);
                }
                return 1;
            } else return 0;
        }
    };

    static HiscoreSavePtr hisave = new HiscoreSavePtr() {

        public void handler(String name) {
            char RAM[] = Machine.memory_region[0];
            FILE f;
            if ((f = fopen(name, "wb")) != null) {
                fwrite(RAM, 0x8350, 1, 3, f);
                fclose(f);
            }
            ;
        }
    };

    public static GameDriver elevator_driver = new GameDriver("Elevator Action", "elevator", "NICOLA SALMORIA\nTATSUYUKI SATOH", machine_driver, elevator_rom, null, null, null, input_ports, null, trak_ports, dsw, keys, color_prom, null, null, ORIENTATION_DEFAULT, hiload, hisave);

    public static GameDriver elevatob_driver = new GameDriver("Elevator Action (bootleg)", "elevatob", "NICOLA SALMORIA\nTATSUYUKI SATOH", machine_driver, elevatob_rom, null, null, null, input_ports, null, trak_ports, dsw, keys, color_prom, null, null, ORIENTATION_DEFAULT, hiload, hisave);
}
