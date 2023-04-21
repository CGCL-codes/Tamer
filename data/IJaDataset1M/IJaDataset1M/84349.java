package unitth.graphics.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import unitth.junit.TestHistory;
import unitth.junit.TestModule;
import unitth.junit.TestModuleSummary;
import unitth.junit.TestPackage;
import unitth.junit.TestPackageSummary;
import unitth.junit.TestRun;

/**
 * This class is responsible for creating graphs representing the pass rates for
 * different test runs and test module runs.
 */
public class PassRateGraphCreator extends GraphCreator {

    private static int RG_TRUE_WIDTH = TRUE_WIDTH;

    private static final int RG_TRUE_HEIGHT = 160;

    private static final int RG_LEFT_OFFSET = 30;

    private static int RG_WIDTH = RG_TRUE_WIDTH + RG_LEFT_OFFSET;

    private static final int RG_TOP_OFFSET = 10;

    private static final int RG_BOTTOM_OFFSET = 5;

    private static final int RG_HEIGHT = RG_TRUE_HEIGHT + RG_TOP_OFFSET + RG_BOTTOM_OFFSET;

    private static final int RG_YAXIS_LOCATION = RG_LEFT_OFFSET;

    private static final int RG_XAXIS_LOCATION = RG_HEIGHT - RG_BOTTOM_OFFSET;

    private static final int RG_BAR_WIDTH = 3;

    private static final int RG_BAR_SPACING = 1;

    private static final String MPR_PREFIX = "mpr-";

    private static final String MAIN_PASS_RATE_IMAGE = "th.png";

    private BufferedImage passRatesImg = null;

    /**
	 * CTOR, takes the test history to generate graphs from as input.
	 * 
	 * @param history
	 *            The test history containing all the statistics for the graphs.
	 */
    public PassRateGraphCreator(TestHistory history) {
        super(history);
        if (null != history) {
            int minWidth = history.getNoRuns() * (RG_BAR_WIDTH + RG_BAR_SPACING) + RG_LEFT_OFFSET;
            if (RG_TRUE_WIDTH < minWidth) {
                RG_TRUE_WIDTH = minWidth + 100;
                RG_WIDTH = RG_TRUE_WIDTH + RG_LEFT_OFFSET;
            }
        }
    }

    private void drawRGDiagramBase(Graphics2D g2) {
        g2.setPaint(Color.WHITE);
        g2.fill(new Rectangle2D.Double(0, 0, RG_WIDTH, RG_HEIGHT));
        g2.setPaint(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] { 3.0f }, 0.0f));
        int xStartPoint = RG_LEFT_OFFSET - 2;
        int xEndPoint = RG_TRUE_WIDTH + 2;
        int yL25 = RG_XAXIS_LOCATION - (RG_TRUE_HEIGHT / 4);
        int yL50 = RG_XAXIS_LOCATION - (RG_TRUE_HEIGHT / 2);
        int yL75 = RG_XAXIS_LOCATION - (RG_TRUE_HEIGHT * 3 / 4);
        int yL100 = RG_XAXIS_LOCATION - RG_TRUE_HEIGHT;
        Line2D.Double l25pct = new Line2D.Double(xStartPoint, yL25, xEndPoint, yL25);
        Line2D.Double l50pct = new Line2D.Double(xStartPoint, yL50, xEndPoint, yL50);
        Line2D.Double l75pct = new Line2D.Double(xStartPoint, yL75, xEndPoint, yL75);
        Line2D.Double l100pct = new Line2D.Double(xStartPoint, yL100, xEndPoint, yL100);
        g2.draw(l25pct);
        g2.draw(l50pct);
        g2.draw(l75pct);
        g2.setStroke(new BasicStroke(0.5f));
        g2.draw(l100pct);
        g2.setPaint(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke((float) YAXIS_WIDTH));
        g2.draw(new Line2D.Double(RG_YAXIS_LOCATION, 0, RG_YAXIS_LOCATION, RG_HEIGHT - 2));
        g2.draw(new Line2D.Double(RG_YAXIS_LOCATION - 3, RG_XAXIS_LOCATION, RG_TRUE_WIDTH + 3, RG_XAXIS_LOCATION));
        g2.setPaint(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 9));
        g2.drawString("100%", 0, yL100 + 4);
        g2.setFont(new Font("Verdana", Font.PLAIN, 9));
        g2.drawString(" 75%", 0, yL75 + 4);
        g2.drawString(" 50%", 0, yL50 + 4);
        g2.drawString(" 25%", 0, yL25 + 4);
    }

    /**
	 * This method creates the graphics object to draw the pass rates on. Then
	 * it in order calls methods for drawing the background and population of the
	 * graph before saving it to a file. <br>
	 * The method is only called when to draw the overall pass rate graph. There
	 * is a corresponding method to be called for drawing test module pass runs.
	 */
    public void drawPassRates() {
        passRatesImg = new BufferedImage(RG_WIDTH, RG_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = passRatesImg.createGraphics();
        drawRGDiagramBase(g2);
        populateRunGraph(g2);
        saveResultToImage(MAIN_PASS_RATE_IMAGE, passRatesImg);
        passRatesImg = null;
    }

    /**
	 * This method creates the graphics object to draw the pass rates on. Then
	 * it in order calls methods for drawing the background and population the
	 * graph before saving it to a file. <br>
	 * The method is only called when to draw a specific module pass rate graph.
	 * There is a corresponding method to be called for drawing the over all
	 * pass rate.
	 */
    public void drawPassRates(TestPackageSummary tps) {
        passRatesImg = new BufferedImage(RG_WIDTH, RG_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = passRatesImg.createGraphics();
        drawRGDiagramBase(g2);
        populateRunGraph(g2, "", tps.getName(), true);
        saveResultToImage(MPR_PREFIX + tps.getName().replace('.', '-') + ".png", passRatesImg);
        passRatesImg = null;
    }

    public void drawPassRates(TestModuleSummary tms) {
        passRatesImg = new BufferedImage(RG_WIDTH, RG_HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = passRatesImg.createGraphics();
        drawRGDiagramBase(g2);
        populateRunGraph(g2, tms.getName(), tms.getPackageName(), false);
        saveResultToImage(MPR_PREFIX + tms.getName().replace('.', '-') + ".png", passRatesImg);
        passRatesImg = null;
    }

    /**
	 * This method is call when the overall pass rate graph is to be populated.
	 * 
	 * @param g2
	 *            The pass rate graphics object to be populated.
	 */
    private void populateRunGraph(Graphics2D g2) {
        if (null == history.getRuns() || null == history) {
            return;
        }
        g2.setStroke(new BasicStroke(1.5f));
        int xStart = RG_YAXIS_LOCATION + YAXIS_WIDTH - RG_BAR_WIDTH;
        int xWidth = 0;
        int yStart = 0;
        int yHeight = 0;
        int barHeight = 0;
        int topBar = RG_XAXIS_LOCATION - RG_TRUE_HEIGHT;
        TestRun[] testRuns = history.getRuns().toArray(new TestRun[history.getRuns().size()]);
        for (int i = testRuns.length - 1; i >= 0; i--) {
            xStart += (RG_BAR_WIDTH + RG_BAR_SPACING);
            xWidth = RG_BAR_WIDTH;
            barHeight = (int) (testRuns[i].getPassPctDouble() / 100.0 * RG_TRUE_HEIGHT);
            yStart = RG_TRUE_HEIGHT - barHeight + RG_TOP_OFFSET;
            yHeight = barHeight - 1;
            if (testRuns[i].getPassPctDouble() != 100.0) {
                g2.setPaint(Color.RED.darker());
                g2.setComposite(makeComposite(2 * 0.1F));
                g2.fill(new Rectangle2D.Double(xStart, topBar, xWidth, RG_TRUE_HEIGHT - yHeight - 2));
            }
            g2.setPaint(new Color(0x00, 0xdf, 0x00));
            g2.setComposite(makeComposite(1F));
            g2.fill(new Rectangle2D.Double(xStart, yStart, xWidth, yHeight));
        }
    }

    /**
	 * This method is call when a test module pass rate graph is to be
	 * populated.
	 * 
	 * @param g2
	 *            The pass rate graphics object to be populated.
	 */
    private void populateRunGraph(Graphics2D g2, String name, String packageName, boolean isPackage) {
        g2.setPaint(new Color(0x00, 0xdf, 0x00));
        g2.setStroke(new BasicStroke(1.5f));
        int xStart = RG_YAXIS_LOCATION + YAXIS_WIDTH - RG_BAR_WIDTH;
        int xWidth = 0;
        int yStart = 0;
        int yHeight = 0;
        int barHeight = 1;
        int topBar = RG_XAXIS_LOCATION - RG_TRUE_HEIGHT;
        TestRun[] testRuns = history.getRuns().toArray(new TestRun[history.getRuns().size()]);
        for (int i = testRuns.length - 1; i >= 0; i--) {
            xStart += (RG_BAR_WIDTH + RG_BAR_SPACING);
            xWidth = RG_BAR_WIDTH;
            if (true == isPackage) {
                TestPackage tp = testRuns[i].getTestPackage(packageName);
                if (null != tp) {
                    barHeight = (int) (tp.getPassPctDouble() / 100.0 * RG_TRUE_HEIGHT);
                } else {
                    barHeight = 1;
                }
            } else {
                TestModule tm = testRuns[i].getTestModule(packageName, name);
                if (null != tm) {
                    barHeight = (int) (tm.getPassPctDouble() / 100.0 * RG_TRUE_HEIGHT);
                } else {
                    barHeight = 1;
                }
            }
            if (1 != barHeight) {
                yStart = RG_TRUE_HEIGHT - barHeight + RG_TOP_OFFSET;
                yHeight = barHeight - 1;
                if (testRuns[i].getPassPctDouble() != 100.0) {
                    g2.setPaint(Color.RED.darker());
                    g2.setComposite(makeComposite(2 * 0.1F));
                    g2.fill(new Rectangle2D.Double(xStart, topBar, xWidth, RG_TRUE_HEIGHT - yHeight - 2));
                }
                g2.setPaint(new Color(0x00, 0xdf, 0x00));
                g2.setComposite(makeComposite(1F));
                g2.fill(new Rectangle2D.Double(xStart, yStart, xWidth, yHeight));
            }
        }
    }
}
