package com.gc.gwt.ext.client;

import com.gc.gwt.wysiwyg.client.EditorUtils;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class SplitPane extends Composite {

    public static final int VERTICAL_SPLIT = 0;

    public static final int HORIZONTAL_SPLIT = 1;

    private int splitOrientation;

    private float leftRatio = 0;

    private int leftWidth = -1;

    private Grid mainContainer;

    private SimplePanel firstWidget;

    private SimplePanel secondWidget;

    private Divider divider;

    private int originalWidth;

    private int originalHeight;

    public SplitPane() {
        splitOrientation = VERTICAL_SPLIT;
        leftRatio = 0.5f;
        init();
    }

    public SplitPane(int splitOrientation) {
        this.splitOrientation = splitOrientation;
        leftRatio = 0.5f;
        init();
    }

    public SplitPane(int splitOrientation, float leftRatio) {
        this.splitOrientation = splitOrientation;
        this.leftRatio = leftRatio;
        init();
    }

    public SplitPane(int splitOrientation, int leftWidth) {
        this.splitOrientation = splitOrientation;
        this.leftWidth = leftWidth;
        init();
    }

    private void init() {
        firstWidget = new SimplePanel();
        DOM.setStyleAttribute(firstWidget.getElement(), "overflow", "hidden");
        firstWidget.setWidth("100%");
        firstWidget.setHeight("100%");
        divider = new Divider(splitOrientation);
        secondWidget = new SimplePanel();
        DOM.setStyleAttribute(secondWidget.getElement(), "overflow", "hidden");
        secondWidget.setWidth("100%");
        secondWidget.setHeight("100%");
        if (splitOrientation == VERTICAL_SPLIT) {
            mainContainer = new Grid(1, 3);
            mainContainer.setWidget(0, 0, firstWidget);
            mainContainer.setWidget(0, 1, divider);
            mainContainer.setWidget(0, 2, secondWidget);
            CellFormatter cellFormatter = mainContainer.getCellFormatter();
            cellFormatter.setHorizontalAlignment(0, 0, HasAlignment.ALIGN_LEFT);
            cellFormatter.setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);
            cellFormatter.setStyleName(0, 1, "SplitPaneDividerField");
            cellFormatter.setHorizontalAlignment(0, 1, HasAlignment.ALIGN_CENTER);
            cellFormatter.setVerticalAlignment(0, 1, HasAlignment.ALIGN_MIDDLE);
            cellFormatter.setHorizontalAlignment(0, 2, HasAlignment.ALIGN_LEFT);
            cellFormatter.setVerticalAlignment(0, 2, HasAlignment.ALIGN_TOP);
        } else {
            mainContainer = new Grid(3, 1);
            mainContainer.setWidget(0, 0, firstWidget);
            mainContainer.setWidget(1, 0, divider);
            mainContainer.setWidget(2, 0, secondWidget);
            CellFormatter cellFormatter = mainContainer.getCellFormatter();
            cellFormatter.setHorizontalAlignment(0, 0, HasAlignment.ALIGN_LEFT);
            cellFormatter.setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);
            cellFormatter.setStyleName(1, 0, "SplitPaneDividerField");
            cellFormatter.setHorizontalAlignment(1, 0, HasAlignment.ALIGN_CENTER);
            cellFormatter.setVerticalAlignment(1, 0, HasAlignment.ALIGN_MIDDLE);
            cellFormatter.setHorizontalAlignment(2, 0, HasAlignment.ALIGN_LEFT);
            cellFormatter.setVerticalAlignment(2, 0, HasAlignment.ALIGN_TOP);
        }
        mainContainer.setStyleName("SplitPane");
        mainContainer.setCellPadding(0);
        mainContainer.setCellSpacing(0);
        initWidget(mainContainer);
    }

    protected void onLoad() {
        super.onLoad();
        this.originalWidth = this.getOffsetWidth();
        this.originalHeight = this.getOffsetHeight();
    }

    private class Divider extends Image implements MouseDownHandler, MouseUpHandler, MouseMoveHandler {

        private boolean dragging = false;

        public Divider(int splitOrientation) {
            super("spacer.gif");
            if (splitOrientation == HORIZONTAL_SPLIT) {
                setStyleName("SplitPaneDivider-Horizontal");
            } else {
                setStyleName("SplitPaneDivider-Vertical");
            }
            addMouseDownHandler(this);
            addMouseUpHandler(this);
            addMouseMoveHandler(this);
        }

        protected void onLoad() {
            super.onLoad();
            CellFormatter cellFormatter = mainContainer.getCellFormatter();
            if (splitOrientation == VERTICAL_SPLIT) {
                if (leftWidth != -1) {
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "width", (int) leftWidth + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 1), "width", "5px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 2), "width", (int) (originalWidth - divider.getOffsetWidth() - leftWidth) + "px");
                } else {
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "width", (int) ((originalWidth - divider.getOffsetWidth()) * leftRatio) + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 1), "width", "5px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 2), "width", (int) ((originalWidth - divider.getOffsetWidth()) * (1 - leftRatio)) + "px");
                }
            } else {
                if (leftWidth != -1) {
                    Window.alert((int) (originalHeight - divider.getOffsetHeight() - leftWidth) + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "height", (int) leftWidth + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(1, 0), "height", "5px");
                    DOM.setStyleAttribute(cellFormatter.getElement(2, 0), "height", (int) (originalHeight - divider.getOffsetHeight() - leftWidth) + "px");
                } else {
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "height", (int) ((originalHeight - divider.getOffsetHeight()) * leftRatio) + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(1, 0), "height", "5px");
                    DOM.setStyleAttribute(cellFormatter.getElement(2, 0), "height", (int) ((originalHeight - divider.getOffsetHeight()) * (1 - leftRatio)) + "px");
                }
            }
        }

        public void onMouseDown(MouseDownEvent event) {
            dragging = true;
            DOM.setCapture(this.getElement());
        }

        public void onMouseUp(MouseUpEvent event) {
            dragging = false;
            DOM.releaseCapture(this.getElement());
        }

        public void onMouseMove(MouseMoveEvent event) {
            if (dragging) {
                CellFormatter cellFormatter = mainContainer.getCellFormatter();
                int newFirstWidth;
                if (splitOrientation == VERTICAL_SPLIT) {
                    newFirstWidth = EditorUtils.parseInt(DOM.getStyleAttribute(cellFormatter.getElement(0, 0), "width")) + event.getX();
                    if (newFirstWidth >= originalWidth) {
                        newFirstWidth = originalWidth;
                    }
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "width", normalizeMin(newFirstWidth) + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 2), "width", normalizeMin(originalWidth - newFirstWidth - divider.getOffsetWidth()) + "px");
                } else {
                    newFirstWidth = EditorUtils.parseInt(DOM.getStyleAttribute(cellFormatter.getElement(0, 0), "height")) + event.getY();
                    if (newFirstWidth >= originalHeight) {
                        newFirstWidth = originalHeight;
                    }
                    DOM.setStyleAttribute(cellFormatter.getElement(0, 0), "height", normalizeMin(newFirstWidth) + "px");
                    DOM.setStyleAttribute(cellFormatter.getElement(2, 0), "height", normalizeMin(originalHeight - newFirstWidth - divider.getOffsetHeight()) + "px");
                }
            }
        }

        private int normalizeMin(int in) {
            if (in <= 0) {
                return 0;
            } else {
                return in;
            }
        }

        /**
     * Overriden to block the browser's default behaviour.
     */
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            DOM.eventPreventDefault(event);
        }
    }

    public void setFirstWidget(Widget widget) {
        firstWidget.clear();
        firstWidget.setWidget(widget);
    }

    public void setPadding(int padding) {
        mainContainer.setCellPadding(padding);
        CellFormatter cellFormatter = mainContainer.getCellFormatter();
        if (splitOrientation == VERTICAL_SPLIT) {
            DOM.setStyleAttribute(cellFormatter.getElement(0, 1), "padding", "0px");
        } else {
            DOM.setStyleAttribute(cellFormatter.getElement(1, 0), "padding", "0px");
        }
    }

    public void setSecondWidget(Widget widget) {
        secondWidget.clear();
        secondWidget.setWidget(widget);
    }
}
