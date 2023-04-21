package pogvue.gui;

import pogvue.datamodel.SequenceI;
import pogvue.datamodel.SequencePoint;
import pogvue.gui.event.RubberbandEvent;
import pogvue.gui.event.RubberbandListener;
import pogvue.gui.event.SequenceSelectionEvent;
import pogvue.gui.event.SequenceSelectionListener;
import pogvue.math.RotatableMatrix;
import pogvue.util.Format;
import pogvue.datamodel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public final class RotatableCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener, RubberbandListener, SequenceSelectionListener {

    private final RotatableMatrix idmat = new RotatableMatrix(3, 3);

    private final RotatableMatrix objmat = new RotatableMatrix(3, 3);

    final RotatableMatrix rotmat = new RotatableMatrix(3, 3);

    private final RubberbandRectangle rubberband;

    private boolean redrawneeded = true;

    private final boolean drawAxes = true;

    private int omx = 0;

    private int mx = 0;

    private int omy = 0;

    private int my = 0;

    Image img;

    private Graphics ig;

    private final Dimension prefsize;

    private final float[] centre = new float[3];

    private final float[] width = new float[3];

    private float[] max = new float[3];

    private float[] min = new float[3];

    private float maxwidth;

    private float scale;

    private final int npoint;

    final Vector points;

    private final float[][] orig;

    private final float[][] axes;

    private final Object parent;

    private int startx;

    private int starty;

    int lastx;

    int lasty;

    private int rectx1;

    private int recty1;

    private int rectx2;

    private int recty2;

    private float scalefactor = 1;

    private final AlignViewport av;

    private final Controller controller;

    public RotatableCanvas(Object parent, AlignViewport av, Controller c, Vector points, int npoint) {
        this.parent = parent;
        this.points = points;
        this.npoint = npoint;
        this.controller = c;
        this.av = av;
        controller.addListener(this);
        prefsize = getPreferredSize();
        orig = new float[npoint][3];
        for (int i = 0; i < npoint; i++) {
            SequencePoint sp = (SequencePoint) points.elementAt(i);
            System.arraycopy(sp.coord, 0, orig[i], 0, 3);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j) {
                    idmat.addElement(i, j, 0);
                    objmat.addElement(i, j, 0);
                    rotmat.addElement(i, j, 0);
                } else {
                    idmat.addElement(i, j, 0);
                    objmat.addElement(i, j, 0);
                    rotmat.addElement(i, j, 0);
                }
            }
        }
        axes = new float[3][3];
        initAxes();
        findCentre();
        findWidth();
        scale = findScale();
        addMouseListener(this);
        addKeyListener(this);
        if (getParent() != null) {
            getParent().addKeyListener(this);
        }
        addMouseMotionListener(this);
        rubberband = new RubberbandRectangle(this);
        rubberband.setActive(true);
        rubberband.addListener(this);
    }

    public boolean handleSequenceSelectionEvent(SequenceSelectionEvent evt) {
        redrawneeded = true;
        repaint();
        return true;
    }

    public void removeNotify() {
        controller.removeListener(this);
        super.removeNotify();
    }

    public void initAxes() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j) {
                    axes[i][j] = 0;
                } else {
                    axes[i][j] = 1;
                }
            }
        }
    }

    private void findWidth() {
        max = new float[3];
        min = new float[3];
        max[0] = (float) -1e30;
        max[1] = (float) -1e30;
        max[2] = (float) -1e30;
        min[0] = (float) 1e30;
        min[1] = (float) 1e30;
        min[2] = (float) 1e30;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < npoint; j++) {
                SequencePoint sp = (SequencePoint) points.elementAt(j);
                if (sp.coord[i] >= max[i]) {
                    max[i] = sp.coord[i];
                }
                if (sp.coord[i] <= min[i]) {
                    min[i] = sp.coord[i];
                }
            }
        }
        width[0] = Math.abs(max[0] - min[0]);
        width[1] = Math.abs(max[1] - min[1]);
        width[2] = Math.abs(max[2] - min[2]);
        maxwidth = width[0];
        if (width[1] > width[0]) maxwidth = width[1];
        if (width[2] > width[1]) maxwidth = width[2];
    }

    private float findScale() {
        int dim, width, height;
        if (size().width != 0) {
            width = size().width;
            height = size().height;
        } else {
            width = prefsize.width;
            height = prefsize.height;
        }
        if (width < height) {
            dim = width;
        } else {
            dim = height;
        }
        return dim * scalefactor / (2 * maxwidth);
    }

    private void findCentre() {
        findWidth();
        centre[0] = (max[0] + min[0]) / 2;
        centre[1] = (max[1] + min[1]) / 2;
        centre[2] = (max[2] + min[2]) / 2;
    }

    public Dimension getPreferredSize() {
        if (prefsize != null) {
            return prefsize;
        } else {
            return new Dimension(400, 400);
        }
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public void paint(Graphics g) {
        if ((img == null) || (prefsize.width != size().width) || (prefsize.height != size().height)) {
            prefsize.width = size().width;
            prefsize.height = size().height;
            scale = findScale();
            img = createImage(size().width, size().height);
            ig = img.getGraphics();
            redrawneeded = true;
        }
        if (redrawneeded) {
            drawBackground(ig, Color.black);
            drawScene(ig);
            if (drawAxes) {
                drawAxes(ig);
            }
            redrawneeded = false;
        } else {
            ig = img.getGraphics();
        }
        g.drawImage(img, 0, 0, this);
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.yellow);
        for (int i = 0; i < 3; i++) {
            g.drawLine(size().width / 2, size().height / 2, (int) (axes[i][0] * scale * max[0] + size().width / 2), (int) (axes[i][1] * scale * max[1] + size().height / 2));
        }
    }

    private void drawBackground(Graphics g, Color col) {
        g.setColor(col);
        g.fillRect(0, 0, prefsize.width, prefsize.height);
    }

    private void drawScene(Graphics g) {
        boolean darker = false;
        int halfwidth = size().width / 2;
        int halfheight = size().height / 2;
        for (int i = 0; i < npoint; i++) {
            SequencePoint sp = (SequencePoint) points.elementAt(i);
            int x = (int) ((float) (sp.coord[0] - centre[0]) * scale) + halfwidth;
            int y = (int) ((float) (sp.coord[1] - centre[1]) * scale) + halfheight;
            float z = sp.coord[1] - centre[2];
            if (sp.sequence instanceof Sequence) {
            } else {
                g.setColor(Color.red);
            }
            if (av != null) {
                if (av.getSelection().contains(((SequencePoint) points.elementAt(i)).sequence)) {
                    g.setColor(Color.gray);
                }
            }
            if (z < 0) {
                g.setColor(g.getColor().darker());
            }
            g.fillRect(x - 3, y - 3, 6, 6);
            g.setColor(Color.red);
        }
    }

    public Dimension minimumsize() {
        return prefsize;
    }

    public Dimension preferredsize() {
        return prefsize;
    }

    public void keyTyped(KeyEvent evt) {
    }

    public void keyReleased(KeyEvent evt) {
    }

    public void keyPressed(KeyEvent evt) {
        requestFocus();
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            scalefactor = (float) (scalefactor * 1.1);
            scale = findScale();
            redrawneeded = true;
            repaint();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            scalefactor = (float) (scalefactor * 0.9);
            scale = findScale();
            redrawneeded = true;
            repaint();
        } else if (evt.getKeyChar() == 's') {
            System.out.println("Rectangle selection");
            if (rectx2 != -1 && recty2 != -1) {
                rectSelect(rectx1, recty1, rectx2, recty2);
                redrawneeded = true;
                repaint();
            }
        }
    }

    public void printPoints() {
        for (int i = 0; i < npoint; i++) {
            SequencePoint sp = (SequencePoint) points.elementAt(i);
            Format.print(System.out, "%5d ", i);
            for (int j = 0; j < 3; j++) {
                Format.print(System.out, "%13.3f  ", sp.coord[j]);
            }
            System.out.println();
        }
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        mx = x;
        my = y;
        omx = mx;
        omy = my;
        startx = x;
        starty = y;
        rectx1 = x;
        recty1 = y;
        rectx2 = -1;
        recty2 = -1;
        SequenceI found = findPoint(x, y);
        if (found != null) {
            if (av != null) {
                if (av.getSelection().contains(found)) {
                    av.getSelection().removeElement(found);
                } else {
                    av.getSelection().addElement(found);
                }
                fireSequenceSelectionEvent(av.getSelection());
                System.out.println("Selection code not implemented in RotatableCanvas");
            }
            System.out.println("Sequence found = " + found.getName());
        }
        redrawneeded = true;
        repaint();
    }

    private void fireSequenceSelectionEvent(Selection sel) {
        controller.handleSequenceSelectionEvent(new SequenceSelectionEvent(this, sel));
    }

    public void mouseMoved(MouseEvent evt) {
    }

    public void mouseDragged(MouseEvent evt) {
        mx = evt.getX();
        my = evt.getY();
        if ((evt.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
        } else {
            rotmat.setIdentity();
            rotmat.rotate((float) (my - omy), 'x');
            rotmat.rotate((float) (mx - omx), 'y');
            for (int i = 0; i < npoint; i++) {
                SequencePoint sp = (SequencePoint) points.elementAt(i);
                sp.coord[0] -= centre[0];
                sp.coord[1] -= centre[1];
                sp.coord[2] -= centre[2];
                sp.coord = rotmat.vectorMultiply(sp.coord);
                sp.coord[0] += centre[0];
                sp.coord[1] += centre[1];
                sp.coord[2] += centre[2];
            }
            for (int i = 0; i < 3; i++) {
                axes[i] = rotmat.vectorMultiply(axes[i]);
            }
            omx = mx;
            omy = my;
            redrawneeded = true;
            paint(this.getGraphics());
        }
    }

    private void rectSelect(int x1, int y1, int x2, int y2) {
        boolean changedSel = false;
        for (int i = 0; i < npoint; i++) {
            SequencePoint sp = (SequencePoint) points.elementAt(i);
            int tmp1 = (int) ((sp.coord[0] - centre[0]) * scale + (float) size().width / 2.0);
            int tmp2 = (int) ((sp.coord[1] - centre[1]) * scale + (float) size().height / 2.0);
            if (tmp1 > x1 && tmp1 < x2 && tmp2 > y1 && tmp2 < y2) {
                if (av != null) {
                    if (!av.getSelection().contains(sp.sequence)) {
                        changedSel = true;
                        av.getSelection().addElement(sp.sequence);
                    }
                }
            }
        }
        if (changedSel) {
            fireSequenceSelectionEvent(av.getSelection());
        }
    }

    private SequenceI findPoint(int x, int y) {
        int halfwidth = size().width / 2;
        int halfheight = size().height / 2;
        int found = -1;
        for (int i = 0; i < npoint; i++) {
            SequencePoint sp = (SequencePoint) points.elementAt(i);
            int px = (int) ((float) (sp.coord[0] - centre[0]) * scale) + halfwidth;
            int py = (int) ((float) (sp.coord[1] - centre[1]) * scale) + halfheight;
            if (Math.abs(px - x) < 3 && Math.abs(py - y) < 3) {
                found = i;
            }
        }
        if (found != -1) {
            return ((SequencePoint) points.elementAt(found)).sequence;
        } else {
            return null;
        }
    }

    public boolean handleRubberbandEvent(RubberbandEvent evt) {
        System.out.println("Rubberband handler called in RotatableCanvas with " + evt.getBounds());
        Rubberband rb = (Rubberband) evt.getSource();
        if (rb.getComponent() == this) {
            Rectangle bounds = evt.getBounds();
            rectSelect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
        }
        redrawneeded = true;
        paint(this.getGraphics());
        return true;
    }

    public void update(Graphics g) {
        paint(g);
    }
}
