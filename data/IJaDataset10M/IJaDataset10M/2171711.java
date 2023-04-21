package net.benojt.iterator;

import javax.swing.JCheckBox;
import net.benojt.FractalPanel;
import net.benojt.display.Display;
import net.benojt.ui.DoubleTextField;
import net.benojt.ui.IntegerSpinner;
import org.w3c.dom.NodeList;

/**
 * buddhabrot.
 * @author frank
 *
 */
public class Buddhabrot extends AbstractIterator {

    static final String XMLNodeMinIter = "minIter";

    static final String XMLNodeMaxValue2 = "maxValue2";

    static final String XMLNodeMaxIter2 = "maxIter2";

    static final String XMLNodeAnti = "antiBuddha";

    protected Display display;

    protected int minIter, maxIter2;

    protected double maxValue2;

    protected boolean anti;

    public Buddhabrot() {
        super();
        this.maxIter = 2000;
        this.maxValue = 16;
        this.minIter = 100;
        this.maxIter2 = 1000;
        this.maxValue2 = 4;
        this.anti = false;
        usebdComplex = false;
    }

    @Override
    public Buddhabrot clone() {
        Buddhabrot it = (Buddhabrot) super.clone();
        it.display = this.display.clone();
        return it;
    }

    @Override
    public void initialize(FractalPanel fp) {
        super.initialize(fp);
        display = fp.getDisplay();
    }

    @Override
    public int iterPoint(double[] coords) {
        double re = coords[0];
        double im = coords[1];
        z.re = 0d;
        z.im = 0d;
        value = 0d;
        iter = 0;
        while (iter++ <= maxIter && value <= maxValue) {
            double _zre = z.re * z.re - z.im * z.im + re;
            double _zim = 2 * z.re * z.im + im;
            z.re = _zre;
            z.im = _zim;
            value = z.re * z.re + z.im * z.im;
        }
        if (iter > minIter && (anti ? iter > maxIter : iter <= maxIter)) {
            z.re = re;
            z.im = im;
            value = 0d;
            iter = 0;
            while (iter++ <= maxIter2 && value <= maxValue2) {
                double _zre = z.re * z.re - z.im * z.im + re;
                double _zim = 2 * z.re * z.im + im;
                z.re = _zre;
                z.im = _zim;
                value = z.re * z.re + z.im * z.im;
                display.setPixel(z.re, z.im, (IteratorReport) this);
            }
        }
        return iter > maxIter ? -1 : iter;
    }

    @Override
    public void buildXML() {
        super.buildXML();
        this.xmlContent.setTagName(Iterator.XMLNodeName);
        this.xmlContent.addProperty(XMLNodeMinIter, this.minIter);
        this.xmlContent.addProperty(XMLNodeMaxValue2, this.maxValue2);
        this.xmlContent.addProperty(XMLNodeMaxIter2, this.maxIter2);
        this.xmlContent.addProperty(XMLNodeAnti, this.anti);
    }

    @Override
    public String loadConfig(NodeList nodes) {
        String errors = super.loadConfig(nodes);
        this.minIter = this.getProperty(XMLNodeMinIter, Integer.class, 500, errors);
        this.maxIter2 = this.getProperty(XMLNodeMaxIter2, Integer.class, this.maxIter2, errors);
        this.maxValue2 = this.getProperty(XMLNodeMaxValue2, Double.class, this.maxValue2, errors);
        this.anti = this.getProperty(XMLNodeAnti, Boolean.class, false, errors);
        return errors;
    }

    public class ConfigDlg extends AbstractIterator.ConfigDlg {

        IntegerSpinner maxIter2SP, minIterSP;

        DoubleTextField mv2JTF;

        JCheckBox antiCB;

        public ConfigDlg(java.awt.Frame frame) {
            super(frame);
        }

        @Override
        protected void uiInit() {
            super.uiInit();
            this.minIterSP = new IntegerSpinner("min Iters:");
            this.addContent(this.minIterSP, NEW_LINE);
            this.mv2JTF = new DoubleTextField(null, "Bailout 2");
            this.mv2JTF.setRemoveE0(true);
            this.addContent(mv2JTF, NEW_LINE);
            this.maxIter2SP = new IntegerSpinner("max Iters 2:");
            this.addContent(this.maxIter2SP, NEW_LINE);
            this.antiCB = new JCheckBox("Antibuddhabrot");
            this.addContent(this.antiCB, COL_SPAN(2), NEW_LINE);
        }

        @Override
        public void dataInit() {
            super.dataInit();
            this.minIterSP.setNumber(minIter);
            this.mv2JTF.setNumber(maxValue2);
            this.maxIter2SP.setNumber(maxIter2);
            this.antiCB.setSelected(anti);
        }

        @Override
        protected void applyBT_action(java.awt.event.ActionEvent e) {
            int mini2 = this.minIterSP.getNumber();
            if (mini2 > 0 && mini2 < 100000000 && mini2 != minIter) {
                minIter = mini2;
                mustRerender = true;
            }
            double mv2 = mv2JTF.getNumber();
            if (mv2 >= minMaxValue && mv2 != maxValue2) {
                maxValue2 = mv2;
                mustRerender = true;
            }
            int maxi2 = this.maxIter2SP.getNumber();
            if (maxi2 > 0 && maxi2 < 100000000 && maxi2 != maxIter2) {
                maxIter2 = maxi2;
                mustRerender = true;
            }
            boolean an = this.antiCB.isSelected();
            if (an != anti) {
                anti = an;
                mustRerender = true;
            }
            super.applyBT_action(e);
        }
    }

    @Override
    public String getInfoMessage() {
        return "This is a variation of the Mandelbrot set. " + "The iteration takes place in two loops. The first loop is a normal " + "Mandelbrot loop that is initialized with (0, 0) and in which no pixels are set.<BR>" + "The second loop is only executed if the number of iterations after which " + "the iteration is escaped is between <B>minIters</B> and <B>maxIters</B>. " + "It will be initialized to the coortinates that are iterated and uses " + "the parameter <B>maxIters 2</B> and <B>Bailout 2</B>. During the second loop " + "the pixel is set in every iteration step.<P>" + "If <B>Antibuddhabrot</B> is checked the second loop is executed if the first " + "loop does not escape after <B>max Iters</B> iterations.<P>" + "The iterated formula can not be configured but there is a template for " + "Buddhabrot fractals where formulas can be inserted.";
    }

    @Override
    public String getDefaultConfig() {
        return "<benojt>	" + "	<panel>	" + "		<iterator class = 'Buddhabrot'>	" + "			<property name = 'maxValue'>16.0</property>" + "			<property name = 'maxIter'>2000</property>" + "			<property name = 'boundingBox'>BoundingBox[left=-2,top=-2,right=2,bottom=2]</property>" + "			<property name = 'minIter'>500</property>" + "			<property name = 'maxValue2'>4.0</property>" + "			<property name = 'maxIter2'>1000</property>" + "			<property name = 'antiBuddha'>false</property>" + "		</iterator>" + "		<renderer class = 'RandomPointRenderer'>	" + "			<property name = 'threadPriority'>1</property>" + "			<property name = 'numberOfPoints'>1000000</property>" + "			<property name = 'dimension'>2</property>" + "			<property name = 'boundingBox'>BoundingBox[left=-2,top=-2,right=2,bottom=2]</property>" + "			<property name = 'setPixel'>false</property>" + "		</renderer>" + "		<coloring class = 'GradientByHits'>	" + "			<color name = 'emptyColor'>java.awt.Color[r=0,g=0,b=0]</color>" + "			<color position = '0.0' name = '0'>	java.awt.Color[r=0,g=0,b=0]</color>" + "			<color position = '0.5' name = '1'>	java.awt.Color[r=0,g=255,b=255]</color>" + "			<color position = '1.0' name = '2'>	java.awt.Color[r=255,g=255,b=255]</color>" + "			<property name = 'colorCount'>100</property>" + "		</coloring>" + "		<display class = 'ComplexPlaneHitCount'>	" + "			<property name = 'pixelSize'>0.0075</property>" + "			<property name = 'viewPoint'>BigDecimalComplex[re=-0.5,im=0.0000]</property>" + "		</display>" + "	</panel>" + "</benojt>";
    }
}
