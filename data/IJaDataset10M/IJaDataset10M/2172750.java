package org.freelords.forms.map;

import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.google.inject.Inject;
import org.freelords.game.Game;
import org.freelords.util.geom.Point;
import org.freelords.util.geom.Rect;

/** 
  * This class handles most of the mini map (the map at the right upper corner
  * of the game screen) actions.
  *
  * <p>
  * This class cares about setting everything up, reacting to mouse clicks or scrolling, and
  * painting the map. The actual drawing of tiles etc. is delegated to various layers.
  * </p>
  * 
  * @author Jan Keller
  */
public class MiniMap implements ControlListener, PaintListener, MouseListener, MouseMoveListener {

    /** The canvas where we draw on. */
    private Canvas canvas;

    /** The layer that draws the background terrain. */
    @Inject
    private MiniMapBackgroundLayer backgroundLayer;

    /** The layer that draws cities and units on the map. */
    @Inject
    private MiniMapEntityLayer entityLayer;

    /** the game object that provides all data. */
    private Game game;

    /** Transforms between map tile and pixel coordinates. */
    private MiniMapView view;

    /** A rectangle that specifies which section of the map is seen in the main display. */
    private Rect mainMapTiles;

    /** A control used to center the main map on clicks. */
    private MainMapControl mainMap;

    /** true if the mouse button is pushed; moves the view whenever the mouse moves. */
    private boolean clicked;

    /** Nothing done, because the minimap is injected. */
    public MiniMap() {
        mainMapTiles = new Rect(0, 0, 1, 1);
    }

    /** Set up the minimap.
	  *
	  * @param parent the parent widget of the canvas we draw on.
	  * @param game the Game instance to fetch all sorts of data from
	  */
    public void init(Composite parent, Game game) {
        this.canvas = new Canvas(parent, SWT.NO_BACKGROUND);
        canvas.addMouseListener(this);
        canvas.addMouseMoveListener(this);
        canvas.addPaintListener(this);
        canvas.addControlListener(this);
        this.game = game;
    }

    /** Returns the canvas */
    public Canvas getCanvas() {
        return canvas;
    }

    public void setMainMapControl(MainMapControl mainMap) {
        this.mainMap = mainMap;
    }

    /** The main map is telling us that it has scrolled */
    public void mainMapScrolled(Rect mainMapTiles) {
        this.mainMapTiles = mainMapTiles;
        canvas.redraw();
    }

    /** Not used, called when the canvas is moved. */
    @Override
    public void controlMoved(ControlEvent e) {
    }

    /** Recreate all the views, images, layers etc. */
    public void controlResized(ControlEvent e) {
        Rectangle size = canvas.getBounds();
        size = new Rectangle(0, 0, size.width, size.height);
        view = new MiniMapView(game.getCurrentMap().getWidth(), game.getCurrentMap().getHeight(), size.width, size.height);
        backgroundLayer.setView(view);
        entityLayer.setView(view);
        canvas.redraw();
    }

    /** This paints the mini map (tiles, entities, main-map view rectangle) */
    @Override
    public void paintControl(PaintEvent e) {
        DrawSurface surf = new DrawSurface(e.gc, null, game, null);
        backgroundLayer.draw(surf);
        entityLayer.draw(surf);
        Rectangle upleft = view.tileToAbsPixels(mainMapTiles.getTopLeft());
        Rectangle downright = view.tileToAbsPixels(mainMapTiles.getBottomRight());
        Rectangle drawAt = new Rectangle(upleft.x, upleft.y, downright.x + downright.width - upleft.x, downright.y + downright.height - upleft.y);
        e.gc.drawRectangle(drawAt);
    }

    /** Not used, act on double click */
    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    /** Refocus the main-map and redraw the mini-map.
	  *
	  * We actually only do the first thing. Since the main map then signals to the
	  * mini map to redraw anyway, we save us the effort.
	  */
    @Override
    public void mouseDown(MouseEvent e) {
        if (e.button != 1) {
            return;
        }
        clicked = true;
        Point tile = view.absPixelToTile(new Point(e.x, e.y));
        mainMap.focusOn(tile);
        mainMap.getCanvas().redraw();
    }

    /** When mouse button is released, note this down. */
    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 1) {
            clicked = false;
        }
    }

    /** Scroll the minimap if the button is pushed. */
    @Override
    public void mouseMove(MouseEvent e) {
        if (clicked) {
            Point tile = view.absPixelToTile(new Point(e.x, e.y));
            mainMap.focusOn(tile);
            mainMap.getCanvas().redraw();
        }
    }
}
