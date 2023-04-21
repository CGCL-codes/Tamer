package org.freelords.forms.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.freelords.animation.DirtyRectangles;
import org.freelords.animation.events.AnimatedEvent;
import org.freelords.entity.EntityId;
import org.freelords.game.Game;
import org.freelords.util.geom.OverlapCalculator;
import org.freelords.util.geom.Point;

public class MapCanvas implements PaintListener, ControlListener {

    private Canvas canvas;

    private Game game;

    private BackgroundLayerWithZoom backgroundLayer;

    private java.util.List<Layer> foregroundLayers;

    private View view;

    private View oldView;

    private Image backgroundImg;

    private Image completeImg;

    private AnimatedEvent defaultEvent = AnimatedEvent.IDLE;

    private AnimatedEvent currentAnimation = null;

    public MapCanvas(Composite parent, BackgroundLayerWithZoom bl, Layer... foregroundLayers) {
        this.canvas = new Canvas(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_BACKGROUND);
        this.backgroundLayer = bl;
        this.foregroundLayers = Arrays.asList(foregroundLayers);
        SelectionListener repaintAfterScroll = new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                MapCanvas.this.canvas.redraw();
            }
        };
        canvas.getHorizontalBar().addSelectionListener(repaintAfterScroll);
        canvas.getVerticalBar().addSelectionListener(repaintAfterScroll);
        canvas.addPaintListener(this);
        canvas.addControlListener(this);
        this.canvas.setData(MapCanvas.class.toString(), this);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static MapCanvas getMapCanvas(Control can) {
        MapCanvas mapCanvas = (MapCanvas) can.getData(MapCanvas.class.toString());
        if (mapCanvas == null) {
            throw new IllegalArgumentException("Control does not contain layers");
        }
        return mapCanvas;
    }

    public BackgroundLayerWithZoom getBackgroundLayer() {
        return backgroundLayer;
    }

    public BackgroundLayerWithZoom getZooms() {
        return backgroundLayer;
    }

    public void setZoomLevel(int zoomLevel) {
        org.freelords.util.geom.Rectangle relWindowPixels = view.getRelWindowPixels();
        Point centerTile = view.relWindowPixelToTile(new Point(relWindowPixels.getX() + relWindowPixels.getWidth() / 2, relWindowPixels.getY() + relWindowPixels.getHeight() / 2));
        backgroundLayer.setZoomLevel(zoomLevel);
        if (backgroundImg != null) {
            backgroundImg.dispose();
            backgroundImg = null;
        }
        if (completeImg != null) {
            completeImg.dispose();
            completeImg = null;
        }
        oldView = null;
        setupScrollBars();
        Rectangle canvasBounds = canvas.getBounds();
        int x = 0;
        int y = 0;
        view = backgroundLayer.createView(new org.freelords.util.geom.Rectangle(x, y, canvasBounds.width, canvasBounds.height));
        view.focusOnTile(centerTile);
    }

    public void focusOn(Point tile) {
        View newView = cloneView();
        newView.focusOnTile(tile);
        org.freelords.util.geom.Rectangle outterPixels = newView.getAbsWindowPixels();
        getCanvas().getHorizontalBar().setSelection(outterPixels.getX());
        getCanvas().getVerticalBar().setSelection(outterPixels.getY());
        redraw();
    }

    public View getView() {
        return view;
    }

    public void setDefaultEvent(AnimatedEvent event) {
        this.defaultEvent = event;
    }

    public void resetView() {
        oldView = null;
    }

    public View cloneView() {
        if (view != null) {
            return backgroundLayer.createView(view.getAbsWindowPixels());
        } else {
            throw new NullPointerException("We haven't created a view yet");
        }
    }

    public void paintControl(PaintEvent e) {
        Canvas canvas = (Canvas) e.widget;
        assert this.canvas == canvas;
        if (game == null) {
            return;
        }
        Canvas can = (Canvas) e.widget;
        Rectangle bounds = can.getBounds();
        int x = can.getHorizontalBar() == null ? 0 : can.getHorizontalBar().getSelection();
        int y = can.getVerticalBar() == null ? 0 : can.getVerticalBar().getSelection();
        org.freelords.util.geom.Rectangle window = new org.freelords.util.geom.Rectangle(x, y, bounds.width, bounds.height);
        oldView = view;
        if (oldView != null && oldView.getAbsOutPixels().contains(window)) {
            view = oldView;
            oldView.reuse(window);
        } else {
            view = backgroundLayer.createView(window);
        }
        org.freelords.util.geom.Rectangle viewAbsRect = view.getAbsOutPixels();
        if (backgroundImg == null || backgroundImg.getBounds().width != viewAbsRect.getWidth() || backgroundImg.getBounds().height != viewAbsRect.getHeight()) {
            if (backgroundImg != null) {
                backgroundImg.dispose();
            }
            if (completeImg != null) {
                completeImg.dispose();
            }
            backgroundImg = new Image(Display.getCurrent(), viewAbsRect.getWidth(), viewAbsRect.getHeight());
            completeImg = new Image(Display.getCurrent(), viewAbsRect.getWidth(), viewAbsRect.getHeight());
            oldView = null;
        }
        DirtyRectangles dirtyRects = new DirtyRectangles();
        Map<EntityId, AnimatedEvent> animations = new HashMap<EntityId, AnimatedEvent>();
        animations.put(null, defaultEvent);
        if (currentAnimation != null) {
            for (EntityId id : currentAnimation.getFocusEntities()) {
                animations.put(id, currentAnimation);
            }
        }
        if (oldView == view) {
        } else if (oldView != null && oldView.getAbsOutPixels().intersects(view.getAbsOutPixels())) {
            OverlapCalculator oc = new OverlapCalculator(oldView.getAbsOutPixels(), view.getAbsOutPixels());
            GC flipGc = new GC(completeImg);
            List<org.freelords.util.geom.Rectangle> clippingRects = new ArrayList<org.freelords.util.geom.Rectangle>();
            Point offset = view.getAbsOutPixels().getTopLeft();
            for (org.freelords.util.geom.Rectangle absPixelRect : oc.getAllRectangles()) {
                clippingRects.add(new org.freelords.util.geom.Rectangle(absPixelRect.getX() - offset.getX(), absPixelRect.getY() - offset.getY(), absPixelRect.getWidth(), absPixelRect.getHeight()));
            }
            DrawSurface surface = new DrawSurface(flipGc, view, game, dirtyRects, animations);
            backgroundLayer.draw(surface, clippingRects);
            Point newTopLeft = view.getAbsOutPixels().getTopLeft();
            Point oldTopLeft = oldView.getAbsOutPixels().getTopLeft();
            org.freelords.util.geom.Rectangle oldBounds = oldView.getAbsOutPixels();
            flipGc.drawImage(backgroundImg, 0, 0, oldBounds.getWidth(), oldBounds.getHeight(), oldTopLeft.getX() - newTopLeft.getX(), oldTopLeft.getY() - newTopLeft.getY(), oldBounds.getWidth(), oldBounds.getHeight());
            flipGc.dispose();
            Image temp = backgroundImg;
            backgroundImg = completeImg;
            completeImg = temp;
        } else {
            GC backgroundGc = new GC(backgroundImg);
            DrawSurface surface = new DrawSurface(backgroundGc, view, game, dirtyRects, animations);
            backgroundLayer.draw(surface);
            backgroundGc.dispose();
        }
        GC gc = new GC(completeImg);
        gc.drawImage(backgroundImg, 0, 0);
        DrawSurface surface = new DrawSurface(gc, view, game, dirtyRects, animations);
        for (Layer layer : foregroundLayers) {
            layer.draw(surface);
        }
        org.freelords.util.geom.Rectangle relWinP = view.getRelWindowPixels();
        e.gc.drawImage(completeImg, relWinP.getX(), relWinP.getY(), relWinP.getWidth(), relWinP.getHeight(), 0, 0, relWinP.getWidth(), relWinP.getHeight());
        gc.dispose();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void redraw() {
        canvas.redraw();
    }

    public void controlMoved(ControlEvent e) {
    }

    public void controlResized(ControlEvent e) {
        Canvas canvas = (Canvas) e.widget;
        assert this.canvas == canvas;
        setupScrollBars();
    }

    public void setupScrollBars() {
        if (game != null) {
            int width = backgroundLayer.getAbsoluteWidth(game.getCurrentMap().getWidth() + 1) - canvas.getBounds().width;
            int height = backgroundLayer.getAbsoluteHeight(game.getCurrentMap().getHeight() + 1) - canvas.getBounds().height;
            canvas.getHorizontalBar().setMaximum(width <= 0 ? 1 : width);
            canvas.getVerticalBar().setMaximum(height <= 0 ? 1 : height);
            canvas.getHorizontalBar().setPageIncrement(64);
            canvas.getVerticalBar().setPageIncrement(32);
        }
    }

    public Game getGame() {
        return game;
    }

    public int getZoomLevel() {
        return backgroundLayer.getZoomLevel();
    }
}
