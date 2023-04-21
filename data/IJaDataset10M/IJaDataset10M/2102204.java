package org.gaugebook.gauges;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.gaugebook.AbstractGauge;
import org.gaugebook.GaugeBook;
import org.gaugebook.LED;
import org.gaugebook.math.Angle;
import com.sun.corba.se.impl.ior.ByteBuffer;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

public class ChartViewerGauge extends AbstractGauge implements KeyListener {

    private String[] fkeylabels = { "OPEN", "PREV", "NEXT", "WIDTH", "PAGE", "ZOOM+", "ZOOM-", "ROTL", "ROTR", "", "", "" };

    private String[] fkeyevents = { null, null, null, null, null, null, null, null, null, null, null, null };

    private boolean[] fkeyleds = { false, false, false, true, true, false, false, false, false, false, false, false };

    public String[] getFkeylabels() {
        return fkeylabels;
    }

    public String[] getFkeyevents() {
        return fkeyevents;
    }

    public boolean[] getFkeyleds() {
        return fkeyleds;
    }

    PDFRenderer renderer;

    PDFFile pdffile;

    PDFPage page;

    private int lastWidth = 0;

    private int lastHeight = 0;

    private int pagenum = 0;

    Angle rotateAngle = new Angle(0);

    boolean fileChanged = false;

    private int dy = 0;

    public ChartViewerGauge() {
        super();
        loadPDF("welcome_DO_NOT_DELETE_chartviewergauge.pdf");
    }

    protected void loadPDF(File file) {
        RandomAccessFile raf = null;
        MappedByteBuffer buf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            pdffile = new PDFFile(buf);
            fileChanged = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadPDF(String name) {
        File file = new File("charts/" + name);
        loadPDF(file);
    }

    int lastPagenum = -1;

    int lastRotation = 0;

    int lastDy = 0;

    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (pdffile == null) return;
        if (lastWidth != w || lastHeight != h || lastDy != dy || fileChanged || pagenum != lastPagenum || rotateAngle.getDegrees() != lastRotation || vi == null || vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            fileChanged = false;
            lastPagenum = pagenum;
            lastDy = dy;
            lastRotation = (int) rotateAngle.getDegrees();
            page = pdffile.getPage(pagenum);
            AffineTransform at = new AffineTransform();
            at.scale(w / page.getWidth(), h / page.getHeight());
            page.addXform(at);
            int pw = (int) page.getBBox().getWidth();
            int ph = (int) page.getBBox().getHeight();
            if (pw < w) {
                pw = w;
            }
            int k = Math.max(pw, ph);
            do {
                gc = getGraphicsConfiguration();
                if (vi == null || vi.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                    vi = gc.createCompatibleVolatileImage(k, k);
                }
                Graphics2D g2 = vi.createGraphics();
                g2.setColor(Color.white);
                g2.fillRect(0, 0, k, k);
                System.out.println("Pagenum: " + page.getPageNumber() + " Rotation: " + page.getRotation() + " RotAngle=" + rotateAngle.getRadians() + " PageRotation: " + page.getRotation() + " w=" + w + " h=" + h + " pw=" + pw + " ph=" + ph);
                Rectangle rect = new Rectangle(0, 0, w, ph);
                Rectangle clip = new Rectangle(0, 0, w, h);
                try {
                    page.waitForFinish();
                } catch (InterruptedException e) {
                }
                Image img = page.getImage(rect.width, rect.height, clip, null, true, true);
                switch((int) rotateAngle.getDegrees()) {
                    case 90:
                        at = new AffineTransform();
                        at.rotate(rotateAngle.getRadians(), 0, 0);
                        at.translate(0, -w);
                        at.scale(h / page.getWidth(), w / page.getHeight());
                        g2.setTransform(at);
                        g2.drawImage(img, 0, 0, w, h, 0, dy, w, h + dy, this);
                        break;
                    case 180:
                        at = new AffineTransform();
                        at.rotate(rotateAngle.getRadians(), 0, 0);
                        at.translate(-w, -h);
                        g2.setTransform(at);
                        g2.drawImage(img, 0, 0, w, h, 0, dy, w, h + dy, this);
                        break;
                    case 270:
                        at = new AffineTransform();
                        at.rotate(rotateAngle.getRadians(), 0, 0);
                        at.translate(-h, 0);
                        at.scale(h / page.getWidth(), w / page.getHeight());
                        g2.setTransform(at);
                        g2.drawImage(img, 0, 0, w, h, 0, dy, w, h + dy, this);
                        break;
                    default:
                        g2.drawImage(img, 0, 0, w, h, 0, dy, w, h + dy, this);
                }
                g2.dispose();
            } while (vi.contentsLost());
        }
        g.drawImage(vi, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isAltDown()) return;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_F1:
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setCurrentDirectory(new File(owner.getPdfPath()));
                fileDialog.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        String ext = null;
                        String s = f.getName();
                        int i = s.lastIndexOf('.');
                        if (i > 0 && i < s.length() - 1) {
                            ext = s.substring(i + 1).toLowerCase();
                        }
                        return f.isDirectory() || ext != null && ext.equals("pdf");
                    }

                    public String getDescription() {
                        return "PDF Charts (*.pdf)";
                    }
                });
                fileDialog.setAcceptAllFileFilterUsed(false);
                int returnVal = fileDialog.showOpenDialog(owner);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileDialog.getSelectedFile();
                    loadPDF(file);
                }
                e.consume();
                break;
            case KeyEvent.VK_F2:
                if (pdffile != null && pdffile.getNumPages() > 1) {
                    if (pagenum >= 0) pagenum--; else pagenum = pdffile.getNumPages() - 1;
                }
                e.consume();
                break;
            case KeyEvent.VK_F3:
                if (pdffile != null && pdffile.getNumPages() > 1) {
                    if (pagenum < pdffile.getNumPages()) pagenum++; else pagenum = 0;
                }
                e.consume();
                break;
            case KeyEvent.VK_F4:
                e.consume();
                break;
            case KeyEvent.VK_F5:
                e.consume();
                break;
            case KeyEvent.VK_F6:
                rotateAngle = new Angle(rotateAngle.getDegrees() - 90);
                dy = 0;
                e.consume();
                break;
            case KeyEvent.VK_F7:
                rotateAngle = new Angle(rotateAngle.getDegrees() + 90);
                dy = 0;
                e.consume();
                break;
            case KeyEvent.VK_F8:
                e.consume();
                break;
            case KeyEvent.VK_F9:
                e.consume();
                break;
            case KeyEvent.VK_F10:
                e.consume();
                break;
            case KeyEvent.VK_F11:
                e.consume();
                break;
            case KeyEvent.VK_F12:
                e.consume();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if ((int) rotateAngle.getDegrees() == 0 && dy < page.getBBox().getHeight() - getHeight()) {
                    dy += 40;
                }
                e.consume();
                break;
            case KeyEvent.VK_PAGE_UP:
                if ((int) rotateAngle.getDegrees() == 0 && dy >= 40) {
                    dy -= 40;
                }
                ;
                e.consume();
                break;
            default:
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
