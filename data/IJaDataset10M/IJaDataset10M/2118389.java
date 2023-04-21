package javaapplication1;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author craig
 */
public class SSNFHPanel extends JPanel implements MouseListener, MouseMotionListener {

    public static final int BGCOLOUR = 0;

    public static final int TRACECOLOUR = 1;

    public static final int AXISCOLOUR = 6;

    public static final int STANDARDFONT = 0;

    public static final int LARGEFONT = 1;

    public int height, width;

    public int borderpx;

    public double max, min;

    public Color[] colours;

    public SSBoolean flash;

    public SSDataContainer data;

    public Font[] fonts;

    public SSSettings mysettings;

    public int mynfh;

    public int historylength;

    public int first, last;

    public int verticalskew, verticaloffset;

    public int horizontalskew, horizontaloffset;

    public int paintmode;

    public boolean selected = false;

    boolean dragging = false;

    int oldx, oldy;

    public static final int PMICON = 0;

    public static final int PMWATERFALL = 1;

    public static final int PMCOLOURWATERFALL = 2;

    void savesettings(File out) throws FileNotFoundException, IOException {
        ObjectOutputStream objoutstream = null;
        FileOutputStream outstream = null;
        outstream = new FileOutputStream(out);
        objoutstream = new ObjectOutputStream(outstream);
        objoutstream.writeInt(verticalskew);
        objoutstream.writeInt(horizontalskew);
        objoutstream.writeInt(verticaloffset);
        objoutstream.writeInt(horizontaloffset);
        objoutstream.writeInt(mynfh);
        objoutstream.writeInt(historylength);
        objoutstream.writeInt(first);
        objoutstream.writeInt(last);
        objoutstream.close();
    }

    void loadsettings(File in) throws FileNotFoundException, IOException {
        ObjectInputStream objinstream = null;
        FileInputStream instream = null;
        instream = new FileInputStream(in);
        objinstream = new ObjectInputStream(instream);
        verticalskew = objinstream.readInt();
        horizontalskew = objinstream.readInt();
        verticaloffset = objinstream.readInt();
        horizontaloffset = objinstream.readInt();
        mynfh = objinstream.readInt();
        historylength = objinstream.readInt();
        first = objinstream.readInt();
        last = objinstream.readInt();
        objinstream.close();
    }

    public SSNFHPanel(SSDataContainer d, SSSettings s, SSBoolean f, int w, int h) {
        data = d;
        flash = f;
        width = w;
        height = h;
        borderpx = 1;
        mysettings = s;
        historylength = 1;
        first = -1;
        last = -1;
        paintmode = PMICON;
        this.setOpaque(false);
        init();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setSkew(int vs, int vo, int hs, int ho) {
        verticalskew = vs;
        verticaloffset = vo;
        horizontalskew = hs;
        horizontaloffset = ho;
    }

    public void setNFHIndex(int n) {
        mynfh = n;
        this.setToolTipText("<html>" + mysettings.nfhdata[mynfh].name + "<br>line2</html>");
    }

    public void setsize(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    private void init() {
    }

    public void setPallet(Color[] c) {
        colours = c;
    }

    public void setFonts(Font[] f) {
        fonts = f;
    }

    public void setBorderPX(int i) {
        borderpx = i;
    }

    public void setMaxMin(double mx, double mn) {
        max = mx;
        min = mn;
    }

    public void setFirstLast(int f, int l) {
        first = f;
        last = l;
    }

    public void setHistoryLength(int h) {
        historylength = h;
    }

    public Color getspectrumcolour(double val, double mx, double mn) {
        Color colour;
        int r, g, b;
        double norm = 1.0 - (val - mn) / (mx - mn);
        if (norm > 1.0) norm = 1.0;
        if (norm < 0.0) norm = 0.0;
        r = 0;
        g = 0;
        b = 0;
        if (norm < 0.5) {
            r = (int) ((1.0 - norm / 0.5) * 255.0);
            g = (int) ((norm / 0.5) * 255.0);
        } else {
            g = (int) ((1.0 - (norm - 0.5) / 0.5) * 255.0);
            b = (int) (((norm - 0.5) / 0.5) * 255.0);
        }
        colour = new Color(r, g, b);
        return colour;
    }

    public Color getscaledcolour(Color c, double n) {
        Color colour;
        int r, g, b;
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        colour = new Color((int) ((double) r * n), (int) ((double) g * n), (int) ((double) b * n));
        return colour;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        setPallet(mysettings.pallet.colours);
        SSNFHContainer nfh = mysettings.nfhdata[mynfh];
        if (paintmode == PMWATERFALL) {
            plotafloor(g, min, 0, 0, width - 1, height - 1, horizontalskew * (historylength - 1), verticalskew * (historylength - 1), max, min, colours[AXISCOLOUR], 1, false, true);
            if (nfh != null) {
                for (int i = historylength - 1; i >= 0; i--) {
                    Color c = getscaledcolour(colours[TRACECOLOUR], ((1.0 - ((double) i / (double) (historylength - 1))) / 2.0 + 0.5));
                    int xpos, ypos, w, h;
                    xpos = borderpx + i * horizontalskew + horizontaloffset;
                    w = width - borderpx * 2 - (historylength - 1) * horizontalskew;
                    ypos = borderpx + i * verticalskew + verticaloffset;
                    h = height - borderpx * 2;
                    System.err.println("plotatrace(" + i + ")");
                    plotatrace(g, xpos, ypos, w - 1, h - 1, nfh.data[i], first, last, max, min, c, 1, false, false, false);
                }
            }
        } else if (paintmode == PMCOLOURWATERFALL) {
            if (nfh != null) {
                for (int i = historylength - 1; i >= 0; i--) {
                    Color c = getscaledcolour(colours[TRACECOLOUR], ((1.0 - ((double) i / (double) (historylength - 1))) / 2.0 + 0.5));
                    int xpos, ypos, w, h;
                    xpos = 0;
                    w = width - borderpx * 2;
                    ypos = i * (height - borderpx * 2) / historylength;
                    h = (height - borderpx * 2) / historylength + 1;
                    plotacolourtrace(g, xpos, ypos, w, h, nfh.data[i], first, last, max, min, c, 1, false, false, false);
                }
            }
        } else if (paintmode == PMICON) {
            Color c;
            c = Color.red;
            if (flash.b == true) plotahydrophoneicon(g, 0, 0, width, height, c, nfh.name);
        }
    }

    public void plotatrace(Graphics g, int xpos, int ypos, int w, int h, double[] data, int first, int last, double max, double min, Color c, int thickness, Boolean crop, Boolean negfill, Boolean posfill) {
        int i, t, yz;
        int[] xPoints;
        int[] yPoints;
        xPoints = new int[4];
        yPoints = new int[4];
        if (first < 0) first = 0;
        if (last == -1 || last > (data.length - 1)) last = data.length - 1;
        g.setColor(c);
        double winterval = ((double) w / ((double) (last - first)));
        yz = -(int) ((0.0 - min) / (max - min) * (double) h) + h + ypos;
        if (crop) {
            if (yz > ypos + h) yz = ypos + h; else if (yz < ypos) yz = ypos;
        }
        yPoints[0] = yz;
        yPoints[3] = yz;
        for (i = first; i < last; i++) {
            xPoints[1] = (int) ((double) (i - first) * winterval) + xpos;
            xPoints[2] = (int) ((double) (i + 1 - first) * winterval) + xpos;
            xPoints[0] = xPoints[1];
            xPoints[3] = xPoints[2];
            yPoints[1] = -(int) ((data[i] - min) / (max - min) * (double) h) + h + ypos;
            yPoints[2] = -(int) ((data[i + 1] - min) / (max - min) * (double) h) + h + ypos;
            if (crop) {
                for (t = 0; t < 4; t++) {
                    if (yPoints[t] > ypos + h) yPoints[t] = ypos + h;
                    if (yPoints[t] < ypos) yPoints[t] = ypos;
                }
            }
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
            if (negfill) {
                if (data[i] < 0.0 && data[i + 1] < 0.0) g.fillPolygon(xPoints, yPoints, 4);
            }
            if (posfill) {
                if (data[i] > 0.0 && data[i + 1] > 0.0) g.fillPolygon(xPoints, yPoints, 4);
            }
        }
    }

    public void plotacolourtrace(Graphics g, int xpos, int ypos, int w, int h, double[] data, int first, int last, double max, double min, Color c, int thickness, Boolean crop, Boolean negfill, Boolean posfill) {
        int i, t, yz;
        int[] xPoints;
        int[] yPoints;
        xPoints = new int[4];
        yPoints = new int[4];
        if (first < 0) first = 0;
        if (last == -1 || last > (data.length - 1)) last = data.length - 1;
        g.setColor(c);
        double winterval = ((double) w / ((double) (last - first)));
        for (i = first; i < last; i++) {
            xPoints[0] = (int) ((double) (i - first) * winterval) + xpos;
            xPoints[1] = xPoints[0];
            xPoints[2] = (int) ((double) (i + 1 - first) * winterval) + xpos;
            xPoints[3] = xPoints[2];
            yPoints[0] = ypos + h;
            yPoints[1] = ypos;
            yPoints[2] = ypos;
            yPoints[3] = ypos + h;
            g.setColor(getspectrumcolour(data[i], max, min));
            g.fillPolygon(xPoints, yPoints, 4);
        }
    }

    public void plotafloor(Graphics g, double level, int xpos, int ypos, int w, int h, int hskew, int vskew, double max, double min, Color c, int thickness, Boolean fill, Boolean corners) {
        int[] xPoints;
        int[] yPoints;
        xPoints = new int[4];
        yPoints = new int[4];
        g.setColor(c);
        int yz = -(int) ((level - min) / (max - min) * (double) h) + h + ypos;
        xPoints[0] = xpos;
        yPoints[0] = yz;
        xPoints[1] = xpos + hskew;
        yPoints[1] = yz + vskew;
        xPoints[2] = xpos + w;
        yPoints[2] = yz + vskew;
        xPoints[3] = xpos + w - hskew;
        yPoints[3] = yz;
        if (fill) g.fillPolygon(xPoints, yPoints, 4); else g.drawPolygon(xPoints, yPoints, 4);
        if (corners) g.drawLine(xPoints[1], yPoints[1], xPoints[1], ypos);
    }

    public void plotahydrophonevectoricon(Graphics g, int xpos, int ypos, int w, int h, Color c, String s) {
        FontMetrics fm = g.getFontMetrics(fonts[STANDARDFONT]);
        java.awt.geom.Rectangle2D rect;
        int textHeight;
        int textWidth;
        g.setFont(fonts[STANDARDFONT]);
        int xc = xpos + w / 2;
        int yc = ypos + h / 2;
        g.setColor(c);
        g.drawOval(xc - 3, yc - 25, 6, 6);
        g.drawOval(xc - 3, yc + 19, 6, 6);
        g.drawLine(xc, yc - 19, xc, yc - 10);
        g.drawLine(xc, yc + 19, xc, yc + 10);
        g.drawLine(xc - 20, yc - 10, xc + 20, yc - 10);
        g.drawLine(xc - 20, yc + 10, xc + 20, yc + 10);
        g.fillRect(xc - 20, yc - 7, 40, 15);
        g.setColor(Color.black);
        rect = fm.getStringBounds(s, g);
        textHeight = (int) (rect.getHeight());
        textWidth = (int) (rect.getWidth());
        g.drawString(s, xc - textWidth / 2, yc + textHeight / 2 - 2);
    }

    ;

    public void plotahydrophoneicon(Graphics g, int xpos, int ypos, int w, int h, Color c, String s) {
        g.drawImage(mysettings.HydShadowImage, xpos, ypos, 32, 32, this);
        g.drawImage(mysettings.HydImage, xpos, ypos, 32, 32, this);
        if (mysettings.nfhdata[mynfh].state == SSNFHContainer.STATEOK) {
            g.drawImage(mysettings.HydOkImage, xpos, ypos, 32, 32, this);
        }
        if (mysettings.nfhdata[mynfh].state == SSNFHContainer.STATEWARNING) {
            g.drawImage(mysettings.HydWarningImage, xpos, ypos, 32, 32, this);
        }
        if (mysettings.nfhdata[mynfh].state == SSNFHContainer.STATEALARM) {
            g.drawImage(mysettings.HydAlarmImage, xpos, ypos, 32, 32, this);
        }
        FontMetrics fm = g.getFontMetrics(fonts[STANDARDFONT]);
        java.awt.geom.Rectangle2D rect;
        int textHeight;
        int textWidth;
        g.setFont(fonts[STANDARDFONT]);
        g.setColor(Color.black);
        rect = fm.getStringBounds(s, g);
        textHeight = (int) (rect.getHeight());
        textWidth = (int) (rect.getWidth());
        int xc = xpos + w / 2;
        int yc = ypos + h / 2;
        g.drawString(s, xc - textWidth / 2, yc + textHeight / 2 - 2);
        if (selected) {
            g.setColor(Color.RED);
            g.drawRect(xpos, ypos, 31, 31);
        }
    }

    ;

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            int mx = e.getX();
            int my = e.getY();
            selected = !selected;
            repaint();
        }
        if (e.getClickCount() == 2) {
            JFrame frame = new JFrame(mysettings.nfhdata[mynfh].name);
            SSNFHPanel mypanel = new SSNFHPanel(data, mysettings, flash, 400, 400);
            mypanel.setPallet(colours);
            mypanel.setBounds(0, 0, 400, 400);
            mypanel.setLocation(0, 0);
            mypanel.setBackground(Color.black);
            mypanel.setBorderPX(1);
            mypanel.setFonts(fonts);
            mypanel.setFirstLast(-1, 100);
            mypanel.setHistoryLength(100);
            mypanel.setMaxMin(0.5, -0.5);
            mypanel.setNFHIndex(mynfh);
            mypanel.setSkew(-5, 0, 10, 0);
            mypanel.paintmode = SSNFHPanel.PMCOLOURWATERFALL;
            frame.getContentPane().add(mypanel);
            SSNFHPanel mypanel2 = new SSNFHPanel(data, mysettings, flash, 400, 400);
            mypanel2.setPallet(colours);
            mypanel2.setBounds(401, 0, 400, 400);
            mypanel2.setLocation(400, 0);
            mypanel2.setBackground(Color.black);
            mypanel2.setBorderPX(1);
            mypanel2.setFonts(fonts);
            mypanel2.setFirstLast(-1, 100);
            mypanel2.setHistoryLength(10);
            mypanel2.setMaxMin(0.5, -0.5);
            mypanel2.setNFHIndex(mynfh);
            mypanel2.setSkew(-5, 0, 10, 0);
            mypanel2.paintmode = SSNFHPanel.PMWATERFALL;
            frame.getContentPane().add(mypanel2);
            frame.pack();
            frame.setVisible(true);
            frame.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        dragging = false;
        e.getComponent().getParent().repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (dragging == true) {
            Component c = e.getComponent();
            System.out.print("x=" + e.getX() + " y=" + e.getY());
            c.setLocation(mysettings.getsnappedX(c.getX() + e.getX() - oldx), mysettings.getsnappedY(c.getY() + e.getY() - oldy));
            repaint();
        } else {
            oldx = e.getX();
            oldy = e.getY();
            dragging = true;
        }
    }

    public void mouseMoved(MouseEvent e) {
    }
}
