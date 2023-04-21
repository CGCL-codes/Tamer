package net.benojt.display;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import net.benojt.FractalPanel;
import net.benojt.coloring.Coloring;
import net.benojt.dlgs.InfoDlg;
import net.benojt.iterator.IteratorReport;
import net.benojt.ui.IntegerSpinner;
import org.w3c.dom.NodeList;

/**
 * a buffered display that just counts the hits of a pixel. 
 * it uses much less space than the ComplexPlaneBuffered.
 * supports oversampling i.e. a pixel is represented by an array in the buffer which 
 * increases image quality.
 * @author felfe
 *
 */
public class ComplexPlaneHitCount extends ComplexPlane implements BufferedDisplay {

    static final String XMLNodeOversampling = "oversampling";

    /** the buffer array where the hits are counted */
    int[][] buffer;

    /** a dummy iterator used as parameter for the coloring */
    IteratorBuffer iteratorBuffer = new IteratorBuffer();

    /** the size of the array in the buffer that represents one pixel on screen */
    int oversampling = 1;

    /** the size of the buffer = display.getdimension() * oversampling */
    Dimension buffDim;

    /** the size of a pixel from the buffer in the complex plane = ps/oversampling */
    double bs;

    Point last;

    @Override
    public ComplexPlaneHitCount clone() {
        ComplexPlaneHitCount di = (ComplexPlaneHitCount) super.clone();
        di.iteratorBuffer = new IteratorBuffer();
        di.buffDim = new Dimension(this.buffDim);
        return di;
    }

    @Override
    public void initialize(FractalPanel forFp) {
        super.initialize(forFp);
        this.createBuffer();
        this.last = null;
    }

    /**
	 * create and initialize the buffer array where the hits are counted
	 * @see buffer
	 */
    protected void createBuffer() {
        buffDim = new Dimension(dispDim.width * oversampling, dispDim.height * oversampling);
        bs = ps / oversampling;
        try {
            if (this.buffer == null || this.buffer.length != buffDim.width || this.buffer[0].length != buffDim.height) this.buffer = new int[buffDim.width][buffDim.height];
            for (int y = 0; y < buffDim.height; y++) {
                for (int x = 0; x < buffDim.width; x++) {
                    this.buffer[x][y] = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this.fp, "Could not initialize display buffer.\n" + "Maybe the image is too large.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Deprecated
    @Override
    public int setPixel(double re, double im, net.benojt.iterator.Iterator it) {
        return this.setPixel(re, im, (IteratorReport) it);
    }

    @Override
    public int setPixel(double re, double im, IteratorReport it) {
        int res = 0;
        int px = (int) (buffDim.width / 2 + (re - viewPoint.re) / bs);
        int py = (int) (buffDim.height / 2 - (im - viewPoint.im) / bs);
        if (0 <= px && buffDim.width > px && 0 <= py && buffDim.height > py) {
            int col = 0;
            if (oversampling == 1) {
                this.iteratorBuffer.setHitsLeft(++this.buffer[px][py]);
                col = this.coloring.getColor(this.iteratorBuffer, px, py);
                super.setPixel(px, py, col);
            } else {
                ++this.buffer[px][py];
                col = this.getColorOversampling(px, py);
                super.setPixel(px / oversampling, py / oversampling, col);
            }
            res = col;
        } else res = 0;
        return res;
    }

    /** computes the color of a pixel as average of the colors of all buffer-pixel 
	 * contained in the display pixel.
	 * @param x
	 * @param y
	 * @return
	 */
    protected int getColorOversampling(int x, int y) {
        int _x = (x / oversampling) * oversampling;
        int _y = (y / oversampling) * oversampling;
        int red = 0;
        int green = 0;
        int blue = 0;
        int os2 = oversampling * oversampling;
        for (int i = 0; i < oversampling; i++) for (int j = 0; j < oversampling; j++) {
            this.iteratorBuffer.setHitsLeft(this.buffer[_x + i][_y + j]);
            int col1 = this.coloring.getColor(this.iteratorBuffer, x, y);
            red += Coloring.MASK_RED & col1;
            green += Coloring.MASK_GREEN & col1;
            blue += Coloring.MASK_BLUE & col1;
        }
        return (Coloring.MASK_RED & (red / os2)) | (Coloring.MASK_GREEN & (green / os2)) | (Coloring.MASK_BLUE & (blue / os2));
    }

    public void reColor() {
        if (this.buffer == null) {
            System.out.println("no buffer");
            return;
        }
        this.coloring = this.fp.getColoring();
        Dimension dim = this.getDimension();
        boolean oldRefreshState = this.fp.isRefreshing();
        if (!oldRefreshState) this.fp.startRefresh();
        for (int y = 0; y < dim.height; y++) {
            for (int x = 0; x < dim.width; x++) {
                int col = 0;
                if (oversampling == 1) {
                    this.iteratorBuffer.setHitsLeft(this.buffer[x][y]);
                    col = this.coloring.getColor(this.iteratorBuffer, x, y);
                } else col = this.getColorOversampling(x * oversampling, y * oversampling);
                this.setPixel(x, y, col);
            }
        }
        if (!oldRefreshState) this.fp.stopRefresh();
    }

    /**
	 * return the number of hits of a pixel at screen coordinates
	 * @param x
	 * @param y
	 * @return number of hits
	 */
    protected int getHits(int x, int y) {
        return buffer[x][y];
    }

    /**
	 * display a dialog with statistics about pixel hits
	 */
    public void showHitStatistics() {
        int maxHits = 0;
        int minHits = Integer.MAX_VALUE;
        int zeroHits = 0;
        long hitCount = 0;
        java.awt.Point maxHitsPoint = null;
        Dimension dim = this.getDimension();
        TreeMap<Integer, Integer> hitStat = new TreeMap<Integer, Integer>();
        for (int y = 0; y < dim.height; y++) {
            for (int x = 0; x < dim.width; x++) {
                int hits = this.getHits(x, y);
                hitCount += hits;
                if (hits == 0) zeroHits++; else {
                    int cat = (hits - 1) / 100;
                    Integer stat = hitStat.get(cat);
                    if (stat == null) hitStat.put(cat, 1); else hitStat.put(cat, ++stat);
                }
                if (hits > maxHits) {
                    maxHits = hits;
                    maxHitsPoint = new java.awt.Point(x, y);
                }
                if (hits < minHits && hits > 0) minHits = hits;
            }
        }
        String statsStr = "";
        if (zeroHits > 0) statsStr += "zero: " + zeroHits + "<BR>";
        for (Integer cat : hitStat.keySet()) statsStr += (cat * 100 + 1) + "-" + ((cat + 1) * 100) + ": " + hitStat.get(cat) + "<BR>";
        statsStr += "total: " + hitCount + "<BR>";
        statsStr += "min hits: " + minHits + "<BR>";
        if (maxHitsPoint != null) statsStr += "max hits: " + maxHits + " at (" + maxHitsPoint.x + ", " + maxHitsPoint.y + ")<BR>";
        new InfoDlg(null, "Hit statistics", statsStr, 200, 200).showDlg();
    }

    @Override
    public Vector<JMenuItem> getMenuItems(MouseEvent me) {
        boolean buildMenu = this.thisMenu.size() == 0;
        super.getMenuItems(me);
        if (buildMenu) {
            final JMenuItem mh = new JMenuItem("Show hit statistics");
            mh.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showHitStatistics();
                }
            });
            this.thisMenu.add(mh);
        }
        return this.thisMenu;
    }

    @Override
    public void buildXML() {
        super.buildXML();
        if (oversampling > 1) this.xmlContent.addProperty(XMLNodeOversampling, this.oversampling);
    }

    /**
	 * show a dialog with info about the pixel at this.mePoint
	 */
    @Override
    protected void showPixelInfo(String info) {
        int hits = this.buffer[this.mePoint.x][this.mePoint.y];
        super.showPixelInfo("<B>Hits:</B> " + hits + "<BR>" + info);
    }

    @Override
    public String loadConfig(NodeList nodes) {
        String errors = super.loadConfig(nodes);
        String osStr = this.loadedPropertyHT.get(XMLNodeOversampling);
        if (osStr == null) oversampling = 1; else try {
            Integer os = new Integer(osStr);
            if (os >= 1 && os <= 5) this.oversampling = os; else this.oversampling = 1;
        } catch (Exception ex) {
            errors += "Could not load coloring property " + XMLNodeOversampling + "\n";
        }
        return errors;
    }

    @Override
    public Vector<Class<? extends Coloring>> getPreferedColorings() {
        Vector<Class<? extends Coloring>> res = new Vector<Class<? extends Coloring>>(2);
        res.add(net.benojt.coloring.GradientByHits.class);
        res.add(net.benojt.coloring.Hits.class);
        return res;
    }

    public class ConfigDlg extends ComplexPlane.ConfigDlg {

        IntegerSpinner overSampleSP;

        public ConfigDlg(java.awt.Frame frame) {
            super(frame);
        }

        @Override
        protected void uiInit() {
            super.uiInit();
            this.overSampleSP = new IntegerSpinner("Oversampling:");
            this.panel.addContent(this.overSampleSP, NEW_LINE);
        }

        @Override
        public void dataInit() {
            super.dataInit();
            this.overSampleSP.setNumber(oversampling);
        }

        @Override
        protected void applyBT_action(java.awt.event.ActionEvent e) {
            int nos = this.overSampleSP.getNumber();
            if (nos >= 1 && nos <= 5) {
                oversampling = nos;
                mustRerender = true;
            }
            super.applyBT_action(e);
        }
    }

    @Override
    public String getInfoMessage() {
        return "A complex plane display which counts the hits for each pixel and colorizes accordingly." + "<BR>You can use oversampling to improve the image qualty. Oversampling means each pixel " + "is divided into a number of subpixels for which the hits are counted. The color for a " + "pixel is then computed as the average of the colors of all subpixels. The number of " + "subpixels is given as <B>OS</B>. OS = 1 means no oversampling. OS = 2 means 2x2 = 4 subpixels etc.";
    }
}
