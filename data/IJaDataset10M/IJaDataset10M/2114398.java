package org.timepedia.chronoscope.client.browser;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import org.timepedia.chronoscope.client.ChronoscopeMenu;
import org.timepedia.chronoscope.client.Cursor;
import org.timepedia.chronoscope.client.HistoryManager;
import org.timepedia.chronoscope.client.InfoWindow;
import org.timepedia.chronoscope.client.canvas.Bounds;
import org.timepedia.chronoscope.client.canvas.View;
import org.timepedia.chronoscope.client.gss.GssProperties;
import org.timepedia.chronoscope.client.util.DateFormatter;
import org.timepedia.chronoscope.client.util.PortableTimer;
import org.timepedia.chronoscope.client.util.PortableTimerTask;
import org.timepedia.chronoscope.client.util.date.DateFormatterFactory;
import org.timepedia.chronoscope.client.util.date.GWTDateFormatter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class GwtView extends View implements DOMView {

    protected abstract static class BrowserTimer extends Timer implements PortableTimer {
    }

    protected String targetElementId;

    protected Element element;

    public GwtView() {
        if (GWT.isClient()) {
            DateFormatterFactory.setDateFormatterFactory(new DateFormatterFactory() {

                private DateFormatter blankFormatter = new GWTDateFormatter("");

                private DateFormatter previousFormatter;

                private String previous = "";

                public DateFormatter getDateFormatter(String format) {
                    if (null == format || "".equals(format) || "undefined".equals(format) || "null".equals(format)) {
                        return blankFormatter;
                    }
                    if (!previous.equals(format)) {
                        previous = format;
                        previousFormatter = new GWTDateFormatter(format);
                    }
                    return previousFormatter;
                }
            });
            HistoryManager.setHistoryManagerImpl(new HistoryManager.HistoryManagerImpl() {

                public void push(String historyToken) {
                    History.newItem(historyToken);
                }
            });
        }
    }

    public void dispose() {
        super.dispose();
        if (null != element && element.hasParentElement()) {
            element.removeFromParent();
        }
        targetElementId = null;
    }

    /**
   * Creates a PortableTimer based on GWT's Timer class.
   */
    public PortableTimer createTimer(final PortableTimerTask run) {
        return new BrowserTimer() {

            public void cancelTimer() {
                cancel();
            }

            public double getTime() {
                return System.currentTimeMillis();
            }

            public void run() {
                run.run(this);
            }
        };
    }

    public static void positionDivElement(Element div, Bounds bounds) {
        DOM.setStyleAttribute(div, "width", "" + (int) bounds.width + "px");
        DOM.setStyleAttribute(div, "height", "" + (int) bounds.height + "px");
        DOM.setStyleAttribute(div, "top", (int) bounds.y + "px");
        DOM.setStyleAttribute(div, "left", (int) bounds.x + "px");
    }

    public static void positionCanvasElement(Element can, Bounds bounds) {
        DOM.setElementPropertyInt(can, "width", (int) bounds.width);
        DOM.setElementPropertyInt(can, "height", (int) bounds.height);
    }

    public static void initDivElement(Element div, Bounds bounds) {
        DOM.setStyleAttribute(div, "overflow", "hidden");
        DOM.setStyleAttribute(div, "visibility", "visible");
    }

    protected static int getClientHeightRecursive(Element element) {
        int height = DOM.getElementPropertyInt(element, "clientHeight");
        if (height != 0) {
            return height;
        }
        Element parent = DOM.getParent(element);
        if (parent != null) {
            return getClientHeightRecursive(parent);
        }
        return 600;
    }

    protected static int getClientWidthRecursive(Element element) {
        int width = DOM.getElementPropertyInt(element, "clientWidth");
        if (width != 0) {
            return width;
        }
        Element parent = DOM.getParent(element);
        if (parent != null) {
            return getClientWidthRecursive(parent);
        }
        return 800;
    }

    protected void initTargetElementId(Element target) {
        targetElementId = DOM.getElementAttribute(target, "id");
        if (targetElementId == null || targetElementId.isEmpty()) {
            targetElementId = getViewId();
            this.element.setId(targetElementId);
        }
        log("initialized target element id=" + targetElementId);
    }

    /**
   * If the DOM element containing the canvas is not visible, we first scroll it
   * into view
   */
    public void ensureViewVisible() {
        super.ensureViewVisible();
        DOM.scrollIntoView(DOM.getElementById(targetElementId));
    }

    /**
   * Create a menu and return it
   */
    public ChronoscopeMenu createChronoscopeMenu(int x, int y) {
        return menuFactory.createChronoscopeMenu(x, y);
    }

    /**
   * Opens an HTML popup info window at the given screen coordinates (within the
   * plot bounds)
   * 
   * It sets the same font family, size, color and bgcolor defined for markers, if
   * you wanted override them use the css selector div.chrono-infoWindow-content.
   * 
   * FIXME: (MCM) this should be a unique instance of popup: ask Shawn
   */
    public InfoWindow createInfoWindow(String html, double x, double y) {
        final PopupPanel pp = new DecoratedPopupPanel(true);
        pp.addStyleName("chrono-infoWindow");
        Widget content = new HTML(html);
        content.setStyleName("chrono-infoWindow-content");
        pp.setWidget(content);
        pp.setPopupPosition(getElement().getAbsoluteLeft() + (int) x, getElement().getAbsoluteTop() + (int) y);
        GssProperties markerProperties = gssContext.getPropertiesBySelector("marker");
        if (markerProperties != null) {
            pp.getElement().getStyle().setBackgroundColor(markerProperties.bgColor.toString());
            pp.getElement().getStyle().setColor(markerProperties.color.toString());
            pp.getElement().getStyle().setProperty("fontFamily", markerProperties.fontFamily.toString());
            pp.getElement().getStyle().setProperty("fontSize", markerProperties.fontSize.toString());
            pp.getElement().getStyle().setPadding(5, Unit.PX);
        }
        pp.getElement().getStyle().setZIndex(9999);
        pp.show();
        return new BrowserInfoWindow(this, pp);
    }

    /**
   * Overridden to disable double buffering
   */
    @Override
    public void flipCanvas() {
    }

    /**
   * The DIV containing the canvas and other misc elements
   */
    public Element getElement() {
        return element;
    }

    public Element getGssCssElement() {
        return ((BrowserGssContext) gssContext).getElement();
    }

    public void setCursor(Cursor cursor) {
        switch(cursor) {
            case CLICKABLE:
                setCursorImpl("pointer");
                break;
            case SELECTING:
                setCursorImpl("text");
                break;
            case DRAGGABLE:
                setCursorImpl("move");
                break;
            case DRAGGING:
                setCursorImpl("move");
                break;
            default:
                setCursorImpl("default");
        }
    }

    protected void setCursorImpl(String cssCursor) {
        getElement().getStyle().setProperty("cursor", cssCursor);
    }

    public native double remainder(double numerator, double modulus);

    private void log(String msg) {
        System.out.println("GwtView> " + msg);
    }
}
