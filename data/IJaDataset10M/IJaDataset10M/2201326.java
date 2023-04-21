package wood.view.viewport;

import static lawu.util.iterator.Iterators.iterator;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lawu.math.Vector;
import lawu.util.iterator.UniversalIterator;
import org.apache.log4j.Logger;
import wood.math.geom.Polygon;
import wood.math.geom.PolygonImpl;
import wood.math.geom.Rectangle;
import wood.model.entity.Entity;
import wood.model.proxy.ModelProxyInterface;
import wood.model.tileobject.ModelSnapshot;
import wood.model.tileobject.Snapshot;
import wood.model.tileobject.TileObject;
import wood.view.draw.Canvas;
import wood.view.draw.ClippableDrawable;
import wood.view.draw.objectdrawer.TileObjectDrawer;
import wood.view.draw.objectdrawer.TileObjectToDrawerRelationship;

public class AreaViewport<C extends Canvas> extends Viewport<C> {

    private static Logger logger = Logger.getLogger("wood.view.av");

    private final ModelProxyInterface model;

    private final TileObjectToDrawerRelationship<C> objRel;

    private final List<Vector> seenTiles = new ArrayList<Vector>();

    private final int gridWidth;

    private final int gridHeight;

    private final int tileSize;

    private final int tileBoundsWidth = 1;

    private final int tileBoundsHeight = 1;

    private final double nextTileXOffset = 0.75;

    private final double nextTileYOffset = 0.5;

    private boolean showGrid = false;

    private final ClippableDrawable<C> background;

    private final Map<TileObject, TileObjectDrawer<C>> drawerMap = new HashMap<TileObject, TileObjectDrawer<C>>();

    public AreaViewport(ModelProxyInterface model, TileObjectToDrawerRelationship<C> objRel, int gridWidth, int gridHeight, int tileSize) {
        this(model, objRel, gridWidth, gridHeight, tileSize, null);
    }

    public AreaViewport(ModelProxyInterface model, TileObjectToDrawerRelationship<C> objRel, int gridWidth, int gridHeight, int tileSize, ClippableDrawable<C> background) {
        this.model = model;
        this.objRel = objRel;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.tileSize = tileSize;
        this.background = background;
    }

    public void init(C canvas) {
        if (background != null) background.init(canvas);
        canvas.setFont(new Font("Arial", Font.BOLD, 15));
        canvas.setScaleFactor(tileSize);
        canvas.resetBrushPos();
        canvas.resetBrushPos();
        canvas.clearScaleFactor();
    }

    protected void doDraw(C canvas) {
        canvas.setScaleFactor(tileSize);
        ModelSnapshot e = model.getSnapshot();
        Entity avatar = e.getAvatar();
        Vector cornerToCorner = getBounds().getUpperRight().subtract(getBounds().getLowerLeft()).divide(tileSize);
        Vector tileCornerToCorner = new Vector(tileBoundsWidth, tileBoundsHeight);
        Vector center = cornerToCorner.subtract(tileCornerToCorner).divide(2);
        Vector avatarY = new Vector(0, avatar.getCurrentLocation().getY() * tileBoundsWidth);
        Vector avatarX = new Vector(nextTileXOffset, nextTileYOffset).multiply(avatar.getCurrentLocation().getX());
        Vector avatarOffset = center.subtract(avatarX.add(avatarY));
        if (background != null) {
            double width = getBounds().getWidth() / tileSize;
            double height = getBounds().getHeight() / tileSize;
            double x = getBounds().getX() / tileSize - (width - tileBoundsWidth) / 2 + avatarX.add(avatarY).getX();
            double y = getBounds().getY() / tileSize - (height - tileBoundsHeight) / 2 + avatarX.add(avatarY).getY();
            background.setClippingBounds(new Rectangle(x, y, width, height));
            background.draw(canvas);
        } else logger.warn("background is null");
        canvas.translateOrigin(avatarOffset);
        UniversalIterator<TileObject> visibles = e.getVisibleTileObjects();
        UniversalIterator<Vector> pts = e.getVisiblePointLocations();
        int c = 0;
        for (Vector v : pts) {
            if (!seenTiles.contains(v)) seenTiles.add(v);
            c++;
        }
        if (showGrid) {
            canvas.translateOrigin(new Vector(0, 0, getDepth() / 10));
            drawGrid(canvas, gridWidth, gridHeight, iterator(seenTiles));
            canvas.translateOrigin(new Vector(0, 0, -getDepth() / 10));
        }
        canvas.translateOrigin(new Vector(0, 0, 2 * getDepth() / 10));
        drawShroud(canvas, gridWidth, gridHeight, seenTiles, Color.BLACK);
        canvas.translateOrigin(new Vector(0, 0, -2 * getDepth() / 10));
        canvas.translateOrigin(new Vector(0, 0, 2 * getDepth() / 10));
        drawShroud(canvas, avatar.getCurrentLocation(), e.getRadiusOfVisibility(), new Color(0, 0, 0, (float) 0.5));
        canvas.translateOrigin(new Vector(0, 0, -2 * getDepth() / 10));
        canvas.translateOrigin(new Vector(0, 0, 3 * getDepth() / 10));
        TileObjectDrawer<C> avatarDrawer = null;
        for (TileObject obj : visibles) {
            if (obj == null) continue;
            TileObjectDrawer<C> drawer;
            if (!drawerMap.containsKey(obj)) {
                if (obj instanceof Snapshot) obj = ((Snapshot) obj).getSubject();
                drawer = objRel.getDrawer(obj);
                if (drawer == null) continue;
                drawer.init(canvas);
                drawerMap.put(obj, drawer);
            } else drawer = drawerMap.get(obj);
            drawer.setSubject(obj);
            if (obj.equals(e.getAvatar())) avatarDrawer = drawer;
        }
        for (TileObjectDrawer<C> drawer : drawerMap.values()) {
            if (drawer.isValid()) if (!drawer.equals(avatarDrawer)) drawer.draw(canvas); else drawerMap.remove(drawer);
        }
        if (avatarDrawer != null) avatarDrawer.draw(canvas);
        canvas.translateOrigin(new Vector(0, 0, -3 * getDepth() / 10));
        canvas.translateOrigin(avatarOffset.negate());
        canvas.resetBrushPos();
        canvas.clearScaleFactor();
    }

    private void drawShroud(C canvas, int i, int j, Iterable<Vector> visibles, Color color) {
        canvas.setBrushColor(color);
        HashSet<Vector> leaveAlone = new HashSet<Vector>();
        for (Vector t : visibles) leaveAlone.add(t);
        for (int x = 0; x < i; ++x) {
            for (int y = 0; y < j; ++y) {
                if (leaveAlone.contains(new Vector(x, (int) Math.floor(y - .5 * x)))) continue;
                Vector loc = new Vector(nextTileXOffset * x, tileBoundsHeight * y);
                if ((x & 1) == 0) loc = loc.add(new Vector(0, nextTileYOffset));
                canvas.drawPolygon(getHexagon(loc));
            }
        }
        canvas.clearBrushColor();
    }

    private void drawShroud(C canvas, Vector center, double radius, Color color) {
        canvas.setBrushColor(color);
        center = new Vector(.75 * center.getX() + .5, .5 * center.getX() + center.getY() + 1);
        canvas.translateOrigin(center);
        canvas.drawCD(radius, 2 * Math.max(gridWidth, gridHeight), 25, 2);
        canvas.translateOrigin(center.negate());
        canvas.clearBrushColor();
    }

    private void drawGrid(C canvas, int i, int j, UniversalIterator<Vector> visibles) {
        HashSet<Vector> leaveAlone = new HashSet<Vector>();
        for (Vector t : visibles) leaveAlone.add(t);
        for (int x = 0; x < i; ++x) {
            for (int y = 0; y < j; ++y) {
                if (!leaveAlone.contains(new Vector(x, (int) Math.floor(y - .5 * x)))) continue;
                Vector loc = new Vector(nextTileXOffset * x, tileBoundsHeight * y);
                if ((x & 1) == 0) loc = loc.add(new Vector(0, nextTileYOffset));
                drawTileOutline(canvas, loc, "(" + x + "," + (int) Math.floor(y - .5 * x) + ")");
            }
        }
        canvas.clearBrushColor();
    }

    private Polygon getHexagon(Vector loc) {
        Vector leftCenter = loc.add(new Vector(0, tileBoundsHeight / 2.0));
        Vector rightCenter = loc.add(new Vector(tileBoundsWidth, tileBoundsHeight / 2.0));
        Vector bottomLeft = loc.add(new Vector(0.25 * tileBoundsWidth, 0));
        Vector bottomRight = loc.add(new Vector(0.75 * tileBoundsWidth, 0));
        Vector topLeft = loc.add(new Vector(0.25 * tileBoundsWidth, tileBoundsHeight));
        Vector topRight = loc.add(new Vector(0.75 * tileBoundsWidth, tileBoundsHeight));
        return new PolygonImpl(iterator(leftCenter, bottomLeft, bottomRight, rightCenter, topRight, topLeft));
    }

    private void drawTileOutline(C canvas, Vector loc, String text) {
        canvas.setBrushColor(Color.RED);
        canvas.drawPolygonBorder(getHexagon(loc));
        canvas.setBrushColor(Color.CYAN);
        canvas.drawText(text, loc.getX() + .25, loc.getY() + .45);
    }

    public void enableGrid() {
        this.showGrid = true;
    }

    public void disableGrid() {
        this.showGrid = false;
    }

    public void toggleGrid() {
        this.showGrid = !this.showGrid;
    }
}
