package drivers;

import static arcadeflex.libc.*;
import static mame.commonH.*;
import static mame.cpuintrf.*;
import static mame.driverH.*;
import static mame.mame.*;
import static mame.osdependH.*;
import static vidhrdw.generic.*;
import static sndhrdw.generic.*;
import static vidhrdw.ccastles.*;
import static sndhrdw.pokeyintf.*;
import static machine.ccastles.*;
import static mame.inptport.*;

public class ccastles {

    static MemoryReadAddress readmem[] = { new MemoryReadAddress(0x0000, 0x0001, MRA_RAM), new MemoryReadAddress(0x0002, 0x0002, ccastles_bitmode_r), new MemoryReadAddress(0x0003, 0x90ff, MRA_RAM), new MemoryReadAddress(0x9400, 0x9401, ccastles_trakball_r), new MemoryReadAddress(0x9500, 0x9501, ccastles_trakball_r), new MemoryReadAddress(0x9600, 0x9600, input_port_0_r), new MemoryReadAddress(0x9a08, 0x9a08, input_port_1_r), new MemoryReadAddress(0x9800, 0x980f, pokey1_r), new MemoryReadAddress(0x9a00, 0x9a0f, pokey2_r), new MemoryReadAddress(0xA000, 0xDFFF, ccastles_rom_r), new MemoryReadAddress(0xE000, 0xFFFF, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress writemem[] = { new MemoryWriteAddress(0x0000, 0x0001, ccastles_xy_w), new MemoryWriteAddress(0x0002, 0x0002, ccastles_bitmode_w), new MemoryWriteAddress(0x0003, 0x90ff, MWA_RAM), new MemoryWriteAddress(0x9800, 0x980F, pokey1_w), new MemoryWriteAddress(0x9A00, 0x9A0F, pokey2_w), new MemoryWriteAddress(0x9C80, 0x9C80, MWA_RAM), new MemoryWriteAddress(0x9D00, 0x9D00, MWA_RAM), new MemoryWriteAddress(0x9D80, 0x9D80, MWA_NOP), new MemoryWriteAddress(0x9E00, 0x9E00, MWA_NOP), new MemoryWriteAddress(0x9E80, 0x9E81, MWA_NOP), new MemoryWriteAddress(0x9E85, 0x9E86, MWA_NOP), new MemoryWriteAddress(0x9E87, 0x9E87, ccastles_bankswitch_w), new MemoryWriteAddress(0x9F00, 0x9F01, ccastles_axy_w), new MemoryWriteAddress(0x9F02, 0x9F07, MWA_RAM), new MemoryWriteAddress(0x9F80, 0x9FBF, ccastles_paletteram_w), new MemoryWriteAddress(-1) };

    static InputPort ccastles_input_ports[] = { new InputPort(0xdf, new int[] { OSD_KEY_4, OSD_KEY_3, OSD_KEY_5, OSD_KEY_T, 0, IPB_VBLANK, OSD_KEY_CONTROL, 0 }), new InputPort(0x3f, new int[] { 0, 0, 0, OSD_KEY_1, OSD_KEY_2, 0, 0, 0 }), new InputPort(-1) };

    static TrakPort ccastles_trak_ports[] = { new TrakPort(Y_AXIS, 0, 1.5, ccastles_trakball_y), new TrakPort(X_AXIS, 0, 1.0, ccastles_trakball_x), new TrakPort(-1) };

    static KEYSet ccastles_keys[] = { new KEYSet(-1) };

    static DSW ccastles_dsw[] = { new DSW(0, 0x10, "SELF TEST", new String[] { "ON", "OFF" }, 1), new DSW(-1) };

    static GfxLayout ccastles_spritelayout = new GfxLayout(8, 16, 256, 4, new int[] { 0x2000 * 8 + 0, 0x2000 * 8 + 4, 0, 4 }, new int[] { 0, 1, 2, 3, 8 + 0, 8 + 1, 8 + 2, 8 + 3 }, new int[] { 0 * 16, 1 * 16, 2 * 16, 3 * 16, 4 * 16, 5 * 16, 6 * 16, 7 * 16, 8 * 16, 9 * 16, 10 * 16, 11 * 16, 12 * 16, 13 * 16, 14 * 16, 15 * 16 }, 32 * 8);

    static GfxLayout fakelayout = new GfxLayout(1, 1, 0, 4, new int[] { 0 }, new int[] { 0 }, new int[] { 0 }, 0);

    static GfxDecodeInfo gfxdecodeinfo[] = { new GfxDecodeInfo(1, 0x0000, ccastles_spritelayout, 0, 1), new GfxDecodeInfo(0, 0, fakelayout, 16, 1), new GfxDecodeInfo(-1) };

    static MachineDriver ccastles_machine = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_M6502, 1500000, 0, readmem, writemem, null, null, interrupt, 4) }, 60, null, 256, 232, new rectangle(0, 255, 0, 231), gfxdecodeinfo, 32, 16 + 16, ccastles_vh_convert_color_prom, VIDEO_TYPE_RASTER | VIDEO_MODIFIES_PALETTE, null, ccastles_vh_start, ccastles_vh_stop, ccastles_vh_screenrefresh, null, null, pokey2_sh_start, pokey_sh_stop, pokey_sh_update);

    /***************************************************************************

      Game driver(s)

    ***************************************************************************/
    static RomLoadPtr ccastles_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x14000);
            ROM_LOAD("ccastles.303", 0xA000, 0x2000, 0xe3d3d32d);
            ROM_LOAD("ccastles.304", 0xC000, 0x2000, 0x31eab944);
            ROM_LOAD("ccastles.305", 0xE000, 0x2000, 0xd765a559);
            ROM_LOAD("ccastles.102", 0x10000, 0x2000, 0x5bbb3ac1);
            ROM_LOAD("ccastles.101", 0x12000, 0x2000, 0xe2aa8e74);
            ROM_REGION(0x4000);
            ROM_LOAD("ccastles.107", 0x0000, 0x2000, 0x399cc984);
            ROM_LOAD("ccastles.106", 0x2000, 0x2000, 0x8b4c0208);
            ROM_END();
        }
    };

    static HiscoreLoadPtr hiload = new HiscoreLoadPtr() {

        public int handler(String name) {
            FILE f;
            if ((f = fopen(name, "rb")) != null) {
                fread(RAM, 0x9000, 1, 0x100, f);
                fclose(f);
            }
            return 1;
        }
    };

    static HiscoreSavePtr hisave = new HiscoreSavePtr() {

        public void handler(String name) {
            FILE f;
            if ((f = fopen(name, "wb")) != null) {
                fwrite(RAM, 0x9000, 1, 0x100, f);
                fclose(f);
            }
        }
    };

    public static GameDriver ccastles_driver = new GameDriver("Crystal Castles", "ccastles", "PAT LAWRENCE\nCHRIS HARDY\nSTEVE CLYNES\nNICOLA SALMORIA", ccastles_machine, ccastles_rom, null, null, null, ccastles_input_ports, null, ccastles_trak_ports, ccastles_dsw, ccastles_keys, null, null, null, ORIENTATION_DEFAULT, hiload, hisave);
}
