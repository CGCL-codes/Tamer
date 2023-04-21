package jemu.system.vz;

import java.awt.*;

/**
 * An improved 6847 emulation / VZ renderer.
 *
 * @author Richard Wilson
 */
public class FullRenderer extends SimpleRenderer {

    protected static final int BORDER = 16;

    protected static final int WIDTH = 256 + BORDER * 2;

    protected static final int HEIGHT = 192 + BORDER * 2;

    protected static final int CWIDTH = WIDTH / 8;

    protected static final int CBORDER = BORDER / 8;

    protected static final int XOFFSET = -8;

    protected static final int YOFFSET = -48;

    protected static final int XDISPEND = CBORDER + 32;

    protected static final int YDISPEND = BORDER + 192;

    protected static final int CYCLE_ADJUST = 206;

    protected byte[] mem;

    protected int data;

    protected int y = YOFFSET;

    protected int x = XOFFSET;

    protected int cline = 24;

    protected int crow = 0;

    protected int offset = 0;

    protected int count = 0;

    protected int addr = 0x7000;

    protected int lineStart = 0x7000;

    protected int vertAdjust = YOFFSET;

    protected int usedBorder;

    protected int usedMask;

    protected int snowMask = 0x03;

    /** Creates a new instance of FullRenderer */
    public FullRenderer(VZMemory memory) {
        super("Acurate VZ Renderer");
        mem = memory.getMemory();
    }

    @Override
    public void setVerticalAdjustment(int value) {
        y = vertAdjust = YOFFSET + value;
    }

    @Override
    public void renderScreen(VZMemory memory) {
        offset = 0;
        x = XOFFSET;
        y = vertAdjust;
        addr = lineStart = 0x7000;
        crow = 0;
        cline = 24;
        count = CYCLE_ADJUST;
        display.updateImage(true);
    }

    @Override
    public void cycle() {
        if ((++count & 3) == 0) {
            if (y >= 0 && y < HEIGHT && x >= 0) {
                if (x < CWIDTH) {
                    if (y >= BORDER && y < YDISPEND && x >= CBORDER && x < XDISPEND) {
                        int ch = data * mapMult + crow;
                        System.arraycopy(pixelMap, ch, pixels, offset, 8);
                        offset += 8;
                        usedBorder = border;
                        usedMask = borderMask;
                        addr++;
                    } else {
                        for (int i = 0; i < 8; i++) pixels[offset++] = usedBorder & usedMask;
                    }
                }
            }
            if (count == VZ.CYCLES_PER_SCAN) {
                count = 0;
                x = XOFFSET;
                if (y++ >= BORDER) {
                    if ((cline -= mapMult) <= 0) {
                        cline = 24;
                        if ((crow += 8) == mapMult) {
                            crow = 0;
                            lineStart += 32;
                        }
                    }
                    addr = lineStart;
                }
                if (y >= BORDER && y < YDISPEND) usedMask = borderMask;
            } else x++;
            data = mem[addr] & 0xff;
        }
    }

    @Override
    public Dimension getDisplaySize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void setMapMult(int value) {
        if (mapMult != value) {
            mapMult = value;
            cline = 24;
            crow = 0;
            if (y >= BORDER & y < YDISPEND) usedMask = borderMask;
        }
    }

    public void setSnow(boolean value) {
        snowMask = value ? 0x03 : 0;
    }

    public boolean isSnow() {
        return snowMask != 0;
    }

    @Override
    public int setData(int value) {
        return (count & snowMask) == 3 ? data = value : value;
    }
}
