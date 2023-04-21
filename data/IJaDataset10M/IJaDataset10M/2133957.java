package br.ufmg.ubicomp.decs.client.ui.handler;

import java.util.HashMap;
import java.util.Map;
import br.ufmg.ubicomp.decs.client.DECSContext;
import br.ufmg.ubicomp.decs.client.eventservice.xml.EntityProperties;
import br.ufmg.ubicomp.decs.client.service.EventService;
import br.ufmg.ubicomp.decs.client.ui.callback.EventConsumedCallback;
import br.ufmg.ubicomp.decs.client.utils.ClientUIUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.event.InfoWindowCloseClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.HTML;

public class EventClickHandler extends AbstractClickHandler implements com.google.gwt.maps.client.event.MarkerClickHandler {

    private EntityProperties entity;

    public EventClickHandler(DECSContext context, EntityProperties entity) {
        super(context);
        this.entity = entity;
    }

    @Override
    public void onClick(MarkerClickEvent event) {
        InfoWindow info = context.getInfoWindow();
        String name = entity.getName();
        LatLng latLon = getLatLong(entity);
        final String reference = entity.getValue("reference");
        HTML htmlWidget = new HTML();
        StringBuffer sb = new StringBuffer();
        sb.append("<h2>" + name + "</h2>");
        InfoWindowContent content = new InfoWindowContent(htmlWidget);
        content.setMaxWidth(150);
        final Marker m = event.getSender();
        info.addInfoWindowCloseClickHandler(new InfoWindowCloseClickHandler() {

            @Override
            public void onCloseClick(InfoWindowCloseClickEvent event) {
                context.removeOverlay(m);
                Map<String, String> params = new HashMap<String, String>();
                params.put(EventService.PARAM_USER_NAME, reference);
                params.put(EventService.PARAM_EMERGENCY, entity.getName());
                context.updateBytesSent(reference, entity.getName());
                context.getProxy().getEventService().eventConsumed(reference, entity.getName(), new EventConsumedCallback(context, params));
            }
        });
        info.open(latLon, content);
    }

    @Override
    public void onClick(ClickEvent event) {
    }
}
