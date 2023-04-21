package org.xfc.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.xfc.XApp;
import org.xfc.components.XDefaultAction;
import org.xfc.components.XFrame;
import org.xfc.components.XThrobber;
import org.xfc.dialog.XErrorDialog;
import org.xfc.util.XUtils;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A simple, flexible print preview dialog. The user can print from it with the
 * same results as from the print dialog.
 * 
 * @author Devon Carew
 */
public class XPrintPreviewDialog extends XFrame {

    static ImageIcon[] pageLayoutIcons = XUtils.splitImageIconStrip(XApp.getApp().getResources().getIcon("/firefox/skin/classic/global/icons/Print-preview.png"), 16);

    private JToolBar toolbar;

    private JLabel pageLabel;

    private JScrollBar scrollBar;

    private XThrobber throbber;

    private PageRenderer pageRenderer;

    private Printable printable;

    private PrinterJob printJob;

    private PageFormat pageFormat;

    private int currentPage = 0;

    private int totalPages = 0;

    private XDefaultAction pageSetupAction = new XDefaultAction("pagesetup-action") {

        public void actionPerformed(ActionEvent event) {
            doPageSetup();
        }
    };

    private XDefaultAction printAction = new XDefaultAction("print-action") {

        public void actionPerformed(ActionEvent event) {
            doPrint();
        }
    };

    public XPrintPreviewDialog() {
        this(XApp.getApp());
    }

    public XPrintPreviewDialog(XApp app) {
        super(app);
        initUI();
    }

    private void initUI() {
        XApp.getComponentFactory().applyAction(pageSetupAction);
        XApp.getComponentFactory().applyAction(printAction);
        setTitle("Print Preview");
        setIconImage(XPrintUtilities.PRINT_ICON.getImage());
        pageLabel = new JLabel(" ");
        pageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scrollBar = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 1);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent event) {
                if (currentPage != scrollBar.getValue()) showPage(scrollBar.getValue());
            }
        });
        pageRenderer = new PageRenderer();
        throbber = new XThrobber(true);
        throbber.setVisible(false);
        toolbar = XApp.getComponentFactory().createToolBar();
        toolbar.add(XApp.getComponentFactory().createButton(pageSetupAction));
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(XApp.getComponentFactory().createButton(printAction));
        toolbar.setBorder(null);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(toolbar, BorderLayout.CENTER);
        topPanel.add(throbber, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)));
        Container content = getContentPane();
        content.setLayout(new FormLayout("4px min:grow 4px pref", "pref 4px min:grow 2px pref 4px"));
        content.add(topPanel, "1, 1, 4, 1, f, f");
        content.add(pageRenderer, "2, 3, f, f");
        content.add(scrollBar, "4, 3, r, f");
        content.add(pageLabel, "2, 5, c, c");
    }

    public void show() {
        setSize(450, 575);
        center();
        super.show();
        startCalcTotalPages();
    }

    public void hide() {
        stopCalcTotalPages();
        super.hide();
    }

    public boolean showPage(int pageNumber) {
        pageRenderer.setCurrentPage(pageNumber);
        currentPage = pageNumber;
        updateLabel();
        if (scrollBar.getValue() != currentPage) scrollBar.setValue(currentPage);
        return true;
    }

    private void updateLabel() {
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
    }

    public void setPrintable(Printable printable, String jobName) {
        this.printable = printable;
        printJob = XPrintUtilities.createPrintJob(jobName);
        pageFormat = XPrintUtilities.getLastUserPageFormat();
        if (pageFormat == null) {
            pageFormat = printJob.defaultPage();
            XPrintUtilities.setLastUserPageFormat(pageFormat);
        } else {
            pageFormat = printJob.validatePage(pageFormat);
        }
    }

    public void setPageable(Pageable pageable, String jobName) {
        XUtils.throwNotImplemented();
    }

    private PageCounter pageCounter;

    private void startCalcTotalPages() {
        pageCounter = new PageCounter();
        Thread thread = new Thread(pageCounter);
        thread.setPriority(Thread.NORM_PRIORITY - 1);
        thread.start();
    }

    private void stopCalcTotalPages() {
        PageCounter copy = pageCounter;
        if (copy != null) copy.stop();
    }

    private void addPage() {
        totalPages++;
        scrollBar.setMaximum(totalPages);
        if (totalPages == 1) showPage(0);
        updateLabel();
    }

    private void doPrint() {
        printJob.setPrintable(printable, pageFormat);
        XPrintTask printTask = new XPrintTask(printJob);
        printTask.start();
        dispose();
    }

    private void doPageSetup() {
        PageFormat newPageFormat = printJob.pageDialog(pageFormat);
        if (newPageFormat != pageFormat) {
            pageFormat = newPageFormat;
            XPrintUtilities.setLastUserPageFormat(pageFormat);
            XPrintUtilities.setLastUserPrintService(printJob.getPrintService());
            pageRenderer.invalidate();
            validate();
        }
    }

    private class PageRenderer extends JComponent {

        private int rendererCurrentPage = -1;

        public PageRenderer() {
            setOpaque(true);
            setBorder(new PageBorder());
        }

        public Dimension getPreferredSize() {
            return new Dimension((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
        }

        private Dimension calcScreenPageSize() {
            Dimension size = getSize();
            Insets insets = getInsets();
            size.width -= (insets.left + insets.right);
            size.height -= (insets.top + insets.bottom);
            double h_w_aspectRatio = pageFormat.getWidth() / pageFormat.getHeight();
            double xNorm = size.width / h_w_aspectRatio;
            if (xNorm < size.height) {
                size.height = (int) (size.width / h_w_aspectRatio);
                if (size.height != (int) (size.width / h_w_aspectRatio)) size.width -= 1;
            } else {
                size.width = (int) (size.height * h_w_aspectRatio);
                if (size.width != (int) (size.height * h_w_aspectRatio)) size.height -= 1;
            }
            return size;
        }

        private void setCurrentPage(int pageNum) {
            rendererCurrentPage = pageNum;
            repaint();
        }

        public void paint(Graphics g) {
            Insets insets = getInsets();
            Dimension size = getSize();
            Dimension screenPageSize = calcScreenPageSize();
            int screenX = (size.width - screenPageSize.width) / 2;
            int screenY = (size.height - screenPageSize.height) / 2;
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.fillRect(screenX, screenY, screenPageSize.width, screenPageSize.height);
            if (getBorder() != null) {
                getBorder().paintBorder(this, g, screenX - insets.left, screenY - insets.top, screenPageSize.width + insets.left + insets.right, screenPageSize.height + insets.top + insets.bottom);
            }
            if (rendererCurrentPage != -1 && rendererCurrentPage < totalPages && printable != null) {
                double screenWidthPixels = screenPageSize.width;
                double pageWidthPixels = pageFormat.getWidth();
                double scaleFactor = screenWidthPixels / pageWidthPixels;
                Graphics2D gCopy = null;
                try {
                    gCopy = (Graphics2D) g.create();
                    gCopy.translate(screenX, screenY);
                    gCopy.scale(scaleFactor, scaleFactor);
                    gCopy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    gCopy.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    gCopy.setClip((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY(), (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableWidth());
                    gCopy.setColor(Color.BLACK);
                    gCopy.setFont(getFont());
                    PrinterGraphicsDelegate printerGraphics = new PrinterGraphicsDelegate(gCopy);
                    printable.print(printerGraphics, pageFormat, rendererCurrentPage);
                } catch (PrinterException pe) {
                    pe.printStackTrace();
                } finally {
                    if (gCopy != null) gCopy.dispose();
                }
            }
        }
    }

    private class PageCounter implements Runnable {

        private boolean shouldStop;

        public void run() {
            throbber.startThrobber();
            throbber.setVisible(true);
            Image image = createImage((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
            Graphics g = image.getGraphics();
            int pageNumber = 0;
            try {
                while (!shouldStop) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setClip((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY(), (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableWidth());
                    g2.setColor(Color.BLACK);
                    g2.setFont(getFont());
                    PrinterGraphicsDelegate printerGraphics = new PrinterGraphicsDelegate(g2);
                    if (Printable.PAGE_EXISTS == printable.print(printerGraphics, pageFormat, pageNumber)) {
                        pageNumber++;
                        addPage();
                    } else {
                        shouldStop = true;
                    }
                    g2.dispose();
                }
            } catch (Throwable t) {
                XErrorDialog.showError("Error Previewing Document", t);
            }
            throbber.setVisible(false);
            throbber.stopThrobber();
        }

        private void stop() {
            shouldStop = true;
        }
    }

    private static class PageBorder implements Border {

        public Insets getBorderInsets(Component component) {
            return new Insets(1, 1, 2, 2);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width - 2, height - 2);
            g.setColor(Color.GRAY);
            g.drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
            g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 1);
        }
    }

    private static class PrinterGraphicsDelegate extends Graphics2D implements PrinterGraphics {

        private Graphics2D g;

        private PrinterJob printerJob;

        PrinterGraphicsDelegate(Graphics2D g) {
            this(g, null);
        }

        PrinterGraphicsDelegate(Graphics2D g, PrinterJob printerJob) {
            this.g = g;
            this.printerJob = printerJob;
        }

        Graphics getGraphics() {
            return g;
        }

        public PrinterJob getPrinterJob() {
            return printerJob;
        }

        public void addRenderingHints(Map hints) {
            g.addRenderingHints(hints);
        }

        public void clearRect(int arg0, int arg1, int arg2, int arg3) {
            g.clearRect(arg0, arg1, arg2, arg3);
        }

        public void clip(Shape s) {
            g.clip(s);
        }

        public void clipRect(int arg0, int arg1, int arg2, int arg3) {
            g.clipRect(arg0, arg1, arg2, arg3);
        }

        public void copyArea(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            g.copyArea(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public Graphics create() {
            return g.create();
        }

        public Graphics create(int arg0, int arg1, int arg2, int arg3) {
            return g.create(arg0, arg1, arg2, arg3);
        }

        public void dispose() {
            g.dispose();
        }

        public void draw(Shape s) {
            g.draw(s);
        }

        public void draw3DRect(int x, int y, int width, int height, boolean raised) {
            g.draw3DRect(x, y, width, height, raised);
        }

        public void drawArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            g.drawArc(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public void drawBytes(byte[] arg0, int arg1, int arg2, int arg3, int arg4) {
            g.drawBytes(arg0, arg1, arg2, arg3, arg4);
        }

        public void drawChars(char[] arg0, int arg1, int arg2, int arg3, int arg4) {
            g.drawChars(arg0, arg1, arg2, arg3, arg4);
        }

        public void drawGlyphVector(GlyphVector glyphVector, float x, float y) {
            g.drawGlyphVector(glyphVector, x, y);
        }

        public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
            g.drawImage(img, op, x, y);
        }

        public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
            return g.drawImage(img, xform, obs);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, Color arg3, ImageObserver arg4) {
            return g.drawImage(arg0, arg1, arg2, arg3, arg4);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
            return g.drawImage(arg0, arg1, arg2, arg3);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, Color arg5, ImageObserver arg6) {
            return g.drawImage(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, ImageObserver arg5) {
            return g.drawImage(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, Color arg9, ImageObserver arg10) {
            return g.drawImage(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        }

        public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, ImageObserver arg9) {
            return g.drawImage(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
        }

        public void drawLine(int arg0, int arg1, int arg2, int arg3) {
            g.drawLine(arg0, arg1, arg2, arg3);
        }

        public void drawOval(int arg0, int arg1, int arg2, int arg3) {
            g.drawOval(arg0, arg1, arg2, arg3);
        }

        public void drawPolygon(int[] arg0, int[] arg1, int arg2) {
            g.drawPolygon(arg0, arg1, arg2);
        }

        public void drawPolygon(Polygon arg0) {
            g.drawPolygon(arg0);
        }

        public void drawPolyline(int[] arg0, int[] arg1, int arg2) {
            g.drawPolyline(arg0, arg1, arg2);
        }

        public void drawRect(int arg0, int arg1, int arg2, int arg3) {
            g.drawRect(arg0, arg1, arg2, arg3);
        }

        public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
            g.drawRenderableImage(img, xform);
        }

        public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
            g.drawRenderedImage(img, xform);
        }

        public void drawRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            g.drawRoundRect(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public void drawString(AttributedCharacterIterator iterator, float x, float y) {
            g.drawString(iterator, x, y);
        }

        public void drawString(AttributedCharacterIterator iterator, int x, int y) {
            g.drawString(iterator, x, y);
        }

        public void drawString(String s, float x, float y) {
            g.drawString(s, x, y);
        }

        public void drawString(String str, int x, int y) {
            g.drawString(str, x, y);
        }

        public boolean equals(Object arg0) {
            return g.equals(arg0);
        }

        public void fill(Shape s) {
            g.fill(s);
        }

        public void fill3DRect(int x, int y, int width, int height, boolean raised) {
            g.fill3DRect(x, y, width, height, raised);
        }

        public void fillArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            g.fillArc(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public void fillOval(int arg0, int arg1, int arg2, int arg3) {
            g.fillOval(arg0, arg1, arg2, arg3);
        }

        public void fillPolygon(int[] arg0, int[] arg1, int arg2) {
            g.fillPolygon(arg0, arg1, arg2);
        }

        public void fillPolygon(Polygon arg0) {
            g.fillPolygon(arg0);
        }

        public void fillRect(int arg0, int arg1, int arg2, int arg3) {
            g.fillRect(arg0, arg1, arg2, arg3);
        }

        public void fillRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
            g.fillRoundRect(arg0, arg1, arg2, arg3, arg4, arg5);
        }

        public void finalize() {
            g.finalize();
        }

        public Color getBackground() {
            return g.getBackground();
        }

        public Shape getClip() {
            return g.getClip();
        }

        public Rectangle getClipBounds() {
            return g.getClipBounds();
        }

        public Rectangle getClipBounds(Rectangle arg0) {
            return g.getClipBounds(arg0);
        }

        public Rectangle getClipRect() {
            return g.getClipRect();
        }

        public Color getColor() {
            return g.getColor();
        }

        public Composite getComposite() {
            return g.getComposite();
        }

        public GraphicsConfiguration getDeviceConfiguration() {
            return g.getDeviceConfiguration();
        }

        public Font getFont() {
            return g.getFont();
        }

        public FontMetrics getFontMetrics() {
            return g.getFontMetrics();
        }

        public FontMetrics getFontMetrics(Font arg0) {
            return g.getFontMetrics(arg0);
        }

        public FontRenderContext getFontRenderContext() {
            return g.getFontRenderContext();
        }

        public Paint getPaint() {
            return g.getPaint();
        }

        public Object getRenderingHint(Key hintKey) {
            return g.getRenderingHint(hintKey);
        }

        public RenderingHints getRenderingHints() {
            return g.getRenderingHints();
        }

        public Stroke getStroke() {
            return g.getStroke();
        }

        public AffineTransform getTransform() {
            return g.getTransform();
        }

        public int hashCode() {
            return g.hashCode();
        }

        public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
            return g.hit(rect, s, onStroke);
        }

        public boolean hitClip(int arg0, int arg1, int arg2, int arg3) {
            return g.hitClip(arg0, arg1, arg2, arg3);
        }

        public void rotate(double theta, double x, double y) {
            g.rotate(theta, x, y);
        }

        public void rotate(double theta) {
            g.rotate(theta);
        }

        public void scale(double sx, double sy) {
            g.scale(sx, sy);
        }

        public void setBackground(Color color) {
            g.setBackground(color);
        }

        public void setClip(int arg0, int arg1, int arg2, int arg3) {
            g.setClip(arg0, arg1, arg2, arg3);
        }

        public void setClip(Shape arg0) {
            g.setClip(arg0);
        }

        public void setColor(Color arg0) {
            g.setColor(arg0);
        }

        public void setComposite(Composite comp) {
            g.setComposite(comp);
        }

        public void setFont(Font arg0) {
            g.setFont(arg0);
        }

        public void setPaint(Paint paint) {
            g.setPaint(paint);
        }

        public void setPaintMode() {
            g.setPaintMode();
        }

        public void setRenderingHint(Key hintKey, Object hintValue) {
            g.setRenderingHint(hintKey, hintValue);
        }

        public void setRenderingHints(Map hints) {
            g.setRenderingHints(hints);
        }

        public void setStroke(Stroke s) {
            g.setStroke(s);
        }

        public void setTransform(AffineTransform Tx) {
            g.setTransform(Tx);
        }

        public void setXORMode(Color c1) {
            g.setXORMode(c1);
        }

        public void shear(double shx, double shy) {
            g.shear(shx, shy);
        }

        public String toString() {
            return g.toString();
        }

        public void transform(AffineTransform Tx) {
            g.transform(Tx);
        }

        public void translate(double tx, double ty) {
            g.translate(tx, ty);
        }

        public void translate(int x, int y) {
            g.translate(x, y);
        }
    }
}
