package edu.umd.cs.piccolo.swtexamples;

import java.util.Random;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import edu.umd.cs.piccolox.swt.SWTGraphics2D;

/**
 * Benchmarking test suite for SWT package
 */
public class SWTBenchTest extends Canvas {

    GeneralPath testShape = new GeneralPath();

    Image testImageOpaque, testImageBitmask, testImageTranslucent, testImageARGB;

    AffineTransform transform = new AffineTransform();

    static final AffineTransform IDENTITY = new AffineTransform();

    double pts[] = new double[20];

    static final Color colors[] = { Color.red, Color.green, Color.blue, Color.white, Color.yellow };

    boolean offscreen;

    boolean antialiased;

    int results[][] = new int[NUM_CONTEXTS][NUM_TESTS];

    static final int CTX_NORMAL = 0;

    static final int CTX_TRANSFORMED = 1;

    static final int NUM_CONTEXTS = 2;

    static String contextNames[] = { "normal", "transform" };

    static final int DRAW_LINE = 0;

    static final int DRAW_RECT = 1;

    static final int FILL_RECT = 2;

    static final int DRAW_OVAL = 3;

    static final int FILL_OVAL = 4;

    static final int DRAW_POLY = 5;

    static final int FILL_POLY = 6;

    static final int DRAW_TEXT = 7;

    static final int DRAW_IMG1 = 8;

    static final int DRAW_IMG2 = 9;

    static final int DRAW_IMG3 = 10;

    static final int DRAW_IMG4 = 11;

    static final int DRAW_IMG5 = 12;

    static final int NUM_TESTS = 13;

    static String testNames[] = { "line", "rect", "fill rect", "oval", "fill oval", "poly", "fill poly", "text", "image", "scaled image", "mask image", "alpha image", "argb image" };

    void testDrawLine(SWTGraphics2D g, Random r) {
        g.drawLine(rand(r), rand(r), rand(r), rand(r));
    }

    void testDrawRect(SWTGraphics2D g, Random r) {
        g.drawRect(rand(r), rand(r), rand(r), rand(r));
    }

    void testFillRect(SWTGraphics2D g, Random r) {
        g.fillRect(rand(r), rand(r), rand(r), rand(r));
    }

    void testDrawOval(SWTGraphics2D g, Random r) {
        g.drawOval(rand(r), rand(r), rand(r), rand(r));
    }

    void testFillOval(SWTGraphics2D g, Random r) {
        g.fillOval(rand(r), rand(r), rand(r), rand(r));
    }

    void genPoly(Random r) {
        for (int i = 0; i < pts.length / 2; i++) {
            pts[2 * i] = rand(r);
            pts[2 * i + 1] = rand(r);
        }
    }

    void testDrawPoly(SWTGraphics2D g, Random r) {
        genPoly(r);
        g.drawPolyline(pts);
    }

    void testFillPoly(SWTGraphics2D g, Random r) {
        genPoly(r);
        g.fillPolygon(pts);
    }

    void testDrawText(SWTGraphics2D g, Random r) {
        g.drawString("Abcdefghijklmnop", rand(r), rand(r));
    }

    void testDrawImg1(SWTGraphics2D g, Random r) {
        g.drawImage(testImageOpaque, rand(r), rand(r));
    }

    void testDrawImg2(SWTGraphics2D g, Random r) {
        Rectangle rect = testImageOpaque.getBounds();
        g.drawImage(testImageOpaque, 0, 0, rect.width, rect.height, rand(r), rand(r), rand(r), rand(r));
    }

    void testDrawImg3(SWTGraphics2D g, Random r) {
        g.drawImage(testImageBitmask, rand(r), rand(r));
    }

    void testDrawImg4(SWTGraphics2D g, Random r) {
        g.drawImage(testImageTranslucent, rand(r), rand(r));
    }

    void testDrawImg5(SWTGraphics2D g, Random r) {
        g.drawImage(testImageARGB, rand(r), rand(r));
    }

    Image loadImage(Display display, String name) {
        try {
            InputStream stream = SWTBenchTest.class.getResourceAsStream(name);
            if (stream != null) {
                ImageData imageData = new ImageData(stream);
                return new Image(display, imageData);
            }
        } catch (Exception e) {
        }
        return null;
    }

    SWTBenchTest(Composite parent, int style) {
        super(parent, style);
        testImageOpaque = loadImage(getDisplay(), "opaque.jpg");
        testImageBitmask = loadImage(getDisplay(), "bitmask.gif");
        testImageTranslucent = loadImage(getDisplay(), "translucent.png");
        testImageARGB = new Image(getDisplay(), 128, 128);
        GC tmpGC = new GC(testImageARGB);
        tmpGC.drawImage(testImageTranslucent, 0, 0);
        tmpGC.dispose();
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent pe) {
                runAll(new SWTGraphics2D(pe.gc, getDisplay()));
            }
        });
    }

    void setupTransform(Graphics2D g, Random r) {
        transform.setToIdentity();
        switch(abs(r.nextInt()) % 5) {
            default:
                double s = r.nextDouble();
                transform.scale(5 * s + 0.1, 5 * s + 0.1);
                break;
        }
        g.setTransform(transform);
    }

    void setupClip(Graphics2D g, Random r) {
    }

    void setupBlend(Graphics2D g, Random r) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, r.nextFloat()));
    }

    void setup(int ctx, Graphics2D g, Random r) {
        switch(ctx) {
            case CTX_NORMAL:
                break;
            case CTX_TRANSFORMED:
                setupTransform(g, r);
                break;
        }
    }

    void test(int testNum, SWTGraphics2D g, Random r) {
        g.setColor(colors[abs(r.nextInt()) % colors.length]);
        g.setBackground(colors[abs(r.nextInt()) % colors.length]);
        switch(testNum) {
            case DRAW_LINE:
                testDrawLine(g, r);
                break;
            case DRAW_RECT:
                testDrawRect(g, r);
                break;
            case FILL_RECT:
                testFillRect(g, r);
                break;
            case DRAW_OVAL:
                testDrawOval(g, r);
                break;
            case FILL_OVAL:
                testFillOval(g, r);
                break;
            case DRAW_POLY:
                testDrawPoly(g, r);
                break;
            case FILL_POLY:
                testFillPoly(g, r);
                break;
            case DRAW_TEXT:
                testDrawText(g, r);
                break;
            case DRAW_IMG1:
                testDrawImg1(g, r);
                break;
            case DRAW_IMG2:
                testDrawImg2(g, r);
                break;
            case DRAW_IMG3:
                testDrawImg3(g, r);
                break;
            case DRAW_IMG4:
                testDrawImg4(g, r);
                break;
            case DRAW_IMG5:
                testDrawImg5(g, r);
                break;
        }
    }

    void runTest(SWTGraphics2D g, int ctx, int testNum) {
        Random r1 = new Random(1);
        Random r2 = new Random(1);
        System.out.println("Test: " + testNames[testNum]);
        long t1 = System.currentTimeMillis();
        int i = 0;
        while (true) {
            if (i % 10 == 0) setup(ctx, g, r1);
            test(testNum, g, r2);
            i++;
            long t2 = System.currentTimeMillis();
            if (t2 - t1 >= 5000) {
                break;
            }
        }
        results[ctx][testNum] += i / 5;
        System.out.println("Shapes per second: " + (results[ctx][testNum]));
    }

    void runAll(SWTGraphics2D g) {
        System.out.println("BENCHMARKING: " + g);
        if (antialiased) {
            System.out.println("ANTIALIASED");
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        for (int ctx = 0; ctx < NUM_CONTEXTS; ctx++) {
            System.out.println("Context: " + contextNames[ctx]);
            for (int i = 0; i < NUM_TESTS; i++) {
                g.setClip(null);
                g.setTransform(IDENTITY);
                runTest(g, ctx, i);
            }
        }
        if (offscreen) {
            g.dispose();
        }
        String fileName = g.getClass().getName().replace('.', '_');
        if (offscreen) fileName += "-offscreen";
        if (antialiased) fileName += "-antialiased";
        dumpResults(fileName + ".txt");
        System.exit(0);
    }

    void dumpResults(String fileName) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            PrintWriter out = new PrintWriter(fout);
            out.print('\t');
            for (int i = 0; i < NUM_TESTS; i++) {
                out.print(testNames[i]);
                out.print('\t');
            }
            out.println("");
            for (int ctx = 0; ctx < NUM_CONTEXTS; ctx++) {
                out.print(contextNames[ctx]);
                for (int i = 0; i < NUM_TESTS; i++) {
                    out.print('\t');
                    out.print(results[ctx][i]);
                }
                out.println("");
            }
            out.close();
            results = new int[NUM_CONTEXTS][NUM_TESTS];
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Point computeSize(int wHint, int hHint) {
        return new Point(512, 512);
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        return computeSize(wHint, hHint);
    }

    static final int abs(int x) {
        return (x < 0 ? -x : x);
    }

    static final double rand(Random r) {
        return abs(r.nextInt()) % 500;
    }

    public static void main(String args[]) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        SWTBenchTest m = new SWTBenchTest(shell, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
        m.setSize(512, 512);
        for (int i = 0; i < args.length; i++) {
            if (args[i].intern() == "-offscreen") m.offscreen = true; else if (args[i].intern() == "-anti") m.antialiased = true; else {
                System.out.println("Usage: java BenchTest [-anti] [-offscreen]");
                System.exit(1);
            }
        }
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
