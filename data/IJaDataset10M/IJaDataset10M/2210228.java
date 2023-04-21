package org.tn5250j;

import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import java.awt.Font;

public class GuiGraphicBuffer {

    private BufferedImage bi;

    private final Object lock = new Object();

    private Line2D separatorLine = new Line2D.Float();

    private Rectangle2D tArea;

    private Rectangle2D aArea;

    private Rectangle2D cArea;

    private Rectangle2D sArea;

    private Rectangle2D pArea;

    private Rectangle2D mArea;

    private Rectangle2D iArea;

    private int width;

    private int height;

    private Rectangle2D cursor = new Rectangle2D.Float();

    public GuiGraphicBuffer() {
        tArea = new Rectangle2D.Float();
        cArea = new Rectangle2D.Float();
        aArea = new Rectangle2D.Float();
        sArea = new Rectangle2D.Float();
        pArea = new Rectangle2D.Float();
        mArea = new Rectangle2D.Float();
        iArea = new Rectangle2D.Float();
    }

    public void resize(int width, int height) {
        if (bi.getWidth() != width || bi.getHeight() != height) {
            synchronized (lock) {
                bi = null;
                bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                this.width = width;
                this.height = height;
                lock.notify();
            }
        }
    }

    public BufferedImage getImageBuffer(int width, int height) {
        synchronized (lock) {
            if (bi == null || bi.getWidth() != width || bi.getHeight() != height) bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            this.width = width;
            this.height = height;
            lock.notify();
        }
        return bi;
    }

    public BufferedImage getImageBuffer() {
        return bi;
    }

    /**
    * Draw the operator information area
    */
    public Graphics2D drawOIA(int fmWidth, int fmHeight, int numRows, int numCols, Font font, Color colorBg, Color colorBlue) {
        Graphics2D g2d;
        g2d = (Graphics2D) bi.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setFont(font);
        g2d.setColor(colorBg);
        g2d.fillRect(0, 0, bi.getWidth(null), bi.getHeight(null));
        tArea.setRect(0, 0, bi.getWidth(null), (fmHeight * (numRows)));
        cArea.setRect(0, fmHeight * (numRows + 1), bi.getWidth(null), fmHeight * (numRows + 1));
        aArea.setRect(0, 0, bi.getWidth(null), bi.getHeight(null));
        sArea.setRect(fmWidth * 9, fmHeight * (numRows + 1), fmWidth * 20, fmHeight);
        pArea.setRect(bi.getWidth(null) - fmWidth * 6, fmHeight * (numRows + 1), fmWidth * 6, fmHeight);
        mArea.setRect((float) (sArea.getX() + sArea.getWidth()) + fmWidth + fmWidth, fmHeight * (numRows + 1), fmWidth + fmWidth, fmHeight);
        separatorLine.setLine(0, (fmHeight * (numRows + 1)) - (fmHeight / 2), bi.getWidth(null), (fmHeight * (numRows + 1)) - (fmHeight / 2));
        g2d.setColor(colorBlue);
        g2d.draw(separatorLine);
        return g2d;
    }

    public void drawCursor(Screen5250 s, int row, int col, int fmWidth, int fmHeight, boolean insertMode, int crossHair, int cursorSize, Color colorCursor, Color colorBg, Color colorWhite, Font font) {
        Graphics2D g2 = getDrawingArea();
        switch(cursorSize) {
            case 0:
                cursor.setRect(fmWidth * (col), (fmHeight * (row + 1)), fmWidth, 1);
                break;
            case 1:
                cursor.setRect(fmWidth * (col), (fmHeight * (row + 1) - fmHeight / 2), fmWidth, fmHeight / 2);
                break;
            case 2:
                cursor.setRect(fmWidth * (col), (fmHeight * row), fmWidth, fmHeight);
                break;
        }
        if (insertMode && cursorSize != 1) {
            cursor.setRect(fmWidth * (col), (fmHeight * (row + 1) - fmHeight / 2), fmWidth, fmHeight / 2);
        }
        Rectangle r = cursor.getBounds();
        r.setSize(r.width, r.height);
        g2.setColor(colorCursor);
        g2.setXORMode(colorBg);
        g2.fill(cursor);
        s.updateImage(r);
        switch(crossHair) {
            case 1:
                g2.drawLine(0, fmHeight * (row + 1), bi.getWidth(null), fmHeight * (row + 1));
                s.updateImage(0, fmHeight * (row + 1), bi.getWidth(null), 1);
                break;
            case 2:
                g2.drawLine(r.x, 0, r.x, bi.getHeight(null) - fmHeight - fmHeight);
                s.updateImage(r.x, 0, 1, bi.getHeight(null) - fmHeight - fmHeight);
                break;
            case 3:
                g2.drawLine(0, fmHeight * (row + 1), bi.getWidth(null), fmHeight * (row + 1));
                g2.drawLine(r.x, 0, r.x, bi.getHeight(null) - fmHeight - fmHeight);
                s.updateImage(0, fmHeight * (row + 1), bi.getWidth(null), 1);
                s.updateImage(r.x, 0, 1, bi.getHeight(null) - fmHeight - fmHeight);
                break;
        }
        g2.dispose();
        g2 = getWritingArea(font);
        g2.setPaint(colorBg);
        g2.fill(pArea);
        g2.setColor(colorWhite);
        g2.drawString((row + 1) + "/" + (col + 1), (float) pArea.getX(), (float) pArea.getY() + fmHeight);
        s.updateImage(pArea.getBounds());
        g2.dispose();
    }

    /**
    * Returns a pointer to the graphics area that we can draw on
    */
    public Graphics2D getDrawingArea() {
        try {
            synchronized (lock) {
                while (bi == null) lock.wait();
                Graphics2D g2;
                g2 = bi.createGraphics();
                return g2;
            }
        } catch (InterruptedException ie) {
            System.out.println("getDrawingarea : " + ie.getMessage());
            return null;
        }
    }

    public void drawImageBuffer(Graphics2D gg2d, int x, int y, int width, int height) {
        synchronized (lock) {
            gg2d.drawImage(bi.getSubimage(x, y, width, height), null, x, y);
        }
    }

    public void drawImageBuffer(Graphics2D gg2d) {
        synchronized (lock) {
            gg2d.drawImage(bi, null, 0, 0);
        }
    }

    /**
    * Returns a pointer to the graphics area that we can write on
    */
    public Graphics2D getWritingArea(Font font) {
        Graphics2D g2;
        try {
            synchronized (lock) {
                while (bi == null) lock.wait();
                g2 = bi.createGraphics();
                if (g2 != null) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                    g2.setFont(font);
                }
                return g2;
            }
        } catch (InterruptedException ie) {
            System.out.println("getWritingarea : " + ie.getMessage());
            return null;
        }
    }

    /**
    * get the
    */
    public Rectangle2D getTextArea() {
        return tArea;
    }

    public Rectangle2D getScreenArea() {
        return aArea;
    }

    public Rectangle2D getCommandLineArea() {
        return cArea;
    }

    public Rectangle2D getStatusArea() {
        return sArea;
    }

    public Rectangle2D getPositionArea() {
        return pArea;
    }

    public Rectangle2D getMessageArea() {
        return mArea;
    }

    public Rectangle2D getInsertIndicatorArea() {
        return iArea;
    }

    public int getWidth() {
        synchronized (lock) {
            return bi.getWidth();
        }
    }

    public int getHeight() {
        synchronized (lock) {
            return bi.getHeight();
        }
    }

    public int getWidth(ImageObserver io) {
        synchronized (lock) {
            return bi.getWidth(io);
        }
    }

    public int getHeight(ImageObserver io) {
        synchronized (lock) {
            return bi.getHeight(io);
        }
    }
}
