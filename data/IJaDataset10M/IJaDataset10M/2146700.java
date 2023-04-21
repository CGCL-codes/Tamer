package net.benojt.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.benojt.FractalPanel;
import net.benojt.coloring.Coloring;
import net.benojt.iterator.Iterator;
import net.benojt.iterator.IteratorReport;
import net.benojt.tools.AbstractUIModule;
import net.benojt.tools.BigDecimalComplex;
import net.benojt.tools.BoundingBox;
import net.benojt.tools.Complex;

/**
 * most basic display implementation holds an image and can set/get pixels. 
 * does not return a display component that can be added to some container. 
 * could be used to render into memory without displaying on screen.
 * @author felfe
 *
 */
public class SimpleDisplay extends AbstractUIModule implements Display {

    static IteratorBuffer emptyBuf = new IteratorBuffer();

    /** the panel this display uses to display the fractal image */
    JPanel thisPanel = new JPanel();

    BufferedImage fractalImage;

    DataBuffer dataBuffer;

    FractalPanel fp;

    Coloring coloring;

    Dimension dispDim;

    Complex viewPoint;

    double ps;

    public SimpleDisplay() {
        super();
    }

    public SimpleDisplay(int w, int h) {
        this();
        this.setDimension(new Dimension(w, h));
    }

    @Override
    public SimpleDisplay clone() {
        SimpleDisplay di = (SimpleDisplay) super.clone();
        di.coloring = this.coloring.clone();
        return di;
    }

    @Override
    public void addFractalPanel(FractalPanel newFp) {
        this.fp = newFp;
        super.addFractalPanel(newFp);
    }

    @Override
    public void initialize(FractalPanel forFp) {
        super.initialize(forFp);
        this.coloring = forFp.getColoring();
        dispDim = this.getDimension();
        viewPoint = forFp.getCurrentView().getViewPoint().toDouble();
        ps = forFp.getCurrentView().getPixelSize().doubleValue();
        this.mustRerender = false;
    }

    public JComponent getDisplayComponent() {
        return this.thisPanel;
    }

    public void setPixel(int x, int y, int col) {
        this.dataBuffer.setElem(y * this.fractalImage.getWidth() + x, col);
    }

    public int setPixel(int x, int y, IteratorReport it) {
        int col = this.coloring.getColor(it, x, y);
        this.setPixel(x, y, col);
        return col;
    }

    @Deprecated
    public int setPixel(int x, int y, Iterator it) {
        int col = this.coloring.getColor(it, x, y);
        this.setPixel(x, y, col);
        return col;
    }

    public int setPixel(double re, double im, IteratorReport it) {
        int res;
        int px = (int) (dispDim.width / 2 + (re - viewPoint.re) / ps);
        int py = (int) (dispDim.height / 2 - (im - viewPoint.im) / ps);
        if (0 <= px && dispDim.width > px && 0 <= py && dispDim.height > py) res = this.setPixel(px, py, it); else res = 0;
        return res;
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    public int setPixel(double re, double im, Iterator it) {
        int res;
        int px = (int) (dispDim.width / 2 + (re - viewPoint.re) / ps);
        int py = (int) (dispDim.height / 2 - (im - viewPoint.im) / ps);
        if (0 <= px && dispDim.width > px && 0 <= py && dispDim.height > py) res = this.setPixel(px, py, it); else res = 0;
        return res;
    }

    public int setPixel(double[] coords, IteratorReport it) {
        return this.setPixel(coords[0], coords[1], it);
    }

    public int getPixel(int x, int y) {
        int c2 = this.dataBuffer.getElem(y * this.fractalImage.getWidth() + x);
        return c2;
    }

    public void clear() {
        Graphics g = this.fractalImage.getGraphics();
        g.setColor(new java.awt.Color(this.coloring.getColor(emptyBuf, 0, 0)));
        g.fillRect(0, 0, this.fractalImage.getWidth(), this.fractalImage.getHeight());
    }

    /**
	 * returns a reference to the image of this display.
	 * @return
	 */
    public BufferedImage getImage() {
        return this.fractalImage;
    }

    public void setDimension(Dimension d) {
        this.fractalImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
        this.dataBuffer = this.fractalImage.getRaster().getDataBuffer();
    }

    public Dimension getDimension() {
        if (this.fractalImage == null) return null;
        return new Dimension(this.fractalImage.getWidth(), this.fractalImage.getHeight());
    }

    public Point getOffset() {
        return new Point();
    }

    public View getNewView(View view) {
        View res = null;
        if (view instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) view;
            Dimension dim = this.getDimension();
            if (dim != null) {
                BigDecimal dx = bb.x2.subtract(bb.x1);
                BigDecimal dy = bb.y2.subtract(bb.y1);
                BigDecimal ps1 = dx.divide(new BigDecimal(dim.width), Math.abs(dx.scale()) + 3, BigDecimal.ROUND_HALF_EVEN).max(dy.divide(new BigDecimal(dim.height), Math.abs(dy.scale()) + 3, BigDecimal.ROUND_HALF_EVEN));
                BigDecimalComplex vp = bb.getCenter();
                ps1 = ps1.round(new MathContext(2));
                vp.re = vp.re.setScale(ps1.scale(), BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros();
                vp.im = vp.im.setScale(ps1.scale(), BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros();
                res = this.getNewView(new BasicView(ps1, vp));
            } else {
                System.out.println("no dimension");
                res = new BasicView();
            }
        } else {
            Iterator it = fp.getIterator();
            PlaneView v = new PlaneView(view != null ? view.getPixelSize() : this.getPixelSize(), view != null ? view.getViewPoint() : this.getViewPoint(), it != null ? it.getMaxIter() : 50);
            res = v;
        }
        return res;
    }

    public View getView() {
        return this.fp.getCurrentView();
    }

    public Vector<Class<? extends Coloring>> getPreferedColorings() {
        return null;
    }

    /** 
	 * returns the size of a pixel from the display in the plane/space.
	 * @return
	 */
    protected BigDecimal getPixelSize() {
        return this.fp.getCurrentView().getPixelSize();
    }

    /**
	 * returns the plane coordinates of the center of the displayed image.
	 * @return
	 */
    protected BigDecimalComplex getViewPoint() {
        return this.fp.getCurrentView().getViewPoint();
    }

    /**
	 * returns the coordinates of a pixel (from the display) in the plane/space
	 * @param p
	 * @return
	 */
    public BigDecimalComplex getPlaneCoords(Point p) {
        Dimension dim = this.getDimension();
        return new BigDecimalComplex(getViewPoint().re.subtract(getPixelSize().multiply(new BigDecimal(dim.width / 2d - p.x))), getViewPoint().im.add(getPixelSize().multiply(new BigDecimal(dim.height / 2d - p.y))));
    }

    /**
	 * returns the coordinates of a point (from the plane/space) in the display
	 * @param c
	 * @return
	 */
    public Point getDispCoords(BigDecimalComplex c) {
        Dimension dim = this.getDimension();
        return new Point(c.re.subtract(getViewPoint().re).divide(getPixelSize(), 1, RoundingMode.HALF_EVEN).intValue() + dim.width / 2, getViewPoint().im.subtract(c.im).divide(getPixelSize(), 1, RoundingMode.HALF_EVEN).intValue() + dim.height / 2);
    }
}
