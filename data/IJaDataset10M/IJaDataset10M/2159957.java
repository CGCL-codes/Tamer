package com.iver.cit.gvsig.fmap.core.symbols;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import junit.framework.TestCase;
import com.iver.cit.gvsig.fmap.core.FPolyline2D;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;

/**
 * Integration test to test that Line symbols always draw in the same
 * place respecting size constraints.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestILineSymbol extends TestCase {

    private ILineSymbol[] symbols;

    private final Dimension sz = new Dimension(401, 401);

    private final FPolyline2D centerL;

    {
        GeneralPathX gp = new GeneralPathX();
        gp.moveTo(0, sz.height / 2);
        gp.lineTo(sz.width, sz.height / 2);
        centerL = new FPolyline2D(gp);
    }

    private static final double sizes[] = new double[] { 300, 250, 225, 200, 100, 50, 30, 15, 5, 3, 2, 1 };

    private static final float INNER_TOLERANCE = TestIMarkerSymbol.INNER_TOLERANCE;

    private static final float OUTTER_TOLERANCE = TestIMarkerSymbol.OUTTER_TOLERANCE;

    protected void setUp() throws Exception {
        super.setUp();
        ISymbol[] allSymbols = TestISymbol.getNewSymbolInstances();
        ArrayList symbols = new ArrayList();
        for (int i = 0; i < allSymbols.length; i++) {
            if (allSymbols[i] instanceof ILineSymbol) {
                ILineSymbol sym = (ILineSymbol) allSymbols[i];
                symbols.add(sym);
            }
        }
        this.symbols = (ILineSymbol[]) symbols.toArray(new ILineSymbol[symbols.size()]);
    }

    public void testDrawingSize() {
        for (int i = 0; i < symbols.length; i++) {
            for (int j = 0; j < sizes.length; j++) {
                BufferedImage bi = new BufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = bi.createGraphics();
                ILineSymbol testSymbol = symbols[i];
                testSymbol.setLineColor(Color.YELLOW);
                testSymbol.setLineWidth(sizes[j]);
                String name = TestISymbol.getNameForSymbol(testSymbol);
                Point2D upperP1 = new Point2D.Double(0, centerL.getBounds().getY() - sizes[j] * 0.5);
                Point2D upperP2 = new Point2D.Double(centerL.getBounds().getWidth(), centerL.getBounds().getY() - sizes[j] * 0.5);
                GeneralPathX gpUp = new GeneralPathX();
                gpUp.moveTo(upperP1.getX(), upperP1.getY());
                gpUp.lineTo(upperP2.getX(), upperP2.getY());
                GeneralPathX gpDown = new GeneralPathX();
                Point2D lowerP1 = new Point2D.Double(0, centerL.getBounds().getY() + sizes[j] * 0.5);
                Point2D lowerP2 = new Point2D.Double(centerL.getBounds().getWidth(), centerL.getBounds().getY() + sizes[j] * 0.5);
                gpDown.moveTo(lowerP1.getX(), lowerP1.getY());
                gpDown.lineTo(lowerP2.getX(), lowerP2.getY());
                testSymbol.draw(g, new AffineTransform(), centerL, null);
                assertFalse("fails sizing line, too big (" + name + ", " + sizes[j] + "px)", isOutsideRect(bi, upperP1, lowerP1, OUTTER_TOLERANCE));
                assertTrue("fails sizing line, too small (" + name + ", " + sizes[j] + "px) \n" + "\t - forgot to enable ANTIALIASING?", fitsInsideRect(bi, upperP1, lowerP1, INNER_TOLERANCE));
            }
        }
    }

    private boolean isOutsideRect(BufferedImage bi, Point2D upper, Point2D lower, float outterTolerance) {
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (j < upper.getY() - outterTolerance && j > lower.getY() + outterTolerance) if (bi.getRGB(i, j) != 0) {
                    System.out.println("too big In pixel (" + i + ", " + j + ")");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean fitsInsideRect(BufferedImage bi, Point2D upper, Point2D lower, float innerTolerance) {
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (j < upper.getY() + innerTolerance && j > lower.getY() - innerTolerance) if (bi.getRGB(i, j) == 0) {
                    System.out.println("does not fit big In pixel (" + i + ", " + j + ")");
                    return false;
                }
            }
        }
        return true;
    }
}
