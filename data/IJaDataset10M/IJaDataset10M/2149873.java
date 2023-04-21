package tjacobs.print;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import tjacobs.ui.menus.FileMenu;
import tjacobs.ui.util.WindowUtilities;

/**
 * This implements the Printable and Pageable java.awt.print interfaces
 * Allows you to very easily and quickly generate a printout from a component
 * @author tjacobs
 */
public class StandardPrint implements Printable, Pageable {

    Component c;

    SpecialPrint sp;

    PageFormat mFormat;

    boolean mScale = false;

    boolean mMaintainRatio = true;

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    public static final int CENTER = 4;

    public static final int TOP = 2;

    public static final int BOTTOM = 3;

    private boolean mPrintPageNumber = false;

    private Font mPageNumberFont;

    private int hAlign = CENTER;

    private int vAlign = BOTTOM;

    private String mTitle;

    private Font mTitleFont;

    public StandardPrint(Component c) {
        this.c = c;
        if (c instanceof SpecialPrint) {
            sp = (SpecialPrint) c;
        }
    }

    public StandardPrint(SpecialPrint sp) {
        this.sp = sp;
    }

    public void setNumberFont(Font f) {
        mPageNumberFont = f;
    }

    public Font getNumberFont() {
        return mPageNumberFont;
    }

    public int getPageNumberHAlignment() {
        return hAlign;
    }

    public int getPageNumberVAlignment() {
        return vAlign;
    }

    public void setPageNumberHAlignment(int num) {
        hAlign = num;
    }

    public void setPageNumberVAlignment(int num) {
        vAlign = num;
    }

    public void setPrintPageNumber(boolean b) {
        mPrintPageNumber = b;
    }

    public boolean getPrintPageNumber() {
        return mPrintPageNumber;
    }

    protected void printPageNumber(Graphics g, PageFormat format, int pageNo) {
        if (!getPrintPageNumber()) return;
        Shape clip = g.getClip();
        g.setClip(null);
        if (mPageNumberFont != null) {
            g.setFont(mPageNumberFont);
        }
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();
        String pageStr = "" + (pageNo + 1);
        int width = fm.stringWidth(pageStr);
        int xspot = (int) (format.getWidth() - width) / 2;
        if (getPageNumberHAlignment() == LEFT) {
            xspot = (int) format.getImageableX();
        } else if (getPageNumberHAlignment() == RIGHT) {
            xspot = (int) (format.getWidth() - format.getImageableX() - format.getImageableWidth());
        }
        int yspot = (int) (format.getImageableY() + format.getImageableHeight() + 2 * height);
        if (getPageNumberVAlignment() == TOP) {
            yspot = (int) format.getImageableY() / 2 - height;
        }
        g.drawString(pageStr, xspot, yspot);
        g.setClip(clip);
    }

    public String getTitle() {
        return mTitle;
    }

    public Font getTitleFont() {
        return mTitleFont;
    }

    public void setTitleFont(Font f) {
        mTitleFont = f;
    }

    public void setTitle(String s) {
        mTitle = s;
    }

    protected void printTitle(Graphics g, PageFormat format) {
        if (mTitle == null) return;
        Shape clip = g.getClip();
        g.setClip(null);
        if (mTitleFont != null) {
            g.setFont(mTitleFont);
        }
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();
        int width = fm.stringWidth(mTitle);
        int xspot = (int) (format.getWidth() - width) / 2;
        int yspot = (int) format.getImageableY() / 2 + height;
        g.drawString(mTitle, xspot, yspot);
        g.setClip(clip);
    }

    public boolean isPrintScaled() {
        return mScale;
    }

    public void setPrintScaled(boolean b) {
        mScale = b;
    }

    public boolean getMaintainsAspect() {
        return mMaintainRatio;
    }

    public void setMaintainsAspect(boolean b) {
        mMaintainRatio = b;
    }

    public void start() throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (mFormat == null) {
            mFormat = job.defaultPage();
        }
        job.setPageable(this);
        if (job.printDialog()) {
            job.print();
        }
    }

    public void setPageFormat(PageFormat pf) {
        mFormat = pf;
    }

    public static void printStandardComponent(Pageable p) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(p);
        job.print();
    }

    private Dimension getJobSize() {
        if (sp != null) {
            return sp.getPrintSize();
        } else {
            return c.getSize();
        }
    }

    public static Image preview(int width, int height, Printable sp, PageFormat pf, int pageNo) {
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        return preview(im, sp, pf, pageNo);
    }

    public static Image preview(Image im, Printable sp, PageFormat pf, int pageNo) {
        return preview(im, sp, pf, pageNo, true);
    }

    public static Image preview(Image im, Printable sp, PageFormat pf, int pageNo, boolean doClipping) {
        Graphics2D g = (Graphics2D) im.getGraphics();
        int width = im.getWidth(null);
        int height = im.getHeight(null);
        double hratio = height / pf.getHeight();
        double wratio = width / pf.getWidth();
        if (doClipping) {
            Rectangle rect = new Rectangle();
            rect.x = (int) (pf.getImageableX() * (wratio));
            rect.y = (int) (pf.getImageableY() * (hratio));
            rect.width = (int) (pf.getImageableWidth() * (wratio));
            rect.height = (int) (pf.getImageableHeight() * (hratio));
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, width, height);
            g.setClip(rect);
        }
        g.scale(wratio, hratio);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) (width * 1 / wratio), (int) (height * 1 / hratio));
        try {
            sp.print(g, pf, pageNo);
        } catch (PrinterException pe) {
            pe.printStackTrace();
        }
        g.dispose();
        return im;
    }

    public int print(Graphics gr, PageFormat format, int pageNo) {
        mFormat = format;
        if (pageNo > getNumberOfPages()) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g = (Graphics2D) gr;
        if (getTitle() != null) {
            printTitle(g, format);
        }
        if (getPrintPageNumber()) {
            printPageNumber(g, format, pageNo);
        }
        g.translate((int) format.getImageableX(), (int) format.getImageableY());
        if (!isPrintScaled()) {
            int horizontal = getNumHorizontalPages();
            int horizontalOffset = (int) ((pageNo % horizontal) * format.getImageableWidth());
            int verticalOffset = (int) ((pageNo / horizontal) * format.getImageableHeight());
            double ratio = getScreenRatio();
            g.scale(1 / ratio, 1 / ratio);
            g.translate(-horizontalOffset, -verticalOffset);
            if (sp != null) {
                sp.printerPaint(g);
            } else {
                c.paint(g);
            }
            g.translate(horizontalOffset, verticalOffset);
            g.scale(ratio, ratio);
        } else {
            double ratio = getScreenRatio();
            g.scale(1 / ratio, 1 / ratio);
            double xScale = 1.0;
            double yScale = 1.0;
            double wid;
            double ht;
            if (sp != null) {
                wid = sp.getPrintSize().width;
                ht = sp.getPrintSize().height;
            } else {
                wid = c.getWidth();
                ht = c.getHeight();
            }
            xScale = format.getImageableWidth() / wid;
            yScale = format.getImageableHeight() / ht;
            if (getMaintainsAspect()) {
                xScale = yScale = Math.min(xScale, yScale);
            }
            g.scale(xScale, yScale);
            if (sp != null) {
                sp.printerPaint(g);
            } else {
                c.paint(g);
            }
            g.scale(1 / xScale, 1 / yScale);
            g.scale(ratio, ratio);
        }
        g.translate((int) -format.getImageableX(), (int) -format.getImageableY());
        return Printable.PAGE_EXISTS;
    }

    public int getNumHorizontalPages() {
        Dimension size = getJobSize();
        if (mFormat == null) mFormat = getPageFormat(0);
        int imWidth = (int) mFormat.getImageableWidth();
        int pWidth = 1 + (int) (size.width / getScreenRatio() / imWidth) - (imWidth / getScreenRatio() == size.width ? 1 : 0);
        return pWidth;
    }

    private double getScreenRatio() {
        double res = Toolkit.getDefaultToolkit().getScreenResolution();
        double ratio = res / 72.0;
        return ratio;
    }

    public int getNumVerticalPages() {
        Dimension size = getJobSize();
        int imHeight = (int) mFormat.getImageableHeight();
        int pHeight = (int) (1 + (size.height / getScreenRatio() / imHeight)) - (imHeight == size.height ? 1 : 0);
        return pHeight;
    }

    public int getNumberOfPages() {
        if (isPrintScaled()) return 1;
        return getNumHorizontalPages() * getNumVerticalPages();
    }

    public Printable getPrintable(int i) {
        return this;
    }

    public PageFormat getPageFormat(int page) {
        if (mFormat == null) {
            PrinterJob job = PrinterJob.getPrinterJob();
            mFormat = job.defaultPage();
        }
        return mFormat;
    }

    public static void main(String args[]) {
        JFrame jf = new JFrame("StandardPrint Test");
        final JTextArea area = new JTextArea();
        area.append("hello\n");
        for (int i = 0; i < 50; i++) {
            area.append("\n");
        }
        area.append("world\n");
        JScrollPane sp = new JScrollPane(area);
        jf.add(sp);
        JMenuBar bar = new JMenuBar();
        FileMenu fm = new FileMenu() {

            private static final long serialVersionUID = 1L;

            public void load(File f) {
            }

            public void save(File f) {
            }

            public Pageable getPageable() {
                return new StandardPrint(area);
            }
        };
        JMenu printMenu = new JMenu("Print");
        JMenuItem print = new JMenuItem("Print");
        printMenu.add(print);
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                StandardPrint sp = new StandardPrint(area);
                sp.setTitle("Hello World");
                sp.setPrintPageNumber(true);
                sp.setPageNumberVAlignment(BOTTOM);
                sp.setPageNumberHAlignment(CENTER);
                System.out.println(sp.getNumberOfPages());
                Image im1 = preview(300, 300, sp, sp.getPageFormat(0), 0);
                Image im2 = preview(300, 300, sp, sp.getPageFormat(1), 1);
                JLabel l = new JLabel(new ImageIcon(im1));
                WindowUtilities.visualize(l, false);
                l = new JLabel(new ImageIcon(im2));
                WindowUtilities.visualize(l, false);
            }
        };
        print.addActionListener(al);
        jf.setJMenuBar(bar);
        bar.add(fm);
        bar.add(printMenu);
        jf.setBounds(100, 100, 400, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
