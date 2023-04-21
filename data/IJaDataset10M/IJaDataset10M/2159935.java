package br.ufmg.ubicomp.decs.client.ui.handler;

import br.ufmg.ubicomp.decs.client.DECSContext;
import br.ufmg.ubicomp.decs.client.eventservice.xml.EntityProperties;
import br.ufmg.ubicomp.decs.client.utils.ClientUIUtils;
import br.ufmg.ubicomp.decs.client.utils.MapUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;

public class ShowInfoClickHandler extends AbstractClickHandler implements MarkerClickHandler {

    private EntityProperties entity;

    public ShowInfoClickHandler(DECSContext context, EntityProperties entity) {
        super(context);
        this.entity = entity;
    }

    @Override
    public void onClick(ClickEvent event) {
    }

    @Override
    public void onClick(MarkerClickEvent event) {
        Marker m = event.getSender();
        LatLng latLon = m.getLatLng();
        MapUtils.showInfo(context, entity, latLon);
        context.setSelectedMarker(entity.getName());
    }
}
