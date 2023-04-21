package v;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import m.BBox;
import utils.C2JUtils;

public class BufferedRenderer16 extends SoftwareVideoRenderer {

    static final String rcsid = "$Id: BufferedRenderer16.java,v 1.2 2011/10/23 20:40:44 velktron Exp $";

    /** Buffered Renderer has a bunch of images "pegged" to the underlying arrays */
    public BufferedImage[] screenbuffer = new BufferedImage[5];

    /** Indexed renderers keep separate color models for each colormap (intended as gamma levels) and 
 * palette levels */
    protected IndexColorModel[][] cmaps;

    public BufferedRenderer16(int w, int h, IndexColorModel icm) {
        super(w, h);
        this.setIcm(icm);
    }

    /** Normally, you only have the palettes available ONLY after you read the palette from disk.
 *  So use the super contructor, and then this when the palettes are available.
 *  
 * @param icm2
 */
    public void setIcm(IndexColorModel icm2) {
        this.icm = icm2;
    }

    public BufferedRenderer16(int w, int h) {
        super(w, h);
    }

    private IndexColorModel icm;

    private int usepalette = 0xFF;

    @Override
    public final void Init() {
        int i;
        for (i = 0; i < 4; i++) {
            this.setScreen(i, this.width, this.height);
        }
        dirtybox = new BBox();
    }

    /** This implementation will "tie" a bufferedimage to the underlying byte raster.
 * 
 * NOTE: thie relies on the ability to "tap" into a BufferedImage's backing array,
 * in order to have fast writes without setpixel/getpixel. If that is not possible,
 * then we'll need to use a special renderer.
 * 
 */
    @Override
    public final void setScreen(int index, int width, int height) {
        WritableRaster r = icm.createCompatibleWritableRaster(width, height);
        if (this.icm == null) screenbuffer[index] = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED); else screenbuffer[index] = new BufferedImage(icm, r, false, null);
        screens[index] = ((DataBufferByte) screenbuffer[index].getRaster().getDataBuffer()).getData();
    }

    /** We only call this once we have a stable WritableRaster, and we only want
 *  a different colormodel (e.g. after changing gamma). It's slower than keepings
 *  severerl BufferedImages ready, so it's only used when changing gamma. The
 *  backing screen, array etc. should not have changed at this moment.
 * 
 * @param index
 * @param r
 */
    protected final void setScreen(int index, WritableRaster r) {
        screenbuffer[index] = new BufferedImage(this.icm, r, false, null);
    }

    public final void changePalette(int pal) {
        this.usepalette = (pal << 8);
    }

    /** Get a bunch of BufferedImages "pegged" on the same output screen of this
 *  Doom Video Renderer, but with different palettes, defined in icms[]
 *  This is VERY speed efficient assuming that an IndexedColorModel will be used,
 *  rather than a 32-bit canvas, and memory overhead is minimal. Call this ONLY
 *  ONCE when initializing the video renderer, else it will invalidate pretty much
 *  everything in an ongoing game.
 * 
 *  NOTE: this will actually CREATE a new byte array for the screen, so it's important
 *  that this is called BEFORE anything else taps into it.
 * 
 * @param screen
 * @param icms
 * @return
 */
    public BufferedImage[] getBufferedScreens(int screen, IndexColorModel[] icms) {
        BufferedImage[] b = new BufferedImage[icms.length];
        this.icm = icms[0];
        if (r == null) {
            setScreen(screen, this.getWidth(), this.getHeight());
            r = screenbuffer[screen].getRaster();
        } else setScreen(screen, r);
        b[screen] = this.screenbuffer[screen];
        for (int i = 0; i < icms.length; i++) {
            if (i != screen) b[i] = new BufferedImage(icms[i], r, false, null);
        }
        return b;
    }

    protected final void specificPaletteCreation(byte[] paldata, short[][] gammadata, final int palettes, final int colors, final int stride, final int gammalevels) {
        System.out.printf("Enough data for %d palettes", maxpalettes);
        System.out.printf("Enough data for %d gamma levels", maxgammas);
        cmaps = new IndexColorModel[maxgammas][];
        cmaps[0] = new IndexColorModel[maxpalettes];
        for (int i = 0; i < maxpalettes; i++) {
            cmaps[0][i] = new IndexColorModel(8, colors, paldata, i * stride * colors, false);
        }
        byte[] tmpcmap = new byte[colors * stride];
        for (int j = 1; j < maxgammas; j++) {
            cmaps[j] = new IndexColorModel[maxpalettes];
            for (int i = 0; i < maxpalettes; i++) {
                for (int k = 1; k < 256; k++) {
                    tmpcmap[3 * k] = (byte) gammadata[j][0x00FF & paldata[i * colors * stride + stride * k]];
                    tmpcmap[3 * k + 1] = (byte) gammadata[j][0x00FF & paldata[1 + i * colors * stride + stride * k]];
                    tmpcmap[3 * k + 2] = (byte) gammadata[j][0x00FF & paldata[2 + i * colors * stride + stride * k]];
                }
                cmaps[j][i] = new IndexColorModel(8, 256, tmpcmap, 0, false);
            }
        }
    }

    WritableRaster r;

    public void report(BufferedImage[] b) {
        System.out.println("Main video buffer " + screens[0]);
        for (int i = 0; i < b.length; i++) {
            System.out.println(((Object) b[i].getRaster()).toString() + " " + b[i].getRaster().hashCode() + " " + ((DataBufferByte) (b[i].getRaster().getDataBuffer())).getData());
        }
    }

    public void setPalette(int palette) {
        this.currentpal = palette % maxpalettes;
        this.currentscreen = this.screenbuffer[currentpal];
    }

    @Override
    public void setUsegamma(int gamma) {
        this.usegamma = gamma % maxgammas;
        this.setCurrentScreen(0);
    }

    public void setCurrentScreen(int screen) {
        super.setCurrentScreen(screen);
        this.screenbuffer = this.getBufferedScreens(usescreen, cmaps[usegamma]);
        this.currentscreen = this.screenbuffer[currentpal];
    }
}
