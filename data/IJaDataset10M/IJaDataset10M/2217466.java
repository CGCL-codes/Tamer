package ces.platform.infoplat.utils.img.gif;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import ces.platform.infoplat.utils.util.*;

/**
 * Graphic extension block.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public final class GifGraphicEx extends GifEx {

    private static final byte mbyBlockSz = (byte) 0x04;

    private byte mbyPackFld = (byte) 0x00;

    private short msDelayTm = (short) 0x00;

    private byte mbyTrIndex = (byte) 0x00;

    private boolean mbIsTransparent = false;

    private boolean mbIsDelayed = false;

    /**
     * Constructor
     */
    GifGraphicEx(BufferedImage imgData) {
        super(imgData);
        mbyCtrlLab = (byte) 0xF9;
    }

    /**
     * Set transparency.
     * @param col the color value to be set transparent.
     *            set <code>null</code> to remove transparency.
     */
    public void setTransparency(Color col) {
        if (col != null) {
            int rgb[] = new int[3];
            rgb[0] = col.getRed();
            rgb[1] = col.getGreen();
            rgb[2] = col.getBlue();
            IndexColorModel cm = (IndexColorModel) getImageData().getColorModel();
            mbyTrIndex = (byte) cm.getDataElement(rgb, 0);
            mbyPackFld = (byte) (mbyPackFld | 0x01);
            mbIsTransparent = true;
        } else {
            mbyPackFld = (byte) (mbyPackFld & 0xFE);
            mbyTrIndex = 0;
            mbIsTransparent = false;
        }
    }

    /**
     * Is transparent.
     */
    public boolean isTransparent() {
        return mbIsTransparent;
    }

    /**
     * Set delay timing
     */
    public void setDelay(int delay) {
        mbyPackFld = (byte) (mbyPackFld | 0x02);
        msDelayTm = (short) delay;
        mbIsDelayed = true;
    }

    /**
     * Reset delay
     */
    public void resetDelay() {
        mbyPackFld = (byte) (mbyPackFld & 0xFD);
        msDelayTm = (short) 0x00;
        mbIsDelayed = false;
    }

    /**
     * Is delayed
     */
    public boolean isDelayed() {
        return mbIsDelayed;
    }

    /**
     * Get delay time
     */
    public int getDelayTime() {
        return msDelayTm & 0xffff;
    }

    /**
     * Write this block
     */
    public void write(DataOutputStream ds) throws IOException {
        ds.writeByte(mbyExIntro);
        ds.writeByte(mbyCtrlLab);
        ds.writeByte(mbyBlockSz);
        ds.writeByte(mbyPackFld);
        ds.writeShort(ByteUtils.changeSequence(msDelayTm));
        ds.writeByte(mbyTrIndex);
        ds.writeByte(mbyBlkTerm);
    }
}
