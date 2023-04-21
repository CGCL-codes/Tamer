package com.neolab.crm.client.app.widgets.hierarchy;

import com.google.gwt.user.client.*;

/**
 * @author Dusan
 */
public class ResizeListener implements EventListener {

    /**
     * currently selected TH element
     */
    private Element th;

    /**
     * start mouse position
     */
    private int startX;

    /**
     * current X position of the cursor
     */
    private int currentX;

    /**
     * the timer that check current cursor position and resizes columns
     */
    private ResizeTimer timer = new ResizeTimer(this);

    /**
     * link to the simple grid
     */
    private MillerColumnsWidget grid;

    /**
     * Creates an instnace of this class and saves the grid into the internal field.
     *
     * @param grid is a link to the owner.
     */
    public ResizeListener(MillerColumnsWidget grid) {
        this.grid = grid;
    }

    /**
     * This method handles all mouse events related to resizing.
     *
     * @param event is an event.
     */
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
            startResizing(event);
        } else if (DOM.eventGetType(event) == Event.ONMOUSEUP && th != null) {
            stopResizing();
        } else if (DOM.eventGetType(event) == Event.ONMOUSEOUT && th != null) {
            interruptResizing(event);
        }
        if (DOM.eventGetType(event) == Event.ONMOUSEMOVE) setCursor(event);
    }

    /**
     * Sets a cursor style.
     *
     * @param event is an event.
     */
    protected void setCursor(Event event) {
        Element th = getTh(event);
        if (this.th != null || isOverBorder(event, th)) {
            DOM.setStyleAttribute(DOM.eventGetTarget(event), "cursor", "e-resize");
            this.currentX = getPositionX(event);
            timer.schedule(20);
        } else DOM.setStyleAttribute(DOM.eventGetTarget(event), "cursor", "");
    }

    /**
     * This method interrupts resiszing.
     *
     * @param event is an event.
     */
    protected void interruptResizing(Event event) {
        int positionX = getPositionX(event);
        int positionY = getPositionY(event);
        Element thead = grid.getTHeadElement();
        int left = DOM.getAbsoluteLeft(thead);
        int top = DOM.getAbsoluteTop(thead);
        int width = getElementWidth(thead);
        int height = getElementHeight(thead);
        if (positionX < left || positionX > left + width || positionY < top || positionY > top + height) {
            th = null;
            timer.cancel();
        }
    }

    /**
     * This method normally stops resisng and changes column width.
     */
    protected void stopResizing() {
        resize();
        timer.cancel();
        th = null;
    }

    /**
     * Resizes selected and sibling columns.
     */
    protected void resize() {
        int position = this.currentX;
        int delta = position - startX;
        Element tr = DOM.getParent(th);
        int left = DOM.getAbsoluteLeft(th);
        int width = getElementWidth(th);
        Element sibling = null;
        int sign = 0;
        int thIndex = DOM.getChildIndex(tr, th);
        if (startX <= left + 2) {
            sign = 1;
            sibling = DOM.getChild(tr, thIndex - 1);
        } else if (startX >= left + width - 2) {
            sign = -1;
            sibling = DOM.getChild(tr, thIndex + 1);
        }
        int thExpectedWidth = width - sign * delta;
        int siblingExpectedWidth;
        int siblingIndex;
        if (sibling != null) {
            siblingExpectedWidth = getElementWidth(sibling) + sign * delta;
            siblingIndex = DOM.getChildIndex(tr, sibling);
        } else {
            siblingExpectedWidth = 0;
            siblingIndex = -1;
        }
        if (thExpectedWidth < 3 || siblingExpectedWidth < 3 && siblingIndex > -1) {
            th = null;
            return;
        }
        if (siblingIndex > -1) {
            grid.setColumnWidth(thIndex, thExpectedWidth);
            grid.setColumnWidth(siblingIndex, siblingExpectedWidth);
        }
        int thWidthNow = getElementWidth(th);
        int siblingWidthNow;
        if (siblingIndex > -1) siblingWidthNow = getElementWidth(sibling); else siblingWidthNow = 0;
        if (thWidthNow > thExpectedWidth) grid.setColumnWidth(thIndex, 2 * thExpectedWidth - thWidthNow);
        if (siblingWidthNow > siblingExpectedWidth && siblingIndex > -1) grid.setColumnWidth(siblingIndex, 2 * siblingExpectedWidth - siblingWidthNow);
        this.startX = position;
    }

    /**
     * This method starts column resizing.
     *
     * @param event is an event.
     */
    protected void startResizing(Event event) {
        if ("e-resize".equalsIgnoreCase(DOM.getStyleAttribute(DOM.eventGetTarget(event), "cursor"))) {
            th = getTh(event);
            startX = getPositionX(event);
            currentX = startX;
            if (!isOverBorder(event, th)) th = null;
        }
    }

    /**
     * Gets element width.
     *
     * @param element is an element
     * @return width in pixels
     */
    protected int getElementWidth(Element element) {
        return DOM.getElementPropertyInt(element, "offsetWidth");
    }

    /**
     * Gets element height.
     *
     * @param element is an element
     * @return height in pizels.
     */
    protected int getElementHeight(Element element) {
        return DOM.getElementPropertyInt(element, "offsetHeight");
    }

    /**
     * Gets mouse X position.
     *
     * @param event is an event.
     * @return X position in pixels.
     */
    protected int getPositionY(Event event) {
        return DOM.eventGetClientY(event);
    }

    /**
     * Gets mouse Y position.
     *
     * @param event is an event.
     * @return Y position in pixels.
     */
    protected int getPositionX(Event event) {
        return DOM.eventGetClientX(event);
    }

    /**
     * This method looks for the TH element starting from the element which produced the event.
     *
     * @param event is an event.
     * @return a TH element or <code>null</code> if there is no such element.
     */
    protected Element getTh(Event event) {
        Element element = DOM.eventGetTarget(event);
        while (element != null && !DOM.getElementProperty(element, "tagName").equalsIgnoreCase("th")) element = DOM.getParent(element);
        return element;
    }

    /**
     * This method detects whether the cursor is over the border between columns.
     *
     * @param event is an event.
     * @param th    is TH element.
     * @return a result of check.
     */
    protected boolean isOverBorder(Event event, Element th) {
        if (th != null) {
            int position = getPositionX(event);
            int left = DOM.getAbsoluteLeft(th);
            int width = getElementWidth(th);
            int index = DOM.getChildIndex(DOM.getParent(th), th);
            return position <= left + 1 && index > 0 || position >= left + width - 1 && index < DOM.getChildCount(DOM.getParent(th)) - 1;
        } else return false;
    }

    /**
     * Gets a link to the grid.
     *
     * @return a grid that initializes this listener.
     */
    public MillerColumnsWidget getGrid() {
        return grid;
    }

    protected static class ResizeTimer extends Timer {

        /**
         * resize listener that starts this timer
         */
        private ResizeListener listener;

        /**
         * Creates the timer.
         *
         * @param listener is a resize listener.
         */
        public ResizeTimer(ResizeListener listener) {
            this.listener = listener;
        }

        /**
         * See class docs.
         */
        public void run() {
            if (listener.th != null) {
                listener.resize();
            }
        }
    }
}
