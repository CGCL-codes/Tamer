package com.moesol.gwt.maps.client.controls;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.moesol.gwt.maps.client.MapView;

public class MapDimmerControl extends Composite {

    MapView m_mapView;

    private double m_opacity = 0.5;

    public MapDimmerControl() {
    }

    public MapDimmerControl(MapView mapView, boolean bHorizontal) {
        super();
        setMapView(mapView, bHorizontal);
    }

    public void setMapView(MapView mapView, boolean bHorizontal) {
        m_mapView = mapView;
        MapButton dimBtn = new MapButton();
        dimBtn.addStyleName("map-DimmerControlDimButton");
        MapButton brightBtn = new MapButton();
        brightBtn.addStyleName("map-DimmerControlBrightenButton");
        dimBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dimMap();
            }
        });
        brightBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                brightenMap();
            }
        });
        if (bHorizontal) {
            HorizontalPanel p = new HorizontalPanel();
            p.add(dimBtn);
            p.add(brightBtn);
            initWidget(p);
        } else {
            VerticalPanel p = new VerticalPanel();
            p.add(dimBtn);
            p.add(brightBtn);
            initWidget(p);
        }
        addStyleName("map-DimmerControl");
        setZindex(100000);
    }

    private void dimMap() {
        m_mapView.incrementMapBrightness(-0.1);
    }

    private void brightenMap() {
        m_mapView.incrementMapBrightness(0.1);
    }

    public void setZindex(int zIndex) {
        this.getElement().getStyle().setZIndex(zIndex);
    }

    public void setOpacity(double val) {
        m_opacity = Math.min(1.0, Math.max(0.0, val));
    }

    public double getOpacity() {
        return m_opacity;
    }
}
