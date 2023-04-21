package shapetool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.apache.log4j.Category;

/**
 * This class represents a surface where visual entities can be freely placed
 * and moved. 
 * 
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Revision: 1.1 $
 */
public class DrawArea extends JPanel implements MouseMotionListener, MouseListener, Printable {

    private static final Category cat = Category.getInstance(DrawArea.class);

    /** General debug flag */
    private static final boolean DEBUG = false;

    /** Component name */
    private String name = null;

    /** Contained entities */
    private List entities = new ArrayList();

    private boolean bringToFront = false;

    private List selectedList = new ArrayList();

    private int dragX0a[], dragY0a[];

    private int selDragX0, selDragY0;

    private int selDragX, selDragY;

    private boolean bSelDrag = false;

    private boolean bIsDragging = false;

    private double scale = 1.0;

    private boolean showGrid = true;

    private Color gridColor = new Color(240, 240, 240);

    private int gridSize = 10;

    private boolean snapToGrid = true;

    private int snapGridSize = 10;

    private boolean antialiasing = true;

    private boolean pageMarginsVisible = true;

    private MouseEvent lastMouseEvent;

    private MouseEvent scaledMouseEvent;

    /** multi-selection stroke */
    private Stroke multiselStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 2.0f, 4f }, 0.0f);

    private int innerMargin = 10;

    /** Repaint flag */
    private boolean isRepaint = true;

    /** Default menu  */
    private JPopupMenu popup = null;

    /** Frame who owns this component */
    private JFrame topLevelFrame;

    private BasicStroke pageBoundingsStroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 2.0f, 4f }, 0.0f);

    private transient java.util.List observers = new Vector();

    private Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private Rectangle2D rec = null;

    private PageFormat pf;

    /** 
	 * Constructs new a draw area with the default size.
	 * @param name name of this draw area.     
	 */
    public DrawArea(String name) {
        this(new Dimension(500, 500), name);
    }

    /**
	 * Constructs a draw area.
	 * @param dim dimension of the drawing area.
	 * @param name name of this draw area.
	 */
    public DrawArea(Dimension dim, String name) {
        super(null);
        setBackground(Color.white);
        setPreferredSize(dim);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.name = name;
        setDoubleBuffered(false);
        addKeyListener(new CustomKeyListener());
        addObserver(new DrawAreaObserverAdapter() {

            public boolean mouseMoved(DrawArea src, MouseEvent e, Entity en) {
                if (en != null) src.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); else src.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return false;
            }
        });
    }

    private class CustomKeyListener extends KeyAdapter {

        private double step = 0.1;

        public void keyPressed(KeyEvent e) {
            DrawArea src = (DrawArea) e.getComponent();
            switch(e.getKeyCode()) {
                case KeyEvent.VK_EQUALS:
                    if (src.getZoomScale() < 3) {
                        src.setZoomScale(src.getZoomScale() + step);
                        src.repaint();
                    }
                    break;
                case KeyEvent.VK_MINUS:
                    if (src.getZoomScale() > .1) {
                        src.setZoomScale(src.getZoomScale() - step);
                        src.repaint();
                    }
                    break;
            }
        }
    }

    public double getZoomScale() {
        return scale;
    }

    public void setZoomScale(double scale) {
        this.scale = scale;
    }

    /**
	 * Sets the top level frame. 
	 * 
	 * <p>This top level frame normally is the frame that contains the draw
	 * area. Setting this parameter it's not obligatory as it's nothing more
	 * than a stardard way to access the top level frame (necessary if some
	 * entity wants to open a dialog, for instance).
	 * 
	 * @param f frame
	 * @see #getTopLevelFrame()
	 */
    public void setTopLevelFrame(JFrame f) {
        topLevelFrame = f;
    }

    /**
	 * Gets the top level frame.
	 * 
	 * @return top level frame
	 * @see #setTopLevelFrame(JFrame)
	 */
    public JFrame getTopLevelFrame() {
        return topLevelFrame;
    }

    /**
	 * Gets the default popup menu of this draw area. 
	 * 
	 * <p>This is the menu that that will be shown if the user tries to open a
	 * popup menu over an open area, e.g., over an area with no entity.
	 * 
	 * @return popup menu.
	 * @see #setPopupMenu(JPopupMenu)
	 */
    public JPopupMenu getPopupMenu() {
        return popup;
    }

    /**
	 * Sets the default popup menu.
	 * @param popup popup menu.
	 * @see #getPopupMenu()
	 */
    public void setPopupMenu(JPopupMenu popup) {
        this.popup = popup;
    }

    /**
	 * Gets the name of this draw area.
	 * @return name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Selects an entity.
	 * 
	 * @param entity entity to be selected
	 * 
	 * @see #clearSelection()
	 * @see #select(java.util.List)
	 */
    public void selectEntity(Entity en) {
        if (en.canBeSelected()) {
            selectedList.add(en);
            en.setSelected(true);
            notifySelect(selectedList);
        }
        repaint();
    }

    public void unselectEntity(Entity en) {
        en.setSelected(false);
        selectedList.remove(en);
        notifySelect(selectedList);
        repaint();
    }

    /**
	 * Selects a list of entities.
	 * 
	 * <p>The previous selection list is cleared.
	 * 
	 * @param sl list of entities
	 * @see #select(Entity)
	 */
    public void setSelectedEntities(List sl) {
        clearSelection();
        Collection reallySel = new ArrayList();
        selectedList = sl;
        for (Iterator i = sl.iterator(); i.hasNext(); ) {
            Entity en = (Entity) i.next();
            if (en.canBeSelected()) {
                en.setSelected(true);
                reallySel.add(en);
            }
        }
        notifySelect(reallySel);
        repaint();
    }

    public List getSelectedEntities() {
        return selectedList;
    }

    /**
	 * Clears the current selection.
	 * 
	 * @see #select(Entity)
	 */
    public void clearSelection() {
        if (!selectedList.isEmpty()) {
            for (Iterator i = selectedList.iterator(); i.hasNext(); ) ((Entity) i.next()).setSelected(false);
            selectedList.clear();
            repaint();
            notifyClearSelection();
        }
    }

    /**
	 * Adds an entity to this draw area.    
	 *  
	 * @param entity entity to be added.
	 * @exception DrawAreaException
	 *            if the supplied entitiy is already defined. This method
	 *            uses the <code>java.lang.Object.equasl(Object)</code> to
	 *            check if two entities are the same. So, be careful when
	 *            overriding this method. 
	 * @see #remove(Entity)
	 */
    public void add(Entity entity) {
        if (entities.contains(entity)) throw new DrawAreaException("could not add entity: " + "entity already defined.");
        entities.add(entity);
        cat.debug("--adding '" + entity.getName() + "'.");
        entity.setParent(this);
        repaint();
    }

    /**
	 * Removes an entity from this draw area.
	 * 
	 * @param entity entity to be removed.
	 * @exception DrawAreaException if the supplied entity is not defined.
	 * @see #add(Entity)
	 */
    public void remove(Entity entity) {
        if (!entities.contains(entity)) throw new DrawAreaException("could not remove entity: " + "entity not defined.");
        cat.debug("--removing '" + entity.getName() + "'.");
        entities.remove(entity);
        entity.setParent(null);
        if (entity.isSelected()) {
            clearSelection();
        }
        repaint();
    }

    /**
	 * Sends an entity to back.
	 * @param e entity
	 */
    public void toBack(Entity e) {
        entities.remove(e);
        entities.add(0, e);
    }

    /**
	 * Sends an entity to front.
	 * @param e entity
	 */
    public void toFront(Entity e) {
        entities.remove(e);
        entities.add(e);
    }

    /**
	 * Gets an entity by its name.
	 * 
	 * @return entity or <code>null</code> if there's no entity with the
	 *          supplied name
	 * 
	 * @see #add(Entity)
	 * @see #remove(Entity)
	 */
    public Entity get(String s) {
        for (Iterator iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity entity = (Entity) iterator.next();
            if (entity.getName().equals(s)) return entity;
        }
        return null;
    }

    /**
	 * Destroys all entities.
	 */
    public void destroy() {
        Collection entities = new Vector(this.entities);
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            Entity e = (Entity) i.next();
            e.destroy();
        }
    }

    public java.util.List getEntities() {
        return entities;
    }

    /**
	 * Enables/disables the <code>repaint()</code> method on this component.
	 * 
	 * <p>You can use this method to increase the performance, avoiding 
	 * intensive and unnecessary invocation to {@link #repaint()}.
	 * 
	 * @param s flag.
	 */
    public int disableRepaint() {
        return ++repaintLock;
    }

    public void enableRepaint(int expectedRepaintLock) {
        assert repaintLock == expectedRepaintLock : "repaintLock == expectedRepaintLock" + "(" + repaintLock + " != " + expectedRepaintLock + ")";
        repaintLock--;
        if (repaintLock == 0) {
            super.repaint();
            repaintCount++;
        }
    }

    private int repaintLock = 0;

    private int repaintCount = 0;

    private int requestedRepaintCount = 0;

    private void __printRepaintInfo() {
        System.err.println("Current repaint lock is " + repaintLock + "   (performed " + (repaintCount) + " of " + (requestedRepaintCount) + ")");
    }

    /**
	 * Repaints this component. 
	 * 
	 * <p>This method was overriden to support {@link #setRepaint(boolean)}.
	 */
    public void repaint() {
        if (repaintLock == 0) {
            repaintCount++;
            super.repaint();
        }
        requestedRepaintCount++;
    }

    /**
	 * Removes all entities from this draw area. 
	 * 
	 * <p>Notice that all entities will be just removed but 
	 * <b>not</b> destroyed.
	 * 
	 * @see #clearAndDestroy()
	 */
    public void clear() {
        java.util.List temp = new ArrayList(entities);
        for (Iterator i = temp.iterator(); i.hasNext(); ) remove((Entity) i.next());
        repaint();
    }

    /**
	 * Removes all entities from this draw area. 
	 * 
	 * <p>Notice that all entities will be just removed and later destroyed 
	 * (via a call the the method {@link Entity#destroy()}.
	 * 
	 * @see #clear()
	 */
    public void clearAndDestroy() {
        java.util.List temp = new ArrayList(entities);
        for (Iterator i = temp.iterator(); i.hasNext(); ) {
            Entity en = (Entity) i.next();
            remove(en);
            en.destroy();
        }
        repaint();
    }

    /**
	 * @see javax.swing.JComponent#paintComponent(Graphics)
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (showGrid) drawGrid(g2);
        drawPrintBoundingBox(g2);
        if (antialiasing) g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(stroke);
        if (notifyPaintFirst(g2)) return;
        drawShapes(g2, 0, 0);
        if (notifyPaintLast(g2)) return;
        if (bSelDrag) {
            g2.setPaint(Color.gray);
            Stroke st = g2.getStroke();
            g2.setStroke(multiselStroke);
            g2.draw(new Line2D.Double(selDragX0, selDragY0, selDragX, selDragY).getBounds2D());
            g2.setStroke(st);
        }
        if (!bIsDragging) fitDrawArea();
    }

    /**
	 * Gets the minimun rectangle that contains all entities.
	 * 
	 * <p>This method just returns the value found by 
	 * {@link #drawShapes(Graphics2D, int, int)}.
	 * 
	 * @return rectangle
	 */
    public Rectangle2D getFullBounds2D() {
        return rec;
    }

    /**
	 * Gets the minimun rectangle that contains all entities.
	 * 
	 * <p>This method just returns the value found by 
	 * {@link #drawShapes(Graphics2D, int, int)}.
	 * 
	 * @return rectangle
	 */
    public Rectangle getFullBounds() {
        if (rec == null) return new Rectangle(); else return new Rectangle((int) rec.getX(), (int) rec.getY(), (int) rec.getWidth(), (int) rec.getHeight());
    }

    /**
	 * Draws all entities.
	 * 
	 * <p>This method evaluates the full bounds of the current set of entities.
	 * 
	 * @param g2 graphic context
	 * @param x x-coord offset
	 * @param y y-coord offset
	 */
    protected void drawShapes(Graphics2D g2, int x, int y) {
        Entity e;
        rec = null;
        Rectangle2D aux;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            e = (Entity) i.next();
            if (e.hasRelativePosition()) g2.translate(e.getX() + x, e.getY() + y);
            e.paint(g2);
            if (DEBUG) {
                g2.setPaint(Color.RED);
                if (!(e instanceof LinkEntity)) g2.drawString("(" + e.getX() + ";" + e.getY() + ")", 10, 10);
            }
            if (e.hasRelativePosition()) g2.translate(-e.getX() - x, -e.getY() - y);
            if (DEBUG) g2.setPaint(Color.green);
            if (e instanceof MeasurableEntity) if (rec == null) {
                rec = getBounds2D(e);
            } else {
                aux = getBounds2D(e);
                if (DEBUG) g2.draw(aux);
                rec = rec.createUnion(aux);
            }
        }
        if (DEBUG) {
            g2.setPaint(Color.GREEN);
            g2.draw(rec);
        }
    }

    protected void refreshFullBounds() {
        Entity e;
        rec = null;
        Rectangle2D aux;
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            e = (Entity) i.next();
            if (e instanceof MeasurableEntity) {
                if (rec == null) {
                    rec = getBounds2D(e);
                } else {
                    aux = getBounds2D(e);
                    rec = rec.createUnion(aux);
                }
            }
        }
    }

    /**
	 * Draws the grid.
	 * @param g graphical context
	 */
    protected void drawGrid(Graphics2D g) {
        Dimension d = getSize();
        int xm = (int) (d.getWidth() / scale);
        int ym = (int) (d.getHeight() / scale);
        g.setPaint(gridColor);
        for (int x = 0; x < xm; x += gridSize) g.drawLine(x, 0, x, ym);
        for (int y = 0; y < ym; y += gridSize) g.drawLine(0, y, xm, y);
    }

    /**
	 * Adjust the position of entities.
     * 
     * <P>If the bounding box has x or y is negative all entities will be moved
     * to the origin.
     * 
     * @param forceX if true force the move even when x is positive 
     * @param forceY if true force the move even when y is positive
	 */
    public void adjustOrigin(boolean forceX, boolean forceY) {
        int level = disableRepaint();
        refreshFullBounds();
        Rectangle r = getFullBounds();
        r.x -= innerMargin;
        r.y -= innerMargin;
        r.width += 2 * innerMargin;
        r.height += 2 * innerMargin;
        r.x = (r.x < 0 || forceX) ? adjustGrid(r.x) : 0;
        r.y = (r.y < 0 || forceY) ? adjustGrid(r.y) : 0;
        Entity e;
        List links = new ArrayList();
        for (Iterator i = entities.iterator(); i.hasNext(); ) {
            e = (Entity) i.next();
            if (e.canSetPosition()) {
                if (e instanceof LinkEntity) {
                    e.move(-r.x, -r.y);
                    links.add(e);
                } else {
                    e.move(-r.x, -r.y);
                }
            }
        }
        LinkEntity le;
        for (Iterator i = links.iterator(); i.hasNext(); ) {
            le = (LinkEntity) i.next();
            le.updatePath();
        }
        refreshFullBounds();
        fitDrawArea();
        enableRepaint(level);
    }

    /**
	 * Gets the bounds of a given entity in term of the draw coordinates.
	 * @param e entity
	 * @return bounding rectangle
	 * @see #getBounds(Entity)
	 */
    public Rectangle2D getBounds2D(Entity e) {
        Rectangle2D r = ((MeasurableEntity) e).getBounds2D();
        if (e.hasRelativePosition()) {
            return new Rectangle2D.Double((double) r.getX() + e.getX(), (double) r.getY() + e.getY(), r.getWidth(), r.getHeight());
        } else {
            return r;
        }
    }

    /**
	 * Gets the bounds of a given entity in terms of the draw coordinates.
	 * @param e entity
	 * @return bounding rectangle
	 * @see #getBounds2D(Entity)     
	 */
    public Rectangle2D getBounds(Entity e) {
        Rectangle r = ((MeasurableEntity) e).getBounds();
        if (e.hasRelativePosition()) return new Rectangle((int) r.getX() + e.getX(), (int) r.getY() + e.getY(), r.width, r.height); else return r;
    }

    /**
	 * Gets the entity that contains the supplied point.
	 * Notice that if we have more than one entity containing the
	 * supplied point this method will return only the first one.
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @return entity or <code>null</code> if no entity contains the supplied
	 *         point.
	 */
    public Entity getEntityAt(int x, int y) {
        Entity en;
        for (int i = entities.size() - 1; i >= 0; i--) {
            en = (Entity) entities.get(i);
            if (en.contains(x, y)) return en;
        }
        return null;
    }

    /**
	 * Gets the list of entities that are contained inside the supplied
	 * rectangle.
	 * @param r rectangle
	 * @return list of entities
	 */
    public java.util.List getEntitiesAt(Rectangle r) {
        java.util.List el = new Vector();
        Entity en;
        for (int i = 0; i < entities.size(); i++) {
            en = (Entity) entities.get(i);
            if (en instanceof MeasurableEntity) if (r.contains(getBounds(en))) {
                el.add(en);
                cat.debug(">>>> rect CONTAINS " + en.getName());
            }
        }
        return el;
    }

    /**
	 * Tests a mouse event to see if it is a popup trigger event. 
	 * 
	 * <p>This method was designed because the <code>MouseEvent.isPopupTrigger()
	 * </code> does not seem to work in a <code>mousePressed()</code>
	 * notification.
	 * 
	 * @param me mouse event.
	 * @return <code>true</code> if the mouse event is a popup trigger.
	 */
    public boolean isPopupTrigger(MouseEvent me) {
        return (me.getModifiers() & 0xc) != 0;
    }

    private ControlPoint cp;

    private Point cp_pos0;

    /**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        int level = disableRepaint();
        try {
            cp = null;
            cp_pos0 = null;
            int _x = e.getX();
            int _y = e.getY();
            bSelDrag = false;
            cat.debug("--MOUSE PRESSED " + e.getX() + " ; " + e.getY());
            Entity en;
            for (int i = entities.size() - 1; i >= 0; i--) {
                en = (Entity) entities.get(i);
                if (en.contains(_x, _y)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    int x0 = _x;
                    int y0 = _y;
                    if (en instanceof ControlPoint) {
                        cp = (ControlPoint) en;
                        cp_pos0 = new Point(_x, _y);
                    }
                    if (notifyMousePressed(e, en)) return;
                    if (isPopupTrigger(e)) {
                        JPopupMenu popup = notifyPopupMenu(_e, en);
                        if (popup != null) {
                            popup.show(this, _e.getX(), _e.getY());
                        } else {
                            popup = en.getPopupMenu((int) x0, (int) y0);
                            if (popup != null) popup.show(this, _e.getX(), _e.getY());
                        }
                    }
                    if (!en.isSelected()) {
                        if (e.isControlDown()) {
                            if (en.canBeSelected()) {
                                selectEntity(en);
                                repaint();
                            }
                        } else {
                            if (en.canBeSelected()) {
                                clearSelection();
                                selectEntity(en);
                                repaint();
                            }
                        }
                    } else {
                        if (e.isControlDown()) {
                            unselectEntity(en);
                        }
                    }
                    int n = selectedList.size();
                    dragX0a = new int[n];
                    dragY0a = new int[n];
                    Entity aux;
                    for (int j = 0; j < n; j++) {
                        aux = (Entity) selectedList.get(j);
                        if (aux.hasRelativePosition()) {
                            dragX0a[j] = _x - aux.getX();
                            dragY0a[j] = _y - aux.getY();
                        } else {
                            aux.savePosition();
                            dragX0a[j] = _x;
                            dragY0a[j] = _y;
                        }
                    }
                    return;
                }
            }
            if (isPopupTrigger(e)) {
                JPopupMenu popup;
                if ((popup = notifyPopupMenu(e, null)) != null) {
                    popup.show(this, (int) _x, (int) _y);
                } else {
                    popup = getPopupMenu();
                    if (popup != null) popup.show(this, e.getX(), e.getY());
                }
            }
            clearSelection();
            cat.debug("--deselection");
            selDragX0 = selDragX = _x;
            selDragY0 = selDragY = _y;
            bSelDrag = false;
            if (notifyMousePressed(e, null)) return;
        } finally {
            enableRepaint(level);
        }
    }

    public MouseEvent getLastMouseEvent() {
        return lastMouseEvent;
    }

    /**
	 * Gets a scaled version of an event.
	 * 
	 * @param e original event
	 * @return scaled version of the event
	 */
    public MouseEvent scaleMouseEvent(MouseEvent unscaledEvent) {
        if (unscaledEvent != lastMouseEvent) {
            lastMouseEvent = unscaledEvent;
            scaledMouseEvent = new MouseEvent((Component) unscaledEvent.getSource(), unscaledEvent.getID(), unscaledEvent.getWhen(), unscaledEvent.getModifiers(), (int) (unscaledEvent.getX() / scale), (int) (unscaledEvent.getY() / scale), unscaledEvent.getClickCount(), unscaledEvent.isPopupTrigger(), unscaledEvent.getButton());
        }
        return scaledMouseEvent;
    }

    /**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
    public void mouseDragged(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        int level = disableRepaint();
        try {
            bIsDragging = true;
            if (notifyMouseDragged(e, selectedList)) return;
            if (cp != null) {
                cp.setPosition(_e.getX(), cp.getY());
                repaint();
                return;
            } else {
                if (!selectedList.isEmpty()) {
                    Entity en;
                    for (int i = 0; i < selectedList.size(); i++) {
                        en = (Entity) selectedList.get(i);
                        if (en.canBeDragged()) {
                            if (en.hasRelativePosition()) {
                                Point2D.Double p = new Point2D.Double(e.getX() - dragX0a[i], e.getY() - dragY0a[i]);
                                if (snapToGrid) adjustGrid(p);
                                en.setPosition((int) p.x, (int) p.y);
                            } else {
                                Point2D.Double p = new Point2D.Double(e.getX() - dragX0a[i], e.getY() - dragY0a[i]);
                                en.restorePosition();
                                en.move((int) p.x, (int) p.y);
                            }
                        }
                    }
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    repaint();
                    return;
                }
            }
            cat.debug("--MOUSE DRAGGED " + e.getX() + " ; " + e.getY());
            bSelDrag = true;
            selDragX = e.getX();
            selDragY = e.getY();
        } finally {
            enableRepaint(level);
        }
    }

    /**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        cp = null;
        bIsDragging = false;
        cat.debug("--MOUSE RELEASED " + e.getX() + " ; " + e.getY());
        if (notifyMouseReleased(e, getEntityAt(e.getX(), e.getY()))) return;
        if (bSelDrag) {
            bSelDrag = false;
            int origX = Math.min(selDragX0, selDragX);
            int origY = Math.min(selDragY0, selDragY);
            int w = Math.abs(selDragX0 - selDragX);
            int h = Math.abs(selDragY0 - selDragY);
            setSelectedEntities(getEntitiesAt(new Rectangle(origX, origY, w, h)));
        }
        if (rec != null && (rec.getX() <= 0 || rec.getY() <= 0)) adjustOrigin(false, false);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        fitDrawArea();
    }

    /**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
    public void mouseMoved(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        if (notifyMouseMoved(e, getEntityAt(e.getX(), e.getY()))) return;
    }

    /**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        if (notifyMouseClicked(e, getEntityAt(e.getX(), e.getY()))) return;
    }

    /**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    public void mouseExited(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        if (notifyMouseExited(e)) return;
    }

    /**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    public void mouseEntered(MouseEvent _e) {
        MouseEvent e = scaleMouseEvent(_e);
        if (notifyMouseEntered(e)) return;
    }

    /**
	 * Adds a draw area observer.
	 * @param dao observer
	 */
    public void addObserver(DrawAreaObserver dao) {
        observers.add(dao);
        cat.debug("Added observer: " + dao);
        repaint();
    }

    /**
	 * Removes a draw area observer.
	 * @param dao observer to remove
	 */
    public void removeObserver(DrawAreaObserver dao) {
        observers.remove(dao);
        cat.debug("Removed observer: " + dao);
        repaint();
    }

    protected boolean notifyPaintFirst(Graphics2D g) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).paintFirst(this, g) || veto;
        return veto;
    }

    protected boolean notifyPaintLast(Graphics2D g) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).paintLast(this, g) || veto;
        return veto;
    }

    protected JPopupMenu notifyPopupMenu(MouseEvent ev, Entity en) {
        JPopupMenu menu = null;
        for (int i = 0; i < observers.size() && (menu == null); i++) menu = ((DrawAreaObserver) observers.get(i)).getPopupMenu(this, ev, en);
        return menu;
    }

    protected boolean notifyMousePressed(MouseEvent ev, Entity en) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) {
            DrawAreaObserver observer = (DrawAreaObserver) observers.get(i);
            cat.debug("Notifying mousePressed for " + observer);
            veto = observer.mousePressed(this, ev, en) || veto;
        }
        return veto;
    }

    protected boolean notifyMouseDragged(MouseEvent ev, java.util.List ens) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).mouseDragged(this, ev, ens) || veto;
        return veto;
    }

    protected boolean notifyMouseReleased(MouseEvent ev, Entity en) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) {
            DrawAreaObserver observer = (DrawAreaObserver) observers.get(i);
            cat.debug("Notifying mouseReleased for " + observer);
            veto = observer.mouseReleased(this, ev, en) || veto;
        }
        return veto;
    }

    protected boolean notifyMouseMoved(MouseEvent ev, Entity en) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).mouseMoved(this, ev, en) || veto;
        return veto;
    }

    protected boolean notifyMouseClicked(MouseEvent ev, Entity en) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).mousePressed(this, ev, en) || veto;
        return veto;
    }

    protected boolean notifyMouseEntered(MouseEvent ev) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).mouseEntered(this) || veto;
        return veto;
    }

    protected boolean notifyMouseExited(MouseEvent ev) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).mouseExited(this) || veto;
        return veto;
    }

    protected boolean notifyClearSelection() {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).clearSelection(this) || veto;
        return veto;
    }

    protected boolean notifySelect(Collection reallySelected) {
        boolean veto = false;
        for (int i = 0; i < observers.size() && !veto; i++) veto = ((DrawAreaObserver) observers.get(i)).select(this, reallySelected) || veto;
        return veto;
    }

    public void setScale(double s) {
        scale = s;
    }

    public double getScale() {
        return scale;
    }

    /**
	 * Snaps a value to grid.
	 * @param x value to be adjusted.
	 * @param s grid spacing.
	 * @return adjusted value.
	 */
    public double adjustGrid(double x) {
        if (x > 0) {
            double dx = x % snapGridSize;
            double s = snapGridSize;
            if (dx > (s / 2)) return (x - dx) + s;
            return (x - dx);
        } else {
            double dx = Math.abs(x % snapGridSize);
            double s = snapGridSize;
            if (dx >= (s / 2)) return (x + dx) - s;
            return (x + dx);
        }
    }

    public int adjustGrid(int x) {
        return (int) adjustGrid((double) x);
    }

    /**
	 * Snaps a point to the current snapping grid.
	 * @param p point to be adjusted.
	 */
    public void adjustGrid(Point2D.Double p) {
        p.x = adjustGrid(p.x);
        p.y = adjustGrid(p.y);
    }

    /**
	 * Snaps a point to the current snapping grid.
	 * @param p point to be adjusted.
	 */
    public Point adjustGrid(Point p) {
        return new Point((int) adjustGrid(p.x), (int) adjustGrid(p.y));
    }

    Point2D.Double normalizeX(double x, double y, double x1, double x2, double yf) {
        if (x < x1) return new Point2D.Double(x1, yf);
        if (x > x2) return new Point2D.Double(x2, yf);
        return new Point2D.Double(x, yf);
    }

    Point2D.Double normalizeY(double x, double y, double y1, double y2, double xf) {
        if (y < y1) return new Point2D.Double(xf, y1);
        if (y > y2) return new Point2D.Double(xf, y2);
        return new Point2D.Double(xf, y);
    }

    /**
	 * Enables/disables the grid visualization.
	 * @param s flag
	 */
    public void setShowGrid(boolean s) {
        showGrid = s;
        repaint();
    }

    public boolean getShowGrid() {
        return showGrid;
    }

    /**
	 * Enables/disables the snap to grid feature.
	 * @param s flag
	 */
    public void setSnapToGrid(boolean s) {
        snapToGrid = s;
    }

    public boolean getSnapToGrid() {
        return snapToGrid;
    }

    /**
	 * Enables/disables the antialiasing feature.
	 * @param s flag
	 */
    public void setAntialiasing(boolean s) {
        antialiasing = s;
        repaint();
    }

    /**
	 * Gets the antialiasing state.
	 * @return state
	 */
    public boolean getAntialiasing() {
        return antialiasing;
    }

    /**
	 * Sets the grid color.
	 * @param s flag
	 */
    public void setGridColor(Color c) {
        gridColor = c;
        repaint();
    }

    /**
	 * Sets the grid size.
	 * @param s flag
	 */
    public void setGridSize(int n) {
        gridSize = n;
        repaint();
    }

    /**
	 * Sets the snap to grip size.
	 * @param s flag
	 */
    public void setSnapToGridSize(int n) {
        snapGridSize = n;
    }

    /**
	 * Updates the preferred size of this draw area component to fit all
	 * entities displayed.
	 */
    public void fitDrawArea() {
        Rectangle r = getFullBounds();
        setPreferredSize(new Dimension(r.width + r.x + innerMargin, r.height + r.y + innerMargin));
        revalidate();
    }

    public void setInnerMargin(int im) {
        innerMargin = im;
        repaint();
    }

    public int getInnerMargin() {
        return innerMargin;
    }

    /**
	 * Sets the page format.
	 * @param pf page format.
	 */
    public void setPageFormat(PageFormat pf) {
        this.pf = pf;
    }

    protected void drawPrintBoundingBox(Graphics2D g) {
        if (pf == null || !pageMarginsVisible) return;
        g.setPaint(Color.red);
        int w = (int) pf.getImageableWidth();
        int h = (int) pf.getImageableHeight();
        Stroke oldStroke = g.getStroke();
        g.setStroke(pageBoundingsStroke);
        g.drawRect(0, 0, w, h);
        g.setStroke(oldStroke);
    }

    public boolean isPageMarginsVisible() {
        return pageMarginsVisible;
    }

    public void setPageMarginsVisible(boolean s) {
        pageMarginsVisible = s;
    }

    /**
	 * @see java.awt.print.Printable#print(Graphics, PageFormat, int)
	 */
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        clearSelection();
        if (pi >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        drawPrintBoundingBox((Graphics2D) g);
        int x1 = (int) pf.getImageableX();
        int y1 = (int) pf.getImageableY();
        if (pageMarginsVisible) drawShapes((Graphics2D) g, x1, y1); else drawShapes((Graphics2D) g, 0, 0);
        return Printable.PAGE_EXISTS;
    }

    /**
	 * @see javax.swing.JComponent#isOptimizedDrawingEnabled()
	 */
    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    /**
	 * Method dispose.
	 */
    public void dispose() {
        destroy();
    }
}
