package uk.ac.lkl.expresser.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import uk.ac.lkl.common.util.BoundingBox;
import uk.ac.lkl.common.util.Dimension;
import uk.ac.lkl.common.util.Location;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.AllocatedColor;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the panel where shape views, expression panels, and property lists are placed
 * 
 * @author Ken Kahn
 *
 */
public class ExpresserCanvasPanel extends AbsolutePanel {

    private int width;

    private int height;

    private int gridSize;

    private PickupDragController gridConstrainedDragController;

    private PickupDragController unconstrainedDragController;

    private ExpresserCanvas expresserCanvas;

    protected ExpresserModel model;

    private EventManager eventManager;

    protected int mouseX;

    protected int mouseY;

    public ExpresserCanvasPanel(int gridSize, int width, int height, ExpresserCanvas expresserCanvas, ExpresserModel model, boolean dragAndDropEnabled) {
        super();
        this.width = width;
        this.height = height;
        this.expresserCanvas = expresserCanvas;
        this.model = model;
        eventManager = new EventManager(this);
        setGridSize(gridSize);
        setPixelSize(width, height);
        if (dragAndDropEnabled) {
            enableDragAndDrop();
        }
        trackMousePosition();
    }

    private void trackMousePosition() {
        MouseMoveHandler handler = new MouseMoveHandler() {

            @Override
            public void onMouseMove(MouseMoveEvent event) {
                mouseX = event.getClientX();
                mouseY = event.getClientY();
            }
        };
        expresserCanvas.addMouseMoveHandler(handler);
    }

    private void enableDragAndDrop() {
        gridConstrainedDragController = new PickupDragController(this, true);
        GridConstrainedSelectionStyleDropController gridConstrainedDropController = new GridConstrainedSelectionStyleDropController(this, gridSize, gridSize);
        gridConstrainedDragController.registerDropController(gridConstrainedDropController);
        gridConstrainedDragController.setBehaviorMultipleSelection(true);
        unconstrainedDragController = new PickupDragController(this, true);
        gridConstrainedDragController.setBehaviorDragStartSensitivity(4);
        unconstrainedDragController.setBehaviorDragStartSensitivity(4);
    }

    public void recomputePixelSize() {
        setPixelSize(width, height);
    }

    @Override
    public void setPixelSize(int width, int height) {
        if (Configuration.isThumbNail()) {
            super.setPixelSize(width, height);
            return;
        }
        BoundingBox boundingBox = getBoundingBox();
        int minX = boundingBox.getMinX();
        expresserCanvas.setXOffset(Math.max(expresserCanvas.getXOffset(), -minX));
        int minY = boundingBox.getMinY();
        expresserCanvas.setYOffset(Math.max(expresserCanvas.getYOffset(), -minY));
        int widthNeeded = boundingBox.getWidth() - 1;
        if (widthNeeded > width) {
            width = widthNeeded;
        }
        int heightNeeded = boundingBox.getHeight() - 1;
        if (heightNeeded > height) {
            height = heightNeeded;
        }
        if (this.width == width && this.height == height) {
            return;
        }
        super.setPixelSize(width, height);
        if (this.height > 0 && this.width > 0) {
            adjustGridLines(width, height);
        }
        this.width = width;
        this.height = height;
    }

    private BoundingBox getBoundingBox() {
        Dimension preferredSize = expresserCanvas.getPreferredSize();
        BoundingBox canvasBoundingBox;
        int left = getAbsoluteLeft();
        int top = getAbsoluteTop();
        if (preferredSize == null) {
            canvasBoundingBox = new BoundingBox(left, top, width + left, height + top);
        } else {
            canvasBoundingBox = new BoundingBox(left, top, preferredSize.width + left, preferredSize.height + top);
        }
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof HasBoundingBox) {
                BoundingBox widgetBoundingBox = ((HasBoundingBox) widget).getBoundingBox();
                canvasBoundingBox.extendToInclude(widgetBoundingBox);
            }
        }
        return canvasBoundingBox;
    }

    public void setGridSize(int gridSize) {
        if (this.gridSize == gridSize) {
            return;
        }
        this.gridSize = gridSize;
        int widgetCount = getWidgetCount();
        List<LocatedWidget> widgetsToPutBack = new ArrayList<LocatedWidget>();
        for (int i = widgetCount - 1; i >= 0; i--) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                ((ShapeView) widget).setGridSize(gridSize);
            }
            if (!(widget instanceof VerticalGridLine || widget instanceof HorizontalGridLine)) {
                widgetsToPutBack.add(new LocatedWidget(widget, widget.getAbsoluteLeft(), widget.getAbsoluteTop()));
            }
            if (widget instanceof GridLine) {
                widget.removeFromParent();
            }
        }
        setPixelSize(width, height);
        if (gridSize >= 10) {
            addGridLines();
        }
        int canvasAbsoluteLeft = getAbsoluteLeft();
        int canvasAbsoluteTop = getAbsoluteTop();
        int size = widgetsToPutBack.size();
        for (int i = size - 1; i >= 0; i--) {
            LocatedWidget locatedWidget = widgetsToPutBack.get(i);
            add(locatedWidget.getWidget(), locatedWidget.getAbsoluteLeft() - canvasAbsoluteLeft, locatedWidget.getAbsoluteTop() - canvasAbsoluteTop);
        }
    }

    protected void addGridLines() {
        for (int i = gridSize - 1; i <= width; i += gridSize) {
            add(new VerticalGridLine(height), i, 0);
        }
        for (int j = gridSize - 1; j <= height; j += gridSize) {
            add(new HorizontalGridLine(width), 0, j);
        }
    }

    /**
     * called when canvas dimensions changed
     * adjusts the length of existing grid lines and
     * removes or adds grid lines as needed
     * @param deltaHeight 
     * @param deltaWidth 
     */
    protected void adjustGridLines(int newWidth, int newHeight) {
        int widgetCount = getWidgetCount();
        for (int i = widgetCount - 1; i >= 0; i--) {
            Widget widget = getWidget(i);
            if (widget instanceof VerticalGridLine) {
                if (getWidgetLeft(widget) >= newWidth) {
                    remove(widget);
                } else {
                    widget.setHeight(newHeight + "px");
                }
            }
            if (widget instanceof HorizontalGridLine) {
                if (getWidgetTop(widget) >= newHeight) {
                    remove(widget);
                } else {
                    widget.setWidth(newWidth + "px");
                }
            }
        }
        int firstX = (width / gridSize) * gridSize + gridSize - 1;
        for (int i = firstX; i <= newWidth; i += gridSize) {
            add(new VerticalGridLine(newHeight), i, 0);
        }
        int firstY = (height / gridSize) * gridSize + gridSize - 1;
        for (int j = firstY; j <= newHeight; j += gridSize) {
            add(new HorizontalGridLine(newWidth), 0, j);
        }
    }

    public MenuItem getGroupMenuItem(final PopupPanel popupMenu) {
        Command groupCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                groupSelectedWidgets();
            }
        };
        MenuItem menuItem = new MenuItem("Make a building block", true, groupCommand);
        return menuItem;
    }

    public MenuItem getUngroupMenuItem(final PopupPanel popupMenu, final GroupShapeView groupShapeView) {
        Command ungroupCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                groupShapeView.undoGroup();
            }
        };
        MenuItem menuItem = new MenuItem("Undo Building Block", true, ungroupCommand);
        return menuItem;
    }

    public MenuItem getCopyMenuItem(final PopupPanel popupMenu) {
        Command copyCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                copySelectedWidgets();
            }
        };
        return new MenuItem("Copy", true, copyCommand);
    }

    public MenuItem getDeleteMenuItem(final PopupPanel popupMenu) {
        Command deleteCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                deleteSelectedWidgets();
            }
        };
        return new MenuItem("Delete", true, deleteCommand);
    }

    public MenuItem getShowPropertiesItem(final PopupPanel popupMenu, final ShapeView shapeView) {
        Command command = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                Widget propertyList = shapeView.getPropertyList(unconstrainedDragController);
                int left = shapeView.getAbsoluteLeft() + shapeView.getOffsetWidth() - getAbsoluteLeft() + 4;
                int top = shapeView.getAbsoluteTop() - getAbsoluteTop();
                add(propertyList, left, top);
                unconstrainedDragController.makeDraggable(propertyList);
            }
        };
        MenuItem menuItem = new MenuItem("Show properties", true, command);
        return menuItem;
    }

    public MenuItem getMakePatternItem(final PopupPanel popupMenu, final GroupShapeView buildingBlockView) {
        Command command = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                int buildingBlockWidth = buildingBlockView.getOffsetWidth();
                PatternView patternView = new PatternView(buildingBlockView, new UnspecifiedTiedNumber(IntegerValue.ONE), new UnspecifiedTiedNumber(IntegerValue.ZERO), new UnspecifiedTiedNumber(IntegerValue.ZERO));
                int left = buildingBlockView.getAbsoluteLeft() - getAbsoluteLeft();
                int top = buildingBlockView.getAbsoluteTop() - getAbsoluteTop();
                add(patternView, left, top);
                patternView.setDraggable(gridConstrainedDragController);
                if (eventManager != null) {
                    BlockShape buildingBlockShape = buildingBlockView.getPatternShape(expresserCanvas);
                    eventManager.patternCreatedOrUpdated(left, top, patternView);
                    model.removeObject(buildingBlockShape);
                }
                PropertyList patternWizard = new PatternWizard(patternView, unconstrainedDragController);
                add(patternWizard, left + buildingBlockWidth + 4, top);
                gridConstrainedDragController.makeDraggable(patternWizard);
                gridConstrainedDragController.toggleSelection(buildingBlockView);
                gridConstrainedDragController.toggleSelection(patternView);
            }
        };
        return new MenuItem("Make Pattern", true, command);
    }

    public MenuItem getUndoPatternItem(final PopupPanel popupMenu, final PatternView patternView) {
        Command command = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                patternView.undoPattern();
            }
        };
        return new MenuItem("Undo Pattern", true, command);
    }

    public GroupShapeView groupSelectedWidgets() {
        Iterable<Widget> selectedWidgets = gridConstrainedDragController.getSelectedWidgets();
        ArrayList<ShapeView> shapes = new ArrayList<ShapeView>();
        for (Widget widget : selectedWidgets) {
            if (widget instanceof ShapeView) {
                shapes.add((ShapeView) widget);
            }
        }
        return addGroupShape(shapes);
    }

    /**
     * @param shapeViews
     * @return
     */
    public GroupShapeView addGroupShape(List<ShapeView> shapeViews) {
        GroupShapeView groupShape = new GroupShapeView(shapeViews);
        int left = groupShape.getSubShapesAbsoluteLeft();
        int top = groupShape.getSubShapesAbsoluteTop();
        add(groupShape, left - getAbsoluteLeft(), top - getAbsoluteTop());
        groupShape.setDraggable(gridConstrainedDragController);
        eventManager.groupCreated(left, top, groupShape);
        for (ShapeView shapeView : shapeViews) {
            BlockShape blockShape = shapeView.getPatternShape(expresserCanvas);
            model.removeObject(blockShape);
        }
        return groupShape;
    }

    public void copySelectedWidgets() {
        Iterable<Widget> selectedWidgets = gridConstrainedDragController.getSelectedWidgets();
        ArrayList<Widget> copies = new ArrayList<Widget>();
        for (Widget widget : selectedWidgets) {
            if (widget instanceof ShapeView) {
                ShapeView shapeView = (ShapeView) widget;
                ShapeView copy = shapeView.copy();
                copies.add(copy);
                int widgetHeight = widget.getOffsetHeight();
                int canvasLeft = getAbsoluteLeft();
                int canvasTop = getAbsoluteTop();
                int widgetLeft = widget.getAbsoluteLeft();
                int widgetTop = widget.getAbsoluteTop();
                add(copy, nearestGridX(widgetLeft - canvasLeft), nearestGridY(widgetTop - canvasTop + widgetHeight));
                copy.setDraggable(gridConstrainedDragController);
                eventManager.shapeCopied(shapeView, copy, expresserCanvas);
            }
        }
        gridConstrainedDragController.clearSelection();
        for (Widget clone : copies) {
            gridConstrainedDragController.toggleSelection(clone);
        }
    }

    protected int nearestGridX(int x) {
        return gridSize * (int) Math.round(((double) x) / gridSize);
    }

    protected int nearestGridY(int y) {
        return gridSize * (int) Math.round(((double) y) / gridSize);
    }

    public void deleteSelectedWidgets() {
        Iterable<Widget> selectedWidgets = gridConstrainedDragController.getSelectedWidgets();
        for (Widget widget : selectedWidgets) {
            remove(widget);
            if (widget instanceof ShapeView) {
                eventManager.shapeDeleted((ShapeView) widget, this);
            }
        }
    }

    public PickupDragController getGridConstrainedDragController() {
        return gridConstrainedDragController;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void selectWidgetsInRectangle(int rectangleLeft, int rectangleTop, int rectangleWidth, int rectangleHeight) {
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView && centerInside(widget.getElement().getOffsetLeft(), widget.getElement().getOffsetTop(), widget.getOffsetWidth(), widget.getOffsetHeight(), rectangleLeft, rectangleTop, rectangleWidth, rectangleHeight)) {
                gridConstrainedDragController.toggleSelection(widget);
            }
        }
    }

    private boolean centerInside(int widgetLeft, int widgetTop, int widgetWidth, int widgetHeight, int rectangleLeft, int rectangleTop, int rectangleWidth, int rectangleHeight) {
        int widgetX = widgetLeft + widgetWidth / 2;
        int widgetY = widgetTop + widgetHeight / 2;
        int rectangleRight = rectangleLeft + rectangleWidth;
        int rectangleBottom = rectangleTop + rectangleHeight;
        return widgetX > rectangleLeft && widgetX < rectangleRight && widgetY > rectangleTop && widgetY < rectangleBottom;
    }

    public void clearSelection() {
        gridConstrainedDragController.clearSelection();
    }

    public boolean containsShapeAt(int x, int y) {
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                if (((ShapeView) widget).contains(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Widget widgetContainingPoint(int x, int y) {
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof GridLine) {
                continue;
            }
            int left = widget.getAbsoluteLeft();
            if (x < left) {
                continue;
            }
            int right = left + widget.getOffsetWidth();
            if (x > right) {
                continue;
            }
            int top = widget.getAbsoluteTop();
            if (y < top) {
                continue;
            }
            int bottom = top + widget.getOffsetHeight();
            if (y > bottom) {
                continue;
            }
            return widget;
        }
        return null;
    }

    public PickupDragController getUnconstrainedDragController() {
        return unconstrainedDragController;
    }

    public ExpresserCanvas getExpresserCanvas() {
        return expresserCanvas;
    }

    public List<ShapeView> getShapeViewsBeingDragged() {
        return expresserCanvas.getShapeViewsBeingDragged();
    }

    public void updateTilesDisplayMode(Map<Location, ArrayList<AllocatedColor>> map) {
        int widgetCount = getWidgetCount();
        int xOffset = getAbsoluteLeft();
        int yOffset = getAbsoluteTop();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                ShapeView shapeView = (ShapeView) widget;
                shapeView.updateDisplay();
                shapeView.updateTilesDisplayMode(map, xOffset, yOffset);
                if (!expresserCanvas.isReadOnly()) {
                    shapeView.setDraggable(getGridConstrainedDragController());
                }
            }
        }
    }

    public List<ShapeView> getShapeViews() {
        ArrayList<ShapeView> shapeViews = new ArrayList<ShapeView>();
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                shapeViews.add((ShapeView) widget);
            }
        }
        return shapeViews;
    }

    public void removeAllSnaphots() {
        int widgetCount = getWidgetCount();
        for (int i = widgetCount - 1; i >= 0; i--) {
            Widget widget = getWidget(i);
            if (widget instanceof TileViewSnapShot) {
                remove(widget);
            }
        }
    }

    public List<TileView> getAllTileViews() {
        ArrayList<TileView> tileViews = new ArrayList<TileView>();
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                ShapeView shapeView = (ShapeView) widget;
                shapeView.addTileViews(tileViews);
            }
        }
        return tileViews;
    }

    public void removeAllTileViews() {
        int widgetCount = getWidgetCount();
        for (int i = widgetCount - 1; i >= 0; i--) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                remove(widget);
            }
        }
    }

    public void moveAllBy(int deltaX, int deltaY) {
        int widgetCount = getWidgetCount();
        for (int i = 0; i < widgetCount; i++) {
            Widget widget = getWidget(i);
            if (widget instanceof ShapeView) {
                int left = getWidgetLeft(widget) + deltaX;
                int top = getWidgetTop(widget) + deltaY;
                setWidgetPosition(widget, left, top);
            }
        }
    }

    public ExpresserModel getModel() {
        return model;
    }

    public void setModel(ExpresserModel model) {
        this.model = model;
    }

    /**
     * @param shapeView
     * @return true if shapeView is being dragged or its (super) container
     */
    public boolean isDragging(ShapeView shapeView) {
        List<ShapeView> shapeViewsBeingDragged = getShapeViewsBeingDragged();
        for (ShapeView shapeViewDragged : shapeViewsBeingDragged) {
            Widget ancestor = shapeView;
            while (ancestor != null) {
                if (ancestor == shapeViewDragged) {
                    return true;
                }
                ancestor = ancestor.getParent();
            }
        }
        return false;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Sets the popup menu underneath the widget unless there isn't room
     * 
     * @param popupMenu
     * @param widget
     *
     */
    public void setPopupPosition(PopupPanel popupMenu, Widget widget) {
        popupMenu.setPopupPosition(mouseX, mouseY);
    }

    @Override
    public void add(Widget widget, int left, int top) {
        super.add(widget, left, top);
        if (!(widget instanceof ExpressionPanel)) {
            return;
        }
        Widget menuBar = Expresser.getMenuBar();
        if (menuBar == null) {
            return;
        }
        int offsetHeight = getOffsetHeight();
        ModelRulesPanel modelRulesPanel = expresserCanvas.getModelRulesPanel();
        int nonCanvasHeight = modelRulesPanel == null ? 0 : modelRulesPanel.getOffsetHeight();
        int visibleCanvasHeight = offsetHeight - nonCanvasHeight;
        if (top > visibleCanvasHeight) {
            int widgetOffsetHeight = widget.getOffsetHeight();
            setWidgetPosition(widget, left, visibleCanvasHeight - widgetOffsetHeight);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXOffset() {
        return expresserCanvas.getXOffset();
    }

    public int getYOffset() {
        return expresserCanvas.getYOffset();
    }
}
