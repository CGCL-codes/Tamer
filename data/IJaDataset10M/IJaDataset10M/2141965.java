package vidhrdw;

import static arcadeflex.libc.*;
import static mame.common.*;
import static mame.commonH.*;
import static mame.driverH.*;
import static mame.mame.*;
import static mame.osdependH.*;
import static vidhrdw.generic.*;

public class yiear {

    public static WriteHandlerPtr yiear_videoram_w = new WriteHandlerPtr() {

        public void handler(int offset, int data) {
            if (videoram.read(offset) != data) {
                dirtybuffer[offset / 2] = 1;
                videoram.write(offset, data);
            }
        }
    };

    public static VhUpdatePtr yiear_vh_screenrefresh = new VhUpdatePtr() {

        public void handler(osd_bitmap bitmap) {
            int i;
            {
                CharPtr vr = videoram;
                for (i = 0; i < videoram_size[0]; i++) if (dirtybuffer[i] != 0) {
                    char byte1 = vr.read(i * 2);
                    char byte2 = vr.read(i * 2 + 1);
                    int code = 16 * (byte1 & 0x10) + byte2;
                    int flipx = byte1 & 0x80;
                    int flipy = byte1 & 0x40;
                    int sx = 8 * (i % 32);
                    int sy = 8 * (i / 32);
                    dirtybuffer[i] = 0;
                    drawgfx(tmpbitmap, Machine.gfx[0], code, 0, flipx, flipy, sx, sy, null, TRANSPARENCY_NONE, 0);
                }
            }
            copybitmap(bitmap, tmpbitmap, 0, 0, 0, 0, Machine.drv.visible_area, TRANSPARENCY_NONE, 0);
            {
                CharPtr sr = spriteram;
                for (i = 23 * 16; i >= 0; i -= 16) {
                    int sy = 239 - sr.read(i + 0x06);
                    if (sy < 239) {
                        int code = 256 * (sr.read(i + 0x0f) & 1) + sr.read(i + 0x0e);
                        int flipx = ((sr.read(i + 0x0f) & 0x40) != 0) ? 0 : 1;
                        int flipy = ((sr.read(i + 0x0f) & 0x80) != 0) ? 1 : 0;
                        int sx = sr.read(i + 0x04);
                        drawgfx(bitmap, Machine.gfx[1], code, 0, flipx, flipy, sx, sy, Machine.drv.visible_area, TRANSPARENCY_PEN, 0);
                    }
                }
            }
        }
    };
}
