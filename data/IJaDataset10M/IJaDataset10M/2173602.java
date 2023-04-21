package net.sf.jcgm.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataInput;
import java.io.IOException;

/**
 * Cell Array.
 * Class=4, Element=9
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: CellArray.java 47 2011-12-14 08:52:12Z phica $
 */
public class CellArray extends Command {

    private final int representationFlag;

    private final int nx;

    private final int ny;

    private final Point2D p;

    private final Point2D q;

    private final Point2D r;

    private Color[] colors;

    private int[] colorIndexes;

    private BufferedImage bufferedImage = null;

    public CellArray(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        this.p = makePoint();
        this.q = makePoint();
        this.r = makePoint();
        this.nx = makeInt();
        this.ny = makeInt();
        int localColorPrecision = makeInt();
        if (localColorPrecision == 0) {
            if (ColourSelectionMode.getType() == ColourSelectionMode.Type.INDEXED) {
                localColorPrecision = ColourIndexPrecision.getPrecision();
            } else {
                localColorPrecision = ColourPrecision.getPrecision();
            }
        }
        this.representationFlag = makeEnum();
        int nColor = this.nx * this.ny;
        if (ColourSelectionMode.getType().equals(ColourSelectionMode.Type.DIRECT)) {
            this.colors = new Color[nColor];
            if (this.representationFlag == 0) {
                int c = 0;
                while (c < nColor) {
                    int numColors = makeInt();
                    Color color = makeDirectColor();
                    int maxIndex = Math.min(numColors, nColor - c);
                    for (int i = 0; i < maxIndex; i++) {
                        this.colors[c++] = color;
                    }
                    if (c > 0 && c % this.nx == 0) {
                        alignOnWord();
                    }
                }
            } else if (this.representationFlag == 1) {
                int i = 0;
                for (int row = 0; row < this.ny; row++) {
                    for (int col = 0; col < this.nx; col++) {
                        this.colors[i++] = makeDirectColor();
                    }
                    alignOnWord();
                }
            } else {
                unsupported("unsupported representation flag " + this.representationFlag);
            }
        } else if (ColourSelectionMode.getType().equals(ColourSelectionMode.Type.INDEXED)) {
            this.colorIndexes = new int[nColor];
            if (this.representationFlag == 0) {
                int c = 0;
                while (c < nColor) {
                    int numColors = makeInt();
                    int colorIndex = makeColorIndex(localColorPrecision);
                    int maxIndex = Math.min(numColors, nColor - c);
                    for (int i = 0; i < maxIndex; i++) {
                        this.colorIndexes[c++] = colorIndex;
                    }
                    if (c > 0 && c % this.nx == 0) {
                        alignOnWord();
                    }
                }
            } else if (this.representationFlag == 1) {
                int i = 0;
                for (int row = 0; row < this.ny; row++) {
                    for (int col = 0; col < this.nx; col++) {
                        this.colorIndexes[i++] = makeColorIndex(localColorPrecision);
                    }
                    alignOnWord();
                }
            } else {
                unsupported("unsupported representation flag " + this.representationFlag);
            }
        } else {
            unsupported("unsupported color selection mode " + ColourSelectionMode.getType());
        }
    }

    @Override
    public void paint(CGMDisplay d) {
        if (this.bufferedImage == null) {
            this.bufferedImage = new BufferedImage(this.nx, this.ny, BufferedImage.TYPE_INT_RGB);
            WritableRaster raster = this.bufferedImage.getRaster();
            int currentPixel = 0;
            for (int row = 0; row < this.ny; row++) {
                for (int col = 0; col < this.nx; col++) {
                    Color c = (this.colors != null) ? this.colors[currentPixel++] : d.getIndexedColor(this.colorIndexes[currentPixel++]);
                    raster.setPixel(col, row, new int[] { c.getRed(), c.getGreen(), c.getBlue() });
                }
            }
            this.colors = null;
            this.colorIndexes = null;
        }
        Graphics2D g2d = d.getGraphics2D();
        AffineTransform transform = AffineTransform.getTranslateInstance(this.p.getX(), this.p.getY());
        assert (this.nx != 0 && this.ny != 0);
        double sx = (this.q.getX() - this.p.getX()) / this.nx;
        double sy = (this.q.getY() - this.r.getY()) / this.ny;
        transform.scale(sx, sy);
        g2d.drawRenderedImage(this.bufferedImage, transform);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CellArray");
        sb.append(" nx=").append(this.nx);
        sb.append(" ny=").append(this.ny);
        sb.append(" representation flag=").append(this.representationFlag);
        sb.append(" p=").append(this.p).append(",");
        sb.append(" q=").append(this.q).append(",");
        sb.append(" r=").append(this.r).append(",");
        return sb.toString();
    }
}
