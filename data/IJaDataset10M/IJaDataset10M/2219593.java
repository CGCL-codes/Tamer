package net.rptools.maptool.model.drawing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import net.rptools.maptool.client.AppState;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.model.CellPoint;
import net.rptools.maptool.model.ZonePoint;

/**
 * A drawing tool that will draw a line template between 2 vertices.
 * 
 * @author jgorrell
 * @version $Revision$ $Date: 2009-02-09 01:00:59 -0600 (Mon, 09 Feb
 *          2009) $ $Author$
 */
public class LineTemplate extends AbstractTemplate {

    /**
	 * Are straight lines drawn double width?
	 */
    private boolean doubleWide = AppState.useDoubleWideLine();

    /**
	 * This vertex is used to determine the path.
	 */
    private ZonePoint pathVertex;

    /**
	 * The calculated path for this line.
	 */
    private ArrayList<CellPoint> path;

    /**
	 * The pool of points.
	 */
    private ArrayList<CellPoint> pool;

    /**
	 * The line is drawn in this quadrant. A string is used as a hack to get
	 * around the hessian library's problem w/ serialization of enums
	 */
    private String quadrant = null;

    /**
	 * Flag used to determine mouse position relative to vertex position
	 */
    private boolean mouseSlopeGreater;

    /**
	 * @see net.rptools.maptool.model.drawing.AbstractTemplate#paintArea(java.awt.Graphics2D,
	 *      int, int, int, int, int, int)
	 */
    @Override
    protected void paintArea(Graphics2D g, int x, int y, int xOff, int yOff, int gridSize, int distance) {
        paintArea(g, xOff, yOff, gridSize, getQuadrant());
    }

    /**
	 * This method is cheating, the distance parameter was replaced with the
	 * offset into the path.
	 * 
	 * @see net.rptools.maptool.model.drawing.AbstractTemplate#paintBorder(java.awt.Graphics2D,
	 *      int, int, int, int, int, int)
	 */
    @Override
    protected void paintBorder(Graphics2D g, int x, int y, int xOff, int yOff, int gridSize, int pElement) {
        boolean[] noPaint = new boolean[4];
        for (int i = pElement - 3; i < pElement + 3; i++) {
            if (i < 0 || i >= path.size() || i == pElement) continue;
            CellPoint p = path.get(i);
            int dx = p.x - x;
            int dy = p.y - y;
            if (Math.abs(dx) == Math.abs(dy) || Math.abs(dx) > 1 || Math.abs(dy) > 1) continue;
            noPaint[dx != 0 ? (dx < 0 ? 0 : 2) : (dy < 0 ? 3 : 1)] = true;
        }
        if (!noPaint[0]) paintCloseVerticalBorder(g, xOff, yOff, gridSize, getQuadrant());
        if (!noPaint[1]) paintFarHorizontalBorder(g, xOff, yOff, gridSize, getQuadrant());
        if (!noPaint[2]) paintFarVerticalBorder(g, xOff, yOff, gridSize, getQuadrant());
        if (!noPaint[3]) paintCloseHorizontalBorder(g, xOff, yOff, gridSize, getQuadrant());
    }

    /**
	 * @see net.rptools.maptool.model.drawing.AbstractTemplate#paint(java.awt.Graphics2D,
	 *      boolean, boolean)
	 */
    @Override
    protected void paint(Graphics2D g, boolean border, boolean area) {
        if (MapTool.getCampaign().getZone(getZoneId()) == null) {
            return;
        }
        if (pathVertex == null) return;
        if (getRadius() == 0) return;
        if (calcPath() == null) return;
        int gridSize = MapTool.getCampaign().getZone(getZoneId()).getGrid().getSize();
        ListIterator<CellPoint> i = path.listIterator();
        while (i.hasNext()) {
            CellPoint p = i.next();
            int xOff = p.x * gridSize;
            int yOff = p.y * gridSize;
            int distance = getDistance(p.x, p.y);
            if (area) {
                paintArea(g, p.x, p.y, xOff, yOff, gridSize, distance);
            }
            if (border) {
                paintBorder(g, p.x, p.y, xOff, yOff, gridSize, i.previousIndex());
            }
        }
    }

    /**
	 * @see net.rptools.maptool.model.drawing.AbstractTemplate#setVertex(ZonePoint)
	 */
    @Override
    public void setVertex(ZonePoint vertex) {
        clearPath();
        super.setVertex(vertex);
    }

    /**
	 * @see net.rptools.maptool.model.drawing.AbstractTemplate#setRadius(int)
	 */
    @Override
    public void setRadius(int squares) {
        if (squares == getRadius()) return;
        clearPath();
        super.setRadius(squares);
    }

    /**
	 * Calculate the path
	 * 
	 * @return The new path or <code>null</code> if there is no path.
	 */
    protected List<CellPoint> calcPath() {
        if (getRadius() == 0) return null;
        if (pathVertex == null) return null;
        int radius = getRadius();
        ZonePoint vertex = getVertex();
        if (vertex.equals(pathVertex)) return null;
        int dx = pathVertex.x - vertex.x;
        int dy = pathVertex.y - vertex.y;
        clearPath();
        path = new ArrayList<CellPoint>();
        path.add(getPointFromPool(0, 0));
        MathContext mc = MathContext.DECIMAL128;
        MathContext rmc = new MathContext(MathContext.DECIMAL64.getPrecision(), RoundingMode.DOWN);
        if (dx != 0 && dy != 0) {
            setQuadrant((dx < 0) ? (dy < 0 ? Quadrant.NORTH_WEST : Quadrant.SOUTH_WEST) : (dy < 0 ? Quadrant.NORTH_EAST : Quadrant.SOUTH_EAST));
            BigDecimal m = BigDecimal.valueOf(dy).divide(BigDecimal.valueOf(dx), mc).abs();
            CellPoint p = path.get(path.size() - 1);
            while (getDistance(p.x, p.y) <= radius) {
                int x = p.x;
                int y = p.y;
                double xValue = BigDecimal.valueOf(y + 1).divide(m, mc).round(rmc).doubleValue();
                double yValue = BigDecimal.valueOf(x + 1).multiply(m, mc).round(rmc).doubleValue();
                if (xValue == x + 1 && yValue == y + 1) {
                    if (doubleWide || !mouseSlopeGreater) path.add(getPointFromPool(x + 1, y));
                    if (doubleWide || mouseSlopeGreater) path.add(getPointFromPool(x, y + 1));
                    path.add(getPointFromPool(x + 1, y + 1));
                } else if (Math.floor(xValue) == x) {
                    path.add(getPointFromPool(x, y + 1));
                } else if (Math.floor(yValue) == y) {
                    path.add(getPointFromPool(x + 1, y));
                } else {
                    System.err.println("I can't do math: dx=" + dx + " dy=" + dy + " m=" + m + " x=" + x + " xValue=" + xValue + " y=" + y + " yValue=" + yValue);
                    return path;
                }
                p = path.get(path.size() - 1);
            }
            if (pool != null) {
                pool.clear();
                pool = null;
            }
        } else {
            int xInc = dx != 0 ? 1 : 0;
            int yInc = dy != 0 ? 1 : 0;
            int x = xInc;
            int y = yInc;
            int xTouch = (dx != 0) ? 0 : -1;
            int yTouch = (dy != 0) ? 0 : -1;
            if (doubleWide) path.add(getPointFromPool(xTouch, yTouch));
            while (getDistance(x, y) <= radius) {
                path.add(getPointFromPool(x, y));
                if (doubleWide) path.add(getPointFromPool(x + xTouch, y + yTouch));
                x += xInc;
                y += yInc;
            }
        }
        return path;
    }

    /**
	 * Get a point from the pool or create a new one.
	 * 
	 * @param x
	 *            The x coordinate of the new point.
	 * @param y
	 *            The y coordinate of the new point.
	 * @return The new point.
	 */
    public CellPoint getPointFromPool(int x, int y) {
        CellPoint p = null;
        if (pool != null) {
            p = pool.remove(pool.size() - 1);
            if (pool.isEmpty()) pool = null;
        }
        if (p == null) {
            p = new CellPoint(0, 0);
        }
        p.x = x;
        p.y = y;
        return p;
    }

    /**
	 * Add a point back to the pool.
	 * 
	 * @param p
	 *            Add this point back
	 */
    public void addPointToPool(CellPoint p) {
        if (pool != null) pool.add(p);
    }

    /**
	 * Get the pathVertex for this LineTemplate.
	 * 
	 * @return Returns the current value of pathVertex.
	 */
    public ZonePoint getPathVertex() {
        return pathVertex;
    }

    /**
	 * Set the value of pathVertex for this LineTemplate.
	 * 
	 * @param pathVertex
	 *            The pathVertex to set.
	 */
    public void setPathVertex(ZonePoint pathVertex) {
        if (pathVertex.equals(this.pathVertex)) return;
        clearPath();
        this.pathVertex = pathVertex;
    }

    /**
	 * Clear the current path. This will cause it to be recalculated during the
	 * next draw.
	 */
    public void clearPath() {
        if (path != null) pool = path;
        path = null;
    }

    /**
	 * Get the quadrant for this LineTemplate.
	 * 
	 * @return Returns the current value of quadrant.
	 */
    public Quadrant getQuadrant() {
        if (quadrant != null) return Quadrant.valueOf(quadrant);
        return null;
    }

    /**
	 * Set the value of quadrant for this LineTemplate.
	 * 
	 * @param quadrant
	 *            The quadrant to set.
	 */
    public void setQuadrant(Quadrant quadrant) {
        if (quadrant != null) this.quadrant = quadrant.name(); else this.quadrant = null;
    }

    /**
	 * Get the mouseSlopeGreater for this LineTemplate.
	 * 
	 * @return Returns the current value of mouseSlopeGreater.
	 */
    public boolean isMouseSlopeGreater() {
        return mouseSlopeGreater;
    }

    /**
	 * Set the value of mouseSlopeGreater for this LineTemplate.
	 * 
	 * @param aMouseSlopeGreater
	 *            The mouseSlopeGreater to set.
	 */
    public void setMouseSlopeGreater(boolean aMouseSlopeGreater) {
        mouseSlopeGreater = aMouseSlopeGreater;
    }

    /**
	 * Get the doubleWide for this LineTemplate.
	 * 
	 * @return Returns the current value of doubleWide.
	 */
    public boolean isDoubleWide() {
        return doubleWide;
    }

    /**
	 * Set the value of doubleWide for this LineTemplate.
	 * 
	 * @param aDoubleWide
	 *            The doubleWide to set.
	 */
    public void setDoubleWide(boolean aDoubleWide) {
        doubleWide = aDoubleWide;
    }

    /** @return Getter for path */
    public ArrayList<CellPoint> getPath() {
        return path;
    }

    /**
	 * @param path
	 *            Setter for the path to set
	 */
    public void setPath(ArrayList<CellPoint> path) {
        this.path = path;
    }

    /**
	 * @see net.rptools.maptool.model.drawing.Drawable#getBounds()
	 */
    public Rectangle getBounds() {
        if (MapTool.getCampaign().getZone(getZoneId()) == null) {
            return new Rectangle();
        }
        int gridSize = MapTool.getCampaign().getZone(getZoneId()).getGrid().getSize();
        ZonePoint vertex = getVertex();
        ZonePoint minp = null;
        ZonePoint maxp = null;
        if (path == null) {
            calcPath();
            if (path == null) {
                return new Rectangle();
            }
        }
        for (CellPoint pt : path) {
            ZonePoint p = MapTool.getCampaign().getZone(getZoneId()).getGrid().convert(pt);
            p = new ZonePoint(vertex.x + p.x, vertex.y + p.y);
            if (minp == null) {
                minp = new ZonePoint(p.x, p.y);
                maxp = new ZonePoint(p.x, p.y);
            }
            minp.x = Math.min(minp.x, p.x);
            minp.y = Math.min(minp.y, p.y);
            maxp.x = Math.max(maxp.x, p.x);
            maxp.y = Math.max(maxp.y, p.y);
        }
        maxp.x += gridSize;
        maxp.y += gridSize;
        if (getXMult(getQuadrant()) < 0) {
            int tmp;
            tmp = vertex.x - (maxp.x - vertex.x);
            maxp.x = vertex.x - (minp.x - vertex.x);
            minp.x = tmp;
        }
        if (getYMult(getQuadrant()) < 0) {
            int tmp;
            tmp = vertex.y - (maxp.y - vertex.y);
            maxp.y = vertex.y - (minp.y - vertex.y);
            minp.y = tmp;
        }
        int width = (int) (maxp.x - minp.x);
        int height = (int) (maxp.y - minp.y);
        minp.x -= 10;
        minp.y -= 10;
        width += 20;
        height += 20;
        return new Rectangle((int) minp.x, (int) minp.y, width, height);
    }

    public Area getArea() {
        if (path == null) {
            calcPath();
            if (path == null) {
                return new Area();
            }
        }
        return null;
    }
}
