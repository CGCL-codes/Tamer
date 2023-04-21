package test.pdf;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import uk.org.retep.pdf.*;
import test.*;

/**
 * This class tests the PDF package by building a simple document
 */
public class PDFJobTest implements Runnable {

    public PDFJobTest() {
    }

    private void doAllTests(Graphics g) {
        doTest(g);
        drawCharSet(g);
        imageTest(g);
    }

    public void run() {
        RunTests.section("Testing uk.org.retep.pdf.PDFJob class");
        try {
            String fname = "test/pdf/pdfjob.pdf";
            FileOutputStream fos = new FileOutputStream(fname);
            PDFJob job = PDF.getPDFJob(fos);
            RunTests.result(this, "Create PrintJob", true);
            Graphics g = job.getGraphics();
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString("This page is in Portrait", 50, 20);
            doAllTests(g);
            doSpecifics(job, g);
            PDFJob pj = (PDFJob) job;
            pj.addNote("This page should appear in Portrait.\n\nYou should on the right hand side\na square containing an arc, going\nfrom the north east to south west\ncorners (from 45 to 225 degrees).", 400, 60, 50, 50);
            RunTests.result(this, "Add text annotation", true);
            PDFPage p1 = pj.getCurrentPage();
            pj.addNote("Above this note is a hidden link (ie: no border).\n\nWhen activated, it should display page 2.", 200, 135, 200, 50);
            g.dispose();
            RunTests.result(this, "dispose Graphics object", true);
            g = job.getGraphics();
            RunTests.result(this, "create second page", true);
            PDFGraphics pg = (PDFGraphics) g;
            PDFOutline outline = pj.addOutline("Landscape page");
            pj.getCurrentPage().setOrientation(90);
            pg.setOrientation();
            RunTests.result(this, "set landscape", true);
            p1.addLink(195, 55, 60, 60, pj.getCurrentPage());
            RunTests.result(this, "add hyperlink on page 1 for this page", true);
            PDFAnnot ann = pj.getCurrentPage().addLink(195, 55, 60, 60, p1);
            ann.setBorder(PDFAnnot.DASHED, 1);
            RunTests.result(this, "add dashed hyperlink on page 2 to view page 1", true);
            pj.addNote("Above this note is a link with a dashed border.\n\nWhen activated, it should display page 1.\nNow as this page is in Landscape, this\nlink also tests that the\nPDFAnnot class translates the coordinates\ncorrectly.\n\nThe link should be around\nthe square arc test.", 200, 135, 200, 50);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString("This page is in Landscape", 50, 20);
            doAllTests(g);
            doSpecifics(pj, g);
            g.dispose();
            RunTests.result(this, "dispose Graphics object for second page", true);
            job.end();
            RunTests.result(this, "PDF File is " + fname, true);
        } catch (Exception ex) {
            RunTests.result(this, "Exception " + ex, false);
            ex.printStackTrace();
        }
    }

    public void doTest(Graphics g) {
        g.drawLine(10, 10, 50, 50);
        RunTests.result(this, "drawLine()", true);
        g.drawRect(30, 10, 10, 10);
        RunTests.result(this, "drawRect()", true);
        g.fillRect(30, 90, 10, 10);
        RunTests.result(this, "fillRect()", true);
        int xp[] = new int[] { 10, 10, 20, 15, 20 };
        int yp[] = new int[] { 50, 60, 60, 55, 50 };
        int np = xp.length;
        g.drawPolygon(xp, yp, np);
        RunTests.result(this, "drawPolygon()", true);
        xp = new int[] { 60, 60, 70, 65, 70 };
        yp = new int[] { 80, 90, 90, 85, 80 };
        np = xp.length;
        g.drawPolyline(xp, yp, np);
        RunTests.result(this, "drawPolyline()", true);
        xp = new int[] { 60, 60, 70, 65, 70 };
        yp = new int[] { 50, 60, 60, 55, 50 };
        np = xp.length;
        g.fillPolygon(xp, yp, np);
        RunTests.result(this, "fillPolygon()", true);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("This is a simple string", 10, 120);
        RunTests.result(this, "drawString() with simple string", true);
        g.drawString("This is a (complex) string", 10, 130);
        RunTests.result(this, "drawString() with complex string", true);
        g.drawString("(complex) string (with ( (multiple brackets ))", 10, 140);
        RunTests.result(this, "drawString() with complex string", true);
        g.drawRect(200, 60, 50, 50);
        g.drawLine(200, 60, 250, 110);
        g.drawLine(200, 110, 250, 60);
        g.drawLine(200, 85, 225, 60);
        g.drawArc(200, 60, 50, 50, 45, 180);
        RunTests.result(this, "drawArc() with square bounds", true);
        g.drawRect(300, 50, 80, 40);
        g.drawLine(300, 50, 380, 90);
        g.drawLine(300, 90, 380, 50);
        g.drawArc(300, 50, 80, 40, 135, 180);
        RunTests.result(this, "drawArc() with rectangular bounds", true);
        g.drawRect(400, 50, 40, 80);
        g.drawLine(400, 50, 440, 130);
        g.drawLine(400, 130, 440, 50);
        g.setColor(Color.blue);
        g.fillArc(400, 50, 40, 80, 135, 180);
        g.setColor(Color.black);
        RunTests.result(this, "fillArc() with rectangular bounds", true);
        g.drawRect(400, 150, 40, 80);
        g.drawLine(400, 150, 440, 230);
        g.drawLine(400, 230, 440, 150);
        g.setColor(Color.blue);
        g.fillArc(400, 150, 40, 80, 135, 225);
        g.setColor(Color.black);
        RunTests.result(this, "fillArc() with rectangular bounds", true);
        Graphics g2 = g.create();
        RunTests.result(this, "create() another Graphics class", true);
        drawStar(g2, 50, false);
        g2.dispose();
        RunTests.result(this, "dispose() child Graphics class", true);
        String fonts[] = new String[] { "SansSerif", "Monospaced", "TimesRoman", "Helvetica", "Courier", "Dialog", "DialogInput" };
        String modes[] = new String[] { "Plain", "Bold", "Italic", "Bold+Italic" };
        int imodes[] = new int[] { Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC };
        int ty = 170;
        for (int i = 0; i < modes.length; i++) g.drawString(modes[i], 100 + (50 * i), ty - 14);
        FontMetrics fm = g.getFontMetrics();
        for (int i = 0; i < fonts.length; i++) g.drawString(fonts[i], 98 - fm.stringWidth(fonts[i]), ty + (12 * i));
        Font cf = g.getFont();
        for (int i = 0; i < fonts.length; i++) {
            for (int j = 0; j < modes.length; j++) {
                g.setFont(new Font(fonts[i], imodes[j], 10));
                g.drawString(modes[j], 100 + (50 * j), ty);
            }
            ty += 12;
        }
    }

    private void doSpecifics(PDFJob job, Graphics g) {
        if (g instanceof PDFGraphics) {
            PDFGraphics pg = (PDFGraphics) g;
            Graphics g2;
            PDFPage page = job.getCurrentPage();
            g2 = g.create();
            drawStar(g2, 390, true);
            g2.dispose();
            RunTests.result(this, "setClip() using a Polygon as the shape", true);
            pg.setLineWidth(3.0);
            pg.drawLine(400, 200, 420, 220);
            pg.setLineWidth(1.0);
            pg.drawLine(400, 220, 420, 200);
            RunTests.result(this, "setLineWidth", true);
            pg.setDefaultLineWidth();
            RunTests.result(this, "setDefaultLineWidth", true);
            PDFOutline outline = job.addOutline("Test Card");
            RunTests.result(this, "Outline covering whole page", true);
            outline.add("Line Drawing", page, 0, 0, 200, 100);
            outline.add("Text Fonts", page, 0, 120, 350, 140);
            outline.add("drawArc 1", page, 198, 58, 54, 54);
            outline.add("drawArc 2", page, 298, 48, 84, 44);
            outline.add("fillArc", page, 398, 48, 44, 84);
            outline.add("lineWidth", page, 398, 198, 30, 30);
            RunTests.result(this, "Sub-Outlines covering areas of a page", true);
        }
    }

    private void drawStar(Graphics g, int ox, boolean clip) {
        int xp[] = new int[] { 391, 370, 311, 370, 391, 412, 471, 412 };
        int yp[] = new int[] { 392, 450, 472, 494, 552, 494, 472, 450 };
        int np = xp.length;
        ox = -391 + ox;
        for (int i = 0; i < np; i++) xp[i] = xp[i] + ox;
        Polygon star = new Polygon(xp, yp, xp.length);
        if (clip) {
            g.setClip(star);
            RunTests.result(this, "setClip on polygon", true);
        }
        g.drawPolygon(star);
        for (int y = 442; y < 502; y += 10) g.drawLine(311 + ox, y, 471 + ox, y);
        for (int x = 420; x < 460; x += 10) g.drawLine(x + ox, 380, x + ox, 500);
    }

    class imageCanvas extends Canvas {

        private Image image;

        public imageCanvas() {
            image = null;
        }

        public void setImage(Image img) {
            image = img;
            repaint();
        }

        public Dimension getMinimumSize() {
            return new Dimension(500, 600);
        }

        public Dimension getPreferredSize() {
            return getMinimumSize();
        }

        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            g.setColor(Color.gray);
            g.fillRect(0, 0, getSize().width, getSize().height);
            if (image != null) g.drawImage(image, 0, 0, this);
        }
    }

    /**
   * This allows us to run the test as a separate app. Here we can see onscreen
   * and printed via awt's printjob
   */
    public static Frame frame;

    public static imageCanvas canvas;

    /**
     * Entry point for the visual tests
     */
    public static void main(String[] args) {
        PDFJobTest j = new PDFJobTest(true);
    }

    protected PDFJobTest(boolean test) {
        frame = new Frame("PDFJobTest");
        frame.setLayout(new BorderLayout());
        frame.add(canvas = new imageCanvas());
        rc = canvas;
        Panel p = new Panel();
        frame.add(p, "South");
        p.setLayout(new FlowLayout());
        Button b;
        p.add(b = new Button("Display"));
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Image i = canvas.createImage(500, 600);
                canvas.setImage(i);
                Graphics g = i.getGraphics();
                doAllTests(g);
                g.dispose();
            }
        });
        p.add(b = new Button("Print"));
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(frame, "PDF Test Print", null);
                if (job != null) {
                    Graphics g = job.getGraphics();
                    doAllTests(g);
                    g.dispose();
                    job.end();
                }
            }
        });
        p.add(b = new Button("PDF"));
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    String fname = "test/pdf/pdfjob.pdf";
                    FileOutputStream fos = new FileOutputStream(fname);
                    PDFJob job = PDF.getPDFJob(fos);
                    if (job != null) {
                        Graphics g = job.getGraphics();
                        doAllTests(g);
                        g.dispose();
                        job.end();
                    }
                } catch (IOException ioe) {
                }
            }
        });
        p.add(b = new Button("Quit"));
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    public void bugTest(PrintJob prnJob) {
        try {
            Graphics grGraphics = prnJob.getGraphics();
            grGraphics.setClip(0, 0, 160, 160);
            grGraphics.setColor(Color.red);
            grGraphics.fillRect(0, 0, 10, 10);
            grGraphics.setColor(Color.blue);
            grGraphics.drawString(" test string", 10, 100);
            grGraphics.setColor(Color.red);
            grGraphics.translate(20, 20);
            grGraphics.drawString(" test string", 60, 100);
            grGraphics.dispose();
        } catch (Exception ee) {
            System.err.println("Print error: " + ee.toString());
            return;
        }
    }

    public void drawCharSet(Graphics g) {
        char[] c = new char[257];
        for (char i = 0; i < 257; i++) c[i] = i;
        for (char i = 0; i < 256; i = (char) (i + 8)) {
            g.drawChars(c, i, 8, 150, 200 + (i * 2));
        }
    }

    public void imageTest(Graphics g) {
    }

    public static ImageObserver rc;
}
