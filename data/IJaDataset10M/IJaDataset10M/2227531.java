package aidc.aigui.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author vboos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchematicCanvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private int xmin;

    private int ymin;

    private int xmax;

    private int ymax;

    private Rectangle selbox;

    private GraphicsObject hilite;

    private GraphicsObject selection;

    static final int PERCENT_GROW_FIT = 1;

    public GraphicsContainer cont;

    AffineTransform afnScreen;

    public double frq;

    static final AffineTransform stdafn[] = { new AffineTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0), new AffineTransform(0.0, 1.0, -1.0, 0.0, 0.0, 0.0), new AffineTransform(-1.0, 0.0, 0.0, -1.0, 0.0, 0.0), new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, 0.0), new AffineTransform(-1.0, 0.0, 0.0, 1.0, 0.0, 0.0), new AffineTransform(0.0, -1.0, -1.0, 0.0, 0.0, 0.0), new AffineTransform(1.0, 0.0, 0.0, -1.0, 0.0, 0.0), new AffineTransform(0.0, 1.0, 1.0, 0.0, 0.0, 0.0) };

    GraphicsContainer cvBase = null;

    public SchematicCanvas() {
        super();
        setBackground(Color.BLACK);
        setFocusable(true);
        afnScreen = new AffineTransform();
        cont = new GraphicsContainer();
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                System.out.println("Schematic resized");
                Fit();
            }
        });
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
    }

    public void SetSize(int xmin, int ymin, int xmax, int ymax) {
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform afnSave = g2.getTransform();
        g2.transform(afnScreen);
        g2.setStroke(new BasicStroke(0.0f));
        g2.setColor(Color.GREEN);
        cont.paint(g2, null);
        g2.setTransform(afnSave);
        if (selbox != null) {
            g.setColor(Color.WHITE);
            g.drawRect(selbox.x, selbox.y, selbox.width, selbox.height);
        }
    }

    /**
	 * Set viewport to rectangle in world coordinates
	 * @param rw Rectangle in world coordinates
	 */
    public void setViewport(Rectangle rw) {
        int cw = getSize().width;
        int ch = getSize().height;
        double zoomXratio = (double) cw / (double) rw.width;
        double zoomYratio = (double) ch / (double) rw.height;
        if (zoomXratio > zoomYratio) {
            ymin = rw.y;
            ymax = ymin + rw.height;
            int w = (int) ((double) cw / zoomYratio);
            xmin = (int) (rw.x - (double) ((w - rw.width) * 0.5));
            xmax = xmin + w;
            zoomXratio = zoomYratio;
        } else {
            xmin = rw.x;
            xmax = xmin + rw.width;
            int h = (int) ((double) ch / zoomXratio);
            ymin = (int) (rw.y - (double) ((h - rw.height) * 0.5));
            ymax = ymin + h;
            zoomYratio = zoomXratio;
        }
        afnScreen = new AffineTransform(zoomXratio, 0.0, 0.0, -zoomYratio, -zoomXratio * xmin, zoomYratio * ymax);
        System.out.print("cw = ");
        System.out.print(cw);
        System.out.print(", ");
        System.out.print("ch = ");
        System.out.println(ch);
        System.out.print("(xmin,ymin) = (");
        System.out.print(xmin);
        System.out.print(",");
        System.out.print(ymin);
        System.out.print("), ");
        System.out.print("(xmax,ymax) = (");
        System.out.print(xmax);
        System.out.print(",");
        System.out.print(ymax);
        System.out.println(")");
        System.out.print("afnScreen = ");
        System.out.println(afnScreen.toString());
        repaint();
    }

    /**
	 * Fits the screen to object's size
	 * 
	 * @author vboos
	 */
    public void Fit() {
        Rectangle rw = new Rectangle(cont.getBounds());
        int h = (rw.width * PERCENT_GROW_FIT + 50) / 100;
        int v = (rw.height * PERCENT_GROW_FIT + 50) / 100;
        rw.grow(h, v);
        setViewport(rw);
    }

    public void mousePressed(MouseEvent e) {
        saySomething("Mouse pressed; # of clicks: " + e.getClickCount(), e);
    }

    public void mouseReleased(MouseEvent e) {
        saySomething("Mouse released; # of clicks: " + e.getClickCount(), e);
    }

    public void mouseEntered(MouseEvent e) {
        saySomething("Mouse entered", e);
    }

    public void mouseExited(MouseEvent e) {
        saySomething("Mouse exited", e);
    }

    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
        if (selection == null) selection = hilite; else {
            selection = null;
            mouseMoved(e);
        }
        saySomething("Mouse clicked (# of clicks: " + e.getClickCount() + ")", e);
    }

    void saySomething(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription + " detected on " + e.getComponent().getClass().getName() + ".");
    }

    private static final long serialVersionUID = 1L;

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        Point src = e.getPoint();
        if (selection != null) return;
        try {
            GraphicsObject newsel = null;
            Point2D dest = afnScreen.inverseTransform(src, null);
            int x = (int) dest.getX();
            int y = (int) dest.getY();
            for (int i = 0; i < cont.objectList.size(); i++) {
                GraphicsObject go = (GraphicsObject) cont.objectList.get(i);
                Rectangle bbox = go.getBounds();
                if (bbox.x <= x && x <= bbox.x + bbox.width && bbox.y <= y && y <= bbox.y + bbox.height) {
                    newsel = go;
                }
            }
            if (hilite != newsel) {
                if (hilite != null) {
                    Rectangle r = selbox;
                    selbox = null;
                    if (r != null) paintImmediately(r.x, r.y, r.width + 1, r.height + 1);
                }
                if (newsel != null) {
                    Shape s = new Rectangle(newsel.getBounds());
                    Rectangle r = afnScreen.createTransformedShape(s).getBounds();
                    selbox = r;
                    paintImmediately(r.x, r.y, r.width + 1, r.height + 1);
                    System.out.println(r.toString());
                } else selbox = null;
                hilite = newsel;
                if (hilite != null) {
                    System.out.println("Selected: " + hilite.toString());
                } else System.out.println("Selected: nothing");
            }
        } catch (NoninvertibleTransformException ex) {
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'e' || c == 'E') {
            if (selection != null && selection instanceof CdgInstance) {
                CdgInstance inst = (CdgInstance) selection;
                System.out.print("instance " + inst.toString());
                cvBase = cont;
                cont = inst.cont;
                selection = null;
                hilite = null;
                selbox = null;
                Fit();
                repaint();
            }
        }
        if (c == 'b' || c == 'B') {
            if (cvBase != null) {
                cont = cvBase;
                cvBase = null;
                selbox = null;
                Fit();
                repaint();
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
    }
}
