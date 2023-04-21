package com.moesol.gwt.maps.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.moesol.gwt.maps.client.MapView;

/**
 * Control for panning and zooming the map.
 */
public class MapPanZoomControl extends Composite {

    static class Presenter {

        double calculateDelta(int panButtonDimension, int eventRelativeCoord, int maxPanPixels) {
            final double halfPanButtonDimension = panButtonDimension / 2.0;
            return ((eventRelativeCoord / halfPanButtonDimension) - 1.0) * maxPanPixels;
        }
    }

    private static MapPanZoomControlUiBinder uiBinder = GWT.create(MapPanZoomControlUiBinder.class);

    interface MapPanZoomControlUiBinder extends UiBinder<Widget, MapPanZoomControl> {
    }

    private Presenter m_presenter = new Presenter();

    private MapView m_map;

    private int m_maxPanPixels;

    private int m_millisBetweenConsecutiveActions;

    @UiField
    DivElement clickHighlight;

    @UiField
    HTML zoomInButton;

    @UiField
    HTML zoomOutButton;

    private Timer m_panButtonTimer = new Timer() {

        @Override
        public void run() {
            m_map.moveMapByPixels((int) m_dx, (int) m_dy);
        }
    };

    private Timer m_zoomInButtonTimer = new Timer() {

        @Override
        public void run() {
            m_map.zoomByFactor(1.05);
        }
    };

    private Timer m_zoomOutButtonTimer = new Timer() {

        @Override
        public void run() {
            m_map.zoomByFactor(0.952380952);
        }
    };

    private boolean m_panning;

    /**
	 * @param map The map to plug this control into.
	 * @param maxPanPixels The maximum number of pixels to
	 * move the map each time a pan occurs. 
	 * @param millisBetweenPans The milliseconds between consecutive
	 * pans that occur while the mouse is down on the pan portion
	 * of the control.
	 */
    public void initPanVals(int maxPanPixels, int millisBetweenPans) {
        m_maxPanPixels = maxPanPixels;
        m_millisBetweenConsecutiveActions = millisBetweenPans;
    }

    public void setMapView(MapView map) {
        m_map = map;
    }

    public MapPanZoomControl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
	 * @param map The map to plug this control into.
	 * @param maxPanPixels The maximum number of pixels to
	 * move the map each time a pan occurs. 
	 * @param millisBetweenPans The milliseconds between consecutive
	 * pans that occur while the mouse is down on the pan portion
	 * of the control.
	 */
    public MapPanZoomControl(MapView map, int maxPanPixels, int millisBetweenPans) {
        m_map = map;
        m_maxPanPixels = maxPanPixels;
        m_millisBetweenConsecutiveActions = millisBetweenPans;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("panButton")
    public void onPanButtonMouseUp(MouseUpEvent e) {
        e.preventDefault();
        stopPanLoop();
    }

    @UiHandler("panButton")
    public void onPanButtonMouseOut(MouseOutEvent e) {
        e.preventDefault();
        stopPanLoop();
    }

    @UiHandler("panButton")
    public void onPanButtonMouseDown(MouseDownEvent e) {
        e.preventDefault();
        updateVelocity(e);
        startPanLoop();
    }

    @UiHandler("panButton")
    public void onPanButtonMouseMove(MouseMoveEvent e) {
        e.preventDefault();
        if (m_panning) {
            updateVelocity(e);
        }
    }

    @UiField
    HTML panButton;

    private double m_dx;

    private double m_dy;

    @SuppressWarnings("rawtypes")
    private void updateVelocity(MouseEvent e) {
        int eventRelativeX = e.getRelativeX(panButton.getElement());
        int eventRelativeY = e.getRelativeY(panButton.getElement());
        final int panButtonWidth = panButton.getOffsetWidth();
        final int panButtonHeight = panButton.getOffsetHeight();
        if (eventRelativeX > 0 && eventRelativeX < panButtonWidth && eventRelativeY > 0 && eventRelativeY < panButtonHeight) {
            final Style clickHighlightStyle = clickHighlight.getStyle();
            clickHighlightStyle.setDisplay(Display.BLOCK);
            clickHighlightStyle.setLeft(eventRelativeX - (clickHighlight.getClientWidth() / 3), Unit.PX);
            clickHighlightStyle.setTop(eventRelativeY - (clickHighlight.getClientHeight() / 3), Unit.PX);
            m_dx = m_presenter.calculateDelta(panButtonWidth, eventRelativeX, m_maxPanPixels);
            m_dy = m_presenter.calculateDelta(panButtonHeight, eventRelativeY, m_maxPanPixels);
        }
    }

    private void startPanLoop() {
        m_panning = true;
        m_panButtonTimer.run();
        m_panButtonTimer.scheduleRepeating(m_millisBetweenConsecutiveActions);
    }

    private void stopPanLoop() {
        final Style clickHighlightStyle = clickHighlight.getStyle();
        clickHighlightStyle.setDisplay(Display.NONE);
        m_panning = false;
        m_panButtonTimer.cancel();
    }

    @UiHandler("zoomInButton")
    public void onZoomInButtonMouseDown(MouseDownEvent e) {
        e.preventDefault();
        zoomInButton.addStyleName("map-PanZoomControlZoomInButtonMouseDown");
        startZoomLoop(m_zoomInButtonTimer);
    }

    private void startZoomLoop(Timer zoomTimer) {
        zoomTimer.run();
        zoomTimer.scheduleRepeating(m_millisBetweenConsecutiveActions);
    }

    @UiHandler("zoomInButton")
    public void onZoomInButtonMouseUp(MouseUpEvent e) {
        e.preventDefault();
        zoomInButton.removeStyleName("map-PanZoomControlZoomInButtonMouseDown");
        stopZoomLoop(m_zoomInButtonTimer);
    }

    private void stopZoomLoop(Timer zoomTimer) {
        zoomTimer.cancel();
    }

    @UiHandler("zoomInButton")
    public void onZoomInButtonMouseOut(MouseOutEvent e) {
        e.preventDefault();
        zoomInButton.removeStyleName("map-PanZoomControlZoomInButtonMouseDown");
        stopZoomLoop(m_zoomInButtonTimer);
    }

    @UiHandler("zoomOutButton")
    public void onZoomOutButtonMouseDown(MouseDownEvent e) {
        e.preventDefault();
        zoomOutButton.addStyleName("map-PanZoomControlZoomOutButtonMouseDown");
        startZoomLoop(m_zoomOutButtonTimer);
    }

    @UiHandler("zoomOutButton")
    public void onZoomOutButtonMouseUp(MouseUpEvent e) {
        e.preventDefault();
        zoomOutButton.removeStyleName("map-PanZoomControlZoomOutButtonMouseDown");
        stopZoomLoop(m_zoomOutButtonTimer);
    }

    @UiHandler("zoomOutButton")
    public void onZoomOutButtonMouseOut(MouseOutEvent e) {
        e.preventDefault();
        zoomOutButton.removeStyleName("map-PanZoomControlZoomOutButtonMouseDown");
        stopZoomLoop(m_zoomOutButtonTimer);
    }
}
