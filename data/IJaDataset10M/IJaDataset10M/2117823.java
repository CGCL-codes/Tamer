package com.quickwcm.admin.ui.client.qui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.Event;

public class QDraggableOptions {

    private Object appendTo;

    private String axis;

    private String[] cancel;

    private Object containment;

    private String cursor;

    private Integer cursorAtTop;

    private Integer cursorAtRight;

    private Integer cursorAtBottom;

    private Integer cursorAtLeft;

    private int delay = -1;

    private int distance = -1;

    private int gridX = -1;

    private int gridY = -1;

    private Object handle;

    private String strHelper;

    private QFunction fnHelper;

    private float opacity = 33;

    private Boolean revert;

    private Boolean scroll;

    private int scrollSensitivity = -1;

    private int scrollSpeed = -1;

    private Object snap;

    private String snapMode;

    private int snapTolerance = -1;

    private int zIndex = -9999;

    private List<QDragListener> dragListeners = new ArrayList<QDragListener>();

    public Object getAppendTo() {
        return appendTo;
    }

    public void setAppendTo(Object appendTo) {
        this.appendTo = appendTo;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public String[] getCancel() {
        return cancel;
    }

    public void setCancel(String[] cancel) {
        this.cancel = cancel;
    }

    public Object getContainment() {
        return containment;
    }

    public void setContainment(Object containment) {
        this.containment = containment;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Integer getCursorAtTop() {
        return cursorAtTop;
    }

    public void setCursorAtTop(Integer cursorAtTop) {
        this.cursorAtTop = cursorAtTop;
    }

    public Integer getCursorAtRight() {
        return cursorAtRight;
    }

    public void setCursorAtRight(Integer cursorAtRight) {
        this.cursorAtRight = cursorAtRight;
    }

    public Integer getCursorAtBottom() {
        return cursorAtBottom;
    }

    public void setCursorAtBottom(Integer cursorAtBottom) {
        this.cursorAtBottom = cursorAtBottom;
    }

    public Integer getCursorAtLeft() {
        return cursorAtLeft;
    }

    public void setCursorAtLeft(Integer cursorAtLeft) {
        this.cursorAtLeft = cursorAtLeft;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public Object getHandle() {
        return handle;
    }

    public void setHandle(Object handle) {
        this.handle = handle;
    }

    public QFunction getFnHelper() {
        return fnHelper;
    }

    public String getStrHelper() {
        return strHelper;
    }

    public void setHelper(String helper) {
        this.strHelper = helper;
        this.fnHelper = null;
    }

    public void setHelper(QFunction helper) {
        this.fnHelper = helper;
        this.strHelper = null;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public Boolean isRevert() {
        return revert;
    }

    public void setRevert(Boolean revert) {
        this.revert = revert;
    }

    public Boolean isScroll() {
        return scroll;
    }

    public void setScroll(Boolean scroll) {
        this.scroll = scroll;
    }

    public int getScrollSensitivity() {
        return scrollSensitivity;
    }

    public void setScrollSensitivity(int scrollSensitivity) {
        this.scrollSensitivity = scrollSensitivity;
    }

    public int getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public Object getSnap() {
        return snap;
    }

    public void setSnap(Object snap) {
        this.snap = snap;
    }

    public String getSnapMode() {
        return snapMode;
    }

    public void setSnapMode(String snapMode) {
        this.snapMode = snapMode;
    }

    public int getSnapTolerance() {
        return snapTolerance;
    }

    public void setSnapTolerance(int snapTolerance) {
        this.snapTolerance = snapTolerance;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int index) {
        zIndex = index;
    }

    public void addDragListeners(QDragListener listener) {
        dragListeners.add(listener);
    }

    public void removeDragListeners(QDragListener listener) {
        dragListeners.remove(listener);
    }

    protected void fireStart(Event e) {
        for (QDragListener listener : dragListeners) {
            listener.onStart(e);
        }
    }

    protected void fireStop(Event e) {
        for (QDragListener listener : dragListeners) {
            listener.onStop(e);
        }
    }

    protected void fireDrag(Event e, int top, int left, int absTop, int absLeft) {
        for (QDragListener listener : dragListeners) {
            listener.onDrag(e, top, left, absTop, absLeft);
        }
    }
}
