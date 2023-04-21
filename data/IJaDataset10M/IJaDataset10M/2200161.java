package com.kenstevens.stratdom.control;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.main.Constants;
import com.kenstevens.stratdom.model.Data;
import com.kenstevens.stratdom.model.Game;
import com.kenstevens.stratdom.model.Sector;
import com.kenstevens.stratdom.model.Unit;
import com.kenstevens.stratdom.model.World;
import com.kenstevens.stratdom.site.UnitMover;
import com.kenstevens.stratdom.ui.MapImageManager;
import com.kenstevens.stratdom.ui.SectorCoords;
import com.kenstevens.stratdom.ui.SectorCoordsSelection;
import com.kenstevens.stratdom.ui.SelectEvent;
import com.kenstevens.stratdom.ui.Selection;
import com.kenstevens.stratdom.ui.UnitSelection;
import com.kenstevens.stratdom.ui.Selection.Source;

@Scope("prototype")
@Component
public class MapCanvasControl implements Observer, TopLevelController {

    private final Canvas canvas;

    private UnitTableControl unitTableControl;

    private Point origin = new Point(0, 0);

    @Autowired
    private MapImageManager mapImageManager;

    @Autowired
    private SelectEvent selectEvent;

    @Autowired
    private UnitMover unitMover;

    @Autowired
    private Data db;

    public MapCanvasControl(Canvas canvas) {
        this.canvas = canvas;
        setCanvasListeners();
    }

    public void setUnitTableControl(UnitTableControl unitTableControl) {
        this.unitTableControl = unitTableControl;
    }

    public void setActiveUnits(List<Unit> units, boolean scrollToSeeUnit) {
        mapImageManager.revert();
        mapImageManager.selectActiveUnits(units);
        if (scrollToSeeUnit) {
            scrollToSeeUnit(units.get(0));
        }
        redraw();
    }

    private SectorCoords getSector(int xpoint, int ypoint) {
        return new SectorCoords(xpoint / Constants.IMG_PIXELS, db.getBoardSize() - (ypoint / Constants.IMG_PIXELS) - 1);
    }

    public void setActiveLocation(SectorCoords coords, boolean scrollToSeeLocation) {
        mapImageManager.revert();
        mapImageManager.selectActiveLocation(coords);
        if (scrollToSeeLocation) {
            scrollToSeeLocation(coords);
        }
        redraw();
    }

    public void setControllers() {
        db.addObserver(this);
        selectEvent.addObserver(this);
    }

    public void setContents() {
        update(db, this);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg instanceof Game && db.getWorld() != null) {
            db.getWorld().addObserver(this);
            origin = new Point(0, 0);
            mapImageManager.buildImage();
            setScrollArea();
            redraw();
            return;
        }
        if (observable instanceof World) {
            mapImageManager.buildImage();
            redraw();
            return;
        }
        if (observable instanceof SelectEvent) {
            Selection selection = (Selection) arg;
            Source source = selection.getSource();
            if (selection instanceof UnitSelection) {
                UnitSelection unitSelection = (UnitSelection) selection;
                List<Unit> units = unitSelection.getUnits();
                boolean scrollToSeeUnit = false;
                if (source == Source.UNIT_TAB) {
                    scrollToSeeUnit = true;
                }
                setActiveUnits(units, scrollToSeeUnit);
            } else if (selection instanceof SectorCoordsSelection) {
                SectorCoordsSelection sectorCoordsSelection = (SectorCoordsSelection) selection;
                SectorCoords coords = sectorCoordsSelection.getSectorCoords();
                boolean scrollToSeeLocation = true;
                if (source == Source.CANVAS_SELECT) {
                    scrollToSeeLocation = false;
                }
                setActiveLocation(coords, scrollToSeeLocation);
            }
        }
    }

    private void scrollToSeeUnit(Unit unit) {
        scrollToSeeLocation(unit.getCoords());
    }

    private void scrollToSeeLocation(SectorCoords coords) {
        Rectangle mapBounds = mapImageManager.getBounds();
        ScrollBar hBar = canvas.getHorizontalBar();
        Rectangle client = canvas.getClientArea();
        int newX = (coords.x - 20) * Constants.IMG_PIXELS;
        if (newX < 0) {
            newX = 0;
        }
        if (newX + client.width > mapBounds.width) {
            newX = mapBounds.width - client.width;
        }
        int hSelection = newX;
        moveHorizontal(mapBounds, hSelection);
        hBar.setSelection(hSelection);
        int newY = ((db.getBoardSize() - coords.y - 1) - 20) * Constants.IMG_PIXELS;
        if (newY < 0) {
            newY = 0;
        }
        if (newY + client.height > mapBounds.height) {
            newY = mapBounds.height - client.height;
        }
        ScrollBar vBar = canvas.getVerticalBar();
        int vSelection = newY;
        moveVertical(mapBounds, vSelection);
        vBar.setSelection(vSelection);
    }

    public void redraw() {
        Display display = Display.getDefault();
        if (display.isDisposed()) return;
        display.asyncExec(new Runnable() {

            public void run() {
                if (canvas.isDisposed()) return;
                canvas.redraw();
            }
        });
    }

    private void moveHorizontal(Rectangle rect, int hSelection) {
        int destX = -hSelection - origin.x;
        canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
        origin.x = -hSelection;
    }

    private void moveVertical(Rectangle rect, int vSelection) {
        int destY = -vSelection - origin.y;
        canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
        origin.y = -vSelection;
    }

    private void selectSectorOrUnits(SectorCoords sectorCoords) {
        List<Unit> unitsInSector = db.getUnitList().unitsAt(sectorCoords);
        if (unitsInSector.size() == 1) {
            selectEvent.selectUnits(unitsInSector, Source.CANVAS_SELECT);
        } else {
            Sector sector = db.getWorld().getSector(sectorCoords);
            if (sector == null) {
                return;
            }
            if (sector.getType().equals(Sector.Type.transport)) {
                selectTransport(unitsInSector);
            } else {
                selectEvent.selectSectorCoords(sectorCoords, Source.CANVAS_SELECT);
            }
        }
    }

    private void selectTransport(List<Unit> unitsInSector) {
        for (Unit unit : unitsInSector) {
            if (unit.getType().equals(Unit.Type.TRANSPORT)) {
                selectEvent.selectUnit(unit, Source.CANVAS_SELECT);
                break;
            }
        }
    }

    public void setCanvasListeners() {
        canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                SectorCoords coords = getSector(e.x - origin.x, e.y - origin.y);
                if (e.button == 1) {
                    List<Unit> activeUnits = unitTableControl.getSelectedUnits();
                    if (!activeUnits.isEmpty()) {
                        Sector sector = db.getWorld().getSector(coords);
                        if (sector.canAttack()) {
                            selectEvent.selectTargetSector(sector, Source.CANVAS_MOVE);
                        }
                        unitMover.moveUnits(activeUnits, coords);
                    }
                } else if (e.button == 3) {
                    selectSectorOrUnits(coords);
                }
            }
        });
        final ScrollBar hBar = canvas.getHorizontalBar();
        hBar.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int hSelection = hBar.getSelection();
                moveHorizontal(mapImageManager.getBounds(), hSelection);
            }
        });
        final ScrollBar vBar = canvas.getVerticalBar();
        vBar.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int vSelection = vBar.getSelection();
                moveVertical(mapImageManager.getBounds(), vSelection);
            }
        });
        canvas.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event e) {
                setScrollArea();
                redraw();
            }
        });
        canvas.addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event e) {
                GC gc = e.gc;
                mapImageManager.drawImage(gc, origin.x, origin.y);
                Rectangle rect = mapImageManager.getBounds();
                Rectangle client = canvas.getClientArea();
                int marginWidth = client.width - rect.width;
                if (marginWidth > 0) {
                    gc.fillRectangle(rect.width, 0, marginWidth, client.height);
                }
                int marginHeight = client.height - rect.height;
                if (marginHeight > 0) {
                    gc.fillRectangle(0, rect.height, client.width, marginHeight);
                }
            }
        });
    }

    private void setScrollArea() {
        final ScrollBar hBar = canvas.getHorizontalBar();
        final ScrollBar vBar = canvas.getVerticalBar();
        Rectangle rect = mapImageManager.getBounds();
        Rectangle client = canvas.getClientArea();
        hBar.setMaximum(rect.width);
        vBar.setMaximum(rect.height);
        hBar.setThumb(Math.min(rect.width, client.width));
        vBar.setThumb(Math.min(rect.height, client.height));
        int hPage = rect.width - client.width;
        int vPage = rect.height - client.height;
        int hSelection = hBar.getSelection();
        int vSelection = vBar.getSelection();
        if (hSelection >= hPage) {
            if (hPage <= 0) hSelection = 0;
            origin.x = -hSelection;
        }
        if (vSelection >= vPage) {
            if (vPage <= 0) vSelection = 0;
            origin.y = -vSelection;
        }
    }
}
